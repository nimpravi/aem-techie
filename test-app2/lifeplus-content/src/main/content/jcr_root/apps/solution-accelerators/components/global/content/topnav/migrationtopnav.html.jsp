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
     <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12" id="nav">
          <ul class="nav navbar menu col-lg-11 col-md-11 col-sm-1 col-xs-1">
    <%

        long absParent = currentStyle.get("absParent", 2L);
    
        String navstart = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);

    
        if (navstart.equals("")) navstart=currentPage.getPath();
    
        Resource rootRes = slingRequest.getResourceResolver().getResource(navstart);
        Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);
        String xs = Doctype.isXHTML(request) ? "/" : "";
        if (rootPage != null) {
            String homeclass = ((rootPage.getName()).equals(currentPage.getName()))? "active-home": "home_icon";
    %>
        <li class="<%=homeclass%> visible-lg visible-md"><a href="<%= rootPage.getPath() %>.html"><img src="/content/dam/solution-accelerators/home-icon.png" alt="Home"></a></li>
        <li class="home_icon visible-xs visible-sm"><a href="<%= rootPage.getPath() %>.html"><img src="/content/dam/solution-accelerators/home-icon.png" alt="Home"></a></li>
    <% 
            Iterator<Page> children = rootPage.listChildren(new PageFilter(request));
            while (children.hasNext()) {
                Page child = children.next();
                String divclass = ((child.getName()).equals(currentPage.getName()))? "active": "";
                %>
                  <li class="<%=divclass%> visible-lg visible-md" ><a href="<%= child.getPath() %>.html"><%= StringEscapeUtils.escapeXml(child.getTitle()) %></a>
    
                <%
            }
    	}
    %>


          </li>  </ul>

<div class="visible-xs col-xs-6 menu-arrow btn-group">
            <div class="button">          
            </div>
         </div>
        <div class="btn-group visible-xs visible-sm col-sm-6 hidden-xs">
            <div type="button" id="tab-button" class="btn button btn-default dropdown-toggle" data-toggle="dropdown">          
            </div>
            <ul class="dropdown-menu" role="menu">
            <%
                if (rootPage != null) {
                    Iterator<Page> children = rootPage.listChildren(new PageFilter(request));
                    while (children.hasNext()) {
                        Page child = children.next();
                        %>
                            <li><a href="<%= child.getPath() %>.html"><%= StringEscapeUtils.escapeXml(child.getTitle()) %></a>
                                
                                <%
                    }
                }
             %>
            </ul>
          </div>
         <!-- <div class="search col-lg-4 col-md-4 col-sm-4 col-xs-5"><a href="#">Search</a></div> -->
</div>

<ul class="slide-menu col-lg-12" role="menu">
     <%
     	if (rootPage != null) {
        	Iterator<Page> children = rootPage.listChildren(new PageFilter(request));
            while (children.hasNext()) {
            Page child = children.next();
            %>
            <li><a href="<%= child.getPath() %>.html"><%= StringEscapeUtils.escapeXml(child.getTitle()) %></a>
			<%
            }
         }
      %>
    </ul>

<script>
$(document).ready(function(){
    $(".box a").tooltip(); 
	$(".menu-arrow .button").click(function(){
		$('.slide-menu').slideToggle();
	}); 
    $("#tab-button").click(function(){
		$('.dropdown-menu').slideToggle();
	});

	  
});
</script>
