/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2012 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 *
 * AdobePatentID="2884US01"
 */

if (!CQ_Analytics.CartMgr) {
    CQ_Analytics.CartMgr = new CQ_Analytics.SessionStore();
    CQ_Analytics.CartMgr.STOREKEY = "CART";
    CQ_Analytics.CartMgr.STORENAME = "cart";

    CQ_Analytics.CartMgr.init = function() {
        if (!this.data) {
            this.data = {};
        } else {
            var store = new CQ_Analytics.SessionPersistence({'container': 'ClientContext'});
            var simulationString = store.get(this.STOREKEY);
            if (simulationString) {
                var referenceAndTotal = simulationString.split("=");
                if (referenceAndTotal.length >= 2) {
                    this.referenceTotalPrice = referenceAndTotal[0];
                    this.simulatedTotalPrice = referenceAndTotal[1];
                    this.updateSimulatedPrice();
                }
            }

            this.initialized = true;
            this.fireEvent("initialize", this);
            this.fireEvent("update");
        }
    };

    //
    // A simulated total is the one thing we persist
    //
    CQ_Analytics.CartMgr.persist = function() {
        if (this.fireEvent("beforepersist") !== false) {
            var store = new CQ_Analytics.SessionPersistence({'container': 'ClientContext'});
            var simulationString = null;
            if (this.referenceTotalPrice && this.simulatedTotalPrice) {
                simulationString = this.referenceTotalPrice + "=" + this.simulatedTotalPrice;
            }
            store.set(this.STOREKEY, simulationString);
            this.fireEvent("persist");
        }
    };

    //
    // Check to see if a simulation is still valid (ie: the reference value underneath it
    // hasn't changed), and if so, apply it to the store.
    //
    CQ_Analytics.CartMgr.updateSimulatedPrice = function() {
        if (this.simulatedTotalPrice && this.referenceTotalPrice == this.data["totalPriceFloat"]) {
            this.data["totalPrice"] = this.simulatedTotalPrice + "";
            this.data["totalPriceFloat"] = this.simulatedTotalPrice;
        } else {
            this.simulatedTotalPrice = null;
            this.persist();
        }
    };

    //
    // Register that the user has simulated a total price.
    //
    CQ_Analytics.CartMgr.registerSimulatedPrice = function(value) {
        if (this.simulatedTotalPrice) {
            // already in a simulation; just update the value
            this.simulatedTotalPrice = value;
            this.data["totalPrice"] = value + "";
        } else {
            // new simulation; store the reference price and simulated value
            this.referenceTotalPrice = this.data["totalPriceFloat"];
            this.simulatedTotalPrice = value;
        }
        this.persist();
    };

    //
    // Override getProperty/setProperty to handle JSON data.
    //
    CQ_Analytics.CartMgr.getProperty = function(name, raw) {
        if (!this.data) {
            this.init();
        }

        var obj = this.data;
        try {
            var parts = name.split(".");
            for (var i = 0; i < parts.length-1; i++) {
                var part = parts[i];
                var indexPos = part.indexOf("[");
                var index = -1;
                if (indexPos > 0) {
                    index = parseInt(part.substring(indexPos+1, part.length-1));
                    part = part.substring(0, indexPos);
                }
                obj = obj[part];

                if (index >= 0) {
                    obj = obj[index];
                }
            }

            var finalPart = parts[parts.length-1];
            if (!raw) {
                var xssName = CQ.shared.XSS.getXSSPropertyName(finalPart);
                if (obj[xssName]) {
                    return obj[xssName];
                }
            }
            return obj[finalPart];
        } catch(e) {
            return undefined;
        }
    };

    CQ_Analytics.CartMgr.validate = function(name, value) {
        if (name == "totalPriceFloat") {
            var price = parseFloat(value);
            return price >= 0;                  // will return false for NaN
        } else if (name.indexOf(".quantity") > 0) {
            var quantity = parseInt(value);
            return quantity >= 0;               // will return false for NaN
        }
        return true;
    }

    CQ_Analytics.CartMgr.setProperty = function(name, value) {
        if (!this.data) {
            this.init();
        }

        if (!this.validate(name, value)) {
            this.fireEvent("update", name);     // reset UI to current value
            return;
        }

        if (name == "totalPriceFloat") {
            this.registerSimulatedPrice(value);
        }

        var obj = this.data;

        var parts = name.split(".");
        for (var i = 0; i < parts.length-1; i++) {
            var part = parts[i];
            var indexPos = part.indexOf("[");
            var index = -1;
            if (indexPos > 0) {
                index = parseInt(part.substring(indexPos+1, part.length-1));
                part = part.substring(0, indexPos);
            }

            if (!obj[part]) {
                obj[part] = {};
            }
            obj = obj[part];

            if (index >= 0) {
                if (!obj[index]) {
                    obj[index] = {};
                }
                obj = obj[index];
            }
        }

        var finalPart = parts[parts.length-1];
        obj[finalPart] = value;
        var xssName = CQ.shared.XSS.getXSSPropertyName(finalPart);
        this.data[xssName] = CQ.shared.XSS.getXSSValue(value);
        this.fireEvent("update", name);
    };

    //
    // Round-trip store to the server for recalculation and persistence
    //
    CQ_Analytics.CartMgr.update = function() {
        var store = this;

        if (this.updateUrl) {
            $CQ.ajax({
                url: this.updateUrl,
                type: "POST",
                data: {
                    "cart": JSON.stringify(store.data)
                },
                externalize: false,
                encodePath: false,
                hook: true,
                success: function(jsonData) {
                    store.data = jsonData;
                    store.updateSimulatedPrice();
                    CQ_Analytics.ClientContextUtils.renderStore(CQ_Analytics.CartMgr.divId, CQ_Analytics.CartMgr.STORENAME);
                    store.fireEvent("updatecomplete");
                    store.fireEvent("update");
                }
            });

        }
    };

    CQ_Analytics.CartMgr.clear = function() {
        if (this.data["entries"]) {
            this.data["entries"] = [];
        }
        if (this.data["vouchers"]) {
            this.data["vouchers"] = [];
        }
        this.data["totalPrice"] = "0";

        this.referenceTotalPrice = null;
        this.simulatedTotalPrice = null;
    };

    CQ_Analytics.CartMgr.reset = function() {
        this.clear();
        this.fireEvent("update");

        // persist changes locally
        this.persist();

        // and push them up to server
        this.update();
    }

    CQ_Analytics.CCM.addListener("configloaded", function() {

        CQ_Analytics.CCM.register(this);

        CQ_Analytics.SegmentMgr.addListener("update", function() {
            if (!this.promotionsMap) {
                return;
            }
            if (!this.data.promotions) {
                this.data.promotions = [];
            }

            var resolvedSegments = CQ_Analytics.SegmentMgr.getResolved();
            var resolvedPromoPaths = [];
            for (var i = 0; i < this.promotionsMap.length; i++) {
                var testPromotionMap = this.promotionsMap[i];
                var resolved = false;
                var testSegments = testPromotionMap.segments.split(",");
                for (var j = 0; j < testSegments.length; j++) {
                    if ($CQ.inArray(testSegments[j], resolvedSegments) >= 0) {
                        resolved = true;
                        break;
                    }
                }
                if (resolved) {
                    resolvedPromoPaths.push(testPromotionMap.path);
                }
            }

            var changed = false;
            for (var i = 0; i < this.data.promotions.length; i++) {
                var promo = this.data.promotions[i];
                var j = $CQ.inArray(promo["path"], resolvedPromoPaths);
                if (j >= 0) {
                    resolvedPromoPaths.splice(j, 1);   // remove
                } else {
                    this.data.promotions.splice(i--, 1);  // remove
                    changed = true;
                }
            }
            for (var i = 0; i < resolvedPromoPaths.length; i++) {
                var promo = { "path": resolvedPromoPaths[i] };
                this.data.promotions.push(promo);
                changed = true;
            }
            if (changed) {
                this.update();
            }
        }, CQ_Analytics.CartMgr);

    }, CQ_Analytics.CartMgr);
}

