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
 * @class CQ.form.Slideshow.SlidesPanel
 * @extends CQ.Ext.Panel
 * @private
 * The SlidesPanel provides the UI to add, remove and select slides.
 * @constructor
 * Creates a new SlidePanel.
 * @param {Object} config The config object
 */
CQ.form.Slideshow.SlidesPanel = CQ.Ext.extend(CQ.Ext.Panel, {

    constructor: function(config) {

        var parentScope = this;

        // lazy initialization does not work on Firefox for these buttons, so instantiating
        // them the old way ...
        var addButton = new CQ.Ext.Button({
            "itemId": "addButton",
            "xtype": "button",
            "text": CQ.I18n.getMessage("Add"),
            "afterRender": function() {
                CQ.Ext.Button.prototype.afterRender.call(this);
                if (parentScope._width) {
                    parentScope.adjustSelectorWidth(parentScope._width)
                }
            },
            "handler": function() {
                if (parentScope.onAddButton) {
                    parentScope.onAddButton();
                }
            }
        });
        var removeButton = new CQ.Ext.Button({
            "itemId": "removeButton",
            "xtype": "button",
            "text": CQ.I18n.getMessage("Remove"),
            "afterRender": function() {
                CQ.Ext.Button.prototype.afterRender.call(this);
                if (parentScope._width) {
                    parentScope.adjustSelectorWidth(parentScope._width)
                }
            },
            "handler": function() {
                if (parentScope.onRemoveButton) {
                    parentScope.onRemoveButton();
                }
            }
        });

        config = config || { };
        var defaults = {
            "layout": "table",
            "layoutConfig": {
                "columns": 3
            },
            "defaults": {
                "style": "padding: 3px;"
            },
            "minSize": 30,
            "maxSize": 30,
            "height": 30,
            "items": [{
                    "itemId": "slideSelector",
                    "xtype": "panel",
                    "layout": "fit",
                    "border": false,
                    "height": 30,
                    "hideBorders": true,
                    "items": [{
                            "itemId": "selector",
                            "xtype": "selection",
                            "type": "select",
                            "listeners": {
                                "selectionchanged": {
                                    "fn": function(comp, value) {
                                        if (this.onSlideChanged) {
                                            this.onSlideChanged(value);
                                        }
                                    },
                                    "scope": this
                                }
                            }
                        }]
                },
                addButton,
                removeButton
            ],
            "listeners": {
                "bodyresize": {
                    "fn": function(comp, w, h) {
                        this.adjustSelectorWidth(w);
                    },
                    "scope": this
                }
            }
        };

        CQ.Util.applyDefaults(config, defaults);
        CQ.form.Slideshow.SlidesPanel.superclass.constructor.call(this, config);
    },

    initComponent: function() {
        CQ.form.Slideshow.SlidesPanel.superclass.initComponent.call(this);
    },

    afterRender: function() {
        CQ.form.Slideshow.SlidesPanel.superclass.afterRender.call(this);
        this.el.setVisibilityMode(CQ.Ext.Element.DISPLAY);
        this.body.setVisibilityMode(CQ.Ext.Element.DISPLAY);
    },

    adjustSelectorWidth: function(width) {
        if (width) {
            var selectorPanel = this.items.get("slideSelector");
            var addButton = this.items.get("addButton");
            var removeButton = this.items.get("removeButton");
            if (addButton.rendered && removeButton.rendered) {
                var selWidth = width
                    - addButton.getEl().getWidth() - removeButton.getEl().getWidth();
                selectorPanel.setSize(selWidth, 30);
                var selector = selectorPanel.items.get("selector");
                selector.setSize(selWidth, addButton.getEl().getHeight());
            } else {
                this._width = width;
            }
        }
    },

    setInitialComboBoxContent: function(data) {
        var selector = this.items.get("slideSelector").items.get("selector");
        selector.setOptions(data);
    },

    select: function(slide) {
        var selector = this.items.get("slideSelector").items.get("selector");
        selector.suspendEvents();
        if (slide) {
            selector.setValue(slide);
        } else {
            selector.setValue(null);
        }
        selector.resumeEvents();
    },

    updateSlide: function(slide) {
        if (slide) {
            var selector = this.items.get("slideSelector").items.get("selector");
            var store = selector.comboBox.store;
            var rowCnt = store.getTotalCount();
            for (var row = 0; row < rowCnt; row++) {
                var rowData = store.getAt(row);
                if (rowData.get("value") == slide) {
                    rowData.set("text", slide.createDisplayText());
                }
            }
            store.commitChanges();
        }
    },

    disableFormElements: function() {
        var selector = this.items.get("slideSelector").items.get("selector");
        selector.disable();
    },

    enableFormElements: function() {
        var selector = this.items.get("slideSelector").items.get("selector");
        selector.enable();
    }

});
