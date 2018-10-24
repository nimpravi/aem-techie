package com.cts.accelerators.lifeplus.services.dto;

import org.apache.sling.commons.json.JSONObject;

import com.cts.accelerators.lifeplus.core.dto.PersonalizationResponse;

public class ContentRetrieverResponse implements PersonalizationResponse {
	
	JSONObject jsonResponse;

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
