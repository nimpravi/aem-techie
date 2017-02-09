<%@page session="false"%><%--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
%><%@page import="org.apache.commons.lang.StringUtils,
                  com.day.cq.wcm.webservicesupport.ConfigurationManager" %><%!

private static final String PN_ENGINE = "cq:targetEngine";
private static final String DEFAULT_ENGINE = "cq";
private static final String TEST_AND_TARGET_ENGINE = "tnt";

%><%

// look up property on current component node...
// (note that the value can be the empty string, meaning "inherit")
String engine = properties.get(PN_ENGINE, String.class);

if (StringUtils.isEmpty(engine)) {
    // ... or from inherited page property...
    engine = pageProperties.getInherited(PN_ENGINE, String.class);
}
if (StringUtils.isEmpty(engine)) {
    // ... or if not explicitly set, use tnt engine if t&t cloud service config is present
    final ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);
    String[] services = pageProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
    if (cfgMgr != null && cfgMgr.getConfiguration("testandtarget", services) != null) {
        engine = TEST_AND_TARGET_ENGINE;
    }
}

if (StringUtils.isEmpty(engine)) {
    // ... otherwise default to "cq"
    engine = DEFAULT_ENGINE;
}

pageContext.setAttribute("engine", engine);

%><cq:include script="engine_${engine}.jsp" />
