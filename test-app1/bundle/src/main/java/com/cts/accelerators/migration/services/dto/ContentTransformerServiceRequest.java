package com.cts.accelerators.migration.services.dto;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

/**
 * ServiceRequest class encapsulating Content transformer parameters
 * 
 * @author 369565
 * 
 */
public class ContentTransformerServiceRequest implements
		AcceleratorServiceRequest {

	private boolean loadDefault;
	private boolean completeMigration;
	private String sourceRootPath;
	private String destinationRootPath;
	private String mappingFilePath;
	private String transformationOrder;
	private String reportFilePath;
	private String contentType;
	private String transformationType;
	private String fileSize;
	/**
	 * @return the loadDefault
	 */
	public boolean isLoadDefault() {
		return loadDefault;
	}
	/**
	 * @param loadDefault the loadDefault to set
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
	 * @param completeMigration the completeMigration to set
	 */
	public void setCompleteMigration(boolean completeMigration) {
		this.completeMigration = completeMigration;
	}
	/**
	 * @return the sourceRootPath
	 */
	public String getSourceRootPath() {
		return sourceRootPath;
	}
	/**
	 * @param sourceRootPath the sourceRootPath to set
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
	 * @param destinationRootPath the destinationRootPath to set
	 */
	public void setDestinationRootPath(String destinationRootPath) {
		this.destinationRootPath = destinationRootPath;
	}
	/**
	 * @return the mappingFilePath
	 */
	public String getMappingFilePath() {
		return mappingFilePath;
	}
	/**
	 * @param mappingFilePath the mappingFilePath to set
	 */
	public void setMappingFilePath(String mappingFilePath) {
		this.mappingFilePath = mappingFilePath;
	}
	/**
	 * @return the transformationOrder
	 */
	public String getTransformationOrder() {
		return transformationOrder;
	}
	/**
	 * @param transformationOrder the transformationOrder to set
	 */
	public void setTransformationOrder(String transformationOrder) {
		this.transformationOrder = transformationOrder;
	}
	/**
	 * @return the reportFilePath
	 */
	public String getReportFilePath() {
		return reportFilePath;
	}
	/**
	 * @param reportFilePath the reportFilePath to set
	 */
	public void setReportFilePath(String reportFilePath) {
		this.reportFilePath = reportFilePath;
	}
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the transformationType
	 */
	public String getTransformationType() {
		return transformationType;
	}
	/**
	 * @param transformationType the transformationType to set
	 */
	public void setTransformationType(String transformationType) {
		this.transformationType = transformationType;
	}
	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
}
