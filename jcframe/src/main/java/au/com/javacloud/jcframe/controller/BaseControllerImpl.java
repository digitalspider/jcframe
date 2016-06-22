package au.com.javacloud.jcframe.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import au.com.javacloud.jcframe.annotation.ExcludeDBWrite;
import au.com.javacloud.jcframe.annotation.IndexPage;
import au.com.javacloud.jcframe.annotation.Secure;
import au.com.javacloud.jcframe.auth.ACLException;
import au.com.javacloud.jcframe.auth.Action;
import au.com.javacloud.jcframe.auth.AuthService;
import au.com.javacloud.jcframe.auth.AuthenticationException;
import au.com.javacloud.jcframe.dao.BaseDAO;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.service.DAOLookupService;
import au.com.javacloud.jcframe.util.GsonExclusionStrategy;
import au.com.javacloud.jcframe.util.HttpUtil;
import au.com.javacloud.jcframe.util.PathParts;
import au.com.javacloud.jcframe.util.ReflectUtil;
import au.com.javacloud.jcframe.util.Statics;

/**
 * Created by david on 22/05/16.
 */
public class BaseControllerImpl<T extends BaseBean, U> implements BaseController<T,U> {

	private final static Logger LOG = Logger.getLogger(BaseControllerImpl.class);

	protected BaseDAO<T> dao;
	protected Class<T> clazz;
    protected String beanName = "bean";
    protected Map<String,List<BaseBean>> lookupMap = new HashMap<String, List<BaseBean>>();
	protected Map<String,Class<? extends BaseBean>> lookupFields = new HashMap<String, Class<? extends BaseBean>>();
    protected String indexUrl = "/";
    protected String listUrl = "/";
	protected String showUrl = "/";
    protected String insertOrEditUrl = "/";
    protected String baseUrl;
	protected String beanUrl;
    protected String contextUrl;
    protected PathParts pathParts;
	protected AuthService<U> authService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Properties configProperties = new Properties();
	private Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
	private boolean isMultipart;
	private String filePath;
	private int maxFileSize = 50 * 1024;
	private int maxMemSize = 4 * 1024;
	private ServletConfig servletConfig;
	private ServletContext servletContext;
	private DAOLookupService daoLookupService;

	public String toString() {
		return getClass().getSimpleName()+"["+clazz.getSimpleName().toLowerCase()+"]";
	}

	@SuppressWarnings("unchecked")
    public void init(Class<T> clazz) {	
		init(clazz, Statics.getAuthService(), Statics.getDaoLookupService());
	}

	@SuppressWarnings("unchecked")
    public void init(Class<T> clazz, AuthService<U> authService, DAOLookupService daoLookupService) {
		this.clazz = clazz;
		this.authService = authService;
		this.daoLookupService = daoLookupService;
		dao = (BaseDAO<T>) Statics.getDaoMap().get(clazz);
		updateUrls(DEFAULT_JSPPAGE_PREFIX,clazz.getSimpleName().toLowerCase());
    }

	protected void updateUrls(String prefix, String contextName) {
		this.indexUrl = prefix+contextName+DEFAULT_INDEX_PAGE;
		this.listUrl = prefix+contextName+DEFAULT_LIST_PAGE;
		this.showUrl = prefix+contextName+DEFAULT_SHOW_PAGE;
		this.insertOrEditUrl = prefix+contextName+DEFAULT_EDIT_PAGE;
	}

    public boolean isInitialised() {
    	return servletContext!=null;
    }

	@Override
    public void initHttp(ServletContext servletContext, ServletConfig servletConfig) throws ServletException {
		this.servletContext = servletContext;
		this.servletConfig = servletConfig;
		dao.initHttp(servletConfig);
		filePath = servletContext.getInitParameter("file-upload");
		if (StringUtils.isBlank(filePath)) {
			filePath = System.getProperty("java.io.tmpdir");
		}
		initLookupMap();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initLookupMap() {
		// Configure lookupMap
    	Map<Method,Class> fieldMethods = ReflectUtil.getPublicSetterMethods(dao.getBeanClass(), ExcludeDBWrite.class);
    	for (Method method : fieldMethods.keySet()) {
    		Class lookupClass = fieldMethods.get(method);
			LOG.debug("lookupClass="+lookupClass.getName());
			if (ReflectUtil.isCollection(lookupClass)) {
				try {
					lookupClass = ReflectUtil.getCollectionGenericClass(clazz, ReflectUtil.getFieldName(method));
				} catch (Exception e) {
					LOG.error(e,e);
				}
			}
    		if (ReflectUtil.isBean(lookupClass)) {
    			try {
        			String fieldName = ReflectUtil.getFieldName(method);
					lookupFields.put(fieldName, lookupClass);
					lookupMap.put(fieldName,daoLookupService.getLookupMap(lookupClass));
					daoLookupService.registerController(lookupClass, this);
    			} catch (Exception e) {
					LOG.error(e,e);
    			}
    		}
    	}
		LOG.info("lookupMap="+lookupMap);
    }

	@Override
	public void reloadLookupMap() {
		for (String fieldName : lookupFields.keySet()) {
			Class<? extends BaseBean> lookupClass = lookupFields.get(fieldName);
			lookupMap.put(fieldName,daoLookupService.getLookupMap(lookupClass));
		}
	}

    public void doAction(ServletAction action, PathParts pathParts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("doAction() "+action+" START");
		this.request = request;
		this.response = response;

		LOG.info("isAuth=" + authService.isAuthenticated(request));
		Principal principal = request.getUserPrincipal();
		if (principal!=null) {
			String username = principal.getName();
			LOG.info("username=" + username);
		}

		contextUrl = HttpUtil.getContextUrl(request);
		LOG.info("contextUrl="+contextUrl);
        baseUrl = HttpUtil.getBaseUrl(request);
        LOG.info("baseUrl="+baseUrl);
        String pathInfo = request.getPathInfo();
		LOG.info("pathInfo="+pathInfo);
		String servletPath = request.getServletPath();
		LOG.info("servletPath="+servletPath);
		this.pathParts = pathParts;
		LOG.info("pathParts="+pathParts);
		beanUrl = baseUrl + "/"+pathParts.get(0);
		LOG.info("beanUrl="+beanUrl);

		String forward = null;
		request.setAttribute(beanName+SUFFIX_FIELDS, dao.getBeanFieldNames() );
		request.setAttribute(beanName+SUFFIX_TYPES, Statics.getClassTypeMap(request).keySet() );
		request.setAttribute(LOOKUPMAP, lookupMap );
		request.setAttribute(CONTEXTURL, contextUrl );
		request.setAttribute(BASEURL, baseUrl );
		request.setAttribute(BEANURL, beanUrl);
		request.setAttribute(EDITURL, beanUrl+"/edit" );
		request.setAttribute(SHOWURL, beanUrl+"/show" );
		request.setAttribute(LISTURL, beanUrl+"/list" );
		request.setAttribute(DELETEURL, beanUrl+"/delete" );
		
   		switch (action) {
    		case GET:
    			try {
					if (pathParts!=null && pathParts.size()>1) {
						if (pathParts.get(1).equalsIgnoreCase(Action.DELETE.name())) {
							LOG.info("action=delete");
							checkAuthAndAcl(request, Action.DELETE);
							delete();
						} else if (pathParts.get(1).equalsIgnoreCase(Action.EDIT.name())) {
							LOG.info("action=edit");
							checkAuthAndAcl(request, Action.EDIT);
							read();
							forward = insertOrEditUrl;
						} else if (pathParts.get(1).equalsIgnoreCase(Action.SHOW.name())) {
							LOG.info("action=show");
							checkAuthAndAcl(request, Action.SHOW);
							read();
							forward = showUrl;
						} else if (pathParts.get(1).equalsIgnoreCase(Action.LIST.name())) {
							LOG.info("action=list");
							checkAuthAndAcl(request, Action.LIST);
							list();
							forward = listUrl;
						} else if (StringUtils.isNumeric(pathParts.get(1))) {
							LOG.info("action=<int>");
							checkAuthAndAcl(request, Action.SHOW);
							read();
							forward = showUrl;
						} else if (pathParts.get(1).equalsIgnoreCase(Action.INSERT.name())) {
							LOG.info("action=insert");
							checkAuthAndAcl(request, Action.INSERT);
							forward = insertOrEditUrl;
						} else if (pathParts.get(1).equalsIgnoreCase(Action.FIND.name())) {
							LOG.info("action=find");
							checkAuthAndAcl(request, Action.FIND);
							find();
							forward = listUrl;
						} else if (pathParts.get(1).equalsIgnoreCase(Action.CONFIG.name())) {
							LOG.info("action=config");
							checkAuthAndAcl(request, Action.CONFIG);
							config();
						}
					}
			        if (forward==null) {
		        		LOG.info("action=list(default)");
						checkAuthAndAcl(request, Action.LIST);
						list();
						forward = listUrl;
						if (clazz.isAnnotationPresent(IndexPage.class)) {
							forward = indexUrl;
						}
			        }
				} catch (Exception e) {
					LOG.error(e,e);
					throw new ServletException(e);
				}
		
				if (beanUrl.endsWith(JSON_SUFFIX) || beanUrl.endsWith(JSON_SUFFIX_LOOKUP)) {
					return ;
				}
	   			break;

	   		// Handle POST
	   		case POST:
				isMultipart = ServletFileUpload.isMultipartContent(request);
				LOG.info("isMultipart="+isMultipart);
		
				try {
					String id = request.getParameter(BaseBean.FIELD_ID);
					if( id == null || id.isEmpty() ) {
						LOG.info("action=create");
						checkAuthAndAcl(request, Action.INSERT);
						create();
						if (isMultipart) {
							upload();
						}
					} else {
						LOG.info("action=update("+id+")");
						checkAuthAndAcl(request, Action.EDIT);
						update(id);
						if (isMultipart) {
							upload();
						}
					}
					list();
				} catch (Exception e) {
					LOG.error(e,e);
					throw new ServletException(e);
				}
				forward = listUrl;
				break;
			default:
				LOG.warn("Action "+action+" has not been implemented!");
	    }
        RequestDispatcher view = request.getRequestDispatcher( forward );
		LOG.info("doAction() "+action+" DONE. forward="+forward);
        view.forward(request, response);
	}
    
    @Override
	public void upload() throws Exception {
		LOG.info("upload() START");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax( maxFileSize );

		File file;

		// Parse the request to get file items.
		List<FileItem> fileItems = upload.parseRequest(request);

		for (FileItem fileItem : fileItems) {
			LOG.info("fileItem="+fileItem);
			if (!fileItem.isFormField()) {
				// Get the uploaded file parameters
				String fieldName = fileItem.getFieldName();
				LOG.info("fieldName="+fieldName);
				String fileName = fileItem.getName();
				LOG.info("fileName="+fileName);
				String contentType = fileItem.getContentType();
				LOG.info("contentType="+contentType);
				boolean isInMemory = fileItem.isInMemory();
				LOG.info("isInMemory="+isInMemory);
				long sizeInBytes = fileItem.getSize();
				LOG.info("sizeInBytes="+sizeInBytes);
				// Write the file
				LOG.info("filePath="+filePath);
				String fullFilePath;
				if (fileName.lastIndexOf("\\") >= 0) {
					fullFilePath = filePath + fileName.substring(fileName.lastIndexOf("\\"));
				} else {
					fullFilePath = filePath + fileName.substring(fileName.lastIndexOf("\\") + 1);
				}
				LOG.info("fullFilePath="+fullFilePath);
				file = new File(filePath);
				fileItem.write(file);
				LOG.info("Uploaded Filename: " + fileName + "<br>");
			}
		}
		LOG.info("upload() DONE");
	}

    @SuppressWarnings("rawtypes")
    @Override
	public T populateBean(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		T bean = ReflectUtil.getNewBean(clazz);
		Map<Method,Class> methods = ReflectUtil.getPublicSetterMethods(clazz, ExcludeDBWrite.class);
		for (Method method : methods.keySet()) {
			Class classType = methods.get(method);
			String fieldName = ReflectUtil.getFieldName(method);
			try {
				String value = request.getParameter(fieldName);
				LOG.debug("classType=" + classType.getSimpleName() +" method=" + method.getName() +  " value=" + value);

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
					ReflectUtil.invokeSetterMethodForPrimitive(bean, method, classType, value);
				}
			} catch (Exception e) {
				LOG.error(e,e);
			}
		}
		return bean;
	}

	protected boolean handleJson(Object o) throws IOException {
		if (beanUrl.endsWith(JSON_SUFFIX_LOOKUP)) {
			List<BaseBean> beans = daoLookupService.getLookupMap(clazz);
			String output = gson.toJson(beans);
			response.setContentType(APPLICATION_JSON);
			response.getWriter().write(output);
			return true;
		} else if (beanUrl.endsWith(JSON_SUFFIX)) {
			String output = gson.toJson(o);
			response.setContentType(APPLICATION_JSON);
			response.getWriter().write(output);
			return true;
		}
		return false;
	}

	private void checkAuthAndAcl(HttpServletRequest request, Action action) throws AuthenticationException, ACLException {
		checkAuth(request);
		checkACL(request, action);
	}

	private void checkACL(HttpServletRequest request, Action action) throws ACLException {
		authService.handleACL(authService.getUser(request), this.clazz, action);
	}

	private void checkAuth(HttpServletRequest request) throws AuthenticationException {
		if (clazz.isAnnotationPresent(Secure.class) && !authService.isAuthenticated(request)) {
			throw new AuthenticationException("Access Denied for class: " + clazz);
		}
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
		if (pathParts.isNumeric(1)) {
			idValue = pathParts.get(1);
		} else if (pathParts.isNumeric(2)) {
			idValue = pathParts.get(2);
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
		int pageNo = pathParts.getInt(2);
		List<T> beans = dao.getAll(pageNo);
		int count = dao.count();
		if (handleJson(beans)) {
			return;
		}
		request.setAttribute(beanName + SUFFIX_COUNT, count );
		request.setAttribute(beanName + SUFFIX_BEANS, beans );
	}

    @Override
    public void update(String id) throws Exception {
		T bean = populateBean(request, response);
		bean.setId( Integer.parseInt(id) );
		dao.saveOrUpdate(bean);
    }

    @Override
    public void delete()  throws Exception {
		int id = pathParts.getInt(2);
		if (id>0) {
			dao.delete(id);
		}
    }

	@Override
	public void find()  throws Exception {
		if (pathParts.size() > 3) {
			boolean exact = false;
			String field = pathParts.get(2);
			String value = pathParts.get(3);
			if (value!=null && value.startsWith("=")) {
				value = value.substring(1);
				exact = true;
			}
			int pageNo = pathParts.getInt(4);

			List<T> beans = dao.find(field, value, pageNo, exact);
			int count = dao.count(field, value);
			if (handleJson(beans)) {
				return;
			}
			request.setAttribute(beanName + SUFFIX_COUNT, count );
			request.setAttribute(beanName + SUFFIX_BEANS, beans);
		}
	}

	@Override
	public void config()  throws Exception {
		if (pathParts.size()>2) {
			if (pathParts.get(2).equals(PROP_ORDER)) {
				if (pathParts.size() > 3) {
					String field = pathParts.get(3);
					if (pathParts.size() > 4) {
						String direction = pathParts.get(4);
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
			} else if (pathParts.get(2).equals(PROP_LIMIT)) {
				int limit = pathParts.getInt(3);
				dao.setLimit(limit);
			} else if (pathParts.get(2).equals(PROP_SET)) {
				if (pathParts.size() > 4) {
					String key = pathParts.get(3);
					String value = pathParts.get(4);
					if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
						configProperties.setProperty(key, value);
					}
				} else if (pathParts.size() > 3) {
					String key = pathParts.get(3);
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
	public AuthService<U> getAuthService() {
		return authService;
	}

	@Override
	public void setAuthService(AuthService<U> authService) {
		this.authService = authService;
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public Map<String, List<BaseBean>> getLookupMap() {
		return lookupMap;
	}

	@Override
	public Map<String, Class<? extends BaseBean>> getLookupFields() {
		return lookupFields;
	}
}
