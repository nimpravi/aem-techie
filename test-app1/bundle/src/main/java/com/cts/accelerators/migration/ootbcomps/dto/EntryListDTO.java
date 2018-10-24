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
 *         (/libs/social/blog/components/entrylist) Description: DTO for
 *         EntryList component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class EntryListDTO extends CoreDTO {
	/**
	 * Maximum number of blog entries
	 */
	@XmlElement
	String limit;
	/**
	 * Number of blog entries per page
	 */
	@XmlElement
	String pageMax;
	/**
	 * Date format used in the list of blog entries. Sample: MMMMM dd, yyyy
	 * hh:mm a for August 31, 2010 10:35 AM
	 */
	@XmlElement
	String dateFormat;

	/**
	 * @return String
	 * 
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return String
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * @return String
	 */
	public String getPageMax() {
		return pageMax;
	}

	/**
	 * @param pageMax
	 */
	public void setPageMax(String pageMax) {
		this.pageMax = pageMax;
	}
}
