<%@page session="false"%><%--
  ~
  ~ ADOBE CONFIDENTIAL
  ~ __________________
  ~
  ~  Copyright 2011 Adobe Systems Incorporated
  ~  All Rights Reserved.
  ~
  ~ NOTICE:  All information contained herein is, and remains
  ~ the property of Adobe Systems Incorporated and its suppliers,
  ~ if any.  The intellectual and technical concepts contained
  ~ herein are proprietary to Adobe Systems Incorporated and its
  ~ suppliers and are protected by trade secret or copyright law.
  ~ Dissemination of this information or reproduction of this material
  ~ is strictly forbidden unless prior written permission is obtained
  ~ from Adobe Systems Incorporated.
  --%><%@ page import="com.day.cq.commons.TidyJSONWriter,
                 org.apache.sling.commons.json.io.JSONWriter,
                 com.day.cq.tagging.Tag,
                 java.io.StringWriter"
        contentType="text/javascript" %><%!
%><%@ include file="/libs/foundation/global.jsp" %><%
    StringWriter buf = new StringWriter();

    TidyJSONWriter writer = new TidyJSONWriter(buf);
    writer.setTidy(true);

    try {
        Page cPage = null;
        if (request.getParameter("path") != null) {
            Resource r = resourceResolver.getResource(request.getParameter("path"));
            cPage = (r != null ? r.adaptTo(Page.class) : null);
        } else {
            cPage = currentPage;
        }

        setTagCloudInitialData(writer, cPage);

    } catch (Exception e) {
        log.error("Error while generating JSON tagcloud initial data", e);
    }
%>if( CQ_Analytics && CQ_Analytics.TagCloudMgr) {
    CQ_Analytics.TagCloudMgr.init(<%=buf%>);
}<%!
    void setTagCloudInitialData(JSONWriter writer, Page currentPage) throws Exception {
        writer.array();
        if (currentPage != null) {
            for (Tag tag : currentPage.getTags()) {
                writer.value(tag.getTagID());
            }
        }
        writer.endArray();
    }
%>
