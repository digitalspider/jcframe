package au.com.javacloud.controller;

import java.security.Principal;

import javax.servlet.annotation.WebServlet;

import au.com.javacloud.model.Page;

@WebServlet(urlPatterns = {"/page/*", "/page.json/*"})
public class PageController extends BaseControllerImpl<Page,Principal> {

    public PageController() {
		super(Page.class);
	}

}