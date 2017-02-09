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

/**
 * A helper class providing a set of utility methods to handle a geolocation map.
 * <br>
 * @static
 * @singleton
 * @class CQ.personalization.GeolocationUtils
 */
CQ.personalization.GeolocationUtils = new function() {
    //hides map on doc click
    var handleDocClick = function() {
        CQ_Analytics.GeolocMap.hide();
    };

    return {
        /**
         * Creates a geolocation map widget that will be aligned over the geolocation store of the Client Context UI.
         * @param {Boolean} show True to show the map
         * @param {String} storeName The name of the store
         */
        createGeolocationMap: function(show, storeName) {
            var h = $CQ("."+storeName+" .cq-cc-store").parent().height() || 100;
            var w = $CQ("."+storeName+" .cq-cc-store").parent().width() || 400;

            CQ_Analytics.GeolocMap = new CQ.Ext.Panel({
                renderTo: "CQ",
                cls: "geolocationmap",
                floating: true,
                height: h,
                width: w,
                layout: "fit",
                border: false,
                bodyBorder: false,
                listeners: {
                    "show": function() {
                        var l = $CQ("."+storeName+" .cq-cc-thumbnail div").parents(".cq-cc-store").offset().left;
                        var t = $CQ("."+storeName+" .cq-cc-thumbnail div").parents(".cq-cc-store").offset().top;
                        this.setPosition(l,t);

                        $CQ("#cq-clientcontext-box").bind("click", handleDocClick);

                        if (CQ_Analytics.GeolocMap.map) {
                            var lat = CQ_Analytics.ClientContext.get(storeName + "/latitude") ||
                                CQ_Analytics.GeolocMap.map.setCenter.lat;
                            var lng = CQ_Analytics.ClientContext.get(storeName + "/longitude") ||
                                CQ_Analytics.GeolocMap.map.setCenter.lng;
                            var point = new google.maps.LatLng(lat, lng);

                            if (CQ_Analytics.GeolocMap.marker) {
                                CQ_Analytics.GeolocMap.marker.setPosition(point);
                                CQ_Analytics.GeolocMap.map.getMap().setCenter(point);
                            }
                        }
                    },
                    "hide": function() {
                        $CQ("#cq-clientcontext-box").unbind("click", handleDocClick);
                    }
                },
                items: [
                    {
                        gmapType: "map",
                        minGeoAccuracy: "GeocoderLocationType.GEOMETRIC_CENTER",
                        xtype: "gmappanel",
                        zoomLevel: 3,
                        listeners: {
                            mapready: function(comp, newLocation, oldLocation) {
                                CQ_Analytics.GeolocMap.map = this;
                                var lat = CQ_Analytics.ClientContext.get(storeName + "/latitude") || this.setCenter.lat;
                                var lng = CQ_Analytics.ClientContext.get(storeName + "/longitude") || this.setCenter.lng;
                                var point = new google.maps.LatLng(lat, lng);

                                CQ_Analytics.GeolocMap.marker = this.addMarker(
                                    point,
                                    {title: "", draggable: true}, // marker
                                    true, //clear
                                    true, //center
                                    undefined
                                );

                                google.maps.event.addListener(CQ_Analytics.GeolocMap.marker, 'dragend', function(mark) {
                                    CQ_Analytics.ClientContext.set(storeName + "/latitude", mark.latLng.lat());
                                    CQ_Analytics.ClientContext.set(storeName + "/longitude", mark.latLng.lng());

                                    CQ_Analytics.GeolocationUtils.computeAddress(mark.latLng.lat(), mark.latLng.lng(), storeName);
                                });
                            }
                        },
                        setCenter: {
                            lat: CQ_Analytics.ClientContext.get(storeName + "/latitude") || CQ_Analytics.GeolocationUtils.DEFAULTS.latitude,
                            lng: CQ_Analytics.ClientContext.get(storeName + "/longitude") || CQ_Analytics.GeolocationUtils.DEFAULTS.longitude
                        }
                    }
                ]
            });

            if (show) {
                CQ_Analytics.GeolocMap.show();
            }
        }
    }
}();

