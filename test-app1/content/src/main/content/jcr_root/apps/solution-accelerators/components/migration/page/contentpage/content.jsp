<%--
Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================
  This is the accelerator content page component component.

  This component will be used for displaying the content page
 ==============================================================================
  
--%>
<%
	
%><%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>
<%
	
%>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="content">
	<cq:include path="titletext"
		resourceType="solution-accelerators/components/migration/content/title" />
	<cq:include path="par" resourceType="foundation/components/parsys" />
	<cq:include path="custompar" resourceType="foundation/components/parsys" />
</div>