<%@include file="/libs/foundation/global.jsp" %>
<%@page import="com.day.cq.wcm.foundation.Image,com.day.cq.wcm.api.WCMMode"%>
<cq:includeClientLib categories="ebazaarresp.collapsibleText&Image"/>
<c:set var="jlabel" value="${properties.label}"/>
<c:set var="jimgposition" value="${properties.imgposition}" />
<c:choose>
        <c:when test="${empty jlabel}">


       				 <c:out value="Edit the Collapsible Text Image."/>


		</c:when>
	<c:otherwise>
		<div id="${properties.anchor}" class="accItem">
            <h5><c:out value="${properties.label}"/></h5>
            <span class="collapsesign">+</span>
            <div class="accContent">

                    <c:choose>
                        <c:when test="${jimgposition eq 'top'}">
                            <c:if test="${not empty properties.accfileReference}"> 
                            	<img src="${properties.accfileReference}"/>
                            </c:if>
                            <c:out value="${properties.accordtext}" escapeXml="false"/>
                        </c:when>
                        <c:when test="${jimgposition eq 'bottom'}">
                            <c:out value="${properties.accordtext}" escapeXml="false"/>
                            <c:if test="${not empty properties.accfileReference}"> 
                            	<img src="${properties.accfileReference}"/>
                            </c:if>
                        </c:when>
                        <c:when test="${jimgposition eq 'left'}">
                            <div class="imageToLeft">
                                 <img src="${properties.accfileReference}"/>
                            </div>    
                            <c:out value="${properties.accordtext}" escapeXml="false"/>
                    	</c:when>
                    	<c:when test="${jimgposition eq 'right'}">
                            <div class="imageToRight">
                                <img src="${properties.accfileReference}"/>
                            </div>
                            <c:out value="${properties.accordtext}" escapeXml="false"/>
                    </c:when>
                </c:choose>
            </div>
		</div>
<hr class="green small last-child"/>
	</c:otherwise>
</c:choose>
