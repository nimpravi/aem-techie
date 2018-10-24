<%@page session="false"%><%--
  ************************************************************************
  ADOBE CONFIDENTIAL
  ___________________

  Copyright 2012 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
  ************************************************************************
  --%><%!
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="com.day.cq.analytics.sitecatalyst.Framework,
                  com.day.cq.wcm.webservicesupport.ConfigurationManager,
                  com.day.cq.wcm.webservicesupport.Configuration,
                  com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap,
                  org.apache.sling.api.resource.ValueMap,
                  org.apache.sling.api.resource.Resource,
                  org.apache.sling.api.resource.ResourceResolver" %><%!
%><%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %><%
    ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);
    Configuration configuration = null;
    HierarchyNodeInheritanceValueMap resInherited = new HierarchyNodeInheritanceValueMap(resourceResolver.getResource(request.getParameter("path") + "/jcr:content"));
    String[] services = resInherited.getInherited("cq:cloudserviceconfigs", new String[]{});
    ResourceResolver adminResolver = resourceResolver;
    if(cfgMgr != null) {
        configuration = cfgMgr.getConfiguration("testandtarget", services);
        adminResolver = configuration != null ? configuration.getResource().getResourceResolver() : null;
    }
    final Boolean isValidConfig = configuration != null && configuration.getInherited("clientcode", null) != null;  
    Resource fwResource = isValidConfig ? adminResolver.getResource(configuration.getPath() + "/jcr:content") : null;
    Framework framework = isValidConfig && fwResource != null ? fwResource.adaptTo(Framework.class) : null;
    String fwUrl = "#";
    if (framework != null) {
        Page fwPage = adminResolver.getResource(configuration.getPath()).adaptTo(Page.class);
        fwUrl = request.getContextPath() + "/bin/wcmcommand?cmd=open&path=" + fwPage.getPath();
    }
    ValueMap resourceConfig = resource.adaptTo(ValueMap.class); 
    String mboxName =  resourceConfig.get("jcr:title", isValidConfig ? "global_" + configuration.getName() : "");
    String fwTitle = isValidConfig ? "<a href=\"" + fwUrl + "\">" + configuration.getTitle() + "</a>": "none";
%><div class="cq-cc-store">Test&Target Global Mbox: <%= mboxName %><br/>
Active Framework: <%= fwTitle %>
</div><%
%>
