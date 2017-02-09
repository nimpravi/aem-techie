package com.cts.accelerators.lifeplus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONObject;

import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.core.dto.PersonalizationResponse;
import com.cts.accelerators.lifeplus.personalization.factory.ContentPersonalization;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverRequest;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

@Component(metatype = true, immediate = true)
@Service(value = CustomPersonalizationContentRetrieverService.class)
public class CustomPersonalizationContentRetrieverService {

	private static final String CLASS_NAME = CustomPersonalizationContentRetrieverService.class
			.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomPersonalizationContentRetrieverService.class);

	@Reference
	com.cts.accelerators.lifeplus.core.services.ConfigurationUtil confUtil;

	@Property(label = "Personalization Content Table", name = "personalizationContentTable", description = "This is the table from which we need to retrieve the personalized content. Give table:fields(comma separated)")
	public static final String VALUE1 = "personalizationContentTable";

	@Property(label = "Personalization Content Based on User Group Table", name = "contentBasedOnUserGroupTable", description = "This is the field from which we need to get the content based on user relationship. Give table:fields(comma separated)")
	public static final String VALUE2 = "contentBasedOnUserGroupTable";

	@Property(label = "Join Tables", name = "joinTables", description = "These are the tables where we need to get additional content based on personalization, like user profile data. Give table:foreign key(with respect to personalization table):fields(comma separated)", cardinality = Integer.MAX_VALUE)
	public static final String VALUE3 = "joinTables";

	@Property(label = "Primary & Foreign Keys Field", name = "key", description = "The field common in the personalization and join table which defines the join relationship between the two tables. Give field")
	public static final String VALUE6 = "key";

	@Property(label = "Order By", name = "orderBy", description = "Results should be poplulated by which fields (comma separted)")
	public static final String VALUE4 = "orderBy";

	@Property(label = "Type of Personalization", name = "personalizationType", description = "This is the field which which defines the type of personalization needed. Give table:fields(comma separated)")
	public static final String VALUE5 = "personalizationType";

	@Property(label = "User Personalization Group", name = "personalizationGroup", description = "This is the field which which defines how the personalization has to be grouped, like type of user. Give table:fields(comma separated)")
	public static final String VALUE7 = "personalizationGroup";

	@Activate
	protected void activate() {
		LOGGER.info("Content Retriever service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Content Retriever service stopped");
	}

	public PersonalizationResponse execute(
			ContentRetrieverRequest retrieverRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " START");

		ContentRetrieverResponse retrieverResponse = new ContentRetrieverResponse();

		retrieverRequest = setConfigProperties(retrieverRequest);

		LOGGER.debug("PersonalizationContentTable : "
				+ retrieverRequest.getPersonalizationContentTable()
				+ " || ContentBasedOnUserGroupTable : "
				+ retrieverRequest.getContentBasedOnUserGroupTable()
				+ " || JoinTables : " + retrieverRequest.getJoinTables()
				+ " || OrderBy : " + retrieverRequest.getOrderBy());

		JSONObject jsonObject = new ContentPersonalization()
				.getPersonalizationContent(retrieverRequest);

		retrieverResponse.setJsonResponse(jsonObject);

		LOGGER.info(" || " + methodName + " END");
		return retrieverResponse;
	}

	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param serviceRequest
	 * @return Content Retriever request variable containing configured values
	 * @throws AcceleratorException
	 */
	private ContentRetrieverRequest setConfigProperties(
			ContentRetrieverRequest serviceRequest) throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START ");
		String PID = "com.cts.accelerators.lifeplus.services.CustomPersonalizationContentRetrieverService";
		if (serviceRequest.getPersonalizationContentTable() == null
				|| serviceRequest
						.getPersonalizationContentTable()
						.equalsIgnoreCase(LifePlusGenericConstants.EMPTY_STRING)) {
			serviceRequest.setPersonalizationContentTable(confUtil.getConfig(
					PID, VALUE1));
		}
		if (serviceRequest.getContentBasedOnUserGroupTable() == null
				|| serviceRequest
						.getContentBasedOnUserGroupTable()
						.equalsIgnoreCase(LifePlusGenericConstants.EMPTY_STRING)) {
			serviceRequest.setContentBasedOnUserGroupTable(confUtil.getConfig(
					PID, VALUE2));
		}
		if (serviceRequest.getJoinTables() == null
				|| serviceRequest.getJoinTables().length == 0) {
			serviceRequest.setJoinTables(confUtil.getMultiFiledConfig(PID,
					VALUE3));
			LOGGER.info(" || " + methodName + " || join tables || "
					+ serviceRequest.getJoinTables());
		}
		if (serviceRequest.getOrderBy() == null
				|| serviceRequest.getOrderBy().equalsIgnoreCase(
						LifePlusGenericConstants.EMPTY_STRING)) {
			serviceRequest.setOrderBy(confUtil.getConfig(PID, VALUE4));
		}
		if (serviceRequest.getPersonalizationType() == null
				|| serviceRequest.getPersonalizationType().equalsIgnoreCase(
						LifePlusGenericConstants.EMPTY_STRING)) {
			serviceRequest.setPersonalizationType(confUtil.getConfig(PID,
					VALUE5));
		}
		if (serviceRequest.getKey() == null
				|| serviceRequest.getKey().equalsIgnoreCase(
						LifePlusGenericConstants.EMPTY_STRING)) {
			serviceRequest.setKey(confUtil.getConfig(PID, VALUE6));
		}
		if (serviceRequest.getPersonalizationGroup() == null
				|| serviceRequest.getPersonalizationGroup().equalsIgnoreCase(
						LifePlusGenericConstants.EMPTY_STRING)) {
			serviceRequest.setPersonalizationGroup(confUtil.getConfig(PID,
					VALUE7));
		}
		LOGGER.info(" || " + methodName + " || END");
		return serviceRequest;
	}

}
