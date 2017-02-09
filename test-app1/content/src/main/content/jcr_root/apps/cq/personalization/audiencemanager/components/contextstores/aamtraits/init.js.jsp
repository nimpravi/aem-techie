<%@page session="false"%><%--**********************************************************************
  *
  * ADOBE CONFIDENTIAL
  * __________________
  *
  *  Copyright 2012 Adobe Systems Incorporated
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
<%@page import="org.apache.sling.api.resource.Resource,
	com.adobe.cq.aam.client.spi.AudienceManagerConfiguration,
	com.day.cq.wcm.api.WCMMode" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/javascript" %>
<%@ include file="/libs/foundation/global.jsp" %><%!
%>
<%
    AudienceManagerConfiguration amConfig = resource.adaptTo(AudienceManagerConfiguration.class);
    if ( amConfig == null ) {
        log.error("No Audience Manager configuration has been found, please configure a AudienceManager cloud service");
%>
        console.log("Audience Manager has not been configured, please configure a CloudService for audience manager before using.");
<%
    } else {
    	String pagePath = resource.getPath();
		String configPath = amConfig.getResource().getPath();
		String partner = amConfig.getPartner();
		String containerNSID = amConfig.getContainerNSID();
		log.debug("Configured with path {} ",configPath);
		boolean publishedPage = (WCMMode.fromRequest(request) == WCMMode.DISABLED);
		// uncomment to simulate a published page. publishedPage = true;
%>

try {
    if ( ! CQ_Analytics.AAM ) {
        console.log("CQ_Analytics.AAM  libraries are missing, something is wrong with the client context loading mechanism");
    } else {
<%   if ( publishedPage ) { %>
	    	// create an audience manager instance to hold and resolve the audience manager segments.
	    	// at the moment we are not setting destinations but if you need to set destinationNames (see audiencemanager.js)
	    	if ( ! CQ_Analytics.AAM.AudienceManager ) {
		        CQ_Analytics.AAM.AudienceManager = new CQ_Analytics.AAM.AudienceMgr({
		        	partner : "<%= partner %>",
			        containerNSID : "<%= containerNSID %>"
		        });
	        }
<%   } else { %>
	    	// create an audience manager instance to hold and resolve the audience manager segments.
	    	// at the moment we are not setting destinations but if you need to set destinationNames (see audiencemanager.js)
	        CQ_Analytics.AAM.AudienceManager =  CQ_Analytics.AAM.AudienceManager || new CQ_Analytics.AAM.AudienceMgr({
	        	partner : "<%= partner %>",
			    simulationPath : "<%= configPath %>",
		        containerNSID : "<%= containerNSID %>"
	        });
		    CQ_Analytics.AAM.TraitsManager = CQ_Analytics.AAM.TraitsManager || new CQ_Analytics.AAM.TraitsMgr(
		    	CQ_Analytics.AAM.AudienceManager,
		    	{
			        store_name : "aamtraits",
			        configPath : "<%= configPath %>",
			        partner : "<%= partner %>",
			        containerNSID : "<%= containerNSID %>",
			        pagePath : "<%= pagePath %>"
		    	}
		    );
		    // shim the dialog.xml to manage the traits.
		    CQ_Analytics.AAM.LookupDialogController = CQ_Analytics.AAM.LookupDialogController || new CQ_Analytics.AAM.LookupDialogCtl(
		    	CQ_Analytics.AAM.TraitsManager,
		    	{
		    		configPath : "<%= configPath %>"
		    	}
		    );
<%	 } %>
    // uncomment to test setup console.log("Checking Audience Manager setup should say undefined."+CQ_Analytics.AAM.AudienceManager.matches('1234'));
	 }
} catch(e) {
    console.log("Error loading CQ_Analystics.AAM "+e.stack);
}
<% } // if ( amConfig == null ) %>
