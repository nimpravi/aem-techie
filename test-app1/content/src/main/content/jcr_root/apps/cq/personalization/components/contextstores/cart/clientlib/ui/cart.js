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

if (CQ_Analytics.CartMgr) {
    CQ_Analytics.CartMgr.renderer = function(store, divId) {
        CQ_Analytics.CartMgr.internalRenderer(store, divId);
    };

    CQ_Analytics.CartMgr.addListener("update", function(eventName, propName) {
        var store = this;

        // Send any changed data up to the server for recalculation (and persistence):
        if (propName && propName != "totalPrice") {
            if (CQ_Analytics.CartMgr.refreshTimeout) {
                clearTimeout(CQ_Analytics.CartMgr.refreshTimeout);
            }

            CQ_Analytics.CartMgr.refreshTimeout = setTimeout(function() {
                store.update();
            }, 50);
        }
    });

    CQ_Analytics.CartMgr.internalRenderer = function(store, divId) {
        var storeDiv = $CQ("#" + divId);
        storeDiv.children().remove();

        if (!store.data["entries"]) {
            storeDiv.append($CQ("<div/>").text(CQ.I18n.getMessage("Not on an eCommerce page.")));
            return;
        }

        storeDiv.off(".cart");

        storeDiv.on("click.cart", "a[data-voucher]", function(event) {
            var code = $CQ(this).attr("data-voucher");

            for (var i = 0; i < store.data.vouchers.length; i++) {
                if (store.data.vouchers[i].code == code) {
                    store.data.vouchers.splice(i, 1);  // remove
                    break;
                }
            }

            if (event && event.preventDefault) event.preventDefault();

            store.fireEvent("update", "vouchers");

            return false;
        });

        var div = $CQ("<div/>").addClass("cq-cc-content");
        var divClear = $CQ("<div/>").addClass("cq-cc-clear");

        if (store.data["entries"] && store.data["entries"].length > 0) {
            var products = $CQ("<ol/>")
                .addClass("cq-cc-cart-items");
            for (var i = 0; i < store.data["entries"].length; i++) {
                var entry = store.data["entries"][i];
                var link = $CQ("<a/>")
                    .attr("href", CQ.shared.HTTP.externalize(entry["page"]))
                    .text(entry["title"]);
                var quantity = $CQ("<span/>")
                    .addClass("cq-cc-store-property")
                    .addClass("cq-cc-cart-item-quantity")
                    .append($CQ("<span/>")
                        .attr("data-store", "cart")
                        .attr("data-property", "entries[" + i + "].quantity")
                        .text(entry["quantity"])
                );
                var price = $CQ("<span/>")
                    .addClass("cq-cc-cart-item-price")
                    .text(entry["priceFormatted"]);
                var li = $CQ("<li/>")
                    .prepend(quantity)
                    .prepend(price)
                    .prepend(link)
                    .prepend($CQ("<img/>")
                        .attr("src", CQ.shared.HTTP.externalize(entry["thumbnail"]))
                );
                products.append(li);
            }
            div.append(products);
        }

        var divTotal = $CQ("<div/>")
            .addClass("cq-cc-cart-totalprice")
            .append($CQ("<span/>")
                .addClass("cq-cc-store-property")
                .attr("data-store", "cart")
                .attr("data-property", "totalPriceFloat")
                .attr("title", "/cart/totalPrice").text(store.data["totalPrice"])
        );
        div.append(divTotal);

        if (store.data["promotions"] && store.data["promotions"].length > 0) {
            var promotions = $CQ("<div/>")
                .addClass("cq-cc-cart-promotions");
            for (var i = 0; i < store.data.promotions.length; i++) {
                var promo = store.data.promotions[i];
                var link = $CQ("<a/>")
                    .attr("href", CQ.shared.HTTP.externalize(promo["path"] + ".html"))
                    .addClass("cq-html-tooltip")
                    .attr("data-tooltip", promo["message"])
                    .text(promo["title"])
                    .hover(
                        function (event) {
                            if (!this.htmltooltip) {
                                this.htmltooltip = new CQ.Ext.Tip({
                                    cls: "cq-cc-cart-tooltip",
                                    html: $CQ(this).attr("data-tooltip")
                                });
                            }
                            this.htmltooltip.setPosition(event.pageX, event.pageY + 20);
                            this.htmltooltip.show();
                        },
                        function () {
                            if (this.htmltooltip) {
                                this.htmltooltip.hide();
                            }
                        }
                    )
                promotions.append(link);
            }
            div.append(promotions);
        }

        if (store.data["vouchers"] && store.data["vouchers"].length > 0) {
            var vouchers = $CQ("<div/>")
                .addClass("cq-cc-cart-vouchers")
                .attr("title", "/cart/vouchers");
            for (var i = 0; i < store.data["vouchers"].length; i++) {
                var voucher = store.data["vouchers"][i];

                var deleteButton = $CQ("<a/>").attr("href", "#").attr("data-voucher", voucher["code"]);
                vouchers.append(deleteButton);

                var html = $CQ("<span/>")
                    .attr("title", voucher["description"] || CQ.I18n.getMessage("Voucher"))
                    .text(voucher["code"]);
                if (voucher["path"]) {
                    html = $CQ("<a/>")
                        .attr("href", CQ.shared.HTTP.externalize(voucher["path"] + ".html"))
                        .append(html);
                }
                vouchers.append(html);
            }
            div.append(vouchers);
        }

        storeDiv.append(div);
        storeDiv.append(divClear);
    };
}