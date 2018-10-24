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
import com.cts.accelerators.lifeplus.helpers.CustomPersonalizationHelper;
import com.cts.accelerators.lifeplus.personalization.factory.ContentPersonalization;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverRequest;
import com.cts.accelerators.lifeplus.services.dto.ContentRetrieverResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

@Component(metatype = true, immediate = true)
@Service(value = CustomPersonalizationService.class)
public class CustomPersonalizationService {

	private static final String CLASS_NAME = CustomPersonalizationService.class
			.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomPersonalizationService.class);

	@Reference
	com.cts.accelerators.lifeplus.core.services.ConfigurationUtil confUtil;

		@Activate
	protected void activate() {
		LOGGER.info("Custom Personalization service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Custom Personalization service stopped");
	}

}
