<%@include file="/libs/foundation/global.jsp"%>
<%


String redirect = request.getParameter("redirect");
String quantity = request.getParameter("quantity");

response.sendRedirect(redirect+"&quantity="+quantity);



%>