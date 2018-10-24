/*
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2012 Adobe Systems Incorporated
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
 */
/*
 * location: libs/social/tally/components/rating/clientlibs/rating.js
 * category: [cq.social.rating]
 */
(function($CQ, CQ) {
    "use strict";
    CQ.soco.Rating = CQ.soco.Rating || {};
    CQ.soco.Rating = function(ratingForm, rating, userRating) {
        if (arguments.length == 0) return;
        var that = this;
        this.form = ratingForm;
        this.rating = rating;
        this.isUserRating = userRating || false;
        ratingForm.find(".ratings-bar").on("mouseleave",function(){that.reset();});
        ratingForm.find(".ratings-star").each(
            function(index){
                $CQ(this).on("hover",function(){that.selectStars(index);});
                $CQ(this).on("click",function(){that.submit(index+1);});
                    });
    };
    CQ.soco.Rating.prototype.selectStars = function(starsToSelect) {
        this.form.find(".ratings-star").each(function(index) {
            if(index <= starsToSelect) {
                $CQ(this).addClass("selected").removeClass("full").removeClass("empty");
            } else {
                $CQ(this).removeClass("selected").removeClass("full").addClass("empty");
            }
        });
    };
    CQ.soco.Rating.prototype.reset = function() {
        var that = this;
        this.form.find(".ratings-star").each(function(index) {
            if(index < that.rating) {
                if(that.isUserRating) {
                    $CQ(this).addClass("selected").removeClass("empty");
                } else {
                       $CQ(this).addClass("full").removeClass("empty").removeClass("selected");
                }
            } else {
                $CQ(this).removeClass("selected").removeClass("full").addClass("empty");
            }
        });
    };
    CQ.soco.Rating.prototype.submit = function(rating){
            var that = this;
            var data = this.form.serialize();
            data += "&" + encodeURIComponent(CQ.soco.TEMPLATE_PARAMNAME) + "=" + encodeURIComponent('rating')
                + "&" + encodeURIComponent("response") + "=" + encodeURIComponent(rating);
            $CQ.ajax(this.form.attr('action'), {
                    data: data,
                    success: function(data, status, jqxhr) {
                        if (jqxhr.status === 201) {
                            CQ.soco.filterHTMLFragment(data, function(node) {
                                that.form.replaceWith(node);
                                that = undefined;
                            });
                        }
                    },
                    fail: function(jqxhr, status) {
                        throw status;
                    },
                    type: "POST"
                });
    };
})($CQ, CQ);
