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
//AdobePatentID="2441US01"
if (!CQ_Analytics.GeolocationUtils) {
    /**
     * A helper class providing a set of utility methods to manage a geolocation store.
     * <br>
     * @static
     * @singleton
     * @class CQ_Analytics.GeolocationUtils
     */
    CQ_Analytics.GeolocationUtils = new function() {
        return {
            /**
             * Initializes a persisted json store that contains the geolocation.
             * @param {String} storeName Name of the store
             */
            init: function(storeName) {
                var geoloc;
                try {
                    if (typeof navigator.geolocation === 'undefined') {
                        geoloc = google.gears.factory.create('beta.geolocation');
                    } else {
                        geoloc = navigator.geolocation;
                    }
                } catch(e) {
                }

                var createStore = function(defaultData) {
                    var store = CQ_Analytics.PersistedJSONStore.registerNewInstance(storeName, defaultData);
                    store.addListener("update", function(event, property) {
                        var latitude = CQ_Analytics.ClientContext.get(storeName + "/latitude");
                        var longitude = CQ_Analytics.ClientContext.get(storeName + "/longitude");

                        if (!latitude || !longitude) {
                            if (property != "generatedThumbnail") {
                                store.setProperty("generatedThumbnail", CQ_Analytics.GeolocationUtils.THUMBNAILS.fallback);
                            } else {
                                //if not lat or lng, display the fallback thumbnail
                                if (store.getProperty(property, true) != CQ_Analytics.GeolocationUtils.THUMBNAILS.fallback) {
                                    store.setProperty(property, CQ_Analytics.GeolocationUtils.THUMBNAILS.fallback);
                                }
                            }
                        } else {
                            //if lat or lng, restore initial thumbnail if was set to the fallback
                            if (store.getProperty("generatedThumbnail", true) == CQ_Analytics.GeolocationUtils.THUMBNAILS.fallback) {
                                store.setProperty("generatedThumbnail", store.getInitProperty("generatedThumbnail"));
                            }
                            if (property == "latitude" || property == "longitude" || !property) {
                                CQ_Analytics.GeolocationUtils.computeAddress(latitude, longitude, storeName);
                            }
                        }
                    });
                };

                var initGeolocationStore = function(data, skipValues) {
                    var store = CQ_Analytics.StoreRegistry.getStore(storeName);
                    if (store) {
                        data = data || CQ_Analytics.GeolocationUtils.DEFAULTS;

                        //backup thumbnail
                        var gt = data["generatedThumbnail"] = store.getInitProperty("generatedThumbnail");
                        store.initJSON(data);

                        if (!skipValues) {
                            store.init();
                            //re set because it gets lost during init
                            store.setProperty("generatedThumbnail", gt);
                        }
                    } else {
                        createStore(data);
                    }
                };

                createStore();
                if (geoloc) {
                    geoloc.getCurrentPosition(
                        function(data) {
                            var d = {
                                "longitude": data.coords.longitude,
                                "latitude": data.coords.latitude
                            };

                            if (data.address) {
                                d["address"] = data.address
                            }

                            initGeolocationStore(d, CQ_Analytics.CCM.areStoresInitialized);
                        }, function(error) {
                            if (!CQ_Analytics.CCM.areStoresInitialized) {
                                var msg = "Error";
                                if( CQ_Analytics.isUIAvailable ) {
                                    //code = 3 default is timeout
                                    msg = CQ.I18n.getMessage("Connection timeout", null, "timeout while connecting geolocation service");
                                    if (error.code == 1) {
                                        msg = CQ.I18n.getMessage("Permission denied", null, "permission denied message from goelocation service");
                                    } else {
                                        if (error.code == 2) {
                                            msg = CQ.I18n.getMessage("Position unavailable", null, "geolocation service couldn't find location");
                                        }
                                    }
                                }

                                var d = {
                                    "address": {
                                        "country": msg
                                    }
                                };

                                initGeolocationStore(d, CQ_Analytics.CCM.areStoresInitialized);
                            }
                        }
                    );
                } else {
                    initGeolocationStore();
                }
            },

            /**
             * Computes the address based on latitude and longitude and sets it in the according store.
             * @param {Number} lat The latitude
             * @param {Number} lng The longitude
             * @param {String} storeName The name of the store
             */
            computeAddress: function(lat, lng, storeName) {
                CQ_Analytics.ClientContext.set(storeName + "/address/region");
                CQ_Analytics.ClientContext.set(storeName + "/address/countryCode");
                CQ_Analytics.ClientContext.set(storeName + "/address/country");
                var geocode_callback = function() {
                    var point = new google.maps.LatLng(lat, lng);
                    var geocoder = new google.maps.Geocoder();
                    geocoder.geocode({location: point},
                        function(result, status) {
                            if (status === "OK" && result[0] && result[0].address_components) {
                                for (var i = 0; i < result[0].address_components.length; i++) {
                                    var a = result[0].address_components[i];
                                    if (a.types && a.types.length) {
                                        if (a.types[0] == "administrative_area_level_1") {
                                            CQ_Analytics.ClientContext.set(storeName + "/address/region", a["short_name"]);
                                        } else {
                                            if (a.types[0] == "country") {
                                                CQ_Analytics.ClientContext.set(storeName + "/address/countryCode", a["short_name"]);
                                                CQ_Analytics.ClientContext.set(storeName + "/address/country", a["long_name"]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    );
                };

                if (!window.google) {
                    window.geocode_callback = geocode_callback;
                    $CQ.getScript(document.location.protocol + "//maps.google.com/maps/api/js?sensor=false&callback=geocode_callback");
                } else {
                    geocode_callback.call();
                }
            }
        }
    }();

    //defines the default location if current one could not be resolved (defaults to Adobe HQ)
    CQ_Analytics.GeolocationUtils.DEFAULTS = {
        "latitude": 37.331375,//= Adobe HQ // 47.554995, = basel
        "longitude": -121.893992//= Adobe HQ // 7.589998 = basel
    };

    //fallback thumbnail on California max zoom
    CQ_Analytics.GeolocationUtils.THUMBNAILS = {
        "fallback": document.location.protocol + "//maps.googleapis.com/maps/api/staticmap?center=37,-121&zoom=0&size=80x80&sensor=false"
    }
}

