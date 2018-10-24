<%--

  Content Page component.


--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>

<% if(currentNode.hasNode("globalFooter")) { %>
<div class="companypage">
	<cq:include path="custompar" resourceType="foundation/components/parsys" />
</div>

<section class="company-outline">
	<div class="container">
        <div class="row" style=" margin-left:0px">
			<cq:include path="custompar1" resourceType="foundation/components/parsys" />
        </div>
        <div class="row" style=" margin-left:0px">
			<cq:include path="custompar2" resourceType="foundation/components/parsys" />
        </div>
        <div class="row" style=" margin-left:0px">
			<cq:include path="custompar3" resourceType="foundation/components/parsys" />
        </div>
    </div>
</section>
<% if(currentNode.hasNode("globalFooter")) { %>
	<cq:include path="globalFooter" resourceType="/apps/solution-accelerators/components/demo/content/GlobalFooter"/>
<% } %>
<% } %>
