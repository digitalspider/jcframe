package au.com.javacloud.jcframe.auth;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import au.com.javacloud.jcframe.auth.AuthService;
import au.com.javacloud.jcframe.auth.BaseAuthServiceImpl;
import au.com.javacloud.jcframe.model.BaseBeanTest;

public class BaseAuthServiceImplTest {

	private AuthService testClass;
	private HttpServletRequest request;
	
	@Before
	public void setup() {
		testClass = new BaseAuthServiceImpl();
		request = BDDMockito.mock(HttpServletRequest.class);
		assertNotNull(testClass);
	}
	
	@Test
	public void testAuthenticated() {
		assertFalse(testClass.isAuthenticated(request));
		Principal principal = BDDMockito.mock(Principal.class);
		BDDMockito.when(request.getUserPrincipal()).thenReturn(principal);
		assertTrue(testClass.isAuthenticated(request));
	}

	@Test
	public void testGetUser() {
		assertNull(testClass.getUser(request));
		Principal principal = BDDMockito.mock(Principal.class);
		BDDMockito.when(principal.getName()).thenReturn("testuser");
		BDDMockito.when(request.getUserPrincipal()).thenReturn(principal);
		assertEquals(principal, testClass.getUser(request));
		assertEquals(principal.getName(), ((Principal)testClass.getUser(request)).getName());
	}

	@Test
	public void testCheckACL() {
		Principal principal = BDDMockito.mock(Principal.class);
		BDDMockito.when(principal.getName()).thenReturn("testuser");
		Class classType = BaseBeanTest.class;
		assertTrue(testClass.checkACL(principal, classType, Action.INSERT));
	}
}
