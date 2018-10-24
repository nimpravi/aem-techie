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
<div id="bg" style="margin-left: auto;">
			<div id="shadowLeft"></div>
			<div id="shadowRight"></div>
			<div id="shadowBottom"></div>
		</div>
<div id="container" style="margin-left: auto;">
	<cq:include script="header.jsp" />
    <cq:include path="helpdropdownmenu" resourceType="/apps/solution-accelerators/components/demo-site/content/helpdropdownmenu" />
	<cq:include script="content.jsp" />
    <!-- MenuDropDown Start-->
			<div id="menuDropDown" style="left: 380px; visibility: hidden;">
				<div id="pointerBG"><div id="pointer" style="left: 257.5px;"></div></div>
				<div id="menuDDLeft" style="height: auto;"></div>
				<div id="menuDDRight"></div>
			</div>
			<!-- MenuDropDown End-->
	<cq:include script="footer.jsp" />
</div>
</body>





