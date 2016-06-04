package au.com.javacloud.auth;

import javax.servlet.http.HttpServletRequest;

import au.com.javacloud.model.BaseBean;

public interface AuthService<U> {
	
	public U getUser(HttpServletRequest request);
	
	public boolean isAuthenticated(HttpServletRequest request);
	
	public <T extends BaseBean> boolean checkACL(U user, Class<T> classType, Action action);
}
