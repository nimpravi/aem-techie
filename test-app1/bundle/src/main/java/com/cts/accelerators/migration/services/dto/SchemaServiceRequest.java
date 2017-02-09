package com.cts.accelerators.migration.services.dto;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

/**
 * ServiceRequest class encapsulating XSLService parameters
 * 
 * @author 369565
 * 
 */
public class SchemaServiceRequest implements AcceleratorServiceRequest {

	private boolean loadDefault;
	// either SCHEMA or XSL
	private String requestType;

	private String storagePath;

	private String moveTo;

	private String nameOfClass;

	private Object object;

	/**
	 * @return the loadDefault
	 */
	public boolean isLoadDefault() {
		return loadDefault;
	}

	/**
	 * @param loadDefault
	 *            the loadDefault to set
	 */
	public void setLoadDefault(boolean loadDefault) {
		this.loadDefault = loadDefault;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType
	 *            the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the storagePath
	 */
	public String getStoragePath() {
		return storagePath;
	}

	/**
	 * @param storagePath
	 *            the storagePath to set
	 */
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	/**
	 * @return the moveTo
	 */
	public String getMoveTo() {
		return moveTo;
	}

	/**
	 * @param moveTo
	 *            the moveTo to set
	 */
	public void setMoveTo(String moveTo) {
		this.moveTo = moveTo;
	}

	/**
	 * @return the nameOfClass
	 */
	public String getNameOfClass() {
		return nameOfClass;
	}

	/**
	 * @param nameOfClass
	 *            the nameOfClass to set
	 */
	public void setNameOfClass(String nameOfClass) {
		this.nameOfClass = nameOfClass;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
