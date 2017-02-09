/**
 * 
 */
package com.cts.accelerators.migration.ootbcomps.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author 
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ImageMultiFieldDTO extends CoreDTO {
	
	@XmlElement
	String imageLayout;
	
	@XmlElement
	List<ImageDTO> imageDTO;
	
	@XmlElement
	CustomImageDTO customImageDTO;
	
	/**
	 * @return the imageLayout
	 */
	public String getImageLayout() {
		return imageLayout;
	}

	/**
	 * @param imageLayout the imageLayout to set
	 */
	public void setImageLayout(String imageLayout) {
		this.imageLayout = imageLayout;
	}
	
	/**
	 * @return the imageDTO
	 */
	public List<ImageDTO> getImageDTO() {
		return imageDTO;
	}

	/**
	 * @param imageDTO the imageDTO to set
	 */
	public void setImageDTO(List<ImageDTO> imageDTO) {
		this.imageDTO = imageDTO;
	}
	
	/**
	 * @return the customImageDTO
	 */
	public CustomImageDTO getCustomImageDTO() {
		return customImageDTO;
	}

	/**
	 * @param customImageDTO the customImageDTO to set
	 */
	public void setCustomImageDTO(CustomImageDTO customImageDTO) {
		this.customImageDTO = customImageDTO;
	}

}
