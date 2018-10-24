package com.cts.accelerators.lifeplus.helpers;

import javax.naming.ldap.PagedResultsResponseControl;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.personalization.factory.ContentPersonalization;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverResponse;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceResponse;

public class CustomPersonalizationHelper {

	private static final String CLASS_NAME = CustomPersonalizationHelper.class.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomPersonalizationHelper.class);
	
	public static PersonalizationServiceResponse getPersonalization(PersonalizationServiceRequest serviceRequest) {
		
		String methodName = "getPersonalization";
		LOGGER.info(" || "+methodName + " START");
		
		PersonalizationServiceResponse serviceResponse = new PersonalizationServiceResponse();
				
		// TODO find the member type from member id
		
		// TODO check friends_list table to find friends list
		// if friends - show testi of friends - latest first
		// if friends testi count < max testi count - show random testi - latest first
		// if friends not present - show random testi - latest first
		
		JSONObject jsonResponse = new ContentPersonalization().getPersonalization(serviceRequest);
		serviceResponse.setJsonResponse(jsonResponse);
		
		LOGGER.info(" || "+methodName + " END");
		return serviceResponse;
	}
	
}
