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

	<cq:include script="header.jsp" />
	<cq:include script="content.jsp" />
	<cq:include script="footer.jsp" />

</body>

<script src="/etc/designs/demo-kao/clientlibs/js/jquery.js" ></script>

<script src="/etc/designs/demo-kao/clientlibs/js/bootstrap.min.js" ></script>



<script type="text/javascript"> 
$(function() { 

	//Goto Top
	$('.gototop').click(function(event) {
		 event.preventDefault();
		// $(".collapse").toggle();
        // alert("dsfs");
		 $('html, body').animate({
			 scrollTop: $(document).height()
		 }, 500);
	});	
	$('#demo').on('shown.bs.collapse', function () {
   $(".kaoup").removeClass("fa fa-angle-up").addClass("fa fa-angle-down");
	     $(".footerlast").removeClass("in").addClass("collapse");
});

$('#demo').on('hidden.bs.collapse', function () {
   $(".kaoup").removeClass("fa fa-angle-down").addClass("fa fa-angle-up");
     $(".footerlast").removeClass("collapse").addClass("in");
});
	//End goto top	
});
</script><!-- /SL Slider -->
