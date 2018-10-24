<%@include file="/libs/foundation/global.jsp"%>


<%@ page import="java.util.Iterator,
        com.day.cq.wcm.api.PageFilter,
        com.day.cq.wcm.api.NameConstants,
        com.day.cq.wcm.api.Page" 
            %>


<div class="row">
<div class="span12">
	<ul class="breadcrumb">

            <%

    long level = currentStyle.get("absParent", 2L);
    long endLevel = currentStyle.get("relParent", 1L);
String delimStr = currentStyle.get("delim", "&#47;");
    String trailStr = currentStyle.get("trail", "");
    int currentLevel = currentPage.getDepth();
    String delim = "";
    while (level < (currentLevel - endLevel)+1) {
        Page trail = currentPage.getAbsoluteParent((int) level);
        if (trail == null) {
            break;
        }
        String title = trail.getNavigationTitle();
        if (title == null || title.equals("")) {
            title = trail.getNavigationTitle();
        }
        if (title == null || title.equals("")) {
            title = trail.getTitle();
        }
        if (title == null || title.equals("")) {
            title = trail.getName();
        }
        %>


<%= xssAPI.filterHTML(delim) %><%
        %><li><a href="<%= xssAPI.getValidHref(trail.getPath()+".html") %>"
             onclick="CQ_Analytics.record({event:'followBreadcrumb',values: { breadcrumbPath: '<%= xssAPI.getValidHref(trail.getPath()) %>' },collect: false,options: { obj: this },componentPath: '<%=resource.getResourceType()%>'})"><%
        %><%= xssAPI.encodeForHTML(title) %><%
    %></a></li><%

    delim = delimStr;
        level++;
    }
    if (trailStr.length() > 0) {
        %><%= xssAPI.filterHTML(trailStr) %><%
    }

%>

				</ul>
