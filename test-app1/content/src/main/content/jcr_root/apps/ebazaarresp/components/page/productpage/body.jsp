<%@include file="/libs/foundation/global.jsp"%>
<!DOCTYPE HTML>

<html>
<head>


    <title></title>
    <link rel="stylesheet" href="<%=currentDesign.getPath() %>/css/style.css" />   
    <!--[if lt IE 9]>
        <script src="http://css3-mediaqueries-js.googlecode.com/files/css3-mediaqueries.js"></script>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">  
  <script type="text/javascript" src="<%=currentDesign.getPath()%>/js/jquery.js"></script> 
     <script type="text/javascript" src="<%=currentDesign.getPath()%>/js/custom.js"></script>
     <script type="text/javascript" src="<%=currentDesign.getPath()%>/js/jquery.liquidcarousel.pack.js"></script> 



    <meta charset="UTF-8">
</head>
<body>


<%@include file="header.jsp" %>

<cq:include path="prodparcoffee" resourceType="/libs/foundation/components/parsys" />

<%@include file="footer.jsp" %>
</body>
</html>