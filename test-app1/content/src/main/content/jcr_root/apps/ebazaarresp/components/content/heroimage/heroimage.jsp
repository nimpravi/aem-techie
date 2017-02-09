<%--

  Hero image component component.

  

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
	String imagepath = properties.get("fileReference", "");

        String path = request.getContextPath() + resource.getPath();

%>

<div class="hero-unit" style="background-image:url(<%=imagepath%>);">


    <a href="<cq:text property="linkURL"/>">more</a>
</div>