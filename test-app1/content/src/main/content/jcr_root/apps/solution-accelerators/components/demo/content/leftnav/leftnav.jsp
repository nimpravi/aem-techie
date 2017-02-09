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


<!-- <aside class="site-topics-menu">
    <header>
        <h3>Global Gateway</h3>
    </header>
    <ul>
        <li>
            <a href="/group/en/group/asia_oceania.html">Asia / Oceania</a>
        </li>
        <li>
            <a href="/group/en/group/northamerica.html">North America / Latin America</a>
        </li>
        <li>
            <a href="/group/en/group/europe.html">Europe / Africa</a>
        </li>
    </ul>
</aside> -->


    <%
            int count = 0;
            long absParent = currentStyle.get("absParent", 2L);
            
            String navstart = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);
            if (navstart.equals("")) navstart=currentPage.getPath();
            Resource rootRes = slingRequest.getResourceResolver().getResource(navstart);
            Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);
            
            Iterator<Page> children = rootPage.listChildren();
            if (children.hasNext()) { %>

				<aside class="site-topics-menu">
                <header>
                    <h3><%=currentPage.getTitle()%></h3>
                </header>
                <ul>
                    <% 
                count=1;
             }

            while (children.hasNext()) {
                Page child = children.next();
                %>
                    <li>
                        <a href="<%= child.getPath() %>.html"><%= StringEscapeUtils.escapeXml(child.getTitle()) %></a>    
                    </li>
                    <%
            }
            if(count!=0) { %>
                </ul>
                </aside>
                <% }
	%>

