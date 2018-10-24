<%@include file="/libs/foundation/global.jsp" %>
<%@page import="com.day.cq.wcm.api.WCMMode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
 <%if( WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.DESIGN ){ 
        %>
<script src="/apps/astrazeneca/components/us/content/horizontal-nav/multicompositefield.js"></script>
<%} %>
<% 
Session session = resourceResolver.adaptTo(Session.class);

Node linksNode = null;
if (session.itemExists(currentStyle.getPath() + "/links")) {
    linksNode = session.getNode(currentStyle.getPath() + "/links");
}

List<List<String>> links = new ArrayList<List<String>>();

if (linksNode != null) {

    NodeIterator regionIterator = linksNode.getNodes();

    Node linkNode = null;
    String linkTitle = null;
    String linkPath = null;
    Page linkPage = null;
    List<String> link = null;

    while (regionIterator.hasNext()) {
        linkNode = regionIterator.nextNode();
        linkTitle = linkNode.hasProperty("title") ? linkNode
                .getProperty("title").getString() : null;
        linkPath = linkNode.getProperty("path").getString();

        link = new ArrayList<String>();
        
        if ((linkTitle == null || linkTitle.isEmpty()) && linkPath.startsWith("/")) {
            linkPage = pageManager.getPage(linkPath);
            if (linkPage != null) {
                linkTitle = linkPage.getNavigationTitle();
                if (linkTitle == null || linkTitle.isEmpty()) {
                    linkTitle = linkPage.getTitle();
                }
            }
        }
        
        link.add(linkPath);
        link.add(linkTitle);
        if(linkPath.startsWith("/")) {
            linkPath += ".html";
        }
        link.add(linkPath);
        links.add(link);
    }
    
    pageContext.setAttribute("links", links);

} else {
    if( WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.DESIGN ){ 
        %><div style='width:300px; padding:10px; text-align:center; text-transform:uppercase; background:#eee; border:dotted 3px #333;'>Configure Top Navigation in Design Mode</div><%
    }
}
%>

<div class="transbox">    
    <div class="headerNav">
        <div class="logo">
            <a href=""><img src="<%=currentDesign.getPath()%>/images/LOGO.png" alt="phillips" /></a>
        </div>        
        <nav>
        <c:if test="${fn:length(links) > 0}">
        <div id="top-navigation">
            <ul>
                <c:forEach var="link" items="${links }">
                 <c:set var="tempPath" value="${link[0]}/"/>
                <li class="${currentPage.path == link[0] or fn:startsWith(currentPage.path, tempPath) ? 'active' : ''}">
                <a href="${link[2] }" title="${link[1] }">${link[1] }</a></li>
                </c:forEach>

            </ul>
         </div>
         </c:if>
            <ul>
            <li class="search"><img src="<%=currentDesign.getPath()%>/images/Search_icon.png" class="pointer"></li>
            </ul>
        </nav>        
    </div>
    </div>

