package com.cts.accelerators.lifeplus.helpers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.core.DatabaseConnectionHelper;

public class TestimonialsHelper {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(TestimonialsHelper.class);
	private final static String CLASS_NAME = TestimonialsHelper.class.getName();

	public static JSONObject getTestimonials(SlingHttpServletRequest request,
			Connection con) {
		String methodName = "getTestimonials";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject jsonResponse = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		String memberId = request.getParameter("member_id");
		String interest = request.getParameter("interest");
		LOGGER.debug(" || " + methodName + " || testi_count1 || "
				+ request.getParameter("testi_count"));
		int testi_count = Integer.parseInt(request.getParameter("testi_count"));

		LOGGER.debug(" || " + methodName + " || memberId || " + memberId);
		LOGGER.debug(" || " + methodName + " || interest || " + interest);
		LOGGER.debug(" || " + methodName + " || testi_count || " + testi_count);

		try {

			final Statement statement = con.createStatement();
			List<Integer> friends_id = new ArrayList<Integer>();
			String query = "";
			String query1 = "";
			ResultSet rs;
			int i = 1;
			int count = 0;

			// TODO check from member id if member type is patient, then take
			// profile data from patients table
			
			// TODO if patient, then first show testimonials of dependents, then
			// friends -> patient-dependent table, member-friends table

			if (memberId.equalsIgnoreCase("0")) {
				query = "SELECT * FROM testimonials AS t JOIN profile AS p WHERE t.member_id=p.member_id AND t.testimonial_type=\""
						+ interest + "\" ORDER BY t.creation_date DESC;";
				LOGGER.debug(" || " + methodName
						+ " || query not logged in || " + query);
				rs = statement.executeQuery(query);
				while (rs.next() && count < testi_count) {
					jsonObject = new JSONObject();
					jsonObject
							.put("testimonial", rs.getString("t.testimonial"));
					jsonObject.put("rating", rs.getString("t.rating"));
					jsonObject.put("creation_date",
							rs.getString("t.creation_date"));
					jsonObject.put("image_path",
							rs.getString("p.member_image_path"));
					jsonObject.put("member_name", rs.getString("p.first_name")
							+ " " + rs.getString("p.last_name"));
					jsonObject.put("city", rs.getString("p.city"));
					jsonObject.put("country", rs.getString("p.country"));
					jsonResponse.put(Integer.toString(i++), jsonObject);
					count++;
				}
				LOGGER.debug(" || " + methodName
						+ " || jsonResponse not logged in || "
						+ jsonResponse.toString());
			} else {
				query = "SELECT mf.friends_id FROM member_friends_list AS mf WHERE mf.member_id="
						+ memberId + ";";
				LOGGER.debug(" || " + methodName + " || query friends list || "
						+ query);
				rs = statement.executeQuery(query);
				while (rs.next()) {
					friends_id.add(rs.getInt("friends_id"));
					LOGGER.debug(" || " + methodName + " || friend_id || "
							+ rs.getInt("friends_id"));
				}
				if (null != friends_id) {
					query = "SELECT * FROM testimonials AS t JOIN profile AS p WHERE t.member_id=p.member_id AND t.testimonial_type=\""
							+ interest
							+ "\" AND t.member_id IN (SELECT t.member_id FROM testimonials AS t JOIN profile AS p WHERE ";
					int size = friends_id.size();
					for (int id : friends_id) {
						LOGGER.debug(" || " + methodName
								+ " || individual friends id || " + id);
						query1 = query1 + " t.member_id=" + id;
						if (count < size - 1) {
							query1 = query1 + " OR ";
						}
						count++;
					}
					query = query + query1 + ")"
							+ " ORDER BY t.creation_date DESC;";
					LOGGER.debug(" || " + methodName
							+ " || query friends testimonials || " + query);
					count = 0;
					rs = statement.executeQuery(query);
					while (rs.next() && count < testi_count) {
						jsonObject = new JSONObject();
						jsonObject.put("testimonial",
								rs.getString("t.testimonial"));
						jsonObject.put("rating", rs.getString("t.rating"));
						jsonObject.put("creation_date",
								rs.getString("t.creation_date"));
						jsonObject.put("image_path",
								rs.getString("p.member_image_path"));
						jsonObject.put(
								"member_name",
								rs.getString("p.first_name") + " "
										+ rs.getString("p.last_name"));
						jsonObject.put("city", rs.getString("p.city"));
						jsonObject.put("country", rs.getString("p.country"));
						jsonResponse.put(Integer.toString(i++), jsonObject);
						count++;
					}
					LOGGER.debug(" || " + methodName
							+ " || jsonResponse friends testi || "
							+ jsonResponse.toString());
				}

				if (count < testi_count) {
					query = "SELECT * FROM testimonials AS t JOIN profile AS p WHERE t.member_id=p.member_id AND t.testimonial_type=\""
							+ interest
							+ "\" AND t.member_id NOT IN (SELECT t.member_id FROM testimonials AS t WHERE "
							+ query1
							+ " OR t.member_id="
							+ memberId
							+ ") ORDER BY t.creation_date DESC;";
					LOGGER.debug(" || " + methodName
							+ " || query if friends testi < count || " + query);
					count = testi_count - count;
					rs = statement.executeQuery(query);
					while (rs.next()) {
						jsonObject = new JSONObject();
						jsonObject.put("testimonial",
								rs.getString("t.testimonial"));
						jsonObject.put("rating", rs.getString("t.rating"));
						jsonObject.put("creation_date",
								rs.getString("t.creation_date"));
						jsonObject.put("image_path",
								rs.getString("p.member_image_path"));
						jsonObject.put(
								"member_name",
								rs.getString("p.first_name") + " "
										+ rs.getString("p.last_name"));
						jsonObject.put("city", rs.getString("p.city"));
						jsonObject.put("country", rs.getString("p.country"));
						jsonResponse.put(Integer.toString(i++), jsonObject);
						count--;
						if (count == 0) {
							break;
						}
					}
					LOGGER.debug(" || " + methodName
							+ " || jsonResponse friends testi < count || "
							+ jsonResponse.toString());
				}
			}
			rs.close();

		} catch (Exception e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION OCCURED || "
					+ e.getMessage());
		}

		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

}
