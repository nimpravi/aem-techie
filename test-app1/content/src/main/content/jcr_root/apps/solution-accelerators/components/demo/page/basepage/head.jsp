<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator Header script.

  Includes the scripts and css to be included in the head tag

  ==============================================================================

--%>
<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>
<%

%>



<cq:include script="/libs/wcm/core/components/init/init.jsp" />

<%

%><cq:include
	script="/libs/cq/cloudserviceconfigs/components/servicelibs/servicelibs.jsp" />
<%
	currentDesign.writeCssIncludes(pageContext);
%>



	<link href="/etc/designs/demo-kao/clientlibs/css/bootstrap.min.css" rel="stylesheet">
    <link href="/etc/designs/demo-kao/clientlibs/css/font-awesome.min.css" rel="stylesheet">

    <link href="/etc/designs/demo-kao/clientlibs/css/mainPage.css" rel="stylesheet">
    <link href="/etc/designs/demo-kao/clientlibs/css/responsive.css" rel="stylesheet">
	<link href="/etc/designs/demo-kao/clientlibs/css/responsiveTab.css" rel="stylesheet">



