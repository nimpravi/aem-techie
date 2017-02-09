<%--  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Compiles a JSON-formatted list of the available display options to
  draw list items. 
--%>
<%@ page import="com.day.cq.wcm.api.WCMMode,
                   com.day.cq.wcm.api.components.DropTarget,
                   com.day.cq.wcm.foundation.List,
                   java.util.Iterator"%><%
%><%@ page import="org.apache.jackrabbit.util.Text" %>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%><%
%><%@include file="/libs/foundation/global.jsp"%><%

   
%>[<%
   Logger logger = LoggerFactory.getLogger("Product");
    try {
    	//String category = slingRequest.getParameter("category");

logger.error("Inside TRY block productdata json ");
        int pageLevel = currentPage.getDepth() - 2;
        String delim = "";

            NodeIterator scripts = slingRequest.getResourceResolver().getResource("/content/philips/en/products/household-products/cooking/cook").adaptTo(Node.class).getNodes();
            String prodCode = "";
			 String prodTitle = "";
            while (scripts.hasNext()) {
                Node script = scripts.nextNode();
                String name = script.getName();
                if(name.equalsIgnoreCase("jcr:content")){
				NodeIterator scriptNodeIter = script.getNodes();
				while(scriptNodeIter.hasNext()){
				Node scriptNode = scriptNodeIter.nextNode();
                    //null check
				if(null != scriptNode.getProperty("sling:resourceType") && scriptNode.getProperty("sling:resourceType").getValue().getString().equals("foundation/components/parsys")){
				NodeIterator parNodeIter = scriptNode.getNodes();
				while(parNodeIter.hasNext()){

				Node productNode = parNodeIter.nextNode();

				if(null != productNode.getProperty("sling:resourceType") && productNode.getProperty("sling:resourceType").getValue().getString().equals("ebazaarresp/components/content/productdetails")){
				if(productNode.hasProperty("prodCode")&& productNode.hasProperty("prodTitle")){
				 Value codes = productNode.getProperty("prodCode").getValue();
                            prodCode = codes.getString();
							Value title = productNode.getProperty("prodTitle").getValue();
							prodTitle = title.getString();
                            %><%= delim %><%
                            if(!prodCode.isEmpty() && (prodCode != "") && !prodTitle.isEmpty() && (prodTitle != "")){
                                %>{<%
                                    %>"text":"<%= prodTitle %>",<%
                                    %>"value":"<%= prodCode %>"<%
                                %>}<%
                                if ("".equals(delim)) delim = ",";
				
				}
				}
				else{
				break;
				}
				}
				}
			
				
                }
            }
        }
    } }catch (Exception re) {
    	logger.error("exception>> in json "+re);
                   }

%>]