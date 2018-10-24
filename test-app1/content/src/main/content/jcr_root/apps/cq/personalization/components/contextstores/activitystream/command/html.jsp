<%@page session="false"%><%--
  ~ Copyright 1997-2011 Day Management AG
  ~ Barfuesserplatz 6, 4001 Basel, Switzerland
  ~ All Rights Reserved.
  ~
  ~ This software is the confidential and proprietary information of
  ~ Day Management AG, ("Confidential Information"). You shall not
  ~ disclose such Confidential Information and shall use it only in
  ~ accordance with the terms of the license agreement you entered into
  ~ with Day.
  --%><%@ page import="com.adobe.granite.security.user.UserProperties,
                     com.day.cq.activitystreams.api.Activity,
                     com.day.cq.activitystreams.api.ActivityService,
                     com.day.cq.wcm.api.components.IncludeOptions,
                     com.day.cq.wcm.foundation.forms.FormsHelper,
                     java.util.Collection,
                     java.util.List" %><%!
%><%@include file="/libs/foundation/global.jsp"%><%

    ActivityService activityService = sling.getService(ActivityService.class);

    Resource userResource = null;

    List<Resource> resources = FormsHelper.getFormEditResources(slingRequest);
    if (resources != null && resources.size() > 0) {
       //1 - we are in formchooser-mode, get the requested resource
        userResource = resources.get(0);
    }

    if( userResource == null) {
        //2 - get connected user activities
        userResource = resourceResolver.getResource(resourceResolver.adaptTo(UserProperties.class).getNode().getPath());
    }

    Long maxNbActivities = Long.parseLong(request.getParameter("limit") != null ? request.getParameter("limit") : "-1");

    Collection<Activity> activities = java.util.Collections.EMPTY_LIST;
    if( userResource != null) {
        activities = activityService.readActivities(userResource, "my", maxNbActivities);
    }

    %><ul class="cq-cc-activities cq-cc-my-stream"><%
    if( activities.isEmpty()) {
        %><li class="cq-cc-no-activity"><span>No activity</span></li><%
    } else {
        for (Activity activity : activities) {
//            String date = DateFormat.getDateTimeInstance(DateFormat.LONG,
//                DateFormat.SHORT).format(activity.getCreated());
            %><li class="cq-cc-activity">
                <div class="cq-cc-activity-body"><%
                    try {
                        IncludeOptions.getOptions(request, true).forceSameContext(true);
                        %><cq:include path="<%= activity.getPath()%>" resourceType="<%= activity.getResourceType()%>"/><%
                    } catch (Exception e){
                        out.println("Error while rendering activity " + activity.getTitle());
                        log.error("Error while rendering activity {}",activity.getTitle(),e);
                    }
                %></div>
                <%--<div class="cq-cc-activity-date"><%=date%></div>--%>
                <div class="cq-cc-activity-clear"></div>
            </li><%
        }
    }
    %></ul><%
%>
