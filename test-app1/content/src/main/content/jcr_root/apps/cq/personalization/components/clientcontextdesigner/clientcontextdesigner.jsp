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
  --%><%@ page contentType="text/html"
             pageEncoding="utf-8"
             import="com.day.text.Text" %><%!
%><%@include file="/libs/foundation/global.jsp"%><%

    String title = properties.get("jcr:title", Text.getName(resource.getPath()));
    String segmentsPath = "/etc/segmentation";
    String ccPath = Text.getRelativeParent(currentPage.getPath(), 1);

%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
<head>
    <title>CQ5 Client Context | <%= title %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <cq:includeClientLib categories="cq.wcm.edit,cq.tagging,cq.personalization,cq.security"/>
    <%
    String dlgPath = null;
    if (editContext != null && editContext.getComponent() != null) {
        dlgPath = editContext.getComponent().getDialogPath();
    }
    %>
    <script src="/libs/cq/ui/resources/cq-ui.js" type="text/javascript"></script>
    <script type="text/javascript" >
        CQ.WCM.launchSidekick("<%= currentPage.getPath() %>", {
            propsDialog: "<%= dlgPath == null ? "" : dlgPath %>",
            locked: <%= currentPage.isLocked() %>
        });
    </script>
</head>
<body>
    <h1 class="no-icon">Client Context</h1>
    <p></p>
    <div id="cq-clientcontext-edit-box" class="cq-clientcontext">
        <div id="cq-clientcontext-box-actions"><div id="cq-clientcontext-box-actions-container"></div></div>
        <div id="cq-clientcontext-box-content"><cq:include path="stores" resourceType="cq/personalization/components/clientcontextdesigner/parsys"/></div>
    </div>
    <script type="text/javascript">
        $CQ(function() {
            CQ_Analytics.SegmentMgr.loadSegments("<%=segmentsPath%>");
            CQ_Analytics.ClientContextUtils.init("<%=ccPath%>");

            $CQ("<div>")
                    .addClass("cq-clientcontext-box-action")
                    .addClass("cq-clientcontext-load")
                    .attr("title", CQ.I18n.getMessage("Load a profile in the ClientContext"))
                    .appendTo("#cq-clientcontext-box-actions-container")
                    .bind("click",function() {
                        var dlg = new CQ.personalization.ProfileLoader({});
                        dlg.show();
                    });
            $CQ("<div>")
                    .addClass("cq-clientcontext-box-action")
                    .addClass("cq-clientcontext-reset")
                    .attr("title", CQ.I18n.getMessage("Reset the ClientContext"))
                    .appendTo("#cq-clientcontext-box-actions-container")
                    .bind("click",function() {
                        CQ_Analytics.ClientContext.reset();
                    });
        });
    </script>
</body>
</html>
