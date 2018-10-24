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
/**
 * The <code>CQ_Analytics.Percentile</code> object is a singleton providing utility functions
 * for matching users to percentiles.
 * @singleton
 * @class CQ_Analytics.Percentile
 */
CQ_Analytics.Percentile = {};

CQ_Analytics.Percentile.matchesPercentiles = function(percentiles) {
    
    var percentileValue = ClientContext.get("/surferinfo/percentile");
    if ( !percentileValue ) {
        percentileValue = Math.round(Math.random() * 100);
        ClientContext.set("/surferinfo/percentile", percentileValue);
    }
    
    for ( var i = 0 ; i < percentiles.length ; i++ ) {
        var percentile = percentiles[i];
        if ( ( percentile.start <= percentileValue ) && ( percentileValue < percentile.end ) )
            return true;
    }
    
    return false;
}