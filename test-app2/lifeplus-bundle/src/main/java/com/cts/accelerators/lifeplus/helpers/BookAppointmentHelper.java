package com.cts.accelerators.lifeplus.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookAppointmentHelper {
/**
 * to insert appointment related data into db
 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookAppointmentHelper.class);
	JSONArray jSONArray = new JSONArray();
	Map<String, String> jSONObject = new HashMap<String, String>();

	public static JSONObject bookDocAppointment(
			SlingHttpServletRequest request, Connection con)
			throws JSONException {
		Statement stmt = null;
		String methodName = "bookDocAppointment";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject jObj = new JSONObject();

		// String json = "";
		String json = request.getParameter("jsonData");
		String member_id_patient = request.getParameter("member_id_patient");
		String member_id_doctor = request.getParameter("member_id_doctor");
		String member_id_hosp = request.getParameter("member_id_hosp");
		LOGGER.debug(" || " + methodName + " || json || " + json);
       
		JSONObject jsonData = new JSONObject(json);
		LOGGER.debug(" || " + methodName + " || JSONObject || " + jsonData);

		jObj = new JSONObject(json);

		LOGGER.debug(" || " + methodName + " || appointment_details || "
				+ jsonData.get("appointment_details"));
          // iterating through the json object 
		JSONArray appointmentDetails = (JSONArray) jsonData
				.get("appointment_details");
		JSONObject appointment = (JSONObject) appointmentDetails.get(0);
		LOGGER.debug(" || " + methodName + " || appointment || " + appointment);
		LOGGER.debug(" || " + methodName + " || appointment || "
				+ appointment.getString("reason"));
		String query = "INSERT INTO appointment (member_id_doctor,member_id_hosp,member_id_patient,reasonForVisit,Insurance,appointment_for) VALUES (?,?,?,?,?,?)";
		LOGGER.debug(" || " + methodName + " || query || "
				+ query);

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt;
		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, member_id_doctor);
			preparedStmt.setString(2, member_id_hosp);
			preparedStmt.setString(3, member_id_patient);
			preparedStmt.setString(4, appointment.getString("reason"));
			preparedStmt.setString(5, appointment.getString("insurance"));
			preparedStmt.setString(6, appointment.getString("appointmentFor"));
			preparedStmt.execute();
		} catch (SQLException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || " + e.getMessage());
		}
		
		String query2 = "INSERT INTO patient_dependent (name,gender,email,dependent_of) VALUES (?,?,?,?)";
		PreparedStatement preparedStmt1; 
		try {
			preparedStmt1 = con.prepareStatement(query2);
			preparedStmt1.setString(1, appointment.getString("flname"));
			preparedStmt1.setString(2, appointment.getString("gender"));
			preparedStmt1.setString(3, appointment.getString("pemail"));
			preparedStmt1.setString(4, member_id_patient);
			preparedStmt1.execute();
		} catch (SQLException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || " + e.getMessage());
		}
		/*
		 * try { stmt = con.createStatement(); ResultSet rs =
		 * stmt.executeQuery(query);
		 * 
		 * }
		 */
		// jObj.getJSONObject("appointment_details");

		return jsonData;
	}

}
