<%@page session="false"%><%--
  Copyright 1997-2011 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.


@deprecated. Use json load.json instead. XSS non property supported.

--%><%@ page import="com.adobe.granite.security.user.UserProperties,
                    com.adobe.granite.security.user.UserPropertiesManager,
                    com.adobe.cq.social.commons.CollabUtil,
                    com.day.cq.commons.Externalizer,
                    org.apache.sling.commons.json.io.JSONWriter,
                    java.util.Date" %>
<%!
%><%@include file="/libs/foundation/global.jsp" %><%

    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");

    String authorizableId = request.getParameter("authorizableId");
    final UserPropertiesManager upm = resourceResolver.adaptTo(UserPropertiesManager.class);
    final String INSIGHT_SEGMENTS_RELPATH = "segments/insight";
    final String INSIGHT_PROP_PREFIX = "profile.insight.";

    JSONWriter w = new JSONWriter(response.getWriter());

    //anonymous - special case
    UserProperties userProperties = null;
    if( !"anonymous".equals(authorizableId)) {
        if (authorizableId != null) {
            try {
                userProperties = upm.getUserProperties(authorizableId, "profile");
            } catch (RepositoryException e) {
                slingResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "");
            }
        } else {
            userProperties = upm.getUserProperties(resourceResolver.getUserID(), "profile");
        }
        if (userProperties != null) {
            w.object();
            String avatar = CollabUtil.getAvatar(userProperties);
            //increate avatar size
            avatar = avatar == null ? "" : avatar.replaceAll("\\.32\\.",".80.");
            w.key("avatar").value(avatar);
            w.key("authorizableId").value(userProperties.getAuthorizableID());
            w.key("path").value(userProperties.getNode().getPath());
            w.key("formattedName").value(userProperties.getDisplayName());
            for (String key : userProperties.getPropertyNames()) {
                if (!key.startsWith("jcr:") &&
                        !key.startsWith("sling:") &&
                        !key.startsWith("cq:last") &&
                        !key.startsWith("memberSince") &&
                        !key.startsWith("birthday")) {
                    Object o = userProperties.getProperty(key);
                    if (o != null && o instanceof String) {
                        String v = o.toString();
                        w.key(key).value(v);
                    }
                }
            }

            Date created = userProperties.getProperty("memberSince", null, Date.class);
            if( created == null) {
                try {
                    created = userProperties.getNode().getProperty("jcr:created").getDate().getTime();
                } catch (PathNotFoundException e) {
                    // no created date set
                }
            }
            if( created != null ) {
                java.text.DateFormat df = com.day.cq.commons.date.DateUtil.getDateFormat("d MMM yyyy h:mm a", slingRequest.getLocale());
                w.key("memberSince")
                    .value(df.format(created));
            }

            Date birthday = userProperties.getProperty("birthday", null, Date.class);
            if( birthday != null ) {
                java.text.DateFormat df = com.day.cq.commons.date.DateUtil.getDateFormat("d MMM yyyy", slingRequest.getLocale());
                w.key("birthday")
                    .value(df.format(birthday));
            }

            // export Insight segments
            try {
                String[] insightSegments = userProperties.getProperty(INSIGHT_SEGMENTS_RELPATH, null, String[].class);
                if (insightSegments != null) {
                    for (String seg : insightSegments) {
                        w.key(INSIGHT_PROP_PREFIX + seg).value("true");
                    }
                }
            } catch (RepositoryException e) {
                // no created date set
            }

            w.endObject();
        }
    } else {
        Externalizer externalizer = sling.getService(Externalizer.class);

        String absoluteDefaultAvatar = "";
        if(externalizer != null){
            absoluteDefaultAvatar = externalizer.relativeLink(slingRequest, CollabUtil.DEFAULT_AVATAR);
        }

        w.object();
        w.key("authorizableId").value("anonymous");
        w.key("formattedName").value("Anonymous Surfer");
        w.key("path").value("/home/users/a/anonymous");
        w.key("avatar").value(absoluteDefaultAvatar);
        w.endObject();
    }
%>
