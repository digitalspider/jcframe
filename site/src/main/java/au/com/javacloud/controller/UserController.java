package au.com.javacloud.controller;

import java.security.Principal;

/**
 * Created by david on 22/05/16.
 */

import javax.servlet.annotation.WebServlet;

import au.com.javacloud.model.User;

@WebServlet(urlPatterns = {"/user/*", "/user.json/*"})
public class UserController extends BaseControllerImpl<User,Principal> {

    public UserController() {
		super.init(User.class);
	}

}