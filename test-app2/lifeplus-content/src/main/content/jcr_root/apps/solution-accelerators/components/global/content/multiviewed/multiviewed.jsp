<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================
  multiviewed component
  
  This components illustrates the steps involved in migration
   ==============================================================================

--%>



<%@include file="/libs/foundation/global.jsp"%>
<%@page
	import="java.util.List,java.util.Map,java.util.ArrayList,java.util.HashMap,com.day.cq.wcm.api.WCMMode"%>
<cq:includeClientLib js="common.multifield" />
<%
	boolean reqConfig = false;
	if (currentNode != null) {

		Node slides = (currentNode.hasNode("surveyset") ? currentNode
				.getNode("surveyset") : null);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		if (slides != null) {
			NodeIterator nodes = slides.getNodes();
			Node slide = null;
			Map<String, String> map = null;
			while (nodes.hasNext()) {
				slide = nodes.nextNode();
				String description = slide.getProperty("description")
						.getString();
				String title = slide.getProperty("title").getString();
				String link = slide.getProperty("link").getString();
				map = new HashMap<String, String>();
				map.put("description", description);
				map.put("title", title);
				map.put("link", link);
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

<%
	if (!reqConfig) {
%>
<h2 class="title">${properties.header }</h2>
<div class="content">
	<c:forEach var="step" items="${list}" varStatus="i">

		<c:if test="${i.count%2==0}">
			<div class="odd box col-lg-4 col-md-4 col-sm-6 col-xs-12">
				<div class="step">
					<a class="circle" href="${step.link }.html">${step.title }</a>
				</div>
				<div class="text">${step.description }</div>
			</div>
		</c:if>
		<c:if test="${i.count%2!=0}">
			<div class="even box col-lg-4 col-md-4 col-sm-6 col-xs-12">
				<div class="step">
					<a class="circle" href="${step.link}.html">${step.title }</a>
				</div>
				<div class="text">${step.description }</div>
			</div>
		</c:if>

	</c:forEach>

</div>

<%
	} else {
		if (WCMMode.fromRequest(request) == WCMMode.EDIT
				|| WCMMode.fromRequest(request) == WCMMode.DESIGN) {
%><div>Configure in Edit Mode</div>
<%
	}
	}
%>
