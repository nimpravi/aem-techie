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
CQ_Analytics = CQ_Analytics || {};
CQ_Analytics.AAM = CQ_Analytics.AAM || {};
/**
 * This class shims between the code that loads the dialog, and the LookupDialog enabling
 * sharing. See the beforerender listener in dialog.xml
 */
CQ_Analytics.AAM.LookupDialogCtl = CQ_Analytics.AAM.LookupDialogCtl || function(traitsManager, config) {


    var traitLookupUrl = config.configPath  || "/etc/cloudservices/audiencemanager/geometrixx";
    traitLookupUrl = traitLookupUrl + ".traits.json";

    return {
        init : function(component) {
            return new CQ_Analytics.AAM.LookupDialog(traitsManager, {
                container : component,
                traitLookupUrl : traitLookupUrl
            });

        }
    };
};
/**
 * Lookup dialog manages lookups of traits during client context configuration.
 * It exposes show and hide methods to allow the caller to manage show and hide.
 * It allows the caller to change the callback function as well as setting it in configuration.
 */
CQ_Analytics.AAM.LookupDialog = CQ_Analytics.AAM.LookupDialog || function(traitsManagerInstance, config) {
    "use strict";



    config = config || {};
    // callback which will be replaced when show is called
    var callback = config.callback || function() {};
    var debugMessages = config.debug || false;
    // url where traits are looked up.
    var traitLookupUrl = config.traitLookupUrl;
    // can accept a container to configure rather than creating one.
    var container = config.container;
    // the traitsManager being used, only if invoked from a dialog.xml
    var traitsManager = traitsManagerInstance;


    // traits when show is called.
    var originalTraits = {};
    // traits as the evolve when the dialog is showing.
    var availableTraits = {};
    // map of traits found by the user before they are added to available traits.
    var newOptionsMap = {};


    // traits selection controlls.
    var newTraitsList = null;
    var selectedTraitsList = null;


    var debug = function() {};
    if ( debugMessages ) {
        debug = function(msg) {
            console.log(msg);
        };
    }

    /**
     * Sets the traits at the start of the dialog.
     * @param newOriginalTraits new traits a map of trait objects keyed by traitID.
     * @private
     */
    function setTraits(newOriginalTraits) {
        originalTraits = newOriginalTraits || {};
        availableTraits = {};
        $.each(originalTraits, function(key, val) {
            availableTraits[key] = val;
        });
    }

    setTraits(config.availableTraits);

    /**
     * Set the traits and force a refresh
     * @param new set of original traits.
     * @private
     */
    function setTraitsWithRefresh(newOriginalTraits) {
        setTraits(newOriginalTraits);
        updateListComponent(selectedTraitsList, availableTraits);
    }
    /**
     * Sets the callback.
     * @param oncompleteCallback callback function.
     *  @private
     */
    function setCallback(oncompleteCallback) {
        callback = oncompleteCallback || function() {};
    }

    /**
     * Update the options map with a new set of options, deleting old options.
     * @param newOptions an array of options.
     * @returns the new option map keyed by object id.
     * @private
     */
    function updateOptionsMap(newOptions) {
        var newOptionsMap = {};
        $.each(newOptions, function(index, val) {
            // some feeds use name instead of title.
            if ( val.name && !val.title ) {
                val.title = val.name;
            }
            newOptionsMap[val.id] = val;
        });
        return newOptionsMap;
    }

    /**
     * Update a list component.
     * @param listComponent the list component that has a setOptions function.
     * @param optionsMap map of options to add.
     * @returns the array of option objects as consumed by the list.
     * @private
     */
    function updateListComponent(listComponent, optionsMap) {
        var newOptions = [];
        $.each(optionsMap, function(key, val) {
            if (val) {
                // some feeds use name instead of title.
                if ( val.name && !val.title ) {
                    val.title = val.name;
                }
                newOptions.push({
                    value : key,
                    text : val.title,
                    qtip : key
                });
            }
        });
        listComponent.setOptions(newOptions);
        return newOptions;
    }

    /**
     * Using the CQ  server search for traits. This will after asynchronously calling the server.
     * @param field the field containing the search value.
     * @param newV the new value
     * @param oldV the old value before this update.
     * @param the list to update with results.
     * @private
     */
    function searchForTraits(field, newV, oldV, listToUpdate) {
        if (newV !== oldV) {
            listToUpdate.hide();
            CQ.Ext.Msg.wait(CQ.I18n.getMessage("Searching...."));
            $.getJSON(traitLookupUrl, {
                q : newV
            }, function(result) {
                CQ.Ext.Msg.wait(CQ.I18n.getMessage("Searching....")).hide();
                if (result.traits) {
                    debug("Value set to " + JSON.stringify(result.traits));
                    listToUpdate.options = [];
                    newOptionsMap = updateOptionsMap(result.traits);
                    updateListComponent(listToUpdate, newOptionsMap);
                    listToUpdate.show();

                }
            }).error(function() {
                CQ.Ext.Msg.wait(CQ.I18n.getMessage("Search Failed, please contact support")).hide();
                CQ.Ext.Msg.alert(CQ.I18n.getMessage('Error'), CQ.I18n.getMessage('Search Failed, please contact support'));
            });
        }
    }

    /**
     * Create a lookup dialog. Constructor.
     */
    function newLookupDialog() {

        // build the list of possible traits to add with a listener on selection.
        newTraitsList = CQ.Util.build({
            allowBlank : true,
            fieldLabel : CQ.I18n.getMessage('Traits to add'),
            fieldSubLabel : CQ.I18n.getMessage('select to add'),
            xtype : 'selection',
            type : 'checkbox',
            listeners : {
                selectionchanged : function(list, value, checked) {
                    debug("Adding Trait " + value + " " + checked);
                    if ( checked && value && newOptionsMap[value] ) {
                        // add the new trait to the availableTraits
                        availableTraits[value] = newOptionsMap[value];
                        newOptionsMap[value] = false;
                        // remove from the newTraitsList
                        updateListComponent(newTraitsList, newOptionsMap);
                        // add to the current traits
                        updateListComponent(selectedTraitsList, availableTraits);
                    }
                }
            }
        });
        // hide it till it has some contents.
        newTraitsList.hide();

        // build a list of selected traits, and bind a to the checkbox click to allow removal.
        selectedTraitsList = CQ.Util.build({
            allowBlank : true,
            fieldLabel : CQ.I18n.getMessage('Current traits'),
            fieldSubLabel : CQ.I18n.getMessage('select to remove'),
            xtype : 'selection',
            type : 'checkbox',
            listeners : {
                selectionchanged : function(list, value, checked) {
                    debug("Removeing Trait " + value + " " + checked);
                    if ( checked && value && availableTraits[value] ) {
                        // add the new trait to the availableTraits
                        newOptionsMap[value] = availableTraits[value];
                        availableTraits[value] = false;
                        // remove from the newTraitsList
                        updateListComponent(newTraitsList, newOptionsMap);
                        // add to the current traits
                        updateListComponent(selectedTraitsList, availableTraits);
                    }
                }
            }

        });

        // populate the list with the currently available traits (ie the original traits).
        updateListComponent(selectedTraitsList, availableTraits);

        var trait = null;
        var traitValue = null;

        // build a search box and bind blur and enter to performing the search.
        // we cant use a suggests box because the search is too slow to be usable, taking between 1s and 30s to respond.
        trait = CQ.Util.build({
            allowBlank : true,
            fieldLabel : CQ.I18n.getMessage('Trait'),
            fieldSubLabel : CQ.I18n.getMessage('enter search'),
            url : traitLookupUrl,
            name : 'q',
            xtype : 'textfield',
            listeners : {
                change : function(field, newV, oldV) {
                    searchForTraits(field, newV, oldV, newTraitsList);
                    traitValue = newV;
                },
                specialkey : function(field, e) {
                    debug("Special key ");
                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
                    if (e.getKey() == e.ENTER) {
                        var newV = trait.getValue();
                        searchForTraits(trait, newV, traitValue, newTraitsList);
                        traitValue = newV;
                    }
                }
            }
        });

        var panels = CQ.Util.build({
            "xtype" : 'panel',
            "layout" : 'column',
            "items" : [
                {
                    "xtype" : 'panel',
                    bodyBorder : false,
                    border : false,
                    title : CQ.I18n.getMessage("Search Traits"),
                    columnWidth: 0.5,
                    items : [{
                        "xtype" : 'panel',
                        layout : 'form',
                        bodyBorder : false,
                        border : false,
                        items : [trait, newTraitsList]
                    }]
                },
                {
                    "xtype" : 'panel',
                    bodyBorder : false,
                    border : false,
                    title : CQ.I18n.getMessage("Selected Traits"),
                    columnWidth: 0.5,
                    items : [{
                        "xtype" : 'panel',
                        layout : 'form',
                        bodyBorder : false,
                        border : false,
                        items : [selectedTraitsList]
                    }]
                }
            ]
        });

        // wrap the components up in a dialog
        if ( container ) {
            // invoked by the client context, so insert ourselves into that panel
            // as there are no other hooks.
            container.removeAll();
            container.add(panels);
            // add hooks in
            container.mon(container,{
                'beforesubmit' : function() {
                    // save the state including full trait objects to the
                    // traitsmanager.
                    traitsManager.setAvailableTraits(availableTraits);
                    // stop the container performing  a reload, since we have already
                    // saved and there is no need to reconfigure the control.
                    // normally this would result in reloading all the js files and
                    // the data from the server. This component doesnt need that.
                    container.hide();
                    return false;
                 }
            });
            container.mon(container,{
                 'loadcontent' : function() {
                     // can't use the information from the dialog since that will
                     // not contain all the information needed, so have to use
                     // the data from the traitsManager.
                     traitsManager.getAvailableTraits(setTraitsWithRefresh);
                 }
            });

            return {
                // empty
            };
        } else {
            var searchDialog = new CQ.Dialog({
                "height" : 200,
                "width" : 600,
                "title" : CQ.I18n.getMessage("Manage Traits"),
                "buttons" : [ {
                    "text" : CQ.I18n.getMessage("OK"),
                    "handler" : function() {
                        debug("Saving "+JSON.stringify(availableTraits));
                        callback(availableTraits);
                    }
                }, {
                    "text" : CQ.I18n.getMessage("Cancel"),
                    "handler" : function() {
                        debug("Cancel "+JSON.stringify(originalTraits));
                        callback(originalTraits);
                    }
                } ],
                items : [panels]
            });
            // expose only the functions we want to.
            return {
                /**
                 * Show the dialog.
                 * @param currentAvailableTraits the available traits.
                 * @param oncompleteCallback a callback function to save the selected traits.
                 *           called with a map of traits keyed by id callback(availableTraits)
                 * @returns nothing.
                 */
                show : function(currentAvailableTraits, oncompleteCallback) {
                    setTraits(currentAvailableTraits);
                    setCallback(oncompleteCallback);
                    searchDialog.show();
                },
                /**
                 * hide the dialog, but dont dispose.
                 * @returns
                 */
                hide : function() {
                    searchDialog.hide();
                }
            };
        }



    } // end of constructor function.

    // create an instance using the constructor function.
    return newLookupDialog();

};
