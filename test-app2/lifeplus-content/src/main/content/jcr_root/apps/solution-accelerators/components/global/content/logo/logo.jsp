<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator Logo component.

  This component will be used for changing the Logo image

  ==============================================================================

--%>
<%@include file="/libs/foundation/global.jsp"%>
<%@ page
	import="com.day.text.Text,com.day.cq.wcm.foundation.Image,com.day.cq.commons.Doctype"%>
<%
	String home = currentStyle.get("absParent", "#");
%>
<c:set var="image" value="${currentStyle.imageReference}"></c:set>

<a id="logo-comp" href="<%=home%>.html"> <img src="${image}" />
</a>
