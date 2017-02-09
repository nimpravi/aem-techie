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

--%><%@page import="com.day.cq.commons.LabeledResource,
                    org.apache.sling.commons.json.io.JSONWriter,
                    com.day.text.Text,
                    com.day.cq.tagging.Tag,
                    com.day.cq.security.User,
                    org.apache.sling.api.resource.ResourceResolver,
                    com.day.cq.wcm.mobile.core.MobileUtil,
                    com.day.cq.personalization.TeaserUtils,
                    java.util.Iterator,
                    java.util.HashMap,
                    com.day.cq.mcm.api.MCMUtil"
%><%@include file="/libs/foundation/global.jsp" %><%

                HashMap<String, Object> values = new HashMap<String, Object>();

                LabeledResource lr = resource.getParent().adaptTo(LabeledResource.class);
                String name = Text.getName(resource.getParent().getPath());

                String text;

                if (lr == null) {
                    text = name;
                } else {
                    text = (lr.getTitle() == null)
                            ? name
                            : lr.getTitle();
                }
                if (text != null) {
                    text = text.replaceAll("<", "&lt;");
                }
                
                values.put("type", "teaser");
                values.put("pluginId", "defaultMCM");
                
                values.put("id", resource.getParent().getPath());
                
                if (lr != null && lr.getDescription() != null) {
                    values.put("description", lr.getDescription());
                }
            
                values.put("experienceTitle", text);


                Page myPage = resource.getParent().adaptTo(Page.class);

                String imageUrl = TeaserUtils.getImage(myPage);
                values.put("image", (imageUrl == null ? "/libs/cq/personalization/templates/teaser/thumbnail.png": imageUrl) );

                ValueMap content = myPage.getProperties();
                if(content != null) {
                    try {
                        if(content.containsKey("onTime"))
                            values.put("onTime", content.get("onTime", String.class));
                        if(content.containsKey("offTime"))
                            values.put("offTime", content.get("offTime", String.class));
                        
                        values.put("lastModifiedDate", content.get("cq:lastModified", String.class));
                        String userId = content.get("cq:lastModifiedBy", String.class);
                        User user = MCMUtil.getUser(resource.getResourceResolver(), userId);
                        if (user != null) {
                               values.put("lastModifiedByTitle", user.getName());
                        }

                        
                        String campaignPath = "";
                        Resource parent = resource.getParent();
                        while(parent != null && !parent.getChild("jcr:content").isResourceType("cq/personalization/components/campaignpage")) {
                            parent = parent.getParent();
                        }
                        if(parent != null)
                            campaignPath = parent.getPath();

                        Iterator<Resource> iter = parent.getResourceResolver().findResources(
                            "/jcr:root/content//*[(@campaignpath = '" + campaignPath + "') " +  
                            "and (@sling:resourceType='cq/personalization/components/teaser')]", 
                            "xpath");
                        int i, length = 100;
                        Resource closest = null;
                        for(i=0; iter.hasNext(); i++) {
                            Resource tmp = iter.next();
                            if(length > tmp.getPath().split("/").length) {
                                closest = tmp;
                                length = tmp.getPath().split("/").length;
                            }
                        }
                        
                        if(closest != null) {
                            parent = closest.getParent();
                            while(parent != null && !parent.getResourceType().equals("cq:Page")) {
                                parent = parent.getParent();
                            }
                            if(parent != null) {
                                Page teaserContainingPage = parent.adaptTo(Page.class);
                                values.put("touchpointTitle", teaserContainingPage.getTitle() + " (" + closest.getName() + ")");
                                values.put("touchpointChannel", (MobileUtil.isMobileResource(parent.getParent())? "Mobile": "Web"));
                                values.put("simulateUrl", teaserContainingPage.getPath() + ".html");
                            }
                        }
                        
                        MCMUtil.addPossibleTagsToValues(myPage,values);
                        MCMUtil.addPossibleSegmentsToValues(content, values, resource.getResourceResolver());
                        
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }

                values.put("analyzeUrl", resource.getParent().getPath());
                
                request.setAttribute("cq.includedexperience", values);
%>
