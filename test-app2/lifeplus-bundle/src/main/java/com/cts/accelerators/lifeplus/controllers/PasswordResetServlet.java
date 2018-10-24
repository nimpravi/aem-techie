package com.cts.accelerators.lifeplus.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.core.services.ConfigurationUtil;
import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.feed.CustomAtomFeed;
import com.cts.accelerators.lifeplus.feed.CustomRssFeed;
import com.cts.accelerators.lifeplus.feed.Feed;
import com.cts.accelerators.lifeplus.helpers.NewAccountValidationHelper;
import com.cts.accelerators.lifeplus.services.NewAccountValidationService;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MailService;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.wcm.foundation.forms.FormsHelper;

@SlingServlet(paths = "/bin/PasswordReset", methods = "{POST,GET}", metatype = true)
public class PasswordResetServlet extends SlingAllMethodsServlet {

	@Reference
	ResourceResolverFactory resolverFactory;

	@Reference
	NewAccountValidationService newAccountValidationService;
	
	@Property(label = "Mail Template Path", value = "/apps/solution-accelerators/components/mails/passwordreset.txt", description = "Mail Template Path")
	public static final String MAIL_TEMPLATE_PATH = "mailTemplatePath";
	
	@Property(label = "Sender's Email Address", value = "", description = "Email address to use for Sender")
	public static final String FROM_EMAIL_ADDR = "fromEmailAddr";
	
	@Reference
	private ConfigurationUtil configurationUtil;
	
	Session jcrSession = null;
	
	@Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
	private MailService mailService;
	
	@Reference
	private MessageGatewayService messageGatewayService;
	
	private MessageGateway messageGateway;

	ResourceResolver resourceResolver;
	
	private static final long serialVersionUID = 5406752739629457297L;

	private final static Logger LOGGER = LoggerFactory
			.getLogger(CustomFeedRenderServlet.class);

	private final static String CLASS_NAME = PasswordResetServlet.class
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
		if (StringUtils.isNotEmpty(requestType) && requestType.equalsIgnoreCase("forgotPassword")) {
			response=sendResetPasswordLink(slingRequest, slingResponse);
		}
		if (StringUtils.isNotEmpty(requestType) && requestType.equalsIgnoreCase("resetPassword")) {
			response=passwordReset(slingRequest, slingResponse);
		}
		LOGGER.info(" || " + methodName + " || requestType || " + requestType);
		return response;
	}
	private JSONObject sendResetPasswordLink(
			SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) {
		String methodName = "sendResetPasswordLink";		
		JSONObject jsonResponse = new JSONObject();
		try {
			Session jcrSession = ConnectionManager.getSession();
			String email = slingRequest.getParameter("email");
			UserManager userManager = slingRequest.getResourceResolver()
					.adaptTo(UserManager.class);
			Authorizable auth = userManager.getAuthorizable(email);
			UUID uniqueId=UUID.randomUUID();
			String verificationPassword=(String.valueOf(uniqueId)).replaceAll("-","");
			/*Value forgotPasswordVerificationUID = jcrSession.getValueFactory()
					.createValue(verificationPassword);
					auth.setProperty("./profile/forgotPasswordVerificationUID", forgotPasswordVerificationUID);*/
					Node userNode=JcrUtils.getNodeIfExists(auth.getPath(), jcrSession);
					Node userProfileNode=JcrUtils.getNodeIfExists(userNode, "profile");
					if(null!=userProfileNode){
						userProfileNode.setProperty("forgotPasswordVerificationUID", verificationPassword);
						jcrSession.save();
					}
					NewAccountValidationServiceRequest newAccountValidationServiceRequest=new NewAccountValidationServiceRequest();
					setConfigProperties(newAccountValidationServiceRequest);
					newAccountValidationServiceRequest.setEmail(email);
					String protocol=slingRequest.getProtocol();
					newAccountValidationServiceRequest.setInitialURL((protocol.contains(AcceleratorGenericConstants.FORWARD_SLASH)?protocol.substring(0,protocol.indexOf(AcceleratorGenericConstants.FORWARD_SLASH)).toLowerCase():protocol.toLowerCase())+"://"+slingRequest.getServerName()+":"+slingRequest.getServerPort());
					jsonResponse=resetPassword(newAccountValidationServiceRequest,verificationPassword);
			
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || AcceleratorException EXCEPTION OCCURED || ",e);
		}
		catch (RepositoryException e) {
			LOGGER.error(" || " + methodName + " || RepositoryException EXCEPTION OCCURED || ",e);
		}
		return jsonResponse;
	}
	
	private JSONObject resetPassword(NewAccountValidationServiceRequest newAccountValidationServiceRequest,String verificationPassword){
		String methodName = "sendValidationEmail";
		LOGGER.info(" || " + methodName + " || START");
		
		HtmlEmail userEmail = new HtmlEmail();
		JSONObject jsonResponse=new JSONObject();
		try {
			resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
			jcrSession = resourceResolver.adaptTo(Session.class);
			String userToAddress = newAccountValidationServiceRequest.getEmail();
			List<InternetAddress> lifeplusEmailReceipent = new ArrayList<InternetAddress>();
			lifeplusEmailReceipent
			.add(new InternetAddress(userToAddress.trim()));
			
			LOGGER.debug("mailTemplatePath " + newAccountValidationServiceRequest.getMailTemplatePath());
			Resource templateRsrc = resourceResolver.getResource(newAccountValidationServiceRequest.getMailTemplatePath()) ;
			LOGGER.debug("templateRsrc " + templateRsrc);
			final MailTemplate mailTemplate = MailTemplate.create(templateRsrc.getPath(),
	                    templateRsrc.getResourceResolver().adaptTo(Session.class));
			jsonResponse=NewAccountValidationHelper.sendResetPasswordLink(newAccountValidationServiceRequest,verificationPassword, userEmail, lifeplusEmailReceipent, messageGatewayService, messageGateway,mailTemplate,mailService);
		} catch (LoginException e) {
			LOGGER.error(" || " + methodName + " || LoginException EXCEPTION OCCURED || ",e);
		}
		catch (AddressException e) {
			LOGGER.error(" || " + methodName + " || AddressException EXCEPTION OCCURED || ",e);
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}
	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param newAccountValidationServiceRequest
	 * @return Content reader request variable containing configured values
	 * @throws AcceleratorException
	 */
	private void setConfigProperties(
			NewAccountValidationServiceRequest newAccountValidationServiceRequest)
			throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START");
		String PID = "com.cts.accelerators.lifeplus.controllers.PasswordResetServlet";
		if(StringUtils.isNotEmpty(configurationUtil.getConfig(PID, FROM_EMAIL_ADDR))){
			newAccountValidationServiceRequest.setFromEmail(configurationUtil.getConfig(PID, FROM_EMAIL_ADDR));
		}
		if(StringUtils.isNotEmpty(configurationUtil.getConfig(PID, MAIL_TEMPLATE_PATH))){
			newAccountValidationServiceRequest.setMailTemplatePath(configurationUtil.getConfig(PID, MAIL_TEMPLATE_PATH));
		}
		
		LOGGER.info(" || " + methodName + " || END");
	}
	
	private JSONObject passwordReset(SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse){
		  String methodName = "passwordReset";
		  String key = slingRequest.getRequestParameter("ky").getString();
		  RequestParameter uid = slingRequest.getRequestParameter("uid");
		  RequestParameter pwd = slingRequest.getRequestParameter("passwordreset");
		  JSONObject jsonResponse=new JSONObject();
		  try {
		  Session jcrSession = ConnectionManager.getSession();
		  JackrabbitSession session = (JackrabbitSession) jcrSession;
		  RequestParameter currentPwd = slingRequest.getRequestParameter("currentPwd");
		 
		  boolean pwdChanged = false;
		  
			
			UserManager userManager = session.getUserManager();
	        String userId = (uid == null) ? resourceResolver.getUserID() : uid.toString();
	        User user = (User) userManager.getAuthorizable(userId);
	        Node userNode=session.getNode(user.getPath());
	        Node userProfileNode=JcrUtils.getNodeIfExists(userNode,"profile");
	        String forgotPasswordVerificationUID="";
	        if(null!=userProfileNode){
	        	forgotPasswordVerificationUID=userProfileNode.hasProperty("forgotPasswordVerificationUID")?userProfileNode.getProperty("forgotPasswordVerificationUID").getString():null;
	        	LOGGER.debug("No user matching uid {}", uid);
	        }
	        if (key!=null && user != null) {
	        	if(!forgotPasswordVerificationUID.equals(key))
	        	{
	        		LOGGER.error("Verification Key is Invalid");
	        		jsonResponse.put("key", "Verification Key is Invalid");

	        	}
	        	else if (currentPwd == null) {
	            	LOGGER.warn("Resetting password without checking the current password");
	            	user.changePassword(pwd.toString());
	            	jsonResponse.put("key", "Password has been reset without checking the current password");
	            } else {
	                user.changePassword(pwd.toString(), currentPwd.toString());
	                jsonResponse.put("key", "Password has been changed successfully");
	            }
	            if (!userManager.isAutoSave()) {
	                session.save();
	            }
	            pwdChanged = true;
	            LOGGER.info("Password changed for user [ {} ]", userId);
	        } else {
	        	LOGGER.error("Failed to locate user [ {} ]", userId);
	        }
	    
	    if (pwdChanged) {
	        String path = "/content/lifeplus/success";
	        if ("".equals(path)) {
	        	LOGGER.warn("Success page 'pwdChangedPage' is not defined. Redirecting to the referrer");
	            FormsHelper.redirectToReferrer(slingRequest, slingResponse, new HashMap<String, String[]>());
	        } else {
	            path = slingRequest.getResourceResolver().map(slingRequest, path);
	            if (path.indexOf(".") < 0) {
	                path += ".html";
	            }
	            slingResponse.sendRedirect(path);
	        }
	    } else {
	    	LOGGER.error("Failed to reset password. Redirecting to the referrer");
	    	jsonResponse.put("key", "Could not set Password");
	        // Currently there is no defined way to report server error.
	        // until 5.6, reset failure was not reported and request was redirected to the success page.
	        // adding a simple error parameter. This should probably be a part of FormsHelper
	        final HashMap<String, String[]> params = new HashMap<String, String[]>();
	        params.put("error", new String[]{"Could not set Password"});
	        FormsHelper.redirectToReferrer(slingRequest, slingResponse, params);
	    }
		  } catch (RepositoryException e) {
			  LOGGER.error(" || " + methodName + " || EXCEPTION || "
						,e);
		    }
			  catch (AcceleratorException e) {
				  LOGGER.error(" || " + methodName + " || EXCEPTION || "
							,e);
				}
		  catch (IOException e) {
			  LOGGER.error(" || " + methodName + " || EXCEPTION || "
						,e);
			}
		  catch(JSONException e){
			  LOGGER.error(" || " + methodName + " || EXCEPTION || "
						,e);
		  }
		return jsonResponse;
	}

	
}
