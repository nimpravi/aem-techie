
<%--
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~
  ~ ADOBE CONFIDENTIAL
  ~ __________________
  ~
  ~  Copyright 2012 Adobe Systems Incorporated
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
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--%><%@page session="false" %><%
%><%@ page import="org.apache.sling.api.resource.Resource,
                   org.apache.sling.api.resource.ResourceUtil,
                   org.apache.sling.api.resource.ResourceResolver,
                   java.io.IOException,
                   com.day.cq.wcm.api.WCMMode" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/><%

    String location;
    Resource wrapped = resource.getChild("default");

    if (ResourceUtil.isA(wrapped, "foundation/components/textimage")) {
        location = wrapped.getPath() + "/image.img.png";
    } else {
        location = "/libs/cq/ui/widgets/themes/default/icons/240x180/page.png";
    }

    // don't cache images on authoring instances
    // Cache-Control: no-cache allows caching (e.g. in the browser cache) but
    // will force revalidation using If-Modified-Since or If-None-Match every time,
    // avoiding aggressive browser caching
    if (!WCMMode.DISABLED.equals(WCMMode.fromRequest(request))) {
        response.setHeader("Cache-Control", "no-cache");
    }

    // make sure to send no Content-Type header for the redirects
    response.setContentType(null);

    String redirectPath = resourceResolver.map(request, location);
    // keep cache killers on the headers as browser like to cache redirects aggressively
    String ck = request.getParameter("cq_ck");
    ck = (ck == null) ? request.getParameter("ck") : ck;
    if (ck != null) {
        redirectPath = redirectPath + "?cq_ck=" + ck;
    }
    response.sendRedirect(redirectPath);
%>