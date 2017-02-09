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
public class TextImageDTO extends CoreDTO {
	
	/**
	 * Properties for the TextImage component 
	 */
	@XmlElement(name = "Image")
	private ImageDTO imageDTO;
	@XmlElement(name = "Text")
	private TextDTO textDTO;
	
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
	 * @return TextDTO
	 */
	public TextDTO getTextDTO() {
		return textDTO;
	}
	/**
	 * @param textDTO
	 */
	public void setTextDTO(TextDTO textDTO) {
		this.textDTO = textDTO;
	}

}
