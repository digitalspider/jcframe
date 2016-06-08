package au.com.javacloud.controller;

import java.io.IOException;
import java.text.DateFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.javacloud.auth.AuthService;
import au.com.javacloud.dao.BaseDAO;
import au.com.javacloud.model.BaseBean;

/**
 * Created by david on 22/05/16.
 */
public interface BaseController<T extends BaseBean, U> {

	public boolean isInitialised();
	
	public void init(ServletContext servletContext, ServletConfig servletConfig) throws ServletException;
	
	public void doAction(ServletAction action, String beanName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	public void list() throws Exception;
	
    public void create() throws Exception;
    
    public void read() throws Exception;
    
    public void update(String id) throws Exception;
    
    public void delete() throws Exception;

    public void find() throws Exception;

    public void config() throws Exception;

    public Class<T> getBeanClass();

    public String getBeanName();
    public void setBeanName(String name);

	public BaseDAO<T> getDao();
	public void setDao(BaseDAO<T> dao);

    public DateFormat getDateFormat();
    public void setDateFormat(DateFormat dateFormat);
    
    public AuthService<U> getAuthService();
    public void setAuthService(AuthService<U> auth);
    
    public String getConfigProperty(String key);
    public void setConfigProperty(String key, String value);

}
