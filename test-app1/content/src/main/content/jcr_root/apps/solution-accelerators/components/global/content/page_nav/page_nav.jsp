
<%@page import="java.util.Iterator,   org.apache.commons.lang3.StringEscapeUtils"%><%--


--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
	// TODO add you code here
%>
 <%!

 %>

<%
        String path=currentStyle.get("parentpath",String.class);
        Resource rootRes = slingRequest.getResourceResolver().getResource(path);
        Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);
if(null!=rootPage)
{
        Iterator<Page> children = rootPage.listChildren();
%>

	<%		while (children.hasNext()) {
              Page child = children.next();

            %>
            <a href="<%= child.getPath() %>.html"><li><%= StringEscapeUtils.escapeXml(child.getTitle()) %></li></a>    
            <%
        }
}


%>

<!-- <a href="#">
                        <li class="pad_right_5">SIGN IN OR LOGIN WITH</li>
                     </a>
                     <a href="#">
                        <li class="pad_right_5"><img src="/content/dam/LifePlus/Life_Homepage_facebook-icon.png" class="pad_bot_10" alt="Facebook"></li>
                     </a>
                     <a href="#">
                        <li><img src="/content/dam/LifePlus/Life_Homepage_Gplus-icon.png" class="pad_bot_10" alt="Google"></li>
                     </a>
 <li>
                        <img src="/content/dam/LifePlus/Life_Homepage_search-Icon.png" alt="search" class="pad_bot_10">
                         <input type="text" class="myBox" name="srch-term" id="srch-term">
                     </li>
-->