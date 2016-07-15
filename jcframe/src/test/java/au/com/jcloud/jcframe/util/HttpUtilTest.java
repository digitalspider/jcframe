package au.com.jcloud.jcframe.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

public class HttpUtilTest {

	private HttpUtil testClass;

	@Before
	public void setup() {
		testClass = new HttpUtil();
	}

	@Test
	public void testGetPathParts() {
		String pathInfo = "";
		PathParts results = HttpUtil.getPathParts(pathInfo);
	}

	@Test
	public void testGetContextUrl() {
		HttpServletRequest request = BDDMockito.mock(HttpServletRequest.class);
		BDDMockito.when(request.getRequestURL()).thenReturn(new StringBuffer("/java/cloud/test.jsp"));
		BDDMockito.when(request.getRequestURI()).thenReturn("/java/cloud/test.jsp");
		BDDMockito.when(request.getServletPath()).thenReturn("/java");
		BDDMockito.when(request.getContextPath()).thenReturn("");
		HttpUtil.getContextUrl(request);
	}

	@Test
	public void testGetBaseUrl() {
		HttpServletRequest request = BDDMockito.mock(HttpServletRequest.class);
		BDDMockito.when(request.getRequestURL()).thenReturn(new StringBuffer("/java/cloud/test.jsp"));
		BDDMockito.when(request.getRequestURI()).thenReturn("/java/cloud/test.jsp");
		BDDMockito.when(request.getServletPath()).thenReturn("/java");
		BDDMockito.when(request.getContextPath()).thenReturn("");
		HttpUtil.getBaseUrl(request);
	}

	@Test
	public void testSendRedirect() throws Exception {
		String redirectParam = "redirect";
		HttpServletRequest request = BDDMockito.mock(HttpServletRequest.class);
		BDDMockito.when(request.getRequestURL()).thenReturn(new StringBuffer("/java/cloud/test.jsp"));
		BDDMockito.when(request.getRequestURI()).thenReturn("/java/cloud/test.jsp");
		BDDMockito.when(request.getServletPath()).thenReturn("/java");
		BDDMockito.when(request.getContextPath()).thenReturn("");
		HttpServletResponse response = BDDMockito.mock(HttpServletResponse.class);
		HttpUtil.sendRedirect(request, response, redirectParam);
	}
}
