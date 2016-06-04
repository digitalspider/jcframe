package au.com.javacloud.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class HttpUtil
{
    private static final Logger LOG = Logger.getLogger(HttpUtil.class);

    public static PathParts getPathParts(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        LOG.debug("pathInfo="+pathInfo);
        if (pathInfo!=null) {
            return new PathParts(pathInfo.substring(1).split("/"));
        }
        return new PathParts(new String[0]);
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