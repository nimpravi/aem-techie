<%--
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

<ul class="nav">
    <%

    // get starting point of navigation
    long absParent = currentStyle.get("absParent", 4L);
    String navstart = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);

    //if not deep enough take current node
    if (navstart.equals("")) navstart=currentPage.getPath();

    Resource rootRes = slingRequest.getResourceResolver().getResource(navstart);
    Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);
    String xs = Doctype.isXHTML(request) ? "/" : "";
    if (rootPage != null) {
        %>
    	<li class="dropdown"><a href="<%= rootPage.getPath() %>.html">Home</a></li>

    <%
        Iterator<Page> children = rootPage.listChildren(new PageFilter(request));
        while (children.hasNext()) {
            Page child = children.next();

            %><li class="dropdown"><a href="<%=child.getPath() %>.html" class="dropdown-toggle" data-toggle="dropdown"><%= StringEscapeUtils.escapeXml(child.getTitle()) %><b class="caret"></b></a>
<ul class="dropdown-menu pull-left" role="menu" aria-labelledby="dLabel">
        <%
Iterator<Page> child1 = child.listChildren();
                while(child1.hasNext()){

					Page child11 = child1.next();
                %>

					<li class="dropdown-submenu">
					<a  href="<%= child11.getPath() %>.html"> <%= StringEscapeUtils.escapeXml(child11.getTitle()) %> </a>  
						<ul class="dropdown-menu">
                            <%
                    Iterator<Page> child2 = child11.listChildren();
                      while(child2.hasNext()){

					Page child21 = child2.next();

                    %>
										<li>
										<a href="<%= child21.getPath() %>.html">  <%= StringEscapeUtils.escapeXml(child21.getTitle()) %></a>  
										</li> 


                            <%
                        }
                    %>
									</ul>             		
								</li>


            <%
            }
            %>
</ul>

    </li>
                    <%
        }
    }
%>
</ul>
