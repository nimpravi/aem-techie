<%@page session="false"%><%@ page import="com.adobe.cq.social.commons.CollabUtil" %>
<%--
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
--%><%!
%><%@include file="/libs/foundation/global.jsp"%><%!
%><%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %><div class="cq-cc-store">
    <cq:include script="internal_prolog.jsp"/>
    <cq:include script="prolog.jsp"/><%
    String store = "profile";
    String[] profileData = properties.get("properties", String[].class);

    String thumbnail = properties.get("thumbnail",String.class);
    boolean hasThumbnail = thumbnail != null;
    String thumbnailDynamicValue = null;
    String thumbnailProperty = thumbnail;
    if( hasThumbnail ) {
        %><div class="cq-cc-thumbnail cq-cc-thumbnail-profiledata"><%
            if( isImage(thumbnail) || isURL(thumbnail)) {
                thumbnailProperty = "generatedThumbnail";
                if( isImage(thumbnail) ) {
                    thumbnailDynamicValue = request.getContextPath() + thumbnail;
                } else {
                    thumbnailDynamicValue = thumbnail;
                }

                //register extra generated property to the store
                %><script type="text/javascript">
                    $CQ(function() {
                        var init = function(store) {
                            store.addInitProperty("<%=thumbnailProperty%>","<%=thumbnailDynamicValue%>");
                            store.setProperty("<%=thumbnailProperty%>","<%=thumbnailDynamicValue%>");
                        };
                        CQ_Analytics.ClientContextUtils.onStoreRegistered("<%=store%>", init);
                    });
                </script><%
            }
            %><div class="cq-cc-store-property"><personalization:storePropertyTag propertyName="<%=thumbnailProperty%>" store="<%=store%>"/></div><%
        %></div><%
    }

    %><div class="cq-cc-content"><%
    if(profileData != null ) {
        int i = 0;
        for(String data: profileData) {
            if( !hasThumbnail || !thumbnail.equals(data)) {
                String defaultValue = "";
                if( "avatar".equals(data)) {
                    defaultValue = "/etc/designs/default/images/social/avatar.png.prof.thumbnail.png";
                }
                %><div class="cq-cc-store-property cq-cc-store-property-level<%=(i++)%>"><personalization:storePropertyTag propertyName="<%=data%>" store="profile" defaultValue="<%=defaultValue%>"/></div><%
            }
        }
    }
    %></div>
    <cq:include script="internal_epilog.jsp"/>
    <cq:include script="epilog.jsp"/>
    <div class="cq-cc-clear"></div>
</div><%!

    boolean isImage(String value) {
        return value != null && (value.toLowerCase().contains(".png")
                                        || value.toLowerCase().contains(".jpg")
                                        || value.toLowerCase().contains(".gif"));
    }

    boolean isURL(String value) {
        return value != null && value.indexOf("://") != -1;
    }
%>
