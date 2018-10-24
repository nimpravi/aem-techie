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
 * @class CQ.form.Slideshow.TitlePanel
 * @extends CQ.Ext.Panel
 * @private
 * The TitlePanel provides the UI to edit the title of each slide.
 * @constructor
 * Creates a new TitlePanel.
 * @param {Object} config The config object
 */
CQ.form.Slideshow.TitlePanel = CQ.Ext.extend(CQ.Ext.Panel, {

    constructor: function(config) {

        config = config || { };
        var defaults = {
            "layout": "table",
            "layoutConfig": {
                "columns": 2
            },
            "defaults": {
                "style": "padding: 3px;"
            },
            "minSize": 30,
            "maxSize": 30,
            "height": 30,
            "items": [{
                    "itemId": "titleLabel",
                    "xtype": "label",
                    "text": CQ.I18n.getMessage("Title")
                }, {
                    "itemId": "titlePanel",
                    "xtype": "panel",
                    "layout": "fit",
                    "border": false,
                    "hideBorders": true,
                    "items": [{
                            "itemId": "title",
                            "xtype": "textfield"
                        }
                    ]
                }
            ],
            "listeners": {
                "bodyresize": {
                    "fn": function(comp, w, h) {
                        this.adjustTitleWidth(w);
                    },
                    "scope": this
                }
            }
        };

        CQ.Util.applyDefaults(config, defaults);
        CQ.form.Slideshow.TitlePanel.superclass.constructor.call(this, config);

    },

    initComponent: function() {
        CQ.form.Slideshow.SlidesPanel.superclass.initComponent.call(this);
    },

    afterRender: function() {
        CQ.form.Slideshow.TitlePanel.superclass.afterRender.call(this);
        this.el.setVisibilityMode(CQ.Ext.Element.DISPLAY);
        this.body.setVisibilityMode(CQ.Ext.Element.DISPLAY);
    },

    adjustTitleWidth: function(width) {
        if (width) {
            var titlePanel = this.items.get("titlePanel");
            // var selector = titlePanel.items.get("selector");
            var titleLabel = this.items.get("titleLabel");
            if (titleLabel.rendered) {
                var titleWidth = width - titleLabel.getEl().getWidth();
                titlePanel.setSize(titleWidth, 30);
            } else {
                this._width = width;
            }
        }
    },

    setTitle: function(title) {
        var titlePanel = this.items.get("titlePanel");
        var titleField = titlePanel.items.get("title");
        alert("titleField: "+titleField);
        titleField.setValue(title ? title : "");
    },

    getTitle: function() {
        var titlePanel = this.items.get("titlePanel");
        var titleField = titlePanel.items.get("title");
        return titleField.getValue();
    },

    disableFormElements: function() {
        var titlePanel = this.items.get("titlePanel");
        var titleField = titlePanel.items.get("title");
        titleField.disable();
    },

    enableFormElements: function() {
        var titlePanel = this.items.get("titlePanel");
        var titleField = titlePanel.items.get("title");
        titleField.enable();
    }

});