<%@page session="false" pageEncoding="utf-8" %>
<%@include file="/libs/wcm/global.jsp"%>
<%
%>
<link rel="stylesheet" href="<%= currentDesign.getPath() %>/clientlibs/css/jquery-ui.css">
<script src="<%= currentDesign.getPath() %>/clientlibs/js/jquery-1.9.1.js"></script>
<script src="<%= currentDesign.getPath() %>/clientlibs/js/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css">

<%@ page import="javax.jcr.Node, 
                   javax.jcr.NodeIterator, 
                   javax.jcr.Session,
                   javax.jcr.query.Query,
                   javax.jcr.query.QueryManager,
                   javax.jcr.query.QueryResult"%>
<%

String height  = currentStyle.get("height")!=null ? currentStyle.get("height").toString():null;
String width   = currentStyle.get("width")!=null ? currentStyle.get("width").toString():null;


/*String productCode = java.net.URLDecoder.decode(request.getQueryString().split("=")[1],"UTF-8");
if(productCode.contains("&")){
    productCode = productCode.substring(0,productCode.indexOf("&"));
}*/
String productCode = slingRequest.getParameter("prodCode");

String prodStoreLoc = null;
Session session = currentNode.getSession();

try{

	QueryManager queryManager = session.getWorkspace().getQueryManager();
    Query query               = queryManager.createQuery("/jcr:root/content/cstore/en//element(*, nt:unstructured) [@sling:resourceType = 'ebazaarresp/components/content/productdetails']", Query.XPATH);
    QueryResult queryResult   = query.execute();
    NodeIterator nodeIterator = queryResult.getNodes();
    
    while(nodeIterator.hasNext()){
    	Node resultNode = nodeIterator.nextNode();
        if(resultNode.hasProperty("prodCode")){
        	String prodCodeProp = resultNode.getProperty("prodCode").getString().trim();
            //out.println(resultNode.getPath()+", ");
           	if(productCode.equalsIgnoreCase(prodCodeProp)){
                if(resultNode.hasProperty("prodStoreLoc")){
                    prodStoreLoc = resultNode.getProperty("prodStoreLoc").getString();
                    //out.println("PSL "+prodStoreLoc);
                }
        	}
        }

    }
}catch(Exception ex){
    ex.printStackTrace();
    //out.println("Exception is: "+ex);
}

if(height != null && width != null){%>
<div id="mapdiv" style="display: none;">
    <iframe width="<%= width%>" height="<%=height%>" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" 
            src="http://maps.google.com.au/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=<%=prodStoreLoc%>&amp;aq=&amp;vpsrc=0&amp;ie=UTF8&amp;hq=&amp;hnear=<%=prodStoreLoc%>&amp;z=14&amp;output=embed">
    </iframe>
</div>
<div>
    <img src="<%= currentDesign.getPath()%>/clientlibs/images/storelocator.jpg" id="CheckStore" height="100" width="100" style="cursor:pointer"/>
</div>
<script>
  $("#CheckStore").click(function() {
        $("#mapdiv").attr("style","display:block");
        $( "#mapdiv" ).dialog({
        		height: <%= height %>,
          		width: <%= width %>,
          		modal: true,
            	buttons: {
        			Ok: function() {
          				$( this ).dialog( "close" );
        			}
                }
        });
      $(".ui-dialog-titlebar-close").remove();
  });
    
</script>

<%}else{
	out.println("Please configure height and width in design mode");
} %>
