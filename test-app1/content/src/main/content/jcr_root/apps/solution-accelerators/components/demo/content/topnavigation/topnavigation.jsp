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


<div class="col-md-12 top_nav2">
    <nav class="navbar navbar-default pull-right" role="navigation">
     <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
    
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav in" id="collapseButtonData" style="height: auto;">

              <%
                    long absParent = currentStyle.get("absParent", 2L);
            
                    String navstart = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);
                    if (navstart.equals("")) navstart=currentPage.getPath();
                    Resource rootRes = slingRequest.getResourceResolver().getResource(navstart);
                    Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);
            
                    Iterator<Page> children = rootPage.listChildren();
                    while (children.hasNext()) {
                        Page child = children.next();
                        if(child.getName().equalsIgnoreCase(currentPage.getName())) {
                            %>
                                      <li class="current"><a href="<%= child.getPath() %>.html" class="active"><%= StringEscapeUtils.escapeXml(child.getTitle()) %></a></li>    
             		 	<% } else { %>
                                      <li class=""><a href="<%= child.getPath() %>.html" class=""><%= StringEscapeUtils.escapeXml(child.getTitle()) %></a></li>    
         				<% }
                    }
                %>

                                <li><div class="right-inner-addon">
            <i class="glyphicon glyphicon-search"></i>
            <input type="search" class="form-control" placeholder="Search">
        </div></li>  
                  </ul>
        </div><!-- /.navbar-collapse -->
      </div><!-- /.container-fluid -->
   </nav>
</div>