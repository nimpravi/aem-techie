<%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.
--%><%@page session="false" contentType="text/html; charset=utf-8" %><%
%><%@ page import="com.day.cq.wcm.api.WCMMode,
                com.day.cq.commons.Doctype,
                java.util.ResourceBundle,
                com.day.cq.i18n.I18n,
                com.day.cq.wcm.api.Page,
                java.util.Iterator,
                org.apache.sling.api.resource.ResourceUtil,
                org.apache.sling.api.resource.Resource,
                org.apache.sling.commons.osgi.OsgiUtil,
                java.text.SimpleDateFormat,
                java.util.Calendar" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %>
<%@ taglib prefix="atom" uri="http://sling.apache.org/taglibs/atom/1.0" %>
<%
%><cq:defineObjects/><%

    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);
    final SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // read the redirect target from the 'page properties' and perform the
    // redirect if WCM is disabled.
    String location = properties.get("redirectTarget", "");
    if (WCMMode.fromRequest(request) == WCMMode.DISABLED && location.length() > 0) {
        // check for recursion
        if (!location.equals(currentPage.getPath())) {
            response.sendRedirect(request.getContextPath() + location + ".html");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return;
    }
    // set doctype
    Doctype.HTML_401_STRICT.toRequest(request);
    String title = currentPage.getTitle();

%><%= Doctype.fromRequest(request).getDeclaration() %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title><%= i18n.get("CQ5 Experience") %> | <%= xssAPI.encodeForHTML(currentPage.getTitle()) %></title>
    
    <%
    com.day.cq.widget.HtmlLibraryManager htmlMgr = sling.getService(com.day.cq.widget.HtmlLibraryManager.class);
    if (htmlMgr != null) {
        htmlMgr.writeIncludes(slingRequest, out, "cq.wcm.edit", "cq.tagging", "cq.personalization", "cq.analytics", "cq.security");
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
    <h1><%= i18n.get("Experience") %> | <%= xssAPI.encodeForHTML(currentPage.getTitle()) %></h1>
    <p><%= xssAPI.encodeForHTML(properties.get("jcr:description", "")) %></p>
    <h2 class="no-icon"><%= i18n.get("Basic Properties")%></h2>
    <ul>
    <%
        String[] segments = OsgiUtil.toStringArray(properties.get("cq:segments"));
        if (segments == null) {
            %><li><%= i18n.get("Experience is <strong>not</strong> targeted.") %></li><%
        } else {
            %><li><%= i18n.get("Experience is targeted at:") %>&nbsp;<%
            String separator = " ";
            for (String s : segments) {
                Resource r = resourceResolver.getResource(s);
                if (r != null) {
                    String segmentTitle = ResourceUtil.getValueMap(r.getChild("jcr:content")).get("jcr:title", r.getName());
                    %><%= separator %><strong><a href="<%= xssAPI.getValidHref(s) %>.html"><%= xssAPI.encodeForHTML(segmentTitle) %></a></strong><%
                    separator = ", ";
                }
            }
            %></li><%
        }
    %>
    </ul>
    <%
        Iterator<Page> iter = currentPage.listChildren();
        while (iter.hasNext()) {
            Page child = iter.next();
            %><%@include file="/libs/cq/personalization/components/campaignpage/item.jsp" %><%
        }
    %>
</div>
</body>
</html>
<%!
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