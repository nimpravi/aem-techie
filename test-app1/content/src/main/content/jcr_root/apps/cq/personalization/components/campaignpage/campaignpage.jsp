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

--%><%@ page import="java.text.SimpleDateFormat,
                 java.util.Calendar,
                 java.util.Iterator,
                 com.day.cq.wcm.api.Page,
                 com.day.cq.widget.HtmlLibraryManager,
                 java.util.ResourceBundle,
                 com.day.cq.wcm.api.WCMMode" %><%
%><%@include file="/libs/foundation/global.jsp" %><%

    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);
    final SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    String campaignTitle = currentPage.getTitle();
%><html>
<head>
    <title><%= i18n.get("CQ5 Campaign") %> | <%= xssAPI.encodeForHTML(campaignTitle) %>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%
        HtmlLibraryManager htmlMgr = sling.getService(HtmlLibraryManager.class);
        if (htmlMgr != null) {
            htmlMgr.writeIncludes(slingRequest, out, "cq.wcm.edit", "cq.security", "cq.personalization");
        }
        String dlgPath = null;
        if (editContext != null && editContext.getComponent() != null) {
            dlgPath = editContext.getComponent().getDialogPath();
        }
        currentDesign.writeCssIncludes(out);
    %>
    <script src="/libs/cq/ui/resources/cq-ui.js" type="text/javascript"></script>
</head>
<body class="cq-ui-body editbar-container">
<div class="editbar-container">
    <%
        // draw the 'edit' bar explicitly
        out.flush();
        if (editContext != null) {
            editContext.includeEpilog(slingRequest, slingResponse, WCMMode.EDIT);
        }
    %>
    <h1><%= i18n.get("Campaign") %> | <%= xssAPI.encodeForHTML(campaignTitle) %></h1>
    <p><%= xssAPI.encodeForHTML(properties.get("jcr:description", "")) %></p>
    <h2 class="no-icon"><%= i18n.get("Basic Properties")%></h2>
    <ul>
        <li><%= i18n.get("Campaign priority is") %>&nbsp;<strong><%= xssAPI.getValidInteger(properties.get("priority", ""), 100) %></strong></li>
        <li><%= currentPage.isValid() ?
                    i18n.get("Campaign is <strong>active</strong>:") :
                    i18n.get("Campaign is <strong>inactive</strong>:") %>
            <%= i18n.get("on/off times are") %> <strong><%= formatOnOffTimes(i18n, dateFmt, currentPage.getOnTime(), currentPage.getOffTime()) %></strong></li>
    </ul>
    <%
        Iterator<Page> iter = currentPage.listChildren();
        while (iter.hasNext()) {
            Page child = iter.next();
            %><%@include file="item.jsp" %><%
        }
    %>
</div>
</body>
</html><%!
    public static String formatDate(I18n i18n, SimpleDateFormat dateFmt, Calendar date) {
        if (date == null) {
            return i18n.get("not defined");
        } else {
            return dateFmt.format(date.getTime());
        }
    }

    public static String formatOnOffTimes(I18n i18n, SimpleDateFormat dateFmt, Calendar onDate, Calendar offDate) {
        return formatDate(i18n, dateFmt, onDate) + " / " + formatDate(i18n, dateFmt, offDate);
    }
%>
