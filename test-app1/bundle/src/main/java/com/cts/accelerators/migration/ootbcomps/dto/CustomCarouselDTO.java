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

import com.cts.accelerators.migration.ootbcomps.dto.CoreDTO;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: Custom Carousel component
 *         (/apps/solution-accelerators/components/global/content/carousel) Description: DTO for Carousel
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class CustomCarouselDTO extends CoreDTO {
	
	@XmlElement
	List<CustomCarouselItemDTO> itemDto;

	/**
	 * @return the itemDto
	 */
	public List<CustomCarouselItemDTO> getItemDto() {
		return itemDto;
	}

	/**
	 * @param itemDto the itemDto to set
	 */
	public void setItemDto(List<CustomCarouselItemDTO> itemDto) {
		this.itemDto = itemDto;
	}
	
}
