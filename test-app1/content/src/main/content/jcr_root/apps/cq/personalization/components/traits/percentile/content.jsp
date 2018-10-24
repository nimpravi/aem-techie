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

<%-- Renders the percentile trait --%>
<%@include file="/libs/foundation/global.jsp"%>
<%@page import="com.day.cq.wcm.api.WCMMode,
                  com.day.cq.i18n.I18n"%>
<%
    String[] values = properties.get("value", new String[0]);
    I18n i18n = new I18n(request);

    if( values.length > 0 ) {
        %>
        <div id="<%=xssAPI.encodeForHTMLAttr(currentNode.getPath())%>">
            <div class="segmenteditor-operator">
                <%= i18n.get("Matching percentiles : ") %>
                <% for ( int i = 0 ; i < values.length; i++ ) { %>
                    <strong> <%= xssAPI.encodeForHTML(values[i]) %></strong>
                    <% if ( i + 1 != values.length ) out.print(" ,"); %>
                <% } %>
            </div>    
        </div>
<%
    } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
        %><img src="/libs/cq/ui/resources/0.gif" class="cq-teaser-placeholder" alt=""><%
    }
%>
