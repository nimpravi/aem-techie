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
 * Integration point with AudienceManager endpoint.
 * This either runs in simulation mode in conjunction with the SegmentMgr or it runs in publish mode and calls
 * the demdex end point. It is configured with the following properties on construction.
 *
 * config.partner  the name of the audience manager partner, defaults to geometrixx
 * config.desinationNames  a map of maps defining destinations, keyed by the destination name.
 *                         each submap contains:
 *                            domain, the domain the key exists in
 *                            segkey, the parameter key for segments ids (optional defaults to segs)
 *                            keysep, the character used to separate keys (optional defaults to ,)
 *                            valsep, the character user to separate values (optional defaults to ;)
 *                         using defaults, destination values of segs=123,segs=32423  or segs=342;32432;234 represent
 *                         segmentids.
 * config.simulationPath  if present the CQ server will be used for simulation, if not, the live Audience Manager endpoint will be used.
 * config.containerNSID the audience manager container to use.
 *
 */
CQ_Analytics.AAM.AudienceMgr = CQ_Analytics.AAM.AudienceMgr ||
    function(config) {
        "use strict";

        var partner = config.partner || 'geometrixx';
        var destinationNames = config.destinationNames || {
                "CQIntegrationDestination" : {
                    domain : ".cqclientintegration.adobe.com",
                    segkey : "segs",
                    keysep : ",",
                    valsep : ";"
                }
            };
        var debugMessages = config.debug || false;

        var containerNSID = config.containerNSID || "0";

        // server url used to resolve traits into segments.
        var resolveSegmentsUrl = false;
        if ( config.simulationPath ) {
            resolveSegmentsUrl = config.simulationPath + ".segments.json";
        }



        var debug = function() {};
        if ( debugMessages ) {
            debug = function(msg) {
                console.log("DEBUG: audiencemanager.js "+msg);
            };
        }
        var error = function(msg) {
            console.log("ERROR: audiencemanager.js "+msg);
        };

        debug("Initialise Audience Manager");

        var audienceManagerUserSegments = {};

        var newStore = new CQ_Analytics.JSONStore();


        var demdexEndpoint = "http://";
        if ( "https:" == document.location.protocol ) {
            demdexEndpoint = "https://";
        }
        // the =? at the end is a placeholder for jQuery jsonp. Do not remove.
        demdexEndpoint += encodeURIComponent(partner)+".demdex.net/event?d_cb=?";

        /**
         *
         * Parse the destination response. 2 forms are accepted.
         * segs=23423;23432;23423;23423;234
         * or segs=23423,segs=23432,segs=234234
         * , and ; may be specified.
         * <pre>
         * {
         *    "dests":[
         *       {
         *          "id":"123-1354180261",
         *          "y":"img",
         *          "c":"http://<some destination URL>"
         *       },
         *       {
         *          "id":"1934-1234567899",
         *          "y":"img",
         *          "c":"http://<another destionation URL>"
         *       }
         *    ],
         *    "stuff":[
         *       {
         *          "cn":"aam_tnt",
         *          "cv":"segs=14612,14623",
         *          "ttl":0,
         *          "dmn":"www.my_domain.com",
         *          "u":"abc123"
         *       },
         *       {
         *          "cn":"aam_xyz",
         *          "cv":"segs=14612,14623",
         *          "ttl":0,
         *          "dmn":"www.my_domain.com",
         *          "u":"abc123"
         *       }
         *    ],
         *    "uuid":"abc123"
         * }
         * </pre>
         *
         */
        function parseDestinationResponse(stuff) {
            var segments = {};
            $.each(stuff, function(index, value) {
                // is there cn value present in the map of names.
                if ( destinationNames[value.cn] ) {
                    // does the domain match.
                    var dest = destinationNames[value.cn];
                    if ( value.dmn.slice(0,dest.domain.length) === dest.domain) {
                        var destn = (dest.segkey || "segs") + "=" ;
                        var destl = destn.length;
                        var keysep = dest.keysep || ",";
                        var valsep = dest.valsep || ";";
                        // extract the terms. in the form segs=213;234;234 or segs=23423,segs=23423
                        $.each(value.cv.split(keysep), function( index, seg) {
                            if ( seg.slice(0,destl) === destn ) {
                                $.each(seg.substring(destl).split(valsep), function(index, segv) {
                                    segments[segv] = true;
                                });
                            }
                        });
                    }
                }
            });
            return segments;
        }

        /**
         * An empty callback.
         */
        function emptyCallback() {
        }

        /**
         * set the new segments, and update the store property representing the new segments if required.
         * @param segments a map of segments keyed by id.
         */
        function setUserSegments(segments) {
            audienceManagerUserSegments = {};
            var ids = [];
            $.each(segments, function(key, value){
                if ( value ) {
                    audienceManagerUserSegments[key] = true;
                    ids.push(key);
                }
            });
            ids.sort();
            // this should trigger updates with anything in the client context that is listening.
            // putting a single property in, avoids multiple updates.
            var current = newStore.getProperty("segments");
            var newIds = ids.join(",");
            if ( current !== newIds ) {
                newStore.setProperty("segments", newIds);
                debug("Set segments to "+newIds);
            }
        }

        /**
         * Invoke the end point with new signals and call the provided callback when complete.
         * @param signals map of signals to send, if the key is sid it will be send as AAM traits.
         * @param callbacl an optional callback function.
         */
        function invoke(signals, callback) {
            callback = callback || emptyCallback;
            if ( resolveSegmentsUrl ) {
                // perform a call to the CQ server in simulation mode.
                if ( !signals.sid ) {
                    callback();
                    newStore.fireEvent("update");
                } else {
                    $.getJSON(resolveSegmentsUrl, { t : signals.sid }, function(response) {
                        if ( response.segments ) {
                            setUserSegments(response.segments);
                        }
                        callback();
                        newStore.fireEvent("update");
                    }).fail(function(jqXHR, textStatus, errorThrown) {
                        error("Failed to resolve segments from AAM server  "+textStatus+" error "+errorThrown);
                        callback();
                        newStore.fireEvent("update");
                    });
                }
            } else {
                var data = {};
                signals = signals || {};
                $.each(signals, function(key, value) {
                   if ( key === "sid" ) {
                       data.d_sid = value;
                   } else {
                       data["c_"+key] = value;
                   }
                });
                data.d_nsid = containerNSID;
                data.d_rtbd = "json";
                $.jsonp({
                    url : demdexEndpoint,
                    callback : "__aaminvoke",
                    data : data,
                    success : function(json, textStatus, xOptions) {
                        // parse the response extracting segments from all configured destinations.
                        if ( json.stuff ) {
                            setUserSegments(parseDestinationResponse(json.stuff));
                        }
                        callback();
                        newStore.fireEvent("update");
                    },
                    error : function(xOptions, textStatus) {
                        error("Failed to retieve json response "+textStatus);
                        callback();
                        newStore.fireEvent("update");
                    }
                });
            }
        }

        /**
         * get the AAM segments for the user.
         */
        function getUserSegments() {
            return audienceManagerUserSegments;
        }


        /**
         * @param segmentId the AAM segment id.
         * @returns true if the AAM segment identified by segmentId is present.
         */
        function checkSegmentMatch(segmentId) {
            return (audienceManagerUserSegments[segmentId]);
        }


        invoke(false, function() {
            CQ_Analytics.ClientContextMgr.register(newStore);
        });

        // make some functions public.
        newStore.getUserSegments = getUserSegments;
        newStore.matches = checkSegmentMatch;
        newStore.invoke = invoke;
        return newStore;
    };