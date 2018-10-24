<%@include file="/libs/foundation/global.jsp" %>

<script>
     function searchbycategory(){

        document.getElementById("f1").submit();
    }
    function searchbyprice(){
    alert('price');
    var price=document.getElementById("f2");


    }
</script>




    <%             

pCategories=request.getParameterValues("categories");  

String categories[]=properties.get("categories",String[].class);

if(categories!=null){
    %>
    <div class="well">
					<h4>by Type</h4>
					<form  id="f1"  method="GET" >

<%
    for(String category : categories)

    {%>

						<label class="checkbox">

                              <input type="checkbox" name="categories" value="<%=category%>" <% if(checkCategory(category)) { %> checked="checked" <%} %> >
	                      <%=category%> 
						</label>

                        <%}%>

	<button class="btn btn-primary pull-right" type="submit" onclick="searchbycategory()">Filter</button>
					</form>
				</div>

<%
}

pPrices=request.getParameterValues("price");  

String prices[]=properties.get("price",String[].class);

if(prices!=null){ %>
<div class="well">
				  <h4>by Price</h4>
				  <form id="f2"  method="GET">
<%
    for(String price : prices){%>

				    <label class="checkbox">
				          <input type="checkbox" name="prices" value="<%=price%>" <% if(checkPrice(price)) { %> checked="checked" <%} %> >
	                      $<%=price%> 
						</label>
 <%}%>
				    <button class="btn btn-primary pull-right" type="submit" onclick="searchbyprice()">Filter</button>
			      </form>
			  </div>
<%}%>



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
