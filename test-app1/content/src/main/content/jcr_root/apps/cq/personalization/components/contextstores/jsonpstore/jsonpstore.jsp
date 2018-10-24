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
  --%><%!
%><%@include file="/libs/foundation/global.jsp"%><%!
%><%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %><div class="cq-cc-store"><%
    String store = properties.get("store", currentStyle.get("store",String.class));
    String serviceURL = properties.get("serviceURL", currentStyle.get("serviceURL",String.class));

    //TODO: thumbnail code is copied at 3 different places. Generalize
    String thumbnail = properties.get("thumbnail",String.class);
    boolean hasThumbnail = thumbnail != null;
    String thumbnailDynamicValue = null;
    String thumbnailProperty = thumbnail;
    if( hasThumbnail ) {
        %><div class="cq-cc-thumbnail"><%
            if( isImage(thumbnail) || isURL(thumbnail)) {
                thumbnailProperty = "generatedThumbnail";
                if( isImage(thumbnail) ) {
                    thumbnailDynamicValue = request.getContextPath() + thumbnail;
                } else {
                    thumbnailDynamicValue = thumbnail;
                }
            }
            %><div class="cq-cc-store-property"><personalization:storePropertyTag propertyName="<%=thumbnailProperty%>" store="<%=store%>"/></div><%
        %></div><%
    }

    %><div class="cq-cc-content"><%

    if( store != null && serviceURL != null ) {
        %><script type="text/javascript">
            $CQ(function() {
                var store = CQ_Analytics.StoreRegistry.getStore("<%=store%>");
                if( store ) {
                    store.addInitProperty("<%=thumbnailProperty%>", "<%=thumbnailDynamicValue%>");
                    store.setProperty("<%=thumbnailProperty%>", "<%=thumbnailDynamicValue%>");
                } else {
                    CQ_Analytics.JSONPStore.registerNewInstance("<%=store%>", "<%=serviceURL%>", {
                        <% if( thumbnailDynamicValue != null ) { %>
                            "<%=thumbnailProperty%>": "<%=thumbnailDynamicValue%>"
                        <% } %>
                    });
                }
            });
        </script><%
    }

    Object[] props = null;
    try {
        props = properties.get("properties", currentStyle.get("properties",Object[].class));
    } catch (Exception e) {
        props = new Object[] {
                properties.get("properties", currentStyle.get("properties",Object.class))
        };
    }
    //props = props != null ? props : currentStyle.get("properties",String[].class);
    if(store != null && props != null ) {
        int i = 0;
        for(Object prop: props) {
            %><div class="cq-cc-store-property cq-cc-store-property-level<%=(i++)%>"><personalization:storePropertyTag propertyName="<%=prop.toString()%>" store="<%=store%>"/></div><%
        }
    }
  %></div>
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
