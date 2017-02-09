<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" import="
		java.util.Iterator,
		java.util.List,
		com.adobe.cq.commerce.api.CommerceService,
		com.adobe.cq.commerce.api.CommerceSession,

		com.day.cq.i18n.I18n,
		com.day.cq.personalization.ClientContextUtil,
		com.day.cq.wcm.foundation.forms.FormsConstants"
%>
<%
    CommerceService commerceService = resource.adaptTo(CommerceService.class);
    CommerceSession session = commerceService.login(slingRequest, slingResponse);


            %>
 <h2>Shopping Cart</h2>
    <table class="table table-striped table-hover">
			      <thead>
			        <tr>
			          <th>Item Name</th>
			          <th>Qty</th>
			          <th>Unit Price</th>
			          <th>Total</th>
		            </tr>
		          </thead>
				  <tbody>
        <%

    String prodTitle = slingRequest.getParameter("prodTitle"); 
    String prodPrice = slingRequest.getParameter("prodPrice");
    String prodCode = slingRequest.getParameter("prodCode");
    String quantity = slingRequest.getParameter("quantity");
    String fileReference = slingRequest.getParameter("fileReference");
    String prodCategories = slingRequest.getParameter("prodCategories");
    String prodDescription = slingRequest.getParameter("prodDescription");
     int totalItem = 0;
     int subtotalItem = 0;
    int shippingcost=10;
//out.println("Title : " + slingRequest.getRequestedSessionId());
//out.println("CNode : " + currentNode.getName());
//out.println("prodcode : " + prodCode);
    if(currentNode != null){
        // out.println("inside first if  : "+currentNode);
        try{
        if(!currentNode.hasNode(slingRequest.getRequestedSessionId())){

               currentNode.addNode(slingRequest.getRequestedSessionId(), "nt:unstructured");  
               currentNode.getSession().save();
               
               Node userNode = currentNode.getNode(slingRequest.getRequestedSessionId());

               if(!userNode.hasNode(prodCode)){
                      userNode.addNode(prodCode, "nt:unstructured");
                      userNode.getSession().save();
                      Node prodNode = userNode.getNode(prodCode);
                      prodNode.setProperty("prodCode", prodCode);
                      prodNode.setProperty("prodPrice", prodPrice);
                      prodNode.setProperty("quantity", quantity);
                      prodNode.setProperty("prodTitle", prodTitle);
                      prodNode.setProperty("fileReference", fileReference);
                      prodNode.setProperty("prodCategories", prodCategories);
                      prodNode.setProperty("prodDescription", prodDescription);
                      prodNode.getSession().save(); 
               }

        }else{
             Node userNode = currentNode.getNode(slingRequest.getRequestedSessionId());
            //out.println("inside else  : ");
             if(!userNode.hasNode(prodCode)){
                 // out.println("inside else  if  : ");
                  userNode.addNode(prodCode, "nt:unstructured");
                  userNode.getSession().save();
                  Node prodNode = userNode.getNode(prodCode);
                  prodNode.setProperty("prodCode", prodCode);
                  prodNode.setProperty("prodPrice", prodPrice);
                  prodNode.setProperty("quantity", quantity);
                  prodNode.setProperty("prodTitle", prodTitle);
                  prodNode.setProperty("fileReference", fileReference);
                  prodNode.setProperty("prodCategories", prodCategories);
                  prodNode.setProperty("prodDescription", prodDescription);
                  prodNode.getSession().save();                            
             }else{
                 //out.println("inside else  else  : ");
                  Node prodNode = userNode.getNode(prodCode);
                  prodNode.setProperty("prodCode", prodCode);
                  prodNode.setProperty("prodPrice", prodPrice);
                  prodNode.setProperty("quantity", quantity);
                  prodNode.setProperty("prodTitle", prodTitle);
                  prodNode.setProperty("fileReference", fileReference);
                  prodNode.setProperty("prodCategories", prodCategories);
                  prodNode.setProperty("prodDescription", prodDescription);
                  prodNode.getSession().save();
             }      
        }       
        }catch(Exception e){}

        try{
            Node userNode = currentNode.getNode(slingRequest.getRequestedSessionId()); 
            NodeIterator productNodes = userNode.getNodes();
            int count = 0;
            int totalPrice;

            if(slingRequest.getRemoteUser()!="anonymous"){
               while(productNodes.hasNext()){
               Node prodNode = productNodes.nextNode();            
               String prodTitle1 =  prodNode.getProperty("prodTitle").getString(); 
               String prodPrice1 = prodNode.getProperty("prodPrice").getString();
               String prodCode1 = prodNode.getProperty("prodCode").getString();
               String quantity1 = prodNode.getProperty("quantity").getString();
               totalPrice = Integer.parseInt(prodPrice1)*Integer.parseInt(quantity1);
               subtotalItem = subtotalItem+totalPrice;
               totalItem = subtotalItem+shippingcost;
               String fileReference1 = prodNode.getProperty("fileReference").getString();
               String prodCategories1 = prodNode.getProperty("prodCategories").getString();
               String prodDescription1 = prodNode.getProperty("prodDescription").getString();
                   //int  shippingcost=

               %>
                <tr>
			          <td><%= prodCode1%></td>
			          <td><input id="quantity-1" name="quantity-1" type="text" class="span1" value="<%= quantity1%>" />

                    <a href="<%=currentPage.getPath() %>.html"><i class="icon-refresh"></i></a>

                <form id="myform" action="<%= resource.getPath() %>.delete.html" method="POST" style="display:inline" >                    
               <input type="hidden" name="prodCode" value="<%=prodCode1 %>"/>
               <input type="hidden" name="redirect" value="<%=currentPage.getPath() %>.html"/>
                  <button type="submit"><i class="icon-trash"></i></button>

               </form>

                    </td>
			          <td>$<%= prodPrice1%></td>
			          <td>$<%= totalPrice %></td>
		            </tr>
               <%
               count++;
               }%>

          <% }else{
              out.println("Please login to add items in the cart");
          }
        }catch(Exception e){}
            %>
        
           <%}else{
               out.println("No items in the Cart");
           } %>
                      </tbody>
		        </table>
     <dl class="dl-horizontal pull-right">
			    <dt>Sub-total:</dt>
			    <dd>$<%=subtotalItem%></dd>
			    <dt>Shipping Cost:</dt>
			    <dd>$<%=shippingcost%></dd>
			    <dt>Total:</dt>
			    <dd>$<%= totalItem %></dd>
		      </dl>


 <div class="clearfix"></div>
			  <a href="/content/ebazzar/en/checkout.html" class="btn btn-success pull-right">Check out</a> <a href="/content/ebazzar/en/fashion/men/mfootwear.html" class="btn btn-primary">Continue Shopping</a> 

