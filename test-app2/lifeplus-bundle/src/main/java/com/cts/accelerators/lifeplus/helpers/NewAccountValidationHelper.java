package com.cts.accelerators.lifeplus.helpers;

import java.io.IOException;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceResponse;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MailService;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;


public class NewAccountValidationHelper {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NewAccountValidationHelper.class);
	
	public static NewAccountValidationServiceResponse sendValidationEmail(NewAccountValidationServiceRequest newAccountValidationServiceRequest,HtmlEmail userEmail,List<InternetAddress> lifeplusEmailReceipent,MessageGatewayService messageGatewayService, MessageGateway messageGateway,MailTemplate mailTemplate,MailService mailService){
		String methodName = "sendValidationEmail";
		StringBuilder activationLink=new StringBuilder(newAccountValidationServiceRequest.getInitialURL());
		java.util.Properties properties = new java.util.Properties();
		int validationNumber=(int) (Math.random()*111234);
		validationNumber+=newAccountValidationServiceRequest.getMember_id();
		Node userNode=newAccountValidationServiceRequest.getUserNode();
		NewAccountValidationServiceResponse newAccountValidationServiceResponse=new NewAccountValidationServiceResponse();
		JSONObject jsonResponse=new JSONObject();
		LOGGER.debug("||validationNumber: "+validationNumber);
		try {
			userNode.setProperty("emailValidationNumber", validationNumber);
			userNode.setProperty("userValidated", false);
			userNode.getSession().save();	
			activationLink.append(LifePlusGenericConstants.SIGNUP_VALIDATION_SERVLET);
			activationLink.append("?email="+newAccountValidationServiceRequest.getEmail());
			activationLink.append("&validationNumber="+validationNumber);
			properties.put("activationLink", activationLink.toString());
			LOGGER.debug("||activationLink: "+ activationLink.toString());
			userEmail = mailTemplate.getEmail(StrLookup.mapLookup(properties), HtmlEmail.class);
			userEmail.setFrom(newAccountValidationServiceRequest.getFromEmail());
			userEmail.setTo(lifeplusEmailReceipent);
			if(mailService!=null)
			{
				LOGGER.debug("Mail Service is Not Null");
				messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
				messageGateway.send(userEmail);
				LOGGER.debug("Sent Mail Successfully.");
				jsonResponse.put("key", LifePlusGenericConstants.SUCCESS);
				newAccountValidationServiceResponse.setJsonResponse(jsonResponse);
			}
			else
			{
				LOGGER.error("Mail Service is Null");
				messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
				messageGateway.send(userEmail);
				LOGGER.debug("Sent Mail Successfully.");
				jsonResponse.put("key", LifePlusGenericConstants.SUCCESS);
				newAccountValidationServiceResponse.setJsonResponse(jsonResponse);
			}
			
			
		} catch (ValueFormatException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (VersionException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (LockException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (ConstraintViolationException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (RepositoryException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		}
		catch (IOException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (MessagingException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (EmailException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		}
		catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		}
		return newAccountValidationServiceResponse;
	}
	
	public static JSONObject sendResetPasswordLink(NewAccountValidationServiceRequest newAccountValidationServiceRequest,String verificationPassword,HtmlEmail userEmail,List<InternetAddress> lifeplusEmailReceipent,MessageGatewayService messageGatewayService, MessageGateway messageGateway,MailTemplate mailTemplate,MailService mailService){
		String methodName = "sendResetPasswordLink";
		StringBuilder forgotPasswordLink=new StringBuilder(newAccountValidationServiceRequest.getInitialURL());
		java.util.Properties properties = new java.util.Properties();
		JSONObject jsonResponse=new JSONObject();
		LOGGER.debug("||validationNumber: "+verificationPassword);
		try {
			forgotPasswordLink.append(LifePlusGenericConstants.PASSWORD_RESET_PAGE_LINK);
			forgotPasswordLink.append("?uid="+newAccountValidationServiceRequest.getEmail());
			forgotPasswordLink.append("&ky="+verificationPassword);
			properties.put("resetPasswordLink", forgotPasswordLink.toString());
			LOGGER.debug("||resetPasswordLink: "+ forgotPasswordLink.toString());
			userEmail = mailTemplate.getEmail(StrLookup.mapLookup(properties), HtmlEmail.class);
			userEmail.setFrom(newAccountValidationServiceRequest.getFromEmail());
			userEmail.setTo(lifeplusEmailReceipent);
			if(mailService!=null)
			{
				LOGGER.debug("Mail Service is Not Null");
				messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
				messageGateway.send(userEmail);
				LOGGER.debug("Sent Mail Successfully.");
				jsonResponse.put("key", LifePlusGenericConstants.SUCCESS_PASSWORD_RESET_LINK);
			}
			else
			{
				LOGGER.error("Mail Service is Null");
				messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
				messageGateway.send(userEmail);
				LOGGER.debug("Sent Mail Successfully.");
				jsonResponse.put("key", LifePlusGenericConstants.SUCCESS_PASSWORD_RESET_LINK);
			}
			
			
		}
		catch (IOException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (MessagingException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		} catch (EmailException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		}
		catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					,e);
		}
		return jsonResponse;
	}
}
