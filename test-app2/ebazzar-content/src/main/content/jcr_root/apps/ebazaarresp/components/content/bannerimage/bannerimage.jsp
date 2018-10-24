<%@ page import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.WCMMode,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image" %><%
        %><%@include file="/libs/foundation/global.jsp"%>

<cq:includeClientLib categories="cq.wcm.edit" />

<script type="text/javascript" language="JavaScript" src="http://svcs-dweb1.abbvie.com/analytics/external/sc_abbvie_main.js"></script>

<c:set var="pagePath" value="<%=currentPage.getPath()%>"/>

<%@ page trimDirectiveWhitespaces="true" %>  
<% 
String imagePath = "";
if (null != properties.get("image/fileReference"))
    {
	imagePath = properties.get("image/fileReference").toString();
	}   
%>
<c:set var="alingm" value="${properties.align}" />
<c:set var="link" value="${properties.linkURL}" />
<c:set var="arrow" value="${properties.arrow}" />
<c:choose>
    <c:when test="${arrow eq true}">
        <c:set var="linktext" value="${properties.linktext}&raquo" />
    </c:when>
    <c:otherwise>
        <c:set var="linktext" value="${properties.linktext}" />
    </c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${alingm eq 'left'}">
    	<c:set var="alignclass" value="bannerBlackTop2"/>
    </c:when>
    <c:otherwise>
        <c:set var="alignclass" value="bannerBlackTop"/>
    </c:otherwise>
</c:choose>
<div class="subBanner">
    <div class="banner" style="background-image:url(<%= imagePath %>)">
          <div class="bannerLeft">
             <div class="bannerBlack">&nbsp;</div>
              <div class="${alignclass}">
                    <div class="bannerTxt">
                        <div>
                            <cq:text property="title"/> 
                         		<c:if test="${not empty linktext}">
                                            <p>
                                                  <c:choose>
                                              <c:when test="${fn:startsWith(link,'/content')}">
                                                <a href="${link}"
                                                    class="blueBtn ${popUpType}" target="_blank">
                                                        ${linktext}</a>
                                                        </c:when>
                                                        <c:when test="${fn:startsWith(link,'http://')}">
                                                <a href="${link}"
                                                    class="blueBtn ${popUpType}" target="_blank">
                                                        ${linktext}</a>
                                                        </c:when>
                                                </c:choose>
                                                     
                                            </p>
                                        </c:if>  
                              </div>
                    </div>
               </div>
          </div>
            <div class="bannerRight">&nbsp;</div>
     </div>
</div>

