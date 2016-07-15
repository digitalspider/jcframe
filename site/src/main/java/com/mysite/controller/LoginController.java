package com.mysite.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.jcloud.jcframe.controller.ServletAction;
import au.com.jcloud.jcframe.annotation.BeanClass;
import com.mysite.model.Login;

import au.com.jcloud.jcframe.controller.BaseControllerImpl;
import au.com.jcloud.jcframe.servlet.FrontControllerServlet;
import au.com.jcloud.jcframe.servlet.UserRoleFilter;
import au.com.jcloud.jcframe.util.HttpUtil;
import au.com.jcloud.jcframe.util.PathParts;

/**
 * Created by david on 10/06/16.
 */
@BeanClass(Login.class)
public class LoginController extends BaseControllerImpl<Integer,Login,Principal> {

    private final static Logger LOG = Logger.getLogger(LoginController.class);

    @Override
    public void doAction(au.com.javacloud.jcframe.controller.ServletAction action, PathParts pathParts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LOG.info("In custom controller!");
            if (action.equals(ServletAction.POST) && StringUtils.isNotBlank(pathParts.get(1)) && pathParts.get(1).equals(FrontControllerServlet.LOGIN)) {
                LOG.info("custom login");
                String username = request.getParameter(FrontControllerServlet.LOGIN_USERNAME);
                String password = request.getParameter(FrontControllerServlet.LOGIN_PASSWORD);
                if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
                    throw new Exception("Username and password are both required!");
                }
                LOG.info("username=" + username);
                List<Login> users = dao.find("username", username, 0, true, true);
                if (!users.isEmpty()) {
                    if (users.size() > 1) {
                        throw new Exception("Too many users with username=" + username);
                    }
                    Login user = users.get(0);
                    LOG.info("user=" + user);
                    if (user.getPassword() != null && user.getPassword().equals(password)) {
                        LOG.info("You are good!!!");
                        request.getSession().setAttribute(UserRoleFilter.SESSION_ATTRIBUTE_USER, username);
                        HttpUtil.sendRedirect(request, response, FrontControllerServlet.PARAM_REDIRECT);
                        return ;
                    } else {
                        throw new Exception("Username or Password is not valid");
                    }
                } else {
                    throw new Exception("Username or Password is not valid");
                }
            } else if (action.equals(ServletAction.GET) && StringUtils.isNotBlank(pathParts.get(1)) && pathParts.get(1).equals(FrontControllerServlet.LOGOUT)) {
                LOG.info("custom logout");
                request.getSession().removeAttribute(UserRoleFilter.SESSION_ATTRIBUTE_USER);
                request.getSession().invalidate();
                HttpUtil.sendRedirect(request, response, FrontControllerServlet.PARAM_REDIRECT);
                return ;
            } else {
                super.doAction(action, pathParts, request, response);
            }
        } catch(Exception e) {
            LOG.error(e);
            throw new ServletException(e);
        }
    }
}
