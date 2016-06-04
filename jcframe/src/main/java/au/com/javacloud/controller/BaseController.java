package au.com.javacloud.controller;

import java.text.DateFormat;

import au.com.javacloud.auth.AuthService;
import au.com.javacloud.dao.BaseDAO;
import au.com.javacloud.model.BaseBean;

/**
 * Created by david on 22/05/16.
 */
public interface BaseController<T extends BaseBean, U> {

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
