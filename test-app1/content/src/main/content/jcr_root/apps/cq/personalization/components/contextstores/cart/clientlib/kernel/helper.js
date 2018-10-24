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
 */

if(!CQ_Analytics.CartHelper) {
    CQ_Analytics.CartHelper = (function() {
        return {
        
            containsProduct: function(data, product, quantity) {
                var productPagePath = product ? product.substring(0, product.lastIndexOf("#")) : null;
                for (var i = 0; data.entries && i < data.entries.length; i++) {
                    var entry = data.entries[i];
                    var entryPagePath = entry.page.substring(0, entry.page.lastIndexOf("#"));
                    if ((!product || entryPagePath == productPagePath) && (!quantity || entry.quantity >= quantity)) {
                        return true;
                    }
                }
                return false;
            }
        };
    })();
}
