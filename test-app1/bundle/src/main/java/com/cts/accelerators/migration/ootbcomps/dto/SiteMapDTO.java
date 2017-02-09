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
 * Name: OOTB SiteMap component (/libs/foundation/components/sitemap)
 * Description: DTO for SiteMap component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class SiteMapDTO extends CoreDTO {

	/**
	 * Properties for the SiteMap component 
	 */
	@XmlElement
	private String rootPath;
		
	/**
	 * @return String
	 */
	public final String getRootPath() {
		return rootPath;
	}
	/**
	 * @param rootPath
	 */
	public final void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

}
