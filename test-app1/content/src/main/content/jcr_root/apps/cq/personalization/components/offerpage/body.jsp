<%@page session="false"%><%@ page import="javax.jcr.Node,
                org.apache.sling.api.resource.Resource,
                org.apache.sling.api.resource.ResourceResolver,
                org.apache.commons.codec.binary.Base64,
                com.day.cq.wcm.api.WCMMode,
                com.day.cq.widget.HtmlLibraryManager,
                com.day.cq.analytics.testandtarget.TestandtargetService,
                com.day.cq.analytics.testandtarget.util.OfferHelper,
                com.day.cq.wcm.webservicesupport.Configuration,
                com.day.cq.wcm.webservicesupport.ConfigurationManager,
                com.day.cq.i18n.I18n,
                org.apache.commons.lang3.StringEscapeUtils" %>
<%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@include file="/libs/foundation/global.jsp"%><%

    I18n i18n = new I18n(slingRequest);
    
    String icnCls = "cq-teaser-header-on";
    if(!currentPage.isValid()) {
        icnCls = "cq-teaser-header-off";
    }

    ResourceResolver resolver = resource.getResourceResolver();
    Node node = resource.adaptTo(Node.class);
    Node campaignNode = OfferHelper.getCampaign(currentPage).adaptTo(Node.class);
    boolean isWidgetOffer = node.hasProperty(OfferHelper.PN_THIRDPARTYID);
    
    Configuration tntConfig = null;
    if(campaignNode.hasProperty("./jcr:content/" + OfferHelper.PN_TESTANDTARGETCONFIG)) {
        String configPath = campaignNode.getProperty("./jcr:content/" + OfferHelper.PN_TESTANDTARGETCONFIG).getString();
        ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);
        if(cfgMgr != null) {
            tntConfig = cfgMgr.getConfiguration(configPath);
        }
    }
    
    String tntConfigPath = tntConfig != null ? tntConfig.getPath() : "";
    String tntFolderId = properties.get("cq:tntFolderId", "");
    String tntLastPublished = properties.get("cq:tntLastPublished", "");
    
    String buttonTitle = i18n.get("Push to Test&Target");
    if (isWidgetOffer) {
        buttonTitle = i18n.get("Remove from Test&Target");
    } else 
    if (!"".equals(tntLastPublished)) {
        buttonTitle = i18n.get("Re-Push to Test&Target");
    }
%>
<body class="cq-ui-body">
<div id="cq-ui-header"><a href="javascript:window.top.location='/welcome';" class="home"></a></div>
<h2 class="cq-ui-h2 cq-teaser-status <%= icnCls %>"><%= StringEscapeUtils.escapeHtml4(currentPage.getTitle()) %></h2>
<p class="cq-ui-p"><%=i18n.get("The content of the components added to the paragraph system below can be included via the reference or teaser component") %></p>
<div class="cq-ui-edit-box">
    <cq:include script="content.jsp" />
</div>

<p class="cq-ui-p"><input id="testandtarget-publish" type="button" value="<%= buttonTitle %>" 
onclick="CQ_Analytics.TestTarget.publishTestTarget('<%= isWidgetOffer? "deleteWidgetOffer" : "saveHTMLOfferContent" %>', '<%= tntConfigPath %>', '<%= tntFolderId %>', '<%= tntLastPublished %>');"/></p>
</body>
