package com.cts.accelerators.core.services;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.helpers.NotificationHelper;
import com.cts.accelerators.migration.services.ContentBackupService;
import com.cts.accelerators.migration.services.dto.NotificationServiceRequest;
import com.cts.accelerators.migration.services.dto.NotificationServiceResponse;
import com.day.cq.mailer.MessageGatewayService;

/**
 * This class is used to notify migration status and response to the user through email 
 * @author 432087
 */


@Component(immediate = true, label = "Notification Service", description = "Notification Service", metatype = true)
@Service(value=NotificationService.class)
@Properties({
		@Property(name = Constants.SERVICE_DESCRIPTION, value = "Notification Service"),
		@Property(name = Constants.SERVICE_VENDOR, value = "Cognizant") })
public class NotificationService implements AcceleratorService {

	@Reference(policy = ReferencePolicy.STATIC)
	private MessageGatewayService messageGatewayService;
	
	@Reference
	private ConfigurationUtil configurationUtil;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotificationService.class);

	@Property(label = "Sender's Email Address", value = "", description = "Email address to use for Sender")
	public static final String FROM_EMAIL_ADDR = "fromEmailAddr";

	@Property(label = "Send Email Notifications", boolValue = true, description = "Do you want to send notifications ?")
	public static final String SEND_NOTIFICATION = "sendNotifications";

	/**
	 * This method will be called by external services and application to start
	 * the content import
	 * 
	 * @param ServiceRequest
	 *            serviceReequest
	 * @return ServiceResponse containing status in boolean format
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		NotificationServiceResponse notificationServiceResponse = new NotificationServiceResponse();
		NotificationServiceRequest notificationServiceRequest = null;
		boolean isValidParameters = false;
		if (serviceRequest instanceof NotificationServiceRequest) {
			notificationServiceRequest = (NotificationServiceRequest) serviceRequest;
			setConfigProperties(notificationServiceRequest);
		}
		isValidParameters = validateRequiredParameters(notificationServiceRequest);
		if (isValidParameters) {
			notificationServiceResponse=notifyMessage(notificationServiceRequest);
		}
		return notificationServiceResponse;
	}
	
	/**
	 * This method is used to validate if all the necessary request variables
	 * are present or not
	 * 
	 * @param notificationServiceRequest
	 * @return validateParams
	 */
	private boolean validateRequiredParameters(
			NotificationServiceRequest notificationServiceRequest){
		String methodName = "validateRequiredParameters";
		boolean validateParams = false;
		LOGGER.info(" || " + methodName + " || START");
		if (notificationServiceRequest != null) {
			if (StringUtils.isNotEmpty(notificationServiceRequest
					.getFromEmailAddr())) {
				LOGGER.info(" || " + methodName + " || return value || "+NotificationHelper
						.isEmailCorrect(notificationServiceRequest
								.getFromEmailAddr()));
				return NotificationHelper
						.isEmailCorrect(notificationServiceRequest
								.getFromEmailAddr());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return validateParams;
	}

	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param notificationServiceRequest
	 * @return
	 * @throws AcceleratorException
	 */
	private NotificationServiceRequest setConfigProperties(
			NotificationServiceRequest notificationServiceRequest)
			throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START");
		String PID = "com.cts.accelerators.core.services.NotificationService";
		notificationServiceRequest.setSendNotifications(configurationUtil
				.getBooleanConfig(PID, SEND_NOTIFICATION));
		if (StringUtils.isEmpty(notificationServiceRequest.getFromEmailAddr())) {
			notificationServiceRequest.setFromEmailAddr((configurationUtil
					.getConfig(PID, FROM_EMAIL_ADDR)));
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Send Notifications? "+notificationServiceRequest.isSendNotifications()+"From Email Address: "+notificationServiceRequest.getFromEmailAddr());
		}
		LOGGER.info(" || " + methodName + " || END");
		return notificationServiceRequest;
	}

	/**
	 * This method will be used to notify the logged in user through email, the status of migration
	 * 
	 * @param notificationServiceRequest
	 * @return notificationServiceResponse
	 */
	public NotificationServiceResponse notifyMessage(
			NotificationServiceRequest notificationServiceRequest) {
		String methodName = "notifyMessage";
		LOGGER.info(" || " + methodName + " || START");
		boolean notificationStatus=false;
		NotificationServiceResponse notificationServiceResponse=new NotificationServiceResponse();
		String fromEmailAddr=notificationServiceRequest.getFromEmailAddr();
		boolean sendNotifications=notificationServiceRequest.isSendNotifications();
			if (sendNotifications && StringUtils.isNotEmpty(fromEmailAddr)) {
			
				StringBuilder emailMessage = new StringBuilder();
				JSONObject messages = notificationServiceRequest.getMessages();
				String toEmail = notificationServiceRequest.getToEmail();
				if (null == messages || messages.toString().equals("{}")) {
					NotificationHelper.constructEmptyResponse(
							notificationServiceRequest,emailMessage);
					notificationStatus=true;
				} else if (messages.toString().contains(
						AcceleratorGenericConstants.STATUS_FAILURE)
						&& messages.toString().contains(
								AcceleratorGenericConstants.STATUS_SUCCESS)) {
					NotificationHelper.constructInitialHTMLMessage(notificationServiceRequest, emailMessage);
					
					notificationStatus=NotificationHelper.constructMessage(
							notificationServiceRequest,emailMessage);
					/*if (StringUtils
							.isNotEmpty(messages
									.getString(AcceleratorGenericConstants.STATUS_SUCCESS))) {
						notificationStatus=NotificationHelper.constructSuccessMessage(
								notificationServiceRequest,emailMessage);
					}
					if (StringUtils
							.isNotEmpty(messages
									.getString(AcceleratorGenericConstants.STATUS_FAILURE))) {
						notificationStatus=NotificationHelper.constructFailureMessage(
								notificationServiceRequest, emailMessage);
					}*/
				} else {
					notificationStatus=NotificationHelper.invalidParamsResponse(
							notificationServiceRequest,emailMessage);
				}
				emailMessage.append("</body></html>");
				notificationStatus=NotificationHelper.sendEmail(emailMessage.toString(), toEmail,
						fromEmailAddr, messageGatewayService);
			
			} else {
				LOGGER.info("From email address is empty. Please provide valid from address in configs");
			}
		notificationServiceResponse.setNotificationSuccessful(notificationStatus);
		LOGGER.info(" || " + methodName + " || END");
		return notificationServiceResponse;
	}
}
