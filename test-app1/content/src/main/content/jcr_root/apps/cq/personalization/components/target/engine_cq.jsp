<%--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~
  ~ ADOBE CONFIDENTIAL
  ~ __________________
  ~
  ~  Copyright 2013 Adobe Systems Incorporated
  ~  All Rights Reserved.
  ~
  ~ NOTICE:  All information contained herein is, and remains
  ~ the property of Adobe Systems Incorporated and its suppliers,
  ~ if any.  The intellectual and technical concepts contained
  ~ herein are proprietary to Adobe Systems Incorporated and its
  ~ suppliers and are protected by trade secret or copyright law.
  ~ Dissemination of this information or reproduction of this material
  ~ is strictly forbidden unless prior written permission is obtained
  ~ from Adobe Systems Incorporated.
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~--%>

<%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="
       java.io.StringWriter,
       com.day.cq.wcm.api.WCMMode,
       com.day.cq.wcm.core.stats.PageViewStatistics,
       com.day.text.Text,
       java.util.ResourceBundle,
	   com.day.cq.i18n.I18n,
	   com.day.cq.personalization.TargetedContentManager,
	   com.day.cq.personalization.ClientContextUtil,
	   org.apache.sling.commons.json.JSONObject,
	   org.apache.sling.commons.json.JSONArray,
	   com.day.cq.commons.JS" %><%

%><cq:includeClientLib categories="personalization.kernel"/><%

    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);

    final TargetedContentManager targetedContentManager = sling.getService(TargetedContentManager.class);

    final String campaignPath = properties.get("campaignpath", String.class);
    final String strategyPath = properties.get("strategyPath", String.class);
    final String location = properties.get("location", resource.getPath());

    String strategy = "";
    if (strategyPath != null) {
        strategy = Text.getName(strategyPath);
        strategy = strategy.replaceAll(".js", "");
    }

    String campaignClass = "";
    if (campaignPath != null) {
        Page campaignPage = pageManager.getPage(campaignPath);
        if (campaignPage != null) {
            campaignClass = "campaign-" + campaignPage.getName();
        }
    }

    //try to generate a "friendly" id for the div where teaser will be placed
    String targetDivId = ClientContextUtil.getId(resource.getPath());

    final PageViewStatistics pwSvc = sling.getService(PageViewStatistics.class);
    String trackingURLStr = null;
    if (pwSvc!=null && pwSvc.getTrackingURI() != null) {
        trackingURLStr = pwSvc.getTrackingURI().toString();
    }

    JSONObject teaserInfo = targetedContentManager.getTeaserInfo(resourceResolver, campaignPath, location);
    JSONArray allTeasers = teaserInfo.getJSONArray("allTeasers");

    // TODO: clean this section below up, should be handled by TargetedContentManager

    // add full URLs for all the teasers (URLs should not be built client-side)

    // add selectors from the current page for the mobile case, e.g. "smart", "feature" etc.
    String selectors = slingRequest.getRequestPathInfo().getSelectorString();
    selectors = selectors != null ? "." + selectors : "";

    for (int i = 0; i < allTeasers.length(); i++) {
        JSONObject t = (JSONObject) allTeasers.get(i);
        t.put("url", t.get("path") + "/_jcr_content/par" + selectors + ".html");
    }
    // use "default" child node as default teaser and add at the end of the teaser list
    JSONObject defaultTeaser = new JSONObject();
    defaultTeaser.put("path", resource.getPath() + "/default");
    defaultTeaser.put("url", resource.getPath() + ".default" + selectors + ".html");
    defaultTeaser.put("name", "default");
    defaultTeaser.put("title", i18n.get("Default"));
    defaultTeaser.put("campainName", "");
    defaultTeaser.put("thumbnail", resource.getPath() + ".thumb.png");
    //defaultTeaser.put("id", campaignName + "_" + teaser.getName());
    allTeasers.put(defaultTeaser);

    if (allTeasers.length() > 0) {
%>
<script type="text/javascript">
    $CQ(function() {
        CQ_Analytics.Engine.loadTeaser({ targetID: <%= JS.str(targetDivId) %>,<%
                                      %> teasers: <%= allTeasers %>,<%
                                      %> strategy: <%= JS.str(strategy) %>,<%
                                      %> trackingURL: <%= JS.str(trackingURLStr) %>});
    });
</script>

<div id="<%= xssAPI.encodeForHTMLAttr(targetDivId) %>" class="campaign <%= xssAPI.encodeForHTMLAttr(campaignClass) %>"><%
    if (resource.getChild("default") != null) {
        //include a default teaser into a noscript tag in case of no JS (SEO...)
        StringWriter defaultHtml = new StringWriter();
        pageContext.pushBody(defaultHtml);
        %><sling:include replaceSelectors="noscript" path="default"/><%
        pageContext.popBody();
%><noscript><%=defaultHtml%></noscript><%
    } %>
</div>

<%  } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) { %>
<style type="text/css">
    .cq-teaser-placeholder-off {
        display: none;
    }
</style>
<h3 class="cq-texthint-placeholder"><%=i18n.get("No active campaigns target this teaser") %></h3>
<img src="/libs/cq/ui/resources/0.gif" class="cq-teaser-placeholder" alt=""><%
    }
%>
