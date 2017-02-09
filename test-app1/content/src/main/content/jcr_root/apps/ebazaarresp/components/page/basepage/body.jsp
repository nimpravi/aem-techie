<%@include file="/libs/foundation/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="javax.servlet.http.HttpSession"%>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
	<title>Welcome to CSTORE - Products</title>

    <cq:includeClientLib categories="apps.ebazzar.responsive"/>
</head>

<body>
   <cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>
     
    <cq:include path="clickstreamcloud" resourceType="cq/personalization/components/clickstreamcloud"/>
    <!-- 


    <cq:includeClientLib categories="cq.personalization.clickstreamcloud"/>
    --> 
    <cq:includeClientLib categories="cq.personalization"/>
     
 

    <%
    // Clean up Session Value
    // if already done has reached
    javax.servlet.http.HttpSession session = slingRequest.getSession();
    
    String sProdCodeSession = (String)session.getAttribute("pCode");

    if("done".equals(sProdCodeSession))
    {
        //session.setAttribute("pCode", "complete");
    }

    %>
    <!--<cq:include script="header.jsp"/> -->
    <cq:include path="header" resourceType="ebazaarresp/components/content/ebazaar-header"/>
	<div class="container">

             <cq:include script="breadcrumb.jsp"/>


	<div class="row">
       <div class="span12">
         <cq:include script="content.jsp"/>
    </div>

	</div>
	</div>
	
	<hr />
    <cq:include script="footer.jsp"/>
    <cq:include path="analytics" resourceType="cq/analytics/components/analytics"/>

    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&libraries=places&language=en-AU"></script>
       <script type="text/javascript">
            var autocomplete = new google.maps.places.Autocomplete($("#address")[0], {});
            google.maps.event.addListener(autocomplete, 'place_changed', function() {
			var place = autocomplete.getPlace();
			var locName = $('#address').val();

									if(locName.indexOf(',') >0){
									var countryArray = locName.split(',');
									}
									else if(locName.indexOf('-')>0){
									var countryArray = locName.split('-');
									}else if(locName.indexOf('.')>0){
									var countryArray = locName.split('.');
									}						
								$('#geolocA').text(countryArray[0].substr(0,10) + '..');
								$('#geolocB').text(countryArray[0].substr(0,10) + '..');
								$("#geosave").click();

				 });


    </script>
<% out.println(currentDesign.getPath()); %>
<script language="javascript" type="text/javascript" src="<%= slingRequest.getContextPath()+currentDesign.getPath() %>/clientlibs/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="<%= slingRequest.getContextPath()+currentDesign.getPath() %>/clientlibs/js/jquery.coolautosuggest.js"></script>
<link rel="stylesheet" type="text/css" href="<%= slingRequest.getContextPath()+currentDesign.getPath() %>/clientlibs/css/jquery.coolautosuggest.css" />

<% int maxResults = 30; 
String path = "/content/ebazzar/";
   String url = "/bin/suggest?path="+path+"&max="+maxResults+"&query=";
%>
<script type="text/javascript">

    $("#Search").coolautosuggest({
        url:"<%=url%>"+$("#Search").val()
    });

    $(".suggestions").style("top","34px");
    $("#suggestions_holder").style("left","13px !important");

</script>
</body>
</html>