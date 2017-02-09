
<%@include file="/libs/foundation/global.jsp"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="com.day.cq.wcm.foundation.Image" %>
<%@ page import="org.apache.sling.commons.json.JSONArray" %>
<%@page
	import="java.util.List,java.util.Map,java.util.ArrayList,java.util.HashMap,com.day.cq.wcm.api.WCMMode"%>
<cq:includeClientLib categories="common.multifield"/>
<%@ taglib prefix="acc" uri="http://localhost:4502/bin/customComponents"%>

<% 
    if (currentNode.getPath()!=null)
{
        String image=currentNode.getPath()+"/images";
        String carouselpath=currentNode.getPath()+"/carouselValues";

    %>

<c:set var="imagepath" value="<%=image%>"/>
<c:set var="carouselpath" value="<%=carouselpath%>"/>
<acc:carousel imageNodePath="${imagepath}" carouselValuesNodePath="${carouselpath}" carouselList="values" />

<div id="myCarousel" class="carousel slide">
               <!-- Indicators -->
               <ol class="carousel-indicators carousel_custom">
                  <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                  <li data-target="#myCarousel" data-slide-to="1"></li>
                  <li data-target="#myCarousel" data-slide-to="2"></li>
                  <li data-target="#myCarousel" data-slide-to="3"></li>
               </ol>
               <div class="carousel-inner">
  <c:forEach items="${values}" var="slide" varStatus="i">   
                                                <c:if test="${i.index==0}">
              <div class="item active"> 
                </c:if>
                <c:if test="${i.index!=0}">
           	<div class="item"> 
                </c:if>
                     <div class="container">
                       <div class="row">
                           <div class="col-md-12 pad_zero">
                          <img src="${slide.imageReference}" alt="carousel" class="carousel_img">
                              <div class="carousel_content">
                                 ${slide.description}
                                 <button type="button" class="primary_button mar_left_43">${slide.buttonText}</button>
                              </div>
                           </div>
                        </div>
                     </div>
   </div>
                   </c:forEach>

            </div>
                   <%
}%>