package au.com.javacloud.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.javacloud.annotation.BeanClass;
import au.com.javacloud.model.User;
import au.com.javacloud.util.PathParts;

/**
 * Created by david on 10/06/16.
 */
public class UserController extends BaseControllerImpl<User,Principal> {

    private final static Logger LOG = Logger.getLogger(UserController.class);

    @BeanClass(bean = User.class)
    public UserController() {
    }

    @Override
    public void doAction(ServletAction action, PathParts pathParts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LOG.info("In custom controller!");
            if (action.equals(ServletAction.POST) && pathParts.get(1).equals("login")) {
                LOG.info("custom login");
                String username = request.getParameter("j_username");
                String password = request.getParameter("j_password");
                if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
                    throw new Exception("Username and password are both required!");
                }
                LOG.info("username="+username);
                List<User> users= dao.find("username", username, 0);
                if (!users.isEmpty()) {
                    if (users.size()>1) {
                        throw new Exception("Too many users with username="+username);
                    }
                    User user = users.get(0);
                    LOG.info("user="+user);
                    if (user.getPassword()!=null && user.getPassword().equals(password)) {
                        LOG.info("You are good!!!");
                    } else {
                        throw new Exception("Password is not valid");
                    }
                }
            } else {
                super.doAction(action, pathParts, request, response);
            }
        } catch(Exception e) {
            LOG.error(e);
            throw new ServletException(e);
        }
    }
}
