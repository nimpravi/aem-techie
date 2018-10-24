
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
 * Name: OOTB Reference component (/libs/foundation/components/reference)
 * Description: DTO for Reference component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ReferenceDTO extends CoreDTO {
	
	/**
	 * Properties for the Reference component 
	 */
	@XmlElement
	private String path;

	/**
	 * @return String
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path 
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
}
