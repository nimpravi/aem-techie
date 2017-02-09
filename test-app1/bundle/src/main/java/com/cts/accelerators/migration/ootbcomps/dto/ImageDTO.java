package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Image component
 *         (/libs/foundation/components/image) Description: DTO for Image
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Image")
@XmlSeeAlso(CoreDTO.class)
public class ImageDTO extends CoreDTO {
	/**
	 * Properties for the Image component
	 */
	@XmlElement
	private String file;
	@XmlElement
	private String imageCrop;
	@XmlElement
	private String fileName;
	@XmlElement
	private String fileReference;
	@XmlElement
	private String imageRotate;
	@XmlElement
	private String jcr_title;
	@XmlElement
	private String alt;
	@XmlElement
	private String linkURL;
	@XmlElement
	private String jcr_description;
	@XmlElement
	private String size;
	@XmlElement
	private String height;
	@XmlElement
	private String width;
	@XmlElement
	private String imageText;
	
	/**
	 * @return String
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return String
	 */
	public String getImageCrop() {
		return imageCrop;
	}

	/**
	 * @param imageCrop
	 */
	public void setImageCrop(String imageCrop) {
		this.imageCrop = imageCrop;
	}

	/**
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return String
	 */
	public String getFileReference() {
		return fileReference;
	}

	/**
	 * @param fileReference
	 */
	public void setFileReference(String fileReference) {
		this.fileReference = fileReference;
	}

	/**
	 * @return String
	 */
	public String getImageRotate() {
		return imageRotate;
	}

	/**
	 * @param imageRotate
	 */
	public void setImageRotate(String imageRotate) {
		this.imageRotate = imageRotate;
	}

	/**
	 * @return String
	 */
	public String getJcr_title() {
		return jcr_title;
	}

	/**
	 * @param jcr_title
	 */
	public void setJcr_title(String jcr_title) {
		this.jcr_title = jcr_title;
	}

	/**
	 * @return String
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * @param alt
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * @return String
	 */
	public String getLinkURL() {
		return linkURL;
	}

	/**
	 * @param linkURL
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	/**
	 * @return String
	 */
	public String getJcr_description() {
		return jcr_description;
	}

	/**
	 * @param jcr_description
	 */
	public void setJcr_description(String jcr_description) {
		this.jcr_description = jcr_description;
	}

	/**
	 * @return String
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return String
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return String
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the imageText
	 */
	public String getImageText() {
		return imageText;
	}

	/**
	 * @param imageText the imageText to set
	 */
	public void setImageText(String imageText) {
		this.imageText = imageText;
	}

}
