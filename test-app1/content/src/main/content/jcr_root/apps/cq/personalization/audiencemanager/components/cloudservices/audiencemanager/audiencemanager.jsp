<%@page session="false"%><%@page import="com.day.cq.analytics.AnalyticsConfiguration,
                com.day.cq.wcm.webservicesupport.Configuration,
                com.day.cq.wcm.webservicesupport.ConfigurationManager,
                org.apache.commons.lang.StringEscapeUtils,
                org.apache.sling.api.SlingHttpServletRequest,
                org.apache.sling.api.resource.Resource,
                org.apache.sling.api.resource.ResourceResolver,
                org.apache.sling.api.resource.ValueMap,
                org.apache.sling.settings.SlingSettingsService,
                org.apache.sling.commons.json.JSONArray,
                java.util.Set,
                java.util.HashSet,
                java.util.Iterator,
                java.net.URLEncoder,
                org.slf4j.Logger,
                org.slf4j.LoggerFactory" %>
<%@include file="/libs/foundation/global.jsp" %><%
/*
 * TODO: Document where this component is used ?
 * TODO: Is it used ?
 */
AnalyticsConfiguration analyticsConfig = resource.adaptTo(AnalyticsConfiguration.class);
ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);

Configuration configuration = null;
String[] services = pageProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
if(cfgMgr != null) {
    configuration = cfgMgr.getConfiguration("audiencemanager", services);
}


if(configuration != null && analyticsConfig != null && configuration.get("enabled", false)) {
%>
<%
} //end if configuration
%>
