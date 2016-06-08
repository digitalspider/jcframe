package au.com.javacloud.controller;

import java.security.Principal;

import au.com.javacloud.annotation.BeanClass;
import au.com.javacloud.model.Page;

public class PageController extends BaseControllerImpl<Page,Principal> {

	@BeanClass(bean=Page.class)
    public PageController() {
	}

}