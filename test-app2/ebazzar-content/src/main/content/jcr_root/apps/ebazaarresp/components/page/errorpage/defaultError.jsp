<%@include file="/libs/foundation/global.jsp" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<%
String message = ((slingRequest.getAttribute("message"))!=null)?(slingRequest.getAttribute("message")).toString():"Page Not Found";
int statusCode = slingRequest.getAttribute("statusCode")!=null?(Integer)slingRequest.getAttribute("statusCode"):404;

%>
<div class="error">
<h1><c:out value="<%=statusCode+" "+message %>"/></h1>
</div>
</body>
</html>