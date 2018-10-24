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
  --%>

<%@page import="com.day.cq.personalization.TargetedContentManager"%>
<%@include file="/libs/foundation/global.jsp"%>
<%@page contentType="text/javascript" import="javax.jcr.query.*,java.util.*,
	com.day.cq.personalization.TargetedContentManager,
	com.day.cq.analytics.testandtarget.util.OfferHelper,
	com.day.cq.analytics.testandtarget.util.Constants,
	org.apache.sling.commons.json.*,
	org.apache.sling.api.resource.ValueMap,
	com.day.cq.commons.Filter" %>
if ( CQ_Analytics && CQ_Analytics.CampaignMgr ) {

<%
	JSONArray campaigns = new JSONArray();

	TargetedContentManager finder = sling.getService(TargetedContentManager.class);
	if ( finder != null ) {
		for ( Page campaignPage : finder.getCampaigns(resourceResolver) ) {	
			
			Node campaignNode = campaignPage.getContentResource().adaptTo(Node.class);
			
			Iterator<Page> experienceIterator = campaignPage.listChildren(new Filter<Page>() {
				public boolean includes(Page element) {
					return element.hasContent() && 
						element.getContentResource().getResourceType().equals(OfferHelper.RT_EXPERIENCE);
				}
			});
			
			JSONArray experiences = new JSONArray(); 

			while ( experienceIterator.hasNext() ) {
				
				Page experiencePage = experienceIterator.next();
				
				JSONObject experience = new JSONObject();
				experience.put("path", experiencePage.getPath());
				experience.put("id", OfferHelper.getCampaignName(experiencePage.getPath()));
				experience.put("title", experiencePage.getTitle());
				
				experiences.put(experience);
			}
			
			JSONObject campaign = new JSONObject();
			campaign.put("path", campaignPage.getPath());
			campaign.put("title", campaignPage.getTitle());
			campaign.put("id", OfferHelper.getThirdPartyCampaignId(campaignNode));
			campaign.put("experiences", experiences);
			campaign.put("cloudConfiguration", campaignPage.getContentResource().adaptTo(ValueMap.class).get(Constants.PN_CQ_CLOUD_SERVICE_CONFIGS, ""));

			campaigns.put(campaign);
		}
	}
%>
	var campaigns = <%= campaigns %>;
	CQ_Analytics.CampaignMgr.addInitProperty('campaigns', campaigns);
	CQ_Analytics.CampaignMgr.init();
}
