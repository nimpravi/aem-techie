<%@include file="/libs/foundation/global.jsp"%><%
%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>


<div class="main-products">    
        
     <div class="product-list">
    <h3 class="accordian_heading"><span>ACCESSORIES</span><hr/></h3> 
    <div class="products">
    

<%
String prodCode = "";
 String title = "";
String desc = "";
if(currentPage != null){
NodeIterator childnodes = currentPage.adaptTo(Node.class).getNodes();
    //out.println("currentPage.getName>>"+currentPage.getName());

ArrayList<String> descPropertyList = new ArrayList<String>();

int i1= 0;

while (childnodes.hasNext()) {
    Node script = childnodes.nextNode();
    String name = script.getName();
    if(name.equalsIgnoreCase("jcr:content")){
    NodeIterator scriptNodeIter = script.getNodes();
    while(scriptNodeIter.hasNext()){
    Node scriptNode = scriptNodeIter.nextNode();
        //out.print("Node name>> "+scriptNode);

    if(scriptNode.hasProperty("sling:resourceType")){
        if(null != scriptNode.getProperty("sling:resourceType") && scriptNode.getProperty("sling:resourceType").getValue().getString().equals("foundation/components/parsys")){
    NodeIterator parNodeIter = scriptNode.getNodes();
    while(parNodeIter.hasNext()){
    Node productNode = parNodeIter.nextNode();

    
    if(null != productNode.getProperty("sling:resourceType") && productNode.getProperty("sling:resourceType").getValue().getString().equals("ebazaarresp/components/content/product")){
        if(productNode.hasProperty("prodCode")){
            Value codes = productNode.getProperty("prodCode").getValue();
                       prodCode = codes.getString();
            //String prodC = (String)slingRequest.getAttribute("prodCode");
             Session mySession = resourceResolver.adaptTo(
                     Session.class);
            Node root = mySession.getRootNode();
            
          if(root.hasNode("content")){
              Node contentNode = root.getNode("content");
              if(contentNode.hasNode("dam")){
                Node damNode =  contentNode.getNode("dam");
                if(damNode.hasNode("philips")){
                    Node philipsNode = damNode.getNode("philips");
                    if(philipsNode.hasNode(prodCode)){
                        Node accessoriesNode = philipsNode.getNode(prodCode);
                        NodeIterator imgNodesIter = accessoriesNode.getNodes();
                        while(imgNodesIter.hasNext()){
                            Node imageNode = imgNodesIter.nextNode();
                            String nodeName = imageNode.getName();

                            if(!nodeName.equalsIgnoreCase("jcr:content")){
                              if(imageNode.hasNode("jcr:content")){
                                    Node jcrNode = imageNode.getNode("jcr:content");
                                  if(jcrNode.hasNode("metadata")){
                                      Node metaDataNode = jcrNode.getNode("metadata");
                                     if(metaDataNode.hasProperty("dc:title")){
                                       title = metaDataNode.getProperty("dc:title").getValue().getString();
                                         // out.println("Meta data title >>"+title);
                                     }
                                      if(metaDataNode.hasProperty("dc:description")){
                                        desc = metaDataNode.getProperty("dc:description").getValue().getString();
                                          //out.println("Meta data deasc >>"+desc);
                                     }

                                  }
                                }

                              
                            %>
    <div class="product">

                            <img src="<%=imageNode.getPath() %>" width="100" height="100"/>
    </p>
  <p class="desc"><%=title%></p>
   <p class="rate">RS. 7999</p>
   
                        <% 
                                log.error("descPropertyList"+descPropertyList.size());

                                    %>
                                    <ul class="list_pro">
                        <li><%=desc%></li>
                       
                        </ul>
                               
                                     <div class="ratings">
                       <!-- <img src="assets/ratings.png" width="151" height="24" alt="ratings" />-->
                       <div class="rating_star">
                       <img src="/etc/designs/philips_ver_2/images/rating_star.png"  />
                       <img src="/etc/designs/philips_ver_2/images/rating_star.png"  />
                       <img src="/etc/designs/philips_ver_2/images/rating_star.png"  />
                       <img src="/etc/designs/philips_ver_2/images/rating_star.png"  />
                       <img src="/etc/designs/philips_ver_2/images/rating_star.png"  /></div>
                       
                       <span class="ratingtxt">(43 User ratings)</span>
                         </div>
                              <input type="button" class="review" value="ADD TO CART"/>

                               <% 
                          %></div><%  
                            
                        }//
                        }
                        
                    } //has prod code images
                     else{
            %>
 <p align="left" class="feedswithoutimage"> No Accessories found for this product</p>

 <%  
        }
                }
              }
           }//
    }  


    }//node iterator

        else{%>

     <p align="left" class="feedswithoutimage"> No Product found in this page</p>
        <%}


    }//par
    }//par node 

    }
    }//while for child nodes

    }//jcr content
    
}//While loop
}

%>
</div>
