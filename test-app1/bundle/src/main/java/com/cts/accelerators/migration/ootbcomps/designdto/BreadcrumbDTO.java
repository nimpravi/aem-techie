package com.cts.accelerators.migration.ootbcomps.designdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.cts.accelerators.migration.ootbcomps.dto.CoreDTO;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Chart component
 *         (/libs/foundation/components/breadcrumb) Description: DTO for
 *         BreadCrumb component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class BreadcrumbDTO extends CoreDTO {
	/**
	 * Properties for the BreadCrumb component
	 */
	@XmlElement
	private String absParent;

	@XmlElement
	private String relParent;

	@XmlElement
	private String delim;

	@XmlElement
	private String trail;

	/**
	 * @return absParent
	 */
	public String getAbsParent() {
		return absParent;
	}

	/**
	 * @param absParent
	 */
	public void setAbsParent(String absParent) {
		this.absParent = absParent;
	}

	/**
	 * @return relParent
	 */
	public String getRelParent() {
		return relParent;
	}

	/**
	 * @param relParent
	 */
	public void setRelParent(String relParent) {
		this.relParent = relParent;
	}

	/**
	 * @return delim
	 */
	public String getDelim() {
		return delim;
	}

	/**
	 * @param delim
	 */
	public void setDelim(String delim) {
		this.delim = delim;
	}

	/**
	 * @return trail
	 */
	public String getTrail() {
		return trail;
	}

	/**
	 * @param trail
	 */
	public void setTrail(String trail) {
		this.trail = trail;
	}

}
