package com.cts.accelerators.lifeplus.services;


/**
 * This is the service which is specifies what type of personalization is needed.
 * 
 * @author Cognizant
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyOption;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.cts.accelerators.core.services.ConfigurationUtil;
import com.cts.accelerators.lifeplus.helpers.CustomPersonalizationHelper;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverRequest;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverResponse;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.PersonalizationServiceResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

@Component(metatype = true, immediate = true)
@Service(value = PersonalizationConfigurationService.class)
public class PersonalizationConfigurationService {

	private static final String CLASS_NAME = PersonalizationConfigurationService.class
			.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PersonalizationConfigurationService.class);

	@Reference
	private ConfigurationUtil configurationUtil;
	
	@Property(label = "Type of Personalization", name = "personalizationType", options = { 
			@PropertyOption(name = "customPersonalization", value = "Custom Personalization") })
	private static final String VALUE1 = "personalizationType";

	@Activate
	protected void activate() {
		LOGGER.info("Custom personalization service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Custom personalization service stopped");
	}

	public PersonalizationServiceResponse execute(
			PersonalizationServiceRequest serviceRequest)
			throws AcceleratorException {
		PersonalizationServiceResponse serviceResponse = new PersonalizationServiceResponse();

		serviceRequest = setConfigProperties(serviceRequest);
		
		serviceResponse = CustomPersonalizationHelper.getPersonalization(serviceRequest);

		return serviceResponse;
	}

	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param serviceRequest
	 * @return Personalization request variable containing configured values
	 * @throws AcceleratorException
	 */
	private PersonalizationServiceRequest setConfigProperties(
			PersonalizationServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START ");
		String PID = "com.cts.accelerators.lifeplus.services.CustomPersonalizationService";
		if (serviceRequest.getPersonalizationType() == null
				|| serviceRequest.getPersonalizationType().equalsIgnoreCase("")) {
			serviceRequest.setPersonalizationType(configurationUtil.getConfig(
					PID, VALUE1));
		}
		LOGGER.info(" || " + methodName + " || END");
		return serviceRequest;
	}

}
