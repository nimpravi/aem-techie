package com.cts.accelerators.migration.helpers;

/**
 * This is the helper class forNotification Service to construct the email message and send.
 * 
 * @author 432087
 * 
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.resolver.Resource;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.services.dto.NotificationServiceRequest;
import com.day.cq.mailer.MailingException;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

public class NotificationHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotificationHelper.class);
	private static final String CLASS_NAME = NotificationHelper.class.getName();

	/**
	 * This method will be used to validate an email address through regular
	 * expressions
	 * 
	 * @param email
	 * @return boolean
	 */
	public static boolean isEmailCorrect(String email) {
		String methodName = "isEmailCorrect";
		LOGGER.info(" || " + methodName + " || START");
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);

		Matcher matcher = pattern.matcher(email);
		LOGGER.info(" || " + methodName + " || END");
		return matcher.matches();
	}

	/**
	 * This method will be used to construct the Success message by parsing the
	 * JSON response passed as parameter to this method
	 * 
	 * @param notificationServiceRequest
	 * @return constructionStatus
	 */
	public static boolean constructMessage(
			NotificationServiceRequest notificationServiceRequest,
			StringBuilder emailMessage) {
		String methodName = "constructSuccessMessage";
		LOGGER.info(" || " + methodName + " || START");
		boolean constructionStatus = false;
		JSONObject messages = notificationServiceRequest.getMessages();

		try {
			
			if ((StringUtils.isNotEmpty(messages
					.getString(AcceleratorGenericConstants.STATUS_SUCCESS)))) {
				emailMessage.append("<tr style='height:44.75pt'>\n");
				emailMessage
						.append("<td width=909 valign=top style='width:682.05pt;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:44.75pt; '>\n");
				emailMessage
						.append("<p class=MsoNormal style=\"font-weight:bold; color:#000;\">\n");

				emailMessage.append("</p>\n");
				emailMessage.append("<p class=MsoNormal>\n");
				emailMessage.append("<b>\n");
				emailMessage
						.append("<span style='font-size:12.0pt; padding-left:2%; padding-left:2%;width: 100%;display: inline-block;word-wrap: break-word; text-align:center;'>\n");
				emailMessage.append("Success ("
						+ messages.getJSONObject(
								AcceleratorGenericConstants.STATUS_SUCCESS)
								.getInt(AcceleratorGenericConstants.COUNT)
						+ ")<br>");
				emailMessage
				.append("View files-<br>");
				emailMessage.append(""+ messages
						.getJSONObject(AcceleratorGenericConstants.STATUS_SUCCESS).getString(AcceleratorGenericConstants.REPORT_PATH)+"<br>");
			

				emailMessage.append("</span></p></td></tr>\n");
			}
			
			if (StringUtils.isNotEmpty(messages
					.getString(AcceleratorGenericConstants.STATUS_FAILURE))) {
				emailMessage.append("<tr style='height:44.75pt'>\n");
				emailMessage
						.append("<td width=909 valign=top style='width:682.05pt;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:44.75pt; '>\n");
				emailMessage
						.append("<p class=MsoNormal style=\"font-weight:bold; color:#000;\">\n");

				emailMessage.append("</p>\n");
				emailMessage.append("<p class=MsoNormal>\n");
				emailMessage.append("<b>\n");
			emailMessage.append("<span style='font-size:12.0pt;padding-left:2%;padding-left: 2%;width: 100%;display: inline-block;word-wrap: break-word;text-align:center;'>Failure ("
					+ messages.getJSONObject(
							AcceleratorGenericConstants.STATUS_FAILURE)
							.getInt(AcceleratorGenericConstants.COUNT)
					+ ")<br>");
			emailMessage.append("View Files -<br>");
			emailMessage.append(""+messages
					.getJSONObject(
							AcceleratorGenericConstants.STATUS_FAILURE)
					.getString(
							AcceleratorGenericConstants.REPORT_PATH)+"<br>");
			
			emailMessage.append("</span></b></p></td></tr>\n");
			}	
		} catch (Exception e) {
			LOGGER.error(
					"Fatal exception while sending Migration notification: in "
							+ CLASS_NAME + "." + methodName, e);
		}
		LOGGER.info(" || " + methodName + " || END");
		return constructionStatus;
	}

	/**
	 * This method will be used to construct the Success message by parsing the
	 * JSON response passed as parameter to this method
	 * 
	 * @param notificationServiceRequest
	 * @return constructionStatus
	 * @throws RepositoryException 
	 * @throws AcceleratorException 
	 */
	public static void constructInitialHTMLMessage(
			NotificationServiceRequest notificationServiceRequest,
			StringBuilder emailMessage) {
		String methodName = "constructSuccessMessage";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject messages = notificationServiceRequest.getMessages();
		emailMessage
				.append("<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\">\n");
		emailMessage.append("<head>\n");
		emailMessage
				.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=us-ascii\">\n");
		emailMessage
				.append("<meta name=Generator content=\"Microsoft Word 14 (filtered medium)\">\n");
		emailMessage.append("<!--[if !mso]>");
		emailMessage.append("<style>v\\:* {behavior:url(#default#VML);}\n");
		emailMessage.append("o\\:* {behavior:url(#default#VML);}\n");
		emailMessage.append("w\\:* {behavior:url(#default#VML);}\n");
		emailMessage.append(".shape {behavior:url(#default#VML);}\n");
		emailMessage.append("</style>\n");
		emailMessage.append("<![endif]-->\n");
		emailMessage.append("<style>\n");
		emailMessage.append("<!--\n");
		/* Font Definitions */
		emailMessage.append("@font-face\n");
		emailMessage.append("{font-family:Calibri;\n");
		emailMessage.append("panose-1:2 15 5 2 2 2 4 3 2 4;}\n");
		emailMessage.append("@font-face\n");
		emailMessage.append("{font-family:Tahoma;\n");
		emailMessage.append("panose-1:2 11 6 4 3 5 4 4 2 4;}\n");
		/* Style Definitions */
		emailMessage.append("p.MsoNormal, li.MsoNormal, div.MsoNormal\n");
		emailMessage.append("{margin:0in;\n");
		emailMessage.append("margin-bottom:.0001pt;\n");
		emailMessage.append("font-size:11.0pt;\n");
		emailMessage.append("font-family:\"Calibri\",\"sans-serif\";}\n");
		emailMessage.append("a:link, span.MsoHyperlink\n");
		emailMessage.append("{mso-style-priority:99;\n");
		emailMessage.append("color:blue;\n");
		emailMessage.append("text-decoration:underline;}\n");
		emailMessage.append("a:visited, span.MsoHyperlinkFollowed\n");
		emailMessage.append("{mso-style-priority:99;\n");
		emailMessage.append("color:purple;\n");
		emailMessage.append("text-decoration:underline;}\n");
		emailMessage.append("p.MsoAcetate, li.MsoAcetate, div.MsoAcetate\n");
		emailMessage.append("{mso-style-priority:99;\n");
		emailMessage.append("mso-style-link:\"Balloon Text Char\"\n");
		emailMessage.append("margin:0in;\n");
		emailMessage.append("margin-bottom:.0001pt;\n");
		emailMessage.append("font-size:8.0pt;\n");
		emailMessage.append("font-family:\"Tahoma\",\"sans-serif\";}\n");
		emailMessage.append("span.BalloonTextChar\n");
		emailMessage.append("{mso-style-name:\"Balloon Text Char\";\n");
		emailMessage.append("mso-style-priority:99;\n");
		emailMessage.append("mso-style-link:\"Balloon Text\";\n");
		emailMessage.append("font-family:\"Tahoma\",\"sans-serif\";}\n");
		emailMessage.append("span.EmailStyle19\n");
		emailMessage.append("{mso-style-type:personal;\n");
		emailMessage.append("font-family:\"Calibri\",\"sans-serif\";\n");
		emailMessage.append("color:windowtext;}\n");
		emailMessage.append("span.EmailStyle20\n");
		emailMessage.append("{mso-style-type:personal;\n");
		emailMessage.append("font-family:\"Calibri\",\"sans-serif\";\n");
		emailMessage.append("color:#1F497D;}\n");
		emailMessage.append("span.EmailStyle21\n");
		emailMessage.append("{mso-style-type:personal-reply;\n");
		emailMessage.append("font-family:\"Calibri\",\"sans-serif\";\n");
		emailMessage.append("color:#1F497D;}\n");
		emailMessage.append(".MsoChpDefault\n");
		emailMessage.append("{mso-style-type:export-only;\n");
		emailMessage.append("font-size:10.0pt;}\n");
		emailMessage.append("@page WordSection1\n");
		emailMessage.append("{size:8.5in 11.0in;\n");
		emailMessage.append("margin:1.0in 1.0in 1.0in 1.0in;}\n");
		emailMessage.append("div.WordSection1\n");
		emailMessage.append("{page:WordSection1;}\n");
		emailMessage.append("-->\n");
		emailMessage.append("</style>\n");
		emailMessage.append("<!--[if gte mso 9]>\n");
		emailMessage.append("<xml>\n");
		emailMessage
				.append("<o:shapedefaults v:ext=\"edit\" spidmax=\"1027\" />\n");
		emailMessage.append("</xml>\n");
		emailMessage.append("<![endif]--><!--[if gte mso 9]>\n");
		emailMessage.append("<xml>\n");
		emailMessage.append("<o:shapelayout v:ext=\"edit\">\n");
		emailMessage.append("<o:idmap v:ext=\"edit\" data=\"1\" />\n");
		emailMessage.append("</o:shapelayout>\n");
		emailMessage.append("</xml>\n");
		emailMessage.append("<![endif]-->\n");
		emailMessage.append("</head>\n");
		emailMessage.append("<body lang=EN-US link=blue vlink=purple>\n");
		emailMessage.append("<div class=WordSection1>\n");
		emailMessage.append("<p class=MsoNormal>\n");
		emailMessage.append("&nbsp;\n");
		emailMessage.append("</p>\n");
		emailMessage.append("<p class=MsoNormal>\n");
		emailMessage.append("<img width=335 height=80 id=\"Picture_x0020_1\" src=\"logo_left.jpg\"style='margin-right:64%;'> <img width=104 height=96 id=\"Picture_x0020_2\" src=\"logo_right.jpg\">\n");
		emailMessage.append("</p>\n");
		emailMessage.append("<p class=MsoNormal>\n");
		emailMessage
				.append("<![if !vml]><span style='mso-ignore:vglayout;position:relative;z-index:251659264;left:-4px;top:5px;width:1303px;height:49px; '><img width=1303 height=44  v:shapes=\"Rectangle_x0020_3\" style=\"background-color:#1b3c67\"></span><![endif]>\n");
		emailMessage.append("<br style='mso-ignore:vglayout' clear=ALL>\n");
		
		try {
			constructStepSpecificMessage(notificationServiceRequest.getRequestType(), emailMessage);
		} catch (AcceleratorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (messages.has(AcceleratorGenericConstants.TOTAL_RECORDS)) {
				emailMessage
						.append("<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 style='margin-left:187.15pt;border-collapse:collapse'>\n");
				emailMessage
						.append("<tr style='height:19.2pt; background-color:#105eb3;'>\n");
				emailMessage
						.append(" <td width=909 valign=top style='width:682.05pt;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:19.2pt'>\n");
				emailMessage
						.append(" <p class=MsoNormal style=\"color:#fff; text-align:center; font-weight:bold;\">\n");
				emailMessage
						.append("Total Number of Records Handled -"
								+ messages
										.getString(AcceleratorGenericConstants.TOTAL_RECORDS)
								+ "</p></td></tr>\n");

			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method will be used to construct the Failure message by parsing the
	 * JSON response passed as parameter to this method
	 * 
	 * @param notificationServiceRequest
	 * @return constructionStatus
	 */
	/*
	 * public static boolean constructFailureMessage( NotificationServiceRequest
	 * notificationServiceRequest, StringBuilder emailMessage) { String
	 * methodName = "constructFailureMessage"; LOGGER.info(" || " + methodName +
	 * " || START"); boolean constructionStatus = false; JSONObject messages =
	 * notificationServiceRequest.getMessages(); try {
	 * emailMessage.append("<span style='font-size:12.0pt'>Failure (" +
	 * messages.getJSONObject(
	 * AcceleratorGenericConstants.STATUS_FAILURE).getInt(
	 * AcceleratorGenericConstants.COUNT) + ")</span></b></p>");
	 * emailMessage.append(
	 * "<p class=MsoNormal> <b><span style='font-size:12.0pt;color:#7F7F7F; padding-right:50%; padding-left:2%;'>View files-"
	 * + messages.getJSONObject( AcceleratorGenericConstants.STATUS_FAILURE)
	 * .getString(AcceleratorGenericConstants.REPORT_PATH) + "");
	 * constructionStatus = true; if (StringUtils.isNotEmpty(messages
	 * .getString(AcceleratorGenericConstants.DESCRIPTION))) {
	 * constructionStatus = constructDescription(emailMessage, messages); } if
	 * (StringUtils.isNotEmpty(messages
	 * .getString(AcceleratorGenericConstants.REPORT_PATH))) {
	 * constructionStatus = constructReportFilePath(emailMessage, messages); }
	 * emailMessage.append("</table>"); } catch (JSONException e) {
	 * LOGGER.error( "Fatal exception while sending Migration notification: in "
	 * + CLASS_NAME + "." + methodName, e); } LOGGER.info(" || " + methodName +
	 * " || END"); return constructionStatus; }
	 */
	/**
	 * This method will be used to construct the message for null response
	 * 
	 * @param notificationServiceRequest
	 * @return constructionStatus
	 */
	public static void constructEmptyResponse(
			NotificationServiceRequest notificationServiceRequest,
			StringBuilder emailMessage) {
		String methodName = "constructEmptyResponse";
		LOGGER.info(" || " + methodName + " || START");
		emailMessage
				.append("<table><tr><td> Response was Null </td></tr></table>");
		LOGGER.info(" || " + methodName + " || END");
	}
	
	public static void constructStepSpecificMessage(String requestType,StringBuilder emailMessage) throws AcceleratorException, RepositoryException{
		
		if (AcceleratorGenericConstants.GENERATE_SAMPLE_XSD.equals(requestType)) {
			Node node=(Node) ConnectionManager.getResourceResolver().getResource("/content/solution-accelerators/Migration/home-page/xsd-generator").adaptTo(Node.class);
			String text="";
			if(node.hasNode("jcr:content")){
				Node jcrNode=node.getNode("jcr:content");
				if(jcrNode.hasNode("titletext")){
					Node titleTextNode=jcrNode.getNode("titletext");
					if(titleTextNode.hasProperty("text"))
					{
						text=titleTextNode.getProperty("text").getString();
						
					}
				}
			}
			emailMessage.append("<p class=MsoNormal style='margin-bottom:4%;'>\n");
			emailMessage.append("<span style='font-size:18.0pt'>\n");
			emailMessage.append("XSD Format\n");
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			emailMessage.append("<p class=MsoNormal>\n");
			emailMessage.append("<span style='font-size:14.0pt'>\n");
			emailMessage.append(text);
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			
		} else if (AcceleratorGenericConstants.CONTENT_BACKUP
				.equalsIgnoreCase(requestType)) {
			Node node=(Node) ConnectionManager.getResourceResolver().getResource("/content/solution-accelerators/Migration/home-page/content-backup").adaptTo(Node.class);
			String text="";
			if(node.hasNode("jcr:content")){
				Node jcrNode=node.getNode("jcr:content");
				if(jcrNode.hasNode("titletext")){
					Node titleTextNode=jcrNode.getNode("titletext");
					if(titleTextNode.hasProperty("text"))
					{
						text=titleTextNode.getProperty("text").getString();
						
					}
				}
			}
			emailMessage.append("<p class=MsoNormal style='margin-bottom:4%;'>\n");
			emailMessage.append("<span style='font-size:18.0pt'>\n");
			emailMessage.append("Content Backup\n");
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			emailMessage.append("<p class=MsoNormal>\n");
			emailMessage.append("<span style='font-size:14.0pt'>\n");
			emailMessage.append(text);
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
		} else if (AcceleratorGenericConstants.CONTENT_TRANSFORMER
				.equalsIgnoreCase(requestType)) {
			Node node=(Node) ConnectionManager.getResourceResolver().getResource("/content/solution-accelerators/Migration/home-page/content-transformer/").adaptTo(Node.class);
			String text="";
			if(node.hasNode("jcr:content")){
				Node jcrNode=node.getNode("jcr:content");
				if(jcrNode.hasNode("titletext")){
					Node titleTextNode=jcrNode.getNode("titletext");
					if(titleTextNode.hasProperty("text"))
					{
						text=titleTextNode.getProperty("text").getString();
						
					}
				}
			}
			emailMessage.append("<p class=MsoNormal style='margin-bottom:4%;'>\n");
			emailMessage.append("<span style='font-size:18.0pt'>\n");
			emailMessage.append("Content Transformer\n");
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			emailMessage.append("<p class=MsoNormal>\n");
			emailMessage.append("<span style='font-size:14.0pt'>\n");
			emailMessage.append(text);
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			
		} else if (AcceleratorGenericConstants.DAM_BULK_UPLOAD
				.equalsIgnoreCase(requestType)) {
			Node node=(Node) ConnectionManager.getResourceResolver().getResource("/content/solution-accelerators/Migration/home-page/dam-bulk-upload").adaptTo(Node.class);
			String text="";
			if(node.hasNode("jcr:content")){
				Node jcrNode=node.getNode("jcr:content");
				if(jcrNode.hasNode("titletext")){
					Node titleTextNode=jcrNode.getNode("titletext");
					if(titleTextNode.hasProperty("text"))
					{
						text=titleTextNode.getProperty("text").getString();
						
					}
				}
			}
			emailMessage.append("<p class=MsoNormal style='margin-bottom:4%;'>\n");
			emailMessage.append("<span style='font-size:18.0pt'>\n");
			emailMessage.append("Digital Asset Bulk Upload\n");
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			emailMessage.append("<p class=MsoNormal>\n");
			emailMessage.append("<span style='font-size:14.0pt'>\n");
			emailMessage.append(text);
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			
		} else if (AcceleratorGenericConstants.CONTENT_IMPORTER
				.equalsIgnoreCase(requestType)) {
			Node node=(Node) ConnectionManager.getResourceResolver().getResource("/content/solution-accelerators/Migration/home-page/content-migration").adaptTo(Node.class);
			String text="";
			if(node.hasNode("jcr:content")){
				Node jcrNode=node.getNode("jcr:content");
				if(jcrNode.hasNode("titletext")){
					Node titleTextNode=jcrNode.getNode("titletext");
					if(titleTextNode.hasProperty("text"))
					{
						text=titleTextNode.getProperty("text").getString();
						
					}
				}
			}
			emailMessage.append("<p class=MsoNormal style='margin-bottom:4%;'>\n");
			emailMessage.append("<span style='font-size:18.0pt'>\n");
			emailMessage.append("Content Migration\n");
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			emailMessage.append("<p class=MsoNormal>\n");
			emailMessage.append("<span style='font-size:14.0pt'>\n");
			emailMessage.append(text);
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			
			
		}
		else if(AcceleratorGenericConstants.COMPLETE_MIGRATION_PROCESS
				.equals(requestType)) {
			Node node=(Node) ConnectionManager.getResourceResolver().getResource("/content/solution-accelerators/Migration/home-page/one-step-migration").adaptTo(Node.class);
			String text="";
			if(node.hasNode("jcr:content")){
				Node jcrNode=node.getNode("jcr:content");
				if(jcrNode.hasNode("titletext")){
					Node titleTextNode=jcrNode.getNode("titletext");
					if(titleTextNode.hasProperty("text"))
					{
						text=titleTextNode.getProperty("text").getString();
						
					}
				}
			}
			emailMessage.append("<p class=MsoNormal style='margin-bottom:4%;'>\n");
			emailMessage.append("<span style='font-size:18.0pt'>\n");
			emailMessage.append("One step Migration\n");
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			emailMessage.append("<p class=MsoNormal>\n");
			emailMessage.append("<span style='font-size:14.0pt'>\n");
			emailMessage.append(text);
			emailMessage.append("</span>\n");
			emailMessage.append("</p>\n");
			
			
		}
	}

	/**
	 * This method will be used to construct the message for invalid/missing
	 * parameters response
	 * 
	 * @param notificationServiceRequest
	 * @return constructionStatus
	 */
	public static boolean invalidParamsResponse(
			NotificationServiceRequest notificationServiceRequest,
			StringBuilder emailMessage) {
		String methodName = "invalidParamsResponse";
		LOGGER.info(" || " + methodName + " || START");
		boolean constructionStatus = false;
		JSONObject messages = notificationServiceRequest.getMessages();
		try {
			emailMessage
					.append("<table><tr><th>Status</th><th>Description</th></tr>");
			emailMessage.append("<tr><td>"
					+ messages.getString(AcceleratorGenericConstants.STATUS)
					+ "</td>");
			String[] descriptionArray = StringUtils.isNotEmpty(messages
					.getString(AcceleratorGenericConstants.DESCRIPTION)) ? messages
					.getString(AcceleratorGenericConstants.DESCRIPTION).split(
							";") : new String[] {};
			for (String description : descriptionArray) {
				emailMessage.append("<td>" + description + "</td></br>");
			}
			emailMessage.append("</tr>");
			emailMessage.append("</table>");
			constructionStatus = true;
		} catch (JSONException e) {
			LOGGER.error(
					"Fatal exception while sending Migration notification: in "
							+ CLASS_NAME + "." + methodName, e);
		}
		LOGGER.info(" || " + methodName + " || END");
		return constructionStatus;
	}

	/**
	 * This method will be used to send the actual email to the logged in user
	 * 
	 * @param emailMessage
	 *            , toEmail, fromEmailAddr, messageGatewayService
	 * @return notificationStatus
	 */
	public static boolean sendEmail(String emailMessage, String toEmail,
			String fromEmailAddr, MessageGatewayService messageGatewayService) {
		String methodName = "sendEmail";
		LOGGER.info(" || " + methodName + " || START");
		boolean notificationStatus = false;
		try {
			HtmlEmail email = new HtmlEmail();
			List<InternetAddress> emailAddresses = new ArrayList<InternetAddress>();
			if (isEmailCorrect(toEmail)) {
				InternetAddress address = new InternetAddress(toEmail.trim());
				emailAddresses.add(address);
				email.setTo(emailAddresses);
				email.setFrom(fromEmailAddr);
				email.setSubject("Notification: Migration Status");
				email.setHtmlMsg(emailMessage);
				MessageGateway<HtmlEmail> messageGateway = messageGatewayService
						.getGateway(HtmlEmail.class);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Email Notification: " + "From: "
							+ fromEmailAddr + "To: " + toEmail + "Email Text: "
							+ emailMessage.toString());
				}
				if (messageGateway != null) {
					messageGateway.send(email);
				}
				notificationStatus = true;
			} else {
				LOGGER.info("Invalid To email ID; notification not sent. Please provide valid email Id in Configs");
			}
		} catch (AddressException e) {
			LOGGER.error(
					"Fatal exception while sending Migration notification: in "
							+ CLASS_NAME + "." + methodName, e);

		} catch (MailingException e) {
			LOGGER.error(
					"Fatal exception while sending Migration notification: in "
							+ CLASS_NAME + "." + methodName, e);

		} catch (EmailException e) {
			LOGGER.error(
					"Fatal exception while sending Migration notification: in "
							+ CLASS_NAME + "." + methodName, e);
		}
		LOGGER.info(" || " + methodName + " || END");
		return notificationStatus;
	}

	/**
	 * This method will be used to construct the description by parsing the
	 * response JSON
	 * 
	 * @param emailMessage
	 *            ,messages
	 * @return constructionStatus
	 */
	/*public static boolean constructDescription(StringBuilder emailMessage,
			JSONObject messages) {
		String methodName = "constructDescription";
		LOGGER.info(" || " + methodName + " || START");
		boolean constructionStatus = false;
		try {
			emailMessage.append("<tr>");
			String[] descriptionArray = StringUtils.isNotEmpty(messages
					.getString(AcceleratorGenericConstants.DESCRIPTION)) ? messages
					.getString(AcceleratorGenericConstants.DESCRIPTION).split(
							";") : new String[] {};
			for (String description : descriptionArray) {
				emailMessage.append("<td>" + description + "</td></br>");
			}
			emailMessage.append("</tr>");
			constructionStatus = true;
		} catch (JSONException e) {
			LOGGER.error(
					"Fatal exception while sending Migration notification: in "
							+ CLASS_NAME + "." + methodName, e);
		}
		LOGGER.info(" || " + methodName + " || END");
		return constructionStatus;
	}*/

	/**
	 * This method will be used to construct the report details by parsing the
	 * response JSON
	 * 
	 * @param emailMessage
	 *            ,messages
	 * @return constructionStatus
	 */
	public static boolean constructReportFilePath(StringBuilder emailMessage,
			JSONObject messages) {
		String methodName = "constructReportFilePath";
		LOGGER.info(" || " + methodName + " || START");
		boolean constructionStatus = false;
		try {
			emailMessage.append("<tr style='height:44.75pt'>\n");
			emailMessage
					.append("<td width=909 valign=top style='width:682.05pt;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:44.75pt; '>\n");
			emailMessage
					.append("<p class=MsoNormal style=\"font-weight:bold; color:#000;\">\n");

			emailMessage.append("</p>\n");
			emailMessage.append("<p class=MsoNormal>\n");
			emailMessage.append("<b>\n");
			emailMessage
					.append("<span style='font-size:12.0pt; padding-left:2%; padding-left:2%;width: 100%;display: inline-block;word-wrap: break-word; text-align:center;'>\n");
			emailMessage
					.append("Detailed report(All)<br>"
							+ messages
									.getString(AcceleratorGenericConstants.REPORT_PATH)
							+ "<br></span></td></tr></table>");
			constructionStatus = true;
		} catch (JSONException e) {
			LOGGER.error(
					"Fatal exception while sending Migration notification: in "
							+ CLASS_NAME + "." + methodName, e);
		}
		LOGGER.info(" || " + methodName + " || END");
		return constructionStatus;

	}
}
