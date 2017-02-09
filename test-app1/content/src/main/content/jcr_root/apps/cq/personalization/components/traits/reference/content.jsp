<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="
       com.day.cq.wcm.api.WCMMode,
       com.day.cq.wcm.api.Template,
       java.util.ResourceBundle,
       com.day.cq.i18n.I18n" %><%!
%><%
    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);

    WCMMode mode = WCMMode.fromRequest(request);
    String segmentPath = properties.get("segmentPath", "");
    if(!"".equals(segmentPath)) {
        Resource r = resourceResolver.getResource(segmentPath);
        Page segmentPage = (r != null ? r.adaptTo(Page.class) : null);
        if(segmentPage != null) {
            Template t = segmentPage.getTemplate();
            if("/libs/cq/personalization/templates/segment".equals(t.getPath())) {
                %><%= i18n.get("Referenced segment:") %> <a href="<%=xssAPI.getValidHref(r.getPath()+".html")%>"><%=xssAPI.encodeForHTML(segmentPage.getTitle())%></a><%
            } else {
                if (mode == WCMMode.EDIT) {
                    %><%= i18n.get("Selected path is not a valid segment:") %> <%=xssAPI.encodeForHTML(r.getPath())%><%
                }
            }
        } else {
            if (mode == WCMMode.EDIT) {
                %><%= i18n.get("Invalid reference:") %> <%=xssAPI.encodeForHTML(segmentPath)%><%
            }
        }
    } else if (mode == WCMMode.EDIT) {
        %><img src="/libs/cq/ui/resources/0.gif" class="cq-teaser-placeholder" alt=""><%
    }
%>
