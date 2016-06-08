package au.com.javacloud.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import au.com.javacloud.model.BaseBean;
import au.com.javacloud.util.HttpUtil;
import au.com.javacloud.util.PathParts;
import au.com.javacloud.util.Statics;

/**
 * Created by david on 7/06/16.
 */
@WebServlet(urlPatterns = {"*.jc"})
public class FrontControllerServlet extends HttpServlet {
	private static final long serialVersionUID = -9034690294608764448L;
	private final static Logger LOG = Logger.getLogger(FrontControllerServlet.class);
	private String serlvetSuffix = ".jc";
	
	public FrontControllerServlet(String serlvetSuffix) {
		this.serlvetSuffix = serlvetSuffix;
	}
	
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

        String beanName = pathParts.getFirst();
        LOG.info("beanName="+beanName);
        BaseController baseController = Statics.getControllerForBeanName(beanName);
        LOG.info("baseController="+baseController);
        if (baseController!=null) {
        	if (!baseController.isInitialised()) {
        		baseController.init(getServletContext(), getServletConfig(), serlvetSuffix);
        	}
        	baseController.doAction(action,beanName,request,response);
        } else {
        	throw new ServletException("Controller not found for request with bean="+beanName);
        }
    }

	public String getSerlvetSuffix() {
		return serlvetSuffix;
	}
}
