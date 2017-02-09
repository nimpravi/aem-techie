<%--

     KAO LOGO COMPONENT

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@ page
	import="com.day.text.Text,com.day.cq.wcm.foundation.Image,com.day.cq.commons.Doctype"%>
<%
	String home = currentStyle.get("absParent", "#");
%>
<c:set var="image" value="${currentStyle.imageReference}"></c:set>
<c:set var="mobimage" value="${currentStyle.mobImageReference}"></c:set>

<div class="upperleft">
    <div class="logo"><img src="${image}" alt="Sotheby's International Realty Logo" /></div>
					</div>