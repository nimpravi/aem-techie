<%--
Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================
  Home banner component.

  SGO Home Banner Component 
  ==============================================================================
  

--%>
<%
	
%><%@include file="/libs/foundation/global.jsp"%>
<%
	
%><%@page session="false"%>
<%
	
%>
<%
	String imagePath = "";
	String imagePath1 = "";
	if (null != properties.get("image/fileReference")) {
		imagePath = properties.get("image/fileReference", "");
	}
if (null != properties.get("image/fileReference1")) {
		imagePath1 = properties.get("image/fileReference1", "");
	}
%>



<img class="visible-lg visible-md" src="<%=imagePath%>">
<img class="visible-sm visible-xs" src="<%=imagePath1%>">

	<div class="caption">
		<c:if test="${not empty properties.bannerTitle}">
			<h2>${properties.bannerTitle}</h2>
		</c:if>
		<c:if test="${not empty properties.bannerDescription}">
       ${properties.bannerDescription}
    </c:if>
		<c:if test="${not empty properties.bannerLinkText}">
			<div class="link">
				<a href="${properties.bannerLink}.html"> <span class="text">${properties.bannerLinkText}</span>
					<span class="arrow-left"></span></a>
			</div>
		</c:if>


	</div>


