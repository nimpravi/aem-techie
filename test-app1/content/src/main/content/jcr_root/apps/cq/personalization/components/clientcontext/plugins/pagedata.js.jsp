<%@page session="false"%><%--**********************************************************************
  *
  * ADOBE CONFIDENTIAL
  * __________________
  *
  *  Copyright 2011 Adobe Systems Incorporated
  *  All Rights Reserved.
  *
  * NOTICE:  All information contained herein is, and remains
  * the property of Adobe Systems Incorporated and its suppliers,
  * if any.  The intellectual and technical concepts contained
  * herein are proprietary to Adobe Systems Incorporated and its
  * suppliers and are protected by trade secret or copyright law.
  * Dissemination of this information or reproduction of this material
  * is strictly forbidden unless prior written permission is obtained
  * from Adobe Systems Incorporated.
  **********************************************************************--%><%!
%><%@ page import="com.day.cq.commons.TidyJSONWriter,
                 org.apache.sling.api.scripting.SlingScriptHelper,
                 org.apache.sling.commons.json.io.JSONWriter,
                 com.day.cq.wcm.api.WCMException,
                 com.day.cq.tagging.Tag,
                 com.day.cq.wcm.core.stats.PageViewStatistics,
                 java.io.StringWriter,
                 java.util.Date,
                 java.text.DecimalFormat,
                 java.util.Date,
                 java.util.Random"
        contentType="text/javascript"%><%!
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

        setPageInitialData(writer, sling, cPage);

    } catch (Exception e) {
        log.error("Error while generating JSON pagedata initial data", e);
    }

%>if( CQ_Analytics && CQ_Analytics.PageDataMgr) {
    CQ_Analytics.PageDataMgr.loadInitProperties(<%=buf%>, true);
}<%!
    void setPageInitialData(JSONWriter writer,
                            SlingScriptHelper sling,
                            Page currentPage) throws Exception {
        writer.object();
        if (currentPage != null) {
            long monthlyHits = 0;
            try {
                final PageViewStatistics pwSvc = sling.getService(PageViewStatistics.class);
                Object[] hits = pwSvc.report(currentPage);
                if (hits != null && hits.length > 2) {
                    monthlyHits = (Long) hits[2];
                }
            } catch (WCMException ex) {
                monthlyHits = -1;
            }
            writer.key("hits").value(monthlyHits);
            writer.key("title").value(currentPage.getTitle());
            writer.key("path").value(currentPage.getPath());

            String navTitle = currentPage.getNavigationTitle();
            if(navTitle == null) {
            	navTitle = currentPage.getTitle();
            }
            if(navTitle == null) {
            	navTitle = currentPage.getName();
            }
            writer.key("navTitle").value(navTitle);

            if(currentPage.getTemplate() != null) {
            	writer.key("template").value(currentPage.getTemplate().getPath());
                writer.key("thumbnail").value(currentPage.getTemplate().getThumbnailPath());
            }

            Tag[] tags = currentPage.getTags();
            String tagsStr = "";
            for(Tag tag: tags) {
                tagsStr += tag.getTitle() + " ";
            }
            writer.key("tags").value(tagsStr);
            String descr = currentPage.getDescription();
            writer.key("description").value(descr != null ? descr : "");

			Page siteLevelPage = currentPage.getAbsoluteParent(2);
            if ( siteLevelPage != null ){
                writer.key("sitesection").value(siteLevelPage.getName());
            }
			
			Page topLevelPage = currentPage.getAbsoluteParent(3);
            if ( topLevelPage!= null ){
                writer.key("subsection").value(topLevelPage.getName());
            } 
        }


        Random rand = new Random(new Date().getTime());
        DecimalFormat df = new DecimalFormat("0.00");
        double r = rand.nextDouble();
        writer.key("random").value(df.format(r));
        writer.endObject();
    }
%>
