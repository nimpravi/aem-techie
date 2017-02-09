
<%--

  Google Maps component.

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@ page session="false" import="com.day.cq.wcm.api.WCMMode" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="isDirection" value="${properties.isDirection}" />
 <c:if test="${isDirection == null || isDirection == 'false'}">
	<cq:includeClientLib categories="app.lifeplus.maps.marker" />
</c:if>
<c:if test="${isDirection != null && isDirection == 'true'}">
	<cq:includeClientLib categories="app.lifeplus.maps.direction" />
</c:if> 

<% if (WCMMode.fromRequest(request) == WCMMode.EDIT) { %>
        GOOGLE MAPS
<% } %>


<c:choose>
    <c:when test='${not empty properties.width}'>
        <c:set var="width" value="${properties.width}"/>
    </c:when>
    <c:when test='${empty properties.width}'>
        <c:set var="width" value="380px"/>
    </c:when>
</c:choose>
<c:choose>
    <c:when test='${not empty properties.height}'>
        <c:set var="height" value="${properties.height}"/>
    </c:when>
    <c:when test='${empty properties.height}'>
        <c:set var="height" value="500px"/>
    </c:when>
</c:choose>

<input type="hidden" id="saddr" value="" />
<input type="hidden" id="daddr" value="" />
<input type="hidden" id="isDir" value="${isDirection}" />

<head>
<script
src="http://maps.googleapis.com/maps/api/js?sensor=false">
</script>
</head>

<script>
	google.maps.event.addDomListener(window, 'load', initialize);
</script>

<body>
    <div id="googleMap" style="width:${width}px;height:${height}px;"></div>
<a id="directions" style="display:none" href="#">directions</a>
</body>
