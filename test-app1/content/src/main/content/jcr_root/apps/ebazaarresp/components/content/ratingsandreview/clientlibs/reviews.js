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
 */
Geometrixx = {};
(function($CQ, CQ, Geometrixx) {
    "use strict";
    var validateForm = function(event) {
        var resp = $CQ(event.target).find("[name='response']").val();
        var comment = $CQ(event.target).find("[name='comment']").val();
        if((resp ==="") ||(comment == "")) {
            alert("Please Enter a comment and a rating");
            return false;
        }
        return true;
    };
    Geometrixx.Reviews = function(ratingForm, rating, userRating) {
        var that = this;
        this.form = ratingForm;
        this.rating = rating;
        this.isUserRating = userRating || false;
        ratingForm.submit(validateForm);
        ratingForm.find(".ratings-bar").bind("mouseleave",function(){that.reset();});
        ratingForm.find(".ratings-star").each(
            function(index){
                $CQ(this).bind("hover",function(){that.selectStars(index);});
                $CQ(this).bind("click",function(){that.setRating(index+1);});
            });
    };
    Geometrixx.Reviews.prototype.setRating = function(rating) {
        this.form.find("input[name='response']").val(rating);
        this.rating = rating;
        this.selectStars(rating-1);
    }; 
    Geometrixx.Reviews.prototype.selectStars = function(starsToSelect) {
        this.form.find(".ratings-star").each(function(index) {
            if(index <= starsToSelect) {
                $CQ(this).addClass("selected").removeClass("full").removeClass("empty");
            } else {
                $CQ(this).removeClass("selected").removeClass("full").addClass("empty");
            }
        });
    };
    Geometrixx.Reviews.prototype.reset = function() {
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
    
   
    
    
})($CQ, CQ, Geometrixx);