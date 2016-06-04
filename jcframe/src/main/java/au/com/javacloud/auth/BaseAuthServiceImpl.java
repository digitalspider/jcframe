package au.com.javacloud.auth;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import au.com.javacloud.model.BaseBean;

public class BaseAuthServiceImpl implements AuthService<Principal> {

	@Override
	public boolean isAuthenticated(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	public <T extends BaseBean> boolean checkACL(Principal user, Class<T> classType, Action action) {
		return true; // Everything is allowed
	}

	@Override
	public Principal getUser(HttpServletRequest request) {
		return request.getUserPrincipal();
	}

}
