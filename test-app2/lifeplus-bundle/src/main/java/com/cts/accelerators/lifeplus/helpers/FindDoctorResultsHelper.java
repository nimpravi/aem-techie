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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindDoctorResultsHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FindDoctorResultsHelper.class);
	JSONArray jSONArray = new JSONArray();
	Map<String, String> jSONObject = new HashMap<String, String>();

	public static JSONObject SearchDoctors(SlingHttpServletRequest request,
			Connection con) throws JSONException {
		String methodName = "SearchDoctors";
		LOGGER.info(" || " + methodName + " || START");
		String doctor_name = (String) request.getParameter("doctor_name");
		String speciality = (String) request.getParameter("speciality");
		String location = (String) request.getParameter("location");
		String date = (String) request.getParameter("date");
		JSONArray jsonArr = new JSONArray();
		if (StringUtils.isEmpty(date)) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			date = dateFormat.format(cal.getTime());
		} else {
			try {
				DateFormat srcDf = new SimpleDateFormat("MM/dd/yyyy");
				Date dateForm = srcDf.parse(date);
				DateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");
				date = destDf.format(dateForm);
				LOGGER.debug("" + date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		LOGGER.error("searchhhhhhhhhhhhh:" + doctor_name);
		JSONObject jsonResponse = new JSONObject();

		if (!StringUtils.isEmpty(doctor_name)
				&& StringUtils.isEmpty(speciality)
				&& StringUtils.isEmpty(location)) {
			String searchKey = "doctor";
			String getMember_id_query = "select * from doctor where name LIKE '"
					+ doctor_name + "%'";
			jsonArr = getDoctor_details(getMember_id_query, con, searchKey,
					date, doctor_name);
			jsonResponse.put("Values", jsonArr);
		}

		else if ((!StringUtils.isEmpty(speciality)
				&& StringUtils.isEmpty(doctor_name) && StringUtils
					.isEmpty(location))
				|| (!StringUtils.isEmpty(speciality)
						&& (!StringUtils.isEmpty(doctor_name)) && StringUtils
							.isEmpty(location))) {

			String speciality_id_query = "select speciality_id from speciality where name ='"
					+ speciality + "'";

			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(speciality_id_query);
				while (rs.next()) {
					String speciality_id = rs.getString("speciality_id");
					jsonArr = fetchDoctorsSpeciality(speciality_id, con, date,
							speciality, doctor_name, location);
					JSONArray jsonArrFinal = new JSONArray();
					for (int n = 0; n < jsonArr.length(); n++) {
						JSONObject object = new JSONObject();
						object = jsonArr.getJSONObject(n);
						LOGGER.info(" || " + " || jsonResponse || " + object);
						jsonArrFinal.put(object);
					}
					if (jsonArrFinal.length() <= 0) {
						jsonArrFinal.put("NO Records Found");
					}
					jsonResponse.put("Values", jsonArrFinal);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		else if ((!StringUtils.isEmpty(location)
				&& StringUtils.isEmpty(speciality) && StringUtils
					.isEmpty(doctor_name))
				|| (!StringUtils.isEmpty(location)
						&& StringUtils.isEmpty(speciality) && !StringUtils
							.isEmpty(doctor_name))) {
			String location_query = " select * from hospital where city like '%"
					+ location
					+ "%' || state like '%"
					+ location
					+ "%' || country like '%"
					+ location
					+ "%' || zip like '%"
					+ location + "%'";
			JSONArray jsonArrFinal = new JSONArray();
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(location_query);
				while (rs.next()) {
					String hospital_id = rs.getString("member_id");
					jsonArr = fetchDoctorsDoctorWithLocation(hospital_id, con,
							date, location, doctor_name);

					for (int n = 0; n < jsonArr.length(); n++) {
						JSONObject object = new JSONObject();
						object = jsonArr.getJSONObject(n);
						LOGGER.info(" || " + " || jsonResponse || " + object);
						jsonArrFinal.put(object);
					}

					jsonResponse.put("Values", jsonArrFinal);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (((!StringUtils.isEmpty(speciality))
				&& (!StringUtils.isEmpty(location)) && StringUtils
					.isEmpty(doctor_name))
				|| ((!StringUtils.isEmpty(speciality))
						&& (!StringUtils.isEmpty(location)) && !StringUtils
							.isEmpty(doctor_name))) {
			String getMember_id_query = "";
			String location_speciality_query = "select ds.member_id doctor_id,h.member_id hosp_member_id from doc_speciality ds,hospital h where ((select s.speciality_id from speciality s where s.name='"
					+ speciality
					+ "')=ds.speciality_id and address_line1 like '%"
					+ location
					+ "%' or address_line2 like '%"
					+ location
					+ "%' OR city like '%"
					+ location
					+ "%' OR state like '%"
					+ location
					+ "%' OR country like '%"
					+ location
					+ "%' OR zip like '%" + location + "%')";
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(location_speciality_query);
				JSONArray jsonArrFinal = new JSONArray();
				while (rs.next()) {
					String doctor_id = rs.getString("doctor_id");
					String hospital_id = rs.getString("hosp_member_id");
					if (StringUtils.isEmpty(doctor_name)) {
						getMember_id_query = "select * from doctor where member_id='"
								+ doctor_id + "'";
					} else {
						getMember_id_query = "select * from doctor where member_id='"
								+ doctor_id
								+ "' and name ='"
								+ doctor_name
								+ "'";
					}

					jsonArr = getDoctor_details(getMember_id_query, con,
							"location", date, hospital_id);

					for (int n = 0; n < jsonArr.length(); n++) {
						JSONObject object = new JSONObject();
						object = jsonArr.getJSONObject(n);
						LOGGER.info(" || " + " || jsonResponse || " + object);
						jsonArrFinal.put(object);
					}

					jsonResponse.put("Values", jsonArrFinal);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		jsonResponse
				.put("coloumnName",
						"Doctor's Availability,Practice Name,Expertise,Language/s Known,Education,Professional");
		jsonResponse.put("source_zip", "");
		LOGGER.info(" || " + methodName + " || jsonResponse || " + jsonResponse);
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	private static JSONArray fetchDoctorsDoctorWithLocation(String hospital_id,
			Connection con, String date, String location, String doctor_name) {
		String doctor_id_query = "select member_id_doctor from doctor_hosp where member_id_hosp='"
				+ hospital_id + "'";
		String searchKey = "location";
		String query = "";
		Statement stmt;
		JSONArray jsonArrFinal = new JSONArray();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(doctor_id_query);
			while (rs.next()) {
				String doctor_id = rs.getString("member_id_doctor");
				if (StringUtils.isEmpty(doctor_name)) {
					query = "select * from doctor where member_id='"
							+ doctor_id + "'";
				} else {
					query = "select * from doctor where member_id='"
							+ doctor_id + "' and name ='" + doctor_name + "'";
				}
				JSONArray jsonArr = new JSONArray();
				jsonArr = getDoctor_details(query, con, searchKey, date,
						hospital_id);

				JSONObject object = new JSONObject();
				for (int n = 0; n < jsonArr.length(); n++) {
					object = jsonArr.getJSONObject(n);
					LOGGER.info(" || " + " || jsonResponse || " + object);
					jsonArrFinal.put(object);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonArrFinal;
	}

	private static JSONArray fetchDoctorsSpeciality(String speciality_id,
			Connection con, String date, String speciality, String doctor_name,
			String location) {
		String doctor_id_query = "select member_id from doc_speciality where speciality_id ='"
				+ speciality_id + "'";
		JSONArray jsonArr = new JSONArray();
		JSONArray jsonArrFinal = new JSONArray();
		Statement stmt;
		String searchKey = "speciality";
		String doctor_detail_query = "";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(doctor_id_query);
			while (rs.next()) {
				JSONObject object = new JSONObject();
				String doctor_id = rs.getString("member_id");

				if (!StringUtils.isEmpty(doctor_name)) {
					doctor_detail_query = "select * from doctor where member_id="
							+ doctor_id
							+ " && name like '%"
							+ doctor_name
							+ "%'";
				}

				else {
					doctor_detail_query = "select * from doctor where member_id='"
							+ doctor_id + "'";
				}
				jsonArr = getDoctor_details(doctor_detail_query, con,
						searchKey, date, speciality);

				for (int n = 0; n < jsonArr.length(); n++) {
					object = jsonArr.getJSONObject(n);
					LOGGER.info(" || " + " || jsonResponse || " + object);
					jsonArrFinal.put(object);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArrFinal;
	}

	private static JSONArray getDoctor_details(String query, Connection con,
			String searchkey, String date, String searchValue) {
		JSONObject jsonobj = new JSONObject();
		JSONArray jsonArr = new JSONArray();

		String member_id = null;
		String name = null;
		String gender = null;
		String rating = null;
		String image_path = null;
		List<String> speciality = new ArrayList<String>();
		List<String> education = new ArrayList<String>();
		List<String> language = new ArrayList<String>();
		List<String> professionalMem = new ArrayList<String>();
		HashMap<String, HashMap<String, String>> hospitalDetails = new HashMap<String, HashMap<String, String>>();
		try {

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				member_id = rs.getString("member_id");
				name = rs.getString("name");
				gender = rs.getString("gender");
				rating = rs.getString("rating");
				image_path = rs.getString("image_path");
				speciality = getDoctorSpeciality(member_id, con);
				education = getEducationDetails(member_id, con);
				language = getLanguageDetails(member_id, con);
				professionalMem = getProfessionalMembership(member_id, con);
				String professInString = convertListToCommaSepaerated(professionalMem);
				hospitalDetails = getHospitalDetails(member_id, con, date,
						searchValue, searchkey);
				String specialityInString = convertListToCommaSepaerated(speciality);
				String educationInString = convertListToCommaSepaerated(education);
				String languageInString = convertListToCommaSepaerated(language);
				LOGGER.info("^member_id" + member_id);
				LOGGER.info("^speciality" + speciality);
				LOGGER.info("^name" + name);
				LOGGER.info("^gender" + gender);
				LOGGER.info("^rating" + rating);
				LOGGER.info("^education" + education);
				LOGGER.info("^language" + language);
				LOGGER.info("^hospitalDetails" + hospitalDetails);

				for (Entry<String, HashMap<String, String>> entry : hospitalDetails
						.entrySet()) {
					jsonobj = new JSONObject();
					jsonobj.put("doctor_id", member_id);
					jsonobj.put("name", name);
					jsonobj.put("gender", gender);
					jsonobj.put("rating", rating);
					jsonobj.put("image_path", image_path);
					if (searchkey.equalsIgnoreCase("speciality")
							|| searchkey
									.equalsIgnoreCase("location_speciality")) {
						jsonobj.put("speciality", searchValue);

					} else {
						jsonobj.put("speciality", specialityInString);
					}
					jsonobj.put("education", educationInString);
					jsonobj.put("language", languageInString);
					jsonobj.put("prof_member", professInString);
					String convertedDate = convertDate(date);
					jsonobj.put("date", convertedDate);
					for (Entry<String, String> hash : entry.getValue()
							.entrySet()) {
						jsonobj.put(hash.getKey(), hash.getValue());
					}

					jsonArr.put(jsonobj);
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonArr;
	}

	private static String convertDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String dateStr = "";
		try {
			Date dateForm = dateFormat.parse(date);
			DateFormat dest = new SimpleDateFormat("MM/dd/yyyy");
			dateStr = dest.format(dateForm);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return dateStr;
	}

	private static List<String> getProfessionalMembership(String member_id,
			Connection con) {

		List<String> professioanl = new ArrayList<String>();

		String language_id = null;
		String professioanl_id_query = "select * from doctor_prof_membership where member_id='"
				+ member_id + "'";

		String professionalName = null;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(professioanl_id_query);
			while (rs.next()) {
				language_id = rs.getString("prof_id");
				professionalName = getProfessionalName(language_id, con);
				professioanl.add(professionalName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return professioanl;

	}

	private static String getProfessionalName(String prof_id, Connection con) {

		String ProfName = null;
		String profNameQuery = "select name from professional_membership where prof_id='"
				+ prof_id + "'";

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(profNameQuery);
			while (rs.next()) {
				ProfName = rs.getString("name");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return ProfName;

	}

	private static String convertListToCommaSepaerated(List<String> speciality) {
		StringBuilder commaSepValueBuilder = new StringBuilder();
		for (int i = 0; i < speciality.size(); i++) {
			commaSepValueBuilder.append(speciality.get(i));
			if (i != speciality.size() - 1) {
				commaSepValueBuilder.append(" ");
			}
		}
		return commaSepValueBuilder.toString();
	}

	private static HashMap<String, HashMap<String, String>> getHospitalDetails(
			String member_id, Connection con, String date, String searchValue,
			String searchkey) {
		String hospitals_id = "select * from doctor_hosp where member_id_doctor='"
				+ member_id + "'";
		String hospital_id = null;
		HashMap<String, String> hospital_details = new HashMap<String, String>();
		HashMap<String, HashMap<String, String>> hospDetails = new HashMap<String, HashMap<String, String>>();
		if (searchkey.equalsIgnoreCase("location")) {

			hospital_details = getHospitalData(searchValue, con, member_id,
					date);
			hospDetails.put(searchValue, hospital_details);

		} else {
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(hospitals_id);
				while (rs.next()) {
					hospital_id = rs.getString("member_id_hosp");
					hospital_details = getHospitalData(hospital_id, con,
							member_id, date);
					hospDetails.put(hospital_id, hospital_details);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return hospDetails;

	}

	private static HashMap<String, String> getHospitalData(String hospital_id,
			Connection con, String member_id, String date) {

		HashMap<String, String> hospData = new HashMap<String, String>();

		String hosp_id_query = "select * from hospital where member_id='"
				+ hospital_id + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(hosp_id_query);
			while (rs.next()) {

				String branch = rs.getString("branchName");
				String address_line1 = rs.getString("address_line1");
				String address_line2 = rs.getString("address_line2");
				String city = rs.getString("city");
				String country = rs.getString("country");
				String zip = rs.getString("zip");
				String telephone_no = rs.getString("telephone_no");
				String state = rs.getString("state");
				String availability = getAppointmentDetails(member_id, con,
						date, hospital_id);
				hospData.put("branch", branch);
				hospData.put("address_line1", address_line1);
				hospData.put("address_line2", address_line2);
				hospData.put("city", city);
				hospData.put("zip", zip);
				hospData.put("country", country);
				hospData.put("state", state);
				hospData.put("telephone_no", telephone_no);
				hospData.put("availability", availability);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hospData;
	}

	private static List<String> getLanguageDetails(String member_id,
			Connection con) {

		List<String> language = new ArrayList<String>();

		String language_id = null;
		String language_id_query = "select * from doctor_language where member_id='"
				+ member_id + "'";

		String languageName = null;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(language_id_query);
			while (rs.next()) {
				language_id = rs.getString("language_id");
				languageName = getLanguageName(language_id, con);
				language.add(languageName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return language;
	}

	private static String getLanguageName(String language_id, Connection con) {

		String languageName = null;
		String specilaityNameQuery = "select name from language where language_id='"
				+ language_id + "'";

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(specilaityNameQuery);
			while (rs.next()) {
				languageName = rs.getString("name");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return languageName;
	}

	private static List<String> getDoctorSpeciality(String member_id,
			Connection con) {

		List<String> speciality = new ArrayList<String>();

		String specilaity_id = null;
		specilaity_id = "select * from doc_speciality where member_id='"
				+ member_id + "'";

		String specialityName = null;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(specilaity_id);
			while (rs.next()) {
				String speciality_id = rs.getString("speciality_id");
				specialityName = getSpecialityName(speciality_id, con);
				speciality.add(specialityName);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return speciality;
	}

	private static String getSpecialityName(String speciality_id, Connection con) {
		String specilaityName = null;
		String specilaityNameQuery = "select name from speciality where speciality_id='"
				+ speciality_id + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(specilaityNameQuery);
			while (rs.next()) {
				specilaityName = rs.getString("name");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return specilaityName;
	}

	private static List<String> getEducationDetails(String member_id,
			Connection con) {
		List<String> education = new ArrayList<String>();

		String specilaity_id = null;
		specilaity_id = "select * from doctor_education where member_id='"
				+ member_id + "'";
		Statement stmt;
		String educationName = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(specilaity_id);
			while (rs.next()) {
				String education_id = rs.getString("education_id");
				educationName = getEducationName(education_id, con);
				education.add(educationName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return education;
	}

	private static String getEducationName(String education_id, Connection con) {
		String educationNameQuery = null;
		String educationName = null;
		educationNameQuery = "select name from education where education_id='"
				+ education_id + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(educationNameQuery);
			while (rs.next()) {
				educationName = rs.getString("name");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return educationName;
	}

	private static JSONObject allbookingSlots(String doctor_id, Connection con,
			String hospital_id, String currentDate) {
		String query = "select s.time_from, s.time_to,s.appoint_duration,s.applicable_days,s.not_avail_from,s.not_avail_to,s.days from schedule_time as s where member_id_doctor='"
				+ doctor_id + "' and member_id_hosp='" + hospital_id + "' ";
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

					e.printStackTrace();
				}
				LOGGER.info(" time_to:   " + applicable_days);
				Date not_avail_from = rs.getTime("not_avail_from");
				LOGGER.info(" time_to:   " + not_avail_from);
				Date not_avail_to = rs.getTime("not_avail_to");
				LOGGER.info(" not_avail_to:   " + not_avail_to);
				String days = rs.getString("days");
				LOGGER.info(" days:   " + days);
				LOGGER.info(" date:::::::::   " + currentDate);
				Date appointmentDate = convertStringToDate(currentDate);
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

			e.printStackTrace();
		}

		return allSlots;
	}

	private static String getAppointmentDetails(String doctor_id,
			Connection con, String currentDate, String hospital_id) {
		String availabilty = "";
		int count = 1;
		availabilty = availableDate(con, currentDate, doctor_id, hospital_id);
		while (availabilty == "" && count <= 7) {
			count++;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = formatter.parse(currentDate);
				Calendar c1 = Calendar.getInstance();
				c1.setTime(date);
				System.out.println(date);
				c1.add(Calendar.DATE, 1);
				date = c1.getTime();
				currentDate = formatter.format(date);

				LOGGER.info("&&&&&&&&& " + currentDate);
				availabilty = availableDate(con, currentDate, doctor_id,
						hospital_id);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		if (count >= 8) {
			availabilty = "Not Available";
		}
		return availabilty;
	}

	private static String availableDate(Connection con, String currentDate,
			String doctor_id, String hospital_id) {
		String availabilty = "";
		String apointmentTime;
		String time = "";
		JSONArray bookedAppointmentList = new JSONArray();
		JSONObject booking_slots = new JSONObject();

		apointmentTime = "select a.appoint_time from appointment as a where member_id_doctor=' "
				+ doctor_id
				+ "' and appoint_date='"
				+ currentDate
				+ "' and member_id_hosp = '" + hospital_id + "'";
		LOGGER.info("&&&&&&&&& " + apointmentTime);
		ResultSet r;
		Statement stmt = null;
		Calendar cal1 = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());

		try {
			stmt = con.createStatement();
			r = stmt.executeQuery(apointmentTime);

			while (r.next()) {
				time = r.getString("appoint_time");

				bookedAppointmentList.put(time);

			}
			booking_slots = allbookingSlots(doctor_id, con, hospital_id,
					currentDate);
			Date date1 = dateFormat.parse(currentDate);
			cal1.setTime(date1);
			SimpleDateFormat sdf = new SimpleDateFormat("EEE");

			SimpleDateFormat year = new SimpleDateFormat("yyyy");
			String dayString = sdf.format(date1);
			String dateWithYear = year.format(date1);
			LOGGER.info(" time_to:   " + dayString);
			SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
			String month = monthFormat.format(date1);
			SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
			String dayForm = dayFormat.format(date1);

			if (dayForm.equalsIgnoreCase("01")
					|| dayForm.equalsIgnoreCase("21")
					|| dayForm.equalsIgnoreCase("31")) {
				dayForm = dayForm + "st";
			} else if (dayForm.equalsIgnoreCase("2")
					|| dayForm.equalsIgnoreCase("22")) {
				dayForm = dayForm + "nd";
			} else if (dayForm.equalsIgnoreCase("3")
					|| dayForm.equalsIgnoreCase("23")) {
				dayForm = dayForm + "rd";
			} else {
				dayForm = dayForm + "th";
			}
			Iterator<?> days = booking_slots.keys();
			while (days.hasNext()) {

				String day = (String) days.next();
				if (day.equalsIgnoreCase(dayString)) {
					JSONArray allslotsinDay = booking_slots.getJSONArray(day);
					if (allslotsinDay.length() != bookedAppointmentList
							.length()) {
						if (currentDate.equalsIgnoreCase(date)) {
							availabilty = "Available Today";
						} else {
							availabilty = "Next Availabilty: " + dayString
									+ "," + dayForm + " " + month + " "
									+ dateWithYear;
						}
					} else {
						availabilty = "";
					}
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return availabilty;
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
		JSONArray time_slot = new JSONArray();
		Iterator<?> slotKeys = allSlots.keys();
		while (slotKeys.hasNext()) {
			String key = (String) slotKeys.next();
			if (key.equalsIgnoreCase(days)) {
				try {
					LOGGER.info("inside firsttry ********" + allSlots.get(key));
					time_slot = allSlots.getJSONArray(key);
				} catch (JSONException e) {
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

	private static Date convertStringToDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = null;
		try {
			dateFormat = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat;
	}

}
