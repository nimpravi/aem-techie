<%@page session="false"%><%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Top Navigation component

  Draws the top navigation

--%><%@include file="/libs/foundation/global.jsp"%><%
%><%@ page import="java.util.Iterator,
        com.day.text.Text,
        com.day.cq.wcm.api.PageFilter,
        com.day.cq.wcm.api.Page,
        com.day.cq.commons.Doctype,
        org.apache.commons.lang3.StringEscapeUtils" 
            %>

<div class="col-md-10">
    <ul class="menu">
        
	<%
        long absParent = currentStyle.get("absParent", 2L);

        String navstart = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);
        if (navstart.equals("")) navstart=currentPage.getPath();
        Resource rootRes = slingRequest.getResourceResolver().getResource(navstart);
        Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);
if(null!=rootPage)
{
        Iterator<Page> children = rootPage.listChildren();
        while (children.hasNext()) {
            Page child = children.next();
            %>
            <a href="<%= child.getPath() %>.html"><li><%= StringEscapeUtils.escapeXml(child.getTitle()) %></li></a>    

        <%
        }}
    %>

    </ul>
</div>