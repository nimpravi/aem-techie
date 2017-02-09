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
CQ_Analytics.BrowserInfo = function() {
    var ua = navigator.userAgent.toLowerCase();
    var check = function(r) {
        return r.test(ua);
    };

    var getBrowser = function() {
        if (check(/opera/)) {
            return {
                browserFamily: "Opera",
                browserVersion: ""
            };
        }

        if (check(/chrome/)) {
            return {
                browserFamily: "Chrome",
                browserVersion: ""
            };
        }

        if (check(/safari/)) {
            if (check(/applewebkit\/4/)) { // unique to Safari 2
                return {
                    browserFamily: "Safari",
                    browserVersion: "2"
                };
            }

            if (check(/version\/3/)) {
                return {
                    browserFamily: "Safari",
                    browserVersion: "3"
                };
            }

            if (check(/version\/4/)) {
                return {
                    browserFamily: "Safari",
                    browserVersion: "4"
                };
            }

            if (check(/version\/5/)) {
                return {
                    browserFamily: "Safari",
                    browserVersion: "5"
                };
            }

            if (check(/version\/6/)) {
                return {
                    browserFamily: "Safari",
                    browserVersion: "6"
                };
            }

            return {
                browserFamily: "Safari",
                browserVersion: "7 or higher"
            };
        }

        if (check(/webkit/)) {
            return {
                browserFamily: "WebKit",
                browserVersion: ""
            };
        }

        if (check(/msie/)) {
            if (check(/msie 6/)) {
                return {
                    browserFamily: "IE",
                    browserVersion: "6"
                };
            }

            if (check(/msie 7/)) {
                return {
                    browserFamily: "IE",
                    browserVersion: "7"
                };
            }

            if (check(/msie 8/)) {
                return {
                    browserFamily: "IE",
                    browserVersion: "8"
                };
            }

            if (check(/msie 9/)) {
                return {
                    browserFamily: "IE",
                    browserVersion: "9"
                };
            }

            if (check(/msie 10/)) {
                return {
                    browserFamily: "IE",
                    browserVersion: "10"
                };
            }

            return {
                browserFamily: "IE",
                browserVersion: "11 or higher"
            };
        }

        if (check(/gecko/)) {
            if (check(/rv:1\.8/)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "2"
                };
            }

            if (check(/rv:1\.9/)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "3"
                };
            }

            if (check(/rv:2.0/)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "4"
                };
            }

            if (check(/rv:5./)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "5"
                };
            }

            if (check(/rv:6./)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "6"
                };
            }

            if (check(/rv:7./)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "7"
                };
            }

            if (check(/rv:8./)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "8"
                };
            }

            if (check(/rv:9./)) {
                return {
                    browserFamily: "Firefox",
                    browserVersion: "9"
                };
            }

            return {
                browserFamily: "Firefox",
                browserVersion: "10 or higher"
            };
        }

        var isAir = check(/adobeair/);
        if (isAir) {
            return {
                browserFamily: "Adobe AIR",
                browserVersion: ""
            };
        }

        return {
            browserFamily: "Unresolved",
            browserVersion: "Unresolved"
        };
    };

    var getOS = function() {
        if (check(/windows 98|win98/)) {
            return "Windows 98";
        }

        if (check(/windows nt 5.0|windows 2000/)) {
            return "Windows 2000";
        }

        if (check(/windows nt 5.1|windows xp/)) {
            return "Windows XP";
        }

        if (check(/windows nt 5.2/)) {
            return "Windows Server 2003";
        }

        if (check(/windows nt 6.0/)) {
            return "Windows Vista";
        }

        if (check(/windows nt 6.1/)) {
            return "Windows 7";
        }

        if (check(/windows nt 4.0|winnt4.0|winnt/)) {
            return "Windows NT 4.0";
        }

        if (check(/windows me/)) {
            return "Windows ME";
        }

        if (check(/mac os x/)) {
            if (check(/ipad/) || check(/iphone/)) {
                return "iOS";
            }
            return "Mac OS X";
        }

        if (check(/macintosh|mac os/)) {
            return "Mac OS";
        }

        if (check(/android/)) {
            return "Android";
        }

        if (check(/linux/)) {
            return "Linux";
        }

        return "Unresolved";
    };

    var getDeviceType = function() {
        if (check(/ipad/)) {
            return "iPad";
        }

        if (check(/iphone/)) {
            return "iPhone";
        }

        if (check(/mobi/)) {
            return "Mobile";
        }

        return "Desktop";
    };

    var b = getBrowser.call();
    this.browserFamily = b.browserFamily;
    this.browserVersion = b.browserVersion;

    this.OSName = getOS.call();
    this.deviceType = getDeviceType.call();
    this.ua = ua;

    //protocol
    var isSecure = /^https/i.test(window.location.protocol);

    //resolution
    this.screenResolution = screen.width + "x" + screen.height;
};

CQ_Analytics.BrowserInfo.prototype = {
    /**
     * Returns the browser name.
     * @return {String} Browser name.
     */
    getBrowserName: function() {
        return this.browserFamily + " " + this.browserVersion;
    },

    /**
     * Returns the browser family.
     * @return {String} Browser family.
     */
    getBrowserFamily: function() {
        return this.browserFamily;
    },

    /**
     * Returns the browser version.
     * @return {String} Browser version.
     */
    getBrowserVersion: function() {
        return this.browserVersion;
    },

    /**
     * Returns the operating system name.
     * @return {String} OS name.
     */
    getOSName: function() {
        return this.OSName;
    },

    /**
     * Returns the screen resolution.
     * @return {String} Screen resolution.
     */
    getScreenResolution: function() {
        return this.screenResolution;
    },

    /**
     * Returns the device type.
     * @return {String} Device type.
     */
    getDeviceType: function() {
        return this.deviceType;
    },

    /**
     * Returns the user agent.
     * @return {String} User agent.
     */
    getUserAgent: function() {
        return this.ua;
    },

    /**
     * Returns if the device is a mobile device.
     * @return {Boolean} <code>true</code> if the device is a mobile device.
     */
    isMobile: function(deviceType) {
        if (!deviceType) {
            deviceType = this.getDeviceType();
        }
        deviceType = deviceType ? deviceType.toLowerCase() : "desktop";
        return deviceType != "desktop";
    },

    /**
     * Returns if the browser is Internet Explorer.
     * @return {Boolean}.
     */
    isIE: function(version) {
        return this.getBrowserFamily() == "IE" &&
            (version ? this.getBrowserVersion() == version : true);
    },

    /**
     * Returns if the browser is Internet Explorer 6.
     * @return {Boolean}.
     */
    isIE6: function() {
        return this.isIE("6");
    },

    /**
     * Returns if the browser is Internet Explorer 7.
     * @return {Boolean}.
     */
    isIE7: function() {
        return this.isIE("7");
    },

    /**
     * Returns if the browser is Internet Explorer 8.
     * @return {Boolean}.
     */
    isIE8: function() {
        return this.isIE("8");
    },

    /**
     * Returns if the browser is Internet Explorer 9.
     * @return {Boolean}.
     */
    isIE9: function() {
        return this.isIE("9");
    }
};

CQ_Analytics.BrowserInfoInstance = new CQ_Analytics.BrowserInfo();