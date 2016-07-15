package au.com.jcloud.jcframe.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HttpUtil
{
    private static final Logger LOG = Logger.getLogger(HttpUtil.class);

    public static PathParts getPathParts(String pathInfo) {
        LOG.debug("pathInfo="+pathInfo);
        if (pathInfo!=null) {
        	if (pathInfo.startsWith("/")) {
        		pathInfo = pathInfo.substring(1);
        	}
        	return new PathParts(pathInfo.split("/"));
        }
        return new PathParts();
    }


    public static String getContextUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String contextUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
        LOG.debug("contextUrl="+contextUrl);
        return contextUrl;
    }
    
    public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl = getContextUrl(request);
        baseUrl = baseUrl + request.getServletPath();
        LOG.debug("baseUrl="+baseUrl);
        return baseUrl;
    }

    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String redirectParam) throws IOException {
        String redirectPath = HttpUtil.getBaseUrl(request);
        if (redirectParam!=null) {
            String redirect = (String) request.getParameter(redirectParam);
            if (StringUtils.isNotEmpty(redirect)) {
                redirectPath += redirect;
            }
        }
        LOG.debug("redirectPath=" + redirectPath);
        response.sendRedirect(response.encodeRedirectURL(redirectPath));
    }
}
