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

<%-- Returns the valid percentile options for the percentile component --%>
<%@ include file="/libs/foundation/global.jsp" %>
<%@ page contentType="application/json"
        import="org.apache.sling.commons.json.*" %>

<%
int percentileStep = 10;
int start = 0;
int end = 100;
JSONArray root = new JSONArray();
for ( int i = 0 ; i < end ; i += percentileStep ) {
    int startRange = i;
    int endRange = i+ percentileStep;
    
	JSONObject step = new JSONObject();
    step.put("value", startRange + "-" + endRange);
    step.put("text", startRange + "% - " + endRange + "%");
    
    root.put(step);
}
%>

<%= root.toString() %>
