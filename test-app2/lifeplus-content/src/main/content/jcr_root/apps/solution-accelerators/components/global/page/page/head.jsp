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
<%@ page
	import="java.util.Date,java.text.SimpleDateFormat,com.day.cq.commons.Doctype,org.apache.commons.lang3.StringEscapeUtils,org.apache.commons.lang3.StringUtils"%>

<cq:include script="/libs/wcm/core/components/init/init.jsp" />
<cq:includeClientLib categories="apps.solution.accelerators" />
<%
	
%><cq:include
	script="/libs/cq/cloudserviceconfigs/components/servicelibs/servicelibs.jsp" />
<%
	currentDesign.writeCssIncludes(pageContext);
%>


