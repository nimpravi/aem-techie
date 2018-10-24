<%@include file="/libs/foundation/global.jsp" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="com.day.cq.wcm.foundation.Image" %>
<%@ page import="org.apache.sling.commons.json.JSONArray" %>

<%@page import="java.util.List,java.util.Map,java.util.ArrayList,java.util.Map,java.util.HashMap,com.day.cq.wcm.api.WCMMode" %>
<cq:includeClientLib categories="common.multifield" /> 



<section>
   <div id="myCarousel" class="carousel slide" data-ride="carousel">

      <ol class="carousel-indicators hidden-xs hidden-sm">
         <li data-target="#myCarousel" data-slide-to="0" class=""></li>
         <li data-target="#myCarousel" data-slide-to="1" class=""></li>
         <li data-target="#myCarousel" data-slide-to="2" class=""></li>
      </ol>

      <% 
      Session session=resourceResolver.adaptTo(Session.class); 
    Node carouselValuesNode=null; 
    Node imagesNode=null; 
	List<Map<String, String>> carouselList = new ArrayList<Map<String, String>>();
    if(currentNode.hasNode("carouselValues")) {
		carouselValuesNode = currentNode.getNode("carouselValues");
    }
	if(currentNode.hasNode("images")) {
		imagesNode = currentNode.getNode("images");
    }
	Map<String, String> valuesMap = new HashMap<String, String>();
	List<String> imagesList = new ArrayList<String>();
	int count = 0;

    if(imagesNode != null)
    { 
        NodeIterator itemIterator = imagesNode.getNodes(); 
        
        Node node1 = null; 
        valuesMap = new HashMap<String, String>();
        while(itemIterator.hasNext())
        { 
            node1 = itemIterator.nextNode(); 
            imagesList.add(node1.getProperty("imageReference").getString());
        } 
    }
    if(carouselValuesNode != null)
        { 
          NodeIterator itemIterator = carouselValuesNode.getNodes(); 

          Node node = null; 
        	String link = "";
          while(itemIterator.hasNext())
          { 
              valuesMap = new HashMap<String, String>();
            node = itemIterator.nextNode(); 
            valuesMap.put("title", node.getProperty("title").getString());
            valuesMap.put("navTitle", node.getProperty("navTitle").getString());
            valuesMap.put("navDesc", node.getProperty("navDesc").getString());
            valuesMap.put("desc", node.getProperty("desc").getString());
             link = node.getProperty("link").getString();
              if(link.startsWith("/")) 
              { 
                  link += ".html"; 
              } 
            valuesMap.put("link", link);
              valuesMap.put("imagePath", imagesList.get(count));
			carouselList.add(valuesMap);
              count++;
          } 
    }
	pageContext.setAttribute("carouselList", carouselList); 
    %>

       <div class="carousel-inner">
       <c:forEach items="${carouselList}" var="entry" varStatus="i">
           <c:if test="${i.index==0}">
               <div class="item active"> 
           </c:if>
           <c:if test="${i.index!=0}">
               <div class="item"> 
           </c:if>
				<img src="${carouselList[i.index].imagePath}" style="width:100%; " alt="First slide">
                  <!--Slider Next Prev button-->
				 <c:if test='${not empty carouselList[i.index].desc && not empty carouselList[i.index].navDesc}' >
					<div id="nav-arrows" class="nav-arrows">
					<div class="container">
					<div class="row ">
					<div class="col-md-4 slideoverlay1 hidden-xs hidden-sm"> 
					
						</div>
						<div class="col-md-6 slideoverlay"> 
						${carouselList[i.index].desc}
					   <a href="${carouselList[i.index].link}">${carouselList[i.index].navDesc}</a>
						</div>
						<div class="col-md-2 slideoverlay navig hidden-sm hidden-xs">
					   
						</div>
						</div>
					</div>
					   
					</div> <!--end Slider Next Prev button-->
				</c:if>
               </div>
       </c:forEach>
       </div>
		<c:if test='${not empty carouselList[0].desc && not empty carouselList[0].navDesc}' >
           <div id="nav-arrows" class="nav-arrows">
                <div class="container">
                <div class="row ">
             	<div class="col-lg-12  navig hidden-sm hidden-xs">
                <div class="pull-right">
                    <a href="#myCarousel" class="active" data-slide="prev">
           			 	<i class=" fa fa-2x fa-angle-left nav-arrow-prev"></i>
                    </a>
                	<a href="#myCarousel" data-slide="next"> <i class="fa fa-2x fa-angle-right nav-arrow-next"></i></a>
                </div>
                </div>
                </div>
                </div>                  
           </div>
        </c:if>
    </div>           
</section>

    <% if(!currentNode.hasNode("carouselValues")) {
        	out.println("Configure Carousel");
    } %>
