package au.com.javacloud.jcframe.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import au.com.javacloud.jcframe.controller.BaseController;
import au.com.javacloud.jcframe.controller.ServletAction;
import au.com.javacloud.jcframe.model.BaseBean;
import au.com.javacloud.jcframe.util.HttpUtil;
import au.com.javacloud.jcframe.util.PathParts;
import au.com.javacloud.jcframe.util.Statics;

/**
 * Created by david on 7/06/16.
 */
@WebServlet(urlPatterns = {"/jc/*"})
public class FrontControllerServlet extends HttpServlet {
	private static final long serialVersionUID = -9034690294608764448L;
	private final static Logger LOG = Logger.getLogger(FrontControllerServlet.class);
    public static final String URL_INDEX = "/index.jsp";
    public static final String URL_LOGIN = "/login.jsp";
    public static final String URL_ERROR = "/error.jsp";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String LOGIN_USERNAME = "j_username";
    public static final String LOGIN_PASSWORD = "j_password";
    public static final String PARAM_REDIRECT = "redirect";

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
        request.setAttribute("contextUrl", HttpUtil.getContextUrl(request));
        request.setAttribute("baseUrl", HttpUtil.getBaseUrl(request));
        request.setAttribute("beantypes", Statics.getClassTypeMap(request).keySet() );
        try {
            PathParts pathParts = HttpUtil.getPathParts(request.getPathInfo());
            LOG.info("doAction() " + action + " pathParts=" + pathParts);
            if (pathParts.isEmpty()) {
                RequestDispatcher view = request.getRequestDispatcher(URL_INDEX);
                view.forward( request, response );
                return;
            }

            String beanName = pathParts.get(0);
            if (beanName != null && beanName.endsWith(BaseController.JSON_SUFFIX_LOOKUP)) {
                beanName = beanName.substring(0, beanName.length() - BaseController.JSON_SUFFIX_LOOKUP.length());
            }
            if (beanName != null && beanName.endsWith(BaseController.JSON_SUFFIX)) {
                beanName = beanName.substring(0, beanName.length() - BaseController.JSON_SUFFIX.length());
            }
            LOG.info("beanName=" + beanName);
            BaseController<? extends BaseBean,?> controller = Statics.getControllerForBeanName(beanName,request);
            LOG.info("controller=" + controller);
            if (controller != null) {
                if (!controller.isInitialised()) {
                    controller.initHttp(getServletContext(), getServletConfig());
                }
                controller.doAction(action, pathParts, request, response);
            } else {
                Class<? extends BaseBean> classType = Statics.getSecureClassTypeMap().get(beanName);
                if (classType!=null) {
                    LOG.error("Login required for bean=" + beanName);
                    RequestDispatcher view = request.getRequestDispatcher(URL_LOGIN);
                    view.forward(request, response);
                    return;
                }
                if (action.equals(ServletAction.POST) && StringUtils.isNotBlank(beanName) && beanName.equals(LOGIN)) {
                    LOG.info("processing login");
                    String username = request.getParameter(LOGIN_USERNAME);
                    String password = request.getParameter(LOGIN_PASSWORD);
                    if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
                        throw new Exception("Username and password are both required!");
                    }
                    LOG.info("username=" + username);
                    request.login(username, password);
                    String user = request.getUserPrincipal().getName();
                    LOG.info("login successful for user " + user);
                    HttpUtil.sendRedirect(request, response, PARAM_REDIRECT);
                    return ;
                } else if (action.equals(ServletAction.GET) && StringUtils.isNotBlank(beanName) && beanName.equals(LOGOUT)) {
                    LOG.info("processing logout");
                    if (request.getUserPrincipal()!=null) {
                        String user = request.getUserPrincipal().getName();
                        request.logout();
                        LOG.info("login successful for user " + user);
                    }
                    HttpUtil.sendRedirect(request, response, PARAM_REDIRECT);
                    return ;
                }
                LOG.error("Controller not found for request with bean=" + beanName);
                HttpUtil.sendRedirect(request, response, null);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("e", e );
            RequestDispatcher view = request.getRequestDispatcher(URL_ERROR);
            view.forward( request, response );
        }
    }
}
