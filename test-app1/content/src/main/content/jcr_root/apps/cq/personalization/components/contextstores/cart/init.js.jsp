<%@page session="false"%><%--
  ~
  ~ ADOBE CONFIDENTIAL
  ~ __________________
  ~
  ~  Copyright 2011 Adobe Systems Incorporated
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
  --%><%
%><%@ page import="java.io.StringWriter,
                   java.util.Map,
                   com.adobe.cq.commerce.api.CommerceService,
                   com.adobe.cq.commerce.api.CommerceSession,
                   com.adobe.cq.commerce.api.promotion.PromotionManager,
                   com.adobe.cq.commerce.common.CommerceHelper,
                   com.day.cq.commons.TidyJSONWriter"
      contentType="text/javascript" pageEncoding="utf-8" %><%!
%><%@include file="/libs/foundation/global.jsp"%><%
%><%
    String updateUrl = null;
    StringWriter cartData = new StringWriter();
    TidyJSONWriter cartWriter = new TidyJSONWriter(cartData);
    StringWriter promotionsMap = null;

    Resource pageResource = resource.getResourceResolver().getResource(slingRequest.getParameter("path"));
    if (pageResource == null) {
        // the path is undefined when editing the ClientContext itself
        pageResource = resource;
    }
    CommerceService commerceService = pageResource.adaptTo(CommerceService.class);
    if (commerceService == null) {
        cartWriter.object().endObject();
    } else {
        updateUrl = resourceResolver.map(request, pageResource.getPath()) + ".commerce.cart.json";

        CommerceSession commerceSession = commerceService.login(slingRequest, slingResponse);
        CommerceHelper.writeCart(cartWriter, commerceSession);

        if (commerceSession.supportsClientsidePromotionResolution()) {
            promotionsMap = new StringWriter();
            TidyJSONWriter promotionsWriter = new TidyJSONWriter(promotionsMap);
            PromotionManager pm = resourceResolver.adaptTo(PromotionManager.class);
            promotionsWriter.array();
            if (pm != null) {
                for (Map.Entry<String, String> promotion : pm.getPromotionsMap(slingRequest).entrySet()) {
                    promotionsWriter.object()
                        .key("segments").value(promotion.getValue())
                        .key("path").value(promotion.getKey())
                        .endObject();
                }
            }
            promotionsWriter.endArray();
        }
    }
%>
    
    if (CQ_Analytics && CQ_Analytics.CartMgr) {
        CQ_Analytics.CartMgr.updateUrl = "<%= xssAPI.encodeForJSString(updateUrl) %>";
        CQ_Analytics.CartMgr.promotionsMap = <%= promotionsMap %>;
        CQ_Analytics.CartMgr.data = <%= cartData %>;
        CQ_Analytics.CartMgr.init();
    }
