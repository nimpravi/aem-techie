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
public class TitleImageDTO extends CoreDTO {
	
	/**
	 * Properties for the TextImage component 
	 */
	@XmlElement(name = "Image")
	private ImageDTO imageDTO;
	@XmlElement(name = "Title")
	private String title;
	
	/**
	 * @return ImageDTO
	 */
	public ImageDTO getImageDTO() {
		return imageDTO;
	}
	/**
	 * @param imageDTO
	 */
	public void setImageDTO(ImageDTO imageDTO) {
		this.imageDTO = imageDTO;
	}
	
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

}
