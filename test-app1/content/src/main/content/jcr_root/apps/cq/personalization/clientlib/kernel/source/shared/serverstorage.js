/*************************************************************************
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
 **************************************************************************/
/**
 * @class CQ_Analytics.ClientContextMgr.ServerStorage
 */
if( CQ_Analytics.ClientContextMgr && ! CQ_Analytics.ClientContextMgr.ServerStorage ) {
    CQ_Analytics.ClientContextMgr.ServerStorage = function() {
        //posting is by default set false: no stats by default. CQ_Analytics.CCM.startPosting() is required.
        this.posting = false;
        this.initialized = false;
    };

    /**
     * @cfg {Number} POST_MODE_PAGELOAD
     * Page load mode constant: POST on every page load.
     * @final
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.POST_MODE_PAGELOAD = 0x1;

    /**
     * @cfg {Number} POST_MODE_TIMER
     * Timer mode constant: POST defined by an time interval.
     * @final
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.POST_MODE_TIMER = 0x2;

    /**
     * @cfg {Number} POST_MODE_DATAUPDATE
     * Data update mode constant: POST if one session store data is updated.
     * @final
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.POST_MODE_DATAUPDATE = 0x4;

    /**
     * @cfg {Number} POST_TIMER
     * Interval in seconds to POST in POST_MODE_TIMER mode (defaults to 600s).
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.POST_TIMER = 600;

    /**
     * @cfg {Number} POST_PROCESS_TIMER
     * Interval in seconds to check if POST is needed in POST_MODE_TIMER mode (defaults to 60s).
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.POST_PROCESS_TIMER = 60;

    /**
     * @cfg {Number} POST_MODE
     * The POST mode of the clickstreamcloud. Must be a & value of the following properties:<ul>
     * <li>POST_MODE_PAGELOAD: POST on page load</li>
     * <li>POST_MODE_TIMER: POST on timer interval</li>
     * <li>POST_MODE_DATAUPDATE: POST when one session store data is updated</li>
     * </ul>
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.POST_MODE = 0x6;

    /**
     * @cfg {Number} POST_PATH
     * Beginning of the path used by post.
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.POST_PATH = "/var/statistics/";

    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.init = function() {
        if (this.isMode(CQ_Analytics.ClientContextMgr.ServerStorage.POST_MODE_TIMER)) {
            var currentObj = this;
            var func = function() {
                currentObj.timer = window.setInterval(function() {
                    try {
                        var lastPost = parseInt(currentObj.data["lastPost"]);
                        var doPost = false;
                        if (isNaN(lastPost)) {
                            doPost = true;
                        } else {
                            var currentTime = new Date().getTime();
                            if (currentTime > lastPost + CQ_Analytics.ClientContextMgr.ServerStorage.POST_TIMER * 1000) {
                                doPost = true;
                            }
                        }
                    } catch(error) {
                    }
                    if (doPost) {
                        currentObj.post();
                    }
                }, CQ_Analytics.ClientContextMgr.ServerStorage.POST_PROCESS_TIMER * 1000);
            };

            func.call(this);
        }
        this.initialized = true;
    };

    /**
     * Returns if mode is defined.
     * @param {Number} mode Mode to check.
     * @return {Boolean} true if mode is defined.
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.isMode = function(mode) {
        return (CQ_Analytics.CCM.POST_MODE & mode) > 0;
    };

    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.handleStoreRegistration = function(sessionstore) {
        if( ! this.initialized ) {
            this.init();
        }
        if (this.isMode(CQ_Analytics.ClientContextMgr.ServerStorage.POST_MODE_DATAUPDATE)) {
            sessionstore.addListener("persist", function() {
                //if a store has been persisted, call current persist
                CQ_Analytics.ClientContextMgr.ServerStorage.post(sessionstore);
            });
        }
    };

    /**
     * Starts the posting.
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.startPosting = function() {
        this.posting = true;
    };

    /**
     * Stops the posting.
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.stopPosting = function() {
        this.posting = false;
    };

    /**
     * Posts the current clientcontext object to the server (occurs only if posting is started).
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.post = function(storeName, forced) {
        if (this.posting || forced) {
            try {
                var obj = this.getCCMToJCR(storeName);
                var currentTime = CQ_Analytics.Utils.getTimestamp();
                obj["./jcr:primaryType"] = "nt:unstructured";
                obj["./sessionId"] = CQ_Analytics.CCM.getSessionId();
                var url = this.POST_PATH + "clientcontext/";
                if (CQ_Analytics.CCM.isAnonymous()) {
                    var sessionSplit = CQ_Analytics.Utils.insert(CQ_Analytics.CCM.getId(), 2, "/");
                    url += "anonymous/" + sessionSplit + "/" + currentTime;
                } else {
                    url += "users/" + CQ_Analytics.CCM.getId() + "/" + currentTime;
                }
                CQ_Analytics.Utils.post(url, null, obj);
                this.lastPost = currentTime;
            } catch(error) {
            }
        }
    };

    /**
     * Returns the current clientcontext object in "JCR style"
     * o.property = value --> ./property = value
     * o.level1.property = value --> ./level1/property = value
     * 2 levels only
     * @return {Object} object representing the clientcontext  in "JCR style"
     * @private
     */
    CQ_Analytics.ClientContextMgr.ServerStorage.prototype.getCCMToJCR = function(storeName) {
        var obj = CQ_Analytics.CCM.get(true);

        var resObj = {};
        for (var key in obj) {
            if( !storeName || key == storeName ) {
                var ov = obj[key], k = encodeURIComponent(key);
                var type = typeof ov;
                if (type == 'object') {
                    for (var l2key in ov) {
                        var v = ov[l2key];
                        //trick for tags
                        l2key = l2key.replace(":", "/");
                        resObj[ "./" + key + "/./" + l2key ] = v;
                    }
                } else {
                    resObj[ "./" + key] = ov;
                }
            }
        }

        return resObj;
    };

    CQ_Analytics.ClientContextMgr.ServerStorage = new CQ_Analytics.ClientContextMgr.ServerStorage();
    
    //support backward compatibility
    
    /**
     * @deprecated
     * @see CQ_Analytics.ClientContextMgr.ServerStorage
     */
    CQ_Analytics.ClickstreamcloudMgr.POST_MODE_PAGELOAD = CQ_Analytics.ClientContextMgr.ServerStorage.POST_MODE_PAGELOAD;

    /**
     * @deprecated
     * @see CQ_Analytics.ClientContextMgr.ServerStorage
     */
    CQ_Analytics.ClickstreamcloudMgr.POST_MODE_TIMER = CQ_Analytics.ClientContextMgr.ServerStorage.POST_MODE_TIMER;

    /**
     * @deprecated
     * @see CQ_Analytics.ClientContextMgr.ServerStorage
     */
    CQ_Analytics.ClickstreamcloudMgr.POST_MODE_DATAUPDATE = CQ_Analytics.ClientContextMgr.ServerStorage.POST_MODE_DATAUPDATE;

    /**
     * @deprecated
     * @see CQ_Analytics.ClientContextMgr.ServerStorage
     */
    CQ_Analytics.ClickstreamcloudMgr.POST_TIMER = CQ_Analytics.ClientContextMgr.ServerStorage.POST_PROCESS_TIMER;

    /**
     * @deprecated
     * @see CQ_Analytics.ClientContextMgr.ServerStorage
     */
    CQ_Analytics.ClickstreamcloudMgr.POST_PROCESS_TIMER = CQ_Analytics.ClientContextMgr.ServerStorage.POST_PROCESS_TIMER;

    /**
     * @deprecated
     * @see CQ_Analytics.ClientContextMgr.ServerStorage
     */
    CQ_Analytics.ClickstreamcloudMgr.POST_MODE = CQ_Analytics.ClientContextMgr.ServerStorage.POST_MODE;

    /**
     * @deprecated
     * @see CQ_Analytics.ClientContextMgr.ServerStorage
     */
    CQ_Analytics.ClickstreamcloudMgr.POST_PATH = CQ_Analytics.ClientContextMgr.ServerStorage.POST_PATH;
}

