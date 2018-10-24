package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Flash component
 *         (/libs/foundation/components/flash) Description: DTO for Flash
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class FlashDTO extends CoreDTO {

	/**
	 * Properties for the Flash component
	 */
	@XmlElement
	private String fileReference;
	@XmlElement
	private int height;
	@XmlElement
	private int width;
	@XmlElement
	private ImageDTO html5SmartImageDTO;
	@XmlElement
	private String wmode;
	@XmlElement
	private String bgColor;
	@XmlElement
	private String flashVersion;
	@XmlElement
	private String attrs;

	/**
	 * @return String
	 */
	public String getFileReference() {
		return fileReference;
	}

	/**
	 * @param width
	 */
	public void setFileReference(String fileReference) {
		this.fileReference = fileReference;
	}

	/**
	 * @return int
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param width
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return int
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return ImageDTO
	 */
	public ImageDTO getHtml5SmartImageDTO() {
		return html5SmartImageDTO;
	}

	/**
	 * @param html5SmartImageDTO
	 */
	public void setHtml5SmartImageDTO(ImageDTO html5SmartImageDTO) {
		this.html5SmartImageDTO = html5SmartImageDTO;
	}

	/**
	 * @return String
	 */
	public String getWmode() {
		return wmode;
	}

	/**
	 * @param wmode
	 */
	public void setWmode(String wmode) {
		this.wmode = wmode;
	}

	/**
	 * @return String
	 */
	public String getBgColor() {
		return bgColor;
	}

	/**
	 * @param bgColor
	 */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * @return String
	 */
	public String getFlashVersion() {
		return flashVersion;
	}

	/**
	 * @param flashVersion
	 */
	public void setFlashVersion(String flashVersion) {
		this.flashVersion = flashVersion;
	}

	/**
	 * @return String
	 */
	public String getAttrs() {
		return attrs;
	}

	/**
	 * @param attrs
	 */
	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}
}
