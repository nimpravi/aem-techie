<%@page session="false"%><%--
  ADOBE CONFIDENTIAL
  __________________

   Copyright 2012 Adobe Systems Incorporated
   All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%>

<%-- Evaluates the percentile trait --%>

<%@ include file="/libs/foundation/global.jsp"  %>
<%@ page contentType="text/javascript;charset=UTF-8"
         import="org.apache.sling.commons.json.*" %>
<%
    JSONArray ranges = new JSONArray();
    String[] values = properties.get("value", new String[0]);
    for ( String value : values ) {
    	String[] range = value.split("-");
        if ( range.length != 2 ) // invalid segment definitoin
            continue;
        
        JSONObject jsonRange = new JSONObject();
        jsonRange.put("start", xssAPI.encodeForJSString(range[0]));
        jsonRange.put("end", xssAPI.encodeForJSString(range[1]));
        ranges.put(jsonRange);
    }
%>
CQ_Analytics.Percentile.matchesPercentiles(<%= ranges.toString() %>)
