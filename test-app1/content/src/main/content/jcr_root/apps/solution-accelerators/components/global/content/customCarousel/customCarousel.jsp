

<%@include file="/libs/foundation/global.jsp"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="com.day.cq.wcm.foundation.Image" %>
<%@ page import="org.apache.sling.commons.json.JSONArray" %>
<%@ page import="java.util.List,java.util.Map,java.util.ArrayList,java.util.Map,java.util.HashMap,com.day.cq.wcm.api.WCMMode"%>

<cq:includeClientLib categories="common.multifield"/>
<%@ taglib prefix="acc" uri="http://localhost:4502/bin/customCarousel"%>

Configure your Carousel <br>
<%
        String image=currentNode.getPath()+"/images";
		out.println("Image Node Path ="+image+"<br>");

        String carouselpath=currentNode.getPath()+"/carouselValues";
		out.println("Carousel Values Node Path ="+carouselpath);

%>
<c:set var="imagepath" value="<%=image%>"/>
<c:set var="carouselpath" value="<%=carouselpath%>"/>
<acc:carousel imageNodePath="${imagepath}" carouselValuesNodePath="${carouselpath}" carouselList="values" />
<br> 
<c:forEach items="${values}" var="slide">
     <br> *********************************** SLIDE DETAILS*********************************<br>

        IMAGE: ${slide.imageReference}<br>
        TITLE:${slide.title}<br>
        DESC:${slide.description}<br>
    	NAVTITLE:${slide.navTitle}<br>
     	NAVDESC:${slide.navDescription}<br>
        LINK:${slide.link} <br>
        BUTTON TEXT:${slide.buttonText} <br>
</c:forEach>


