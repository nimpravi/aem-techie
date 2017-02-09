/*******************************************************************************
 *
 * ADOBE CONFIDENTIAL __________________
 *
 * Copyright 2013 Adobe Systems Incorporated All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Adobe Systems Incorporated and its suppliers, if any. The intellectual and
 * technical concepts contained herein are proprietary to Adobe Systems
 * Incorporated and its suppliers and are protected by trade secret or copyright
 * law. Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from Adobe
 * Systems Incorporated.
 ******************************************************************************/
CQ_Analytics = window.CQ_Analytics || {};
CQ_Analytics.AAM = CQ_Analytics.AAM || {};

/**
 * The trait manager manages traits and their refresh after traits have been
 * updated on the Audience Manager.
 */
CQ_Analytics.AAM.TraitsMgr = CQ_Analytics.AAM.TraitsMgr ||
        function(audienceManagerInstance, config) {
            "use strict";

            // using jQuery/closure code style rather than prototype to hide internal private methods.
            // see http://javascript.crockford.com/private.html

            function newAAMTraitsMgr() {


                // save configuration
                // store name for traits
                var storename = config.store_name || "aamtraits";
                // the url where traits are looked up, returns an array of trait objects, query specified in q param.
                var traitLookupUrl = config.configPath  || "/etc/cloudservices/audiencemanager/geometrixx";
                traitLookupUrl = traitLookupUrl + ".traits.json";
                // server url of this configuration where the traits config is saved.
                var availableTraitsUrl = config.pagePath || false;
                var debugMessages = config.debug || false;
                // if set to true, mockup the traits not requiring a server connection.
                var mockup = false;

                var audienceManager = audienceManagerInstance;



                /**
                 * When the control needs re-rendering, set to true.
                 */
                var needsUpdate = true;

                /**
                 * map of traits keyed by ID.
                 */
                var availableTraits = false;

                var currentUserId = false;

                var userTraits = {};

                /**
                 * Template for the chooser
                 */
                var chooserTemplate = function(key, label, checkedClass) {
                    return "<label data-key='" + key + "' class='" + checkedClass + "' >" +
                        "<div class='toggle'><div class='green'></div><div class='red'></div></div>" +
                        label + "</label>";
                };

                // create a new instance from the "factory". (this is the super() call).
                var newStore = CQ_Analytics.PersistedJSONStore.getInstance(storename, null, null, function() {
                    this.init();
                    this.reset();
                });

                var debug = function() {};
                if ( debugMessages ) {
                    debug = function(msg) {
                        console.log("DEBUG aamtraits.js: "+msg);
                    };
                }
                var error = function(msg) {
                    console.log("ERROR aamtraits.js: "+msg);
                };


                debug("Created newStore as " + newStore);

                /**
                 * Get the context store loader url for audience manager.
                 */
                function getLoaderUrl() {
                    return CQ_Analytics.ClientContextMgr.getClientContextURL("/contextstores/audiencemanager/loader.json");
                }

                /**
                 * Load the traits selected for the user.
                 */
                function loadUserTraits(userId, callback) {

                    // need to reload from http://localhost:4502/etc/clientcontext/default/contextstores/profiledata/loader.json?authorizableId=aparker%40geometrixx.info
                    // suitable for this store

                    var loaderUrl = getLoaderUrl();
                    $.getJSON(loaderUrl, {
                            authorizableId : userId
                        },
                        function(response) {
                            currentUserId = userId;
                            userTraits = {};
                            if ( response.selectedTraits ) {
                                debug("Loaded User Triats for user "+currentUserId+" as "+response.selectedTraits);
                                userTraits = $.parseJSON(response.selectedTraits);
                            } else {
                                debug("Loaded User Triats for user "+currentUserId+" as "+response.selectedTraits);
                            }
                            callback();
                    }).fail(function(jqXHR, textStatus, errorThrown) {
                        error("Loaded User Triats for user "+currentUserId+" gave error "+textStatus);
                        currentUserId = userId;
                        userTraits = {};
                        callback();
                    });
                }

                /**
                 * save the currently selected traits.
                 */
                function saveUserTraits() {
                    if ( !currentUserId ) {
                        currentUserId = CQ_Analytics.ProfileDataMgr.getProperty("authorizableId");
                    }
                    var loaderUrl = getLoaderUrl();
                    debug("Saving User Triats for user "+currentUserId);
                    $.post(loaderUrl, {
                            authorizableId : currentUserId,
                            selectedTraits : JSON.stringify(userTraits)
                        }, function(data, textStatus, jqXHR) {
                            if ( jqXHR.status !== 200 ) {
                                error("Unable to update saved traits for user, "+textStatus+" please investigate, POST was to "+loaderUrl+" "+errorThrown);
                            }
                        }, "json").fail(function(jqXHR, textStatus, errorThrown) {
                            error("Unable to update saved traits for user, "+textStatus+" please investigate, POST was to "+loaderUrl+" "+errorThrown);
                        });
                }

                /**
                 * Change the context based on a profile change. Resets the
                 * cookie and latitude and longitude.
                 */
                function changeProfile() {
                    var userId = CQ_Analytics.ProfileDataMgr.getProperty("authorizableId");
                    debug("User id "+userId+" currentUser id "+currentUserId);
                    if (currentUserId !== userId) {
                        // set while performing load to avoid race.
                        currentUserId = userId;
                        userTraits = {};
                        debug("Perforing Load User traits");
                        loadUserTraits(userId, function() {
                            needsUpdate = true;
                            newStore.fireEvent("update");
                            resolveSegments();
                            debug("Done resolving segments after change of profile.");
                        });
                    }
                }

                /**
                 * Allow the user to edit the aviable traits (or mockup the operation).
                 * @param callback a function(availableTraits) where avaialbeTraits is a map of trait objects keyed by id.
                 */
                function selectAvailableTraits(callback) {
                    if (mockup) {
                        availableTraits = {
                                73801 : {
                                    title : "Trait 73801"
                                },
                                73802 : {
                                    title : "Trait 73801"
                                },
                                73803 : {
                                    title : "Trait 73801"
                                },
                                73804 : {
                                    title : "Trait 73801"
                                }
                        };
                        callback();
                    } else {
                        if ( false ) {
                            // enable this if you want the add traits dialog to appear
                            // when there are no traits.
                            // this could be per instance.
                            var dialog = CQ_Analytics.AAM.LookupDialog({
                                traitLookupUrl : traitLookupUrl
                            });
                            dialog.show(availableTraits, function(newtraits) {
                                    dialog.hide();
                                    availableTraits = newtraits;
                                    saveAvailableTraits(callback);
                            });
                        } else {
                            availableTraits = {};
                            callback();
                        }
                    }

                }
                /**
                 * Save the to the config as a json encoded block. No need to have this as separate values.
                 * @param callback function(availableTraits) called once save is posted (async).
                 */
                function saveAvailableTraits(callback) {
                    if ( availableTraitsUrl ) {
                        $.post(availableTraitsUrl, {
                            availableTraits : JSON.stringify(availableTraits)
                        }, function() {
                            debug("Saved Traits");
                        }).error(function(){
                            debug("Failed to save trats");
                        });
                    } else {
                        debug("Not saving traits config, no url to save to.");
                    }
                    callback();
                }

                /**
                 * Load available traits and call the callback.
                 * @param callback function(availableTraits)
                 */
                function loadTraitsList(callback) {
                    // set available traits to nothing while we load.
                    // this prevents a race when multiple events are triggering load.
                    availableTraits = {};
                    // load from the config url.
                    if ( availableTraitsUrl ) {
                        debug("Loading traits from "+availableTraitsUrl);

                        $.getJSON(availableTraitsUrl + ".json", function(response) {
                            if ( response.availableTraits ) {
                                try {
                                    // save what was loaded and callback.
                                    availableTraits = $.parseJSON(response.availableTraits);
                                    callback();
                                } catch (e) {
                                    error(" Avaialable Traits were invalid, error, reloading "+e.stack);
                                    // loading failed, give the user an option to try and load
                                    selectAvailableTraits(callback);
                                }
                            } else {
                                error(" No traits found  "+JSON.stringify(response));
                                selectAvailableTraits(callback);
                            }
                        }).error(function() {
                            error("Error Loading traits. ");
                            // loading failed, give the user an option to try and load
                            selectAvailableTraits(callback);
                        });
                    } else {
                        error(" No traits url  ");

                        // loading failed, give the user an option to try and load
                        selectAvailableTraits(callback);
                    }
                }


                /**
                 * converts the current set of enabled traits into a set of segments and updates the segment manager.
                 */
                function resolveSegments() {
                    var traitIds = [];
                    debug("Resolving segments for user traits ");



                    $.each(userTraits, function(key, value){
                        if (value && availableTraits[key]) {
                            traitIds.push(key);
                        }
                    });
                    if ( traitIds.length === 0 ) {
                        traitIds.push(-1);
                    }
                    var signals = {
                          sid : traitIds
                    };

                    // signal the audience manager with new trait ids
                    audienceManager.invoke(signals);
                }

                /**
                 * render the controls
                 * @param store the store being rendered.
                 * @param divId. The ID of the div where the store should be rendered.
                 * @returns void
                 */
                function internalRenderer(store, divId) {
                    if (needsUpdate) {
                        debug("Performing internal render");
                        needsUpdate = false;
                        var traitDiv = $("#" + divId);
                        traitDiv.children().remove();
                        debug("Starting to render Traits "+JSON.stringify(availableTraits));
                        $.each(availableTraits, function(key, value) {
                            if (value) {
                                debug("Info " + JSON.stringify(value));
                                var checked = (userTraits[key]);
                                traitDiv.prepend(chooserTemplate(key, value.title, checked?'checked':'' ));
                            }
                        });
                        // attach an event handler to all the traits to
                        // manage
                        // selection and deselection.
                        $("label", traitDiv).each(function() {
                            debug("Binding to "+this.id);
                        });
                        $("label", traitDiv).click(function() {
                            var key = $(this).data("key");
                            debug("Clicked "+key+" current state "+$(this).hasClass("checked"));
                            if ($(this).hasClass("checked")) {
                                $(this).removeClass('checked');
                                delete userTraits[key];
                                debug("Trait " + key + " Off ");
                            } else {
                                $(this).addClass('checked');
                                userTraits[key] = true;
                                debug("Trait " + key + " On ");
                            }
                            saveUserTraits();
                            resolveSegments();
                            // IE7 will bubble event and cause double click.
                            return false;
                        });
                        debug("Done render Traits ");
                    }
                }


                // bind the public methods to new store.
                /**
                 * Render function.
                 */
                newStore.renderer = internalRenderer;

                /**
                 * Save new Set of traits
                 * @param newAvailableTraits new map of available traits keyed by id.
                 * @public
                 */
                newStore.setAvailableTraits = function(newAvailableTraits) {
                    availableTraits = newAvailableTraits;
                    debug("Saving Traits "+JSON.stringify(availableTraits));
                    saveAvailableTraits(function(){
                        needsUpdate = true;
                        newStore.fireEvent("update");
                        debug("Fired update");
                    });
                };

                /**
                 * Load traits
                 * @param a callback to call when the traits are loaded.
                 * @public
                 */
                newStore.getAvailableTraits = function(callback) {
                    callback(availableTraits);
                };



                // Load the traits and once loaded register handlers and register the store.
                loadTraitsList(function() {
                    // don't register the store until we have loaded available traits.
                    // or else rendering will happen before ready.
                    debug("Available traits loaded "+JSON.stringify(availableTraits)+" registering this store");
                    CQ_Analytics.ClientContextUtils.onStoreRegistered("profile", function(profileStore) {
                        changeProfile();
                        profileStore.addListener("update", changeProfile);
                        debug("Registered this agains profile store, and loaded current");
                    });
                    CQ_Analytics.CCM.register(newStore);
                });
                // make certain the profile is loaded.
                debug("New Trait Manager created " + newStore);

                return newStore;

            } // end of constructor.


            // create a new Traits Manager using the constructor.
            return newAAMTraitsMgr(config);

        };

