<%--

 ADOBE CONFIDENTIAL
 __________________

  Copyright 2012 Adobe Systems Incorporated
  All Rights Reserved.

 NOTICE:  All information contained herein is, and remains
 the property of Adobe Systems Incorporated and its suppliers,
 if any.  The intellectual and technical concepts contained
 herein are proprietary to Adobe Systems Incorporated and its
 suppliers and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material
 is strictly forbidden unless prior written permission is obtained
 from Adobe Systems Incorporated.

  ==============================================================================

  Feed link component

  Draws a feed link. Optionally, it can display a text along with a feed icon

--%><%@ page session="false" import="com.day.cq.wcm.api.WCMMode" %><%
%><%@include file="/libs/foundation/global.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<cq:includeClientLib categories="cq.social.feedlink" /><%

    String text = properties.get("text", "");
    boolean icon = properties.get("icon", false);
    String format = properties.get("format", "atom");
    String type = format.equals("atom") ? "application/atom+xml" : "application/rss+xml";
    String title = format.equals("atom") ? "Atom 1.0" : "RSS 2.0";
    String suffix = format.equals("atom") ? ".customfeeds" : ".customfeeds.rss.xml";
    String link = currentPage.getPath() + suffix;
    String iconCls = icon ? " feedlink-icon" : "";

    if (text.length() > 0 || icon) {
        %><a id="feed-link" class="feedlink-text<%= xssAPI.encodeForHTMLAttr(iconCls) %>" <%
            %>href="<%= xssAPI.getValidHref(link) %>" <% 
            %>target="_blank" title="<%= xssAPI.encodeForHTMLAttr(title) %>" <%
        %>><%
            if (text.length() > 0) {
                %><%= xssAPI.filterHTML(text) %><%
            }
        %></a><%
    } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
        %><img src="/libs/cq/ui/resources/0.gif" class="cq-feedlink-placeholder" alt=""><%
    }

%><link rel="alternate" <%
    %>type="<%= xssAPI.encodeForHTMLAttr(type) %>" <%
    %>title="<%= xssAPI.encodeForHTMLAttr(title) %> (Page)" <%
    %>href="<%= xssAPI.getValidHref(link) %>" />

    <c:set var="list" value="<%=properties.get("feedFields",String[].class) %>" />
    <c:forEach items="${list}" var="listItems" varStatus="itr">
        <input type="hidden" id="feedFields${itr.count}" value="${listItems}" />
    </c:forEach>

<script>

$( document ).ready(function() {
    $( "#feed-links" ).click(function() {
        var pagePath = "${currentPage.path}";
        var requestType = "feed-link";
        var json = "";
        var format = "${properties.format}";
        var i = 1;
        var feedFields = "";
        while($("#feedFields"+i).val() != undefined) {
			feedFields = feedFields + ";" + $("#feedFields"+i).val();
            i++;
        }
        var queryParams = "requestType="+requestType+"&listroot="+pagePath+"&format="+format+"&feedFields="+feedFields;
        $.post("/bin/CustomFeedServlet",queryParams, function(data) {
            alert("data :: "+data);
        });
    });
});

</script>


