package com.cts.accelerators.lifeplus.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleTimeHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ScheduleTimeHelper.class);

	public static JSONObject appointmentScheduleTiming(
			SlingHttpServletRequest request, Connection con)
			throws JSONException {
		String methodName = "appointmentScheduleTiming";
		LOGGER.info("|| " + methodName + " || request || " + request);
		String doctor_member_id = request.getParameter("doc_id");
		LOGGER.info("|| " + methodName + " || doctor_member_id || "
				+ doctor_member_id);

		String date = request.getParameter("date");
		LOGGER.info("|| " + methodName + " || date || " + date);

		String query = "select * from doctor_hosp where member_id_doctor='"
				+ doctor_member_id + "' ";
		LOGGER.info("|| " + methodName + " || query || " + query);
		String hospital_member_id = null;
		JSONObject json = new JSONObject();
		JSONObject jsonResponse = new JSONObject();
		try {
			if (StringUtils.isEmpty(date)) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				date = dateFormat.format(cal.getTime());
			} else {
				DateFormat srcDf = new SimpleDateFormat("MM/dd/yyyy");
				Date dateForm = srcDf.parse(date);
				DateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");
				date = destDf.format(dateForm);
				LOGGER.debug("" + date);

			}
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			JSONArray jsonArr = new JSONArray();
			while (rs.next()) {

				LOGGER.info("member_id" + rs.getString("member_id_hosp"));

				hospital_member_id = rs.getString("member_id_hosp");
				LOGGER.info("hospital_member_id:::::::" + hospital_member_id);

				LOGGER.info("|| " + methodName + " || hospital_member_id || "
						+ hospital_member_id);
				json = getAppointmentDetails(con, hospital_member_id,
						doctor_member_id, date);
				LOGGER.info("|| " + methodName + " || availibility || " + json);
				jsonArr.put(json);
				jsonResponse.put("hospitalSlots", jsonArr);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		LOGGER.info("jsonResponse:::::::" + jsonResponse);

		return jsonResponse;

	}

	private static JSONObject getAppointmentDetails(Connection con,
			String hospital_member_id, String doctor_member_id, String date) {

		JSONObject job = new JSONObject();
		String methodName = "getAppointmentDetails";
		JSONObject allSlots = new JSONObject();
		allSlots = getAllSlots(con, hospital_member_id, doctor_member_id, date);
		job = getBookedAppointmentList(con, hospital_member_id,
				doctor_member_id, date, allSlots);

		LOGGER.info("|| " + methodName + " || bookedAppointmentList || " + job);
		LOGGER.info("|| " + methodName + " || allSlots || " + allSlots);

		return job;
	}

	private static JSONObject getAllSlots(Connection con,
			String hospital_member_id, String doctor_member_id, String date) {

		String query = "select s.time_from, s.time_to,s.appoint_duration,s.applicable_days,s.not_avail_from,s.not_avail_to,s.days from schedule_time as s where member_id_doctor='"
				+ doctor_member_id
				+ "' and member_id_hosp='"
				+ hospital_member_id + "' ";
		String methodName = "getAllSlots";
		LOGGER.info("|| " + methodName + " || query || " + query);

		JSONObject allSlots = new JSONObject();
		JSONArray slots = new JSONArray();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			LOGGER.info("In try::::::::::::");
			while (rs.next()) {
				Date time_from = rs.getTime("time_from");
				Date time_to = rs.getTime("time_to");
				Integer appoint_duration = rs.getInt("appoint_duration");
				LOGGER.info(" time_to_appointment::::::   " + appoint_duration);
				Date applicable_days = rs.getDate("applicable_days");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String applicable_date = sdf.format(applicable_days);
				try {
					applicable_days = sdf.parse(applicable_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LOGGER.info(" time_to:   " + applicable_days);
				Date not_avail_from = rs.getTime("not_avail_from");
				LOGGER.info(" time_to:   " + not_avail_from);
				Date not_avail_to = rs.getTime("not_avail_to");
				LOGGER.info(" not_avail_to:   " + not_avail_to);
				String days = rs.getString("days");
				LOGGER.info(" days:   " + days);
				LOGGER.info(" date:::::::::   " + date);
				Date appointmentDate = convertStringToDate(date);
				LOGGER.info(" appointmentDate:   " + appointmentDate);
				if (applicable_days.after(appointmentDate)) {
					if (days.equalsIgnoreCase("Mon")) {
						LOGGER.info("Monday");
						slots = bookingSlots(time_from, time_to,
								appoint_duration, not_avail_from, not_avail_to,
								allSlots, days);
						allSlots.put("Mon", slots);
					}
					if (days.equalsIgnoreCase("Tue")) {
						LOGGER.info("Tue");
						slots = bookingSlots(time_from, time_to,
								appoint_duration, not_avail_from, not_avail_to,
								allSlots, days);
						allSlots.put("Tue", slots);
					}
					if (days.equalsIgnoreCase("Wed")) {
						LOGGER.info("Wed");
						slots = bookingSlots(time_from, time_to,
								appoint_duration, not_avail_from, not_avail_to,
								allSlots, days);
						allSlots.put("Wed", slots);
					}
					if (days.equalsIgnoreCase("Thur")) {
						LOGGER.info("Thur");
						slots = bookingSlots(time_from, time_to,
								appoint_duration, not_avail_from, not_avail_to,
								allSlots, days);
						allSlots.put("Thur", slots);
					}
					if (days.equalsIgnoreCase("Fri")) {
						LOGGER.info("Fri");
						slots = bookingSlots(time_from, time_to,
								appoint_duration, not_avail_from, not_avail_to,
								allSlots, days);
						allSlots.put("Fri", slots);
					}
					if (days.equalsIgnoreCase("Sat")) {

						LOGGER.info("Sat");
						slots = bookingSlots(time_from, time_to,
								appoint_duration, not_avail_from, not_avail_to,
								allSlots, days);
						allSlots.put("Sat", slots);
					}
					if (days.equalsIgnoreCase("Sun")) {
						LOGGER.info("Sun");
						slots = bookingSlots(time_from, time_to,
								appoint_duration, not_avail_from, not_avail_to,
								allSlots, days);
						allSlots.put("Sun", slots);
					}

					LOGGER.info("if failed not monday top sunday");

				}
				LOGGER.info("if out ");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allSlots;
	}

	private static Date convertStringToDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = null;
		try {
			dateFormat = format.parse(date);
			LOGGER.info(" dateFormat:   " + dateFormat);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return dateFormat;

	}

	private static JSONArray bookingSlots(Date time_from, Date time_to,
			Integer appoint_duration, Date not_avail_from, Date not_avail_to,
			JSONObject allSlots, String days) {

		LOGGER.info("time_from" + time_from);
		LOGGER.info("time_to" + time_to);
		LOGGER.info("appoint_duration" + appoint_duration);
		LOGGER.info("not_avail_from" + not_avail_from);
		LOGGER.info("not_avail_to" + not_avail_to);
		LOGGER.info("allSlots" + allSlots);
		LOGGER.info("days" + days);
		LOGGER.info("444444444444");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		// List<String> time_slot = new ArrayList<String>();

		JSONArray time_slot = new JSONArray();
		Iterator<?> slotKeys = allSlots.keys();
		while (slotKeys.hasNext()) {
			String key = (String) slotKeys.next();
			if (key.equalsIgnoreCase(days)) {
				try {
					LOGGER.info("inside firsttry ********" + allSlots.get(key));
					// time_slot.put(allSlots.get(key));

					time_slot = allSlots.getJSONArray(key);
					// finalArray=time_slot;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(time_from);
		String addTime = df.format(cal1.getTime());
		time_slot.put(addTime);
		try {

			while (time_to.after(time_from)) {
				cal1.add(Calendar.MINUTE, appoint_duration);
				addTime = df.format(cal1.getTime());

				time_from = df.parse(addTime);
				time_slot.put(addTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time_slot;
	}

	/**
	 * @param con
	 * @param hospital_member_id
	 * @param doctor_member_id
	 * @param date
	 * @param stmt
	 * @param appointTime
	 * @param allSlots
	 * @param methodName
	 */
	private static JSONObject getBookedAppointmentList(Connection con,
			String hospital_member_id, String doctor_member_id, String date,
			JSONObject allSlots) {
		String methodName = "getBookedAppointmentList";

		JSONObject bookeUnbookeddAppointmentList = new JSONObject();
		JSONObject json = new JSONObject();

		JSONArray bookedAppointmentList = new JSONArray();
		String appointTime = null;
		Calendar cal1 = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		int count = 1;

		try {
			while (count <= 7) {

				bookedAppointmentList = getBookedAppointmentSlots(con,
						hospital_member_id, doctor_member_id, date, appointTime);

				Date date1 = dateFormat.parse(date);
				cal1.setTime(date1);
				SimpleDateFormat sdf = new SimpleDateFormat("EEE");
				String dayString = sdf.format(date1);
				LOGGER.info(" time_to:   " + dayString + methodName);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM");
				String daySt = sdf1.format(date1);
				count++;

				String dayWithDate = dayString + " " + daySt;
				JSONObject slots = new JSONObject();
				Iterator<?> days = allSlots.keys();
				while (days.hasNext()) {

					String day = (String) days.next();
					if (day.equalsIgnoreCase(dayString)) {
						JSONArray allslotsinDay = allSlots.getJSONArray(day);
						for (int n = 0; n < allslotsinDay.length(); n++) {
							String name = (String) allslotsinDay.get(n);
							if (bookedAppointmentList.equals(name)) {
								name = convertIntoAMPM(name);
								slots.put(name, "Not Available");
							} else {
								name = convertIntoAMPM(name);
								slots.put(name, "Available");
							}
						}
					}
					if ((slots.length() <= 0 && day.equalsIgnoreCase(dayString))
							|| (!allSlots.has(dayString))) {

						LOGGER.info("dayyyyyyyyyyyyy" + day);
						LOGGER.info("dayyyyyyyyyyyyy" + dayWithDate);
						slots.put("DR Slots not Available for " + dayWithDate,
								"Not Available");

					}
					bookeUnbookeddAppointmentList.put(dayWithDate, slots);
				}

				json.put("hospitalId", hospital_member_id);
				json.put("slots", bookeUnbookeddAppointmentList);

				SimpleDateFormat formattr = new SimpleDateFormat("yyyy-MM-dd");
				Date dateee;

				dateee = formattr.parse(date);
				Calendar c19 = Calendar.getInstance();
				c19.setTime(dateee);

				c19.add(Calendar.DATE, 1);
				dateee = c19.getTime();
				date = formattr.format(dateee);
				LOGGER.info("&&&&&&&&& " + date);

			}

		} catch (ParseException e) {

			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return json;
	}

	private static String convertIntoAMPM(String name) {

		String[] s1 = name.split(":");
		int a = Integer.parseInt(s1[0]);
		String ap = null;
		if (a > 12) {
			a = a - 12;
			ap = "PM";
		} else {
			ap = "AM";
		}
		String time2 = Integer.toString(a);
		StringBuffer sb = new StringBuffer();
		sb.append(time2).append(":").append(s1[1]).append(":").append(ap);
		String output = sb.toString();

		return output;
	}

	/**
	 * @param con
	 * @param hospital_member_id
	 * @param doctor_member_id
	 * @param date
	 * @param appointTime
	 * @param methodName
	 * @param bookedAppointmentList
	 * @return
	 */
	private static JSONArray getBookedAppointmentSlots(Connection con,
			String hospital_member_id, String doctor_member_id, String date,
			String appointTime) {
		String methodName = "getBookedAppointmentSlots";
		// List<String> bookedAppointmentList = new ArrayList<String>();
		JSONArray bookedAppointmentList = new JSONArray();
		try {
			String query = "select a.appoint_time from appointment as a where member_id_doctor=' "
					+ doctor_member_id
					+ "' and appoint_date='"
					+ date
					+ "' and member_id_hosp='" + hospital_member_id + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				appointTime = rs.getString("appoint_time");
				LOGGER.info("|| " + methodName + " || request || "
						+ appointTime);
				bookedAppointmentList.put(appointTime);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return bookedAppointmentList;
	}
}
