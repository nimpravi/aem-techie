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
 * Name: OOTB Profile component (/libs/foundation/components/profile)
 * Description: DTO for Profile component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ProfileDTO extends CoreDTO{
	
	/**
	 * Properties for the Profile component 
	 */
	@XmlElement
	private String jcr_title;
	@XmlElement
	private String deleteTitle;
	@XmlElement
	private String cols;
	@XmlElement
	private String width;
	@XmlElement
	private String constraintMessage;
	@XmlElement
	private String constraintType;
	@XmlElement
	private String required;
	@XmlElement
	private String requiredMessage;
	@XmlElement
	private String jcr_description;
	@XmlElement
	private SizeFieldDTO sizeFieldDTO;
	@XmlElement
	private String defaultValue;
	@XmlElement
	private String honoricPrefixTitle;
	@XmlElement
	private String title;
	@XmlElement
	private String givenNameTitle;
	@XmlElement
	private String middleNameTitle;
	@XmlElement
	private String familyNameTitle;
	@XmlElement
	private String honoricSuffixTitle;
	@XmlElement
	private String name;
	@XmlElement
	private String confirmationTitle;
	
	/**
	 * @return String
	 */
	public String getJcr_title() {
		return jcr_title;
	}
	/**
	 * @param jcr_title 
	 */
	public void setJcr_title(String jcr_title) {
		this.jcr_title = jcr_title;
	}
	/**
	 * @return String
	 */
	public String getDeleteTitle() {
		return deleteTitle;
	}
	/**
	 * @param deleteTitle 
	 */
	public void setDeleteTitle(String deleteTitle) {
		this.deleteTitle = deleteTitle;
	}
	/**
	 * @return String
	 */
	public String getCols() {
		return cols;
	}
	/**
	 * @param cols 
	 */
	public void setCols(String cols) {
		this.cols = cols;
	}
	/**
	 * @return String
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width 
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return String
	 */
	public String getConstraintMessage() {
		return constraintMessage;
	}
	/**
	 * @param constraintMessage 
	 */
	public void setConstraintMessage(String constraintMessage) {
		this.constraintMessage = constraintMessage;
	}
	/**
	 * @return String
	 */
	public String getConstraintType() {
		return constraintType;
	}
	/**
	 * @param constraintType 
	 */
	public void setConstraintType(String constraintType) {
		this.constraintType = constraintType;
	}
	/**
	 * @return String
	 */
	public String getRequired() {
		return required;
	}
	/**
	 * @param required 
	 */
	public void setRequired(String required) {
		this.required = required;
	}
	/**
	 * @return String
	 */
	public String getRequiredMessage() {
		return requiredMessage;
	}
	/**
	 * @param requiredMessage 
	 */
	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}
	/**
	 * @return String
	 */
	public String getJcr_description() {
		return jcr_description;
	}
	/**
	 * @param jcr_description 
	 */
	public void setJcr_description(String jcr_description) {
		this.jcr_description = jcr_description;
	}
	/**
	 * @return SizeFieldDTO
	 */
	public SizeFieldDTO getSizeFieldDTO() {
		return sizeFieldDTO;
	}
	/**
	 * @param sizeFieldDTO 
	 */
	public void setSizeFieldDTO(SizeFieldDTO sizeFieldDTO) {
		this.sizeFieldDTO = sizeFieldDTO;
	}
	/**
	 * @return String
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue 
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return String
	 */
	public String getHonoricPrefixTitle() {
		return honoricPrefixTitle;
	}
	/**
	 * @param honoricPrefixTitle 
	 */
	public void setHonoricPrefixTitle(String honoricPrefixTitle) {
		this.honoricPrefixTitle = honoricPrefixTitle;
	}
	/**
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title 
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return String
	 */
	public String getGivenNameTitle() {
		return givenNameTitle;
	}
	/**
	 * @param givenNameTitle 
	 */
	public void setGivenNameTitle(String givenNameTitle) {
		this.givenNameTitle = givenNameTitle;
	}
	/**
	 * @return String
	 */
	public String getMiddleNameTitle() {
		return middleNameTitle;
	}
	/**
	 * @param middleNameTitle 
	 */
	public void setMiddleNameTitle(String middleNameTitle) {
		this.middleNameTitle = middleNameTitle;
	}
	/**
	 * @return String
	 */
	public String getFamilyNameTitle() {
		return familyNameTitle;
	}
	/**
	 * @param familyNameTitle 
	 */
	public void setFamilyNameTitle(String familyNameTitle) {
		this.familyNameTitle = familyNameTitle;
	}
	/**
	 * @return String
	 */
	public String getHonoricSuffixTitle() {
		return honoricSuffixTitle;
	}
	/**
	 * @param honoricSuffixTitle 
	 */
	public void setHonoricSuffixTitle(String honoricSuffixTitle) {
		this.honoricSuffixTitle = honoricSuffixTitle;
	}
	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return String
	 */
	public String getConfirmationTitle() {
		return confirmationTitle;
	}
	/**
	 * @param confirmationTitle 
	 */
	public void setConfirmationTitle(String confirmationTitle) {
		this.confirmationTitle = confirmationTitle;
	}
}
