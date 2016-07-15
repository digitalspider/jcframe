package au.com.jcloud.jcframe.servlet;

/**
 * Created by david on 13/06/16.
 */

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = {"/jc/*"})
public class UserRoleFilter implements Filter {

    public static String SESSION_ATTRIBUTE_USER = "jcuser";
    public static String SESSION_ATTRIBUTE_ROLES = "jcroles";

    public void init(FilterConfig cfg) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain next) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String user = (String)request.getSession().getAttribute(SESSION_ATTRIBUTE_USER);

        @SuppressWarnings("unchecked")
		List<String> roles = (List<String>)request.getSession().getAttribute(SESSION_ATTRIBUTE_ROLES);

        next.doFilter(new UserRoleRequestWrapper(user, roles, request), response);
    }

    public void destroy() {
    }
}
