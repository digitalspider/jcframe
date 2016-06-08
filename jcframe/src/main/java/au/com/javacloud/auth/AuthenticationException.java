package au.com.javacloud.auth;

public class AuthenticationException extends Exception {
	private static final long serialVersionUID = -3408803447928991605L;
	
	public AuthenticationException(String msg) {
		super(msg);
	}
}
