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
 * Name: OOTB Video component (/libs/foundation/components/video)
 * Description: DTO for Video component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class VideoDTO extends CoreDTO {
	
	/**
	 * Properties for the Video component 
	 */
	@XmlElement
	private String file;
	@XmlElement
	private String height;
	@XmlElement
	private String width;
	
	/**
	 * @return String
	 */
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	/**
	 * @return String
	 */
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	
	/**
	 * @return String
	 */
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}	
}
