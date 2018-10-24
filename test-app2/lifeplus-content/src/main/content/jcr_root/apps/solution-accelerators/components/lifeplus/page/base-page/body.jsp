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
%>
<body>
<div class="container-fluid" id="body_wrapper">
	<div class="container">

        <cq:include script="header.jsp" />

        <cq:include script="content.jsp" />

        <sling:include path="login" resourceType="/apps/solution-accelerators/components/lifeplus/content/SignIn"/>
        <!-- footer component will be included here -->


<div class="container bottom_border ">
     <div class="row pad_top_20  pad_left_15">
        <sling:include path="lifeplusfooter1"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
        <sling:include path="lifeplusfooter2"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
                 <sling:include path="lifeplusfooter3"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
                 <sling:include path="lifeplusfooter4"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
         <sling:include path="lifeplusfooter5"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
         <sling:include path="lifeplusfooter6"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
    </div> 
    <p class="copyright">Copyright 2014 Life Inc. All Rights Reserved</p>
</div>

        </div>
</div>
<div class="overlay"></div>
</body>
