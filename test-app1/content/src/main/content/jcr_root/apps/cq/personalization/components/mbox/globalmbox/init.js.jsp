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
<%@ page contentType="text/javascript" %><%
%><%@ include file="/libs/foundation/global.jsp" %><%
%><%@page import="com.day.cq.analytics.sitecatalyst.Framework,
                  com.day.cq.wcm.webservicesupport.ConfigurationManager,
                  com.day.cq.wcm.webservicesupport.Configuration,
                  com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap,
                  org.apache.commons.lang3.StringEscapeUtils,
                  org.apache.sling.api.resource.ValueMap,
                  org.apache.sling.commons.json.JSONArray,
                  org.apache.sling.api.resource.Resource,
                  org.apache.sling.api.resource.ResourceResolver" %><%
    ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);
    Configuration configuration = null;
    HierarchyNodeInheritanceValueMap resInherited = new HierarchyNodeInheritanceValueMap(resourceResolver.getResource(request.getParameter("path") + "/jcr:content"));
    String[] services = resInherited.getInherited("cq:cloudserviceconfigs", new String[]{});
    ResourceResolver adminResolver = resourceResolver;
    if (cfgMgr != null) {
        configuration = cfgMgr.getConfiguration("testandtarget", services);
        adminResolver = configuration != null ? configuration.getResource().getResourceResolver() : null;
    }
    if (configuration != null) {
        ValueMap resourceConfig = resource.adaptTo(ValueMap.class); 
        Resource fwResource = adminResolver.getResource(configuration.getPath() + "/jcr:content");
        Framework framework = fwResource.adaptTo(Framework.class);
        String mboxName = resourceConfig.get("jcr:title", "global_" + configuration.getName());
        if (framework != null && framework.scVars().size() > 0) {
%>
    mboxDefine("<%= mboxName %>", "<%= mboxName %>"<%
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
            if (!key.startsWith("profile.")) {
                key = "profile." + key;
            }
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
    %>);
    if (!CQ_Analytics.mboxes) { CQ_Analytics.mboxes = new Array(); }
    CQ_Analytics.mboxes.splice(0, 0, {id: "<%= mboxName %>", name: "<%= mboxName %>",
        mappings: [<%
            boolean comma = false;
            if(framework != null) {
                for(String key: framework.scVars() ) {
                    if(comma) {
                        %>,<%
                    } else {
                        comma = true;
                    }
                    %>{param:'<%= xssAPI.encodeForJSString(key) %>',ccKey:'<%= xssAPI.encodeForJSString(framework.getMapping(key)) %>'}<%
                }
            }
    %>],
        isProfile: [<%
            comma = false;
            if (framework != null) {
                for(String key: framework.scVars() ) {
                    if(comma) {
                        %>,<%
                    } else {
                        comma = true;
                    }
                    %>'<%= xssAPI.encodeForJSString(key) %>'<%
                }
            }
    %>]
    });
<%
        }
    }
%><%!
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
