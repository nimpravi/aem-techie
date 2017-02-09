<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@ page import="com.day.cq.wcm.api.WCMMode,
                     org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper,
                     java.io.StringWriter,
                     java.io.PrintWriter,
                     org.apache.sling.api.SlingHttpServletResponse,
                     java.util.Set,
                     java.util.HashSet" %><%
%><%@include file="/libs/foundation/global.jsp"%><%
    String segmentPath = properties.get("segmentPath", "");
    StringBuffer buffer = new StringBuffer();
    if(!"".equals(segmentPath)) {
        //infinite loop check
        Set<String> segmentsTrace = (Set) request.getAttribute("segmentsTrace");
        segmentsTrace = (segmentsTrace != null ? segmentsTrace : new HashSet<String>());
        Page segmentPage = pageManager.getContainingPage(resource);
        if( segmentPage != null) {
            segmentsTrace.add(segmentPage.getPath());
        }
        request.setAttribute("segmentsTrace",segmentsTrace);

        if( !segmentsTrace.contains(segmentPath)) {
            Resource r = resourceResolver.resolve(segmentPath + "/jcr:content/traits");
            if( r != null) {
                WCMMode.DISABLED.toRequest(slingRequest);
                componentContext.setDefaultDecorationTagName("");

                try {
                    CustomResponseWrapper responseWrapper = new CustomResponseWrapper(slingResponse);
                    RequestDispatcher rd = slingRequest.getRequestDispatcher(r.getPath() + ".trait.js");
                    rd.include(request, responseWrapper);
                    String res = responseWrapper.getString();
                    res = res.replaceAll("\\n", "");
                    res = res.replaceAll("\\r", "");
                    if (res != null && res.length() > 0) {
                        buffer.append(res);
                        responseWrapper.clearWriter();
                    } else {
                        buffer.append("( true )");
                    }
                } catch (Exception e) {
                    //should never happen. Content is corrupted
                    buffer.append("( eval(\"throw new Error('Invalid trait found ").append(segmentPath).append("') \"))");
                }
            } else {
                buffer.append("( true )");
            }
        } else {
            //infinite loop
            buffer.append("( eval(\"throw new Error('Infinite loop detected in referenced segment: ").append(segmentPath).append("') \"))");
        }
    } else {
        buffer.append("( true )");
    }

    %><%=buffer.toString()%><%
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
