package com.cts.accelerators.migration.ootbcomps.dto;

import java.util.List;

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
 * Name: OOTB List component (/apps/solution-accelerators/components/demo/content/listoflinks)
 * Description: DTO for List Of Link component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ListOfLinkDTO extends CoreDTO{
	
	/**
	 * Properties for the List Of Link component 
	 */
	@XmlElement
	private String title;
	
	
	@XmlElement
	List<ListOfLinkItemDTO> itemDto;
	
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the itemDto
	 */
	public List<ListOfLinkItemDTO> getItemDto() {
		return itemDto;
	}
	/**
	 * @param itemDto the itemDto to set
	 */
	public void setItemDto(List<ListOfLinkItemDTO> itemDto) {
		this.itemDto = itemDto;
	}
	
	
}
