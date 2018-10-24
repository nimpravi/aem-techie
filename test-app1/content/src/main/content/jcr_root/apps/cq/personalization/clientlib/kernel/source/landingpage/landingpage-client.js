/*
 * ***********************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 * Copyright 2011 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 * ***********************************************************************
 */

window.CQ_trackLandingPagesStats = true;

function initializeLandingPageLoader(allLandingPages, strategyName, targetElementId, isEditMode, trackingURL) {
    isEditMode = CQ.Ext && (isEditMode == "true" || isEditMode === true);

    if( window.CQ_Analytics ) {
        var LANDINGPAGE_SUFFIX = ".html";

        var toExecute = function() {
            var currentVisibleLandingPage = null;
            //function which chooses and loads a landingPage.
            var loadLandingPages = function() {
                var landingPages = new Array();
                if (CQ_Analytics.SegmentMgr) {
                    var lastBoost = 0;
                    for (var i = 0; i < allLandingPages.length; i++) {
                        if (!allLandingPages[i]["segments"] ||
                            allLandingPages[i]["segments"].length == 0 ||
                            CQ_Analytics.SegmentMgr.resolveArray(allLandingPages[i]["segments"]) === true) {
                            var boost = CQ_Analytics.SegmentMgr.getMaxBoost(allLandingPages[i]["segments"]);
                            if (boost == lastBoost) {
                                //same boost, add to list
                                landingPages.push(allLandingPages[i]);
                            } else {
                                if (boost > lastBoost) {
                                    //better boost, clear list and keep only this one
                                    landingPages = new Array();
                                    landingPages.push(allLandingPages[i]);
                                    lastBoost = boost;
                                }
                            }
                        }
                    }
                }
                if (landingPages.length > 0) {
                    // fallback: display first
                    var landingPageToShow = landingPages[0];
                    if (CQ_Analytics.StrategyMgr) {
                        var lp = CQ_Analytics.StrategyMgr.choose(strategyName, landingPages);
                        if (lp != null) {
                            landingPageToShow = lp;
                        }
                    }
                    if (!currentVisibleLandingPage || currentVisibleLandingPage.path != landingPageToShow.path) {
                        var previousLandingPage = currentVisibleLandingPage;
                        currentVisibleLandingPage = landingPageToShow;

                        var request = CQ.shared.HTTP.get(landingPageToShow.path + LANDINGPAGE_SUFFIX);
                        var text = request.responseText;

                        var extractDiv = function(text, id) {
                            var ret = "";
                            if( text && text.indexOf("id=\"" + id + "\"") != -1) {
                                var index = text.indexOf("id=\"" + id + "\"");
                                var oDivIndex = text.substring(0, index).lastIndexOf("<div");
                                var tmp = text.substring(oDivIndex);
                                var split = tmp.split(new RegExp("<div", "ig"));
                                var opened = 0;
                                for(var i=0;i<split.length;i++) {
                                    opened++;
                                    var split2 = split[i].split(new RegExp("</div", "ig"));
                                    for(var j=1; j < split2.length; j++) {
                                        opened--;

                                        if(opened == 1) {
                                            var cDivIndex = split[i].lastIndexOf("</div") + 6;

                                            cDivIndex = tmp.indexOf(split[i]) + cDivIndex;
                                            tmp = tmp.substring(0, cDivIndex);

                                            tmp = tmp.substring(tmp.indexOf(">") + 1, tmp.lastIndexOf("</div"));
                                            return tmp;
                                        }
                                    }
                                }
                             }
                             return "";
                        };

                        text = extractDiv(text, targetElementId);

                        var target = $CQ("#" + targetElementId)[0];

                        var removeEditables = function(filter, show) {
                            if( isEditMode ) {
                                var editables = CQ.WCM.getEditables();
                                for(var epath in editables) {
                                    var editable = editables[epath];
                                    if( !filter || editable.path.indexOf(filter) != -1) {
                                        editable.hide();
                                        editable.remove();
                                    }
                                }
                            }
                        };

                        var node = document.createElement("div");
                        node.innerHTML = text;

                        if( previousLandingPage ) {
                            $CQ("object", target).parent().fadeOut("slow");
                            $CQ("img", target).fadeOut("slow");
                            $CQ(target).slideUp("slow", function() {
                                removeEditables(previousLandingPage.path, false);
                                $CQ(target).children().remove();

                                var toInject = target.insertBefore(node,target.firstChild);

                                $CQ(target).slideDown("slow", function() {
                                    if( isEditMode ) {
                                        CQ.DOM.executeScripts(CQ.Ext.get(node));
                                    }
                                });
                            });
                        } else {
                            var toInject = target.insertBefore(node,target.firstChild);
                            $CQ(target).slideDown("slow", function() {
                                if( isEditMode ) {
                                    CQ.DOM.executeScripts(CQ.Ext.get(node));
                                }
                            });
                        }

                        try {
                            if(window.CQ_trackLandingPagesStats && trackingURL) {
                                if( !CQ_Analytics.loadedLandingPagesStack) {
                                    //store in loadedLandingPagesStack the list of landingPages shown in the page.
                                    CQ_Analytics.loadedLandingPagesStack = [];
                                    //on window unload, post
                                    $CQ(window).unload(function() {
                                        try {
                                            var loadedLandingPages = CQ_Analytics.loadedLandingPagesStack;
                                            if( loadedLandingPages ) {
                                                delete CQ_Analytics.loadedLandingPagesStack;
                                                //build the URL : trackingURL + paths
                                                var url = trackingURL;
                                                for(var i=0;i<loadedLandingPages.length; i++) {
                                                    url = CQ.shared.HTTP.addParameter(url,"path",loadedLandingPages[i]);
                                                }
                                                //run get in asynch mode.
                                                CQ.shared.HTTP.get(url, function() {});
                                            }
                                        } catch(error) {}
                                    });
                                }
                                CQ_Analytics.loadedLandingPagesStack.push(landingPageToShow.path);
                            }
                        } catch(error) {}
                    }
                } else {
                    CQ_Analytics.Utils.clearElement(targetElementId);
                    currentVisibleLandingPage = null;
                }
            };

            loadLandingPages.call();

            //loaded landingPage might change everytime a segment resolution state changes
            if (CQ_Analytics.SegmentMgr) {
                CQ_Analytics.SegmentMgr.addListener("update", loadLandingPages);
            }
        };

        //first landingPage load is done when all stores are loaded
        if( CQ_Analytics.ClickstreamcloudMgr ) {
            if( CQ_Analytics.ClickstreamcloudMgr.areStoresLoaded ) {
                toExecute.call(this);
            } else {
                CQ_Analytics.ClickstreamcloudMgr.addListener("storesloaded",toExecute);
            }
        }
    }
    }
