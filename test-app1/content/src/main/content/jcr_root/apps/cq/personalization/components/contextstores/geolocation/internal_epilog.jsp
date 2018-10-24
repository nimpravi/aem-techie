<%@page session="false"%><%@ page import="com.day.cq.wcm.api.WCMMode" %>
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
--%><%@include file="/libs/foundation/global.jsp"%><%
    String store = properties.get("store", currentStyle.get("store",String.class));
    if( store != null ) {
        if(WCMMode.fromRequest(request) != WCMMode.DISABLED) { %><script type="text/javascript">
            $CQ(function() {
                var handleGeolocationMap = function(event) {
                    if( !CQ_Analytics.GeolocMap) {
                        CQ.personalization.GeolocationUtils.createGeolocationMap(true, "<%=store%>");
                    } else {
                        CQ_Analytics.GeolocMap.show();
                    }

                    event.stopPropagation();
                };

                if( CQ_Analytics.ClientContextUI ) {
                    var renderer = function() {
                        $CQ(".cq-clientcontext .<%=store%> .cq-cc-thumbnail div").parent().bind("click", handleGeolocationMap);
                    };

                    if( !CQ_Analytics.ClientContextUI.rendered ) {
                        CQ_Analytics.ClientContextUI.addListener("render", renderer);
                    } else {
                        renderer();
                    }
                }
            });
        </script><% }
    }
%>
