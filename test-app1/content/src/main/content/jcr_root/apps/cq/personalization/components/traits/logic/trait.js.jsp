<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@ page import="java.io.StringWriter,
                 org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper,
                 java.io.PrintWriter,
                 org.apache.sling.api.SlingHttpServletResponse,
                 com.day.cq.wcm.api.WCMMode" %><%!
 %><%@include file="/libs/foundation/global.jsp"%><%
    WCMMode.DISABLED.toRequest(slingRequest);
    componentContext.setDefaultDecorationTagName("");
    
    StringBuffer globalBuffer = new StringBuffer();
    try {
        if( currentNode.hasNode("orpar") || currentNode.hasNode("andpar")) {
            Node par = currentNode.hasNode("andpar") ? currentNode.getNode("andpar") : currentNode.getNode("orpar");
            NodeIterator ni = par.getNodes();

            final String connector = currentNode.hasNode("andpar") ? " && " : " || ";

            CustomResponseWrapper responseWrapper = new CustomResponseWrapper(slingResponse);
            boolean first = true;
            StringWriter buffer = new StringWriter();
            while(ni.hasNext()) {
                Node trait = ni.nextNode();
                try {
                    RequestDispatcher rd = slingRequest.getRequestDispatcher(trait.getPath() + ".trait.js");
                    rd.include(request,responseWrapper);
                    String res = responseWrapper.getString();
                    res = res.replaceAll("\\n","");
                    res = res.replaceAll("\\r","");
                    if(res != null && res.length() > 0) {
                        if( !first ) {
                            buffer.append(connector);
                        }
                        buffer.append(" ( ").append(res).append(" ) ");
                        responseWrapper.clearWriter();
                        first = false;
                    }
                } catch (Exception e) {
                    //should never happen. Content is corrupted
                    buffer.append("( eval(\"throw new Error('Invalid trait found").append(trait.getPath()).append("') \"))");
                }
            }
            if( first) {
                globalBuffer.append("( true )");
            } else {
                globalBuffer.append("(").append(buffer.toString()).append(")");
            }

        } else {
            globalBuffer.append("( true )");
        }
    } catch (Exception e) {
        //should never happen. Content is corrupted
        globalBuffer.append("( eval(\"throw new Error('Invalid trait found') \"))");
    }
    out.println(globalBuffer);
%><%!
    class CustomResponseWrapper extends SlingHttpServletResponseWrapper {
        private StringWriter string = new StringWriter();
        private PrintWriter writer = new PrintWriter(string);
        public CustomResponseWrapper(SlingHttpServletResponse slingHttpServletResponse) {
            super(slingHttpServletResponse);
        }

        public PrintWriter getWriter() {
            return writer;
        }

        public String getString() {
            return string.toString();
        }

        public void clearWriter() {
            writer.close();
            string = new StringWriter();
            writer = new PrintWriter(string);
        }
    }
%>
