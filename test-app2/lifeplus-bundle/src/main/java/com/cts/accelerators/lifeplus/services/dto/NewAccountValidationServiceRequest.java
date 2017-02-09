package com.cts.accelerators.lifeplus.services.dto;

import javax.jcr.Node;
import javax.jcr.Session;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;


public class NewAccountValidationServiceRequest implements AcceleratorServiceRequest{
	private String email;
	private int member_id;
	Node userNode;
	String initialURL;
	private String fromEmail;
	private String mailTemplatePath;
	
	/**
	 * @return the mailTemplatePath
	 */
	public String getMailTemplatePath() {
		return mailTemplatePath;
	}
	/**
	 * @param mailTemplatePath the mailTemplatePath to set
	 */
	public void setMailTemplatePath(String mailTemplatePath) {
		this.mailTemplatePath = mailTemplatePath;
	}
	/**
	 * @return the fromEmail
	 */
	public String getFromEmail() {
		return fromEmail;
	}
	/**
	 * @param fromEmail the fromEmail to set
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	/**
	 * @return the initialURL
	 */
	public String getInitialURL() {
		return initialURL;
	}
	/**
	 * @param initialURL the initialURL to set
	 */
	public void setInitialURL(String initialURL) {
		this.initialURL = initialURL;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the userNode
	 */
	public Node getUserNode() {
		return userNode;
	}
	/**
	 * @param userNode the userNode to set
	 */
	public void setUserNode(Node userNode) {
		this.userNode = userNode;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the member_id
	 */
	public int getMember_id() {
		return member_id;
	}
	/**
	 * @param member_id the member_id to set
	 */
	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}
	
}
