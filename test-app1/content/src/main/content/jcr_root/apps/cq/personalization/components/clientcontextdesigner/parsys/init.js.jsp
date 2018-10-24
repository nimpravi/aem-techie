<%@page session="false"%><%--**********************************************************************
  *
  * ADOBE CONFIDENTIAL
  * __________________
  *
  *  Copyright 2011 Adobe Systems Incorporated
  *  All Rights Reserved.
  *
  * NOTICE:  All information contained herein is, and remains
  * the property of Adobe Systems Incorporated and its suppliers,
  * if any.  The intellectual and technical concepts contained
  * herein are proprietary to Adobe Systems Incorporated and its
  * suppliers and are protected by trade secret or copyright law.
  * Dissemination of this information or reproduction of this material
  * is strictly forbidden unless prior written permission is obtained
  * from Adobe Systems Incorporated.
  **********************************************************************--%>
<%@page import="com.day.cq.personalization.ClientContextUtil,
                com.day.cq.wcm.api.WCMMode,
                org.apache.sling.api.SlingHttpServletResponse,
                org.apache.sling.api.resource.ResourceResolver,
                org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper,
                org.apache.sling.jcr.resource.JcrResourceResolverFactory,
                org.apache.sling.jcr.api.SlingRepository,
                java.io.PrintWriter,
                java.io.StringWriter,
                java.util.Iterator,
                java.util.List,
                java.util.ArrayList"
        contentType="text/javascript;charset=utf-8" %><%
%><%@include file="/libs/foundation/global.jsp"%><%

    StringWriter buffer = new StringWriter();

    if( resource != null) {
        if( !WCMMode.DISABLED.equals(WCMMode.fromRequest(slingRequest))) {
            WCMMode.PREVIEW.toRequest(slingRequest);
        }
        componentContext.setDefaultDecorationTagName("");

        Iterator<Resource> it = resourceResolver.listChildren(resource);
        while (it.hasNext()) {
            Resource store = it.next();

            //include all .init.js files from the context stores
            try {
                CustomResponseWrapper responseWrapper = new CustomResponseWrapper(slingResponse);
                RequestDispatcher rd = slingRequest.getRequestDispatcher(store.getPath() + ".init.js");
                rd.include(slingRequest, responseWrapper);
                String res = responseWrapper.getString();
                if (res != null && res.length() > 0) {
                    buffer.append(res);
                    responseWrapper.clearWriter();
                }
            } catch (Exception e) {
                //should never happen. Content is corrupted
                log.error("error while including a init file {}",store.getPath(),e);
            }
        }
    }

    %><%=buffer%><%

    // include plugins before std parsys
    List<String> plugins = null;

    //needs an admin session to read the plugins in case of no read access
    SlingRepository repo = sling.getService(SlingRepository.class);
    Session admin = null;

    try {
        admin = repo.loginAdministrative(null);
        ResourceResolver adminResolver = sling.getService(JcrResourceResolverFactory.class).getResourceResolver(admin);
        plugins = ClientContextUtil.getPlugins(adminResolver, new String[]{
                "/libs/cq/personalization/components/clickstreamcloud/plugins",
                "/libs/cq/personalization/components/clientcontext/plugins",
                "/apps/cq/personalization/components/clickstreamcloud/plugins",
                "/apps/cq/personalization/components/clientcontext/plugins"
        });
     } catch (RepositoryException re) {
        log.error("Cannot retrieve plugins",re);
     } finally {
        if (admin != null) {
            admin.logout();
        }
    }

    if( plugins != null) {
        for(String pluginPath: plugins){
            %><cq:include script="<%=pluginPath%>" /><%
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
