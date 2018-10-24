package com.cts.accelerators.lifeplus.personalization.factory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.core.DatabaseConnectionHelper;
import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverRequest;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceRequest;

public class ContentPersonalization extends PersonalizationAbstractFactory {

	private final static String CLASS_NAME = ContentPersonalization.class
			.getName();
	private final static Logger LOGGER = LoggerFactory
			.getLogger(ContentPersonalization.class);

	public ContentPersonalization() {
		super(PersonalizationType.CustomPersonalization);
	}

	public JSONObject getPersonalizationContent(
			ContentRetrieverRequest retrieverRequest) {
		String methodName = "getPersonalizationContent";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject jsonResponse = new JSONObject();
		JSONObject jsonObject = new JSONObject();

		try {
			Connection con = null;
			DatabaseConnectionHelper dataBaseHelper = new DatabaseConnectionHelper();
			con = dataBaseHelper.getConnection();

			String personalizationContentTable = retrieverRequest
					.getPersonalizationContentTable()
					.substring(
							0,
							retrieverRequest
									.getPersonalizationContentTable()
									.lastIndexOf(LifePlusGenericConstants.COLON));
			String[] personalizationContentTableFields = retrieverRequest
					.getPersonalizationContentTable()
					.substring(
							retrieverRequest
									.getPersonalizationContentTable()
									.lastIndexOf(LifePlusGenericConstants.COLON) + 1,
							retrieverRequest.getPersonalizationContentTable()
									.length())
					.split(LifePlusGenericConstants.CSV_SEPARATOR);
			String contentBasedOnUserGroupTable = retrieverRequest
					.getContentBasedOnUserGroupTable()
					.substring(
							0,
							retrieverRequest
									.getContentBasedOnUserGroupTable()
									.lastIndexOf(LifePlusGenericConstants.COLON));
			String contentBasedOnUserGroupTableFields = retrieverRequest
					.getContentBasedOnUserGroupTable()
					.substring(
							retrieverRequest
									.getContentBasedOnUserGroupTable()
									.lastIndexOf(LifePlusGenericConstants.COLON) + 1,
							retrieverRequest.getContentBasedOnUserGroupTable()
									.length());
			String[] joinTables = retrieverRequest.getJoinTables();
			String[] orderByValues = retrieverRequest.getOrderBy().split(
					LifePlusGenericConstants.COLON);
			String orderByTable = orderByValues[0]
					.equalsIgnoreCase(personalizationContentTable) ? "t."
					: "p.";
			String orderBy = orderByValues[1];
			String joinTable = "";
			String[] joinTableFields = {};

			String key = retrieverRequest.getKey();
			String personalizationType = retrieverRequest
					.getPersonalizationType().split(
							LifePlusGenericConstants.COLON)[1];
			String personalizationGroup = retrieverRequest
					.getPersonalizationGroup().split(
							LifePlusGenericConstants.COLON)[1];

			/**
			 * TODO check member_type from database
			 */
			String memberType = LifePlusGenericConstants.PATIENT_TABLE;

			for (String joinTablesItem : joinTables) {
				if (joinTablesItem.split(LifePlusGenericConstants.COLON)[0]
						.equalsIgnoreCase(memberType)) {
					joinTable = joinTablesItem
							.split(LifePlusGenericConstants.COLON)[0];
					joinTableFields = joinTablesItem
							.split(LifePlusGenericConstants.COLON)[1]
							.split(LifePlusGenericConstants.CSV_SEPARATOR);
				}
			}

			try {

				final Statement statement = con.createStatement();
				List<Integer> friends_id = new ArrayList<Integer>();
				String query = "";
				String firstQuery = "";
				String secondQuery = "";
				String friendsListQuery = "";
				String addingFriendsMemberIdQuery = "";
				ResultSet rs;
				int i = 1;
				int count = 0;
				firstQuery = "SELECT * FROM " + joinTable + " AS p ";
				LOGGER.debug(" || " + methodName + " || firstQuery || "
						+ firstQuery);
				secondQuery = "(SELECT * FROM " + personalizationContentTable
						+ " WHERE " + personalizationType + "='"
						+ retrieverRequest.getInterest() + "' AND " + key
						+ "!=" + retrieverRequest.getMemberId() + ") AS t";
				LOGGER.debug(" || " + methodName
						+ " || secondQuery without login || " + secondQuery);
				if (!retrieverRequest.getMemberId().equalsIgnoreCase("0")) {
					friendsListQuery = "SELECT mf."
							+ contentBasedOnUserGroupTableFields + " FROM "
							+ contentBasedOnUserGroupTable + " AS mf WHERE mf."
							+ key + "=" + retrieverRequest.getMemberId()
							+ " AND mf." + contentBasedOnUserGroupTableFields
							+ "!='NULL';";
					LOGGER.debug(" || " + methodName
							+ " || query friends list || " + friendsListQuery);
					rs = statement.executeQuery(friendsListQuery);
					while (rs.next()) {
						friends_id.add(rs
								.getInt(contentBasedOnUserGroupTableFields));
						LOGGER.debug(" || " + methodName + " || friend_id || "
								+ rs.getInt(contentBasedOnUserGroupTableFields));
					}
					if (null != friends_id) {
						secondQuery = "SELECT COUNT(*) count FROM "
								+ personalizationContentTable + " WHERE "
								+ personalizationType + "='"
								+ retrieverRequest.getInterest() + "'";
						if (friends_id.size() > 0) {
							secondQuery = secondQuery + " AND ";
							for (int id : friends_id) {
								addingFriendsMemberIdQuery = addingFriendsMemberIdQuery
										+ key + "=" + id;
								if (count < friends_id.size() - 1) {
									addingFriendsMemberIdQuery = addingFriendsMemberIdQuery
											+ " OR ";
								}
								count++;
							}
						}
						secondQuery = secondQuery + addingFriendsMemberIdQuery
								+ ";";
						LOGGER.debug(" || " + methodName
								+ " || secondQuery friends list || "
								+ secondQuery);
						rs = statement.executeQuery(secondQuery);
						while (rs.next()) {
							count = Integer.parseInt(rs.getString("count"));
						}
						secondQuery = "(SELECT * FROM "
								+ personalizationContentTable
								+ " WHERE "
								+ personalizationType
								+ "='"
								+ retrieverRequest.getInterest()
								+ "' AND "
								+ addingFriendsMemberIdQuery
								+ " LIMIT "
								+ retrieverRequest.getTestimonialCount()
								+ " UNION (SELECT * FROM "
								+ personalizationContentTable
								+ " WHERE "
								+ personalizationType
								+ "='"
								+ retrieverRequest.getInterest()
								+ "' AND "
								+ key
								+ " NOT IN (SELECT "
								+ key
								+ " FROM "
								+ personalizationContentTable
								+ " WHERE "
								+ addingFriendsMemberIdQuery
								+ " AND "
								+ key
								+ "="
								+ retrieverRequest.getMemberId()
								+ ") LIMIT "
								+ (retrieverRequest.getTestimonialCount() - count)
								+ ")) as t";
						LOGGER.debug(" || "
								+ methodName
								+ " || secondQuery friends list < testi count || "
								+ secondQuery);
					}

				}

				query = firstQuery + " RIGHT JOIN " + secondQuery + " ON t."
						+ key + "=p." + key;
				if (retrieverRequest.getTestimonialCount() - count == 0) {
					query += " ORDER BY " + orderByTable + orderBy;
				}
				query += " LIMIT " + retrieverRequest.getTestimonialCount()
						+ ";";

				LOGGER.debug(" || " + methodName + " || query || " + query);
				rs = statement.executeQuery(query);
				int rsCount = 0;
				while (rs.next()) {
					rsCount++;
					jsonObject = new JSONObject();
					for (String personalizationField : personalizationContentTableFields) {
						jsonObject.put(personalizationField,
								rs.getString("t." + personalizationField));
					}
					for (String joinTableField : joinTableFields) {
						jsonObject.put(joinTableField,
								rs.getString("p." + joinTableField));
					}
					jsonResponse.put(Integer.toString(i++), jsonObject);
				}
				if(rsCount == 0) {
					retrieverRequest.setStatus(LifePlusGenericConstants.STATUS_FAILURE);
					retrieverRequest
							.setDescription(retrieverRequest
									.getDescription() + "The result set of the query is empty. Please check the logs; ");
				} else {
					retrieverRequest.setStatus(LifePlusGenericConstants.STATUS_SUCCESS);
				}
				jsonResponse.put(LifePlusGenericConstants.STATUS, retrieverRequest.getStatus());
				jsonResponse.put(LifePlusGenericConstants.DESCRIPTION, retrieverRequest.getDescription());
				LOGGER.debug(" || " + methodName + " || jsonResponse || "
						+ jsonResponse);
				rs.close();

			} catch (SQLException e) {
				LOGGER.error(" || " + methodName + " || EXCEPTION OCCURED || "
						+ e.getMessage());
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				LOGGER.error(" || "
						+ methodName
						+ " || EXCEPTION OCCURED || Please check if the configurations are done in the felix console");
			} else {
				LOGGER.error(" || " + methodName + " || EXCEPTION OCCURED || "
						+ e.getMessage());
			}
		}

		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	public JSONObject getPersonalization(
			PersonalizationServiceRequest serviceRequest) {

		return null;
	}

}
