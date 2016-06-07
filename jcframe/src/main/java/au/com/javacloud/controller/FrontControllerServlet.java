package au.com.javacloud.controller;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.javacloud.util.HttpUtil;
import au.com.javacloud.util.PathParts;
import au.com.javacloud.util.Statics;

/**
 * Created by david on 7/06/16.
 */
@WebServlet(urlPatterns = {"*.jc"})
public class FrontControllerServlet extends HttpServlet {
    private final static Logger LOG = Logger.getLogger(FrontControllerServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextUrl = HttpUtil.getContextUrl(request);
        LOG.debug("contextUrl="+contextUrl);
        String baseUrl = HttpUtil.getBaseUrl(request);
        LOG.info("baseUrl="+baseUrl);
        PathParts pathParts = HttpUtil.getPathParts(request);
        LOG.info("pathParts="+pathParts);

        String beanName = pathParts.getFirst();
        Class classType = Statics.getUrlClassMap().get(beanName);
        BaseController baseController = Statics.getControllerMap().get(classType);
        baseController.doGet(request,response);
    }
}
