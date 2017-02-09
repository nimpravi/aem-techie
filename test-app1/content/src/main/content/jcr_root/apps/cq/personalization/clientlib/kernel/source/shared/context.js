/*
 * Copyright 1997-2010 Day Management AG
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
 * @deprecated
 */
if (!window.CQ_Context) {
    window.CQ_Context = function() {};
    window.CQ_Context.prototype = new CQ_Analytics.Observable();

    window.CQ_Context.prototype.getProfile = function() {
        return (function() {
            return {
                /**
                 *
                 */
                getUserId: function() {
                    return this.getProperty("authorizableId");
                },

                /**
                 *
                 */
                getDisplayName: function() {
                    var fn = this.getProperty("formattedName");
                    if( fn ) return fn;

                    fn = this.getProperty("displayName");
                    if( fn ) return fn;

                    //fallback
                    return this.getUserId();
                },

                /**
                 *
                 */
                getFirstname: function() {
                    return this.getProperty("givenName");
                },

                /**
                 *
                 */
                getLastname: function() {
                    return this.getProperty("familyName");
                },

                /**
                 *
                 */
                getEmail: function() {
                    return this.getProperty("email");
                },

                /**
                 *
                 * @param {String} name
                 */
                getProperty: function(name) {
                    if (CQ_Analytics && CQ_Analytics.ProfileDataMgr) {
                        return CQ_Analytics.ProfileDataMgr.getProperty(name);
                    }
                    return "";
                },

                /**
                 *
                 */
                getProperties: function() {
                    if (CQ_Analytics && CQ_Analytics.ProfileDataMgr) {
                        return CQ_Analytics.ProfileDataMgr.getData();
                    }
                    return {};
                },

                /**
                 *
                 */
                getAvatar: function() {
                    return this.getProperty("avatar");
                },

                /**
                 *
                 * @param {Function} fct
                 * @param {Object} scope
                 */
                onUpdate: function(fct, scope) {
                    if (fct && CQ_Analytics && CQ_Analytics.ProfileDataMgr) {
                        CQ_Analytics.ProfileDataMgr.addListener("update",fct,scope || this);
                    }
                }
            }
        })();
    };

    window.CQ_Context = new window.CQ_Context();
}

