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
                   com.day.cq.wcm.api.Template,
                   java.util.Iterator, com.day.cq.wcm.api.WCMMode" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/><%

    String location = null;

    // ideal case: page image
    if (pageProperties.get("image/fileReference",
            pageProperties.get("image/file/jcr:content/jcr:mimeType", String.class)) != null) {
        location = currentPage.getPath() + ".img.png";
    }

    if (location == null) {
        // fallback: components that provide an image

        // find first component in par
        Resource par = resourceResolver.getResource(currentPage.getPath() + "/jcr:content/par");
        Resource firstComponent = null;
        for (Iterator<Resource> kids = par.listChildren(); kids.hasNext();) {
            firstComponent = kids.next();
            break;
        }

        if (ResourceUtil.isA(firstComponent, "foundation/components/textimage")) {
            location = firstComponent.getPath() + "/image.img.png";

        } else if (ResourceUtil.isA(firstComponent, "foundation/components/parbase")) {
            location = firstComponent.getPath() + ".img.png";
        }
    }

    if (location == null) {
        // fallback: template image
        Template template = currentPage.getTemplate();
        if (template != null) {
            location = template.getThumbnailPath();
        }
    }

    if (location == null) {
        // fallback: default icon
        location = "/libs/cq/ui/widgets/themes/default/icons/48x48/page.png";
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