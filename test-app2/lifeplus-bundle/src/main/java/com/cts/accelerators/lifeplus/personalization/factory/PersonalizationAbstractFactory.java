package com.cts.accelerators.lifeplus.personalization.factory;

import org.apache.sling.commons.json.JSONObject;

import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverRequest;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverResponse;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceResponse;

public abstract class PersonalizationAbstractFactory {

	/**
	 * This variable will contain the appropriate Personalization type
	 */
	private PersonalizationType personalizationType = null;

	public PersonalizationAbstractFactory(
			PersonalizationType personalizationType) {
		this.personalizationType = personalizationType;
	}

	/**
	 * @param retrieverRequest
	 * @return
	 */
	public abstract JSONObject getPersonalizationContent(
			ContentRetrieverRequest retrieverRequest);
	
	/**
	 * @param serviceRequest
	 * @return
	 */
	public abstract JSONObject getPersonalization(
			PersonalizationServiceRequest serviceRequest);

	/**
	 * @return the personalizationType
	 */
	public PersonalizationType getPersonalizationType() {
		return personalizationType;
	}

	/**
	 * @param personalizationType
	 *            the personalizationType to set
	 */
	public void setPersonalizationType(PersonalizationType personalizationType) {
		this.personalizationType = personalizationType;
	}

}
