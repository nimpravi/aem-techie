package com.cts.accelerators.migration.services.dto;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

/**
 * ServiceRequest class encapsulating Report Generator parameters
 * 
 * @author 369565
 * 
 */

public class ReportGeneratorServiceRequest implements AcceleratorServiceRequest {

	// Required for Reporting Functions
	private String reportRootPath;
	private boolean loadDefault;
	private String reportType;

	// Required for Reporting Form
	private boolean viewReport;
	private String reportDate;
	private String reportDetailedType;
	private boolean specificReport;
	private String specificReportPath;
	private boolean completeMigrationReports;
	private String getHeaderString;

	/**
	 * @return the reportRootPath
	 */
	public String getReportRootPath() {
		return reportRootPath;
	}

	/**
	 * @param reportRootPath
	 *            the reportRootPath to set
	 */
	public void setReportRootPath(String reportRootPath) {
		this.reportRootPath = reportRootPath;
	}

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
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType
	 *            the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the viewReport
	 */
	public boolean isViewReport() {
		return viewReport;
	}

	/**
	 * @param viewReport
	 *            the viewReport to set
	 */
	public void setViewReport(boolean viewReport) {
		this.viewReport = viewReport;
	}

	/**
	 * @return the reportDate
	 */
	public String getReportDate() {
		return reportDate;
	}

	/**
	 * @param reportDate
	 *            the reportDate to set
	 */
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	/**
	 * @return the reportDetailedType
	 */
	public String getReportDetailedType() {
		return reportDetailedType;
	}

	/**
	 * @param reportDetailedType
	 *            the reportDetailedType to set
	 */
	public void setReportDetailedType(String reportDetailedType) {
		this.reportDetailedType = reportDetailedType;
	}

	/**
	 * @return the specificReport
	 */
	public boolean isSpecificReport() {
		return specificReport;
	}

	/**
	 * @param specificReport
	 *            the specificReport to set
	 */
	public void setSpecificReport(boolean specificReport) {
		this.specificReport = specificReport;
	}

	/**
	 * @return the specificReportPath
	 */
	public String getSpecificReportPath() {
		return specificReportPath;
	}

	/**
	 * @param specificReportPath
	 *            the specificReportPath to set
	 */
	public void setSpecificReportPath(String specificReportPath) {
		this.specificReportPath = specificReportPath;
	}

	/**
	 * @return the completeMigrationReports
	 */
	public boolean isCompleteMigrationReports() {
		return completeMigrationReports;
	}

	/**
	 * @param completeMigrationReports
	 *            the completeMigrationReports to set
	 */
	public void setCompleteMigrationReports(boolean completeMigrationReports) {
		this.completeMigrationReports = completeMigrationReports;
	}

	/**
	 * @return the getHeaderString
	 */
	public String getGetHeaderString() {
		return getHeaderString;
	}

	/**
	 * @param getHeaderString
	 *            the getHeaderString to set
	 */
	public void setGetHeaderString(String getHeaderString) {
		this.getHeaderString = getHeaderString;
	}

}
