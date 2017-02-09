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
						<span class="label">Type: </span>
                          <form  id="f1"  method="GET" >
<%
    for(String category : categories)

    {%>

						<label class="checkbox inline">

                             <input type="checkbox" name="categories" value="<%=category%>" <% if(checkCategory(category)) { %> checked="checked" <%} %> >
	                      <%=category%> 
						</label>

                              <%}%>
                        <button class="btn btn-primary pull-right" type="submit" onclick="searchbycategory()">Filter</button>
                              </form>
					  </div>
                          <%}
%>
					  <div class="span6">	
						<span class="label">Price: </span>
						<label class="checkbox inline">
						  <input type="checkbox" id="inlineCheckbox1" value="option1"> $0-$10
						</label>
						<label class="checkbox inline">
						  <input type="checkbox" id="inlineCheckbox2" value="option2"> $10-$50
						</label>
						<label class="checkbox inline">
						  <input type="checkbox" id="inlineCheckbox3" value="option3"> $10-$50
						</label>
						<label class="checkbox inline">
						  <input type="checkbox" id="inlineCheckbox3" value="option3"> $100 & above
						</label>
					  </div>
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