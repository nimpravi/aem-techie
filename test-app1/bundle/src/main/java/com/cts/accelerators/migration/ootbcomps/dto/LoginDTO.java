package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant
 * Application : Migration Project
 * Name: OOTB Login component (/libs/foundation/components/login)
 * Description: DTO for Login component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class LoginDTO extends CoreDTO{
	
	/**
	 * Properties for the Login component 
	 */
	@XmlElement
	private String sectionLabel;
	@XmlElement
	private String usernameLabel;
	@XmlElement
	private String passwordLabel;
	@XmlElement
	private String loginLabel;
	@XmlElement
	private String redirectTo;
	
	/**
	 * @return String
	 */
	public String getSectionLabel() {
		return sectionLabel;
	}
	/**
	 * @param sectionLabel 
	 */
	public void setSectionLabel(String sectionLabel) {
		this.sectionLabel = sectionLabel;
	}
	/**
	 * @return String
	 */
	public String getUsernameLabel() {
		return usernameLabel;
	}
	/**
	 * @param usernameLabel 
	 */
	public void setUsernameLabel(String usernameLabel) {
		this.usernameLabel = usernameLabel;
	}
	
	/**
	 * @return String
	 */
	public String getPasswordLabel() {
		return passwordLabel;
	}
	/**
	 * @param passwordLabel 
	 */
	public void setPasswordLabel(String passwordLabel) {
		this.passwordLabel = passwordLabel;
	}
	
	/**
	 * @return String
	 */
	public String getLoginLabel() {
		return loginLabel;
	}
	/**
	 * @param loginLabel 
	 */
	public void setLoginLabel(String loginLabel) {
		this.loginLabel = loginLabel;
	}
	
	/**
	 * @return String
	 */
	public String getRedirectTo() {
		return redirectTo;
	}
	/**
	 * @param redirectTo 
	 */
	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}
}
