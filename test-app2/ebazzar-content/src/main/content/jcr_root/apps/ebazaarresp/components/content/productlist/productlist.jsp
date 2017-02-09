<%@ page import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image,
    org.apache.commons.lang.StringUtils" %>
   <%@page import="javax.servlet.http.HttpSession"%> 
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.sling.api.SlingHttpServletRequest"%>
<%@page import="com.day.cq.ebazaar.SearchUtils,
                java.util.Locale,
                java.util.ResourceBundle,
                com.day.cq.i18n.I18n"%>
<%@include file="/libs/foundation/global.jsp" %>

<%@page import="com.day.cq.security.profile.Profile"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>

<cq:setContentBundle/>

<!--<script type="text/javascript">
    //alert("in here ....");
    function redirectToShop(productId){
        var redirecturl = "/content/ebazzar/en/cart.html?prodid=productId";
		window.load(redirecturl);

    }
</script>-->

<%
Locale pageLocale = currentPage.getLanguage(false);
//If above bool is set to true. CQ looks in to page path rather than jcr:language property.
ResourceBundle resourceBundle = slingRequest.getResourceBundle(pageLocale);
I18n i18n = new I18n(resourceBundle);

String applicationName = "";
if(request.getRequestURI() != null){
    String requestUri[] = request.getRequestURI().split("/");
    applicationName = requestUri[2].toString();
    //applicationName = "ebazaar";
}

 //to refine the product details based on gender
 final Profile currentProfile = slingRequest.adaptTo(Profile.class);

 char gender='A';
 Set userProfile=currentProfile.keySet();
 Iterator userIterator=userProfile.iterator();
 while(userIterator.hasNext()){
 Object userKey=userIterator.next();
 if(userKey.toString().equalsIgnoreCase("gender")){
     String userValue=currentProfile.get(userKey).toString();
     if(userValue.equalsIgnoreCase("male")){
         gender='M';
     }else{
         gender='W';
     }
 }
 }


//Product component type
String slingResourceType = "ebazaarresp/components/content/productdetails";

String searchPath = (String)properties.get("searchPath");

//Get search keyword from request parameter
String searchString = (String)slingRequest.getParameter("q");

String searchStringValue = (String)slingRequest.getParameter("qVal");


String pagePath = "";
boolean hasQueryString = false;
//Map to store the Properties which has to be searched against the search keyword
Map inputPropMap = new HashMap();
//temp Map to iterate store result products
Map resultsProdMap = new HashMap();
//List to store all result products
List productDetails = new ArrayList();



List prodDet = new ArrayList();

//if(searchString != null && searchString.trim().length() > 0) {
    
    if(searchString != null && searchString.trim().length() > 0)
    {
        if(searchString.equalsIgnoreCase("offer"))
        {
            inputPropMap.put("prodOffer","");
            hasQueryString = true;          
        }
        else if (searchString.equalsIgnoreCase("weather"))
        {
            inputPropMap.put("prodWeather", searchStringValue);
            hasQueryString = true;
        }
        else if (searchString.equalsIgnoreCase("anonymous"))
        {
            // Do nothing here !
        }
        else
        {
            inputPropMap.put("prodTitle",searchString);
            hasQueryString = true;
        }
    }

    
    //Get the list of products
    if(hasQueryString)
    {
        
        productDetails = SearchUtils.getProductsByProperties(inputPropMap, searchPath, slingResourceType, slingRequest);
    }
    else if ("anonymous".equalsIgnoreCase(searchString))
    {

        productDetails = SearchUtils.getProductListByRatings(slingRequest, slingResourceType);
        
        
    }
    else
    {

        productDetails = SearchUtils.getProductsByPath(searchPath, slingResourceType, slingRequest);
    }
    
    
    if(searchString != null && searchString.trim().length() > 0 && searchString.equalsIgnoreCase("weather")){
        
    Iterator prodIterator=productDetails.iterator();
    while(prodIterator.hasNext()){
        Map productMap=(HashMap)prodIterator.next();
        Set productMapSet=productMap.keySet();
        Iterator productMapIterator=productMapSet.iterator();
        while(productMapIterator.hasNext()){
            Object prodMapKey=productMapIterator.next();
            String prodMapValue=productMap.get(prodMapKey).toString();
           
            if((prodMapKey.toString().equalsIgnoreCase("prodcode")) && prodMapValue.charAt(0)==gender){
                prodDet.add(productMap);
            }
        }
    }
    if(gender!='A'){
    productDetails=prodDet;
    }

    }
    
    if(productDetails.size() > 0) {
        
         categories=request.getParameterValues("categories");  
         

         %>

<ul class="thumbnails">

<%

    for(int i=0;i<productDetails.size();i++){
        
        resultsProdMap = (HashMap)productDetails.get(i);
        if(checkCategory((String)resultsProdMap.get("vimal")) || categories==null ){
        int pageitem = resultsProdMap.get("nodePath").toString().indexOf("/jcr:content");
        pagePath = resultsProdMap.get("nodePath").toString().substring(0, pageitem) + ".html";

        double prodRating = 0.0;
         
        if(resultsProdMap.get("aver") != null)
            prodRating = Double.valueOf(resultsProdMap.get("aver").toString().trim())/20.0; 
                             
            // out.println("Average : " + prodRating);
%>


<li class="span4">
<div class="thumbnail">
    <a href="<%= pagePath%>?prodCode=<%= (String)resultsProdMap.get("prodCode") %>" class="srch_rslt_img" >
        <img  height="200" width="300" src="<%= (String)resultsProdMap.get("fileReference")%>" alt=""/>
  	</a>
    <div class="caption"><h4> <%= (String)resultsProdMap.get("prodTitle") %></h4><p>
								 <% if (resultsProdMap.get("prodOffer")== null ) { %>
        $ <%= (String)resultsProdMap.get("prodPrice") %>
               <% }else
                  { 
               %>
                   <strike>
                       $<%= (String)resultsProdMap.get("prodPrice") %> 
                   </strike>
                   &nbsp;
                   $ <%= Double.parseDouble(resultsProdMap.get("prodPrice").toString()) - (Double.parseDouble(resultsProdMap.get("prodPrice").toString()) * Double.parseDouble(resultsProdMap.get("prodOffer").toString()) / 100) %> 
    <%
                  }
               %></p> <form id="form11" name="form11" method="post" action="/services/ebazaarresp">
            <%
            String prodPrice = (String)resultsProdMap.get("prodPrice");
            String prodTitle = (String)resultsProdMap.get("prodTitle");
            String prodCode = (String)resultsProdMap.get("prodCode");
            String prodDescription =(String)resultsProdMap.get("prodDescription");
            String prodCategories = (String)resultsProdMap.get("prodCategories");
            String fileReference = (String)resultsProdMap.get("fileReference");
            // String applicationName = "";
             String quantity = "1";
             String redirect="";
           String language= "";
            if(request.getRequestURI() != null){
                String requestUri[] = request.getRequestURI().split("/");
                applicationName = requestUri[2].toString();
                language= requestUri[3].toString();
            }
          // prodPrice=realPrice ;
            if(applicationName.equalsIgnoreCase("ebazzar"))
            {

              redirect = "/content/ebazzar/"+language+"/cart.html?prodPrice="+prodPrice+"&prodTitle="+prodTitle+"&prodCode="+prodCode+"&prodCategories="+prodCategories+"&fileReference="+fileReference+"&prodDescription="+prodDescription;

            }
            

            %>
        <a class="btn btn-primary" href="<%= pagePath%>?prodCode=<%= (String)resultsProdMap.get("prodCode") %>" onclick="linkClick()"><%=i18n.get("View") %></a>
          <input type="hidden" name="quantity"  value="1" />
         <input type="hidden" name="redirect" value="<%=redirect %>"/>
         <input name="Add to Cart" type="submit" class="btn btn-success" value="Add to Cart" />

			        <!-- <a class="btn btn-success" href="shopping-cart.html">Add to Cart</a> -->
		          </form>
        								<!-- <a  class="btn btn-success" href="shopping-cart.html">Add to Cart</a> -->
							</div>
    </div>
</li>

<%
        }
    } 
  %></ul>
<!--<div class="pagination">
	<ul>
						<li class"disabled"><span>Prev</span></li>
						<li class"disabled"><span>1</span></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">4</a></li>
						<li><a href="#">5</a></li>
						<li><a href="#">Next</a></li>
					</ul>
				</div>-->
<%
    } else {
        
        out.println("No Matching Records found !!!");
        //out.println("No matching found !!!"); 
    }

//} else { out.println("Search String missing!!!"); }
%>


<%!private String categories[]=null;%>

<%!
private boolean checkCategory(String pCategory){
    if(categories==null)
        return false;
    for(String category : categories){
        if(category.equalsIgnoreCase(pCategory)){
            return true;
        }
    }
    return false;
}
%>

