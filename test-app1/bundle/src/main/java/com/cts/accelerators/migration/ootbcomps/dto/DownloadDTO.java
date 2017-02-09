package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Chart component
 *         (/libs/foundation/components/download) Description: DTO for Download
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class DownloadDTO extends CoreDTO {

	/**
	 * Properties for Download component
	 */
	@XmlElement
	private String jcr_description;
	@XmlElement
	private String file;
	@XmlElement
	private String fileName;
	@XmlElement
	private String fileReference;
	@XmlElement
	private int autoUploadDelay;

	/**
	 * @return jcr_description
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
	 * @return file
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
	 * @return fileName
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
	 * @return fileReference
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
	 * @return autoUploadDelay
	 */
	public int getAutoUploadDelay() {
		return autoUploadDelay;
	}

	/**
	 * @param autoUploadDelay
	 */
	public void setAutoUploadDelay(int autoUploadDelay) {
		this.autoUploadDelay = autoUploadDelay;
	}
}
