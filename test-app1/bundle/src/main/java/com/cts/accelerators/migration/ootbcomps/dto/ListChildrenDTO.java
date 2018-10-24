package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB ListChildren
 *         component (/libs/foundation/components/listchildren) Description: DTO
 *         for ListChildren component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ListChildrenDTO extends CoreDTO {

	/**
	 * Properties for the ListChildren component
	 */
	@XmlElement
	private String listroot;

	/**
	 * @return String
	 */
	public String getListroot() {
		return listroot;
	}

	/**
	 * @param listroot
	 */
	public void setListroot(String listroot) {
		this.listroot = listroot;
	}

}
