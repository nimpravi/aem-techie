package com.cts.accelerators.lifeplus.helpers;

import java.sql.Connection;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleMapHelper {

	public static final String CLASS_NAME = GoogleMapHelper.class.getName();
	public static final Logger LOGGER = LoggerFactory
			.getLogger(GoogleMapHelper.class);

	public static JSONObject getGoogleMapsData(SlingHttpServletRequest request, Connection con) {
		String methodName = "getGoogleMapsData";
		LOGGER.info(" || " + methodName + " || START");
		
		JSONObject jsonResponse = new JSONObject();
		String jsonString = request.getParameter("json"); 
		LOGGER.debug(" || " + methodName + " || jsonString || "+jsonString);
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			JSONObject docDetails = new JSONObject();
			JSONArray docDetailsArray = new JSONArray();
			JSONObject obj = new JSONObject();
			JSONArray jsonArray = (JSONArray) jsonObj.get("Values");
			for(int i = 0; i < jsonArray.length(); i++) {
				obj = (JSONObject) jsonArray.get(i);
				docDetails = new JSONObject();
				docDetails.put("name", obj.get("name"));
				docDetails.put("education", obj.get("education"));
				docDetails.put("country", obj.get("country"));
				docDetails.put("zipcode", obj.get("zip"));
				docDetails.put("address_line1", obj.get("address_line1"));
				docDetails.put("address_line2", obj.get("address_line2"));
				docDetails.put("city", obj.get("city"));
				docDetails.put("country", obj.get("country"));
				docDetails.put("speciality", obj.get("speciality"));
				docDetails.put("telephone_no", obj.get("telephone_no"));
				docDetails.put("rating", obj.get("rating"));
				docDetails.put("image_path", obj.get("image_path"));
				docDetails.put("doctor_id", obj.get("doctor_id"));
				docDetails.put("date", obj.get("date"));
				docDetailsArray.put(docDetails);
			}
			jsonResponse.put("doctor_details", docDetailsArray);
			jsonResponse.put("source_zip",jsonObj.get("source_zip"));
			LOGGER.debug(" || " + methodName + " || jsonResponse || "+jsonResponse);
			LOGGER.debug(" || " + methodName + " || jsonResponse || "+jsonResponse.toString());
		} catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION OCCURED || " + e.getMessage());
		}
		
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;	
	}
	
}
