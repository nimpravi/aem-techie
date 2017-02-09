<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Package component

  Displays and provides editing of a package

--%><%@ page contentType="text/html"
             pageEncoding="utf-8"
             import="com.day.text.Text" %>
<%@include file="/libs/foundation/global.jsp"%><%
    String id = "cq-segment-validitystatus";
    String segmentPath = currentPage.getPath();
%>
<div id="<%=id%>"></div>
<script type="text/javascript">
    $CQ(function() {
        if(CQ_Analytics.SegmentMgr) {
            CQ_Analytics.SegmentMgr.addListener("update",function() {
                updateStatus();
            });
            updateStatus();
        }

    });

    var updateStatus = function() {
        var elem = document.getElementById("<%=id%>");
        if(elem) {
            var resolve = CQ_Analytics.SegmentMgr.resolve("<%=segmentPath%>");
            var cssClass = "";
            var text = "";
            if(resolve === true) {
                cssClass = "cq-segment-validitystatus-resolved";
                text = CQ.I18n.getMessage("Data loaded in the Client Context resolve the current segment.");
            } else {
                if(resolve === false) {
                    cssClass = "cq-segment-validitystatus-unresolved";
                    text = CQ.I18n.getMessage("Data loaded in the Client Context do not resolve the current segment.");
                } else {
                    cssClass = "cq-segment-validitystatus-invalid";
                    text = CQ.I18n.getMessage("Segment is invalid: {0}", resolve);
                }
            }
            elem.innerHTML = '<ul><li><div class="li-bullet '+cssClass+'">'+text+'</div></li></ul>';
        }
    }

</script>
