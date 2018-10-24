<%@page session="false"%><%--**********************************************************************
  *
  * ADOBE CONFIDENTIAL
  * __________________
  *
  *  Copyright 2011 Adobe Systems Incorporated
  *  All Rights Reserved.
  *
  * NOTICE:  All information contained herein is, and remains
  * the property of Adobe Systems Incorporated and its suppliers,
  * if any.  The intellectual and technical concepts contained
  * herein are proprietary to Adobe Systems Incorporated and its
  * suppliers and are protected by trade secret or copyright law.
  * Dissemination of this information or reproduction of this material
  * is strictly forbidden unless prior written permission is obtained
  * from Adobe Systems Incorporated.
  **********************************************************************--%><%!
%><%@ page import="com.day.cq.wcm.msm.api.LiveRelationshipManager,
    java.util.Collection,
    com.day.cq.wcm.msm.api.LiveRelationship,
    com.day.cq.wcm.msm.api.RolloutConfig,
    java.util.List,
    com.day.text.Text,
    com.day.cq.wcm.api.WCMException,
    java.io.PrintWriter,
    com.day.cq.wcm.emulator.EmulatorGroup,
    org.apache.sling.commons.json.JSONObject,
    com.day.cq.wcm.emulator.Emulator,
    com.day.cq.wcm.emulator.EmulatorService,
    static com.day.cq.wcm.mobile.api.MobileConstants.HTML_ID_CONTENT_CSS,
    org.apache.sling.commons.json.JSONException,
    com.day.cq.wcm.mobile.api.device.DeviceGroup,
    com.day.cq.wcm.mobile.api.device.DeviceGroupList,
    org.apache.commons.lang3.StringEscapeUtils,
    java.io.StringWriter" %><%!
    %><%@include file="/libs/foundation/global.jsp"%><%

    Page cPage = null;
    if (request.getParameter("path") != null) {
        Resource r = resourceResolver.getResource(request.getParameter("path"));
        cPage = (r != null ? r.adaptTo(Page.class) : null);
    } else {
        cPage = currentPage;
    }

    if( cPage != null ) {
        //found corresponding mobile page
        LiveRelationshipManager relationMgr = sling.getService(LiveRelationshipManager.class);
        Page mobilePage = getMobilePage(relationMgr, cPage);
        if( mobilePage == null ) {
            //try by injecting "_mobile" in the path
            String path = cPage.getPath();
            String computedPath = Text.getAbsoluteParent(path, 1);
            computedPath = path.replaceAll(computedPath, computedPath + "_mobile");
            Resource computed = resourceResolver.resolve(computedPath);
            if( computed != null ) {
                mobilePage = computed.adaptTo(Page.class);
            }
        }

        if( mobilePage != null) {
            EmulatorService emulatorService = sling.getService(EmulatorService.class);
            List<Emulator> allEmulators = getEmulators(emulatorService, mobilePage);
            final DeviceGroupList deviceGroups = mobilePage.adaptTo(DeviceGroupList.class);

            if(deviceGroups != null && !allEmulators.isEmpty()) {

%><script type="text/javascript">
    $CQ(function() {
        CQ_Analytics.ClientContextUI.onLoad(function() {

            var path = CQ_Analytics.PageDataMgr.getProperty("path");

            if( !path ) return;
            if( !CQ_Analytics.MobileSliderUtils.CONFIG ) return;

            //default
            var app = path;

            //try to identity the corresponding CONFIG
            for(var c in CQ_Analytics.MobileSliderUtils.CONFIG) {
                //test if path contains /app
                if( path.indexOf("/" + c) != -1) {
                    //but take care of /app-anotherapp
                    //it should be exactly /app/
                    if( path.indexOf("/" + c + "/") != -1) {
                        app = c;
                    } else {
                        //or end by /app
                        if( path.lastIndexOf("/" + c) == (path.length - ("/" + c).length)) {
                            app = c;
                        }
                    }
                }
            }

            //surferinfo slider
            $CQ(function() {
                var createSliderSI = function(event, show, vertical) {
                    CQ_Analytics.SurferInfoMgr.slider = new CQ_Analytics.Slider({
                        "vertical": vertical,
                        "wrap": "circular",
                        "animation": "slow",
                        "start": start,
                        "clazz": "cq-cc-slider-surferinfo",
                        "parent": $CQ(".cq-cc-surferinfo-thumbnail").parent()
                    });
                    CQ_Analytics.SurferInfoMgr.slider.init();

                    var devicesObj = {};

                    var surferInfoStore = ClientContext.get("surferinfo");
                    var valuesStore = surferInfoStore.initProperty;
                    var deviceList = [{
                        "id": "Desktop",
                        "picturePath": ClientContext.get("surferinfo/thumbnail", true),
                        "name": ClientContext.get("surferinfo/browserFamily", true)
                    }];

                    <%
                        for (final DeviceGroup group : deviceGroups) {
                            final List<Emulator> emulators = group.getEmulators();
                            for (final Emulator emulator : emulators) {
                                final JSONObject e = new JSONObject();
                                try {
                                    e.put("id",emulator.getName());
                                    e.put("name",emulator.getName());
                                    e.put("path",emulator.getPath());
                                    e.put("picturePath",emulator.getPath() + "/thumbnail.png");
                                    e.put("css",emulator.getPath() + "/css/source/emulator.css");
                                    e.put("contextCss",emulator.getContentCssPath());
                                    e.put("group", group.getName());
                                    %>deviceList.push(<%=e.toString()%>);<%
                                    %>devicesObj["<%=emulator.getName()%>"] = <%=e.toString()%>;<%
                                } catch (JSONException e1) {}
                            }
                        }
                    %>

                    var getItemHTML = function(id, imagePath, title) {
                        var item = $CQ("<li>");
                        var div = $CQ("<div>")
                                .attr("title", title)
                                .attr("data-id",id)
                                .css("width", "80px")
                                .css("height", "80px")
                                .css("background-image", "url("+_g.shared.HTTP.externalize(imagePath)+")")
                                .css("background-repeat", "no-repeat")
                                .css("background-position", "center center")
                                .css("padding", "4px")
                                .bind("click",function() {
                                    CQ_Analytics.SurferInfoMgr.slider.select($CQ(this).parent().attr("jcarouselindex"));
                                })
                                .appendTo(item);
                        return item;
                    };

                    var start = 1;
                    var totalItems = 0;
                    var current = CQ_Analytics.SurferInfoMgr.getProperty("device") || "" ;
                    for(var i = 0; i < deviceList.length; i++) {
                        var p = deviceList[i];
                        totalItems ++;
                        CQ_Analytics.SurferInfoMgr.slider.carousel.append(getItemHTML(p["id"], p["picturePath"], p["name"]));
                        if( p["id"] == current ) {
                            start = totalItems;
                        }
                    }

                    CQ_Analytics.SurferInfoMgr.slider.onSelect = function(toLoadId) {
                        var mobilePath = "<%=mobilePage.getPath()%>";

                        var speed = "slow";

                        var DESKTOP_MAIN_ID = CQ_Analytics.MobileSliderUtils.getConfig(app, "DESKTOP_MAIN_ID");
                        var MOBILE_MAIN_ID = CQ_Analytics.MobileSliderUtils.getConfig(app, "MOBILE_MAIN_ID");

                        var startEmulator = function(device) {
                            $CQ(document.body).css("display","block");
                            CQ_Analytics.MobileSliderUtils.injectCss("/libs/wcm/emulator/widgets.css");
                            $CQ(document.body).css("display","block");

                            {
                                var emulator = devicesObj[device];
                                CQ_Analytics.MobileSliderUtils.injectCss(emulator["contentCss"]);
                                for(var name in devicesObj) {
                                    CQ_Analytics.MobileSliderUtils.injectCss(devicesObj[name]["css"]);
                                }
                            }

                            if (!CQ_Analytics.SurferInfoMgr.slider.emulatorMgr) {
                                $CQ.getScript(_g.shared.HTTP.externalize("/libs/wcm/emulator/widgets.js"), function() {
                                    try {
                                        var config = {
                                            defaultEmulator: device,
                                            contentCssId: "<%=HTML_ID_CONTENT_CSS%>",
                                            showCarousel: true,
                                            emulatorConfigs: <%=getEmulatorsConfig(deviceGroups, request.getContextPath())%>
                                        };

                                        if( !CQ_Analytics.SurferInfoMgr.slider.emulatorMgr ) {
                                            CQ.wcm.emulator.EmulatorManager.SKIP_GROUP_TEST = true;

                                            var emulatorMgr = CQ.WCM.getEmulatorManager();
                                            emulatorMgr.launch(config);
                                            CQ_Analytics.SurferInfoMgr.slider.emulatorMgr = emulatorMgr;
                                        }
                                        CQ_Analytics.SurferInfoMgr.slider.emulatorMgr.switchEmulator(device);

                                        CQ_Analytics.SurferInfoMgr.currentDevice = device;
                                        $CQ("#" + MOBILE_MAIN_ID).fadeIn(speed, function() {
                                            window.setTimeout(function() {
                                                CQ.WCM.toggleEditables(true, mobilePath);
                                            }, 500);
                                        });
                                    } catch(error) {
                                        console.log("error while loading emulator.js",error);
                                    }
                                });
                            } else {
                                CQ.wcm.emulator.EmulatorManager.SPECIAL_WRAPPING_ID = MOBILE_MAIN_ID;
                                CQ.wcm.emulator.EmulatorManager.SKIP_GROUP_TEST = true;
                                CQ_Analytics.SurferInfoMgr.slider.emulatorMgr.switchEmulator(device);

                                CQ_Analytics.SurferInfoMgr.currentDevice = device;
                                $CQ("#" + MOBILE_MAIN_ID).fadeIn(speed, function() {
                                    window.setTimeout(function() {
                                        CQ.WCM.toggleEditables(true, mobilePath);
                                    }, 500);
                                });
                            }
                        };

                        var stopEmulator = function() {
                            if( CQ_Analytics.SurferInfoMgr.slider.emulatorMgr) {
                                $CQ("#cq-emulator-toolbar").slideUp(speed, function() {
                                    var device = CQ_Analytics.SurferInfoMgr.currentDevice;

                                    CQ_Analytics.SurferInfoMgr.slider.emulatorMgr.stopEmulator();

                                    CQ_Analytics.MobileSliderUtils.removeCss("/libs/wcm/emulator/widgets.css");

                                    var emulator = devicesObj[device];
                                    CQ_Analytics.MobileSliderUtils.removeCss(emulator["contentCss"]);
                                    for(var name in devicesObj) {
                                        CQ_Analytics.MobileSliderUtils.removeCss(devicesObj[name]["css"]);
                                    }

                                    CQ_Analytics.SurferInfoMgr.currentDevice = "Desktop";
                                });
                            }

                        };

                        var restore = function() {
                            window.CQURLInfo.requestPath = "<%=currentPage.getPath()%>";
                            window.CQURLInfo.selectors = window.CQURLInfo.selectors_ori;
                            CQ.WCM.toggleEditables();
                            stopEmulator();
                            $CQ("#" + MOBILE_MAIN_ID).fadeOut(speed, function() {
                                CQ_Analytics.MobileSliderUtils.switchToDesktop(app);

                                $CQ("#" + MOBILE_MAIN_ID).attr("id",MOBILE_MAIN_ID + "_mobile");
                                $CQ("#"+DESKTOP_MAIN_ID+"_excluded").attr("id",DESKTOP_MAIN_ID);

                                $CQ("#"+MOBILE_MAIN_ID+"_mobile").appendTo(document.body);
                                $CQ("#" + DESKTOP_MAIN_ID).fadeIn(speed, function() {
                                    CQ.WCM.getSidekick().loadContent(path);
                                    window.setTimeout(function() {
                                        CQ.WCM.toggleEditables(true, path);
                                    }, 500);
                                });
                            });
                        };

                        var inject = function(device) {
                            var emulator = devicesObj[device];

                            if( !window.CQURLInfo ) {
                                window.CQURLInfo = {
                                    requestPath: "<%=currentPage.getPath()%>",
                                    selectors: [emulator["group"]]
                                };
                            }
                            window.CQURLInfo.requestPath = "<%=mobilePage.getPath()%>";
                            window.CQURLInfo.selectors_ori = window.CQURLInfo.selectors;
                            window.CQURLInfo.selectors = [emulator["group"]];

                            $CQ(document.body).children().addClass("excluded");

                            if( !CQ_Analytics.SurferInfoMgr.slider.emulatorMgr) {
                                var req = CQ.shared.HTTP.get(mobilePath + "." + emulator["group"] + ".html");
                                var t = req.responseText;
                                t = t.substring(t.indexOf("<div id=\""+MOBILE_MAIN_ID+"\""), t.indexOf("</body>"));
                                t = t.replace("id=\""+MOBILE_MAIN_ID+"\"","id=\""+MOBILE_MAIN_ID+"_mobile\" style=\"display: none\"");
                                //var toInject = $CQ(t);

                                //toInject.hide();
                                var n = document.createElement("div");
                                n.innerHTML = t;
                                var toInject = $CQ($CQ("body")[0].insertBefore(n,$CQ("body")[0].firstChild));
                                CQ.DOM.executeScripts(CQ.Ext.get(n));
                                var parent = toInject.parent();
                                var toRemove = toInject;
                                toInject = toInject.children().prependTo(parent);
                                toInject.addClass("injected");
                                toRemove.remove();

                            }

                            CQ.WCM.toggleEditables();
                            $CQ("#"+DESKTOP_MAIN_ID).fadeOut(speed, function() {
                                $CQ("#"+DESKTOP_MAIN_ID).attr("id",DESKTOP_MAIN_ID + "_excluded");
                                $CQ("#"+MOBILE_MAIN_ID+"_mobile").attr("id",MOBILE_MAIN_ID);

                                window.setTimeout(function() {
                                    CQ_Analytics.MobileSliderUtils.switchToMobile(app);

                                    CQ.WCM.getSidekick().loadContent(mobilePath);
                                    startEmulator(device);
                                }, 200);
                            });
                        };

                        if( toLoadId == "Desktop") {
                            if( CQ_Analytics.SurferInfoMgr.currentDevice != "Desktop") {
                                CQ_Analytics.SurferInfoMgr.reset();
                                if( CQ_Analytics.SurferInfoMgr.slider.emulatorMgr ) {
                                    restore();
                                }
                            }
                        } else {
                            //TODO improve with more emulator data
                            CQ_Analytics.SurferInfoMgr.setProperty("device", toLoadId);
                            CQ_Analytics.SurferInfoMgr.setProperty("browserFamily", "");
                            CQ_Analytics.SurferInfoMgr.setProperty("browserVersion", "");
                            CQ_Analytics.SurferInfoMgr.setProperty("OS", "");
                            var emulator = devicesObj[toLoadId];
                            CQ_Analytics.SurferInfoMgr.setProperty("thumbnail", emulator["picturePath"]);
                            if( !CQ_Analytics.SurferInfoMgr.slider.emulatorMgr ) {
                                inject(toLoadId);
                            } else {
                                startEmulator(toLoadId);
                            }

                        }
                    };

                    CQ_Analytics.SurferInfoMgr.slider.getCurrentValue = function() {
                        return CQ_Analytics.SurferInfoMgr.getProperty("device");
                    };

                    CQ_Analytics.SurferInfoMgr.slider.show();

                };

                var handleSliderSI = function(event) {
                    if( !CQ_Analytics.SurferInfoMgr.slider) {
                        createSliderSI.call(this, event, true);
                    } else {
                        CQ_Analytics.SurferInfoMgr.slider.show();
                    }
                    if( CQ_Analytics.ProfileDataMgr.slider ) CQ_Analytics.ProfileDataMgr.slider.hide();
                    event.stopPropagation();
                };

                if( CQ_Analytics.MobileSliderUtils.CONFIG[app]) {
                    $CQ(".cq-cc-surferinfo-thumbnail").parent().parent().bind("click", handleSliderSI);
                }



            });
        });
    });
</script><% }
    }

    //case of a mobile page, SurerInfo should be updated when emulator is changed
%><script type="text/javascript">
    $CQ(function() {
        CQ_Analytics.ClientContextUI.onLoad(function() {
            if( window.CQ && window.CQ.WCM) {
                var emulMgr = CQ.WCM.getEmulatorManager();
                if( emulMgr ) {
                    emulMgr.on("start", function(emulator) {
                        //TODO improve with more emulator data
                        CQ_Analytics.SurferInfoMgr.setProperty("device", emulator["name"]);
                        CQ_Analytics.SurferInfoMgr.setProperty("browserFamily", "");
                        CQ_Analytics.SurferInfoMgr.setProperty("browserVersion", "");
                        CQ_Analytics.SurferInfoMgr.setProperty("OS", "");
                        CQ_Analytics.SurferInfoMgr.setProperty("thumbnail",
                                CQ.shared.HTTP.externalize(
                                        "/libs/wcm/mobile/components/emulators/" + emulator["name"] + "/thumbnail.png"));
                    });
                }
            }
        });
    });
</script>
<%
}%><%!

    Page getMobilePage(LiveRelationshipManager relationMgr, Page currentPage) throws com.day.cq.wcm.api.WCMException {
        if (relationMgr != null && currentPage != null) {
            PageManager pageManager = currentPage.getPageManager();
            Collection<LiveRelationship> relations  = relationMgr.getLiveRelationships(currentPage, null, null, false);
            for(LiveRelationship lr: relations) {
                List<RolloutConfig> configs = lr.getRolloutConfigs();
                for(RolloutConfig rc: configs) {
                    if( rc.getPath().toLowerCase().indexOf("mobile") != -1) {
                        Page mobilePage = pageManager.getContainingPage(lr.getTargetPath());
                        if( mobilePage != null) {
                            return mobilePage;
                        }
                    }
                }
            }
        }
        return null;
    }

    List<EmulatorGroup> getEmulatorGroups(EmulatorService emulatorService, Page mobilePage) {
        return emulatorService.getEmulatorGroups(mobilePage.adaptTo(Resource.class));
    }

    List<Emulator> getEmulators(EmulatorService emulatorService, Page mobilePage) {
        return emulatorService.getEmulators(mobilePage.adaptTo(Resource.class));
    }

    StringWriter getEmulatorsConfig(DeviceGroupList deviceGroups, String contextPath) {
        StringWriter res = new StringWriter();
        String delim = "";

        res.write("{");
        for (final DeviceGroup group : deviceGroups) {
            final List<Emulator> emulators = group.getEmulators();
            for (final Emulator emulator : emulators) {
                res.write(delim);
                res.write(StringEscapeUtils.escapeEcmaScript(emulator.getName()) + ": {");
                res.write("plugins: {");

                String pluginDelim = "";
                if (emulator.canRotate()) {
                    res.write("rotation: {");
                    res.write("ptype: CQ.wcm.emulator.plugins.RotationPlugin.NAME,");
                    res.write("config: {");
                    res.write("defaultDeviceOrientation: \"vertical\"");
                    res.write("}");
                    res.write("}");

                    pluginDelim = ",";
                }

                if (emulator.hasTouchScrolling()) {
                    res.write(pluginDelim + "touchscrolling: {");
                    res.write("ptype: CQ.wcm.emulator.plugins.TouchScrollingPlugin.NAME,");
                    res.write("config: {}");
                    res.write("}");
                }

                res.write("},");
                res.write("group: \"" + StringEscapeUtils.escapeEcmaScript(group.getName()) + "\",");
                res.write("title: \"" + StringEscapeUtils.escapeEcmaScript(emulator.getTitle()) + "\",");
                res.write("description: \"" + StringEscapeUtils.escapeEcmaScript(emulator.getDescription()) + "\",");
                res.write("contentCssPath: \"" +
                        (null != emulator.getContentCssPath() ?
                                contextPath + emulator.getContentCssPath():
                                "null") + "\"");


                res.write("}");
                delim = ",";
            }
        }
        res.write("}");
        return res;

    }
%>
