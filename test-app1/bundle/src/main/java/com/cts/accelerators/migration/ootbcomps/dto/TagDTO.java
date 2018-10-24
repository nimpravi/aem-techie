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
 * Name: OOTB Tag component (/libs/social/commons/components/tagging)
 * Description: DTO for Tag component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TagComponent")
@XmlSeeAlso(CoreDTO.class)
public class TagDTO extends CoreDTO {
	@XmlElement
	private String tagId;
	@XmlElement
	private String filterVal;
	@XmlElement
	private String name;
	@XmlElement
	private String jcr_title;
	@XmlElement
	private String jcr_description;
	@XmlElement
	private String parentID;
	@XmlElement
	private String componentID;
	
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getFilterVal() {
		return filterVal;
	}
	public void setFilterVal(String filterVal) {
		this.filterVal = filterVal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJcr_title() {
		return jcr_title;
	}
	public void setJcr_title(String jcr_title) {
		this.jcr_title = jcr_title;
	}
	public String getJcr_description() {
		return jcr_description;
	}
	public void setJcr_description(String jcr_description) {
		this.jcr_description = jcr_description;
	}
	/**
	 * @return the parentID
	 */
	public String getParentID() {
		return parentID;
	}
	/**
	 * @param parentID the parentID to set
	 */
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	/**
	 * @return the componentID
	 */
	public String getComponentID() {
		return componentID;
	}
	/**
	 * @param componentID the componentID to set
	 */
	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}
	
}
