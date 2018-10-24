<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@ page import="com.adobe.granite.security.user.UserProperties,
                 com.adobe.granite.security.user.UserPropertiesManager,
                 com.adobe.cq.social.commons.CollabUtil,
                 com.day.cq.commons.Externalizer,
                 com.day.cq.commons.JSONWriterUtil,
                 com.day.cq.commons.TidyJSONWriter,
                 com.day.cq.commons.TidyJsonItemWriter,
                 com.day.cq.tagging.Tag,
                 com.day.cq.wcm.api.WCMException,
                 com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.core.stats.PageViewStatistics,
                 com.day.cq.xss.ProtectionContext,
                 com.day.cq.xss.XSSProtectionService,
                 org.apache.sling.api.SlingHttpServletRequest,
                 org.apache.sling.api.scripting.SlingScriptHelper,
                 org.apache.sling.commons.json.JSONException,
                 org.apache.sling.commons.json.io.JSONWriter,
                 javax.jcr.Node,
                 javax.jcr.RepositoryException,
                 java.io.StringWriter,
                 java.net.URI,
                 java.text.DecimalFormat,
                 java.util.Collections, java.util.Date, java.util.Random" %><%!
%><%@ include file="/libs/foundation/global.jsp" %><%
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");

    Externalizer externalizer = sling.getService(Externalizer.class);
    XSSProtectionService xss = sling.getService(XSSProtectionService.class);

    String absoluteDefaultAvatar = "";
    if(externalizer != null){
        absoluteDefaultAvatar = externalizer.absoluteLink(slingRequest, slingRequest.getScheme(), CollabUtil.DEFAULT_AVATAR);
    }

    boolean isEditMode = (WCMMode.fromRequest(request) != WCMMode.DISABLED);
    UserPropertiesManager upm = resourceResolver.adaptTo(UserPropertiesManager.class);
    StringWriter buf = new StringWriter();
    try {
        Page cPage = null;
        if (request.getParameter("path") != null) {
            Resource r = resourceResolver.getResource(request.getParameter("path"));
            cPage = (r != null ? r.adaptTo(Page.class) : null);
        } else {
            cPage = currentPage;
        }

        TidyJSONWriter writer = new TidyJSONWriter(buf);
        writer.setTidy(true);

        writer.object();
        writer.key("data").object();
        //TODO find a more generic place for these configs
//        setProfileInitialData(writer, upm, slingRequest, absoluteDefaultAvatar, xss);
//        setPageInitialData(writer, sling, cPage);
//        setTagCloudInitialData(writer, cPage);
//        setSurferInfoInitialData(writer,slingRequest);
        writer.endObject();

        //write clickstreamcloud UI config
        setClickstreamCloudUI(writer, isEditMode);

        //dump configs found under current node
        dumpConfigs(currentNode, writer, isEditMode);

        writer.endObject();
    } catch (Exception e) {
        log.error("Error while generating JSON clickstreamcloud config", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
    }
    // send string buffer
    response.getWriter().print(buf.getBuffer().toString());

%><%!
    void setProfileInitialData(JSONWriter writer, UserPropertiesManager upm,
                               SlingHttpServletRequest slingRequest,
                               String absoluteDefaultAvatar,
                               XSSProtectionService xss) throws Exception {

        writer.key("profile").object();
        UserProperties userProperties = upm.getUserProperties(slingRequest.getResourceResolver().getUserID(), "profile");
        if (userProperties != null) {
            final String id = userProperties.getAuthorizableID();
            String avatar = CollabUtil.getAvatar(userProperties, userProperties.getProperty(UserProperties.EMAIL),absoluteDefaultAvatar);
            //increate avatar size
            avatar = avatar == null ? "" : avatar.replaceAll("\\.32\\.",".80.");
            writer.key("avatar").value(avatar);
            writer.key("path").value(userProperties.getNode().getPath());

            Boolean isLoggedIn = id != null && "anonymous".equals(id);
            writer.key("isLoggedIn").value(isLoggedIn);
            writer.key("isLoggedIn" + JSONWriterUtil.KEY_SUFFIX_XSS)
            		.value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT,isLoggedIn.toString()));

            writer.key("authorizableId").value(id);
            writer.key("authorizableId" + JSONWriterUtil.KEY_SUFFIX_XSS)
                    .value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT, id));

            writer.key("formattedName")
                    .value(userProperties.getDisplayName());
            writer.key("formattedName" + JSONWriterUtil.KEY_SUFFIX_XSS)
                    .value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT,userProperties.getDisplayName()));

            for (String key : userProperties.getPropertyNames()) {
                if (!key.startsWith("jcr:") && !key.startsWith("sling:") && !key.startsWith("cq:last")) {
                    String s = userProperties.getProperty(key, null, String.class);
                    s = s != null ? s : "";
                    writer.key(key)
                            .value(s);
                    writer.key(key + JSONWriterUtil.KEY_SUFFIX_XSS)
                            .value(xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT,s));
                }
            }

            Date created = userProperties.getProperty("memberSince", null,  Date.class);
            if( created == null) {
                created = userProperties.getProperty("jcr:created", null, Date.class);
            }
            if( created != null ) {
                java.text.DateFormat df = com.day.cq.commons.date.DateUtil.getDateFormat("d MMM yyyy h:mm a", slingRequest.getLocale());
                writer.key("memberSince")
                    .value(df.format(created));
            }

            Date birthday = userProperties.getProperty("birthday", null, Date.class);
            if( birthday != null ) {
                java.text.DateFormat df = com.day.cq.commons.date.DateUtil.getDateFormat("d MMM yyyy", slingRequest.getLocale());
                writer.key("birthday")
                    .value(df.format(birthday));
            }
        }
        writer.endObject();
    }

    void setPageInitialData(JSONWriter writer,
                            SlingScriptHelper sling,
                            Page currentPage) throws Exception {
        writer.key("pagedata").object();
        if (currentPage != null) {
            long monthlyHits = 0;
            try {
                final PageViewStatistics pwSvc = sling.getService(PageViewStatistics.class);
                Object[] hits = pwSvc.report(currentPage);
                if (hits != null && hits.length > 2) {
                    monthlyHits = (Long) hits[2];
                }
            } catch (WCMException ex) {
                monthlyHits = -1;
            }
            writer.key("hits").value(monthlyHits);
            writer.key("title").value(currentPage.getTitle());
            writer.key("path").value(currentPage.getPath());

            String navTitle = currentPage.getNavigationTitle();
            if(navTitle == null) {
            	navTitle = currentPage.getTitle();
            }
            if(navTitle == null) {
            	navTitle = currentPage.getName();
            }
            writer.key("navTitle").value(navTitle);

            if(currentPage.getTemplate() != null) {
            	writer.key("template").value(currentPage.getTemplate().getPath());
                writer.key("thumbnail").value(currentPage.getTemplate().getThumbnailPath());
            }

            Tag[] tags = currentPage.getTags();
            String tagsStr = "";
            for(Tag tag: tags) {
                tagsStr += tag.getTitle() + " ";
            }
            writer.key("tags").value(tagsStr);
            String descr = currentPage.getDescription();
            writer.key("description").value(descr != null ? descr : "");

        }


        Random rand = new Random(new Date().getTime());
        DecimalFormat df = new DecimalFormat("0.00");
        double r = rand.nextDouble();
        writer.key("random").value(df.format(r));
        writer.endObject();
    }

    void setTagCloudInitialData(JSONWriter writer, Page currentPage) throws Exception {
        writer.key("tagcloud").object();
        if (currentPage != null) {
            writer.key("tags").array();
            for (Tag tag : currentPage.getTags()) {
                writer.value(tag.getTagID());
            }
            writer.endArray();
        }
        writer.endObject();
    }

    void setSurferInfoInitialData(JSONWriter writer,
                                  SlingHttpServletRequest request) throws Exception {
        writer.key("surferinfo").object();
        String ip = request.getRemoteAddr();
        String keywords = null;
        String referer = request.getHeader("Referer");
        if (referer != null) {
            URI uri = new URI(referer);
            String query = uri.getQuery();
            if (query != null) {
                int qindex = query.indexOf("q=");
                if(qindex >-1) {
                    int andindex = query.indexOf("&",qindex);
                    keywords = query.substring(qindex+2,andindex > -1 ? andindex : query.length());
                    keywords = keywords.replaceAll("\\+", " ");
                }
            }
        }
        keywords = (keywords != null ? keywords : (request.getParameter("q") != null ? request.getParameter("q") : ""));
        keywords = keywords.replaceAll("<","&lt;");
        keywords = keywords.replaceAll(">","&gt;");
        writer.key("IP").value(ip);
        writer.key("keywords").value(keywords);
        writer.endObject();
    }


    void setClickstreamCloudUI(JSONWriter writer, boolean isEditMode) throws Exception {
        writer.key("ui").object();
        writer.key("target").value("clickstreamcloud-gui");
        writer.key("version").value("light");
        if (isEditMode) {
            writer.key("hideEditLink").value(false);
            writer.key("hideLoadLink").value(false);
            writer.key("disableKeyShortcut").value(false);
        }
        writer.endObject();
    }

    void dumpConfigs(Node rootNode, JSONWriter writer, boolean isEditMode) throws JSONException, RepositoryException {
        if (rootNode != null) {
            writer.key("configs").object();
            NodeIterator ni = rootNode.getNodes();
            TidyJsonItemWriter tjiw = new TidyJsonItemWriter(Collections.EMPTY_SET);
            while (ni.hasNext()) {
                Node child = ni.nextNode();
                writer.key(child.getName()).object();
                if (child.hasNode("ui")) {
                    writer.key("ui");
                    tjiw.dump(child.getNode("ui"), writer, 10);
                }

                if (child.hasNode("store")) {
                    writer.key("store");
                    tjiw.dump(child.getNode("store"), writer, 10);
                }

                if (isEditMode) {
                    if (child.hasNode("edit")) {
                        writer.key("edit");
                        tjiw.dump(child.getNode("edit"), writer, 10);
                    }
                }
                writer.endObject();
            }
        }
        writer.endObject();
    }
%>
