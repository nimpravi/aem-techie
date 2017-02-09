/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2013 Adobe Systems Incorporated
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
 **************************************************************************/

CQ_Analytics.Engine = function() {

    window.CQ_trackTeasersStats = true;

    function isEditMode() {
        // ensure boolean return value
        return !!( window.CQ && CQ.WCM && CQ.WCM.isEditMode() );
    }

    /**
     * Returns a jQuery promise for an editable given its path.
     * During page load, editables might not be available right away, or it is
     * known that this editable will be created and one wants to listen for it.
     * This promise will resolve when the editable is present, with the editable
     * as argument.
     *
     * @param path the editable path
     * @returns {*} jQuery promise object - use promise.done(function(editable) {...}) to handle it
     */
    function getEditablePromise(path) {
        var deferred = $CQ.Deferred();
        var editable = CQ.WCM.getEditable(path);
        if (editable) {
            // already available, can resolve right away
            deferred.resolve(editable);
        } else {
            // not ready yet, extjs still loading, need to hook into editableready event
            CQ.WCM.onEditableReady(path, function(editable) {
                deferred.resolve(editable);
            });
        }
        return deferred.promise();
    }

    function initTracking(teaser, trackingURL) {
        if (!CQ_Analytics.loadedTeasersStack) {
            // store in loadedTeasersStack the list of teasers shown in the page.
            CQ_Analytics.loadedTeasersStack = [];
            // on window unload, post
            $CQ(window).unload(function() {
                try {
                    var loadedTeasers = CQ_Analytics.loadedTeasersStack;
                    if (loadedTeasers) {
                        delete CQ_Analytics.loadedTeasersStack;
                        //build the URL : trackingURL + paths
                        var url = trackingURL;
                        for (var i=0; i < loadedTeasers.length; i++) {
                            url = CQ.shared.HTTP.addParameter(url, "path", loadedTeasers[i]);
                        }
                        //run get in asynch mode.
                        CQ.shared.HTTP.get(url, function() {});
                    }
                } catch(error) {}
            });
        }
        CQ_Analytics.loadedTeasersStack.push(teaser.path);
    }

    /**
     * Gives an HTML text describing how selection was done based on the decisionInfo etc.
     */
    function buildDecisionHTML(decisionInfo, winnerPath, strategy) {
        var html = "", text;

        function getHTML(text, url, thumbnail, match) {
            return '<a href="' + url + '" class="cq-teaser-segment-link">' +
                       '<img src="' + thumbnail + '" class="cq-teaser-decision-thumbnail ' + (match ? 'cq-teaser-decision-match' : 'cq-teaser-decision-nomatch') + '">' +
                   '</a>' + text + '<br>';
        }

        for (var i = 0; i < decisionInfo.length; i++) {
            var info = decisionInfo[i];

            var path = CQ.shared.HTTP.externalize(info.teaser.path + ".html");

            if (info.hasOwnProperty("boost")) {
                // match
                if (info.noSegment) {
                    text = CQ.I18n.getMessage("Experience: {0} - match (no segments, boost = {1})", [info.teaser.title, info.boost]);
                } else {
                    text = CQ.I18n.getMessage("Experience: {0} - match (boost = {1})", [info.teaser.title, info.boost]);
                }
                var item = getHTML(text, path, info.teaser.thumbnail, true);
                if (winnerPath === info.teaser.path) {
                    html += "<b>" + item + "</b>";
                } else {
                    html += item;
                }
            } else {
                // no match
                if (info.unknownSegment) {
                    text = CQ.I18n.getMessage("Experience: {0} - no match (unknown segment)", [info.teaser.title]);
                } else {
                    text = CQ.I18n.getMessage("Experience: {0} - no match", [info.teaser.title]);
                }
                html += getHTML(text, path, info.teaser.thumbnail, false);
            }
        }

        html += "<br>";

        if (strategy) {
            html += CQ.I18n.getMessage("Strategy <b>{0}</b> selected current teaser.", strategy );
        } else {
            html += CQ.I18n.getMessage("No strategy configured, used the first match.");
        }

        html += "<br>";

        return html;
    }

    /**
     * Sets up the tooltip explaining the teaser selection.
     */
    function setDecisionTooltip(editablePromise, decisionInfo, winnerPath, strategy) {
        editablePromise.done(function(editable) {
            if ( editable.teaserToolTip ) {
                editable.teaserToolTip.hide();
                editable.teaserToolTip.remove();
            }

            editable.teaserToolTip = new CQ.Ext.Tip({
                "html": buildDecisionHTML(decisionInfo, winnerPath, strategy),
                "title": CQ.I18n.getMessage("Selection decision"),
                "width": 450,
                "autoHide": false,
                "closable": true,
                "height": 300,
                "floating": true,
                "autoHeight": false,
                "bodyStyle": "overflow-y: scroll;"
            });

            editable.on(CQ.wcm.EditRollover.EVENT_SHOW_HIGHTLIGHT, function(highlight) {
                if ( !this.teaserInfoButton ) {
                    this.teaserInfoButton = CQ.Ext.DomHelper.append('CQ',{
                        tag: 'div',
                        cls: 'x-tool x-tool-help cq-teaser-tooltip-tool'
                    }, true);
                    this.teaserInfoButton.position("absolute");
                    this.teaserInfoButton.on("click", function() {
                        var pos = this.getXY();
                        editable.teaserToolTip.setPosition(pos[0] - 460, pos[1] - 100);
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
                if (this.teaserInfoButton) {
                    this.teaserInfoButton.hide();
                }
            });
        });
    }

    /**
     * Removes the tooltip explaining the teaser selection.
     */
    function removeDecisionTooltip(editablePromise) {
        editablePromise.done(function(editable) {
            if ( editable.teaserToolTip ) {
                editable.teaserToolTip.hide();
                editable.teaserToolTip.remove();
                editable.teaserToolTip = null;
            }
        });
    }

    function resolveTeaserCandidates(teasers, decisionInfo) {
        var candidates = [];
        var lastBoost = 0;

        for (var i = 0; i < teasers.length; i++) {
            var teaser = teasers[i],
                segments = teaser.segments;

            var info;
            if (decisionInfo) {
                info = {
                    teaser: teaser
                };
                decisionInfo.push(info);
            }

            // teaser becomes a candidate if:
            // - it does not specify a segment (= applies to everyone)
            // - its segments can be resolved (= are currently active)
            // - at least one of its segments has the highest boost overall
            var match = !segments || segments.length === 0;

            if (match && info) {
                info.noSegment = true;
            }

            // check if segment(s) evaluate
            if (!match && CQ_Analytics.SegmentMgr.resolve(segments)) {
                match = true;
                // HACK: to avoid changing SegmentMgr.resolve()
                // if segment is unkown (= outside configured segmentPath), SegmentMgr currently
                // sees it as a match, but we don't want this here, it's very confusing, because
                // this gives a lot of false matches
                if (segments && segments.length > 0) {
                    if (!CQ_Analytics.SegmentMgr.segments[segments[0]]) {
                        match = false;
                        if (info) {
                            info.unknownSegment = true;
                        }
                    }
                }
            }

            if (match) {
                // handle boost
                var boost = CQ_Analytics.SegmentMgr.getMaxBoost(segments);

                if (info) {
                    info.boost = boost;
                }

                if (boost === lastBoost) {
                    // same boost, add to list
                    candidates.push(teaser);
                } else {
                    if (boost > lastBoost) {
                        // better boost, clear list and keep only this one
                        candidates = [];
                        candidates.push(teaser);
                        lastBoost = boost;
                    }
                }
            }

        }
        return candidates;
    }

    // stores functions by editable path
    var teaserListeners = {};

    function trackTeaserLoader(editablePath, fn) {
        // first make make sure an existing one is gone (precautious)
        CQ_Analytics.Engine.stopTeaserLoader(editablePath);

        teaserListeners[editablePath] = fn;
    }

    return {

        /**
         * Stops the teaser loader for the given editable (or editable path) to run
         * (it runs continuously when client context simulation is active).
         * @param editable editable object or path
         */
        stopTeaserLoader: function(editable) {
            var editablePath = editable.path || editable;

            if (!editablePath) {
                return;
            }
            var listener = teaserListeners[editablePath];
            if (listener) {
                CQ_Analytics.SegmentMgr.removeListener("update", listener);
                delete teaserListeners[editablePath];
            }
        },

        /**
         * Resolves a teaser to display.
         * @param {Array} teasers all teaser candidates
         * @param {String} strategy name of a strategy (optional)
         * @param {Array} decisionInfo empty array that will be filled with the list of teaser candidates and their evaluated boost (optional)
         * @returns {Object} the resolved teaser or null
         */
        resolveTeaser: function(teasers, strategy, decisionInfo) {
            // select the candidates based on the segments and boost
            var candidates = resolveTeaserCandidates(teasers, decisionInfo);
            if (candidates.length === 0) {
                return null;
            }
            // let strategy find the final teaser or fall back to first
            return CQ_Analytics.StrategyMgr.choose(strategy, candidates) || candidates[0];
        },

        /**
         * Initializes the teaser loader for a given element. Selects a teaser from a list
         * (dependending on the given strategy) and will to load choosen teaser content into a DOM element.
         * Also allows for overriding the automatic teaser selection for simulation purposes.
         *
         * @param {Object} options map of options
         * @param {String} options.targetID       ID of DOM element on which to insert choosen teaser
         * @param {Array}  options.teasers        Complete list of available teasers
         * @param {String} [options.strategy]     Name of the selection strategy (must be availabe in CQ_Analytics.StrategyManager)
         * @param {String} [options.trackingURL]  URL of the tracking service for teaser impressions (if window.CQ_trackTeasersStats)
         */
        loadTeaser: function(options) {
            // in wcm authoring mode, we need to get our editable
            var editable, editablePath;
            if (isEditMode()) {
                editablePath = CQ.WCM.getEditablePathFromDOM(document.getElementById(options.targetID));
                editable = getEditablePromise(editablePath);
            }

            var campaignStore = ClientContext.get("campaign");
            if (campaignStore && campaignStore.isCampaignSelected()) {
                return;
            }

            var toExecute = function() {
                // Simulation: Override the normal teaser display if
                // the PageDataMgr has an experience set.
                var forceExp = CQ_Analytics.PageDataMgr.getExperience();
                if (forceExp) {
                    CQ_Analytics.PageDataMgr.clearExperience();
                    var TEASER_SUFFIX = "/_jcr_content/par.html";
                    if ( isEditMode() ) {
                        TEASER_SUFFIX += "?wcmmode=disabled";
                    }
                    CQ_Analytics.Utils.loadElement(forceExp + TEASER_SUFFIX, options.targetID);
                    return;
                }

                var currentTeaser = null;

                // function which chooses and loads a teaser.
                var loadTeasers = function() {
                    var decisionInfo = null;
                    if (isEditMode()) {
                        decisionInfo = [];
                    }
                    var teaserToShow = CQ_Analytics.Engine.resolveTeaser(options.teasers, options.strategy, decisionInfo);
                    if (teaserToShow) {
                        if (!currentTeaser || currentTeaser.path !== teaserToShow.path) {
                            currentTeaser = teaserToShow;

                            var url = teaserToShow.url;
                            if ( isEditMode() ) {
                                url += "?wcmmode=disabled";
                            }
                            CQ_Analytics.Utils.loadTeaserElement(url, options.targetID);

                            if (window.CQ_trackTeasersStats && options.trackingURL) {
                                initTracking(teaserToShow, options.trackingURL);
                            }

                            if ( editable ) {
                                setDecisionTooltip(editable, decisionInfo, currentTeaser.path, options.strategy);
                            }
                        }
                    } else {
                        if ( editable ) {
                            removeDecisionTooltip(editable);
                        }
                        CQ_Analytics.Utils.clearElement(options.targetID);
                        currentTeaser = null;
                    }
                };

                loadTeasers.call();

                //loaded teaser might change everytime a segment resolution state changes
                if (CQ_Analytics.SegmentMgr) {
                    if (editablePath) {
                        // keep track of the listener to allow the TargetEditor to disable it
                        // when it takes over control of teaser/experience display
                        trackTeaserLoader(editablePath, loadTeasers);
                    }
                    CQ_Analytics.SegmentMgr.addListener("update", loadTeasers);
                }
            };

            // first teaser load is done when all stores are loaded
            if (CQ_Analytics.CCM.areStoresInitialized) {
                toExecute.call(this);
            } else {
                CQ_Analytics.CCM.addListener("storesinitialize", toExecute);
            }
        }
    };
}();
