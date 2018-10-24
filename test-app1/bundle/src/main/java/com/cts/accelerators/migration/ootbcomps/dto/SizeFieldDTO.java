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
 * Name: OOTB SizeField component ()
 * Description: DTO for SizeField component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class SizeFieldDTO extends CoreDTO {
	
	/**
	 * Properties for the SizeField component 
	 */
	@XmlElement
	private int rows;
	@XmlElement
	private int cols;
	
	/**
	 * @return int
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * @param rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	/**
	 * @return int
	 */
	public int getCols() {
		return cols;
	}
	/**
	 * @param cols
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}
}
