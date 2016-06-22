package au.com.javacloud.jcframe.auth;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import au.com.javacloud.jcframe.auth.AuthService;
import au.com.javacloud.jcframe.auth.BaseAuthServiceImpl;

public class BaseAuthServiceImplTest {

	private AuthService testClass;
	
	@Before
	public void setup() {
		testClass = new BaseAuthServiceImpl();
	}
	
	@Test
	public void test() {
		assertNotNull(testClass);
	}
}
