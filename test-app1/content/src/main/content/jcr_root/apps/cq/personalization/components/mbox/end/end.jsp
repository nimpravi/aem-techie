<%@page session="false"%><%--
  Copyright 1997-2010 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Mbox 'end' component

  Draws the end of a Test&Target Mbox

--%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="javax.jcr.Node,
                  javax.jcr.Property,
                  javax.jcr.Value,
                  javax.jcr.PathNotFoundException,
                  javax.jcr.RepositoryException,
                  com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap,
                  com.day.cq.analytics.AnalyticsConfiguration,
                  com.day.cq.analytics.testandtarget.util.MboxHelper,
                  com.day.cq.analytics.sitecatalyst.Framework,
                  com.day.cq.wcm.webservicesupport.ConfigurationManager,
                  com.day.cq.wcm.webservicesupport.Configuration,
                  org.apache.sling.api.resource.Resource,
                  org.apache.sling.api.resource.ResourceResolver,
                  org.apache.sling.api.resource.ValueMap,
                  org.apache.sling.commons.json.JSONArray,
                  org.apache.commons.lang3.StringEscapeUtils,
                  com.day.cq.wcm.api.WCMMode" %>
<%
    final WCMMode wcmMode = WCMMode.fromRequest(request);

    ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);
    Configuration configuration = null;
    final Resource startMbox = MboxHelper.searchStartElement(resource);
    HierarchyNodeInheritanceValueMap mboxProperties = new HierarchyNodeInheritanceValueMap(startMbox);
    String[] services = mboxProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
    Boolean frameworkIsLocal = mboxProperties.get("cq:cloudserviceconfigs", null) != null;
    String mboxType = mboxProperties.get("cq:mboxtype", "d");
    if (services.length == 0) {
        mboxProperties = new HierarchyNodeInheritanceValueMap(currentPage.getContentResource());
        services = mboxProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
    }
    if(cfgMgr != null) {
        configuration = cfgMgr.getConfiguration("testandtarget", services);
    }
    final AnalyticsConfiguration analyticsConfig = resource.adaptTo(AnalyticsConfiguration.class);
    final Boolean isValidConfig = ( (analyticsConfig != null && analyticsConfig.get("cq:ttclientcode") != null) || 
            (configuration != null && configuration.getInherited("clientcode", null) != null) );
  
    final Framework framework = isValidConfig && frameworkIsLocal ? resourceResolver.getResource(configuration.getPath() + "/jcr:content").adaptTo(Framework.class) : null;
    final ValueMap resourceConfig = startMbox.adaptTo(ValueMap.class); 
    final String mboxId = MboxHelper.getMboxId(resource);
    final String mboxName = MboxHelper.getMboxName(resource);
    final String clientCode;
    if (analyticsConfig != null && analyticsConfig.get("cq:ttclientcode") != null) {
        clientCode = analyticsConfig.get("cq:ttclientcode", null);
    } else if (configuration != null) {
        clientCode = configuration.getInherited("clientcode", null);
    } else {
        clientCode = null;
    }

    // draw the edit bar
    if (editContext != null) {
        editContext.includeEpilog(slingRequest, slingResponse, wcmMode);
    }
    
    // turn of decoration and close the decorating DIV
    componentContext.setDecorate(false);
    %> 
    </div>  
</div>
<%
if(isValidConfig && (wcmMode != WCMMode.EDIT)) {
  String[] mappings = resourceConfig.get("cq:mappings", new String[0]);  
  String[] isProfile = resourceConfig.get("cq:isprofile", new String[0]);      
  if ("d".equals(mboxType)) {
    mboxType = wcmMode == WCMMode.DISABLED ? "s" : "a";
  }
  if (framework != null || mappings.length > 0) {
    mboxType = "a";
  }
%>
<script type="text/javascript">
<% if ("a".equals(mboxType)) { %><%
%>mboxDefine("<%= mboxId %>", "<%= mboxName %>"<%
%><% } else { %><%
%>mboxCreate("<%= mboxName %>"<%
%><% } %><%
    //page parameters
    for(String key: resourceConfig.keySet() ) {
        if(key.indexOf(":") == -1) {
            %>,<%
            %>"<%= key %> =<%=resourceConfig.get(key, "") %>"<%
        }
    }
    //static parameters
    String[] staticparams = resourceConfig.get("cq:staticparams", new String[0]);
    Node resNode = currentPage.adaptTo(Node.class);
    for(String arr : staticparams) {
        JSONArray jsonElem = new JSONArray(arr);
        String key = jsonElem.getString(0);
        String value = jsonElem.getString(1);
        boolean isConstant = true;
        if(value.startsWith("./") && resNode != null) {
            try {
                Property prop = resNode.getProperty(value);
                value = getPropertyValue(prop);
            }catch(PathNotFoundException e) {}
        } else if(value.startsWith("$")) {
            isConstant = false;            
            value = value.substring(1);
        }
      %>,<%
      %>"<%= xssAPI.encodeForJSString(key + "=" + (isConstant ? value : ""))%>"<%= isConstant ? "" : "+eval('" + xssAPI.encodeForJSString(value) + "')" %><%
    }
%>);<%
    if ("a".equals(mboxType)) {
%>    
if (!CQ_Analytics.mboxes) { CQ_Analytics.mboxes = new Array(); }
CQ_Analytics.mboxes.push({id: "<%= mboxId %>", name: "<%= mboxName %>",
    mappings: [<%
        boolean comma = false;
        if (framework != null && mappings.length == 0) {
            for (String key: framework.scVars() ) {
                if (comma) {
                    %>,<%
                } else {
                    comma = true;
                }
                %>{param:'<%= xssAPI.encodeForJSString(key) %>',ccKey:'<%= xssAPI.encodeForJSString(framework.getMapping(key)) %>'}<%
            }
        }

        for (int i=0; i < mappings.length; i++) {
            if (comma) {
                %>,<%
            } else {
                comma = true;
            }
            %>{param:'<%= xssAPI.encodeForJSString(mappings[i]) %>',ccKey:'<%= xssAPI.encodeForJSString(mappings[i]) %>'}<%
        }
%>],
    isProfile: [<%
        comma = false;
        if(framework != null && mappings.length == 0) {
            for(String key: framework.scVars() ) {
                if (key.startsWith("tnt_profile.")) {
                    if(comma) {
                        %>,<%
                    } else {
                        comma = true;
                    }
                    %>'<%= xssAPI.encodeForJSString(framework.getMapping(key)) %>'<%
                }
            }
        }
        
        for(int i=0; i < isProfile.length; i++) {
            if(comma) {
                %>,<%
            } else {
                comma = true;
            }
            %>'<%= xssAPI.encodeForJSString(isProfile[i]) %>'<%
        }
%>]
});<%
    }
%>
// hide default mbox content; hiding just the outer div doesn't work, it will be shown again a bit later
var defaultDivs = document.getElementById("<%= mboxId %>").getElementsByTagName("div");
for(var i=0; i<defaultDivs.length; i++) {
    defaultDivs[i].style.visibility="hidden";
}
// show default mbox content if still there
function showDefault<%= mboxName.replace("-", "_") %>() {
    var defaultContent = document.getElementById("<%= mboxId %>");
    if (defaultContent) {
        CQ_Analytics.TestTarget.ignoreNextUpdate('<%= mboxName %>');
        var defaultDivs = defaultContent.getElementsByTagName("div");
        for(var i=0; i<defaultDivs.length; i++) {
            defaultDivs[i].style.visibility="visible";
        }
    }
}
// wait 2s for campaign content to arrive before showing default content
setTimeout("showDefault<%= mboxName.replace("-", "_") %>()", 2000);
</script><%
%><%
} else {
// WCM.EDITMODE
%><%
%><script type="text/javascript">
<%-- force sidekick to reload in preview mode --%>
CQ.Ext.onReady(function(){
    var top = CQ.WCM.getTopWindow();
    if (top.CQ.WCM.isSidekickReady()) {
        top.CQ.WCM.getSidekick().previewReload = true;
    } else {
        top.CQ.WCM.on("sidekickready", function(sidekick){
            sidekick.previewReload = true;
        });
    }
});
</script><%
%><%
}
%>
<%!
/**
 * Returns a {@link Property} value and handles multi-value properties by concatenating
 * the values separated by comma.
 *
 * @param property The property to get the value from
 * @return A string representation of the value of this property
 */
protected String getPropertyValue(Property property) throws RepositoryException {
    if(!property.isMultiple()) {
        return property.getString();
    }else{
        String v = "";
        Value[] values = property.getValues();
        for(int i=0; i<values.length; i++) {
            if(i>0) {
                v += ",";
            }
            v += values[i].getString();
        }
        return v;
    }
}
%>
