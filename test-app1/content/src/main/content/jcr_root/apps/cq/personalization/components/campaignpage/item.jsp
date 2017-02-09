<%@page session="false"%><%--
  ************************************************************************
  ADOBE CONFIDENTIAL
  ___________________

  Copyright 2011 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
  ************************************************************************

--%><%@page import="com.day.cq.wcm.core.stats.PageViewStatistics,
                    com.day.cq.i18n.I18n,
                    com.day.cq.wcm.api.WCMException,
                    org.apache.sling.commons.osgi.OsgiUtil,
                    org.apache.sling.api.resource.ValueMap,
                    com.day.cq.tagging.Tag,
                    com.day.cq.wcm.api.Template,
                    org.apache.sling.api.resource.ResourceUtil,
                    com.day.cq.wcm.foundation.Image,
                    com.day.cq.personalization.TeaserUtils"%>
<%
    String childTitle = child.getTitle();

    ValueMap content = child.getProperties();
    if (content == null) {
        content = ValueMap.EMPTY;
    }
    String childDescription = content.get("jcr:description", String.class);

    String imagePath = TeaserUtils.getImage(child);

    String childTypeLabel = i18n.get("Item");
    Template template = child.getTemplate();
    if (template != null) {
        childTypeLabel = template.getTitle();
    }

    // TODO: move the hitCounter to a Java class and generalize it to get whatever statistic
    //        is appropriate for the given resource type...

    long monthlyHits = 0;
    try {
        final PageViewStatistics pwSvc = sling.getService(PageViewStatistics.class);
        Object[] hits = pwSvc.report(child);
        if (hits != null && hits.length > 2) {
            monthlyHits = (Long) hits[2];
        }
    } catch (WCMException ex) {
        monthlyHits = -1;
    }

%><h2 class="no-icon">
    <a class="cq-teaser-header" href="<%= xssAPI.getValidHref(child.getPath()) %>.html"><%= xssAPI.encodeForHTML(childTitle) %></a>
</h2>
<img class="cq-teaser-img" src="<%= imagePath %>" alt="<%= xssAPI.encodeForHTMLAttr(childTitle) %>" border="0">
<ul class="cq-teaser-data">
    <%
        if (childDescription != null) {
    %>
    <li><%= xssAPI.encodeForHTML(childDescription) %></li>
    <%
        }
        if (child.getOnTime() != null || child.getOffTime() != null || isCampaign(child)) {
    %>
    <li>
        <div class="li-bullet">
            <%= child.isValid() ?
                    i18n.get("{0} is <strong>active</strong>:", null, childTypeLabel) :
                    i18n.get("{0} is <strong>inactive</strong>:", null, childTypeLabel) %>
            <%= i18n.get("on/off times are") %> <strong><%= formatOnOffTimes(i18n, dateFmt, child.getOnTime(), child.getOffTime()) %></strong>
        </div>
    </li>
    <%
        }
        String[] childSegments = OsgiUtil.toStringArray(content.get("cq:segments"));
        if (childSegments != null || isExperience(child)) {
            if (childSegments == null) {
    %><li><%= i18n.get("{0} is <strong>not</strong> targeted.", null, childTypeLabel) %></li><%
            } else {
    %><li><%= i18n.get("{0} is targeted at:", null, childTypeLabel) %>&nbsp;<%
                String separator = " ";
                for (String s : childSegments) {
                    Resource r = resourceResolver.getResource(s);
                    if (r != null) {
                        String segmentTitle = ResourceUtil.getValueMap(r.getChild("jcr:content")).get("jcr:title", r.getName());
        %><%= separator %><strong><a href="<%= xssAPI.getValidHref(s) %>.html"><%= xssAPI.encodeForHTML(segmentTitle) %></a></strong><%
                        separator = ", ";
                    }
                }
    %></li><%
            }
        }
%>
    <li><%= i18n.get("{0} has been viewed <strong>{1}</strong> times last month", null, childTypeLabel, monthlyHits) %></li>
</ul><%!
    static String CAMPAIGN_TYPE = "cq/personalization/components/campaignpage";
    static String EXPERIENCE_TYPE = "cq/personalization/components/experiencepage";

    public static boolean isCampaign(Page page) {
        return ResourceUtil.isA(page.getContentResource(), CAMPAIGN_TYPE);
    }

    public static boolean isExperience(Page page) {
        return ResourceUtil.isA(page.getContentResource(), EXPERIENCE_TYPE);
    }

%>
