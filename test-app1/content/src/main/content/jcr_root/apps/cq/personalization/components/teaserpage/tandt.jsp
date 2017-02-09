<%@page session="false"%><%--
  ************************************************************************
  ADOBE CONFIDENTIAL
  ___________________

  Copyright 2011 Adobe Systems Incorporated
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

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@ page import="com.day.cq.analytics.testandtarget.util.OfferHelper" %>
<cq:include script="content.jsp"/>
<script type="text/javascript">
if ( ClientContext && ClientContext.get("campaign") ) {
    var campaignStore = ClientContext.get("campaign");
    campaignStore.setSuppressEvents(true);
    campaignStore.setProperties({
        "campaign/name" : "<%= currentPage.getParent(2).getTitle() %>",
        "campaign/path": "<%= currentPage.getParent(2).getPath() %>",
        "campaign/id" : "<%= OfferHelper.getThirdPartyCampaignId(currentPage.getParent(2).getContentResource().adaptTo(Node.class)) %>",
        "campaign/recipe/name":  "<%= currentPage.getParent().getTitle() %>",
        "campaign/recipe/path": "<%= currentPage.getParent().getPath() %>",
        "campaign/recipe/id": "<%= OfferHelper.getOfferName(currentPage.getParent().getPath()) %>"
    });
    campaignStore.setSuppressEvents(false);
}
</script>
