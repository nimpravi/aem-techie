<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator Search Box component.

  This component will be used for displaying the search box

  ==============================================================================

--%>
<%@include file="/libs/foundation/global.jsp"%>
<%
	
%><%@page session="false"%>
<%
	
%>
<form class="navbar-form form-search pull-right">
	<div class="input-append">
		<input type="text" class="span2 search-query" placeholder="Search">
		<button type="submit" class="btn">
			<i class="icon-search"></i>
		</button>
	</div>
</form>