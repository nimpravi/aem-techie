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
 * The <code>BrowserInfoInstance</code> object is a singleton providing utility methods to retrieve client browser information.
 * @class CQ_Analytics.BrowserInfoInstance
 * @singleton
 */
if( CQ_Analytics.BrowserInfoInstance ) {
    //mapping between a browser version text and its translation. Note that not all version text needs to be translated
    CQ_Analytics.BrowserInfoInstance.versionI18nMapping = {
        "7 or higher": CQ.I18n.getMessage("{0} or higher", 7, "{0} is a placeholder for browser version, ex: 7 or higher"),
        "10 or higher": CQ.I18n.getMessage("{0} or higher", 10, "{0} is a placeholder for browser version, ex: 7 or higher"),
        "11 or higher": CQ.I18n.getMessage("{0} or higher", 11, "{0} is a placeholder for browser version, ex: 7 or higher"),
        "Unresolved": CQ.I18n.getMessage("Unresolved", null, "Browser version unresolved")
    };

    //mapping between a browser family text and its translation. Note that not all version text needs to be translated
    CQ_Analytics.BrowserInfoInstance.familyI18nMapping = {
        "Unresolved": CQ.I18n.getMessage("Unresolved", null, "Browser family unresolved")
    };

    //mapping between an OS text and its translation. Note that not all version text needs to be translated
    CQ_Analytics.BrowserInfoInstance.osI18nMapping = {
        "Unresolved": CQ.I18n.getMessage("Unresolved", null, "Operating system unresolved")
    };

    /**
     * Returns the browser version. I18n of the possible parts.
     * @return {String} Browser version.
     */
    CQ_Analytics.BrowserInfoInstance.getBrowserVersionI18n = function() {
        return CQ_Analytics.BrowserInfoInstance.versionI18nMapping[this.getBrowserVersion()]
            || this.getBrowserVersion();
    };

    /**
     * Returns the browser family. I18n of the possible parts.
     * @return {String} Browser family.
     */
    CQ_Analytics.BrowserInfoInstance.getBrowserFamilyI18n = function() {
        return CQ_Analytics.BrowserInfoInstance.familyI18nMapping[this.getBrowserFamily()]
            || this.getBrowserFamily();
    };

    /**
     * Returns the operating system name. I18n of the possible parts.
     * @return {String} OS name.
     */
    CQ_Analytics.BrowserInfoInstance.getOSNameI18n = function() {
        return CQ_Analytics.BrowserInfoInstance.osI18nMapping[this.getOSName()]
            || this.getOSName();
    };

    /**
     * Returns the browser name. I18n of the possible parts.
     * @return {String} Browser name.
     */
    CQ_Analytics.BrowserInfoInstance.getBrowserNameI18n = function() {
        return this.getBrowserFamilyI18n() + " " + this.getBrowserVersionI18n();
    };
}