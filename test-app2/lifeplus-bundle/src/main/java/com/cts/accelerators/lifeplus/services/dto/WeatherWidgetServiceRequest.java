package com.cts.accelerators.lifeplus.services.dto;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;

public class WeatherWidgetServiceRequest implements AcceleratorServiceRequest{
	private String param_q;
	private String param_format;
	private String param_lang;
	private String apiKey;
	private String apiUrl;
	private String temperature;
	private String cloudCover;
	private String humidity;
	private String feelsLikeTemperature;
	
	/**
	 * @return the param_location
	 */
	public String getParam_location() {
		return param_q;
	}
	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}
	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	/**
	 * @return the apiUrl
	 */
	public String getApiUrl() {
		return apiUrl;
	}
	/**
	 * @param apiUrl the apiUrl to set
	 */
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	/**
	 * @return the temperature
	 */
	public String getTemperature() {
		return temperature;
	}
	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	/**
	 * @return the cloudCover
	 */
	public String getCloudCover() {
		return cloudCover;
	}
	/**
	 * @param cloudCover the cloudCover to set
	 */
	public void setCloudCover(String cloudCover) {
		this.cloudCover = cloudCover;
	}
	/**
	 * @return the humidity
	 */
	public String getHumidity() {
		return humidity;
	}
	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	/**
	 * @return the feelsLikeTemperature
	 */
	public String getFeelsLikeTemperature() {
		return feelsLikeTemperature;
	}
	/**
	 * @param feelsLikeTemperature the feelsLikeTemperature to set
	 */
	public void setFeelsLikeTemperature(String feelsLikeTemperature) {
		this.feelsLikeTemperature = feelsLikeTemperature;
	}
	/**
	 * @param param_location the param_location to set
	 */
	public void setParam_location(String param_location) {
		this.param_q = param_location;
	}
	/**
	 * @return the param_format
	 */
	public String getParam_format() {
		return param_format;
	}
	/**
	 * @param param_format the param_format to set
	 */
	public void setParam_format(String param_format) {
		this.param_format = param_format;
	}
	/**
	 * @return the param_lang
	 */
	public String getParam_lang() {
		return param_lang;
	}
	/**
	 * @param param_lang the param_lang to set
	 */
	public void setParam_lang(String param_lang) {
		this.param_lang = param_lang;
	}
	
}
