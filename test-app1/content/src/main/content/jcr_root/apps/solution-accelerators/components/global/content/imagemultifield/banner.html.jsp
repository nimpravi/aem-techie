<%@include file="/libs/foundation/global.jsp"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="com.day.cq.wcm.foundation.Image" %>
<%@ page import="org.apache.sling.commons.json.JSONArray" %>
<%@page import="java.util.List,java.util.Map,java.util.ArrayList,java.util.Map,java.util.HashMap,com.day.cq.wcm.api.WCMMode" %>


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
<section class="brands-website hidden-xs hidden-sm"> 
 <div class="container">
     <div class="row-fluid">
        <div class="col-md-12"> 
            <h4><img src='<%=currentNode.getProperty("iconReference").getString()%>' type="image/png" alt='<%=currentNode.getProperty("alt").getString()%>'> <%=currentNode.getProperty("linktext").getString()%></h4>
        </div>
    </div>
<div class="row-fluid">
	<div class="col-md-12">
		<ul>

<%      for(int i = 0; i < orderSplit.length; i++){
    		str = orderSplit[i];
    		str = str.substring(1,str.length()-1);
            	node = imageNode.getNode(str); 
      		 if(node.hasProperty("fileReference")) {
        %>
            <li><a href="http://www.google.com"> <img src='<%=node.getProperty("fileReference").getString()%>'/> </a></li>
        <%
   		 }
		}
    %>
        </ul>
    </div>
</div>
    </div>
</section>

<section class="brands-website visible-xs visible-sm hidden-lg hidden-md"> 
    <div class="container-fluid">
        <div class="row">
        <div class="col-md-12"> <h4> 
            <img src='<%=currentNode.getProperty("iconReference").getString()%>' type="image/png" alt='<%=currentNode.getProperty("alt").getString()%>'> <%=currentNode.getProperty("linktext").getString()%>
                <button type="button" class="btn btn-success" data-toggle="collapse" data-target="#demo1" aria-expanded="true" aria-controls="demo1">
                     <i class="fa fa-plus-circle"></i>
                </button>
            </h4>
            </div>
        </div>
    
        <div id="demo1" class="collapse in">
            <div class="col-md-12" id="mainContent">
                <ul class="list-inline">
                    <%      for(int i = 0; i < orderSplit.length; i++){
                            str = orderSplit[i];
                            str = str.substring(1,str.length()-1);
                                node = imageNode.getNode(str); 
                             if(node.hasProperty("fileReference")) {
                        %>
                            <li><img src='<%=node.getProperty("fileReference").getString()%>'/></li>
                        <%
                         }
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</section>

<% } else
{
    out.println("Configure Images");
}

%>

