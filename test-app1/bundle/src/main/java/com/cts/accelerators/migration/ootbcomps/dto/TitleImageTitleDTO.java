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
 * Name:  TitleImage component (solution-accelerators/components/demo/content/imagetitle)
 * Description: DTO for TitleImage component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class TitleImageTitleDTO extends CoreDTO {
	
	/**
	 * Properties for the TextImage component 
	 */
	@XmlElement
	private TitleLinkDTO titleLinkDTO;
	@XmlElement
	private TextImageDTO textImageDTO;
	@XmlElement
	private TitleImageDTO titleImageDTO;
	
	/**
	 * @return the titleLinkDTO
	 */
	public TitleLinkDTO getTitleLinkDTO() {
		return titleLinkDTO;
	}
	/**
	 * @param titleLinkDTO the titleLinkDTO to set
	 */
	public void setTitleLinkDTO(TitleLinkDTO titleLinkDTO) {
		this.titleLinkDTO = titleLinkDTO;
	}
	/**
	 * @return the titleImageDTO
	 */
	public TitleImageDTO getTitleImageDTO() {
		return titleImageDTO;
	}
	/**
	 * @param titleImageDTO the titleImageDTO to set
	 */
	public void setTitleImageDTO(TitleImageDTO titleImageDTO) {
		this.titleImageDTO = titleImageDTO;
	}
	/**
	 * @return the textImageDTO
	 */
	public TextImageDTO getTextImageDTO() {
		return textImageDTO;
	}
	/**
	 * @param textImageDTO the textImageDTO to set
	 */
	public void setTextImageDTO(TextImageDTO textImageDTO) {
		this.textImageDTO = textImageDTO;
	}
	
	

}
