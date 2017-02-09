


<article>    
    <div class="content">
        <section class="leftColumn">
          <div class="spiralfluoresentbox">
            <p class="spiralfluoresent">&nbsp;</p>  
            <p class="accordiantitle"><%if(null != productDetails.get("prodTitle")){ %><%=productDetails.get("prodTitle").toUpperCase()%><%} %></p>
            <p class="productinfo">
<%if(null != productDetails.get("prodDescription")){ %>

<%=productDetails.get("prodDescription")%><%}%>
            </p>
          </div> 
            <div class="accordion accordionItem">
         <h2 class="accord-header" id="occord-first-h">SPECIFICATION</h2>
          <div class="accord-content" id="occord-first">



            <%if(null != productSpecs){

Iterator<String> productSpecsIter = productSpecs.keySet().iterator();
    while(productSpecsIter.hasNext()){
String specName = productSpecsIter.next();
        String specValue = productSpecs.get(specName);
%>
  <p class="labelleft"><%=specName%></p><p class="labelright"><%=specValue%></p>
              <%
    }
}%>

        </div>



                <cq:include path="accordion accordionItem" resourceType="/apps/ebazaarresp/components/content/product-accessories" />

         </div>

        </section>    
        <section class="rightColumn">
           <div class="productDetail" align="center">
                <p><img src="<%=productDetails.get("fileReference") %>" width="113px" height="114px"></p>
                <p class="flouresentTitle"><%if(null != productDetails.get("prodTitle")){ %><%=productDetails.get("prodTitle")%><%} %></p>
                <p class="feedswithoutimage">Similar Products</p>

               <cq:include path="similarProds" resourceType="/apps/ebazaarresp/components/content/philipssimilarproducts" />

          <cq:include path="rating" resourceType="/libs/foundation/components/parsys" />      
           </div>
    <div class="facebookfeeds">


  <cq:include path="facebookfeed" resourceType="/libs/foundation/components/parsys" /></div>    
           
           <cq:include path="storelocator" resourceType="/libs/foundation/components/parsys" />
               
           
        </section>
    </div>
</article>