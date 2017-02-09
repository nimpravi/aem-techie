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
%><%@ page import="com.day.cq.wcm.api.WCMMode, org.apache.commons.lang3.StringEscapeUtils, com.day.cq.commons.Doctype" %><%
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
    <title>CQ5 <%= StringEscapeUtils.escapeHtml4(component.getTitle()) %> | <%= StringEscapeUtils.escapeHtml4(title) %>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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
    <link href="/libs/cq/ui/resources/cq-ui-appl.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" >
        CQ.WCM.launchSidekick("<%= currentPage.getPath() %>", {
            propsDialog: "<%= dlgPath == null ? "" : dlgPath %>",
            locked: <%= currentPage.isLocked() %>
        });
    </script>
</head>
<cq:include script="body.jsp"/>
</html>
