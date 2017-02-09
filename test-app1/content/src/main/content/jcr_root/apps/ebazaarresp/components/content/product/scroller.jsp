<%@include file="/libs/foundation/global.jsp"%>
         
<%@page import="com.day.cq.dam.api.Asset"%><link rel="stylesheet" type="text/css" href="/apps/ebazaarresp/components/content/product/styles/scrollable-horizontal.css" rel="stylesheet" />
         <link rel="stylesheet" type="text/css" href="/apps/ebazaarresp/components/content/product/styles/scrollable-buttons.css" rel="stylesheet" />
 <script src="http://cdn.jquerytools.org/1.2.7/full/jquery.tools.min.js"></script>


<body>
<%

if(null != productDetails.get("prodTitle")){ %>
<h1 style="margin-left: 300px;"><%=productDetails.get("prodTitle")%></h1><%} %>
<div style="height:400px; width:800px">
<%if(null != productDetails.get("fileReference")){ %>
<div style=" float: left;">


<img src="<%=productDetails.get("fileReference")%>" /></div><%} %>
<div style=" width:400px;float: right;">
<h3>
<%if(null != productDetails.get("prodDescription")){ %>

<p style="margin-top: 80px;"><%=productDetails.get("prodDescription")%><br/><br/><%}%>
<%if(null != productDetails.get("prodPrice")){ %>

Product Price    :   Rs. <%=productDetails.get("prodPrice")%><br/><%} %>
<%if(null != productDetails.get("prodStoreLoc")){ %>
Store Location   :    <%=productDetails.get("prodStoreLoc")%><br/><%} %>
<%if(null != productDetails.get("prodOffer")){ %>
Product Offer    : <%=productDetails.get("prodOffer")%></p></h3><%} %>
<%if(null != productDetails.get("viewmore")){ %>
<a class="button_example" href="<%=productDetails.get("viewmore")%>">Know More</a><%}%></div>
</div>
<%if(relatedProdsPath.size() > 0){%>
<DIV style="margin-left: 500px;"><h2>SIMILAR PRODUCTS</h2></DIV>

<!-- HTML structures -->
<div style="margin:0 auto; width: 634px; height:120px;">
<!-- "previous page" action -->
<a class="prev browse left"></a>

<!-- root element for scrollable -->
<div class="scrollable" id="scrollable">

  <!-- root element for the items -->
  <div class="items">
<%for(int i=0;i<relatedProdsPath.size();i++){
    Resource res = resourceResolver.getResource(relatedProdsPath.get(i));
    Asset asset = res.adaptTo(Asset.class);
Resource rendition = asset.getRendition("cq5dam.thumbnail.140.180.png");
%>
<img src="<%=rendition.getPath() %>"  />
<%  
}
%> 
    <!-- 1-5 -->
   
   
  </div>

</div>

<!-- "next page" action -->
<a class="next browse right"></a>
</div>
<%} %>
<script>
$(function() {
  // initialize scrollable
  $(".scrollable").scrollable();
});
</script>

