package au.com.javacloud.jcframe.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.javacloud.jcframe.auth.AuthService;
import au.com.javacloud.jcframe.dao.BaseDAO;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.service.DAOLookup;
import au.com.javacloud.jcframe.util.PathParts;

/**
 * Created by david on 22/05/16.
 */
public interface BaseController<T extends BaseBean, U> {

    public static final String SUFFIX_BEANS = "s";
    public static final String SUFFIX_FIELDS = "fields";
    public static final String SUFFIX_TYPES = "types";
    public static final String SUFFIX_COUNT = "count";
    public static final String LOOKUPMAP = "lookupMap";
    public static final String CONTEXTURL = "contextUrl";
    public static final String BASEURL = "baseUrl";
    public static final String BEANURL = "beanUrl";
    public static final String EDITURL = "editUrl";
    public static final String SHOWURL = "showUrl";
    public static final String LISTURL = "listUrl";
    public static final String DELETEURL = "listUrl";
    public static final String DEFAULT_JSPPAGE_PREFIX = "/jsp/";
    public static final String DEFAULT_LIST_PAGE = "/list.jsp";
    public static final String DEFAULT_SHOW_PAGE = "/show.jsp";
    public static final String DEFAULT_EDIT_PAGE = "/edit.jsp";
    public static final String DEFAULT_INDEX_PAGE = "/index.jsp";
    public static final String JSON_SUFFIX_LOOKUP = ".jn";
    public static final String JSON_SUFFIX = ".json";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";
    public static final String APPLICATION_FORM = "application/x-www-form-urlencoded";

    public static final String PROP_ORDER = "order";
    public static final String PROP_ORDER_ASC = "ASC";
    public static final String PROP_ORDER_DESC = "DESC";
    public static final String PROP_LIMIT = "limit";
    public static final String PROP_SET = "set";

	public boolean isInitialised();
	
	public void init(Class<T> clazz);
	
	public void init(Class<T> clazz, AuthService<U> authService, DAOLookup daoLookupService);

	public void initHttp(ServletContext servletContext, ServletConfig servletConfig) throws ServletException;
	
	public void doAction(ServletAction action, PathParts pathParts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	public T populateBean(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	public void list() throws Exception;
	
    public void create() throws Exception;
    
    public void read() throws Exception;
    
    public void update(String id) throws Exception;
    
    public void delete() throws Exception;

    public void find() throws Exception;

    public void config() throws Exception;

    public void upload() throws Exception;

    public void initLookupMap();
    public void reloadLookupMap();

    public Class<T> getBeanClass();

    public String getBeanName();
    public void setBeanName(String name);

	public BaseDAO<T> getDao();
	public void setDao(BaseDAO<T> dao);

    public AuthService<U> getAuthService();
    public void setAuthService(AuthService<U> auth);
    
    public String getConfigProperty(String key);
    public void setConfigProperty(String key, String value);

    public Map<String, Map<Integer,BaseBean>> getLookupMap();
    public Map<String, Class<? extends BaseBean>> getLookupFields();

}
