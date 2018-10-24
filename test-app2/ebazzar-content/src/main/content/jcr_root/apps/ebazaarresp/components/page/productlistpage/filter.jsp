<%@include file="/libs/foundation/global.jsp" %>


<script>
     function searchbycategory(){

    

        document.getElementById("f1").submit();
    }
</script>


<div class="row">
			<div class="span12">
				<div class="accordion" id="accordion1">
				  <div class="accordion-group">
					<div class="accordion-heading">
					  <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion1" href="#collapseOne">
						<b>Filters</b>
					  </a>
					</div>

					<div id="collapseOne" class="accordion-body collapse">
					  <div class="accordion-inner">
                               <%             

                            pCategories=request.getParameterValues("categories");  

                           String categories[]=properties.get("categories",String[].class);

                         if(categories!=null){
                        %>
              <div class="span6">

                          <form  id="f1"  method="GET" ><span class="label">Type: </span>
<%
    for(String category : categories)

    {%>

						<label class="checkbox inline">

                             <input type="checkbox" name="categories" value="<%=category%>" <% if(checkCategory(category)) { %> checked="checked" <%} %> >
	                      <%=category%> 
						</label>

                              <%}%>&nbsp;
                        <button class="btn pull-right" type="submit" onclick="searchbycategory()">Filter</button>
                              </form>
					  </div>
                          <%}

pPrices=request.getParameterValues("price");  

String prices[]=properties.get("price",String[].class);

if(prices!=null){ 
%>
					  <div class="span6">	
						<form id="f2"  method="GET"><span class="label">Price: </span>

                          <%
    for(String price : prices){%>
						<label class="checkbox inline">
						 <input type="checkbox" name="prices" value="<%=price%>" <% if(checkPrice(price)) { %> checked="checked" <%} %> >
	                      $<%=price%> 
						</label>
 <%}%>&nbsp;
                          <button class="btn  pull-right" type="submit">Filter</button>
			      </form>
					  </div>
                          <%}%>
					  </div>
					  
					</div>
				 </div>
				</div> 
			</div>
		</div>	






<%!private String pCategories[]=null;%>
<%!private String pPrices[]=null;%>

<%!
    
    private boolean checkCategory(String pCategory){
    
    if(pCategories==null)
        
        return false;

    for(String category : pCategories){
        
        if(category.equalsIgnoreCase(pCategory)){

            return true;
            
        }
            }

    return false;

}%>



<%!
    
    private boolean checkPrice(String pPrice){
    
    if(pPrices==null)
        
        return false;

    for(String price : pPrices){
        
        if(price.equalsIgnoreCase(pPrice)){
            
            return true;
            
        }
            }

    return false;
    
}%>