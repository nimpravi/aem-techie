<%--
  Copyright 1997-2008 Day Management AG
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

--%><%
%><%@ page import="com.day.cq.wcm.api.WCMMode,
                   com.day.cq.wcm.api.components.DropTarget,
                   com.day.cq.wcm.foundation.List,
                   java.util.Iterator"%><%
%><%@ page import="org.apache.jackrabbit.util.Text" %><%
%><%@include file="/libs/foundation/global.jsp"%><%

    response.setContentType("text/plain");
%>[<%

    try {
        String filterNodes = slingRequest.getParameter("nodeName");
        int pageLevel = currentPage.getDepth() - 2;
        String delim = "";
        for(int l=0;l<pageLevel;l++){
            NodeIterator scripts = slingRequest.getResourceResolver().getResource(currentPage.getParent(l).getPath()).adaptTo(Node.class).getNodes();
            String value = "";
            while (scripts.hasNext()) {
                Node script = scripts.nextNode();
                String name = script.getName();
                if(name.equalsIgnoreCase("jcr:content")){
                    if(script.hasProperty(filterNodes)){
                        if(script.getProperty(filterNodes).isMultiple()){
                            int n = script.getProperty(filterNodes).getValues().length;
                            Value[] brands = script.getProperty(filterNodes).getValues();
                            for(int i=0;i<brands.length;i++){
                                value = brands[i].getString();
                                %><%= delim %><%
                                if(!value.isEmpty() && (value != "")){
                                    %>{<%
                                        %>"text":"<%= value %>",<%
                                        %>"value":"<%= value %>"<%
                                    %>}<%
                                    if ("".equals(delim)) delim = ",";
                                }
                            }
                        }else{
                            Value brands = script.getProperty(filterNodes).getValue();
                            value = brands.getString();
                            %><%= delim %><%
                            if(!value.isEmpty() && (value != "")){
                                %>{<%
                                    %>"text":"<%= value %>",<%
                                    %>"value":"<%= value %>"<%
                                %>}<%
                                if ("".equals(delim)) delim = ",";
                            }
                        }
                    }else{
                        break;
                    }
                }
            }
        }
    } catch (Exception re) {}

%>]