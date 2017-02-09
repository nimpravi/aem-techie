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
/**
 * @class CQ.personalization.ProfileLoader
 * @extends CQ.Dialog
 * ProfileLoader is a dialog providing functionalities to select, load a profile and update the
 * <code>{@link CQ_Analytics.ProfileDataMgr}</code>.
 * @constructor
 * Creates a new ProfileLoader.
 * @param {Object} config The config object
 */
CQ.personalization.ProfileLoader = CQ.Ext.extend(CQ.Dialog, {
    constructor: function(config) {
        config = (!config ? {} : config);

        var profileCombo = new CQ.Ext.form.ComboBox({
            "fieldLabel": CQ.I18n.getMessage("Select profile"),
            "name": "profile",
            "cls": "cq-eclickstreamcloud",
            "stateful": false,
            "typeAhead":true,
            "triggerAction":"all",
            "inputType":"text",
            "displayField":"name",
            "valueField": "id",
            "emptyText": "",
            "minChars":0,
            "editable":true,
            "lazyInit": false,
            "queryParam": "filter",
            "triggerScrollOffset": 80,
            "listeners": {
                "render": function(comp) {
                    var scroller = $CQ(comp.innerList.dom);

                    if (!scroller) {
                        return;
                    }

                    scroller.on('scroll', function(e) {
                        if (comp.refreshing || comp.loading || (comp.store.getCount() >= comp.store.getTotalCount())) {
                            return;
                        }

                        if ((this.scrollTop > 0) && ((this.scrollTop + this.clientHeight + comp.triggerScrollOffset) >= this.scrollHeight)) {
                            if (!comp.moreStore) {
                                comp.moreStore = new CQ.Ext.data.GroupingStore({
                                    "proxy": new CQ.Ext.data.HttpProxy({
                                        "url": comp.store.proxy.url,
                                        "method": comp.store.proxy.conn.method
                                    }),
                                    "reader": comp.store.reader,
                                    "listeners": {
                                        "load": function() {
                                            if (comp.loadingIndicator) {
                                                comp.loadingIndicator.remove();
                                                comp.loadingIndicator = undefined;
                                            }

                                            for (var i = 0; i < comp.moreStore.getCount(); i++) {
                                                var record = comp.moreStore.getAt(i);
                                                comp.store.add(record);
                                            }

                                            comp.refreshing = false;
                                        }
                                    },
                                    "dataView": this
                                });
                            }

                            var lastParams = comp.store.lastOptions ? comp.store.lastOptions.params : comp.store.baseParams;
                            var moreParams = $CQ.extend({}, lastParams, {
                                'limit': comp.store.baseParams ? comp.store.baseParams.limit : 25,
                                'start': comp.store.getCount()
                            });

                            comp.loadingIndicator = comp.innerList.createChild({'tag': 'div', "cls": "loading-indicator", 'html': CQ.I18n.getMessage("Loading...")});
                            comp.refreshing = true;
                            comp.moreStore.load({
                                "params": moreParams
                            });
                        }
                    });

                }
            },
            "fieldDescription": CQ.I18n.getMessage("Select the profile you want to load."),
            "tpl" :new CQ.Ext.XTemplate(
                '<tpl for=".">',
                '<div class="cq-eclickstreamcloud-list">',
                '<div class="cq-eclickstreamcloud-list-entry">{[values.name==""? values.id: CQ.shared.XSS.getXSSTablePropertyValue(values, "name")]}</div>',
                '</div>',
                '</tpl>'),
            "itemSelector" :"div.cq-eclickstreamcloud-list",
            "store": new CQ.Ext.data.Store({
                "autoLoad":false,
                "proxy": new CQ.Ext.data.HttpProxy({
                    "url": "/bin/security/authorizables.json",
                    "method":"GET"
                }),
                "baseParams": {
                    "start": 0,
                    "limit": 15,
                    "hideGroups": "true"
                },
                "reader": new CQ.Ext.data.JsonReader({
                    "root":"authorizables",
                    "totalProperty":"results",
                    "id":"id",
                    "fields":["name", "name" + CQ.shared.XSS.KEY_SUFFIX,"id", "home"]})
            }),
            "defaultValue": ""
        });

        var currentObj = this;
        var defaults = {
            "height": 170,
            "width": 400,
            "title": CQ.I18n.getMessage("Profile Loader"),
            "items": {
                "xtype": "panel",
                items: [profileCombo]
            },
            "buttons": [
                {
                    "text": CQ.I18n.getMessage("OK"),
                    "handler":function() {
                        CQ_Analytics.ProfileDataMgr.loadProfile(profileCombo.getValue());
                        // Refresh Test & Target info for new user:
						if (CQ_Analytics.TestTarget && CQ_Analytics.TestTarget.deleteMboxCookies) {
							CQ_Analytics.TestTarget.deleteMboxCookies();
						}
                        currentObj.hide();
                    }
                },
                CQ.Dialog.CANCEL
            ]
        };

        CQ.Util.applyDefaults(config, defaults);

        // init component by calling super constructor
        CQ.personalization.ProfileLoader.superclass.constructor.call(this, config);
    }
});
