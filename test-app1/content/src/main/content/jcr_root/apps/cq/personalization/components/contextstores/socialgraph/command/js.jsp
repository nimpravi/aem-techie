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
                    com.adobe.granite.security.user.UserPropertiesManager,
                    com.adobe.cq.social.commons.CollabUtil,
                    com.day.cq.wcm.foundation.forms.FormsHelper,
                    com.day.cq.xss.ProtectionContext,
                    com.day.cq.xss.XSSProtectionService,
                    org.apache.sling.commons.json.io.JSONWriter,
                    java.io.StringWriter,
                    java.util.Arrays,
                    java.util.List" %><%!
%><%@include file="/libs/foundation/global.jsp"%><%

    final UserPropertiesManager upm = resourceResolver.adaptTo(UserPropertiesManager.class);
    XSSProtectionService xss = sling.getService(XSSProtectionService.class);
    Resource userResource = null;

    List<Resource> resources = FormsHelper.getFormEditResources(slingRequest);
    if (resources != null && resources.size() > 0) {
       //1 - we are in formchooser-mode, get the requested resource
        userResource = resources.get(0);
    }

    UserProperties userProperties = userResource != null ? upm.getUserProperties(userResource.adaptTo(Node.class)) : null;

    response.setContentType("text/javascript");
    response.setCharacterEncoding("utf-8");

    out.print(request.getParameter("callback"));
    out.print("(");

    if( userProperties != null) {
        Long limit = Long.parseLong(request.getParameter("limit") != null ? request.getParameter("limit") : "-1");

        StringWriter sw = new StringWriter();
        final JSONWriter w = new JSONWriter(sw);
        w.setTidy(Arrays.asList(slingRequest.getRequestPathInfo().getSelectors()).contains("tidy"));
        w.object();

        String[] friends = userProperties.getProperty("friends", new String[0], String[].class);
        if( friends != null ) {
            w.key("friends");
            w.array();
            int count = 0;
            for(String f: friends) {
                try {
                    UserProperties up = upm.getUserProperties(f, "profile");
                    if( up != null ) {
                        w.object();
                        w.key("authorizableId").value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT,f));
                        w.key("avatar").value(CollabUtil.getAvatar(up));
                        w.key("formattedName").value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT,
                                                                           up.getDisplayName()));
                        w.endObject();
                        count ++;
                        if( limit > -1 && count >= limit) break;
                    }
                } catch (RepositoryException e) {
                    //unknown user
                }
            }
            w.endArray();
        }

        String[] followers = userProperties.getProperty("followers", new String[0], String[].class);
        if( followers != null ) {
            w.key("followers");
            w.array();
            int count = 0;
            for(String f: followers) {
                try {
                    UserProperties up = upm.getUserProperties(f, "profile");
                    if( up != null ) {
                        w.object();
                        w.key("authorizableId").value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT,f));
                        w.key("avatar").value(CollabUtil.getAvatar(up));
                        w.key("formattedName").value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT,
                                                                           up.getDisplayName()));
                        w.endObject();
                        count ++;
                        if( limit > -1 && count >= limit) break;
                    }
                } catch (RepositoryException e) {
                    //unknown user
                }
            }
            w.endArray();
        }

        w.endObject();
        sw.close();
        out.print(sw.toString());

    } else {
        out.print("{}");
    }
    out.print(")");
%>
