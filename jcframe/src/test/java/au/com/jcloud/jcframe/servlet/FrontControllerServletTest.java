package au.com.jcloud.jcframe.servlet;

import static org.junit.Assert.assertNotNull;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import au.com.jcloud.jcframe.controller.ServletAction;

public class FrontControllerServletTest {

	private FrontControllerServlet testClass;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;
	private RequestDispatcher errorRequestDispatcher;
	private RequestDispatcher loginRequestDispatcher;
	private RequestDispatcher indexRequestDispatcher;

	@Before
	public void setup() {
		testClass = new FrontControllerServlet();
		request = BDDMockito.mock(HttpServletRequest.class);
		response = BDDMockito.mock(HttpServletResponse.class);
		requestDispatcher = BDDMockito.mock(RequestDispatcher.class);
		errorRequestDispatcher = BDDMockito.mock(RequestDispatcher.class);
		loginRequestDispatcher = BDDMockito.mock(RequestDispatcher.class);
		indexRequestDispatcher = BDDMockito.mock(RequestDispatcher.class);
		BDDMockito.when(request.getRequestURL()).thenReturn(new StringBuffer("/java/cloud/test.jsp"));
		BDDMockito.when(request.getRequestURI()).thenReturn("/java/cloud/test.jsp");
		BDDMockito.when(request.getServletPath()).thenReturn("/java");
		BDDMockito.when(request.getContextPath()).thenReturn("");
		BDDMockito.when(request.getRequestDispatcher(FrontControllerServlet.URL_ERROR)).thenReturn(errorRequestDispatcher);
		BDDMockito.when(request.getRequestDispatcher(FrontControllerServlet.URL_LOGIN)).thenReturn(loginRequestDispatcher);
		BDDMockito.when(request.getRequestDispatcher(FrontControllerServlet.URL_INDEX)).thenReturn(indexRequestDispatcher);
		BDDMockito.when(request.getRequestDispatcher(BDDMockito.anyString())).thenReturn(requestDispatcher);
	}

	@Test
	public void testDoGet() throws Exception {
		assertNotNull(testClass);
		testClass.doGet(request, response);
	}

	@Test
	public void testDoAction() throws Exception {
		assertNotNull(testClass);
		testClass.doAction(ServletAction.GET, request, response);
	}

}
