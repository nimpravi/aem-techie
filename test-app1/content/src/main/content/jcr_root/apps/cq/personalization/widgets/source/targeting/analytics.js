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


// Analytics mode for the implicit mbox
/**
 * Analytics UI for the Implicit Mbox component
 *
 * @constructor
 *
 * @param {HTMLDomElement} parent The parent DOM element for this component
 * @param {String} location The location of the target component
 */
CQ.personalization.TargetAnalyticsOverlay = function(parent, location) {

    this.parent = parent;
    this.location = location;
    /* the minimum height of the analytics window */
    this.BG_MIN_HEIGHT = 250;

    if ( $CQ(parent).find('.cq-analyzable-overlay').length === 0 ) {
        this.overlay = $CQ('<div>').attr('class', 'cq-analyzable-overlay-contents');

        $CQ(parent).append
            ($CQ('<div>').attr('class', 'cq-analyzable-overlay').append(
                $CQ('<div>').attr('class','cq-analyzable-overlay-background-1').css('min-height', this.BG_MIN_HEIGHT + 'px').append(
                        this.overlay)));
    } else {
        this.overlay = $CQ(parent).find('.cq-analyzable-overlay-contents');
    }
};

CQ.personalization.TargetAnalyticsOverlay.prototype = {

    BAR_MAX_WIDTH_PX : 220,

    /**
     * Toggles the visibility of this component and - if visible - displays the performance report
     * for the the specified <tt>campaignPath</tt>
     *
     * @public
     */
    toggle: function(campaignPath) {

        var topOverlay = $CQ(this.parent).find('.cq-analyzable-overlay');

        if ( topOverlay.is(':visible') ) {
            topOverlay.hide();
        } else {
            topOverlay.show();
                if ( campaignPath === '') {
                    this.displayMessage(CQ.I18n.get("No campaign selected.") + " ");
                    var link = $CQ("<a>")
                        .attr("href", "#")
                        .text(CQ.I18n.getMessage("See Client Context") + " ")
                        .click(function() {
                            CQ_Analytics.ClientContextUI.show();
                            return false;
                        })
                        .appendTo(this.overlay);
                    $CQ("<img>")
                        .attr("src", CQ.shared.HTTP.externalize("/libs/cq/ui/widgets/themes/default/icons/16x16/clientcontext.png"))
                        .appendTo(link);
                } else {
                    this.showAnalyticsForCampaign(campaignPath);
                }
        }
    },

    /**
     * Installs a listener on the campaign which refreshes the performance report for newly selected campaigns
     * when this component is visible.
     *
     * @public
     */
    installCampaignStoreListener: function() {

        var that = this;
        var campaignStore = ClientContext.get("campaign");
        if ( !campaignStore )
            return;

        campaignStore.addListener("update", function(event, property) {
            if ( ( property === "path" || $CQ.inArray("path", property) ) && ClientContext.get("campaign/path") !== '' )
                if ( $CQ(that.parent).find('.cq-analyzable-overlay').is(':visible') ) {
                    that.showAnalyticsForCampaign(ClientContext.get("campaign/path"), this.location);
                }
        }, this);
    },

    /**
     * Displays the most significant part of a number in a human-readable format
     *
     * <p>For instance, 12500 would be displayed as 12k</p>
     */
    displayCompressedNumber: function(number) {

        var millions = number / 1000000;
        if ( millions > 1 )
            return Math.round(millions) + CQ.I18n.get("m", [], "A shorthand notation for millions.");

        var thousands = number / 1000;
        if ( thousands > 1 )
            return Math.round(thousands) + CQ.I18n.get("k", [], "A shorthand notation for thousands");

        return number;

    },

    /**
     * @private
     */
    showAnalyticsForCampaign: function(campaignPath) {
        var url = CQ.shared.HTTP.externalize("/libs/cq/analytics/testandtarget/command");
        this.overlay.text(CQ.I18n.get('Loading campaign analytics...'));

        var that = this;

        $CQ.post(url, { 'cmd': 'getPerformanceReport', 'campaignPath': campaignPath, 'location': this.location},
            function(response) {
                if ( response.error ) {
                    that.overlay.text(response.error);
                    return;
                }

                that.overlay.empty();

                var legend = $CQ('<div>').attr('class', 'cq-analyzable-legend');

                var conversionsLegend = $CQ('<div>').attr('class', 'cq-analyzable-legend-lift');

                $CQ('<span>').text(CQ.I18n.get('LIFT')).appendTo(conversionsLegend);
                var liftLegend = $CQ('<div>').attr('class', 'cq-analyzable-legend-lift-image').appendTo(conversionsLegend);
                $CQ('<div>').attr('class', 'cq-analyzable-legend-lift-image-negative').appendTo(liftLegend);
                $CQ('<div>').attr('class', 'cq-analyzable-legend-lift-image-positive').appendTo(liftLegend);


                var impressionsLegend = $CQ('<div>').attr('class', 'cq-analyzable-legend-impressions');
                $CQ('<div>').attr('class', 'cq-analyzable-legend-impressions-image').appendTo(impressionsLegend);
                $CQ('<span>').text(CQ.I18n.get('VISITORS')).appendTo(impressionsLegend);

                legend.append(conversionsLegend).append(impressionsLegend).appendTo(that.overlay);

                if ( response.experiences && response.experiences.length > 0 ) {

                    var defaultExperienceConversionRate;
                    var maxConversionRate = 0;
                    $CQ.each(response.experiences, function(index, experience)  {
                        if ( index === 0 )
                            defaultExperienceConversionRate = that.conversionRate(experience);

                        maxConversionRate = Math.max(maxConversionRate, that.conversionRate(experience));
                    });

                    $CQ.each(response.experiences, function (index, experience) {

                        var highlight = [];
                        var isDefault = index === 0;
                        var conversionRate = that.conversionRate(experience);
                        var isWinner = conversionRate === maxConversionRate && conversionRate > 0;
                        var liftPercentage = '';
                        var liftClass = '';
                        if ( !isDefault )  {
                            liftPercentage = that.percentage(maxConversionRate, conversionRate - defaultExperienceConversionRate, true);
                            liftClass = liftPercentage.charAt(0) == '+' ? 'cq-analyzable-row-lift-positive' : 'cq-analyzable-row-lift-negative';
                        }

                        var row;
                        var wrapper;
                        if ( isWinner ) {
                            wrapper = $CQ('<div>');
                            $CQ('<div>').attr('class', 'cq-analyzable-row-winner-marker').appendTo(wrapper);
                            row = $CQ('<div>').attr('class', 'cq-analyzable-row');
                            row.addClass('cq-analyzable-row-winner');
                            row.appendTo(wrapper);
                        } else {
                            row = $CQ('<div>').attr('class', 'cq-analyzable-row');
                        }

                        var iconDiv = $CQ('<div>').attr('class', 'cq-analyzable-row-icon').appendTo(row);

                        var thumb = experience.thumbnail ? CQ.shared.HTTP.externalize(experience.thumbnail) : that.location + '.thumb.png';
                        var thumbImage = $CQ('<img>').attr('src', thumb ).appendTo(iconDiv);
                        CQ_Analytics.onImageLoad(thumb, function(img) {
                            // keep target image size in sync with .cq-analyzable-row-icon width and height in analytics.less
                            var scaled = CQ_Analytics.scaleImage(img.width, img.height, 60, 44);
                            thumbImage.width(scaled.width);
                            thumbImage.height(scaled.height);
                            thumbImage.css("top", scaled.top);
                            thumbImage.css("left", scaled.left);
                            thumbImage.fadeIn();
                        });

                        var rowFirst = $CQ('<div>').attr('class', 'cq-analyzable-row-first');
                        if ( liftPercentage )
                            $CQ('<div>').attr('class', liftClass).text(liftPercentage).appendTo(rowFirst);
                        var experienceDiv = $CQ('<div>').attr('class', 'cq-analyzable-row-experience').text(experience.name).appendTo(rowFirst);

                        if ( isDefault ) {
                            $CQ('<span>').attr('class', 'cq-analyzable-row-highlight-default').text(CQ.I18n.get('DEFAULT')).appendTo(experienceDiv);
                        }

                        if ( isWinner ) {
                            $CQ('<span>').attr('class', 'cq-analyzable-row-highlight-winner').text(CQ.I18n.get('WINNER')).appendTo(experienceDiv);
                        }

                        var rowSecond = $CQ('<div>').attr('class', 'cq-analyzable-row-second');
                        $CQ('<div>').attr('class', 'cq-analyzable-row-impression-count').text(that.displayCompressedNumber(experience.impressions)).appendTo(rowSecond);
                        $CQ('<div>').attr('class', 'cq-analyzable-row-conversion-rate').text(that.percentage(experience.impressions, experience.conversions)).appendTo(rowSecond);

                        that.drawConversionBars(rowSecond, conversionRate, maxConversionRate, isDefault);

                        rowFirst.appendTo(row);
                        rowSecond.appendTo(row);

                        if ( wrapper )
                            that.overlay.append(wrapper);
                        else
                            that.overlay.append(row);
                    });
                } else {
                    that.overlay.append($CQ("<div>").text(
                            CQ.I18n.get('No performance data returned by the Test&Target API. Make sure that your campaign is activated and mboxes are accessed on a publish instance.')));
                }

            }
        );
    },

    /**
     * @private
     */
    conversionRate: function(experience) {

        if ( experience.conversions === 0 || experience.impressions === 0  )
            return 0;

        return experience.conversions / experience.impressions;
    },

    /**
     * @private
     */
    percentage: function(total, slice, forceSign) {
        if ( total === 0 || slice === 0 )
            return '0%';

        if ( typeof forceSign === "undefined ")
            forceSign = false;

        var prefix;
        if ( forceSign ) {
            prefix = ( slice >= 0 ? '+ ' : '- ');
        } else {
            prefix = ( slice >= 0 ? '' : '-');
        }

        return prefix + Math.abs(( 100 * slice / total ).toFixed(2)) + '%';
    },

    /**
     * @private
     */
    displayMessage: function(message) {
        this.overlay.text(message);
    },

    /**
     * @private
     */
    drawConversionBars: function(row, conversionRate, maxConversionRate, isDefault) {

        var baseBarWidth;
        var positiveLiftBarWidth = 0;
        var negativeLiftBarWidth = 0;
        var conversionRateRatio = ( conversionRate / maxConversionRate );
        if ( isDefault) {
            baseBarWidth = conversionRateRatio * this.BAR_MAX_WIDTH_PX;
            defaultExperienceConversionRate = conversionRate;
        } else {
            var conversionDelta = (conversionRate - defaultExperienceConversionRate) / maxConversionRate;
            var defaultExperienceBaseBarWidth = defaultExperienceConversionRate / maxConversionRate * this.BAR_MAX_WIDTH_PX;
            if ( conversionDelta > 0 ) { // positive lift
                baseBarWidth = defaultExperienceBaseBarWidth;
                if ( baseBarWidth !== 0 ) {
                    positiveLiftBarWidth = baseBarWidth * conversionDelta;
                } else {
                    positiveLiftBarWidth = this.BAR_MAX_WIDTH_PX * conversionDelta;
                }
            } else { // negative lift
                negativeLiftBarWidth = - conversionDelta * this.BAR_MAX_WIDTH_PX;
                baseBarWidth = (defaultExperienceConversionRate / maxConversionRate * this.BAR_MAX_WIDTH_PX  )- negativeLiftBarWidth;
            }
        }

        var bars = $CQ('<div>').attr('class', 'analyzable-row-bars').appendTo(row);

        $CQ('<div>').attr('class', 'analyzable-row-bar-default').css('width', baseBarWidth + 'px').appendTo(bars);
        if ( positiveLiftBarWidth !== 0 )
            $CQ('<div>').attr('class', 'analyzable-row-bar-positive-lift').css('width', positiveLiftBarWidth+ 'px').appendTo(bars);
        if ( negativeLiftBarWidth !== 0 )
            $CQ('<div>').attr('class', 'analyzable-row-bar-negative-lift').css('width', negativeLiftBarWidth + 'px').appendTo(bars);
    }
};