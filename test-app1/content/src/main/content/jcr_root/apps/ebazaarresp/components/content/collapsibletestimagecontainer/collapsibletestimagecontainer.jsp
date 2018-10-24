<%-- 
  - @(Component)        Accordion Container.  
  - Description:        Allows to drop the accordions. 
--%>
<%@include file="/libs/foundation/global.jsp" %>
<%@page import="com.day.cq.wcm.api.WCMMode"%>  
<cq:includeClientLib categories="ebazaarresp.collapsibleText&Image"/>
<c:choose>
    <c:when  test="${globalWCMMode eq 'EDIT' }">
        <c:out value="Drop the accodions in the container."/>
    </c:when>
</c:choose>
<div id="myAccordian" class='accordionContainer'>
  	<hr class="green small last-child"/>
	<cq:include path="accordions" resourceType="foundation/components/parsys"/>
</div>
<c:out value="${properties.text}" escapeXml="false"/>