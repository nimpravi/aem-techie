<%@page session="false"%><%--
  Copyright 1997-2011 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  OpenSocial component.

--%><%@include file="/libs/foundation/global.jsp"%><%
%><%@ page import="org.apache.commons.lang3.StringEscapeUtils,
        org.apache.sling.api.resource.ResourceUtil" %>
        <cq:includeClientLib categories="cq.opensocial.container"/>
        <%
        final String specUrl = properties.get("specUrl", "");
        final String id = "gadget-id-"+currentNode.getName();

    if ("".equals(specUrl)) {
        %><div><img src="/etc/designs/default/0.gif" class="cq-gadget-placeholder"/></div><%
    } else {
        final String resourcePath = new StringBuilder(request.getContextPath())
                .append(slingRequest.getResource().getPath())
                .append(".userprefs.json")
                .toString();
        %>
        <div id="<%= id %>" class="gadgets-gadget"></div> 
        <script type="text/javascript">renderGadget({specUrl: '<%= StringEscapeUtils.escapeEcmaScript(specUrl) %>'},'<%= id %>','<%= resourcePath %>');</script>
        <%
    }
    %>    
<div class="clear"></div>
