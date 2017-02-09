<%--

 ADOBE CONFIDENTIAL
 __________________

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

--%><%@include file="/libs/foundation/global.jsp"%>
<%@ page session="false" import="com.day.cq.commons.Externalizer,
                 org.apache.sling.api.resource.ResourceResolver,
                 com.day.cq.i18n.I18n,
                 com.day.cq.wcm.webservicesupport.ConfigurationManager,
                 org.apache.sling.api.resource.Resource,
                 com.adobe.granite.auth.oauth.ProviderConfigProperties,
                 org.osgi.service.cm.ConfigurationAdmin,
                 org.osgi.service.cm.Configuration"%><%
%><%
    String appId = "";
    I18n i18n = new I18n(slingRequest);

    // Getting attached facebook cloud service config in order to fetch appID
    ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);
    com.day.cq.wcm.webservicesupport.Configuration facebookConfiguration = null;
    String[] services = pageProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
    if(cfgMgr != null) {
        facebookConfiguration = cfgMgr.getConfiguration("facebookconnect", services);
    }

    // Once cloud service config found. getting relevant clientId/appId
    if(facebookConfiguration != null) {
        Resource configResource = facebookConfiguration.getResource();
        Page configPage = configResource.adaptTo(Page.class);
        final String configid = configPage.getProperties().get("oauth.config.id",String.class);
        ConfigurationAdmin ca = sling.getService(org.osgi.service.cm.ConfigurationAdmin.class);
        Configuration[] matchingConfigs = ca.listConfigurations("(&(oauth.config.id="+configid+")(service.factoryPid="+ProviderConfigProperties.FACTORY_PID+"))");
        appId =  getClientId(matchingConfigs);
    }

    // Getting publish link for current page.
    Externalizer externalizer = sling.getService(Externalizer.class);
    ResourceResolver resolver = sling.getService(ResourceResolver.class);
    String currentPageURL = "";
    if(externalizer != null){
        currentPageURL = externalizer.publishLink(resolver, currentPage.getPath()) + ".html";
    }

    String domain = properties.get("domain", currentPageURL);
    String linkTarget = properties.get("linkTarget", "_blank");
    boolean showRecommendations = true;
    boolean showHeader = properties.get("showHeader", false);

    String width = currentStyle.get("width", "300");
    String height = currentStyle.get("height", "500");
    String font = currentStyle.get("font", "");
    String colorscheme = currentStyle.get("color", "light");

%>

<cq:includeClientLib categories="cq.social.plugins.facebook"/>
<div id="fb-root"></div>

<script src="//connect.facebook.net/en_US/all.js"></script>

<script type="text/javascript">
    $CQ(document).ready(function(){
        FB.Event.subscribe('xfbml.render',function(){
            $CQ('.FB_Loader').css('background','url()');
        });
    });
</script>

<% if(!appId.equals("")) { %>
    <div class="fb-activity cq-social-facebook-plugins-div" data-app-id="<%=appId%>" data-site="<%=domain%>" data-width="<%=width%>" data-height="<%=height%>" data-header="<%=showHeader%>" data-linktarget="<%=linkTarget%>" data-font="<%=font%>" data-recommendations="<%=showRecommendations%>"></div>
<% } else { %>
    <h3 class="cq-texthint-placeholder"><%= i18n.get("Please configure facebook cloud service.") %></h3>
<% } %>

<%!
    String getClientId(Configuration[] matchingConfigs){
        Configuration providerConfig;
        String clientId ="";
        if(matchingConfigs != null && matchingConfigs.length == 1){
            providerConfig = matchingConfigs[0];
            clientId = (String)providerConfig.getProperties().get("oauth.client.id");
        }
        return clientId;
    }
%>
