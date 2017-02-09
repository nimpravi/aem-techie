<%@page import="com.cts.accelerators.lifeplus.services.dto.*"%>
<%@page import="com.cts.accelerators.lifeplus.services.*"%>
<%@include file="/libs/foundation/global.jsp"%>
<% 
WeatherWidgetService weatherWidgetService=sling.getService(WeatherWidgetService.class);
WeatherWidgetServiceRequest weatherWidgetServiceRequest=new WeatherWidgetServiceRequest();
weatherWidgetServiceRequest.setParam_format("json");
weatherWidgetServiceRequest.setParam_lang("en");
weatherWidgetServiceRequest.setParam_location("Bangalore");
out.println(((WeatherWidgetServiceResponse)weatherWidgetService.execute(weatherWidgetServiceRequest)).getJsonResponse().toString());
%>