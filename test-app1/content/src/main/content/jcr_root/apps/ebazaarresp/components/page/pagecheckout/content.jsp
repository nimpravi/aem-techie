
<jsp:directive.include file="/libs/foundation/global.jsp" />
<jsp:directive.page import="com.day.cq.commons.Doctype,com.day.cq.wcm.api.components.DropTarget,com.day.cq.wcm.foundation.Image,org.apache.commons.lang.StringUtils,org.apache.sling.api.resource.ResourceResolver" />
 <jsp:directive.page import="com.day.cq.commons.jcr.JcrUtil,java.util.Iterator,javax.jcr.Node,javax.jcr.NodeIterator,com.day.cq.wcm.api.components.DropTarget,org.apache.jackrabbit.commons.JcrUtils"/>
<%@ page import="com.day.cq.i18n.I18n,
                   com.day.cq.personalization.ProfileUtil,
                   com.day.cq.security.profile.Profile"%>

<%
 final Profile currentProfile = slingRequest.adaptTo(Profile.class);
    final boolean isAnonymous = ProfileUtil.isAnonymous(currentProfile);
    String aclass="accordion-body collapse";
    %>
	    <div class="span12">
	      <h2>Checkout Process</h2>
	      <div class="accordion" id="accordion2">
            <%if (isAnonymous) { %>
	        <div class="accordion-group">
	          <div class="accordion-heading"> <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">CHECKOUT OPTIONS </a> </div>
	          <div id="collapseOne" class="accordion-body collapse in">
	            <div class="accordion-inner">
	              <div class="span4">

                      <a href="#myModal" data-toggle="modal">Registered User</a> <br>
					    <a data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo" >Guest User</a><br>

	                <em>By creating an account you will be able to shop faster, be up to date on an order's status, and keep track of the orders you have previously made.</em> </div>

                </div>
              </div>
            </div>
              <%}else{
                aclass="accordion-body collapse in";
        }
        %>
	        <div class="accordion-group">
	          <div class="accordion-heading"> <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo"> ACCOUNT & BILLING DETAILS </a> </div>
	          <div id="collapseTwo" class="<%=aclass%>">
	            <div class="accordion-inner">
                    <cq:include path="baddress" resourceType="foundation/components/parsys"/>
	                <br />
	               <a data-toggle="collapse" data-parent="#accordion2" href="#collapseThree" class="btn btn-primary">Continue</a>

                </div>
              </div>
            </div>
	        <div class="accordion-group">
	          <div class="accordion-heading"> <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThree"> SHIPPING DETAILS </a> </div>
	          <div id="collapseThree" class="accordion-body collapse">
	            <div class="accordion-inner">
					<cq:include path="saddress" resourceType="foundation/components/parsys"/>
	              <br />
	              <a data-toggle="collapse" data-parent="#accordion2" href="#collapseFour" class="btn btn-primary">Continue</a>
                </div>
              </div>
            </div>
	        <div class="accordion-group">
	          <div class="accordion-heading"> <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseFour"> SHIPPING METHOD </a> </div>
	          <div id="collapseFour" class="accordion-body collapse">
	            <div class="accordion-inner">
	              <form>
	                <label class="radio">
	                  <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" >
	                  Free Shipping <b>($0.00)</b> </label>
	                <label class="radio">
	                  <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2" checked>
	                  Flat Shipping Rate <b>($10.00)</b> </label>
	                <a data-toggle="collapse" data-parent="#accordion2" href="#collapseFive" class="btn btn-primary">Continue</a>
                  </form>
                </div>
              </div>
            </div>
	        <div class="accordion-group">
	          <div class="accordion-heading"> <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseFive">  PAYMENT METHOD </a> </div>
	          <div id="collapseFive" class="accordion-body collapse">
	            <div class="accordion-inner">
	              <form>
	                <label class="radio">
	                  <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" >
	                  Paypal</label>
	                <label class="radio">
	                  <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2" checked>
	                  Cash on Delivery </label>
	               <a data-toggle="collapse" data-parent="#accordion2" href="#collapseSix" class="btn btn-primary">Continue</a>
                  </form>
                </div>
              </div>
            </div>
	        <div class="accordion-group">
	          <div class="accordion-heading"> <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseSix"> CONFIRM ORDER </a> </div>
	          <div id="collapseSix" class="accordion-body collapse">
	            <div class="accordion-inner">
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
        int totalItem = 0;
     int subtotalItem = 0;
    int shippingcost=10;
     int totalPrice;
   String id= slingRequest.getRequestedSessionId();
    String resourcePath = "/content/ebazzar/en/cart/jcr:content/par/shoppingcart/"+slingRequest.getRequestedSessionId();
    Resource res = resourceResolver.getResource(resourcePath);
   Iterator<Resource> children = res.listChildren();

    while(children.hasNext()){
       Resource child = children.next();
       ValueMap prop = child.adaptTo(ValueMap.class);
         String prodCode = prop.get("prodCode", String.class);
         String prodPrice = prop.get("prodPrice",String.class);
         String quantity = prop.get("quantity",String.class);
     totalPrice = Integer.parseInt(prodPrice)*Integer.parseInt(quantity);
          subtotalItem = subtotalItem+totalPrice;
               totalItem = subtotalItem+shippingcost;

%>
                <tr>
			          <td><%= prodCode%></td>
			          <td><%= quantity%></td>
			          <td>$<%= prodPrice%></td>
			          <td>$<%= totalPrice %></td>
		            </tr>

<%
}

%>

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
	              <a href="/content/ebazzar/en/confirm-order.html" class="btn btn-success pull-right">Confirm Order</a> </div>
              </div>
            </div>
          </div>
        </div>
