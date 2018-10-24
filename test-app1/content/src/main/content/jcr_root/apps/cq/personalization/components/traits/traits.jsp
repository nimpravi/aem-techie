<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@ page import="com.day.cq.wcm.api.WCMMode"%><%!
%><%@include file="/libs/foundation/global.jsp"%><%
    String id = "trait-status-" + currentNode.getPath();
%><div class="segmenteditor-container-content" id="<%=xssAPI.encodeForHTMLAttr(id)%>">
<cq:include script="content.jsp" />
</div>
<div class="segmenteditor-container-logic-text">
<cq:include script="connection.jsp" />
</div>
<% if (WCMMode.fromRequest(request) != WCMMode.DISABLED) {%><cq:include script="status.jsp"/><% }%>
