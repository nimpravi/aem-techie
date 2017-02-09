<%--

  AppointmentDoctorInfo component.



--%><%@ page import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image, com.day.cq.wcm.foundation.Placeholder" %><%
%><%@include file="/libs/foundation/global.jsp"%>



<c:if test="${not empty properties.title}">
    <c:choose>
        <c:when test="${properties.showdescription=='true'}">

<div class="col-md-5 doc_detail">
    <c:set  var="imgval" value="${properties.Image}"/>
  <span class="left_align"> 
 <img src="/content/dam/LifePlus/Life_Testimonials_profile-pic_4.png" alt="doc" class="pad_left_20">  
   </span>
<span class="class_tb">  <b> Dr. Eric Foreman, MD</b></span>
                           <span>Neurologist</span>
                           <span> 1 Gustave L. Levy Pl, </span>
                           <span class="pad_left_88"> New York, NY</span>
						   <span class="pad_left_88"> (212) 241-6500 </span>
    					 <p class="pad_left_20 pad_top_20">
                                    <c:if test="${not empty properties.desc}">
                             <c:if test="${fn:length(properties.desc) > properties.Size }">
                                 ${properties.desc}
                                    </c:if>
								</c:if>
 						   </p>
   </div> 
        </c:when>
        <c:otherwise>



        <div class="col-md-5 search_results bg_none">  
        <span class="left_align"> 
<img src="/content/dam/LifePlus/Find-Doc_Location-pin_availabe.png" alt="unavailable" class="pad_left_20"> 
        </span>
<span class="class_tb">  <b> Dr. Eric Foreman, MD</b></span>
                           <span>Neurologist</span>
                           <span class=" mar_left_17per"> 1 Gustave L. Levy Pl, </span>
                          <span class=" mar_left_17per"> New York, NY</span>
						   <span class=" mar_left_17per"> (212) 241-6500 </span>
                          <c:set  var="AvailVal" value="${properties.Availability}"/>
                          <c:choose>
                          <c:when test="${AvailVal=='avail'}">

                        <span class=" mar_left_17per pad_top_20"> Mon -Sat</span>
                        <span class=" mar_left_17per"> 8.00 A.m - 12.00 P.m</span>
                        </c:when>
                        <c:when test="${AvailVal=='FTA'}">
                        <span class="pad_left_88 pad_top_20"> Appointment Time </span>\
                        <span class="pad_left_88"> Sat, 15 NOv 2014| 8.00 A.m <img src="/content/dam/LifePlus/Find-Doc_icon_calendar.png" alt="cal" class="pad_left_5"> </span>
                        </c:when>
                        </c:choose>
    </div>



        </c:otherwise>

    </c:choose>







</c:if>

<%-- ${fn:substring(properties.desc, 0,properties.Size )}--%>
