package com.cts.accelerators.lifeplus.controllers;

import java.io.IOException;
import java.sql.Connection;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.feed.CustomAtomFeed;
import com.cts.accelerators.lifeplus.feed.CustomRssFeed;
import com.cts.accelerators.lifeplus.feed.Feed;
import com.cts.accelerators.lifeplus.services.NewAccountValidationService;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

@SlingServlet(paths = "/bin/SignupValidationServlet", methods = "POST", metatype = false)
public class SignupValidationServlet extends SlingAllMethodsServlet {

	@Reference
	ResourceResolverFactory resolverFactory;
	
	private static final long serialVersionUID = 5406752739629457297L;

	private final static Logger LOGGER = LoggerFactory
			.getLogger(CustomFeedRenderServlet.class);

	private final static String CLASS_NAME = SignupValidationServlet.class
			.getName();

	protected void doPost(SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) throws ServletException,
			IOException {
			try {
				JSONObject jsonResponse=identifyPageRequest(slingRequest, slingResponse);
				slingResponse.getWriter().write(jsonResponse.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	protected void doGet(SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) throws ServletException,
			IOException {
		doPost(slingRequest, slingResponse);
	}

	private JSONObject identifyPageRequest(
			SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) throws JSONException {

		String methodName = "identifyPageRequest";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject response = null;

		String requestType = slingRequest
				.getParameter(LifePlusGenericConstants.REQUEST_TYPE);
		if (StringUtils.isEmpty(requestType)) {
			response=validateUser(slingRequest, slingResponse);
		} else if (requestType.equalsIgnoreCase("userValidationStatus")) {
			response=checkUserValidationStatus(slingRequest, slingResponse);
		}
		LOGGER.info(" || " + methodName + " || requestType || " + requestType);

		return response;
	}

	private JSONObject validateUser(SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) {
		String methodName = "validateUser";		
		JSONObject jsonResponse = new JSONObject();
		try {
			Session jcrSession = ConnectionManager.getSession();
			String email = slingRequest.getParameter("email");
			String validationNumber = slingRequest
					.getParameter("validationNumber");
			UserManager userManager = slingRequest.getResourceResolver()
					.adaptTo(UserManager.class);
			Authorizable auth = userManager.getAuthorizable(email);
			Value[] validationNumberValue = auth
					.getProperty("./profile/emailValidationNumber");
			
			for (Value value : validationNumberValue) {
				String validationNumberFromNode = value.getString();
				if (validationNumberFromNode.equals(validationNumber)) {
					Value userValidated = jcrSession.getValueFactory()
							.createValue(true);
					auth.setProperty("./profile/userValidated", userValidated);
					jsonResponse
							.put("key",
									"User Validated Successfully. Login with your credentials");
				}
			}
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || AcceleratorException EXCEPTION OCCURED || ",e);
		} catch (RepositoryException e) {
			LOGGER.error(" || " + methodName + " || RepositoryException EXCEPTION OCCURED || ",e);
		} catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || JSONException EXCEPTION OCCURED || ",e);
		} 
		return jsonResponse;
		
	}
	private JSONObject checkUserValidationStatus(
			SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) {
		String methodName = "checkUserValidationStatus";		
		JSONObject jsonResponse = new JSONObject();
		boolean validationStatus=false;
		try {
			Session jcrSession = ConnectionManager.getSession();
			String email = slingRequest.getParameter("email");
			UserManager userManager = slingRequest.getResourceResolver()
					.adaptTo(UserManager.class);
			Authorizable auth = userManager.getAuthorizable(email);
			Value[] validationStatusValue = auth
					.getProperty("./profile/userValidated");
			
			for (Value value : validationStatusValue) {
				validationStatus=value.getBoolean();
			}
			jsonResponse.put("key",validationStatus);
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || AcceleratorException EXCEPTION OCCURED || ",e);
		}
		catch (RepositoryException e) {
			LOGGER.error(" || " + methodName + " || RepositoryException EXCEPTION OCCURED || ",e);
		}
		catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || JSONException EXCEPTION OCCURED || ",e);
		}
		return jsonResponse;
	}

}
