<%--
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

--%><%@page session="false" contentType="text/html; charset=utf-8" %><%
%><%@ page import="com.day.cq.commons.Doctype, com.day.cq.wcm.api.WCMMode" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/><%

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
    <title>CQ5 <%= xssAPI.encodeForHTML(component.getTitle()) %> | <%= xssAPI.encodeForHTML(title) %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%
    com.day.cq.widget.HtmlLibraryManager htmlMgr = sling.getService(com.day.cq.widget.HtmlLibraryManager.class);
    if (htmlMgr != null) {
        htmlMgr.writeIncludes(slingRequest, out, "cq.wcm.edit", "cq.tagging", "cq.personalization", "cq.security");
    }
    String dlgPath = null;
    if (editContext != null && editContext.getComponent() != null) {
        dlgPath = editContext.getComponent().getDialogPath();
    }
    currentDesign.writeCssIncludes(out);
    %>
    <script src="/libs/cq/ui/resources/cq-ui.js" type="text/javascript"></script>
    <script type="text/javascript" >
        CQ.WCM.launchSidekick("<%= currentPage.getPath() %>", {
            propsDialog: "<%= dlgPath == null ? "" : dlgPath %>",
            locked: <%= currentPage.isLocked() %>
        });
    </script>
</head>
<cq:include script="body.jsp"/>
</html>
