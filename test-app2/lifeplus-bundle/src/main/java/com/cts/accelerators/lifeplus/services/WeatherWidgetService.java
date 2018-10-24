package com.cts.accelerators.lifeplus.services;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
import com.cts.accelerators.core.services.AcceleratorService;
import com.cts.accelerators.core.services.ConfigurationUtil;
import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;
import com.cts.accelerators.lifeplus.helpers.WeatherWidgetHelper;
import com.cts.accelerators.lifeplus.services.dto.WeatherWidgetServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.WeatherWidgetServiceResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.services.ContentImporterService;

@Component(immediate = true, label = "Weather Widget Service", description = "Weather Widget Service", metatype = true)
@Service(value=WeatherWidgetService.class)
@Properties({
		@Property(name = Constants.SERVICE_DESCRIPTION, value = "Weather Widget Service"),
		@Property(name = Constants.SERVICE_VENDOR,value = "Cognizant")
})


public class WeatherWidgetService implements AcceleratorService{
	@Property(label= LifePlusGenericConstants.WEATHER_API_KEY , name="apiKey", value = "193ea7f222dff1964cd35985da494")
	public static final String VALUE1 = "apiKey";
	@Property(label = LifePlusGenericConstants.WEATHER_API_URL,name="apiUrl", value = "http://api.worldweatheronline.com/free/v2/weather.ashx?")
	public static final String VALUE2 = "apiUrl";
	@Property(label = LifePlusGenericConstants.WEATHER_API_PROPERTY_TEMPERATUE,name="temperature", value = "data.current_condition`0.temp_C")
	public static final String VALUE3 = "temperature";
	@Property(label = LifePlusGenericConstants.WEATHER_API_PROPERTY_CLOUD_COVER,name="cloudCover", value = "data.current_condition`0.cloudcover")
	public static final String VALUE4 = "cloudCover";
	@Property(label = LifePlusGenericConstants.WEATHER_API_PROPERTY_HUMIDITY,name="humidity", value = "data.current_condition`0.humidity")
	public static final String VALUE5 = "humidity";
	@Property(label = LifePlusGenericConstants.WEATHER_API_PROPERTY_FEELS_LIKE_TEMPERATURE,name="feelsLikeTemperature", value = "data.weather`0.hourly`0.FeelsLikeC")
	public static final String VALUE6 = "feelsLikeTemperature";
	
	@Reference
	private ConfigurationUtil configurationUtil;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentImporterService.class);
	
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");
		WeatherWidgetServiceResponse serviceResponse = new WeatherWidgetServiceResponse();
		WeatherWidgetServiceRequest weatherWidgetServiceRequest=null;
		if (serviceRequest instanceof WeatherWidgetServiceRequest) {
			weatherWidgetServiceRequest = (WeatherWidgetServiceRequest) serviceRequest;
			setConfigProperties(weatherWidgetServiceRequest);
		}
		JSONObject validationResponse = new JSONObject();
		validationResponse = validateRequiredParameters(weatherWidgetServiceRequest);
		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
		} catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					, e);
		}
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(validationStatus)) {
			serviceResponse = fetchWeatherReport(weatherWidgetServiceRequest);
		} else {
			serviceResponse.setJsonResponse(validationResponse);
		}
		LOGGER.info(" || " + methodName + " || END");
		return serviceResponse;
	}

	/**
	 * This method is used to validate if all the necessary request variables
	 * are present or not
	 * 
	 * @param weatherWidgetServiceRequest
	 * @return
	 * @throws AcceleratorException
	 */
	private JSONObject validateRequiredParameters(
			WeatherWidgetServiceRequest weatherWidgetServiceRequest)
			throws AcceleratorException {
		String methodName = "validateRequiredParameters : content importer";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true;
		boolean isValidPath = true;
		boolean pathCheck = true;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (weatherWidgetServiceRequest != null) {
				if (StringUtils.isEmpty(weatherWidgetServiceRequest.getApiKey())) {
					parameterPresent = false;
					statusDescription
							.append("Weather API Key is Missing;");
				}
				if (StringUtils.isEmpty(weatherWidgetServiceRequest.getApiUrl())) {
					parameterPresent = false;
					statusDescription
							.append("Weather API URL is Missing;");
				}
				if (StringUtils.isEmpty(weatherWidgetServiceRequest.getParam_location())) {
					parameterPresent = false;
					statusDescription
							.append("Weather Api Location Parameter is Missing;");
				}
				if (StringUtils.isEmpty(weatherWidgetServiceRequest.getCloudCover()) && StringUtils.isEmpty(weatherWidgetServiceRequest.getTemperature()) &&StringUtils.isEmpty(weatherWidgetServiceRequest.getHumidity()) &&  StringUtils.isEmpty(weatherWidgetServiceRequest.getFeelsLikeTemperature())) {
					parameterPresent = false;
					statusDescription
							.append("None of the whether information are specified to retrieve from the API;");
				}
				
			} else {
				parameterPresent = false;
				statusDescription
						.append("Weather API request information missing;");
			}
			if (!parameterPresent || !isValidPath) {
				responseObject.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_FAILURE);
				responseObject.put(AcceleratorGenericConstants.DESCRIPTION,
						statusDescription.toString());
			} else {
				responseObject.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_SUCCESS);

			}
		} catch (JSONException e) {
				LOGGER.error(" || " + methodName + " || EXCEPTION || "
						, e);
		}
		LOGGER.debug(" || " + methodName + " || responseObject || "
				+ responseObject);
		LOGGER.info(" || " + methodName + " || END");
		return responseObject;
	}
	
	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param weatherWidgetServiceRequest
	 * @return Content reader request variable containing configured values
	 * @throws AcceleratorException
	 */
	private void setConfigProperties(
			WeatherWidgetServiceRequest weatherWidgetServiceRequest)
			throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START");
		String PID = "com.cts.accelerators.lifeplus.services.WeatherWidgetService";
		weatherWidgetServiceRequest.setApiKey(configurationUtil.getConfig(PID, VALUE1));
		weatherWidgetServiceRequest.setApiUrl(configurationUtil.getConfig(PID, VALUE2));
		weatherWidgetServiceRequest.setTemperature(configurationUtil.getConfig(PID, VALUE3));
		weatherWidgetServiceRequest.setCloudCover(configurationUtil.getConfig(PID, VALUE4));
		weatherWidgetServiceRequest.setFeelsLikeTemperature(configurationUtil.getConfig(PID, VALUE6));
		weatherWidgetServiceRequest.setHumidity(configurationUtil.getConfig(PID, VALUE5));
		LOGGER.info(" || " + methodName + " || END");
	}
	
	private WeatherWidgetServiceResponse fetchWeatherReport(WeatherWidgetServiceRequest weatherWidgetServiceRequest){
		String methodName = "fetchWeatherReport";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || END");
		return WeatherWidgetHelper.fetchWeatherReport(weatherWidgetServiceRequest);
	}
}
