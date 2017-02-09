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
 * The <code>CQ_Analytics.ProfileDataMgr</code> object is a store providing surfer information, like referral keywords,
 * mouse position and browser details.
 * @class CQ_Analytics.SurferInfoMgr
 * @singleton
 * @extends CQ_Analytics.SessionStore
 */
if (CQ_Analytics.CCM && CQ_Analytics.SurferInfoMgr) {
    CQ_Analytics.CCM.addListener("configloaded", function() {
        //add to std clickstream cloud ui
        CQ_Analytics.ClickstreamcloudUI.register(
                this.getSessionStore(),
                CQ_Analytics.CCM.getUIConfig(this.getName()));
    }, CQ_Analytics.SurferInfoMgr);

    var setI18nProperties = function() {
        var bi = CQ_Analytics.BrowserInfoInstance;
        this.addInitProperty("browserFamily_i18n", bi.getBrowserFamilyI18n());
        this.addInitProperty("browserVersion_i18n", bi.getBrowserVersionI18n());
        this.addInitProperty("browser_i18n", "${/surferinfo/browserFamily_i18n} ${/surferinfo/browserVersion_i18n}");
        this.addInitProperty("OS_i18n", bi.getOSNameI18n());
    };

    setI18nProperties.call(CQ_Analytics.SurferInfoMgr);

}