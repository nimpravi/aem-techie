package com.cts.accelerators.migration.services.dto;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

/**
 * ServiceRequest class encapsulating DAMbulkupload parameters
 * 
 * @author 300311
 * 
 */
public class DamUploadServiceRequest implements AcceleratorServiceRequest {

	private boolean loadDefault;
	private boolean completeMigration;
	private boolean enableProxy;
	private String sourceRootPath;
	private String cqRootPath;
	private String fileDestinationRootPath;
	private String localhost;
	private String port;
	private String username;
	private String password;
	private int proxyPort;
	private String proxyHost;
	private String proxyUsername;
	private String proxyPassword;
	private String reportFilePath;
	private String transformationType;
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
	 * @return the enableProxy
	 */
	public boolean isEnableProxy() {
		return enableProxy;
	}
	/**
	 * @param enableProxy the enableProxy to set
	 */
	public void setEnableProxy(boolean enableProxy) {
		this.enableProxy = enableProxy;
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
	 * @return the cqRootPath
	 */
	public String getCqRootPath() {
		return cqRootPath;
	}
	/**
	 * @param cqRootPath the cqRootPath to set
	 */
	public void setCqRootPath(String cqRootPath) {
		this.cqRootPath = cqRootPath;
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
	 * @return the localhost
	 */
	public String getLocalhost() {
		return localhost;
	}
	/**
	 * @param localhost the localhost to set
	 */
	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}
	/**
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}
	/**
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	/**
	 * @return the proxyUsername
	 */
	public String getProxyUsername() {
		return proxyUsername;
	}
	/**
	 * @param proxyUsername the proxyUsername to set
	 */
	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}
	/**
	 * @return the proxyPassword
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}
	/**
	 * @param proxyPassword the proxyPassword to set
	 */
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
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
