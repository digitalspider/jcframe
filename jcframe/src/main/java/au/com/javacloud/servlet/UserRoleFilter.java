package au.com.javacloud.servlet;

/**
 * Created by david on 13/06/16.
 */
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
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

    public void init(FilterConfig cfg) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain next) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String user = (String)request.getSession().getAttribute("user");

        List<String> roles = (List<String>)request.getSession().getAttribute("roles");

        next.doFilter(new UserRoleRequestWrapper(user, roles, request), response);
    }

    public void destroy() {
    }
}