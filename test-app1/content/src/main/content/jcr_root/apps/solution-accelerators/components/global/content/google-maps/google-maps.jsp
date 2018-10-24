
<%--

  Google Maps component.

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="isDirection" value="${properties.isDirection}" />
 <c:if test="${isDirection == null || isDirection == 'false'}">
	<cq:includeClientLib categories="app.lifeplus.maps.marker" />
</c:if>
<c:if test="${isDirection != null && isDirection == 'true'}">
	<cq:includeClientLib categories="app.lifeplus.maps.direction" />
</c:if> 


<br>GOOGLE MAPS<br>

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
<div id="googleMap" style="width:500px;height:380px;"></div>
<a id="directions" style="display:none" href="#">directions</a>
</body>



