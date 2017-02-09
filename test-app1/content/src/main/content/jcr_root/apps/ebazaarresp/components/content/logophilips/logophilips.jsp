<%@include file="/libs/foundation/global.jsp"%>
<style>
div.logo {
    margin-left: 19px;
    margin-top: 19px;
    margin-right: 61px;
    width: 80px;
    height: 14px;
    vertical-align: middle;
    float: left;
    image-rendering: optimizeQuality;
}
</style>    
<%if(currentDesign.getPath().equals("/etc/designs/philips")){%>

  <c:set var="absParent" value="${currentStyle.absParent}"></c:set>
<c:set var="image" value="${currentStyle.imageReference}"></c:set>
<c:set var="currentPath" value="<%=currentPage.getPath()%>"></c:set>
<c:choose>
<c:when test="${absParent eq currentPath}">
<div class="logo">
  <a href="#"><img src="${image}" width="80px" height="14px" alt="pag"
      /></a></div>

</c:when>

<c:otherwise>
      <div class="logo">  <a href="${absParent}"><img src="${image}" width="80px" height="14px" alt="pag"
    /></a></div>
</c:otherwise>
</c:choose>

<%}
else{%>
                <%@include file="logo2.jsp"%>                
                <%
}
%>