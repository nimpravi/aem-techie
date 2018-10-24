
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
 * Name: OOTB Redirect component (/libs/foundation/components/redirect)
 * Description: DTO for Redirect component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class RedirectDTO  extends CoreDTO{
	/**
	 * Properties for the Redirect component 
	 */
	@XmlElement
	private String redirectTarget;

	/**
	 * @return String
	 */
	public final String getRedirectTarget() {
		return redirectTarget;
	}

	/**
	 * @param redirectTarget 
	 */
	public final void setRedirectTarget(String redirectTarget) {
		this.redirectTarget = redirectTarget;
	}
	
		
}
