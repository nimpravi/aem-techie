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
 * Name: OOTB SlideShow component (/libs/foundation/components/slideshow)
 * Description: DTO for SlideShow component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class SlideShowDTO extends CoreDTO {
	/**
	 * Properties for the SlideShow component 
	 */
	@XmlElement
	private String slideshowHeight;
	@XmlElement
	private String slideshowWidth;
	
	
	/**
	 * @return String
	 */
	public final String getSlideshowHeight() {
		return slideshowHeight;
	}
	/**
	 * @param slideshowHeight
	 */
	public final void setSlideshowHeight(String slideshowHeight) {
		this.slideshowHeight = slideshowHeight;
	}
	/**
	 * @return String
	 */
	public final String getSlideshowWidth() {
		return slideshowWidth;
	}
	/**
	 * @param slideshowWidth
	 */
	public final void setSlideshowWidth(String slideshowWidth) {
		this.slideshowWidth = slideshowWidth;
	}
	
}
