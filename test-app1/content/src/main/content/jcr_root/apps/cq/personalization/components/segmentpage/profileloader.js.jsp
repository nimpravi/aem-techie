<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@ page import="" %>
<%@ page import="com.day.text.Text, com.adobe.granite.security.user.UserProperties, com.adobe.granite.security.user.UserPropertiesManager" %>
<%!
%><%@include file="/libs/foundation/global.jsp" %><%

    String authorizableId = request.getParameter("authorizableId");
    UserProperties userProperties;
    if (authorizableId != null) {
        final UserPropertiesManager upm = resourceResolver.adaptTo(UserPropertiesManager.class);
        userProperties = upm.getUserProperties(authorizableId, "profile");
    } else {
        userProperties = upm.getUserProperties(resourceResolver.getUserID(), "profile");
    }
    if (userProperties != null) {
        for (String key : userProperties.getPropertyNames()) {
            if (!key.startsWith("jcr:") && !key.startsWith("sling:") && !key.startsWith("cq:last")) {
                String s = userProperties.getProperty(key, null, String.class);
                s = s != null ? s.replaceAll("'","\\\\'") : "";
                out.println("CQ_Analytics.ProfileDataMgr.setProperty('" + key + "','" + s + "');");
            }
        }
    }
%>
