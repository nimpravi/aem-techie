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
<%@page import="java.util.Set"%><cq:setContentBundle/>
<%
Locale pageLocale = currentPage.getLanguage(false);
//If above bool is set to true. CQ looks in to page path rather than jcr:language property.
ResourceBundle resourceBundle = slingRequest.getResourceBundle(pageLocale);
I18n i18n = new I18n(resourceBundle);
List Rprod=new ArrayList();

%>
<%
String applicationName = "";
if(request.getRequestURI() != null){
    String requestUri[] = request.getRequestURI().split("/");
    applicationName = requestUri[2].toString();
    //applicationName = "ebazaar";
}
 %>


<%

//Product component type
String slingResourceType = "ebazaar/components/component/productdetails";
Map resultsProdMap = new HashMap();
String searchPath = "/content/ebazzar/en";
String pagePath = "";
boolean hasQueryString = false;

List prodDet = new ArrayList();

javax.servlet.http.HttpSession session = slingRequest.getSession();

    Rprod =(List) session.getAttribute("RecentVProduct");
if(Rprod!=null){
%>
<div>
<b>Recently Viewed Product</b>
<ul class="thumbnails">

    <%

    for(int i=0;i<Rprod.size();i++){
        
        resultsProdMap = (HashMap)Rprod.get(i);

        int pageitem = resultsProdMap.get("nodePath").toString().indexOf("/jcr:content");
        pagePath = resultsProdMap.get("nodePath").toString().substring(0, pageitem) + ".html";

    //  double prodRating = 0.0;
         
    //  if(resultsProdMap.get("aver") != null)
    //     prodRating = Double.valueOf(resultsProdMap.get("aver").toString().trim())/20.0; 

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

    <%}

%>
    </ul>
 </div>
        <%
               }else{%>
        <b>No Recently Viewed Product</b>

<%}
%>
