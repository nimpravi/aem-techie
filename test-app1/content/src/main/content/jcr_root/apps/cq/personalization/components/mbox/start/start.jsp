<%@page session="false"%><%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="java.util.Iterator,
                  javax.jcr.Node,
                  com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap,
                  com.day.cq.analytics.AnalyticsConfiguration,
                  com.day.cq.analytics.testandtarget.util.MboxHelper,
                  com.day.cq.wcm.webservicesupport.ConfigurationManager,
                  com.day.cq.wcm.webservicesupport.Configuration,
                  org.apache.sling.api.resource.Resource,
                  org.apache.sling.api.resource.ResourceUtil,
                  com.day.cq.wcm.api.WCMMode,
                  com.day.cq.wcm.foundation.Paragraph,
                  com.day.cq.wcm.api.components.Toolbar"%><%               
 
final WCMMode wcmMode = WCMMode.fromRequest(request);
ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);
Configuration configuration = null;
HierarchyNodeInheritanceValueMap mboxProperties = new HierarchyNodeInheritanceValueMap(resource);
String[] services = mboxProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
if (services.length == 0) {
    mboxProperties = new HierarchyNodeInheritanceValueMap(currentPage.getContentResource());
    services = mboxProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
}
if(cfgMgr != null) {
    configuration = cfgMgr.getConfiguration("testandtarget", services);
}
final AnalyticsConfiguration analyticsConfig = resource.adaptTo(AnalyticsConfiguration.class);
final ValueMap resourceConfig = resource.adaptTo(ValueMap.class);
final Boolean isValidConfig = ( (analyticsConfig != null && analyticsConfig.get("cq:ttclientcode") != null) || 
        (configuration != null && configuration.getInherited("clientcode",null) != null) );

String styleOverride = "";
if (editContext != null
        && WCMMode.fromRequest(request) == WCMMode.EDIT
        && resource instanceof Paragraph) {
            editContext.getEditConfig().getToolbar().add(0, new Toolbar.Separator());
            editContext.getEditConfig().getToolbar().add(0, new Toolbar.Label("Start of Mbox " + resourceConfig.get("jcr:title", "")));
            styleOverride = "style=\"visibility:visible;\"";
}

final String mboxId = MboxHelper.getMboxId(resource);

if (WCMMode.fromRequest(request) != WCMMode.DISABLED) {
    %><cq:includeClientLib categories="cq.analytics"/><%
}
if (isValidConfig) {
    String mboxJs = null;
    Node configNode = null;
    if (configuration != null) {
        configNode = configuration.getResource().adaptTo(Node.class);
    } else {
        configNode = analyticsConfig.getResource().adaptTo(Node.class);
    }

    if (configNode.hasNode("./jcr:content/mbox.js")) {
        final Node scriptNode = configNode.getNode("./jcr:content/mbox.js");
        mboxJs = scriptNode.getPath();
%>
        <script type="text/javascript">
            CQClientLibraryManager.write([{"p":"<%= mboxJs %>","c":[]}],false);
        </script>
<%
    } else {
        %><cq:includeClientLib categories="testandtarget" /><%
    }
}
%>
<cq:includeClientLib categories="testandtarget.util" />
<div class="mboxDefault" id="<%= mboxId %>" <%= styleOverride %>>
<div><% 
    componentContext.setDecorate(true);

    if(!isValidConfig && 
        WCMMode.fromRequest(request) == WCMMode.EDIT) { %>
    <h3 class="cq-texthint-placeholder">Test&amp;Target is not configured for this mbox.</h3>
<%
    }
%>
