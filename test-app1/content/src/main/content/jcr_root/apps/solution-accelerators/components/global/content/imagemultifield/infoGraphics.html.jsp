<%@include file="/libs/foundation/global.jsp"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="com.day.cq.wcm.foundation.Image" %>
<%@ page import="org.apache.sling.commons.json.JSONArray" %>

<%
    Iterator<Resource> children = resource.listChildren();

if(children.hasNext()){

      Resource imagesResource = children.next();
    Node imageNode = imagesResource.adaptTo(Node.class);


        ValueMap map = imagesResource.adaptTo(ValueMap.class);
        String order = map.get("order", String.class);
		String str = "";

   		  order = order.substring(1,order.length()-1);
          String[] orderSplit = order.split(",");

        Image img = null; String src = null;

    Node node = null;
    %>
<section class="other-venture hidden-xs hidden-sm">
    <div class="container">
        <div class="row-fluid">
        <div class="col-md-12"> 
            <h4><img src='<%=currentNode.getProperty("iconReference").getString()%>' type="image/png" alt='<%=currentNode.getProperty("alt").getString()%>'> <%=currentNode.getProperty("linktext").getString()%></h4>
        </div>
        </div>
    </div>
<div class="container">
    <div class="row-fluid">  
    <%      for(int i = 0; i < orderSplit.length; i++){
               str = orderSplit[i];
    		str = str.substring(1,str.length()-1);
            	node = imageNode.getNode(str); 
             if(node.hasProperty("fileReference")) {
            %>
        		<div class="col-md-1">
                    <div class="thumbnail with-caption yl borleft">
                        <img src='<%=node.getProperty("fileReference").getString()%>'/>
                    </div>
             <% if(node.hasProperty("imagetext")) {  %>      
                    <a href="http://www.google.com"><p></p></a>
             <% } %>
        		</div>
            <%
             }
            } 
        %>
        
       <div class="col-md-3">
                <div class="row">
                    <div class="col-md-12">
                        <ul class="row outerDiv">
                            <li class="col-lg-12 col-xs-12 col-sm-12 item">
                                <div class="col-lg-2 col-xs-2 col-sm-2  pull-left"><img src="/content/dam/demo-kao/inquiry.png"></div>
                                <div class="col-lg-9 col-xs-9 col-sm-9 details pull-left">
                                     <a href="http://www.google.com"><p class="pr">Product Inquiry</p></a>
                                    <a href="http://www.google.com"> <p class="pt">Click to view more</p></a>
                                </div>
                                <div class="col-lg-1 col-xs-1 col-sm-1 arrow pull-left"> <img class="pull-right" src="/content/dam/demo-kao/arrow.png"> </div>
                                <div class="clear-fix"></div>
                        	</li>
                            <div class="clear-fix"></div>
                           <li class="col-lg-12 col-xs-12 col-sm-12 item">
                               <div class="col-lg-2 col-xs-2 col-sm-2  pull-left"><img src="/content/dam/demo-kao/QandA.png"></div>
                               <div class="col-lg-9 col-xs-9 col-sm-9 details pull-left">
                                     <a href="http://www.google.com"><p class="pr">Product Q &amp; A</p></a>
                                   <a href="http://www.google.com">  <p class="pt">Ask your question here</p></a>
                                </div>
                               <div class="col-lg-1 col-xs-1 col-sm-1 arrow pull-left"> <img class="pull-right" src="/content/dam/demo-kao/arrow.png"> </div>
                               <div class="clear-fix"></div>
                            </li>
                        </ul> 
                    </div>

                </div>


    </div>
</div>
</section>

<section class="other-venture visible-xs visible-sm">
    <div class="container">
        <div class="row-fluid">
            <div class="col-md-12"> 
                <h4> 
                    <img src='<%=currentNode.getProperty("iconReference").getString()%>' type="image/png" alt='<%=currentNode.getProperty("alt").getString()%>'> <%=currentNode.getProperty("linktext").getString()%>
                </h4>
            </div>
        </div>
     </div>

    <div class="container" style="padding-left: 30px;">
        <div class="row-fluid">
            <%      for(int i = 0; i < orderSplit.length; i++){
                   str = orderSplit[i];
                str = str.substring(1,str.length()-1);
                    node = imageNode.getNode(str); 
                 if(node.hasProperty("fileReference")) {
                %>
                    <div class="col-md-1  thm yl">
                        <div class="thm1">
                        <img src='<%=node.getProperty("fileReference").getString()%>'>
                    	</div>

                 <% if(node.hasProperty("imagetext")) {  %>      
                         <div></div>
                 <% } %>
                    </div>
                <%
                 }
                } 
            %>

        </div>

            <div class="col-md-3" style="margin-right:0px margin-top:-5px;">
                <div class="row" style="margin-right:0px;">
                <div class="col-md-12" style="border: 1px solid #ccc;"><ul id="prodInq" style="display:inline-block; padding-left:0px;"><li><img style="margin-top: -60px;" src="/content/dam/demo-kao/inquiry.png">
                </li><li><h3 style="color:#ff8c25;">Product Inquiry</h3>Click to view more</li><li></li></ul> <img id="prodInqArrow" class="pull-right" src="/content/dam/demo-kao/arrow.png" style="margin-top:16px;"></div>
                
                </div>
                <div class="row" style="margin-right:0px;">
                <div class="col-md-12" style="border: 1px solid #ccc; border-top:none;"><ul id="prodInq" style="display:inline-block; padding-left:0px;"><li><img style="margin-top: -60px;" src="/content/dam/demo-kao/inquiry.png">
                </li><li><h3 style="color:#ff8c25;">Product Inquiry</h3>Click to view more</li><li></li></ul> <img id="prodInqArrow" class="pull-right" src="/content/dam/demo-kao/arrow.png" style="margin-top:16px;"></div>
                
                </div>
                
                </div>

            </div>
        </div>    
    </div>
</section>

<%}else
{
    out.println("Configure Images");
}

%>

