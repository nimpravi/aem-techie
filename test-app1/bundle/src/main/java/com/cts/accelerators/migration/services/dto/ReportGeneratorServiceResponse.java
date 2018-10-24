package com.cts.accelerators.migration.services.dto;

import java.util.Map;

import org.apache.sling.commons.json.JSONObject;

import com.cts.accelerators.core.dto.AcceleratorServiceResponse;

/**
 * 
 * @author 369565
 * 
 */

public class ReportGeneratorServiceResponse implements
		AcceleratorServiceResponse {

	private JSONObject reportJsonResponse;
	private Map<String, String> reportFilePaths;

	/**
	 * @return the reportJsonResponse
	 */
	public JSONObject getReportJsonResponse() {
		return reportJsonResponse;
	}

	/**
	 * @param reportJsonResponse
	 *            the reportJsonResponse to set
	 */
	public void setReportJsonResponse(JSONObject reportJsonResponse) {
		this.reportJsonResponse = reportJsonResponse;
	}

	/**
	 * @return the reportFilePaths
	 */
	public Map<String, String> getReportFilePaths() {
		return reportFilePaths;
	}

	/**
	 * @param reportFilePaths
	 *            the reportFilePaths to set
	 */
	public void setReportFilePaths(Map<String, String> reportFilePaths) {
		this.reportFilePaths = reportFilePaths;
	}
}