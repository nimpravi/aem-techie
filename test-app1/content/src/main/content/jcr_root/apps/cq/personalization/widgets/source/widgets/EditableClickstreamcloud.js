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
 * @class CQ.personalization.EditableClickstreamcloud
 * @extends CQ.Dialog
 * EditableClickstreamcloud is a dialog allowing to edit the Clickstreamcloud by providing access to the properties
 * of the Clickstreamcloud session stores.
 * It mainly contains {@link CQ.personalization.EditableClickstreamcloud.FormSection FormSection}.
 * @deprecated since 5.5
 * @constructor
 * Creates a new EditableClickstreamcloud.
 * @param {Object} config The config object
 */
CQ.personalization.EditableClickstreamcloud = CQ.Ext.extend(CQ.Dialog, {
    constructor: function(config) {
        config = (!config ? {} : config);
        this.fieldsContainer = new CQ.Ext.TabPanel({});
        var currentObj = this;
        var defaults = {
            "id": "cq-editable-clickstreamcloud",
            "title": CQ.I18n.getMessage("Edit the current Clickstream Cloud"),
            "width": 400,
            "height": 400,
            "warnIfModified": false,
            "animCollapse": false,
            "collapsible": true,
            "stateful": true,
            "items": this.fieldsContainer,
            "buttons": [{
                "text": CQ.I18n.getMessage("Add"),
                "tooltip": CQ.I18n.getMessage("Add a new property"),
                "handler": function() {
                    var section = currentObj.getActiveSection();
                    if (section) {
                        section.addFieldHandler();
                    }
                }
            },{
                "text": CQ.I18n.getMessage("Remove"),
                "tooltip": CQ.I18n.getMessage("Remove the selected property"),
                "handler": function() {
                    var section = currentObj.getActiveSection();
                    if (section) {
                        section.removeFieldHandler();
                    }
                },
                "listeners": {
                    "mouseover": function() {
                        var section = currentObj.getActiveSection();
                        if (section) {
                            if (section.lastSelectedItem) {
                                section.lastSelectedItemToDelete = section.lastSelectedItem;
                            } else {
                                section.lastSelectedItemToDelete = null;
                            }
                        }
                    },
                    "mouseout": function() {
                        var section = currentObj.getActiveSection();
                        if (section) {
                            section.lastSelectedItemToDelete = null;
                        }
                    }
                }
            },{
                "text": CQ.I18n.getMessage("Reset"),
                "tooltip": CQ.I18n.getMessage("Revert the current properties to the intial values"),
                "handler": function() {
                    var section = currentObj.getActiveSection();
                    if (section) {
                        section.reset();
                    }
                }
            },{
                "text": CQ.I18n.getMessage("Done"),
                "tooltip": CQ.I18n.getMessage("Close the current dialog"),
                "handler": function() {
                    currentObj.hide();
                }
            }],
            "listeners": {
            	"beforeshow": function(cmp) {
                    if(CQ_Analytics.Sitecatalyst) {
                    	currentObj.reload();
                    }
            	}
            }
        };

        CQ.Util.applyDefaults(config, defaults);

        // init component by calling super constructor
        CQ.personalization.EditableClickstreamcloud.superclass.constructor.call(this, config);
    },

    /**
     * Returns the active displayed section.
     * @return {CQ.personalization.EditableClickstreamcloud.FormSection} The active section.
     * @private
     */
    getActiveSection: function() {
        return this.fieldsContainer.layout.activeItem;
    },

    /**
     * Adds the given section to the main tab.
     * @param {CQ.personalization.EditableClickstreamcloud.FormSection} section Section to add.
     * @private
     */
    addSection: function(section) {
        if (section) {
            this.fieldsContainer.add(section);
            this.fieldsContainer.doLayout();
            var ai = this.getActiveSection();
            if( !ai ) {
                this.fieldsContainer.setActiveTab(0);
            }
        }
    },

    /**
     * Registers a session store to the current EditableClickstreamcloud.
     * @param {Object} config Config object. Expected configs are: <ul>
     * <li><code>sessionStore:</code> session store to be editable</li>
     * <li><code>mode:</code> one of the following UI modes: <code>{@link #EditableClickstreamcloud.MODE_TEXTFIELD MODE_TEXTFIELD}</code>,
     * <code>{@link #EditableClickstreamcloud.MODE_LINK MODE_LINK}</code>
     * or <code>{@link #EditableClickstreamcloud.MODE_STATIC MODE_STATIC}</code> (default)</li>
     * <li><code>title:</code> section title</li>
     * <li><code>sectionConfig:</code> initial section config</li>
     * </ul>
     */
    register: function(config /*sessionStore, mode, title, sectionConfig*/) {
        var section = new CQ.personalization.EditableClickstreamcloud.FormSection(config);
        this.addSection(section);
    },

    /**
     * Reloads each of the contained sections.
     * @private
     */
    reload: function() {
        this.fieldsContainer.items.each(function(item,index,length) {
            if(item.reload) {
                item.reload();
            }
            return true;
        });
    }
});

/**
 * @class CQ.personalization.EditableClickstreamcloud.FormSection
 * @extends CQ.Ext.Panel
 * FormSection is a panel providing UI to access and edit the properties of a Clickstreamcloud session store.
 * @deprecated since 5.5
 * @constructor
 * Creates a new FormSection.
 * @param {Object} config The config object
 */
CQ.personalization.EditableClickstreamcloud.FormSection = CQ.Ext.extend(CQ.Ext.Panel, {
    /**
     * @cfg {CQ.form.Field} newPropertyNameField
     * The field config to specify the name of a new property (defaults to a textfield).
     */
    newPropertyNameField: null,

    /**
     * @cfg {CQ.form.Field} newPropertyValueField
     * The field config to specify the value of a new property (defaults to a textfield).
     */
    newPropertyValueField: null,

    /**
     * @cfg {String} mode Display mode
     * Session store properties will be displayed depending on this property with:<ul>
     * <li><code>{@link CQ.personalization.EditableClickstreamcloud#EditableClickstreamcloud.MODE_TEXTFIELD EditableClickstreamcloud.MODE_TEXTFIELD}:</code> a textfield</li>
     * <li><code>{@link CQ.personalization.EditableClickstreamcloud#EditableClickstreamcloud.MODE_LINK EditableClickstreamcloud.MODE_LINK}:</code> a link (not editable)</li>
     * <li><code>{@link CQ.personalization.EditableClickstreamcloud#EditableClickstreamcloud.MODE_STATIC EditableClickstreamcloud.MODE_STATIC}</code> (default): a static text (not editable)</li>
     * </ul>
     */
    mode: null,

    /**
     * @cfg {CQ_Analytics.SessionStore} sessionStore
     * The session store to display and edit.
     */
    sessionStore: null,

    /**
     * @cfg {String} title
     * The section title.
     */
    title: null,

    constructor: function(config) {
        config = (!config ? {} : config);

        config.newPropertyNameField = config.newPropertyNameField || {};
        config.newPropertyValueField = config.newPropertyValueField || {};

        var currentObj = this;
        var defaults = {
            "layout": "form",
            "autoScroll": true,
            "bodyStyle": CQ.themes.Dialog.TAB_BODY_STYLE,
            "labelWidth": CQ.themes.Dialog.LABEL_WIDTH,
            "defaultType": "textfield",
            "stateful": false,
            "border": false,
            "defaults": {
                "anchor": CQ.themes.Dialog.ANCHOR,
                "stateful": false
            }
        };

        CQ.Util.applyDefaults(config, defaults);

        // init component by calling super constructor
        CQ.personalization.EditableClickstreamcloud.FormSection.superclass.constructor.call(this, config);
    },

    initComponent: function() {
        CQ.personalization.EditableClickstreamcloud.FormSection.superclass.initComponent.call(this);
        this.loadFields();
    },

    /**
     * Resets the session store and reloads the fields.
     */
    reset: function() {
        this.sessionStore.reset();
        for (var i = this.items.items.length - 1; i >= 0; i--) {
            this.remove(this.items.items[i]);
        }

        this.reload();
    },

    /**
     * Reloads the fields.
     */
    reload: function() {
        this.removeAllFields();
        this.loadFields();
        this.doLayout();
    },

    /**
     * Shows a dialog used to add a property/value pair in the store.
     * New property name field is defined set by {@link newPropertyNameField} config.
     * New property value field is defined set by {@link newPropertyNameField} config.
     * @private
     */
    addFieldHandler: function() {
        var currentObj = this;

        var newPropertyNameConfig = CQ.Util.applyDefaults(this.newPropertyNameField, {
            "xtype": "textfield",
            "name": "newPropertyName",
            "fieldLabel": CQ.I18n.getMessage("Name"),
            "allowBlank": false
        });

        var newPropertyName = CQ.Util.build(newPropertyNameConfig);

        var newPropertyValueConfig = CQ.Util.applyDefaults(this.newPropertyValueField, {
            "xtype": "textfield",
            "name": "newPropertyValue",
            "fieldLabel": CQ.I18n.getMessage("Value")
        });

        var newPropertyValue = CQ.Util.build(newPropertyValueConfig);

        var dialog = new CQ.Dialog({
            "height": 250,
            "width": 400,
            "title": CQ.I18n.getMessage("Add new property to {0}", this.title),
            "items": {
                "xtype": "panel",
                items: [newPropertyName, newPropertyValue]
            },
            "buttons": [
                {
                    "text": CQ.I18n.getMessage("OK"),
                    "handler":function() {
                        if (newPropertyName.isValid()) {
                            var names = newPropertyName.getValue();
                            if (!(names instanceof Array)) {
                                names = [names];
                            }
                            var labels = null;
                            if (newPropertyName.getLabel) {
                                labels = newPropertyName.getLabel();
                                if (!labels instanceof Array) {
                                    labels = [labels];
                                }
                            }
                            for (var i = 0; i < names.length; i++) {
                                var name = names[i];
                                var label = (labels != null && i < labels.length) ? labels[i] : names[i];
                                var value = newPropertyValue.getValue();
                                currentObj.sessionStore.setProperty(name, value);
                                currentObj.addField(label, value, name);
                            }
                            currentObj.doLayout();
                            dialog.hide();
                        }
                    }
                },
                CQ.Dialog.CANCEL
            ]});
        dialog.show();
    },

    /**
     * Removes the selected field.
     * @private
     */
    removeFieldHandler: function() {
        if (this.lastSelectedItemToDelete) {
            this.sessionStore.removeProperty(this.lastSelectedItemToDelete.getName());
            this.remove(this.lastSelectedItemToDelete);
            this.lastSelectedItemToDelete = null;
        }
    },

    /**
     * Removes all the fields.
     * @private
     */
    removeAllFields: function() {
        if( this.items ) {
            this.items.each(function(item,index,length) {
                this.remove(item);
                return true;
            },this);
        }
    },

    /**
     * Loads a field for each non invisible session store property.
     * @private
     */
    loadFields: function() {
        var storeConfig = CQ_Analytics.CCM.getStoreConfig(this.sessionStore.getName());
        var names = this.sessionStore.getPropertyNames(storeConfig["invisible"]);
        for (var i = 0; i < names.length; i++) {
            var name = names[i];

            //exclude xss properties
            if( !CQ.shared.XSS.KEY_REGEXP.test(name)) {
                this.addField(this.sessionStore.getLabel(name), this.sessionStore.getProperty(name, true), name, this.sessionStore.getLink(name));
            }
        }
    },

    /**
     * Adds a field to the section.
     * @param {String} label Label.
     * @param {String} value Value.
     * @param {String} name Name.
     * @param {String} link (optional) Link (only if section mode is <code>{@link CQ.personalization.EditableClickstreamcloud#EditableClickstreamcloud.MODE_LINK EditableClickstreamcloud.MODE_LINK}</code>)
     */
    addField: function(label, value, name, link) {
        if (this.mode == CQ.personalization.EditableClickstreamcloud.MODE_TEXTFIELD) {
        	 if(!CQ_Analytics.Sitecatalyst) {
        		 this.addTextField(label, value, name);
        	 } else {
        		 this.addTriggerField(label, value, name);
        	 }
        } else {
            if (this.mode == CQ.personalization.EditableClickstreamcloud.MODE_LINK && link) {
                this.addLink(label, link);
            } else {
                this.addStaticText(label);
            }
        }
    },

    /**
     * Handles a property change: updates the session store.
     * @param {String} name Property name.
     * @param {String} newValue The new value.
     * @param {String} oldValue The old value.
     * @private
     */
    onPropertyChange: function(name, newValue, oldValue) {
        //copy property value to xss property for display
        if( this.sessionStore.getPropertyNames().indexOf(name + CQ.shared.XSS.KEY_SUFFIX) != -1) {
            this.sessionStore.setProperty(name + CQ.shared.XSS.KEY_SUFFIX, newValue);
        }
        this.sessionStore.setProperty(name, newValue);
    },

    /**
     * Add a triggerfield to the section.
     * @param {String} label Label.
     * @param {String} value Default value.
     * @param {String} name Field name.
     */
    addTriggerField: function(label, value, name) {
    	var currentObj = this;

        var tf = new CQ.Ext.form.TriggerField({
            "fieldLabel": label,
            "value": value,
            "name": name,
            "listeners": {
                "change": function(field, newValue, oldValue) {
                    currentObj.onPropertyChange(name, newValue, oldValue);
                },
                "destroy": function() {
                    if( this.container ) {
                        this.container.parent().remove();
                    }
                },
                "focus": function() {
                    currentObj.lastSelectedItem = tf;
                },
                "blur": function() {
                    if (currentObj.lastSelectedItem === tf) {
                        currentObj.lastSelectedItem = null;
                    }
                }
            }
        });

        tf.onTriggerClick = function(e) {
            var dialog = new CQ.personalization.SitecatalystDialog({
                profileLabel: currentObj.sessionStore.STORENAME + "." + label
            });
            dialog.show();
            dialog.alignToViewport("c");
        };

        this.add(tf);
    },

    /**
     * Adds a textfield to the section.
     * @param {String} label Label.
     * @param {String} value Default value.
     * @param {String} name Field name.
     */
    addTextField: function(label, value, name) {
        var currentObj = this;

        var tf = new CQ.Ext.form.TriggerField({
            "fieldLabel": label,
            "value": value,
            "name": name,
            "listeners": {
                "change": function(field, newValue, oldValue) {
                    currentObj.onPropertyChange(name, newValue, oldValue);
                },
                "destroy": function() {
                    if( this.container ) {
                        this.container.parent().remove();
                    }
                },
                "focus": function() {
                    currentObj.lastSelectedItem = tf;
                },
                "blur": function() {
                    if (currentObj.lastSelectedItem === tf) {
                        currentObj.lastSelectedItem = null;
                    }
                }
            }
        });

        this.add(tf);
    },

    /**
     * Adds a link to the section.
     * @param {String} text Link text.
     * @param {String} href Link href.
     */
    addLink: function(text, href) {
        if (href) {
            this.add(new CQ.Static({
                "html": "<a href=" + href + ">" + text + "</a>"
            }));
        } else {
            this.addStaticText(text);
        }
    },

    /**
     * Adds a static text to the section.
     * @param {String} text Text to add.
     */
    addStaticText: function(text) {
        if (text) {
            this.add(new CQ.Static({
                "html": text
            }));
        }
    }
});

/**
 * Textfield display mode: property is displayed with a textfield.
 * @static
 * @final
 * @type String
 * @member CQ.personalization.EditableClickstreamcloud
 */
CQ.personalization.EditableClickstreamcloud.MODE_TEXTFIELD = "textfield";

/**
 * Link display mode: property is displayed with a link.
 * @static
 * @final
 * @type String
 * @member CQ.personalization.EditableClickstreamcloud
 */
CQ.personalization.EditableClickstreamcloud.MODE_LINK = "link";

/**
 * Static display mode: property is displayed with a static text.
 * @static
 * @final
 * @type String
 * @member CQ.personalization.EditableClickstreamcloud
 */
CQ.personalization.EditableClickstreamcloud.MODE_STATIC = "static";