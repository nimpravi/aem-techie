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
if( CQ_Analytics.CCM && CQ_Analytics.SegmentMgr) {
    CQ_Analytics.CCM.addListener("configloaded", function() {
        //add to std clickstream cloud ui
        CQ_Analytics.ClickstreamcloudUI.register(
                this.getSessionStore(),
                CQ_Analytics.CCM.getUIConfig(this.getName()));

    }, CQ_Analytics.SegmentMgr);
}