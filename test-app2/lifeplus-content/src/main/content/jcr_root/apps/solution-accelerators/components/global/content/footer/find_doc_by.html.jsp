<%--

  find_doc_by component.

  

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
	// TODO add you code here
%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<cq:includeClientLib categories="common.multifield"/>


<div class="col-md-2">
             <p class="font_14bold_grey">${properties.title}</p>
              <ul class="list-unstyled list_def">
<%

  try{

    	Session session = resourceResolver.adaptTo(Session.class);
        Node linkNode = null;

        if(session.itemExists(currentNode.getPath() + "/textwithlink")){
            linkNode = session.getNode(currentNode.getPath()+"/textwithlink");
        }

        if(linkNode != null){

             NodeIterator itemIterator = linkNode.getNodes();
             Node itemNode = null;
             String itemText = null;
             String itemURL = null;
             List<String> itemList = null;

           		  while(itemIterator.hasNext()){
                 itemNode = itemIterator.nextNode();
                 itemText = itemNode.getProperty("text").getString();
                 itemURL  = itemNode.getProperty("link").getString();



                 if(itemURL.startsWith("/")) {
                     itemURL += ".html";
                 } %><li>
         <%

                 out.println(itemText);

                      out.println("\n");

                  }%></li>
             </ul>

         <%

        }else{
            out.println("Please configure the URL paths");
        }
    }catch(Exception ex){
    	out.println("");
    }

    %>

</div>
    
