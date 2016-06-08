package au.com.javacloud.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

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
}