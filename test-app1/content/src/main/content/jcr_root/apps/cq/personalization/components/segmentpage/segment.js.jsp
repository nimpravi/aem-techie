<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@ page contentType="application/x-javascript"
             pageEncoding="utf-8"
             import="java.io.PrintWriter,
                     java.io.StringWriter,
                     java.util.Iterator,
                     org.apache.commons.lang3.StringEscapeUtils,
                     org.apache.sling.api.SlingHttpServletResponse,
                     org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper,
                     com.day.cq.wcm.api.WCMMode" %><%
%><%@include file="/libs/foundation/global.jsp"%><%

    WCMMode.DISABLED.toRequest(slingRequest);
    componentContext.setDefaultDecorationTagName("");

    Page segmentPage = pageManager.getContainingPage(resource.getPath());
    if (segmentPage != null) {
        Resource traits = segmentPage.getContentResource("traits");
        StringWriter buffer = new StringWriter();
        if (traits != null) {
            try {
                CustomResponseWrapper responseWrapper = new CustomResponseWrapper(slingResponse);
                RequestDispatcher rd = slingRequest.getRequestDispatcher(traits.getPath() + ".trait.js");
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
                log.error("error while building a segment {}",traits.getPath(),e);
                buffer.append("( true == 'Content is corrupted: invalid trait found ").append(traits.getPath()).append("' )");
            }
        } else {
            buffer.append("( true )");
        }

        int boost = 0;
        ValueMap vm = segmentPage.getProperties("definition");
        if( vm != null) {
            boost = vm.get("boost", 0);
        }
        response.setContentType("application/x-javascript");
        %>CQ_Analytics.SegmentMgr.register("<%=segmentPage.getPath()%>","<%=StringEscapeUtils.escapeEcmaScript(buffer.toString())%>",<%=boost%>);<%

        //include segments registration for the children
        Iterator<Page> iter = segmentPage.listChildren();
        while (iter.hasNext()) {
            Page child = iter.next();
            RequestDispatcher rd = slingRequest.getRequestDispatcher(child.getPath() + ".segment.js");
            rd.include(request, response);
        }
    }


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
