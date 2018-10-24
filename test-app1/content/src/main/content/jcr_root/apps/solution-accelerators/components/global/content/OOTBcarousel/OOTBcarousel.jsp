<%@include file="/libs/foundation/global.jsp"%>
<%@ taglib prefix="acc" uri="http://localhost:4502/bin/carousel"%>

Configure your Carousel <br>
USING TAGLIBS
<%
        String carousel=currentNode.getPath();
%>
<c:set var="carouselpath" value="<%=carousel%>"/>
<c:out value="${carouselpath}"/>
<acc:OOTBcarousel carouselNodePath="${carouselpath}" OOTBCarouselList="values" />
<c:forEach items="${values}" var="slide" varStatus="i">
    <br> *********************************** SLIDE DETAILS*********************************<br>

    <c:out value="${slide.playSpeed}"/>

</c:forEach>