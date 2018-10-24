<%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Title component.

  Draws a title either store on the resource or from the page
  ==============================================================================

--%>
<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>
<%
	
%>


			<c:if test="${empty properties.title}">
				<h2>${currentPage.title}</h2>

			</c:if>
			<h2>${properties.title}</h2>

			<c:if test="${not empty properties.text}">
                <div class="titledesc"> 
                    <p>${properties.text}</p>
				</div>
</c:if>
