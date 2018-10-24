
 <div class="content_main">
        
        	<div class="content">
                <div class="leftpane">
					<div class="product">
                    	<img src="<%=productDetails.get("fileReference") %>" width="213px" height="192px" />
                        
                        <p class="desc"><%if(null != productDetails.get("prodTitle")){ %><%=productDetails.get("prodTitle").toUpperCase()%><%} %></p>
                        <p class="rate"><%if(null != productDetails.get("prodPrice")){ %>Rs.<%=productDetails.get("prodPrice")%><%} %></p>
                       <cq:include path="rating" resourceType="/libs/foundation/components/parsys" />
                         
                    </div>
                    
                   
                    <div class="leftpane_content">
                   <cq:include path="facebookfeed" resourceType="/libs/foundation/components/parsys" />
                    </div>               
                </div>
                 <div class="rightpane">
                 		<p class="txtcontent">
<%if(null != productDetails.get("prodDescription")){ %>

<%=productDetails.get("prodDescription")%><%}%>                        </p>
                 <div style="height: 150px;">
                <cq:include path="similarProds" resourceType="/apps/ebazaarresp/components/content/philipssimilarproducts" />
                 <cq:include path="storelocator" resourceType="/libs/foundation/components/parsys" />
                 </div>
                 <div class="clearfix"></div>
                 <!-- Accordion -->

	<h3 class="accordian_heading"><span>SPECIFICATIONS</span><hr/></h3>
	<div class="description">
    <table border="0" cellspacing="0" cellpadding="0">
  
       <%if(null != productSpecs){

Iterator<String> productSpecsIter = productSpecs.keySet().iterator();
    while(productSpecsIter.hasNext()){
String specName = productSpecsIter.next();
        String specValue = productSpecs.get(specName);
%>
 <tr>
    <td><%= specName%></td>
    <td><%=specValue %></td>
  </tr>
              <%
    }
}%>
</table>
</div>
</div>
</div>
                 </div>
   <%@include file="productaccessories2.jsp" %>
        
      