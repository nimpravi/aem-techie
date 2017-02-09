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
// Strategy which random selects a teaser
CQ_Analytics.StrategyMgr.register("random", function(teasers) {
    var random = null;
    if( CQ_Analytics.PageDataMgr ) {
        random = CQ_Analytics.PageDataMgr.getProperty("random");
    }
    if( ! random ) {
        random = window.CQ_StrategyRandom;
    }
    if( ! random ) {
        random = window.CQ_StrategyRandom = Math.random();
    }

    if( parseFloat(random) > 1) {
        random = 1 / random;
    }

    if( parseFloat(random) == 1) {
        random = 0;
    }

    var ranNum = Math.floor(random*teasers.length);
    return teasers[ranNum];
});