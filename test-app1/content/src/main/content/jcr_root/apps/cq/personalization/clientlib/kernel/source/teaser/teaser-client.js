/*
 * Copyright 1997-2009 Day Management AG
 * Barfuesserplatz 6, 4001 Basel, Switzerland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Day Management AG, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Day.
 */
window.CQ_trackTeasersStats = true;

/**
 * Initializes every needed to select a teaser from a list (dependending on the given strategy) and
 * to load choosen teaser content into a DOM element.
 * Also allows for overriding the automatic teaser selection for simulation purposes.
 * @param {Array} allTeasers Teasers list
 * @param {String} strategyName Name of the selection strategy (must be availabe in CQ_Analytics.StrategyManager)
 * @param {DOMElement} targetElementId DOM element to insert choosen teaser
 * @param {Boolean} isEditMode True if edit mode is enabled
 * @param {String} trackingURL (optional) URL of the tracking service for teaser impressions (if window.CQ_trackTeasersStats)
 */
function initializeTeaserLoader(allTeasers, strategyName, targetElementId, isEditMode, trackingURL, editablePath) {
    isEditMode = !!(CQ.Ext && (isEditMode == "true" || isEditMode === true));
    if( window.CQ_Analytics ) {
        var toExecute = function() {
            var TEASER_SUFFIX = "/_jcr_content/par.html";
            if( isEditMode ) {
                TEASER_SUFFIX += "?wcmmode=disabled";
            }

            // Simulation: Override the normal teaser display if
            // the PageDataMgr has an experience set.
            var forceExp = CQ_Analytics.PageDataMgr.getExperience();
            if (forceExp) {
                CQ_Analytics.PageDataMgr.clearExperience();
                CQ_Analytics.Utils.loadElement(forceExp + TEASER_SUFFIX, targetElementId);
                return;
            }

            //function which computes an HTML text describing how selection is done.
            var computeDecisionHTML = function(teaserPath) {
                var html = "";

                var teasers = new Array();
                if (CQ_Analytics.SegmentMgr) {
                    var lastBoost = 0;
                    for (var i = 0; i < allTeasers.length; i++) {
                        var p = CQ.shared.HTTP.externalize(allTeasers[i].path + ".html");
                        if (!allTeasers[i]["segments"] ||
                            allTeasers[i]["segments"].length == 0 ||
                            CQ_Analytics.SegmentMgr.resolveArray(allTeasers[i]["segments"]) === true) {
                            var boost = CQ_Analytics.SegmentMgr.getMaxBoost(allTeasers[i]["segments"]);
                            var params = [allTeasers[i]["title"], boost, allTeasers[i].thumbnail, p];
                            if (teaserPath == allTeasers[i].path) {
                                html += CQ.I18n.getMessage("<b><a href=\"{3}\" class=\"cq-teaser-segment-link\"><img src=\"{2}\" class=\"cq-teaser-decision-thumbnail cq-teaser-decision-match\"></a>Experience: {0} - match ( boost = {1} )</b><br>", params);
                            } else {
                                html += CQ.I18n.getMessage("<a href=\"{3}\" class=\"cq-teaser-segment-link\"><img src=\"{2}\" class=\"cq-teaser-decision-thumbnail cq-teaser-decision-match\"></a>Experience: {0} - match ( boost = {1} )<br>", params);
                            }

                            if (boost == lastBoost) {
                                //same boost, add to list
                                teasers.push(allTeasers[i]);
                            } else {
                                if (boost > lastBoost) {
                                    //better boost, clear list and keep only this one
                                    teasers = new Array();
                                    teasers.push(allTeasers[i]);
                                    lastBoost = boost;
                                }
                            }
                        } else {
                            var params = [allTeasers[i]["title"], allTeasers[i].thumbnail, p];
                            html += CQ.I18n.getMessage("<a href=\"{2}\" class=\"cq-teaser-segment-link\"><img src=\"{1}\" class=\"cq-teaser-decision-thumbnail cq-teaser-decision-nomatch\"></a>Experience: {0} - no match<br>", params);
                        }
                    }
                }
                html += CQ.I18n.getMessage("<br>Strategy <b>{0}</b> selected current teaser.<br>", strategyName);
                return html;
            };

            var currentVisibleTeaser = null;
            var ttip = null;
            //function which chooses and loads a teaser.
            var loadTeasers = function() {
                var teasers = new Array();
                if (CQ_Analytics.SegmentMgr) {
                    var lastBoost = 0;
                    for (var i = 0; i < allTeasers.length; i++) {
                        if (!allTeasers[i]["segments"] ||
                            allTeasers[i]["segments"].length == 0 ||
                            CQ_Analytics.SegmentMgr.resolveArray(allTeasers[i]["segments"]) === true) {
                            var boost = CQ_Analytics.SegmentMgr.getMaxBoost(allTeasers[i]["segments"]);
                            if (boost == lastBoost) {
                                //same boost, add to list
                                teasers.push(allTeasers[i]);
                            } else {
                                if (boost > lastBoost) {
                                    //better boost, clear list and keep only this one
                                    teasers = new Array();
                                    teasers.push(allTeasers[i]);
                                    lastBoost = boost;
                                }
                            }
                        }
                    }
                }
                if (teasers.length > 0) {
                    // fallback: display first
                    var teaserToShow = teasers[0];
                    if (CQ_Analytics.StrategyMgr) {
                        var teas = CQ_Analytics.StrategyMgr.choose(strategyName, teasers);
                        if (teas != null) {
                            teaserToShow = teas;
                        }
                    }
                    if (!currentVisibleTeaser || currentVisibleTeaser.path != teaserToShow.path) {
                        currentVisibleTeaser = teaserToShow;
                        var url = teaserToShow.path + TEASER_SUFFIX;
                        url = CQ.shared.HTTP.addSelectors(url, CQ.shared.HTTP.getSelectors(window.location.href));
                        CQ_Analytics.Utils.loadTeaserElement(url, targetElementId);

                        if(window.CQ_trackTeasersStats && trackingURL) {
                            if( !CQ_Analytics.loadedTeasersStack) {
                                //store in loadedTeasersStack the list of teasers shown in the page.
                                CQ_Analytics.loadedTeasersStack = [];
                                //on window unload, post
                                $CQ(window).unload(function() {
                                    try {
                                        var loadedTeasers = CQ_Analytics.loadedTeasersStack;
                                        if( loadedTeasers ) {
                                            delete CQ_Analytics.loadedTeasersStack;
                                            //build the URL : trackingURL + paths
                                            var url = trackingURL;
                                            for(var i=0;i<loadedTeasers.length; i++) {
                                                url = CQ.shared.HTTP.addParameter(url,"path",loadedTeasers[i]);
                                            }
                                            //run get in asynch mode.
                                            CQ.shared.HTTP.get(url, function() {});
                                        }
                                    } catch(error) {}
                                });
                            }
                            CQ_Analytics.loadedTeasersStack.push(teaserToShow.path);
                        }

                        if( isEditMode ) {
                            if( editablePath ) {
                                var editable = CQ.WCM.getEditable(editablePath);
                                if( editable) {
                                    if( editable && editable.teaserToolTip ) {
                                        editable.teaserToolTip.hide();
                                        editable.teaserToolTip.remove();
                                        editable.teaserToolTip = null;
                                    } else {
                                        editable.on(CQ.wcm.EditRollover.EVENT_SHOW_HIGHTLIGHT, function(highlight) {
                                            if( ! this.teaserInfoButton ) {
                                                this.teaserInfoButton = CQ.Ext.DomHelper.append('CQ',{
                                                    tag: 'div',
                                                    cls: 'x-tool x-tool-help cq-teaser-tooltip-tool'
                                                }, true);
                                                this.teaserInfoButton.position("absolute");
                                                this.teaserInfoButton.on("click", function() {
                                                    if( !editable.teaserToolTip ) {
                                                        editable.teaserToolTip = new CQ.Ext.Tip({
                                                            "html": computeDecisionHTML(currentVisibleTeaser.path),
                                                            "title": CQ.I18n.getMessage("Selection decision"),
                                                            "width": 450,
                                                            "autoHide": false,
                                                            "closable": true,
                                                            "height": 300,
                                                            "floating": true,
                                                            "autoHeight": false,
                                                            "bodyStyle": "overflow-y: scroll;"
                                                        });
                                                    }
                                                    var pos = this.getXY();
                                                    editable.teaserToolTip.setPosition(pos[0] - 460,pos[1] - 100);
                                                    editable.teaserToolTip.show();
                                                });
                                            }
                                            this.teaserInfoButton.anchorTo(
                                                highlight.frameBottom.getEl(),
                                                "tr",
                                                [-26, -15]);
                                            this.teaserInfoButton.show();
                                        });

                                        editable.on(CQ.wcm.EditRollover.EVENT_HIDE_HIGHTLIGHT, function(highlight) {
                                            if( this.teaserInfoButton) {
                                                this.teaserInfoButton.hide();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if( isEditMode ) {
                        var editable = CQ.WCM.getEditable(editablePath);
                        if( editable && editable.teaserToolTip ) {
                            editable.teaserToolTip.hide();
                            editable.teaserToolTip.remove();
                            editable.teaserToolTip = null;
                        }
                    }
                    CQ_Analytics.Utils.clearElement(targetElementId);
                    currentVisibleTeaser = null;
                }
            };

            loadTeasers.call();

            //loaded teaser might change everytime a segment resolution state changes
            if (CQ_Analytics.SegmentMgr) {
                CQ_Analytics.SegmentMgr.addListener("update", loadTeasers);
            }
        };

        //first teaser load is done when all stores are loaded
        if( CQ_Analytics.CCM.areStoresInitialized) {
            toExecute.call(this);
        } else {
            CQ_Analytics.CCM.addListener("storesinitialize",toExecute);
        }
    }
}
