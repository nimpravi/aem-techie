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

if( !CQ_Analytics.MobileSliderUtils ) {
    /**
     * A helper class providing a set of utility methods for the mobile slider.
     * <br>
     * @static
     * @singleton
     * @class CQ_Analytics.MobileSliderUtils
     * @since 5.5
     */
    CQ_Analytics.MobileSliderUtils = function() {
        return {
            /**
             * Injects a CSS into the DOM.
             * @param {String} url CSS URL
             */
            injectCss: function(url) {
                $CQ("head").append("<link>");
                var css = $CQ("head").children(":last");
                css.attr({
                    rel:  "stylesheet",
                    type: "text/css",
                    href: CQ.shared.HTTP.externalize(url)
                });
            },

            /**
             * Removes a CSS from the DOM.
             * @param {String} url CSS URL
             */
            removeCss: function(url) {
                $CQ("[href='"+CQ.shared.HTTP.externalize(url)+"']").remove();
            },

            /**
             * Switches the UI to the mobile version: injects mobile CSS/classes and removes desktop CSS/classes.
             * @param {String} app Name of the application (used to find config).
             */
            switchToMobile: function(app) {
                this.injectMobileBodyClass(app);
                this.injectMobileCss(app);
            },

            /**
             * Switches the UI to the desktop version: injects desktop CSS/classes and removes mobile CSS/classes.
             * @param {String} app Name of the application (used to find config).
             */
            switchToDesktop: function(app) {
                this.injectDesktopBodyClass(app);
                this.injectDesktopCss(app);
            },

            /**
             * Injects desktop CSS and removes mobile CSS.
             * @param {String} app Name of the application (used to find config).
             */
            injectDesktopCss: function(app) {
                var cssList = this.getConfig(app, "DESKTOP_CSS");
                if( cssList ) {
                    for(var i=0;i<cssList.length;i++) {
                        var css = cssList[i];
                        CQ_Analytics.MobileSliderUtils.injectCss(CQ_Analytics.Variables.replace(css, "app", app));
                    }
                }

                cssList = this.getConfig(app, "MOBILE_CSS");
                if( cssList ) {
                    for(var i=0;i<cssList.length;i++) {
                        var css = cssList[i];
                        CQ_Analytics.MobileSliderUtils.removeCss(CQ_Analytics.Variables.replace(css, "app", app));
                    }
                }
            },

            /**
             * Injects mobile CSS and removes mobile CSS.
             * @param {String} app Name of the application (used to find config).
             */
            injectMobileCss: function(app) {
                var cssList = this.getConfig(app, "MOBILE_CSS");
                if( cssList ) {
                    for(var i=0;i<cssList.length;i++) {
                        var css = cssList[i];
                        CQ_Analytics.MobileSliderUtils.injectCss(CQ_Analytics.Variables.replace(css, "app", app));
                    }
                }

                cssList = this.getConfig(app, "DESKTOP_CSS");
                if( cssList ) {
                    for(var i=0;i<cssList.length;i++) {
                        var css = cssList[i];
                        CQ_Analytics.MobileSliderUtils.removeCss(CQ_Analytics.Variables.replace(css, "app", app));
                    }
                }
            },

            /**
             * Injects mobile classes on the body and removes desktop classes from the body.
             * @param {String} app Name of the application (used to find config).
             */
            injectMobileBodyClass: function(app) {
                var classList = this.getConfig(app, "MOBILE_BODY_CLASS");
                if( classList ) {
                    for(var i=0;i<classList.length;i++) {
                        var c = classList[i];
                        $CQ(document.body).addClass(c);
                    }
                }

                classList = this.getConfig(app, "DESKTOP_BODY_CLASS");
                if( classList ) {
                    for(var i=0;i<classList.length;i++) {
                        var c = classList[i];
                        $CQ(document.body).removeClass(c);
                    }
                }
            },

            /**
             * Injects desktop classes on the body and removes mobile classes from the body.
             * @param {String} app Name of the application (used to find config).
             */
            injectDesktopBodyClass: function(app) {
                var classList = this.getConfig(app, "DESKTOP_BODY_CLASS");
                if( classList ) {
                    for(var i=0;i<classList.length;i++) {
                        var c = classList[i];
                        $CQ(document.body).addClass(c);
                    }
                }

                classList = this.getConfig(app, "MOBILE_BODY_CLASS");
                if( classList ) {
                    for(var i=0;i<classList.length;i++) {
                        var c = classList[i];
                        $CQ(document.body).removeClass(c);
                    }
                }
            },

            /**
             * Returns a mobile slider config property. See {@link #MobileSliderUtils.CONFIG CONFIG}.
             * @param {String} app Name of the application (used to find config)
             * @param {String} property Name of the property
             */
            getConfig: function(app, property) {
                var config = CQ_Analytics.MobileSliderUtils.CONFIG[app];
                if( !config ) return null;

                return CQ_Analytics.MobileSliderUtils.CONFIG[app][property];
            }
        }
    }();

    /**
     * Mobile slider config per application. Sample config:
     * <pre><code>
window.CQMobileSlider["geometrixx-outdoors"] = {
    //CSS used by desktop that need to be removed when mobile
    DESKTOP_CSS: [
        "/etc/designs/${app}/clientlibs_desktop_v1.css"
    ],

    //CSS used by mobile that need to be removed when desktop
    MOBILE_CSS: [
        "/etc/designs/${app}/clientlibs_mobile_v1.css"
    ],

    //id of the content that needs to be removed when mobile
    DESKTOP_MAIN_ID: "main",

    //id of the content that needs to be removed when desktop
    MOBILE_MAIN_ID: "main",

    //body classes used by desktop that need to be removed when mobile
    DESKTOP_BODY_CLASS: [
        "page"
    ],

    //body classes used by mobile that need to be removed when desktop
    MOBILE_BODY_CLASS: [
        "page-mobile"
    ]
};
     * </code></pre>
     * @static
     * @type Object
     */
    CQ_Analytics.MobileSliderUtils.CONFIG = window.CQMobileSlider || {};
}