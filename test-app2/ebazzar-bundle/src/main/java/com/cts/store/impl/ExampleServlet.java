package com.cts.store.impl;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;



@SlingServlet(paths = { "/services/ebazaarresp" }, methods = { "POST" })
public class ExampleServlet extends SlingAllMethodsServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {

		String redirect = request.getParameter("redirect");
		String quantity = request.getParameter("quantity");
		response.sendRedirect(redirect+"&quantity="+quantity);
		

	}
	
	
	
}