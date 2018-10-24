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

// utility functions for resizing images on the client side

if ( typeof CQ_Analytics == "undefined" ) CQ_Analytics = {};

/** Run callback when image given as url is loaded.
 *  Used to get the real size of an image. Callback gets passed an Image object. */
CQ_Analytics.onImageLoad = function(url, callback) {
    var img = new Image();
    img.src = url;
    if (img.complete) {
        callback(img);
    } else {
        img.onload = function() { callback(this); };
        // we need to call the callback even if the image is not loaded due to an error
        // todo: separate onload, onerror callbacks would make more sense
        img.onerror = function() { callback(this); };
    }
};

/** Scales an image in portrait or landscape (width/height) properly
 *  for centered display in a box with targetWidth/targetHeight. Use letterBox=true
 *  the image should fit in completely, and letterBox=false if the image
 *  should be "zoomed" and only a part in the center displayed in the box.
 */
CQ_Analytics.scaleImage = function (width, height, targetWidth, targetHeight, letterBox) {
    var result = {
        width: 0,
        height: 0,
        left: 0,
        top: 0
    };

    if ((width <= 0) || (height <= 0) || (targetWidth <= 0) || (targetHeight <= 0)) {
        return result;
    }

    var resultWidth = width / height * targetHeight;

    // portrait/landscape vs. zoom/letterbox
    var scaleOnWidth = (resultWidth > targetWidth) ? letterBox : !letterBox;

    if (scaleOnWidth) {
        result.width = targetWidth;
        result.height = Math.floor(height / width * targetWidth);
    } else {
        result.width = Math.floor(resultWidth);
        result.height = targetHeight;
    }

    // center image
    result.left = Math.floor((targetWidth - result.width) / 2);
    result.top = Math.floor((targetHeight - result.height) / 2);

    return result;
};