<%@page session="false"%><%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator Homepage component.

  This component will be used for displaying the contents of the home page

  ==============================================================================

--%>
<%@include file="/libs/foundation/global.jsp"%>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="content">

		<div class="banner col-lg-12 col-md-12 col-sm-12 col-xs-12 padding-zero">
			<cq:include path="banner"
				resourceType="solution-accelerators/components/global/content/homebanner" />
    </div>
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<cq:include path="main"
				resourceType="solution-accelerators/components/global/content/multiviewed" />
			<cq:include path="par" resourceType="foundation/components/parsys" />
		</div>

</div>
