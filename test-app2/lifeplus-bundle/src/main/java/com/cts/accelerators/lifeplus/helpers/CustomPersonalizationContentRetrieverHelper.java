package com.cts.accelerators.lifeplus.helpers;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.personalization.factory.ContentPersonalization;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverRequest;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverResponse;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceResponse;

public class CustomPersonalizationContentRetrieverHelper {
	
	private static final String CLASS_NAME = CustomPersonalizationContentRetrieverHelper.class.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomPersonalizationContentRetrieverHelper.class);
	
	public static ContentRetrieverResponse getPersonalizationContent(ContentRetrieverRequest retrieverRequest) {
		
		String methodName = "getPersonalizationContent";
		LOGGER.info(" || "+methodName + " START");
				
		ContentRetrieverResponse retriverResponse = new ContentRetrieverResponse();
		
		// TODO find the member type from member id
		
		// TODO check friends_list table to find friends list
		// if friends - show testi of friends - latest first
		// if friends testi count < max testi count - show random testi - latest first
		// if friends not present - show random testi - latest first
		
		JSONObject jsonResponse = new ContentPersonalization().getPersonalizationContent(retrieverRequest);
		retriverResponse.setJsonResponse(jsonResponse);
		
		LOGGER.info(" || "+methodName + " END");
		return retriverResponse;
	}

}
