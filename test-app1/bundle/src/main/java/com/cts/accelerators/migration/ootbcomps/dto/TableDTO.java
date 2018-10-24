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
 * Name: OOTB Table component (/libs/foundation/components/table)
 * Description: DTO for Table component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class TableDTO extends CoreDTO {
	/**
	 * Properties for the Table component 
	 */
	@XmlElement
	private String tableData;
	
	/**
	 * @return String
	 */
	public final String getTableData() {
		return tableData;
	}
	/**
	 * @param tableData
	 */
	public final void setTableData(String tableData) {
		this.tableData = tableData;
	}

}
