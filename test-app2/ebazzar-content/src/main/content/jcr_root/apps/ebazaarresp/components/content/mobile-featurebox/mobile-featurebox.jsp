<%@page import="org.apache.sling.api.resource.ResourceResolver"%>
<%@include file="/libs/foundation/global.jsp"%>
<%@page import="com.day.cq.wcm.api.WCMMode"%>

<cq:includeClientLib categories="ebazaarresp.featurebox"/>

<c:if test="<%=WCMMode.fromRequest(request)!=WCMMode.DISABLED %>">
    <cq:includeClientLib categories="ebazaarresp.featurebox-author"/>
</c:if> 
<style>

#CQ #featureBoxDialog iframe {
    width: 100%; /* fix for richtext iframe inside custom multifield*/
}
</style>
<c:set var="numberOfItems" value="${properties.numberofitems}" />
<c:set var="images" value="${properties.image}" />
<c:set var="text" value="${properties.text}" />
<c:set var="url" value="${properties.url}" />
<c:set var="align" value="${properties.align}" />
<c:set var="boxtitlePosition" value="${properties.boxtitlePosition}" />
<c:set var="boxtitle" value="${properties.boxtitle}" /> 
<script> 
function setSizeOfFeatureBox(){
    return ${numberOfItems};
}
</script>
<%String imageTitle = "";%>
<c:choose>
    <c:when test="${numberOfItems eq 1}">
    	<c:set value="${images}" var="image"/>
        <%imageTitle = getImageTitle(pageContext, resourceResolver);%>
		<div class="device">
      		<a class="arrow-left" href="#"></a>
        	<div id="featurebox" class="swiper-container">
        		<div class="swiper-wrapper">
				<c:choose>
	            <c:when test="${empty align || align eq 'imgtxt'}">
	                <div class="swiper-slide"><c:if test="${not empty url}">
	                    <a href="<c:out value='${url}'/>" class="fbox feature-box0">
	                </c:if>
	                 <div class="mobilecaraouselcommontexttop">${boxtitle}</div>   
	                <div class="mobilecaraouselimg-first"><img
	                    src="<c:out value='${images}'/>" alt="<%=imageTitle%>" title="<%=imageTitle%>"></img></div>
	                <div class="mobilecaraouseltext-second"><c:out value="${ text}"
	                    escapeXml="false"></c:out></div>   
	                <c:if test="${not empty url}">
	                    </a>
	                </c:if></div>
	            </c:when>
	            <c:otherwise>
	                <div class="swiper-slide"><c:if test="${not empty url}">
	                    <a href="<c:out value='${url}'/>" class="fbox feature-box0">
	                </c:if>
	                 <div class="mobilecaraouselcommontexttop">${boxtitle}</div>
	                 <div class="mobilecaraouseltext-first"><c:out value="${ text}"
	                     escapeXml="false"></c:out></div>
	                <div class="mobilecaraouselimg-second"><img
	                    src="<c:out value='${images}'/>" alt="<%=imageTitle%>" title="<%=imageTitle%>"></img></div>
	                <c:if test="${not empty url}">
	                    </a>
	                </c:if></div>
	            </c:otherwise> 
	        	</c:choose>
        		</div>
        	</div>
			<a class="arrow-right" href="#"></a>
			<div class="pagination"></div>
		</div>
	</c:when>
    <c:when test="${numberOfItems gt 1}">
    	<div class="device">
			<a class="arrow-left" href="#"></a>

			<div id="featurebox" class="swiper-container">
				<div class="swiper-wrapper">
				<c:forEach var="i" begin="0" end="${numberOfItems-1}">
	            	<c:set value="${images[i]}" var="image"/> 
	            	<%imageTitle = getImageTitle(pageContext, resourceResolver);%>                                 
	            	<c:choose>
	                <c:when test="${empty align[i] || align[i] eq 'imgtxt'}">
	                    <div class="swiper-slide"><c:if test="${not empty url[i]}">
	                        <a href="<c:out value='${url[i]}'/>" class="fbox feature-box${i}">
	                    </c:if>
	                      <div class="mobilecaraouselcommontexttop">${boxtitle[i]}</div>
	                    <div class="mobilecaraouselimg-first"><img
	                        src="<c:out value='${images[i]}'/>" alt="<%=imageTitle%>" title="<%=imageTitle%>"></img></div> 
	                    <div class="mobilecaraouseltext-second"><c:out value="${ text[i]}"
	                         escapeXml="false"></c:out></div>     
	                    <c:if test="${not empty url[i]}">
	                        </a>
	                    </c:if></div>
	                </c:when>
	                <c:otherwise>
	                    <div  class="swiper-slide"><c:if test="${not empty url[i]}">
	                        <a href="<c:out value='${url[i]}'/>" class="fbox feature-box${i}"> 
	                    </c:if>
	                     <div class="mobilecaraouselcommontexttop">${boxtitle[i]}</div>
	                    <div class="mobilecaraouseltext-first"><c:out value="${ text[i]}"
	                         escapeXml="false"></c:out></div>
	                    <div class="mobilecaraouselimg-second"><img
	                        src="<c:out value='${images[i]}'/>" alt="<%=imageTitle%>" title="<%=imageTitle%>"></img></div>
	                    <c:if test="${not empty url[i]}">
	                        </a>
	                    </c:if></div>
	                </c:otherwise>
	            	</c:choose>
				</c:forEach>
				</div>
			</div>
			<a class="arrow-right" href="#"></a>
			<div class="pagination"></div>
		</div>
	</c:when>
	<c:otherwise>
		<c:if test="<%=WCMMode.fromRequest(request)==WCMMode.EDIT || WCMMode.fromRequest(request)==WCMMode.DESIGN  %>">
			Edit the content
		</c:if>
	</c:otherwise>
</c:choose>
<%!
String getImageTitle(PageContext pageContext, ResourceResolver resourceResolver ) {
	String imageTitle = "";
	try {
		int i = ((Integer)pageContext.getAttribute("i")).intValue();
	    String imageDamPath = (String)pageContext.getAttribute("image");
	    if(imageDamPath!=null ){
	        Resource imageRes = resourceResolver.getResource(imageDamPath.concat("/jcr:content/metadata"));
	        if(imageRes!=null){
	            Node node = imageRes.adaptTo(Node.class);
	            if(node.hasProperty("dc:title")){
	                imageTitle = node.getProperty("dc:title").getString();
	            }
	        }
	    }
	} catch (Exception e) {
		//
	}
    return imageTitle;
}
%>