package com.cts.accelerators.migration.services.dto;

import java.util.HashMap;

import org.apache.sling.commons.json.JSONObject;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

/**
 * ServiceRequest class encapsulating Content importer parameters
 * 
 * @author 369565
 * 
 */

public class ContentImporterServiceRequest implements AcceleratorServiceRequest {

	private boolean completeMigration;
	private boolean loadDefault;
	private boolean enableDuplication;
	private boolean enableReplication;
	private boolean disableUpdate;
	private String sourceRootPath;
	private String fileDestinationRootPath;
	private String importOrder;
	private String pageStoragePath;
	private String damStoragePath;
	private String taxonomyStoragePath;
	private String relpicationAgent;
	private HashMap<String, String> reportRootPaths;
	private DamUploadServiceRequest damRequest;
	private String reportFilePath;
	private JSONObject jsonResponse;
	private String transformationType;

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
	 * @return the enableDuplication
	 */
	public boolean isEnableDuplication() {
		return enableDuplication;
	}
	/**
	 * @param enableDuplication the enableDuplication to set
	 */
	public void setEnableDuplication(boolean enableDuplication) {
		this.enableDuplication = enableDuplication;
	}
	/**
	 * @return the enableReplication
	 */
	public boolean isEnableReplication() {
		return enableReplication;
	}
	/**
	 * @param enableReplication the enableReplication to set
	 */
	public void setEnableReplication(boolean enableReplication) {
		this.enableReplication = enableReplication;
	}
	/**
	 * @return the disableUpdate
	 */
	public boolean isDisableUpdate() {
		return disableUpdate;
	}
	/**
	 * @param disableUpdate the disableUpdate to set
	 */
	public void setDisableUpdate(boolean disableUpdate) {
		this.disableUpdate = disableUpdate;
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
	 * @return the fileDestinationRootPath
	 */
	public String getFileDestinationRootPath() {
		return fileDestinationRootPath;
	}
	/**
	 * @param fileDestinationRootPath the fileDestinationRootPath to set
	 */
	public void setFileDestinationRootPath(String fileDestinationRootPath) {
		this.fileDestinationRootPath = fileDestinationRootPath;
	}
	/**
	 * @return the importOrder
	 */
	public String getImportOrder() {
		return importOrder;
	}
	/**
	 * @param importOrder the importOrder to set
	 */
	public void setImportOrder(String importOrder) {
		this.importOrder = importOrder;
	}
	/**
	 * @return the pageStoragePath
	 */
	public String getPageStoragePath() {
		return pageStoragePath;
	}
	/**
	 * @param pageStoragePath the pageStoragePath to set
	 */
	public void setPageStoragePath(String pageStoragePath) {
		this.pageStoragePath = pageStoragePath;
	}
	/**
	 * @return the damStoragePath
	 */
	public String getDamStoragePath() {
		return damStoragePath;
	}
	/**
	 * @param damStoragePath the damStoragePath to set
	 */
	public void setDamStoragePath(String damStoragePath) {
		this.damStoragePath = damStoragePath;
	}
	/**
	 * @return the taxonomyStoragePath
	 */
	public String getTaxonomyStoragePath() {
		return taxonomyStoragePath;
	}
	/**
	 * @param taxonomyStoragePath the tagStoragePath to set
	 */
	public void setTaxonomyStoragePath(String taxonomyStoragePath) {
		this.taxonomyStoragePath = taxonomyStoragePath;
	}
	/**
	 * @return the relpicationAgent
	 */
	public String getRelpicationAgent() {
		return relpicationAgent;
	}
	/**
	 * @param relpicationAgent the relpicationAgent to set
	 */
	public void setRelpicationAgent(String relpicationAgent) {
		this.relpicationAgent = relpicationAgent;
	}
	/**
	 * @return the reportRootPaths
	 */
	public HashMap<String, String> getReportRootPaths() {
		return reportRootPaths;
	}
	/**
	 * @param reportRootPaths the reportRootPaths to set
	 */
	public void setReportRootPaths(HashMap<String, String> reportRootPaths) {
		this.reportRootPaths = reportRootPaths;
	}
	/**
	 * @return the damRequest
	 */
	public DamUploadServiceRequest getDamRequest() {
		return damRequest;
	}
	/**
	 * @param damRequest the damRequest to set
	 */
	public void setDamRequest(DamUploadServiceRequest damRequest) {
		this.damRequest = damRequest;
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
	 * @return the jsonResponse
	 */
	public JSONObject getJsonResponse() {
		return jsonResponse;
	}
	/**
	 * @param jsonResponse the jsonResponse to set
	 */
	public void setJsonResponse(JSONObject jsonResponse) {
		this.jsonResponse = jsonResponse;
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
}
