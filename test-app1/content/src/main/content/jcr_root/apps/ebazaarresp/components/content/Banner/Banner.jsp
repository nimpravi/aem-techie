<%@include file="/libs/foundation/global.jsp"%><%
String imagePath = properties.get("bgImage","/etc/designs/philips/images/Banner_BG.png");
//String imagePath = properties.get("bgImage","/etc/designs/philips/images/Banner_BG.png");
%>

<%if(currentDesign.getPath().equals("/etc/designs/philips_ver_2")){%>
<ul class="bjqs" style="height: 400px;"><li><img src="<%=imagePath%>" /></li></ul>
<%}
else{%>
<style>

header{background-image:url('<%=imagePath%>') !important;}
</style><%}%>