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
/**
 * The <code>CQ_Analytics.PageDataMgr</code> object is a store providing page data information.
 * @class CQ_Analytics.PageDataMgr
 * @extends CQ_Analytics.SessionStore
 */
if (!CQ_Analytics.PageDataMgr) {
    CQ_Analytics.PageDataMgr = function() {};

    CQ_Analytics.PageDataMgr.prototype = new CQ_Analytics.SessionStore();

    /**
     * @cfg {String} STORENAME
     * Store internal name
     * @final
     * @private
     */
    CQ_Analytics.PageDataMgr.prototype.STORENAME = "pagedata";
    
    /**
     * internal name for the setExperience etc cookie.
     * @final
     * @private
     */
    CQ_Analytics.PageDataMgr.prototype.FORCE_EXPERIENCE_COOKIE = "cq-forceexperience";

    //inheritDoc
    CQ_Analytics.PageDataMgr.prototype.init = function() {
        this.data = {};
        for (var p in this.initProperty) {
            this.data[p] = this.initProperty[p];
        }
        this.initialized = true;
        this.fireEvent("initialize",this);
        this.fireEvent("update");
    };

    //inheritDoc
    CQ_Analytics.PageDataMgr.prototype.getLabel = function(name) {
        return name;
    };

    //inheritDoc
    CQ_Analytics.PageDataMgr.prototype.getLink = function(name) {
        return "";
    };

    
    /**
     * Sets a cookie that forces the display of a specific teaser page in a
     * teaser when loading the next page.
     * @param {String} path Path to the teaserpage to show.
     */
    CQ_Analytics.PageDataMgr.prototype.setExperience = function(path) {
        CQ.shared.HTTP.setCookie(CQ_Analytics.PageDataMgr.FORCE_EXPERIENCE_COOKIE, path, "/");
    };
    
    /**
     * Retrieves the content of the force experience cookie. See {@link #setExperience}.
     * If no cookie is set null or "" is returned.
     * @return {Object}
     */
    CQ_Analytics.PageDataMgr.prototype.getExperience = function() {
        return CQ.shared.HTTP.getCookie(CQ_Analytics.PageDataMgr.FORCE_EXPERIENCE_COOKIE, "/");
    };
    
    /**
     * Removes the force experience cookie. See {@link #setExperience}.
     */
    CQ_Analytics.PageDataMgr.prototype.clearExperience = function() {
        CQ.shared.HTTP.clearCookie(CQ_Analytics.PageDataMgr.FORCE_EXPERIENCE_COOKIE, "/");
    };
    
    CQ_Analytics.PageDataMgr = new CQ_Analytics.PageDataMgr();

    CQ_Analytics.CCM.addListener("configloaded", function() {
        this.loadInitProperties(CQ_Analytics.CCM.getInitialData(this.getName()));
        this.init();

        //registers Page Data to clickstreamcloud manager
        CQ_Analytics.CCM.register(this);

    }, CQ_Analytics.PageDataMgr);
}
