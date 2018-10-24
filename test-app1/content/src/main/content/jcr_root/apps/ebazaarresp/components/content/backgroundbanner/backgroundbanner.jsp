<%--

  Background Banner Carousel component.

  Background Banner Carousel

--%><%
%><%@include file="/libs/foundation/global.jsp"%>
<%@page import="java.util.List,java.util.Map,java.util.ArrayList,java.util.HashMap,com.day.cq.wcm.api.WCMMode"%><%
%><%@page session="false" %><%
%><%

    boolean reqConfig = false;
if(currentNode != null) {

    Node slides = (currentNode.hasNode("slides") ? currentNode.getNode("slides") : null);
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    if (slides != null) {
        NodeIterator nodes = slides.getNodes();
        Node slide = null;
        Map<String, String> map = null;
        while (nodes.hasNext()) {
            slide = nodes.nextNode();
            String path = slide.getProperty("fileReference").getString();
            String title = slide.getProperty("title").getString();
            map = new HashMap<String, String>();
            map.put("path", path);
            map.put("title", title);
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
<cq:includeClientLib css="ebazaarresp.backgroundbanner"/>

<%if(!reqConfig) {%>
	<div id="carousel">
      <div class="p-main-article-background">
         <c:forEach var="slide" items="${list }" varStatus="i">
         	<div class="p-main-article-background-${i.count }" style="left: ${i.index * 100}%;">
	            <img
	               src="${slide.path }"
	               alt="">
	         </div>
         </c:forEach>
      </div>
      <div class="p-white-overlay"></div>
      <div class="p-s01-story-component">
         <div class="p-wrapper">
            <div class="p-wrapper-carousel" style="opacity: 1; left: 0px;width: ${fn:length(list) * 100}%">
				<c:forEach var="slide" items="${list }" varStatus="i">
					<div class="page contentpage basicpage articlepage">
                        <div class="p-main-left" style="width: ${100/fn:length(list)}%">
							<div class="p-block p-large p-absolute-left p-purple">
								<div class="p-block-content">
		                           <div class="p-caption">
		                              <h2>${slide.title }</h2>
		                           </div>
		                        </div>
							</div>
						</div>
					</div>
				</c:forEach>
            </div>
         </div>
      </div>
      
	  <div class="p-arrow p-left"></div>
      <div class="p-arrow p-right"></div>
</div>
<cq:includeClientLib js="ebazaarresp.backgroundbanner"/>
<%} else {
    if( WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.DESIGN ){ 
        %><div style='width:340px; padding:10px; text-align:center; text-transform:uppercase; background:#eee; border:dotted 3px #333;'>Configure in Edit Mode</div><%
    }
}%>

<cq:includeClientLib js="common.multifield"/>
