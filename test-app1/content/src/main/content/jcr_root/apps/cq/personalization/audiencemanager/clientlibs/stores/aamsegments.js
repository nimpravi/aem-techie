/*******************************************************************************
 *
 * ADOBE CONFIDENTIAL __________________
 *
 * Copyright 2013 Adobe Systems Incorporated All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Adobe Systems Incorporated and its suppliers, if any. The intellectual and
 * technical concepts contained herein are proprietary to Adobe Systems
 * Incorporated and its suppliers and are protected by trade secret or copyright
 * law. Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from Adobe
 * Systems Incorporated.
 ******************************************************************************/
CQ_Analytics = window.CQ_Analytics || {};
CQ_Analytics.AAM = CQ_Analytics.AAM || {};

/**
 * The segment manager manages segments and their refresh after traits have been
 * updated on the Audience Manager.
 */
CQ_Analytics.AAM.SegmentsMgr = CQ_Analytics.AAM.SegmentsMgr ||
        function(audienceManagerInstance, config) {
            "use strict";

            // using jQuery/closure code style rather than prototype to hide internal private methods.
            // see http://javascript.crockford.com/private.html

            function newAAMSegmentsMgr() {


                // save configuration
                var storename = config.store_name || "aamsegments";
                var debugMessages = config.debug || false;
                var segmentationUrl = "/etc/segmentation/aam.infinity.json";
                var audienceManager = audienceManagerInstance;

                /**
                 * Contains the segments for aam.
                 */
                var cachedSegmentInfo = null;

                /**
                 * When the control needs re-rendering, set to true.
                 */
                var needsUpdate = true;



                /**
                 * Template for the chooser
                 */
                var displayTemplate = function(key, label) {
                    return "<span class='aamsegments-name' >" + label + "</span>";
                };

                // create a new instance from the "factory". (this is the super() call).
                var newStore = CQ_Analytics.JSONStore.getInstance(storename, null, null, function() {
                    this.init();
                    this.reset();
                });

                var debug = function() {};
                if ( debugMessages ) {
                    debug = function(msg) {
                        console.log("DEBUG: aamsegments.js "+msg);
                    };
                }
                var error = function(msg) {
                    console.log("ERROR: aamsegments.js "+msg);
                };

                debug("Created newStore as " + newStore);

                /**
                 * Recursively add segments to the segment info cache, indexed
                 * by segment ID.
                 * @private
                 * @param segmentFolder an object that could be a segment or segment folder.
                 * @return map of segments by ID, each value containing an object with title, key and content.
                 */
                function addSegments(segmentFolder) {
                    var segments = {};
                    if (segmentFolder['jcr:content']) {
                        if (segmentFolder['jcr:content'].aam_sid) {
                            debug("Got segment " + segmentFolder['jcr:content'].aam_sid);
                            // segment
                            segments[segmentFolder['jcr:content'].aam_sid] = {
                                title : segmentFolder['jcr:content']['jcr:title'],
                                key : segmentFolder['jcr:content'].aam_sid,
                                content : segmentFolder['jcr:content']
                            };
                        } else {
                            debug("In Folder " + segmentFolder['jcr:content'].aam_path);
                        }
                    }
                    if ("object" === typeof segmentFolder) {
                        // recurse into anything that is not jcr:content
                        $.each(segmentFolder, function(k, v) {
                            if (k !== "jcr:content") {
                                // use underscore.js to merge the maps.
                                segments = $.extend(segments, addSegments(v));
                            }
                        });
                    }
                    return segments;
                }




                /**
                 * render the controls
                 * @param store the store being rendered.
                 * @param divId. The ID of the div where the store should be rendered.
                 * @returns void
                 */
                function internalRenderer(store, divId) {
                    if (needsUpdate) {
                        needsUpdate = false;
                        var segmentDiv = $("#" + divId);
                        // its possible that div might not exist, if the user has chosen not to add a Segments display.
                        // the ClientContext store will still be updated.
                        if (segmentDiv) {
                            segmentDiv.children().remove();
                            $.each(audienceManager.getUserSegments(), function(key, value) {
                                if (value && cachedSegmentInfo[key]) {
                                    var segInfo = cachedSegmentInfo[key];
                                    segmentDiv.prepend(displayTemplate(key, segInfo.title));
                                }
                            });
                        }
                    }
                }

                /**
                 * Get a copy of all segments known to CQ for this partner. This
                 * could be big, but should not be an issue.
                 * @param callback a callback function, no params.
                 * @returns void.
                 * @private
                 */
                function loadSegmentInfo(callback) {
                    debug("Loading Segment Info " + newStore + " with callback " + callback);
                    if (cachedSegmentInfo) {
                        callback();
                    } else {
                        debug("Get segment info from " + segmentationUrl);
                        cachedSegmentInfo = cachedSegmentInfo || {};
                        // this could be a lot of data and might need to
                        // replace it with a service.
                        $.getJSON(segmentationUrl, function(data) {
                            cachedSegmentInfo = $.extend(cachedSegmentInfo, addSegments(data));
                            callback();
                        }).fail(function(jqXHR, textStatus, errorThrown){
                            error("failed to load segment info from "+segmentationUrl+" cause "+textStatus);
                            callback();
                        });
                    }
                }

                // connect to audience manager to be notified when its updated.
                audienceManager.addListener("update", function() {
                    needsUpdate = true;
                    newStore.fireEvent("update");
                });


                // bind the public methods to new store.
                newStore.renderer = internalRenderer;


                loadSegmentInfo(function() {
                    // register the store once its fully loaded and configured
                    // Wait for configuration to complete

                    CQ_Analytics.CCM.register(newStore);
                });
                debug("New Segment Manager created " + newStore);

                return newStore;

            } // end of constructor.



            return newAAMSegmentsMgr();

        };

