package au.com.javacloud.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import au.com.javacloud.controller.BaseController;
import au.com.javacloud.controller.ServletAction;
import au.com.javacloud.util.HttpUtil;
import au.com.javacloud.util.PathParts;
import au.com.javacloud.util.Statics;

/**
 * Created by david on 7/06/16.
 */
@WebServlet(urlPatterns = {"/jc/*"})
public class FrontControllerServlet extends HttpServlet {
	private static final long serialVersionUID = -9034690294608764448L;
	private final static Logger LOG = Logger.getLogger(FrontControllerServlet.class);
    public static final String JSON_SUFFIX = ".json";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doAction(ServletAction.GET,request,response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doAction(ServletAction.POST,request,response);
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doAction(ServletAction.DELETE,request,response);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doAction(ServletAction.PUT,request,response);
    }
    
    @Override
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doAction(ServletAction.TRACE,request,response);
    }
    
    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doAction(ServletAction.HEAD,request,response);
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doAction(ServletAction.OPTIONS,request,response);
    }
    
    protected void doAction(ServletAction action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PathParts pathParts = HttpUtil.getPathParts(request.getPathInfo());
        LOG.info("doAction() "+action+" pathParts="+pathParts);

        String beanName = pathParts.get(0);
        if (beanName != null && beanName.endsWith(JSON_SUFFIX)) {
            beanName = beanName.substring(0,beanName.length()-JSON_SUFFIX.length());
        }
        LOG.info("beanName="+beanName);
        BaseController baseController = Statics.getControllerForBeanName(beanName);
        LOG.info("baseController="+baseController);
        if (baseController!=null) {
        	if (!baseController.isInitialised()) {
        		baseController.initHttp(getServletContext(), getServletConfig());
        	}
        	baseController.doAction(action, pathParts, request,response);
        } else {
        	throw new ServletException("Controller not found for request with bean="+beanName);
        }
    }
}