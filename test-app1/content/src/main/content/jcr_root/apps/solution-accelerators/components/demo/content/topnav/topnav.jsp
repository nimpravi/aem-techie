<%--

  Top Navigation component.


--%>

    <%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%@ page import="java.util.Iterator,
        com.day.text.Text,
        com.day.cq.wcm.api.PageFilter,
        com.day.cq.wcm.api.Page,
        com.day.cq.commons.Doctype,
        org.apache.commons.lang3.StringEscapeUtils" 
            %>
<%
	// TODO add you code here
%>


<nav class="site-nav">
    <ul>
        <%
        long absParent = currentStyle.get("absParent", 2L);

        String navstart = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);
        if (navstart.equals("")) navstart=currentPage.getPath();
        Resource rootRes = slingRequest.getResourceResolver().getResource(navstart);
        Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);

        Iterator<Page> children = rootPage.listChildren();
        while (children.hasNext()) {
            Page child = children.next();
            %>
            <li>
                <a href="<%= child.getPath() %>.html"><%= StringEscapeUtils.escapeXml(child.getTitle()) %></a>    
            </li>
            <%
        }
    %>
    </ul>
</nav>