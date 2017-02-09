<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

--%><%@ page contentType="text/html"
             pageEncoding="utf-8"
             import="com.day.cq.widget.HtmlLibraryManager,
                     com.day.text.Text,
                     java.util.Iterator,
                     com.day.cq.wcm.api.Page,
                     java.io.IOException,
                     com.day.cq.wcm.foundation.ParagraphSystem,
                     com.day.cq.wcm.foundation.Paragraph,
                     com.day.cq.wcm.api.components.IncludeOptions,
                     java.util.ResourceBundle,
					 com.day.cq.i18n.I18n" %>
<%@ page import="com.day.cq.wcm.api.NameConstants" %>
<%!
%><%@include file="/libs/foundation/global.jsp"%><%
	
	final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
	I18n i18n = new I18n(resourceBundle);  

    boolean isRoot = "/etc/segmentation".equals(resource.getPath());
    String segmentPath = Text.getRelativeParent(resource.getPath(), 1);
    String parentPath = Text.getRelativeParent(segmentPath, 1);
    String parentName = Text.getName(parentPath);
    boolean canVisitParent = false;
    try {
        Item item = currentNode.getSession().getItem(parentPath + "/jcr:primaryType");
        if(item != null) {
            canVisitParent = NameConstants.NT_PAGE.equals(((Property) item).getString());
        }
    } catch (Exception e) {
        //do nothing
    }

    String title = properties.get("jcr:title", Text.getName(segmentPath));
    String descr = properties.get("jcr:description", "");
    long boost = properties.get("definition/boost", 0);

%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
<head>
    <title>CQ5 Segment | <%= title %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%
    HtmlLibraryManager htmlMgr = sling.getService(HtmlLibraryManager.class);
    if (htmlMgr != null) {
        htmlMgr.writeIncludes(slingRequest, out, "cq.wcm.edit", "cq.tagging", "cq.personalization", "cq.security");
    }
    String dlgPath = null;
    if (editContext != null && editContext.getComponent() != null) {
        dlgPath = editContext.getComponent().getDialogPath();
    }
    %>
    <script src="/libs/cq/ui/resources/cq-ui.js" type="text/javascript"></script>
    <script type="text/javascript" >
        CQ.WCM.launchSidekick("<%= currentPage.getPath() %>", {
            propsDialog: "<%= dlgPath == null ? "" : dlgPath %>",
            locked: <%= currentPage.isLocked() %>
        });
    </script>
</head>
<body>
    <cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>

    <% if( !isRoot) { %>
        <p class="breadcrumb"><% if (canVisitParent) { %><a target="_top" href="<%= parentPath %>.html"><% } %><%= parentName %><% if (canVisitParent) { %></a><% } %>&nbsp;&gt;&nbsp;<%= Text.getName(segmentPath) %></p>
    <% } %>
    <h1><%= title %><p><%= descr %><%
        if (boost > 0) {
            %>, Boost: <%= boost %><%
        }
    %></p></h1>
    <%

        Iterator<Page> piter = currentPage.listChildren() ;
        while (piter.hasNext()) {
            Page child = piter.next();
            String childTitle = child.getTitle() != null ? child.getTitle() : child.getName();
            String childDescr = child.getDescription() != null ? child.getDescription() : "";
            long childBoost = child.getContentResource().adaptTo(ValueMap.class).get("definition/boost", 0);
            Resource andRes = child.getContentResource("and");
            %>
            <h2 class="no-icon"><a href="<%= child.getPath() %>.html"><%= childTitle %></a></h2>
            <p><%= childDescr %></p><%
            %><ul><%
                %><li>Boost: <b><%= childBoost %></b></li><%
                if (andRes != null) {
                    ParagraphSystem ands = new ParagraphSystem(andRes);
                    %><li>Traits: <%
                    String andDel = "";
                    for (Paragraph and : ands.paragraphs()) {
                        Resource orRes = resourceResolver.getResource(and.getPath());
                        if (orRes != null) {
                            %><%= andDel %><%
                            ParagraphSystem ors = new ParagraphSystem(orRes);
                            String orDel = "";
                            for (Paragraph or : ors.paragraphs()) {
                                IncludeOptions.getOptions(request, true).forceSameContext(true);
                                %><%= orDel %><%
                                %><sling:include resource="<%= or %>" replaceSelectors="content"/><%
                                if ("".equals(orDel)) orDel = " OR ";
                            }
                            if ("".equals(andDel)) andDel = " AND ";
                        }
                    }
                    %></li><%
                }
            %></ul><%
        }
    %>
    <div class="definition-container"><%
        %><cq:include path="definition" resourceType="./definition"/><%
    %></div>

    <h2 class="no-icon"><%=i18n.get("Segment Editor") %></h2>
    <p><%=i18n.get("Use the segment editor to build your segment by combining and editing the available traits.") %></p>
    <div class="edit-box">
        <%--<cq:include path="and" resourceType="./logic/and"/>--%>
        <cq:include path="traits" resourceType="/libs/cq/personalization/components/traits/logic/and"/>
    </div>
    <cq:include path="validitystatus" resourceType="./validitystatus"/>
</body>
</html>
