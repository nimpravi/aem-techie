/*
 * Copyright 1997-2009 Day Management AG
 * Barfuesserplatz 6, 4001 Basel, Switzerland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Day Management AG, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Day.
 */
if (CQ_Analytics.CampaignMgr) {

    /**
     * Refreshes the experiences according to the currently selected campaign
     * 
     * @private
     */
    CQ_Analytics.CampaignMgr.refreshExperiences = function() {
        if (!this.data) {
            return;
        }

        var campaign = this.getCampaignByPath(this.data.path);

        var experienceSelector = $CQ("#cq-cc-campaign-experience-selector");
        experienceSelector.children().remove();

        if ( !campaign ) {
            experienceSelector.append($CQ("<option>").text(CQ.I18n.getMessage("(simulation)")));
            experienceSelector.select2('val', CQ.I18n.getMessage("(simulation)"));
            return;
        }

        var attributes = { value: CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE };
        if (this.data["recipe/path"] === CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE) {
            attributes.selected = "selected";
        }
        
        experienceSelector.append($CQ("<option>", attributes).text(CQ.I18n.getMessage("(default)")));
        if ( attributes.selected === 'selected' ) {
            experienceSelector.select2('val', attributes.value);
        }

        var that = this;

        var hasExperiences = false;

        $CQ.each(campaign.experiences, function(index, experience) {

            hasExperiences = true;

            var experiencePath = experience.path;
            var attributes = { value: experiencePath };

            if ( that.data["recipe/path"] === experiencePath ) {
                attributes.selected = 'selected';
            }

            experienceSelector.append(
                $CQ("<option>", attributes).text(experience.title)
            );
            
            if ( attributes.selected === 'selected' ) {
                experienceSelector.select2('val', experiencePath);
            }
        });
    };

    /**
     * @private
     */
    CQ_Analytics.CampaignMgr.getCampaignByPath = function(path) {
        return this.getCampaignBy("path", path);
    };

    /**
     * Reloads this store's data from the server side
     * 
     * <p>Maintains the currently selected properties and does not fire any events.</p>
     */
    CQ_Analytics.CampaignMgr.reload = function(experienceToSelect) {

        this.setSuppressEvents(true);

        var that = this;

        var url = CQ_Analytics.ClientContextMgr.getClientContextURL('/content/jcr:content/stores/' + this.STORENAME + '.init.js');
        url = CQ.shared.HTTP.externalize(url);

        $CQ.ajax(url).done(function(result) {
            that.refreshExperiences();
            if (experienceToSelect) {
                that.setProperty("recipe/path", experienceToSelect);
            }
            that.setSuppressEvents(false);
            that.fireEvent("update");

        }).always(function() {
            // make sure events are always enabled again, even on failure
            that.setSuppressEvents(false);
        });
    };

    CQ_Analytics.CampaignMgr.getExperienceByPath = function(path) {
        if (path === CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE) {
            return {
                title:  CQ.I18n.getMessage("(default)"),
                path :  CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE,
                id   :  CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE
            };
        }
        return this.getExperienceBy("path", path);
    };

    CQ_Analytics.CampaignMgr.displayWarning = function(warning) {

        var warningIcon = $CQ('<span>').attr('class','cq-cc-store-warning-sign').text('!');

        $CQ('<div>').attr('class', 'cq-cc-store-warning').text(warning)
            .appendTo($CQ('#cq-cc-store-warnings')).prepend(warningIcon);
    };

    CQ_Analytics.CampaignMgr.renderer = function(store, divId) {

        var campaign, i;

        var that = this;

        $CQ("#" + divId).children().remove();

        var container = "<div class='cq-cc-store'>";
        container += "<div id='cq-cc-store-warnings'></div>";

        if ( this.data.campaigns) {

            // render the campaign selector
            container += "<div class='cq-cc-campaign-prop'><a class='label' target='_blank' id='cq-cc-campaign-selector-label'>Active campaign: </a> <select id='cq-cc-campaign-selector'>";
            container += '<option value="">' + CQ.I18n.getMessage("(simulation)") +'</option>';
            for ( i = 0 ; i < this.data.campaigns.length ; i++ ) {
                campaign = this.data.campaigns[i];
                var selected = false;
                if ( this.data.path && this.data.path === campaign.path ) {
                    selected = true;
                }
                var selectedAttribute = selected ? ' selected="selected" ' : '';
                container += '<option value="' + campaign.path + '" ' + selectedAttribute + '>' + campaign.title+ '</option>';
            }
            container += "</select></div>";

            // render the campaign experience selector
            container += "<div class='cq-cc-campaign-prop'>";
            container += "<a class='label' target='_blank' id='cq-cc-campaign-experience-selector-label'>";
            container += CQ.I18n.getMessage("Active experience:") + " </a> <select id='cq-cc-campaign-experience-selector'>";
            container += "</select></div>";
        } else {
            container += CQ.I18n.getMessage("No campaigns found");
        }

        container += "</div>";

        var select2Opts = {
                'width': '180px',
                'dropdownCssClass': 'cq-cc-campaign-store-dropdown'
        }
        
        $CQ("#" + divId).append(container);
        $CQ('#cq-cc-campaign-selector').select2(select2Opts);
        $CQ('#cq-cc-campaign-experience-selector').select2(select2Opts);

        that.refreshExperiences();

        // enable/disable links to campaign and experience pages
        var campaignPath = this.getProperty("path");
        $CQ('#cq-cc-campaign-selector-label')
            .toggleClass('cq-cc-campaign-prop-button', !!(campaignPath))
            .attr('href', campaignPath ? campaignPath + '.html' : null);

        var experiencePath = this.getProperty("recipe/path");
        if ( experiencePath === CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE ) {
            experiencePath = null;
        }
            
        $CQ('#cq-cc-campaign-experience-selector-label')
            .toggleClass('cq-cc-campaign-prop-button', !!(experiencePath))
            .attr('href', experiencePath ? experiencePath + '.html' : null);

        campaign = this.getCampaignByPath(campaignPath);

        if ( campaign && campaign.experiences.length === 0) {
            this.displayWarning(CQ.I18n.getMessage("The selected campaign does not have any experience pages."));
        }

        // when a campaign is selected for simulation, change the relevant properties and fire an update event 
        $CQ('#cq-cc-campaign-selector').change(function() {

            var selected = $CQ(this).find(':selected');

            that.setCampaign( that.getCampaignByPath(selected.val()) );

            that.refreshExperiences();
        });

        // when a campaign experience is selected for simulation, ( you guessed it ) change the relevant properties
        // and fire an update event
        $CQ('#cq-cc-campaign-experience-selector').change(function() {

            var path = $CQ(this).find(":selected").val();

            that.setExperience( that.getExperienceByPath(path) );
        });
    };
    
    CQ_Analytics.CCM.addListener("configloaded", function() {

        //add to std clickstream cloud ui
        CQ_Analytics.ClickstreamcloudUI.register(
                this.getSessionStore(),
                CQ_Analytics.CCM.getUIConfig(this.getName()));

    }, CQ_Analytics.CampaignMgr);
}