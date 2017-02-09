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

// Authoring UI for targeting

// internal scope
(function() {

    // Utility to traverse an object tree and call a function (key, obj, parent) for every object.
    function traverse(obj, fn, key, parent) {
        if (typeof obj === "object") {
            if (fn.apply(this, [key, obj, parent])) {
                return true;
            }
            var stop = false;
            $CQ.each(obj, function(k, v) {
                if (traverse(v, fn, k, obj)) {
                    stop = true;
                    return true;
                }
            });
            if (stop) {
                return true;
            }
        }
        return false;
    }

    /** if a string starts with a certain prefix */
    function startsWith(str, prefix) {
        return (str.indexOf(prefix) === 0);
    }

    /** returns the last name in a path; eg. /content/foo/bar => bar */
    function basename(path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    function createPage(path, resourceType, params, successFn, scope) {
        var defaultParams = {};
        defaultParams["jcr:primaryType"] = "cq:Page";
        defaultParams["jcr:content/jcr:primaryType"] = "cq:PageContent";
        defaultParams["jcr:content/sling:resourceType"] = resourceType;

        params = CQ.utils.Util.applyDefaults(params, defaultParams);

        CQ.HTTP.post(path, function(options, success, xhr, response) {
            if (success) {
                if (successFn) {
                    successFn.call(scope, response);
                }
            }
        }, params);
    }

    // path inside an offer page that contains the actual offer/teaser component(s)
    var OFFER_INNER_PATH = "jcr:content/par";
    var DEFAULT_COMPONENT = "default";

    // the placeholder image for experiences that don't have an offer yet.
    var OFFER_PLACEHOLDER = "/libs/cq/ui/widgets/themes/default/icons/240x180/page.png";

    // client context properties
    var CAMPAIGN_PROP = "campaign/path";
    var EXPERIENCE_PROP = "campaign/recipe/path";

    /**
     * Authoring UI for targeting.
     *
     * @constructor
     * @param {CQ.wcm.EditBase} component EditBase instance of target component
     */
    CQ.personalization.TargetEditor = function(component) {
        this.component = component;
        // initially, the target component renders the default experience included
        // TODO: make this dependent on rendering engine (t+t=> default present, cq teaser=> not)
        this.activeExperience = null; //CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE;

        // get updated by campaign reselection
        var campaignStore = ClientContext.get("campaign");
        if (campaignStore) {
            campaignStore.addListener("update", this.onCampaignUpdate, this);
        }

        // need to hook into nested editables when we switch the teaser content
        CQ.WCM.on("editableready", this.onEditableReady, this);

        // need to listen for "mode change" events (i.e. in case of switching to preview mode)
        CQ.WCM.getTopWindow().CQ.WCM.on("wcmmodechange", this.onWCMModeChange, this);
        this.render();
        // update default content if the ClientContext already has a selection, but after the component
        // is rendered since we potentially change the DOM
        if ( campaignStore && ClientContext.get(CAMPAIGN_PROP) ) {
            this.update();
        }

        this.renderOverlayIcon();

        var targetEditor = this;

        // hook into observation to make sure the popup stays positioned at the right place
        var observeElementPosition = component.observeElementPosition;
        component.observeElementPosition = function() {
            targetEditor.position();
            observeElementPosition.call(this);
            //we should also reposition the placeholder in case we don't have an offer.
            if (!targetEditor.getOfferURL(targetEditor.activeExperience) && !component.deleted) {
                targetEditor.createMissingOfferPlaceholder();
            }
        };

        // remove the target editor UI if the editable component is removed
        var remove = component.remove;
        component.remove = function() {
            targetEditor.remove();
            delete component.targetEditor;
            remove.call(this);
        };
    };

    CQ.personalization.TargetEditor.prototype = {

        // private: listener for campaign store changes
        onCampaignUpdate: function(event, property) {
            // if no property is given, this event means that everything needs to reload
            var reload = (typeof property === "undefined");
            this.update(reload);
        },

        // private: track all nested components (that are part of the targeting)
        onEditableReady: function(editable) {
            if (!editable) {
                return;
            }

            var offer;

            // if the placeholder is defined we must hide it first.
            if (this.missingOfferPlaceholder) {
                this.missingOfferPlaceholder.hide();
            }

            // get base path to current offer to check against editable path
            if (this.activeExperience === CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE || !this.activeExperience) {
                offer = this.component.path + "/" + DEFAULT_COMPONENT;
            } else {
                offer = this.offers[this.activeExperience];
                if (!offer) {
                    editable.suspendDropTargets();
                    this.createMissingOfferPlaceholder();
                }
                offer = offer + "/" + OFFER_INNER_PATH;
            }

            // we are only interested in components from the offer page we load
            if (startsWith(editable.path, offer)) {
                this.trackNestedEditable(editable);
            }
        },

        /*
         * Intercept mode changes.
         * If there's anything special we need to do when switching modes (edit, preview, design etc.) this is the place.
         */
        onWCMModeChange: function(mode) {
            if (mode === CQ.utils.WCM.MODE_PREVIEW) {
               // switching to preview mode, need to hide the experiences window.
               this.hide();
            }
        },

        // update preview images when current active offer component is edited
        // therefore listen to afteredit event for all our nested components
        trackNestedEditable: function(editable) {
            // get notified after all edit operations
            editable.on(CQ.wcm.EditBase.EVENT_AFTER_EDIT, function() {
                // update preview images (update entire view as we don't necessarily know what changed
                // in the component, for smooth loading & proper image scaling)
                this.reloadExperiencesInPopup(true);
            }, this);

            var targetEditor = this;

            // disable drag and drop for the nested editable, because it breaks the page content
            // see issue CQ5-30220
            editable.orderable = false;

            if (editable instanceof CQ.wcm.EditRollover) {
                editable.on(CQ.wcm.EditRollover.EVENT_SHOW_HIGHTLIGHT, function(component) {
                    targetEditor.showOverlayIcon();
                });
                editable.on(CQ.wcm.EditRollover.EVENT_HIDE_HIGHTLIGHT, function(component) {
                    targetEditor.hideOverlayIcon();
                });
            } else {
                var el = $CQ(editable.element.dom);
                el.mouseover(function(event) {
                    targetEditor.showOverlayIcon();
                });
                el.mouseout(function(event) {
                    // keep visible if the mouse goes over the overlay icon
                    if (!targetEditor.overlayIcon.is(event.relatedTarget)) {
                        targetEditor.hideOverlayIcon();
                    }
                });
            }
        },

        // if we don't have an offer for the current experience we have to show a placeholder
        createMissingOfferPlaceholder: function() {
            //determine where should we "anchor" this overlay
            var anchor = $CQ(this.component.element.dom);
            var that = this;

            //pre-determine the position.
            var cssPosition = {
                left:  anchor.offset().left + "px",
                top:   anchor.offset().top  + "px",
                width: anchor.width()       + "px",
                height:anchor.height()      + "px"
            };

            if (this.missingOfferPlaceholder) {
                // if we already have a placeholder let's show it now
                this.missingOfferPlaceholder.show();

                // apply the position values again, because the anchor may have been repositioned (e.g. via browser resize).
                this.missingOfferPlaceholder.css(cssPosition);
                return;
            }

            // create the placeholder div initially
            this.missingOfferPlaceholder = $CQ("<div>")
                .addClass("cq-targeting-offer-placeholder")
                .css(cssPosition)
                .mouseover(function(e){
                    that.showOverlayIcon();
                })
                .mouseout(function(e){
                    if (!that.overlayIcon.is(e.relatedTarget)) {
                        that.hideOverlayIcon();
                    }
                })
               .appendTo("#"+CQ.Util.ROOT_ID);

            //add a clickable text so the user can easily add content to the experience
            $CQ("<span>")
                .addClass("cq-editrollover-insert-message")
                .html(CQ.I18n.getMessage("Add offer"))
                .click(function(e) {
                    // it will create the default offer.
                    that.createOffer(that.activeExperience, function(){
                        // need to load them locally again to show updated image
                        that.reloadExperiencesInPopup(true);
                    });
                    return false;
                })
                .appendTo(this.missingOfferPlaceholder);
        },

        /**
         * Updates the selection from the ClientContext campaign store.
         *
         * Called when the the campaign or experience has been changed anywhere, including
         * when an experience was selected in our popup. It all goes via the ClientContext
         * campaign store.
         */
        update: function(reload) {
            var campaign = ClientContext.get(CAMPAIGN_PROP);
            
            // make sure to update the select2 widget and not the backing select native widget
            this.campaignSelector.select2("val", campaign);

            if (!reload) {
                // only reload experiences list if campaign changed
                if (campaign !== this.campaign) {
                    this.campaign = campaign;
                    reload = true;
                }
            }

            var experience = ClientContext.get(EXPERIENCE_PROP);

            // safety check: if campaign is selected, but experience is empty,
            // interpret as DEFAULT experience selected
            if (campaign && !experience) {
                experience = CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE;
            }
            
            // quick validation (should be in CampaignMgr?)
            if (experience && experience.indexOf("/") === 0) {
                // if experience is not part of current campaign, stop
                if (!startsWith(experience, this.campaign + "/")) {
                    return;
                }
            }

            this.reloadExperiencesInPopup(reload, function() {
                this.switchExperience(experience);
            });
        },

        /**
         * Load new offer in our inner html.
         *
         * Replace the inner html of the target component with the complete parsys of the offer page
         * (this will automatically instantiate components inside, as the necessary script code will
         * be part of the html returned).
         *
         * Note that we have to refresh on the parent component path, in order to get the html
         * snippet that actually includes the important CQ.WCM.edit() calls.
         *
         * @param {String} experience    the path of the experience we are switching to
         * @param {Boolean} forceRefresh force reload of the inner editable after switching.
         *                               This is necessary when adding experience content.
         *
         */
        switchExperience: function(experience, forceRefresh) {
            // leaving experience editing and going to simulation (= no campaign set)
            if (!this.campaign) {
                // only update if we aren't in simulation already
                // important, client context reset events trigger lots of repated events
                if (this.activeExperience != null) {
                    this.activeExperience = null;

                    // hide placeholder if it is currently shown
                    if (this.missingOfferPlaceholder) {
                        this.missingOfferPlaceholder.hide();
                    }

                    // reload the full component so the engine implementation is reincluded and starts from
                    // scratch (includes e.g. mboxDefault div and mbox script, or cq teaser loading code)
                    // Note: this will NOT include the editcontext for the target component itself, i.e.
                    //       it will not do a CQ.WCM.edit() and reload another TargetEditor
                    var componentUrl = this.component.path + ".html";

                    // Add current page selectors to have the same effect of this ajax as if the page
                    // was rendered. This is needed for mobile teasers (see CQ5-32760)
                    // NOTE: WTF-7 - URLs must be built on the server side, and we get a "refreshURL" or "simulationURL"
                    componentUrl = CQ.shared.HTTP.addSelectors(componentUrl, CQ.shared.HTTP.getSelectors(window.location.href));

                    this.component.refresh(componentUrl);
                }
                return;
            }

            // avoid reloading of offer that's currently shown
            // unless this is called after adding an offer to the current experience.
            if ((experience !== this.activeExperience) || (forceRefresh)) {
                var oldExperience = this.activeExperience;
                var url = this.getOfferURL(experience);
                if (!url) {
                    // if there is no offer (yet), show the default content behind the missingOfferPlaceholder
                    url = this.component.path + ".default.html";
                }
                this.activeExperience = experience;

                // make sure the cq teaser engine (if running) disables itself for this target component
                // Note: this is engine specific; ideally this should be some generic concept
                CQ_Analytics.Engine.stopTeaserLoader(this.component);

                // defer slightly to avoid issues with the refresh() method when this
                // function is called while this.component is still loading initially
                this.component.refresh.defer(15, this.component, [url]);

                // mark selected experience
                this.carousel.find('li[data-cq-experience="' + oldExperience + '"]').removeClass("cq-targeting-experience-selected");
                var selectedItem = this.carousel.find('li[data-cq-experience="' + experience + '"]');
                selectedItem.addClass("cq-targeting-experience-selected");
                this.carousel.jcarousel("scroll", selectedItem.index());
            }
        },

        getOfferURL: function(experience) {
            if (experience === CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE || !experience) {
                return this.component.path + ".default.html";
            }
            var offer = this.offers[experience];
            if (offer) {
                return offer + "/" + OFFER_INNER_PATH + ".html";
            }
            return null;
        },

        /** create experience for the given segment */
        // TODO: merge with create offer in single request
        createExperience: function(name, title, segment) {
            var params = {};
            params[":nameHint"] = name;
            params["jcr:content/jcr:title"] = title;
            params["jcr:content/cq:segments"] = segment;
            params["jcr:content/cq:segments@TypeHint"] = "String[]";

            createPage(
                this.campaign + "/*",
                "cq/personalization/components/experiencepage",
                params,
                function(response) {
                    var experience = response.headers[CQ.HTTP.HEADER_PATH];
                    this.createOffer(experience, function() {
                        // reload (because experience is new) and select globally
                        var campaignStore = ClientContext.get('campaign');
                        if (campaignStore) {
                            campaignStore.reload(experience);
                        }
                    });
                },
                this
            );
        },

        /** create an offer under the given experience based on the current component */
        createOffer: function(experience, callback) {
            var componentName = basename(this.component.path);

            var params = {};
            // name & title of the page is not important, just use some hints regarding the mbox location
            params[":nameHint"] = basename(CQ.WCM.getPagePath()) + "-" + componentName;
            // copy title from experience page
            params["jcr:content/jcr:title"] = basename(CQ.WCM.getPagePath()) + " (" + componentName + ")";
            params["jcr:content/location"] = this.component.path;
            params["jcr:content/par/sling:resourceType"] = "foundation/components/parsys";
            // copy default component
            params[OFFER_INNER_PATH + "/" + componentName + "@CopyFrom"] = this.component.path + "/" + DEFAULT_COMPONENT;
            createPage(experience + "/*", "cq/personalization/components/teaserpage", params, function(response) {
                this.offers[experience] = response.headers[CQ.HTTP.HEADER_PATH];
                // if successful, get the path of the created page and load it as offer
                this.switchExperience(experience, true);
                callback.call(this);
            }, this);
        },

        /** Removes the offer for the specified experience */
        removeOffer:function(experience, callback) {

            var scope = this;
            var path = this.offers[experience];
            var params = {};
            params[":operation"] = "delete";

            CQ.HTTP.post(path, function(options, success, xhr, response) {
                if (success) {
                    if (callback) {
                        callback.call(scope, response);
                    }
                }
            }, params);
        },

        deleteExperience: function(path) {
            if ( !path ) {
                return;
            }

            $CQ.post(path, { ':operation' : 'delete' }, function() {
                // reload (because experience is new) and select globally
                var campaignStore = ClientContext.get('campaign');
                if (campaignStore) {
                    campaignStore.reload(CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE);
                }
            });
        },

        renderOverlayIcon: function() {
            var targetEditor = this;
            this.overlayIcon = $CQ("<div>")
                .addClass("cq-targeting-launch-icon cq-targeting-icon-placeholder")
                .attr("title", CQ.I18n.get("Experiences..."))
                .click(function() {
                    targetEditor.toggle();
                })
                .mouseover(function(event) {
                    targetEditor.showOverlayIcon();
                })
                .mouseout(function(event) {
                    var el = $CQ(targetEditor.component.element.dom);
                    // keep visible if the mouse goes over the element
                    if (!targetEditor.el.is(event.relatedTarget)) {
                        targetEditor.hideOverlayIcon();
                    }
                })
                .appendTo($CQ("#" + CQ.Util.ROOT_ID))
                .hide();

            this.positionOverlayIcon();
        },

        /** initially renders the basic editor popup container */
        render: function() {
            if (this.el) {
               return;
            }

            var that = this;

            // main element
            this.el = $CQ("<div>").addClass("cq-targeting-editor");

            // toolbar on top
            var toolbar = $CQ("<div>")
                .addClass("cq-targeting-editor-toolbar")
                .appendTo(this.el);
            $CQ("<div>")
                .addClass("cq-targeting-action cq-targeting-action-edit")
                .attr("title", CQ.I18n.getMessage("Edit targeting settings"))
                .appendTo(toolbar)
                .click(function() {
                    CQ.wcm.EditBase.showDialog(that.component, CQ.wcm.EditBase.EDIT);
                    return false;
                });
            this.addEl = $CQ("<div>")
                .addClass("cq-targeting-action cq-targeting-action-add")
                .attr("title", CQ.I18n.getMessage("Add new experience"))
                .appendTo(toolbar)
                .click(function() {
                    that.addExperience();
                    return false;

                });
            
            var holder = $CQ("<div>")
                .addClass("cq-targeting-campaign-selector")
                .appendTo(toolbar);
            
            var campaignMgr = ClientContext.get("campaign");
            var campaigns = CQ_Analytics.CampaignMgr.data.campaigns;
            var selectedCampaign = ClientContext.get(CAMPAIGN_PROP);
            var i;

            var container = $CQ("<div>").addClass("cq-cc-store").appendTo(holder);
            var selectorContainer = $CQ("<div>").addClass("cq-cc-campaign-prop").appendTo(container);

            this.campaignSelector = $CQ("<select>").change(function() {
                var selected = $CQ(this).find(":selected");
                if ( selected.length ) {
                    ClientContext.set(CAMPAIGN_PROP,selected[0].value);
                }
            }).appendTo(selectorContainer);

            $CQ("<option>").attr({"value":"", "label": CQ.I18n.getMessage("(simulation)")}).html(CQ.I18n.getMessage("(simulation)")).appendTo(this.campaignSelector);
            for (i = 0; i < campaigns.length; i++) {
                $CQ("<option>")
                    .attr({"value":campaigns[i].path,"label":campaigns[i].title})
                    .html(campaigns[i].title)
                    .appendTo(this.campaignSelector);
            }
            
            if ( selectedCampaign ) {
                this.campaignSelector.val(selectedCampaign);
            }
            
            this.campaignSelector.select2({
                'width': '180px',
                'dropdownCssClass': 'cq-cc-campaign-store-dropdown'
            });
            
            
            $CQ("<div>")
                .addClass("cq-targeting-action cq-targeting-action-close")
                .attr("title", CQ.I18n.getMessage("Close the experience switcher"))
                .appendTo(toolbar)
                .click(function() {
                    that.hide();
                    return false;
                });

            // attach to #CQ authoring div
            $CQ("#" + CQ.Util.ROOT_ID).append(this.el);

            // make sure it's not visible at start
            this.el.hide();
        },

        /** build HTML for a single experience list item */
        createExperienceItem: function(thumbnail, experience, label) {
            var that = this;

            // build item to show this experience/offer with image & text
            var item = $CQ("<li>")
                .attr("data-cq-experience", experience)
                .attr("title", CQ.I18n.getMessage("Switch to experience: {0}", label))
                .click(function() {
                    ClientContext.set(EXPERIENCE_PROP, $CQ(this).attr("data-cq-experience"));
                    return false;
                });

            var itemContent = $CQ("<div>")
                .addClass("cq-targeting-experience-content")
                .appendTo(item);

            thumbnail = CQ.shared.HTTP.externalize(thumbnail);

            var thumbWrap = $CQ("<div>")
                .addClass("cq-targeting-experience-img-clip")
                .appendTo(itemContent);
            var thumb = $CQ("<img>")
                .attr("src", thumbnail)
                .addClass("cq-targeting-experience-img")
                .hide()
                .appendTo(thumbWrap);

            // if there's no thumbnail then don't do anything.
            if (thumbnail !== "") {
                CQ_Analytics.onImageLoad(thumbnail, function(img) {
                    // keep target image size in sync with @imgWidth and @imgHeight in target.less
                    var w = img.width;
                    var h = img.height;
                    if (img.width == 0) {
                        // this means the image is missing so we need to
                        // replace it with the placeholder.
                        // this usually happens when the targeted component is not an image.
                        thumb.attr("src",OFFER_PLACEHOLDER);
                        // ugly, hardcode the values
                        w = 240;
                        h = 180;
                    }
                    var scaled = CQ_Analytics.scaleImage(w, h, 140, 100);
                    thumb.width(scaled.width);
                    thumb.height(scaled.height);
                    thumb.css("top", scaled.top);
                    thumb.css("left", scaled.left);
                    thumb.fadeIn();
                });
            }
            $CQ("<div>")
                .addClass("cq-targeting-experience-label")
                .text(label)
                .appendTo(itemContent);

            if (experience !== CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE) {

                var quickActions = $CQ("<div>")
                    .addClass("cq-targeting-experience-quickactions")
                    .appendTo(itemContent);

                // if we don't have an offer for this experience then show the "add" button
                if (!this.offers[experience]) {
                    // show the add button
                    $CQ("<div>")
                        .addClass("cq-targeting-experience-quickactions-add")
                        .attr("title", CQ.I18n.getMessage("Add experience content"))
                        .click(function(e) {
                            that.createOffer($CQ(this).parents("li").attr("data-cq-experience"), function(){
                                // need to load them locally again to show updated image
                                that.reloadExperiencesInPopup(true);
                            });
                            return false;
                        })
                        .appendTo(quickActions);
                    // show the DELETE EXPERIENCE button if we don't have an offer.
                    $CQ("<div>")
                        .addClass("cq-targeting-experience-quickactions-delete")
                        .attr("title", CQ.I18n.getMessage("Delete experience"))
                        .click(function() {
                            var origin = this;

                            CQ.Ext.Msg.confirm(
                                CQ.I18n.getMessage("Confirm experience deletion"),
                                CQ.I18n.getMessage("Are you sure you want to delete this experience? This will delete all content for all locations that use this experience!"),
                                function(btnId) {
                                    if ( btnId === "yes") {
                                        that.deleteExperience($CQ(origin).parents('li').attr('data-cq-experience'));
                                    }
                                }
                            );
                            return false;
                        })
                        .appendTo(quickActions);
                } else {
                    // show the delete
                    $CQ("<div>")
                        .addClass("cq-targeting-experience-quickactions-remove")
                        .attr("title", CQ.I18n.getMessage("Remove offer"))
                        .click(function() {
                            var origin = this;

                            CQ.Ext.Msg.confirm(
                                CQ.I18n.getMessage("Confirm offer removal"),
                                CQ.I18n.getMessage("Are you sure you want to remove the offer for this experience?"),
                                function(btnId) {
                                    if ( btnId === "yes") {
                                        // remove the offer and reload the experiences
                                        that.removeOffer($CQ(origin).parents('li').attr('data-cq-experience'), function(){
                                            that.reloadExperiencesInPopup(true);
                                        });
                                    }
                                }
                            );
                            return false;
                        })
                        .appendTo(quickActions);
                }

            }

            return item;
        },

        /**
         * Loads all experiences in the editor popup (but not show the popup).
         * Will make sure experiences are loaded first before calling callback.
         * Won't load experiences again if forceLoad==false. Also finds the
         * individual offers for the target component.
         */
        reloadExperiencesInPopup: function(forceLoad, callback) {
            // nothing to do if we are not forced to reload and offers are present
            if (this.offers && !forceLoad) {
                if (callback) {
                    callback.call(this);
                }
                return;
            }

            var that = this;
            // map experience => offers
            that.offers = {};

            if (!this.campaign) {
                that.el.find(".cq-targeting-editor-content").remove();

                that.carousel = $CQ("<div>")
                    .addClass("cq-targeting-editor-content")
                    .addClass("cq-targeting-editor-clientcontext-hint")
                    .appendTo(that.el);
                
                var text = $CQ("<span>")
                    .text(CQ.I18n.getMessage("No campaign selected."))
                    .appendTo(this.carousel);
                
                if ( forceLoad && callback ) {
                    callback.call(this);
                }
                return;
            }

            // retrieve all experiences from the server & find matching offers for this mbox
            // TODO: custom json (to avoid recursion-too-deep issue)
            $CQ.ajax(this.campaign + ".infinity.json?ck=" + new Date()).done(function(campaign) {
                that.campaignTree = campaign;

                // recreate from scratch, jcarousel (or the version we have) doesn't support updates
                if (that.carousel) {
                    // remove carousel container div
                    that.el.find(".cq-targeting-editor-content").remove();
                    that.carousel = null;
                }

                var selectedExperience = ClientContext.get(EXPERIENCE_PROP);
                if (!selectedExperience) {
                    // safety fallback: if no experience is selected, use DEFAULT
                    selectedExperience = CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE;
                }
                var selectedItemIdx = -1;

                var contentDiv = $CQ("<div>").addClass("cq-targeting-editor-content").appendTo(that.el);
                that.carousel = $CQ("<ul>").appendTo(contentDiv);

                // entry for default content
                that.offers[CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE] = that.component.path;

                if (selectedExperience === CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE) {
                    selectedItemIdx = 0;
                }

                that.carousel.append(that.createExperienceItem(
                    that.component.path + ".thumb.png?cq_ck=" + Date.now(),
                    CQ_Analytics.CampaignMgr.DEFAULT_EXPERIENCE,
                    CQ.I18n.getMessage("Default")
                ));

                var itemCounter = 1;
                // existing experiences
                $CQ.each(campaign, function(experienceName, experience) {
                    if (experience["jcr:content"] &&
                        experience["jcr:content"]["sling:resourceType"] === "cq/personalization/components/experiencepage") {
                        // experience found
                        var offer = that.findOffer(experience, that.component.path);

                        var experiencePath = that.campaign + "/" + experienceName;
                        var imgUrl;
                        if (offer !== null) {
                            var offerPath = experiencePath + "/" + offer.name;
                            imgUrl = offerPath + ".thumb.png?cq_ck=" + Date.now();
                            that.offers[experiencePath] = offerPath;
                        } else {
                            // missing offer, show nothing yet
                            imgUrl = "";
                            that.offers[experiencePath] = null;
                        }

                        if (experiencePath === ClientContext.get(EXPERIENCE_PROP)) {
                            selectedItemIdx = itemCounter;
                        }

                        that.carousel.append(that.createExperienceItem(
                            imgUrl,
                            experiencePath,
                            experience["jcr:content"]["jcr:title"] || experienceName
                        ));
                        itemCounter++;
                    }
                });

                // build jcarousel (will change dom)
                that.carousel.jcarousel({
                    start: selectedItemIdx,
                    itemFallbackDimension: 142 // keep in sync with @itemWidth from target.less
                });

                // mark selected experience
                if (selectedExperience) {
                    $CQ(that.carousel).find('li[data-cq-experience="' + selectedExperience + '"]').addClass("cq-targeting-experience-selected");
                }

                if (callback) {
                    callback.call(that);
                }
            }).fail(function() {
                CQ.Notification.notify("Could not load experiences");
            });
        },

        // recursively search for offers with the right @location
        findOffer: function(experience, location) {
            var result = null;
            $CQ.each(experience, function(offerName, child) {
                if (child["jcr:primaryType"] === "cq:Page") {
                    if (child["jcr:content"] && child["jcr:content"].location === location) {
                        result = {
                            name: offerName,
                            obj: child
                        };
                        return true;
                    }
                }
            });
            return result;
        },

        /** handles the click on the "add experience" button */
        addExperience: function () {
            var that = this;

            // if no campaign is selected, ignore
            if (!that.campaign) {
                return;
            }
            if (!that.segmentDialogShown) {
                that.segmentDialogShown = true;

                $CQ.ajax("/etc/segmentation.infinity.json").done(function(segmentTree) {
                    var segments = {};
                    segmentTree.path = "/etc/segmentation";
                    traverse(segmentTree, function(key, obj, parent) {
                        if (parent) {
                            obj.path = parent.path + "/" + key;
                        }
                        if (obj["jcr:content"] &&
                            obj["jcr:content"]["sling:resourceType"] === "cq/personalization/components/segmentpage") {
                            segments[obj.path] = {
                                path: obj.path,
                                name: key,
                                title: obj["jcr:content"]["jcr:title"] || key
                            };
                        }
                    });
                    var width = 270;
                    // build segment selection "dialog" UI
                    var dialog = $CQ("<div>")
                        .addClass("cq-targeting-experience-dialog")
                        .css("position", "absolute")
                        .css("top", (that.addEl.offset().top + 33) + "px")
                        .css("left", (that.addEl.offset().left - 5) + "px")
                        .css("width",  width + "px")
                        .css("height", "100px");

                    $CQ("<h3>").text(CQ.I18n.getMessage("Choose Segment:")).appendTo(dialog);

                    // find used segments
                    var usedSegments = {};
                    if (that.campaignTree) {
                        traverse(that.campaignTree, function(key, obj, parent) {
                            if (obj["sling:resourceType"] === "cq/personalization/components/experiencepage") {
                                var segments = obj["cq:segments"];
                                if ($CQ.isArray(segments)) {
                                    $CQ.each(segments, function(idx, segment) {
                                        usedSegments[segment] = true;
                                    });
                                } else if (typeof segments === "string") {
                                    usedSegments[segments] = true;
                                }
                            }
                        });
                    }

                    var select = $CQ("<select>").appendTo(dialog);
                    $CQ.each(segments, function(path, segment) {
                        var title = segment.title;
                        if (usedSegments[segment.path]) {
                            title = title + CQ.I18n.getMessage(" (in use)");
                        }
                        $CQ("<option>")
                            .attr("value", segment.path)
                            .text(title)
                            .appendTo(select);
                    });

                    var inspectLink = $CQ("<a>")
                        .addClass("cq-targeting-segment-link")
                        .attr("target", "_blank")
                        .text(CQ.I18n.getMessage("Inspect"))
                        .appendTo(dialog);

                    select.change(function() {
                        inspectLink.attr('href', select.find(':selected').val() + ".html");
                    });

                    $CQ("<input>").attr("type", "submit").attr("value", CQ.I18n.getMessage("Add")).click(function() {
                        var segment = segments[$CQ(select).val()];
                        if (segment) {
                            that.createExperience(segment.name, segment.title, segment.path);
                        }

                        $CQ(dialog).remove();
                        that.segmentDialogShown = false;
                    }).appendTo(dialog);

                    $CQ("<input>").attr("type", "submit").attr("value", CQ.I18n.getMessage("Cancel")).click(function() {
                        $CQ(dialog).remove();
                        that.segmentDialogShown = false;
                    }).appendTo(dialog);

                    $CQ("#CQ").append(dialog);
                });
            }
        },

        toggle: function() {
            if (this.shown) {
                this.hide();
            } else {
                this.show();
            }
        },

        // position the popup above the components dom element
        position: function() {
            if (this.shown) {
                var anchor = $CQ(this.component.element.dom);

                // if the anchor is hidden then most probably the settings dialog is opened
                // in which case we want the experience dialog hidden too
                if ('none' === anchor.css("display")) {
                    this.el.css("display","none");
                } else {
                    this.el.css("top",   anchor.offset().top  + "px")
                        .css("left",  anchor.offset().left + "px")
                        .css("width", anchor.width()       + "px")
                        .css("display", "block");
                    if (this.el.offset().top < 0) {
                        this.el.offset({top: 0});
                    }
                }
            } else {
                this.positionOverlayIcon();
            }
        },

        positionOverlayIcon: function() {
            var anchor = $CQ(this.component.element.dom);

            var ICON_SIZE = 40; // keep in sync with height + width of cq-targeting-launch-icon in target.less

            this.overlayIcon.css("top",  (anchor.offset().top + anchor.height() - ICON_SIZE) + "px")
                            .css("left", (anchor.offset().left + anchor.width() - ICON_SIZE) + "px");
        },

        showOverlayIcon: function() {
            // do not show the overlay icon while the experience popup is open
            // and not if we are in preview mode
            if (!this.shown && !CQ.WCM.isPreviewMode()) {
                if( $CQ.support.opacity) {
                    this.overlayIcon.fadeIn("normal");
                } else {
                    this.overlayIcon.show();
                }
            }
        },

        hideOverlayIcon: function() {
            if( $CQ.support.opacity) {
                this.overlayIcon.fadeOut("fast");
            } else {
                this.overlayIcon.hide();
            }
        },

        show: function() {
            this.shown = true;

            // make sure experiences are loaded if not yet
            // (usually done by ClientContext update already)
            this.reloadExperiencesInPopup();

            // bring into position before fade in
            this.position();

            // do not show the overlay icon while the experience popup is open
            this.hideOverlayIcon();

            if( $CQ.support.opacity) {
                this.el.fadeIn("normal");
            } else {
                this.el.show();
            }
        },

        hide: function() {
            if( $CQ.support.opacity) {
                this.el.fadeOut("fast");
            } else {
                this.el.hide();
            }
            this.shown = false;
        },

        remove: function() {
            this.el.remove();
            this.overlayIcon.remove();
            if (this.missingOfferPlaceholder) {
                this.missingOfferPlaceholder.remove();
            }

            var campaignStore = ClientContext.get("campaign");
            if (campaignStore) {
                campaignStore.removeListener("update", this.onCampaignUpdate);
            }

            CQ.WCM.un("editableready", this.onEditableReady, this);
        }
    };

    /**
     * Gets or creates the TargetEditor object for a target component.
     * @param {CQ.wcm.EditBase} component a CQ.wcm.EditBase instance for a target component
     */
    CQ.personalization.TargetEditor.get = function(component) {
        if (!component.targetEditor) {
            component.targetEditor = new CQ.personalization.TargetEditor(component);
        }
        return component.targetEditor;
    };

}());
