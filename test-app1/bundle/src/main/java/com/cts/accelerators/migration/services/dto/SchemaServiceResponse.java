package com.cts.accelerators.migration.services.dto;

import org.apache.sling.commons.json.JSONObject;

import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
/**
 * 
 * @author 369565
 *
 */

public class SchemaServiceResponse implements AcceleratorServiceResponse {
	
	private JSONObject jsonResponse;

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
}
