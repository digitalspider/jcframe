package au.com.javacloud.controller;

import java.security.Principal;

import au.com.javacloud.annotation.BeanClass;
import au.com.javacloud.model.User;

/**
 * Created by david on 10/06/16.
 */
public class UserController extends BaseControllerImpl<User,Principal> {

    @BeanClass(bean = User.class)
    public UserController() {
    }
}
