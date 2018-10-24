package com.cts.accelerators.lifeplus.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.services.dto.WeatherWidgetServiceRequest;
import com.cts.accelerators.lifeplus.services.dto.WeatherWidgetServiceResponse;

public class WeatherWidgetHelper {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WeatherWidgetHelper.class);
	private static final long serialVersionUID = 1L;

	public static WeatherWidgetServiceResponse fetchWeatherReport(
			WeatherWidgetServiceRequest serviceRequest){
		String methodName = "fetchWeatherReport";
		HttpURLConnection con = null;
		LOGGER.info(" || " + methodName + " || START");
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"proxy.cognizant.com", 6050));
		LOGGER.info(">>>>>>>>>>>proxy>>>>>>>>>>>>>>>> " + proxy);
		StringBuilder urlBuilder=new StringBuilder(serviceRequest.getApiUrl().endsWith("?")?serviceRequest.getApiUrl():serviceRequest.getApiUrl()+"?");
		WeatherWidgetServiceResponse weatherWidgetServiceResponse=new WeatherWidgetServiceResponse();
		JSONObject jsonResponse=new JSONObject();
		if(StringUtils.isNotEmpty(serviceRequest.getParam_location())){
			urlBuilder.append("q="+serviceRequest.getParam_location()+"&");
		}
		if(StringUtils.isNotEmpty(serviceRequest.getApiKey())){
			urlBuilder.append("key="+serviceRequest.getApiKey()+"&");
		}
		if(StringUtils.isNotEmpty(serviceRequest.getParam_format())){
			urlBuilder.append("format="+serviceRequest.getParam_format()+"&");
		}
		if(StringUtils.isNotEmpty(serviceRequest.getParam_lang())){
			urlBuilder.append("langt="+serviceRequest.getParam_lang());
		}
		try {
			URL obj = new URL(urlBuilder.toString());
			LOGGER.info(">>>>>>>>>>>obj>>>>>>>>>>>>>>>> " + obj);
			con = (HttpURLConnection) obj.openConnection(proxy);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			LOGGER.info(">>>>>>>>>>>br>>>>>>>>>>>>>>>> " + br);
			
			StringBuilder inputString = new StringBuilder();

			StringBuilder data = new StringBuilder();
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				data = inputString.append(inputLine);
				LOGGER.info(">>>>>>>>>>>data>>>>>>>>>>>>>>>> " + data);
			}
			JSONObject weatherJSON=null;
			if(StringUtils.isNotEmpty(data.toString())){
				weatherJSON= new JSONObject(data.toString());	
			}
			String[] temperaturePath;
			Object tempJSON=new Object();
			if(StringUtils.isNotEmpty(serviceRequest.getTemperature())){
				temperaturePath=serviceRequest.getTemperature().split("\\.");
				if(null!=weatherJSON){
					for(int i=0;i<temperaturePath.length;i++){
						if(i==0){
							//tempJSON=weatherJSON.get(temperaturePath[i]);
							JSONObject jsonObject=new JSONObject(weatherJSON.toString());
							String[] tempArray=null;
							if(temperaturePath[i].contains("`")){
								tempArray=temperaturePath[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(temperaturePath[i]);
								if((i+1)==temperaturePath.length){
									jsonResponse.put("temperature", tempJSON.toString());
								}
							}
						}
						else{
							JSONObject jsonObject=new JSONObject(tempJSON.toString());
							String[] tempArray=null;
							if(temperaturePath[i].contains("`")){
								tempArray=temperaturePath[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(temperaturePath[i]);
								if((i+1)==temperaturePath.length){
									jsonResponse.put("temperature", tempJSON.toString());
								}
							}
						}
					}
				}
			}
			String[] cloudCover;
			if(StringUtils.isNotEmpty(serviceRequest.getCloudCover())){
				cloudCover=serviceRequest.getCloudCover().split("\\.");
				if(null!=weatherJSON){
					for(int i=0;i<cloudCover.length;i++){
						if(i==0){
							//tempJSON=weatherJSON.get(cloudCover[i]);
							JSONObject jsonObject=new JSONObject(weatherJSON.toString());
							String[] tempArray=null;
							if(cloudCover[i].contains("`")){
								tempArray=cloudCover[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(cloudCover[i]);
								if((i+1)==cloudCover.length){
									jsonResponse.put("cloudcover", tempJSON.toString());
								}
							}
						}
						else{
							JSONObject jsonObject=new JSONObject(tempJSON.toString());
							String[] tempArray=null;
							if(cloudCover[i].contains("`")){
								tempArray=cloudCover[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(cloudCover[i]);
								if((i+1)==cloudCover.length){
									jsonResponse.put("cloudcover", tempJSON.toString());
								}
							}
						}
					}
				}
			
			}
			String[] humidity;
			if(StringUtils.isNotEmpty(serviceRequest.getHumidity())){
				humidity=serviceRequest.getHumidity().split("\\.");
				if(null!=weatherJSON){
					for(int i=0;i<humidity.length;i++){
						if(i==0){
							//tempJSON=weatherJSON.get(humidity[i]);
							JSONObject jsonObject=new JSONObject(weatherJSON.toString());
							String[] tempArray=null;
							if(humidity[i].contains("`")){
								tempArray=humidity[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(humidity[i]);
								if((i+1)==humidity.length){
									jsonResponse.put("humidity", tempJSON.toString());
								}
							}
						}
						else{
							JSONObject jsonObject=new JSONObject(tempJSON.toString());
							String[] tempArray=null;
							if(humidity[i].contains("`")){
								tempArray=humidity[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(humidity[i]);
								if((i+1)==humidity.length){
									jsonResponse.put("humidity", tempJSON.toString());
								}
							}
						}
					}
				}
			
			
			}
			
			String[] feelsLikeTemperature;
			if(StringUtils.isNotEmpty(serviceRequest.getFeelsLikeTemperature())){
				feelsLikeTemperature=serviceRequest.getFeelsLikeTemperature().split("\\.");
				if(null!=weatherJSON){
					for(int i=0;i<feelsLikeTemperature.length;i++){
						if(i==0){
							//tempJSON=weatherJSON.get(feelsLikeTemperature[i]);
							JSONObject jsonObject=new JSONObject(weatherJSON.toString());
							String[] tempArray=null;
							if(feelsLikeTemperature[i].contains("`")){
								tempArray=feelsLikeTemperature[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(feelsLikeTemperature[i]);
								if((i+1)==feelsLikeTemperature.length){
									jsonResponse.put("feelsLikeTemperature", tempJSON.toString());
								}
							}
						}
						else{
							JSONObject jsonObject=new JSONObject(tempJSON.toString());
							String[] tempArray=null;
							if(feelsLikeTemperature[i].contains("`")){
								tempArray=feelsLikeTemperature[i].split("`");
								if(tempArray.length==2){
									JSONArray jsonArray=jsonObject.getJSONArray(tempArray[0]);
									tempJSON=jsonArray.get(Integer.parseInt(tempArray[1]));
								}
							}
							else
							{
								tempJSON=jsonObject.get(feelsLikeTemperature[i]);
								if((i+1)==feelsLikeTemperature.length){
									jsonResponse.put("feelsLikeTemperature", tempJSON.toString());
								}
							}
						}
					}
				}
			
			
			}
			
			
		} catch (MalformedURLException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					, e);
		} catch (IOException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					, e);
		}
		catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					, e);
		}
		weatherWidgetServiceResponse.setJsonResponse(jsonResponse);
		LOGGER.info(" || " + methodName + " || END");
		return weatherWidgetServiceResponse;
	}
}
