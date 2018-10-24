package com.cts.accelerators.lifeplus.services;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
import com.cts.accelerators.core.services.AcceleratorService;
import com.cts.accelerators.core.services.ConfigurationUtil;
import com.cts.accelerators.lifeplus.helpers.NewAccountValidationHelper;
import com.cts.accelerators.lifeplus.helpers.WeatherWidgetHelper;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.NewAccountValidationServiceResponse;
import com.cts.accelerators.lifeplus.services.dto.WeatherWidgetServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.WeatherWidgetServiceResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.services.ContentImporterService;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MailService;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


@Component(immediate = true, label = "Signup Validation Service", description = "Signup Validation Service", metatype = true)
@Service(value=NewAccountValidationService.class)
@Properties({
		@Property(name = Constants.SERVICE_DESCRIPTION, value = "Signup Validation Service"),
		@Property(name = Constants.SERVICE_VENDOR,value = "Cognizant")
		
})

public class NewAccountValidationService implements AcceleratorService{

	
	
	@Property(label = "Sender's Email Address", value = "", description = "Email address to use for Sender")
	public static final String FROM_EMAIL_ADDR = "fromEmailAddr";
	
	
	@Property(label = "Mail Template Path", value = "/apps/solution-accelerators/components/mails/signupvalidation.txt", description = "Mail Template Path")
	public static final String MAIL_TEMPLATE_PATH = "mailTemplatePath";
	
	@Reference
	private ConfigurationUtil configurationUtil;
	
	@Reference
    ResourceResolverFactory resolverFactory;
	
	Session jcrSession = null;
	
	@Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
	private MailService mailService;
	
	@Reference
	private MessageGatewayService messageGatewayService;
	
	private MessageGateway messageGateway;

	ResourceResolver resourceResolver;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NewAccountValidationService.class);
	
	public AcceleratorServiceResponse execute(AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {

		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");
		NewAccountValidationServiceResponse serviceResponse = new NewAccountValidationServiceResponse();
		NewAccountValidationServiceRequest newAccountValidationServiceRequest=null;
		if (serviceRequest instanceof NewAccountValidationServiceRequest) {
			newAccountValidationServiceRequest = (NewAccountValidationServiceRequest) serviceRequest;
			setConfigProperties(newAccountValidationServiceRequest);
		}
		JSONObject validationResponse = new JSONObject();
		validationResponse = validateRequiredParameters(newAccountValidationServiceRequest);
		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
		} catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					, e);
		}
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(validationStatus)) {
			serviceResponse = sendValidationEmail(newAccountValidationServiceRequest);
		} else {
			serviceResponse.setJsonResponse(validationResponse);
		}
		LOGGER.info(" || " + methodName + " || END");
		return serviceResponse;
	}
	
	private JSONObject validateRequiredParameters(
			NewAccountValidationServiceRequest newAccountValidationServiceRequest)
			throws AcceleratorException {
		String methodName = "validateRequiredParameters : NewAccountValidationService";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true;
		boolean isValidPath = true;
		boolean pathCheck = true;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (newAccountValidationServiceRequest != null) {
				if (newAccountValidationServiceRequest.getMember_id() == 0) {
					parameterPresent = false;
					statusDescription
							.append("Signup Account Validation member id is missing;");
				}
				if (StringUtils.isEmpty(newAccountValidationServiceRequest.getEmail())) {
					parameterPresent = false;
					statusDescription
							.append("Signup Account Validation email is missing;");
				}
				
			} else {
				parameterPresent = false;
				statusDescription
						.append("Signup Account Validation request information missing;");
			}
			if (!parameterPresent || !isValidPath) {
				responseObject.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_FAILURE);
				responseObject.put(AcceleratorGenericConstants.DESCRIPTION,
						statusDescription.toString());
			} else {
				responseObject.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_SUCCESS);

			}
		} catch (JSONException e) {
				LOGGER.error(" || " + methodName + " || EXCEPTION || "
						, e);
		}
		LOGGER.debug(" || " + methodName + " || responseObject || "
				+ responseObject);
		LOGGER.info(" || " + methodName + " || END");
		return responseObject;
	}
		
	private NewAccountValidationServiceResponse sendValidationEmail(NewAccountValidationServiceRequest newAccountValidationServiceRequest){
		String methodName = "sendValidationEmail";
		LOGGER.info(" || " + methodName + " || START");
		
		HtmlEmail userEmail = new HtmlEmail();
		NewAccountValidationServiceResponse newAccountValidationServiceResponse=null;
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
			newAccountValidationServiceResponse=NewAccountValidationHelper.sendValidationEmail(newAccountValidationServiceRequest, userEmail, lifeplusEmailReceipent, messageGatewayService, messageGateway,mailTemplate,mailService);
		} catch (LoginException e) {
			LOGGER.error(" || " + methodName + " || LoginException EXCEPTION OCCURED || ",e);
		}
		catch (AddressException e) {
			LOGGER.error(" || " + methodName + " || AddressException EXCEPTION OCCURED || ",e);
		}
		LOGGER.info(" || " + methodName + " || END");
		return newAccountValidationServiceResponse;
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
		String PID = "com.cts.accelerators.lifeplus.services.NewAccountValidationService";
		if(StringUtils.isNotEmpty(configurationUtil.getConfig(PID, FROM_EMAIL_ADDR))){
			newAccountValidationServiceRequest.setFromEmail(configurationUtil.getConfig(PID, FROM_EMAIL_ADDR));
		}
		if(StringUtils.isNotEmpty(configurationUtil.getConfig(PID, MAIL_TEMPLATE_PATH))){
			newAccountValidationServiceRequest.setMailTemplatePath(configurationUtil.getConfig(PID, MAIL_TEMPLATE_PATH));
		}
		
		LOGGER.info(" || " + methodName + " || END");
	}
}
