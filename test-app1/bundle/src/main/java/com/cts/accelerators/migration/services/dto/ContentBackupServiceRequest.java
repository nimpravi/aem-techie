package com.cts.accelerators.migration.services.dto;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

/**
 * ServiceRequest class encapsulating Content Reader parameters
 * 
 * @author
 * 
 */

public class ContentBackupServiceRequest implements AcceleratorServiceRequest {

	private boolean loadDefault;
	private boolean completeMigration;
	private String moveTo;
	private String sourceRootPath;
	private String destinationRootPath;
	private String reportFilePath;

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
	 * @return the completeMigration
	 */
	public boolean isCompleteMigration() {
		return completeMigration;
	}

	/**
	 * @param completeMigration
	 *            the completeMigration to set
	 */
	public void setCompleteMigration(boolean completeMigration) {
		this.completeMigration = completeMigration;
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
	 * @return the sourceRootPath
	 */
	public String getSourceRootPath() {
		return sourceRootPath;
	}

	/**
	 * @param sourceRootPath
	 *            the sourceRootPath to set
	 */
	public void setSourceRootPath(String sourceRootPath) {
		this.sourceRootPath = sourceRootPath;
	}

	/**
	 * @return the destinationRootPath
	 */
	public String getDestinationRootPath() {
		return destinationRootPath;
	}

	/**
	 * @param destinationRootPath
	 *            the destinationRootPath to set
	 */
	public void setDestinationRootPath(String destinationRootPath) {
		this.destinationRootPath = destinationRootPath;
	}

	/**
	 * @return the reportFilePath
	 */
	public String getReportFilePath() {
		return reportFilePath;
	}

	/**
	 * @param reportFilePath
	 *            the reportFilePath to set
	 */
	public void setReportFilePath(String reportFilePath) {
		this.reportFilePath = reportFilePath;
	}
}
