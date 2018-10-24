<%@page session="false"%><%--
  ************************************************************************
  ADOBE CONFIDENTIAL
  ___________________

  Copyright 2011 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
  ************************************************************************
  --%>
<%@page import="com.adobe.cq.aam.client.spi.AudienceManagerConfiguration" %>
<%@page trimDirectiveWhitespaces="true" %><%!
%><%@include file="/libs/foundation/global.jsp"%><%!
%><%@include file="/libs/wcm/global.jsp"%><%!
%><%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %><%!
%>
<cq:setContentBundle/>
<%
    AudienceManagerConfiguration amConfig = resource.adaptTo(AudienceManagerConfiguration.class);
    if ( amConfig != null ) {
%>
<div class="cq-cc-store"><fmt:message key="Audience Manager Segments"/><personalization:storeRendererTag store="aamsegments"/></div>
<%  } else { %>
<div class="cq-cc-store"><fmt:message key="No Audience Manager configuration found, please configure"/></div>
<%  } %>
