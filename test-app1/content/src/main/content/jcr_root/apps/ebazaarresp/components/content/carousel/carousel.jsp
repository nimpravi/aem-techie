<%-- 
* 
* 
* Program Name :  carousel.jsp
* Application  :  Philips
* Purpose      :  See description
* Description  :  Carousel. Displays the Banners as Carousel with description and buttons(optional). 
* Names of Databases accessed: JCR
* Modification History:
* ---------------------
*    Date          Modified by                       Modification Description
*-----------    ----------------                    -------------------------
*  3-March-2014   Cognizant Technology solutions            Initial Creation
*-----------    ----------------                    -------------------------

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@ page import="com.day.cq.commons.Doctype,
                 com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.api.components.DropTarget,
				 com.cts.store.MTMultiCompositeFieldHelper"%>
<%@ page trimDirectiveWhitespaces="true" %>
<cq:includeClientLib categories="apps.carousal.clientlibs"/>
<c:set var="initialLoad" value="true" />
<script type="text/javascript" src="/etc/designs/ebazzarresp/carouselclientlibs/js/carousel.js"></script>
<cq:includeClientLib js="common.multifield"/>	
<cq:setContentBundle/>

<%
MTMultiCompositeFieldHelper mht = new MTMultiCompositeFieldHelper(); 
 String[] props = {"title","imagepath","linktext","linkURL","description","descrPosition","checkSymbol","imageLink"};
 %> 


<input type='hidden' value='${properties.transType}' id="transType"/>
<input type='hidden' value='${properties.transTime}' id="transTime"/>
<input type='hidden' value='${properties.playDelay}' id="playDelay"/>
<c:set  var="mtmvalues" 
                value="<%= mht.getMultiCompositeValuesArrayList(currentNode.getPath(),
                   "surveyset",props, slingRequest)%>" />

<c:choose>
    <c:when test="${fn:length(mtmvalues) eq 0}">

               <h4>Double click or right click to edit ${component.title}</h4>

    </c:when>
    <c:otherwise>

     <div class="subBanner3" >
        <div id="carousel" class="carousel_inner">
        <c:forEach var="item"  items="${mtmvalues}" varStatus="loopIterator">
            	<c:set var = "angleQuote" value ="&nbsp;"/>
            <c:if test ="${item.checkSymbol}">
                <c:set var = "angleQuote" value ="&raquo;"/>
            </c:if>
            	<c:set var = "bannerClass" value ="bannerLeft"/>    
            	<c:set var="imagePath" value="${item.imagepath}" />
            	<c:set var="bannerBlackTopClass" value="bannerBlackTop2" />
         <c:if test ="${empty imagePath and (item.descrPosition eq 'Center')}">
            <c:set var = "bannerClass" value ="banner${item.descrPosition}"/>
           <!-- <c:set var="bannerBlackTopClass" value="bannerBlackTop" /> -->
        </c:if> 
            <div class="carousel_box">
                    <div class="banner" style="background-image:url(${imagePath})" >
                        <div class="${bannerClass}">
                            <div class="bannerBlack">&nbsp;</div>
                            <div class="${bannerBlackTopClass }">
                                <div class="bannerTxt">
                                     <div class="textAlign">
                                     ${item.description}
                                     
                                     <c:if test="${not empty item.linktext}">
                                            <p>
                                              <c:choose>
                                              <c:when test="${fn:startsWith(item.linkURL,'/content')}">
                                              
                                                <a href="${item.linkURL}.html"
                                                    class="blueBtn" target="_blank">
                                                        ${item.linktext}${angleQuote}</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                        <a href="${item.linkURL}"
                                                    class="blueBtn" target="_blank">
                                                        ${item.linktext}${angleQuote}</a> 
                                                        </c:otherwise>
                                                </c:choose>        

                                            </p>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <c:if test ="${bannerClass ne 'bannerCenter'}">
                        <div class="bannerRight">&nbsp;</div>
                       </c:if>

						<c:if test = "${not empty imagePath}">
							<c:choose>
                                <c:when test="${fn:startsWith(item.imageLink,'/content')}">
                           			<a  href="${item.imageLink}.html" target="_blank"><div class="imgLink"></div></a> 
								</c:when>
								<c:otherwise>
									<a  href="${item.imageLink}" target="_blank"><div class="imgLink"></div></a> 
								</c:otherwise>
							</c:choose>
                        </c:if>
                    </div>
                </div>
                  </c:forEach> 
              </div>
           
              <p class="btns">
                    <span id="carousel_prev">
                        <img src="/etc/designs/ebazzarresp/carouselclientlibs/images/left_arrow.png"/>
                    </span>
                 <span id="carousel_next">
                     <img src="/etc/designs/ebazzarresp/carouselclientlibs/images/right_arrow.png"/>
                </span>
            </p>

		<c:if test ="${properties.bottomControl}">
                <ul id="imgButs">

                </ul>
         </c:if>
           </div>
    </c:otherwise>
</c:choose>

