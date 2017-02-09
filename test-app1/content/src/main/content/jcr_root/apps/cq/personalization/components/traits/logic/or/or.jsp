<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.
--%><%@include file="/libs/foundation/global.jsp"%><%
%><%@ page import="
    java.util.ResourceBundle,
    com.day.cq.i18n.I18n" %><%

    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);

    String id = "trait-status-" + currentNode.getPath();

%><div class="segmenteditor-or">
    <div class="segmenteditor-container-or-header" id="<%=xssAPI.encodeForHTMLAttr(id)%>"><%= i18n.get("OR", "Logical OR in ClientContext segment editor") %></div>
    <div class="segmenteditor-container-or">
        <cq:include path="orpar" resourceType="foundation/components/parsys"/>
    </div>
</div>
<div class="segmenteditor-container-logic-text">
    <cq:include script="../connection.jsp" />
</div>
<%-- if (WCMMode.fromRequest(request) != WCMMode.DISABLED) {%><cq:include script="../status.jsp"/><% }--%>
