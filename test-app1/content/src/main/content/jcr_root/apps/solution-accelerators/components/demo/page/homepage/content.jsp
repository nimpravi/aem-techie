<%--

  Homepage component.

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%@page import="com.day.cq.wcm.api.WCMMode" %>

<cq:include path="custompar" resourceType="foundation/components/parsys" />
<section>
	<div class="container">
        <div class="row company1" style=" margin-left:0px">
            <cq:include path="custompar1" resourceType="foundation/components/parsys" />
        </div>
        <div class="clear-fix"></div>
        <div class="row company2" style="margin-left:0px;">
            <cq:include path="custompar2" resourceType="foundation/components/parsys" />
            <% if(currentNode.hasNode("custompar2")) {
    				if(currentNode.getNode("custompar2").hasNodes()) { %>
           			<cq:include path="news" resourceType="/apps/solution-accelerators/components/demo/content/news-and-events"/>
            <% 		} 
			   } %>
        </div>
    </div>
</section>

<cq:include path="custompar3" resourceType="foundation/components/parsys" />

<% if(currentNode.hasNode("globalFooter")) { %>
	<cq:include path="globalFooter" resourceType="/apps/solution-accelerators/components/demo/content/GlobalFooter"/>
<% } %>

