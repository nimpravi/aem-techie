<%@include file="/libs/foundation/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Philips</title> 

<link href="<%=currentDesign.getPath() %>/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=currentDesign.getPath() %>/css/smoothness/jquery-ui-1.10.4.custom.css" rel="stylesheet" type="text/css">

    <script src="<%=currentDesign.getPath() %>/js/jquery-1.10.2.js"></script>
    <script src="<%=currentDesign.getPath() %>/js/jquery-ui-1.10.4.custom.js"></script>
    <script src="<%=currentDesign.getPath() %>/js/jquery.tinycarousel.js"></script>




<script>

    $(function() {

        $( "#accordion" ).accordion({ collapsible: false });
        
    });
    $(document).ready(function()
        {
            $('#slider1').tinycarousel();
        });


</script>
</head>
<body>
<div class="wrapper">
       
       
     <%@include file="header.jsp"%> 
     
          <cq:include path="prod2" resourceType="/libs/foundation/components/parsys" />
        
         <%@include file="footer.jsp"%> 
     
        
</div>
</body></html>
