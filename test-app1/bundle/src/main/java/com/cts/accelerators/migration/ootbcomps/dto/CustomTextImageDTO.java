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
 * Name: OOTB TextImage component (/libs/foundation/components/textimage)
 * Description: DTO for TextImage component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class CustomTextImageDTO extends CoreDTO {
	
	/**
	 * Properties for the TextImage component 
	 */
	@XmlElement(name = "Image")
	private ImageDTO imageDTO;
	@XmlElement(name = "HeadingText")
	private TextDTO headingTextDTO;
	@XmlElement(name = "MainText")
	private TextDTO mainTextDTO;
	
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
	 * @return the headingTextDTO
	 */
	public TextDTO getHeadingTextDTO() {
		return headingTextDTO;
	}
	/**
	 * @param headingTextDTO the headingTextDTO to set
	 */
	public void setHeadingTextDTO(TextDTO headingTextDTO) {
		this.headingTextDTO = headingTextDTO;
	}
	/**
	 * @return the mainTextDTO
	 */
	public TextDTO getMainTextDTO() {
		return mainTextDTO;
	}
	/**
	 * @param mainTextDTO the mainTextDTO to set
	 */
	public void setMainTextDTO(TextDTO mainTextDTO) {
		this.mainTextDTO = mainTextDTO;
	}

}
