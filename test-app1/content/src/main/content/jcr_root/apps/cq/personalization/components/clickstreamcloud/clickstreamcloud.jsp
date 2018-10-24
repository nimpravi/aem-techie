<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@ include file="/libs/foundation/global.jsp" %><%
%><%@ page import="com.day.cq.personalization.ClientContextUtil,
                   com.day.cq.wcm.api.WCMMode, java.util.Iterator" %><%
    if (WCMMode.fromRequest(request) == WCMMode.DISABLED) {
        %><cq:includeClientLib categories="personalization.kernel"/><%
    } else {
        //include personalization editing widgets
        %><cq:includeClientLib categories="cq.personalization"/><%
    }
    String segmentsPath = "/etc/segmentation";
    String ccPath = currentStyle.get("path","/etc/clientcontext/default");
    String currentPath = currentPage != null ? currentPage.getPath() : "";

%><script type="text/javascript">
    $CQ(function() {
        CQ_Analytics.SegmentMgr.loadSegments("<%=segmentsPath%>");
        CQ_Analytics.ClientContextMgr.PATH = "<%=ccPath%>";
    });
</script>

<script type="text/javascript" src="<%=request.getContextPath()%>/etc/clientcontext/legacy/config.init.js?path=<%=xssAPI.getValidHref(currentPath)%>"></script><%

%><div id="clickstreamcloud-gui"></div><script type="text/javascript">
    $CQ(function() {
        //old style load config
        CQ_Analytics.ClickstreamcloudMgr.loadConfig();
    })
</script>
