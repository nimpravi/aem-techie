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
//initialization of all the analytics objects available in edit mode
CQ.Ext.onReady(function() {
    //link clickstreamcloud editor to clickstreamcloud ui box
    if (CQ_Analytics.ClickstreamcloudUI) {
        CQ_Analytics.ClickstreamcloudUI.addListener("editclick", function() {

            if( !CQ_Analytics.ClickstreamcloudEditor ) {
                //clickstreamcloud editor itself
                CQ_Analytics.ClickstreamcloudEditor = new CQ.personalization.EditableClickstreamcloud();

                //registers the session stores
                var reg = function(mgr) {
                    if (mgr) {
                        var config = CQ_Analytics.ClickstreamcloudMgr.getEditConfig(mgr.getSessionStore().getName());
                        config["sessionStore"] = mgr.getSessionStore();
                        CQ_Analytics.ClickstreamcloudEditor.register(config);
                    }
                };

                //profile data
                reg.call(this, CQ_Analytics.ProfileDataMgr);

                //page data
                reg.call(this, CQ_Analytics.PageDataMgr);

                //tagcloud data
                reg.call(this, CQ_Analytics.TagCloudMgr);

                //surfer info data
                reg.call(this, CQ_Analytics.SurferInfoMgr);
                
                //eventinfodata
                reg.call(this, CQ_Analytics.EventDataMgr);
            }
            CQ_Analytics.ClickstreamcloudEditor.show();
        });

        CQ_Analytics.ClickstreamcloudUI.addListener("loadclick", function() {
            var dlg = new CQ.personalization.ProfileLoader({});
            dlg.show();
        });
    }
});