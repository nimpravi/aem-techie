package com.cts.accelerators.migration.services.dto;

/**
 * ServiceRequest class encapsulating NotificationService parameters
 * 
 * @author 432087
 * 
 */
import org.apache.sling.commons.json.JSONObject;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

public class NotificationServiceRequest implements AcceleratorServiceRequest{
	private String fromEmailAddr;
	private boolean sendNotifications;
	private JSONObject messages;
	private String toEmail;
	private String requestType;
	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}
	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	/**
	 * @return the messages
	 */
	public JSONObject getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(JSONObject messages) {
		this.messages = messages;
	}
	/**
	 * @return the toEmail
	 */
	public String getToEmail() {
		return toEmail;
	}
	/**
	 * @param toEmail the toEmail to set
	 */
	/**
	 * @return the fromEmailAddr
	 */
	public String getFromEmailAddr() {
		return fromEmailAddr;
	}
	/**
	 * @param fromEmailAddr the fromEmailAddr to set
	 */
	public void setFromEmailAddr(String fromEmailAddr) {
		this.fromEmailAddr = fromEmailAddr;
	}
	/**
	 * @return the sendNotifications
	 */
	public boolean isSendNotifications() {
		return sendNotifications;
	}
	/**
	 * @param sendNotifications the sendNotifications to set
	 */
	public void setSendNotifications(boolean sendNotifications) {
		this.sendNotifications = sendNotifications;
	}
	/**
	 * @param toEmail the toEmail to set
	 */
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	
}
