<%@page session="false"%><%@page import="com.day.cq.analytics.AnalyticsConfiguration"%><%
%><%@include file="/libs/foundation/global.jsp"%><%
AnalyticsConfiguration analyticsConfig = (resourcePage != null) ? resourcePage.adaptTo(AnalyticsConfiguration.class) : null;
if (analyticsConfig != null) {
    String analyticsPath = analyticsConfig.getPath().replace("/jcr:content/analytics", "");
    String section;
    if(analyticsPath.equals(currentPage.getPath())) {
        section = "home";
    } else { // the section is the first 'folder' beginning at the analytics root node
        section = currentPage.getPath().replace(analyticsPath + "/", "");
        if (section.indexOf("/") > 0) {
            section = section.substring(0, section.indexOf("/"));
        }
    }
    %>CQ_Analytics.ClientContextUtils.onStoreRegistered("pagedata", function() {
        CQ_Analytics.PageDataMgr.addInitProperty('sitesection', '<%= section %>');
        CQ_Analytics.PageDataMgr.addInitProperty('url', '<%= request.getRequestURL() %>');
        CQ_Analytics.PageDataMgr.addInitProperty('urlType', '<%= (request.getQueryString() != null? "campaign":
                                                    (request.getRequestURI().indexOf(currentPage.getPath()) == -1 ? "vanity" : "standard") ) %>');
    });<%
}%>
