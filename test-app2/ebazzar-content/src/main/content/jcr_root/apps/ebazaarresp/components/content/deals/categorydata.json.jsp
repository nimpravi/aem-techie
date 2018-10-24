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
%><%@ page import="org.apache.jackrabbit.util.Text" %>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%><%
%><%@include file="/libs/foundation/global.jsp"%><%

    response.setContentType("text/plain");
%>[<%
   Logger logger = LoggerFactory.getLogger("Product");
    try {

logger.error("Inside TRY block ");
        int pageLevel = currentPage.getDepth() - 2;
        String delim = "";


            Iterator<Page> scripts = slingRequest.getResourceResolver().getResource("/content/philips/en/products/household-products").adaptTo(Page.class).listChildren();
 logger.error("oUTside WHILE block ");
            while (scripts.hasNext()) {
                logger.error("Inside WHILE block ");
                Page script = (Page)scripts.next();
 Iterator<Page> scriptPages = script.listChildren();
 logger.error("oUTside WHILE block ");
            while (scriptPages.hasNext()) {

 Page scriptPage = (Page)scriptPages.next();
                String name = scriptPage.getName();
                String path = scriptPage.getPath();
                logger.error("Inside WHILE block "+name);

                 %><%= delim %>{<%
                                    %>"text":"<%= name %>",<%
                                    %>"value":"<%=path %>"<%
                                %>}<%
                                if ("".equals(delim)) delim = ",";
            }
    }
    }
               catch (Exception re) {
 logger.error("Inside catch block ");
                   }

%>]