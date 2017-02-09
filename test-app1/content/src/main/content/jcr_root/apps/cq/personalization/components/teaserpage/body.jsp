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

--%><%@ page import="com.day.cq.i18n.I18n"
%><%@include file="/libs/foundation/global.jsp"%><%

    I18n i18n = new I18n(slingRequest);
%>
<body class="cq-ui-body">
<h1><%= i18n.get("Teaser") %> | <%= xssAPI.encodeForHTML(currentPage.getTitle()) %></h1>
<p><%= xssAPI.encodeForHTML(properties.get("jcr:description", "")) %></p>
<h2 class="no-icon"><%= i18n.get("Basic Properties")%></h2>
<ul>
    <li><%= i18n.get("Display location: ") %><strong><%= xssAPI.encodeForHTML(properties.get("location", "")) %></strong></li>
</ul>
<div id="cq-ui-header"><a href="javascript:window.top.location='/welcome';" class="home"></a></div>
<h2 class="no-icon"><%= i18n.get("Content")%></h2>
<p><%=i18n.get("The paragraph system below will be shown in teaser or target components:") %></p>
<div class="edit-box wide">
    <cq:include script="content.jsp" />
</div>
</body>
