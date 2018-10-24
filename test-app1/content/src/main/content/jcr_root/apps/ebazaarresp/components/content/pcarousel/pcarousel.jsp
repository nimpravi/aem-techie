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
<%@page import="java.util.List,java.util.Map,java.util.ArrayList,java.util.HashMap,com.day.cq.wcm.api.WCMMode"%><%
%><%@page session="false" %><%
%>

<%

    boolean reqConfig = false;
if(currentNode != null) {

    Node slides = (currentNode.hasNode("carousel") ? currentNode.getNode("carousel") : null);
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    if (slides != null) {
        NodeIterator nodes = slides.getNodes();
        Node slide = null;
        Map<String, String> map = null;
        while (nodes.hasNext()) {
            slide = nodes.nextNode();
            map = new HashMap<String, String>();
            if(slide.hasProperty("description")){
                 String description = slide.getProperty("description").getString();
                map.put("description", description);
            }
            if(slide.hasProperty("heroimage")){
            	String heroimage = slide.getProperty("heroimage").getString();
				 map.put("heroimage", heroimage);
            }
            if(slide.hasProperty("herolink")){
           	 String herolink = slide.getProperty("herolink").getString();
				 map.put("herolink", herolink);
            }

            if(slide.hasProperty("subimage")){
             String subimage = slide.getProperty("subimage").getString();
				 map.put("subimage", subimage);
            }
            if(slide.hasProperty("subimage")){
             String subheader = slide.getProperty("subheader").getString();
				 map.put("subheader", subheader);
            }
            if(slide.hasProperty("subtext")){
             String subtext = slide.getProperty("subtext").getString();
				 map.put("subtext", subtext);
            }
            if(slide.hasProperty("linktext")){
             String linktext = slide.getProperty("linktext").getString();
                 map.put("linktext", linktext);
            }
            if(slide.hasProperty("linkURL")){
             String linkurl = slide.getProperty("linkURL").getString();
                 map.put("linkURL", linkurl);
            }


        list.add(map);

        }
    } else {
        reqConfig = true;
    }
    request.setAttribute("list", list);
} else {
    reqConfig = true;
}
%>


<%if(!reqConfig) {%>
<div class="ka">


				<div class="homeintro homeintro-portal fullwidth">

<div id="headerbanner-content" class="headerbanner">
    <c:forEach var="slide" items="${list }" varStatus="i">

        <a href="${slide.herolink}.html"> <img class="carousel-item" src="${slide.heroimage }" /></a>


             </c:forEach>	
    </div>
					<div class="bannerwrap">
						<div class="bannerwrap-inner">

							<div class="carousel carousel-bannerwrap">
								<div class="carousel-nav carousel-nav-dotted carousel-nav-animated"></div>
								<ul id="bannerwrap-content" class="carousel-content">	
                  
                                     <c:forEach var="slide" items="${list }" varStatus="i">

									<li class="carousel-item">
                  
										<!-- Heroproduct banner component -->
										<div class="heroproductbanner" style="background:url('${slide.subimage }') no-repeat;">
												<h3 class="header">${slide.subheader }</h3>
												<p class="copy">${slide.subtext }</p>
												<a class="readmore" href="${slide.linkURL}.html">${slide.linktext } <span class="arrow-right"></span></a>
										</div>
									
									</li>

                                       </c:forEach>

								</ul>
							</div>
						</div>
					</div>	

					<div class="topright"></div>

				</div>
</div>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="js/libs/jquery-1.7.2.min.js"><\/script>')</script>
	<script type="text/javascript" src="/etc/designs/ebazzarresp/pcarouselclientlibs/js/jquery.cycle.js"></script>
	<script type="text/javascript" src="/etc/designs/ebazzarresp/pcarouselclientlibs/js/jquery.cycle.transitions.js"></script>
	<script type="text/javascript" src="/etc/designs/ebazzarresp/pcarouselclientlibs/js/raphael.timerdot.js"></script>
	<script type="text/javascript" src="/etc/designs/ebazzarresp/pcarouselclientlibs/js/jquery.timerdot.js"></script>
	<script  type="text/javascript" src="/etc/designs/ebazzarresp/pcarouselclientlibs/js/philips.carousel.js"></script>
	<script  type="text/javascript" src="/etc/designs/ebazzarresp/pcarouselclientlibs/js/plugins.js"></script>

	<script>
 		jQuery(function ($) { 

 			// Initialize combined herobanners and bannerwrap carousel
			var heroBannerCarousel = $('#headerbanner-content').cycle({
				timeout: 0
			});

			$('#bannerwrap-content').carousel({
				before: function(curr, next) {
					heroBannerCarousel.cycle($(next).index());
				}
			});

 		});	

	</script>


<%} else {
    if( WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.DESIGN ){ 
        %><div style='width:340px; padding:10px; text-align:center; text-transform:uppercase; background:#eee; border:dotted 3px #333;'>Configure in Edit Mode</div><%
    }
}%>

<!--<link rel="stylesheet" type="text/css" href="http://www.crsc.philips.com/crsc/styles/screen.internet.css" />
	 CRSC temporary font-face, to be replaced with new central API 
	<link rel="stylesheet" type="text/css" href="http://www.crsc.philips.com/crsc/styles/fonts.css" />
<link rel="stylesheet" type="text/css" href="/etc/designs/ebazzarresp/pcarouselclientlibs/css/style.css" />-->

<cq:includeClientLib categories="apps.pcarousal.clientlibs"/>
<cq:includeClientLib js="common.multifield"/>	
<cq:setContentBundle/>
