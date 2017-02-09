<%@include file="/libs/foundation/global.jsp"%>

    <meta content="width=320px, initial-scale=1, user-scalable=yes" name="viewport" />
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
	<script src="/apps/ebazaarresp/components/content/disclaimerpopup/custom.js"></script>
 	<link href="/apps/ebazaarresp/components/content/disclaimerpopup/overlaypopup.css" rel="stylesheet" type="text/css" />

<% 
   String title = properties.get("title","Add Title");
   String informationText =properties.get("informationText","");
   %> 
<div class="disclaimer-content">
    <%if(title=="Add Title"){%>
	<p><%=title %></p>
    <%}else{%>
    <p><a class="show-popup" href="#"><%=title %></a> </p>
    <%}%>
</div>
 
<div class="overlay-bg">
    <div class="overlay-content">
        <div ><%=informationText %></div>
        <button class="close-btn">Close</button>
    </div>
</div>



