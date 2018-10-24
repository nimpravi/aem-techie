package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Chart component
 *         (/libs/foundation/components/external) Description: DTO for External
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ExternalDTO extends CoreDTO {

	/**
	 * Properties of the External component
	 */
	@XmlElement
	private String target;
	@XmlElement
	private String passparams;
	@XmlElement
	private int widthheight;

	/**
	 * @return target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return passparams
	 */
	public String getPassparams() {
		return passparams;
	}

	/**
	 * @param passparams
	 */
	public void setPassparams(String passparams) {
		this.passparams = passparams;
	}

	/**
	 * @return widthheight
	 */
	public int getWidthheight() {
		return widthheight;
	}

	/**
	 * @param widthheight
	 */
	public void setWidthheight(int widthheight) {
		this.widthheight = widthheight;
	}

}
