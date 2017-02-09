<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Default body script.

  Draws the HTML body with the page content.

  ==============================================================================

--%>
<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>
<%
	StringBuffer cls = new StringBuffer();
	for (String c : componentContext.getCssClassNames()) {
		cls.append(c).append(" ");
	}
%><body>
	<cq:include path="clientcontext"
		resourceType="cq/personalization/components/clientcontext" />

	<div class="container">
		<cq:include script="header.jsp" />
		<sling:include path="navmenu"
			resourceType="solution-accelerators/components/global/content/topnav" addSelectors="migrationtopnav"/>
		<cq:include script="content.jsp" />
		<cq:include path="footer"
			resourceType="solution-accelerators/components/global/content/footer" />
	</div>
	<cq:include path="timing" resourceType="foundation/components/timing" />
	<cq:include path="cloudservices"
		resourceType="cq/cloudserviceconfigs/components/servicecomponents" />
    <div class="shade"></div>
    <div class="loading"><img class="loading-image" src="/etc/designs/solution-accelerators/images/loading-image.gif">
        <span style="position: relative; top: 55px; color:#fff;">Processing your request..Please wait...</span>
    </div>
</body>
