package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CoreDTO {
	// Do nothing DTO for all OOTB components
	private String parentNodePath;

	private String resourceType;

	private String nodeName;

	public CoreDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CoreDTO(String parentNodePath, String resourceType, String nodeName) {
		super();
		this.parentNodePath = parentNodePath;
		this.resourceType = resourceType;
		this.nodeName = nodeName;
	}

	/**
	 * @return the parentNodePath
	 */
	@XmlElement(name = "parentNodePath")
	public String getParentNodePath() {
		return parentNodePath;
	}

	/**
	 * @param parentNodePath
	 *            the parentNodePath to set
	 */
	public void setParentNodePath(String parentNodePath) {
		this.parentNodePath = parentNodePath;
	}

	/**
	 * @return the resourceType
	 */
	@XmlElement(name = "resourceType")
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType
	 *            the resourceType to set
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * @return the nodeName
	 */
	@XmlElement(name = "nodeName")
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName
	 *            the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
