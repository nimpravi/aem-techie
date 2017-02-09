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
%><%@page import="java.util.*,
                  com.day.cq.wcm.api.WCMMode,
				  com.day.cq.analytics.testandtarget.util.MboxHelper,
				  com.day.cq.wcm.webservicesupport.ConfigurationManager,
				  org.apache.sling.commons.json.*" %><%

    // 1. Extract globally useful information

    WCMMode wcmMode = WCMMode.fromRequest(request);

    String mboxId = MboxHelper.qualifyMboxNameOrId(MboxHelper.getMboxId(resource), wcmMode);
    String mboxName = MboxHelper.qualifyMboxNameOrId(MboxHelper.getMboxName(resource), wcmMode);
    // we can't use mboxCreate multiple times so when authoring, which can involve component refreshes
    // always use accurate rendering so we can refresh the mbox multiple times
    boolean forceAccurateRendering = WCMMode.fromRequest(request) != WCMMode.DISABLED;
%>

<%-- 2. include default content --%>
<div class="mboxDefault" id="<%= mboxId %>">
    <% 
        // CQ5-32864: avoid decorations for the targetparsys component since it leads to DOM leaks when simulating
        WCMMode.DISABLED.toRequest(request);
        if (resource.getChild("default") != null) { 
    %>
    <sling:include path="default" />
    <% 
        }
        wcmMode.toRequest(request);
    %>
</div>

<%-- 3. Prepare mbox call
	 For accurate rendering mboxes use define + push so that the ClientContext parameters are taken into account
	 For non-accurate rendering mboxes use plain create
 --%>
<script type="text/javascript">
    var mboxId = '<%= mboxId %>';
    var mboxName = '<%= mboxName %>';
    var mboxLocation = '<%= resource.getPath() %>';
    var wcmMode = '<%= wcmMode %>';

    if ( typeof mboxDefine == 'function' && wcmMode !== 'ANALYTICS' ) {
        var callParameters = [ mboxId ];
        var callFunction;
        var replaced = false;

        <%
            if ( forceAccurateRendering || MboxHelper.isAccurateRendering(resource ) ) { %>
        callParameters.push(mboxName);
        callFunction = mboxDefine;

        <%
            Map<String,String> mappedCcParameterNames = MboxHelper.getMappedClientContextParameterNames(resource, pageProperties,
                sling.getService(ConfigurationManager.class));
            StringBuilder profileParameterNames = new StringBuilder("'");
            JSONArray mappings = new JSONArray();

            for ( Map.Entry<String,String> ccParameterEntry : mappedCcParameterNames.entrySet() ) {
                profileParameterNames.append(xssAPI.encodeForJSString(ccParameterEntry.getValue())).append(",");
                mappings.put(new JSONObject().put("ccKey", ccParameterEntry.getKey()).put("param", ccParameterEntry.getValue()));
            }

            profileParameterNames.append("'");
        %>

        replaced = CQ_Analytics.TestTarget.addMbox({id: mboxName, name: mboxName,
            mappings: <%= mappings.toString() %> , isProfile: <%= profileParameterNames.toString() %> });
        // Make sure the default div does not get cached, since it might no longer be connected to the DOM
        mboxFactories.get('default').getMboxes().get(mboxName).each(function(mbox) {
            mbox.clearDefaultDiv();
        });

        <% } else { %>
        callFunction = mboxCreate;
        <% } %>

        <%
            Map<String, String> staticParameterMap = MboxHelper.getStaticParameters(resource);
            JSONObject staticParameters = new JSONObject();
            for ( Map.Entry<String, String> entry : staticParameterMap.entrySet() )
                staticParameters.put(entry.getKey(), entry.getValue());
        %>
        var staticParameters = <%= staticParameters.toString() %>;

        for ( var key in staticParameters )
            callParameters.push(key  + '=' + staticParameters[key]);
        <%--
            4. Trigger mbox calls
        --%>
        if ( !replaced ) { // do not define the same mbox twice, this leads to additional JS calls
            callFunction.apply(undefined, callParameters);
        }


        <%--
            5. Handle mboxDefine case specially to account for page flicker and authoring
        --%>
        if ( callFunction == mboxDefine ) {

            // if the campaign store is available at this time we are most likely simulating
            // so trigger an update if the (auto) campaign is selected
            var campaignStore = ClientContext.get("campaign");
            if ( campaignStore && !campaignStore.isCampaignSelected() ) {
                CQ_Analytics.TestTarget.triggerUpdate();
            }

            // wait for 2 seconds for the T&T answer to come in to prevent page flicker issues
            // also see the overlay of mbox.protoype.setOffer which completes this
            CQ_Analytics.TestTarget.ignoreLateMboxArrival(mboxId, mboxName, 2000);
        }
    }

    <%--
        6. Analytics mode hooks
    --%>
    <% if ( wcmMode == WCMMode.ANALYTICS ) { %>
    var mboxSelector = '#' + mboxName + '.mboxDefault';  // the selector for this mbox. mboxName is unique in DOM
    $CQ(mboxSelector).css('visibility', 'visible'); // override the mbox handling of hiding the default content
    var analyzables = $CQ(mboxSelector).closest('.cq-analyzable'); // find the closest analyzable parent wrapping the default content
    $CQ(analyzables).each(function(index, analyzable) {
        $CQ(analyzable).append(
                $CQ('<div>').attr('class', 'cq-analyzable-launch-button').attr('title', CQ.I18n.get('Show analytics data')).
                        click(function() {
                            if ( !ClientContext.get("campaign") ) {
                                return;
                            }
                            var location = '<%=resource.getPath()%>'; //mboxLocation cannot be used in this closure
                            var overlay = new CQ.personalization.TargetAnalyticsOverlay(analyzable, location);
                            overlay.toggle(ClientContext.get("campaign/path"));
                            overlay.installCampaignStoreListener();
                        }
                )
        );
    });
    <% } %>
</script>
