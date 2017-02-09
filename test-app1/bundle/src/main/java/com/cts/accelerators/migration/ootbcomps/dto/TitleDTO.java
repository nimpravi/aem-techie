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
 * Name: OOTB Title component (/libs/foundation/components/title)
 * Description: DTO for Title component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class TitleDTO extends CoreDTO {

	/**
	 * Properties for the Title component 
	 */
	@XmlElement
	private String jcr_title;
	@XmlElement
	private String type;
	
	/**
	 * @return String
	 */
	public final String getJcr_title() {
		return jcr_title;
	}
	/**
	 * @param jcr_title 
	 */
	public final void setJcr_title(String jcr_title) {
		this.jcr_title = jcr_title;
	}
	/**
	 * @return String
	 */
	public final String getType() {
		return type;
	}
	/**
	 * @param type
	 */
	public final void setType(String type) {
		this.type = type;
	}

}
