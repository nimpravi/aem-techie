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
  --%>

<%@ page import="com.day.cq.commons.TidyJSONWriter,
                 org.apache.sling.commons.json.io.JSONWriter,
                 org.apache.sling.api.SlingHttpServletRequest,
                 java.net.URI,
                 java.io.StringWriter"
        contentType="text/javascript" %><%!
%><%@ include file="/libs/foundation/global.jsp" %><%
    StringWriter buf = new StringWriter();

    TidyJSONWriter writer = new TidyJSONWriter(buf);
    writer.setTidy(true);

    try {
        setSurferInfoInitialData(writer, slingRequest);
    } catch (Exception e) {
        log.error("Error while generating JSON surferinfo initial data", e);
    }
%>if( CQ_Analytics && CQ_Analytics.SurferInfoMgr) {
    CQ_Analytics.SurferInfoMgr.loadInitProperties(<%=buf%>, true);
}<%!
    void setSurferInfoInitialData(JSONWriter writer,
                                  SlingHttpServletRequest request) throws Exception {
        writer.object();
        String ip = request.getRemoteAddr();
        String keywords = null;
        String referer = request.getHeader("Referer");
        if (referer != null) {
            URI uri = new URI(referer);
            String query = uri.getQuery();
            if (query != null) {
                int qindex = query.indexOf("q=");
                if(qindex >-1) {
                    int andindex = query.indexOf("&",qindex);
                    keywords = query.substring(qindex+2,andindex > -1 ? andindex : query.length());
                    keywords = keywords.replaceAll("\\+", " ");
                }
            }
        }
        keywords = (keywords != null ? keywords : (request.getParameter("q") != null ? request.getParameter("q") : ""));
        keywords = keywords.replaceAll("<","&lt;");
        keywords = keywords.replaceAll(">","&gt;");
        writer.key("IP").value(ip);
        writer.key("keywords").value(keywords);
        writer.endObject();
    }
%>
