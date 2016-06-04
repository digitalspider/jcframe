package au.com.javacloud.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.auth.Action;
import au.com.javacloud.auth.AuthService;
import au.com.javacloud.dao.BaseDAO;
import au.com.javacloud.model.BaseBean;
import au.com.javacloud.util.GsonExclusionStrategy;
import au.com.javacloud.util.HttpUtil;
import au.com.javacloud.util.PathParts;
import au.com.javacloud.util.ReflectUtil;
import au.com.javacloud.util.Statics;

/**
 * Created by david on 22/05/16.
 */
public class BaseControllerImpl<T extends BaseBean, U> extends HttpServlet implements BaseController<T,U> {

	private final static Logger LOG = Logger.getLogger(BaseControllerImpl.class);

	private static final long serialVersionUID = -2841993759251817415L;
	protected BaseDAO<T> dao;
	protected Class<T> clazz;
    protected String beanName = "bean";
    protected Map<String,List<BaseBean>> lookupMap = new HashMap<String, List<BaseBean>>();
    protected String indexUrl = "/";
    protected String listUrl = "/";
	protected String showUrl = "/";
    protected String insertOrEditUrl = "/";
    protected String baseUrl;
    protected String contextUrl;
    protected PathParts pathParts;
	protected DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected AuthService<U> authService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Properties configProperties = new Properties();
	private Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();

	public static final String BEANS_SUFFIX = "s";
	public static final String BEANS_FIELDSUFFIX = "fields";
	public static final String LOOKUPMAP = "lookupMap";
	public static final String CONTEXTURL = "contextUrl";
	public static final String BASEURL = "baseUrl";
	public static final String BEANURL = "beanUrl";
	public static final String DEFAULT_JSPPAGE_PREFIX = "/jsp/";
	public static final String DEFAULT_LIST_PAGE = "/list.jsp";
	public static final String DEFAULT_SHOW_PAGE = "/show.jsp";
	public static final String DEFAULT_EDIT_PAGE = "/edit.jsp";
	public static final String DEFAULT_INDEX_PAGE = "/index.jsp";
	public static final String JSON_SUFFIX = ".json";
	
	public static final String PROP_USE_INDEX = "useindex";
	public static final String PROP_AUTH = "auth";
	public static final String PROP_ORDER = "order";
	public static final String PROP_ORDER_ASC = "ASC";
	public static final String PROP_ORDER_DESC = "DESC";
	public static final String PROP_LIMIT = "limit";
	public static final String PROP_SET = "set";

	@SuppressWarnings("unchecked")
	public BaseControllerImpl(Class<T> clazz) {
		this(clazz, Statics.getAuthService());
	}

	@SuppressWarnings("unchecked")
    public BaseControllerImpl(Class<T> clazz, AuthService<U> authService) {
		super();
		this.clazz = clazz;
		this.authService = authService;
		dao = Statics.getDaoMap().get(clazz);
		updateUrls(DEFAULT_JSPPAGE_PREFIX,clazz.getSimpleName().toLowerCase());
		configProperties.setProperty(PROP_USE_INDEX, "false");
		configProperties.setProperty(PROP_AUTH, "false");
    }

	protected void updateUrls(String prefix, String contextName) {
		this.indexUrl = prefix+contextName+DEFAULT_INDEX_PAGE;
		this.listUrl = prefix+contextName+DEFAULT_LIST_PAGE;
		this.showUrl = prefix+contextName+DEFAULT_SHOW_PAGE;
		this.insertOrEditUrl = prefix+contextName+DEFAULT_EDIT_PAGE;
	}

    public BaseControllerImpl(BaseDAO<T> dao, String beanName, String beansName, String listUrl, String showUrl, String insertOrEditUrl) {
		super();
        this.dao = dao;
        this.beanName = beanName;
        this.listUrl = listUrl;
		this.showUrl = showUrl;
        this.insertOrEditUrl = insertOrEditUrl;
	}

    @SuppressWarnings("rawtypes")
	@Override
    public void init() throws ServletException {
    	super.init();
		dao.init(getServletConfig());
    	Map<Method,Class> fieldMethods = ReflectUtil.getPublicSetterMethods(dao.getBeanClass());
    	for (Method method : fieldMethods.keySet()) {
    		Class lookupClass = fieldMethods.get(method);
			LOG.debug("lookupClass="+lookupClass.getName());
    		if (ReflectUtil.isBean(lookupClass)) {
    			try {
        			BaseDAO lookupDao = Statics.getDaoMap().get(lookupClass);
        			String fieldName = ReflectUtil.getFieldName(method);
					LOG.debug("fieldName="+fieldName+" lookupDao="+lookupDao);
        			lookupMap.put(fieldName,lookupDao.getLookup());
    			} catch (Exception e) {
					LOG.error(e,e);
    			}
    		}
    	}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doGet() START");
		this.request = request;
		this.response = response;

		contextUrl = HttpUtil.getContextUrl(request);
		LOG.debug("contextUrl="+contextUrl);
        baseUrl = HttpUtil.getBaseUrl(request);
        LOG.info("baseUrl="+baseUrl);
		pathParts = HttpUtil.getPathParts(request);
		LOG.info("pathParts="+pathParts);


		String forward = null;

    	try {
    		request.setAttribute(beanName+BEANS_FIELDSUFFIX, dao.getBeanFieldNames() );
    		request.setAttribute(LOOKUPMAP, lookupMap );
    		request.setAttribute(CONTEXTURL, contextUrl );
    		request.setAttribute(BASEURL, baseUrl );
    		request.setAttribute(BEANURL, contextUrl+"/"+clazz.getSimpleName().toLowerCase());

			if (pathParts!=null && !pathParts.isEmpty()) {
				if (pathParts.get(0).equalsIgnoreCase(Action.DELETE.name())) {
					LOG.info("action=delete");
					if (checkAuthAndAcl(request, Action.DELETE)) {
						delete();
					}
				} else if (pathParts.get(0).equalsIgnoreCase(Action.EDIT.name())) {
					LOG.info("action=edit");
					if (checkAuthAndAcl(request, Action.EDIT)) {
						read();
						forward = insertOrEditUrl;
					}
				} else if (pathParts.get(0).equalsIgnoreCase(Action.SHOW.name())) {
					LOG.info("action=show");
					if (checkAuthAndAcl(request, Action.SHOW)) {
						read();
						forward = showUrl;
					}
				} else if (pathParts.get(0).equalsIgnoreCase(Action.LIST.name())) {
					LOG.info("action=list");
					if (checkAuthAndAcl(request, Action.LIST)) {
						list();
						forward = listUrl;
					}
				} else if (StringUtils.isNumeric(pathParts.get(0))) {
					LOG.info("action=<int>");
					if (checkAuthAndAcl(request, Action.SHOW)) {
						read();
						forward = showUrl;
					}
				} else if (pathParts.get(0).equalsIgnoreCase(Action.INSERT.name())) {
					LOG.info("action=insert");
					if (checkAuthAndAcl(request, Action.INSERT)) {
						forward = insertOrEditUrl;
					}
				} else if (pathParts.get(0).equalsIgnoreCase(Action.FIND.name())) {
					LOG.info("action=find");
					if (checkAuthAndAcl(request, Action.FIND)) {
						find();
						forward = listUrl;
					}
				} else if (pathParts.get(0).equalsIgnoreCase(Action.CONFIG.name())) {
					LOG.info("action=config");
					if (checkAuthAndAcl(request, Action.CONFIG)) {
						config();
					}
				}
			}
	        if (forward==null) {
        		LOG.info("action=list(default)");
				if (checkAuthAndAcl(request, Action.LIST)) {
					list();
					forward = listUrl;
					if (configProperties.getProperty(PROP_USE_INDEX).equalsIgnoreCase("true")) {
						forward = indexUrl;
					}
				}
	        }
		} catch (Exception e) {
			LOG.error(e,e);
			throw new ServletException(e);
		}

		if (baseUrl.endsWith(JSON_SUFFIX)) {
			return ;
		}

        RequestDispatcher view = request.getRequestDispatcher( forward );
		LOG.info("doGet() DONE. forward="+forward);
        view.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doPost() START");
		this.request = request;
		this.response = response;

		pathParts = HttpUtil.getPathParts(request);
		LOG.info("pathParts="+pathParts);
		baseUrl = HttpUtil.getBaseUrl(request);
		LOG.info("baseUrl="+baseUrl);

		try {
			String id = request.getParameter(BaseBean.FIELD_ID);
			if( id == null || id.isEmpty() ) {
				LOG.info("action=create");
				if (authService.checkACL(authService.getUser(request), this.clazz, Action.INSERT)) {
					create();
				}
			} else {
				LOG.info("action=update("+id+")");
				if (authService.checkACL(authService.getUser(request), this.clazz, Action.EDIT)) {
					update(id);
				}
			}

			request.setAttribute(beanName+BEANS_SUFFIX, dao.getAll(0) );
			request.setAttribute(beanName+BEANS_FIELDSUFFIX, dao.getBeanFieldNames() );
			request.setAttribute(LOOKUPMAP, lookupMap );
    		request.setAttribute(CONTEXTURL, contextUrl );
    		request.setAttribute(BASEURL, baseUrl );
    		request.setAttribute(BEANURL, contextUrl+"/"+clazz.getSimpleName().toLowerCase());
		} catch (Exception e) {
			LOG.error(e,e);
			throw new ServletException(e);
		}
		RequestDispatcher view = request.getRequestDispatcher( listUrl );
		LOG.info("doPost() DONE. view="+view);
		view.forward(request, response);
	}

    @SuppressWarnings("rawtypes")
	protected T populateBean(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		T bean = ReflectUtil.getNewBean(clazz);
		Map<Method,Class> methods = ReflectUtil.getPublicSetterMethods(clazz);
		for (Method method : methods.keySet()) {
			Class classType = methods.get(method);
			String fieldName = ReflectUtil.getFieldName(method);
			try {
				String value = request.getParameter( fieldName );

				if (ReflectUtil.isBean(classType)) {
					// Handle BaseBeans
					if (StringUtils.isNumeric(value)) {
						int id = Integer.parseInt(value);
						ReflectUtil.invokeSetterMethodForBeanType(bean, method, classType, id);
					}
				} else if (ReflectUtil.isCollection(classType)) {
					// Handle Collections
					ReflectUtil.invokeSetterMethodForCollection(bean, method, classType, value);
				} else {
					// Handle primitives
					ReflectUtil.invokeSetterMethodForPrimitive(bean, method, classType, value, dateFormat);
				}
			} catch (Exception e) {
				LOG.error(e,e);
			}
		}
		return bean;
	}

	protected boolean handleJson(Object o) throws IOException {
		if (baseUrl.endsWith(JSON_SUFFIX)) {
			String output = gson.toJson(o);
			response.getWriter().write(output);
			return true;
		}
		return false;
	}

	private boolean checkAuthAndAcl(HttpServletRequest request, Action action) {
		return checkAuth(request) && checkACL(request, action);
	}

	private boolean checkACL(HttpServletRequest request, Action action) {
		return (authService.checkACL(authService.getUser(request), this.clazz, action));
	}

	private boolean checkAuth(HttpServletRequest request) {
		if (configProperties.get(PROP_AUTH).equals("true")) {
			return authService.isAuthenticated(request);
		}
		return true;
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

	protected HttpServletResponse getResponse() {
		return response;
	}
	
    public String getConfigProperty(String key) {
    	return configProperties.getProperty(key);
    }
    
    public void setConfigProperty(String key, String value) {
    	configProperties.setProperty(key, value);
    }

    @Override
    public void create() throws Exception {
		T bean = populateBean(request, response);
		dao.saveOrUpdate(bean);
    }

    @Override
    public void read() throws Exception {
		String idValue = null;
		if (pathParts.isNumeric(0)) {
			idValue = pathParts.get(0);
		} else if (pathParts.isNumeric(1)) {
			idValue = pathParts.get(1);
		}
		if (idValue!=null) {
			int id = Integer.parseInt(idValue);
			T bean = dao.get(id);
			if (handleJson(bean)) {
				return;
			}
			request.setAttribute(beanName, bean);
		}
    }

	@Override
	public void list() throws Exception {
		int pageNo = pathParts.getInt(1);
		List<T> beans = dao.getAll(pageNo);
		if (handleJson(beans)) {
			return;
		}
		request.setAttribute(beanName+BEANS_SUFFIX, beans );
	}

    @Override
    public void update(String id) throws Exception {
		T bean = populateBean(request, response);
		bean.setId( Integer.parseInt(id) );
		dao.saveOrUpdate(bean);
    }

    @Override
    public void delete()  throws Exception {
		int id = pathParts.getInt(1);
		if (id>0) {
			dao.delete(id);
		}
    }

	@Override
	public void find()  throws Exception {
		if (pathParts.size() > 2) {
			String field = pathParts.get(1);
			String value = pathParts.get(2);
			int pageNo = pathParts.getInt(3);
			List<T> beans = dao.find(field, value, pageNo);
			if (handleJson(beans)) {
				return;
			}
			request.setAttribute(beanName + BEANS_SUFFIX, beans);
		}
	}

	@Override
	public void config()  throws Exception {
		if (pathParts.size()>1) {
			if (pathParts.get(1).equals(PROP_ORDER)) {
				if (pathParts.size() > 2) {
					String field = pathParts.get(2);
					if (pathParts.size() > 3) {
						String direction = pathParts.get(3);
						if (direction.equalsIgnoreCase(PROP_ORDER_ASC)) {
							dao.setOrderBy(field,true);
						} else if (direction.equalsIgnoreCase(PROP_ORDER_DESC)) {
							dao.setOrderBy(field,false);
						}
					} else {
						dao.toggleOrderBy(field);
					}
				} else {
					dao.setOrderBy("",true);
				}
			} else if (pathParts.get(1).equals(PROP_LIMIT)) {
				int limit = pathParts.getInt(2);
				dao.setLimit(limit);
			} else if (pathParts.get(1).equals(PROP_SET)) {
				if (pathParts.size() > 3) {
					String key = pathParts.get(2);
					String value = pathParts.get(3);
					if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
						configProperties.setProperty(key, value);
					}
				} else if (pathParts.size() > 2) {
					String key = pathParts.get(2);
					if (StringUtils.isNotBlank(key)) {
						if (configProperties.contains(key)) {
							configProperties.setProperty(key, null);
						}
					}
				}
			}
		}
	}

	public BaseDAO<T> getDao() {
		return dao;
	}

	public void setDao(BaseDAO<T> dao) {
		this.dao = dao;
	}

	@Override
	public String getBeanName() {
		return beanName;
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public Class<T> getBeanClass() {
		return clazz;
	}

	@Override
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	@Override
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
		dao.setDateFormat(dateFormat);
	}

	@Override
	public AuthService<U> getAuthService() {
		return authService;
	}

	@Override
	public void setAuthService(AuthService<U> authService) {
		this.authService = authService;
	}

}
