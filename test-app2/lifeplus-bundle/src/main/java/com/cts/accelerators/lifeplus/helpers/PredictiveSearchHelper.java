package com.cts.accelerators.lifeplus.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Predictive Search Helper class is used for querying the Database and
 * fetch the field results for the predictive Search of the Generic Search
 * component 
 *
 * @author 406407
 * 
 */
public class PredictiveSearchHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PredictiveSearchHelper.class);
	JSONArray jSONArray = new JSONArray();
	Map<String, String> jSONObject = new HashMap<String, String>();
	
	/**
	 * This method will be used for querying the database and fetch the field results for the predictive Search.
	 * 
	 * @param - request and connection
	 * 
	 * @return - JsonObject of the query results
	 * */
	public static JSONObject PredictiveSearch(SlingHttpServletRequest request,
			Connection con) throws JSONException {
		String methodName = "PredictiveSearch";
		LOGGER.info(" || " + methodName + " || START");
		Statement stmt = null;

		String searchkey1 = (String) request.getParameter("searchkey");
		String searchkey2 = (String) request.getParameter("tableName");

		LOGGER.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>searchkey1:>>>>>>>>>>>>>>>>>>>>>>>"
				+ searchkey1);

		LOGGER.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>searchkey2:>>>>>>>>>>>>>>>>>>>>>>>"
				+ searchkey2);
		JSONObject jsonobj = new JSONObject();
		int i = 0;
		
		JSONObject jsonResponse = new JSONObject();
	
		String query = "Select" + " " + searchkey1 + " " + "from" + " "+ searchkey2;

		LOGGER.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Ist query>>>>>>>>>>>>>>>>>>>>>>>>>> :"
				+ query);


		if (null != searchkey1 && !searchkey1.isEmpty()) {
		
			query = query + ";";
			LOGGER.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Final Query>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> :"+ query);
		}
	

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			LOGGER.error("RS >>>>>>>>>>>>>>>>>>>>>>>>>>: " + rs);
			LOGGER.error("RS Size: " + rs.getFetchSize());

			while (rs.next()) {

				jsonobj = new JSONObject();
				jsonobj.put("text", rs.getString(searchkey1));
				jsonobj.put("value", rs.getString(searchkey1));
				LOGGER.error("jsonoobj>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + jsonobj);
	            jsonResponse.put("value_" + i, jsonobj);
	            i++;

				
			}
		} catch (SQLException e) {
			LOGGER.error(" || " + methodName + " || SQL EXCEPTION || ",e);
		}

		LOGGER.info(" || " + methodName + " || jsonResponse || " + jsonResponse);
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}


}


