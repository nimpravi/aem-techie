/**
 * 
 */
package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author CustomCarouselItemDTO
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class CustomCarouselItemDTO{
	
	@XmlElement
	public String title;
	@XmlElement
	public String titleDesc;
	@XmlElement
	public String navTitle;
	@XmlElement
	public String navDesc;
	@XmlElement
	public String link;
	@XmlElement
	public String imagePath;
	
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
	 * @return the titleDesc
	 */
	public String getTitleDesc() {
		return titleDesc;
	}
	/**
	 * @param titleDesc the titleDesc to set
	 */
	public void setTitleDesc(String titleDesc) {
		this.titleDesc = titleDesc;
	}
	/**
	 * @return the navTitle
	 */
	public String getNavTitle() {
		return navTitle;
	}
	/**
	 * @param navTitle the navTitle to set
	 */
	public void setNavTitle(String navTitle) {
		this.navTitle = navTitle;
	}
	/**
	 * @return the navDesc
	 */
	public String getNavDesc() {
		return navDesc;
	}
	/**
	 * @param navDesc the navDesc to set
	 */
	public void setNavDesc(String navDesc) {
		this.navDesc = navDesc;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
