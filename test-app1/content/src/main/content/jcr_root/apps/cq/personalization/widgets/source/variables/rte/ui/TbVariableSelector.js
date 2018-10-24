/*
 * Copyright 1997-2010 Day Management AG
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
 * @class CQ.form.rte.ui.TbVariableSelector
 * @extends CQ.form.rte.ui.TbElement
 * @private
 * This class represents a variable selecting element for use in
 * {@link CQ.form.rte.ui.ToolbarBuilder}.
 */
CQ.form.rte.ui.TbVariableSelector = CQ.Ext.extend(CQ.form.rte.ui.TbElement, {

    variableSelector: null,

    variables: null,

    toolbar: null,

    constructor: function(id, plugin, tooltip, variables) {
        CQ.form.rte.ui.TbVariableSelector.superclass.constructor.call(this, id, plugin, false,
                tooltip);
        this.variables = variables;
    },

    /**
     * Creates HTML code for rendering the options of the variable selector.
     * @return {String} HTML code containing the options of the variable selector
     * @private
     */
    createStyleOptions: function() {
        var htmlCode = "";
        if (this.variables) {
            for (var v in this.variables) {
                var variableToAdd = this.variables[v];
                htmlCode += "<option value=\"" + variableToAdd.value + "\">" + CQ.I18n.getVarMessage(variableToAdd.text) + "</option>";
            }
        }
        return htmlCode;
    },

    getToolbar: function() {
        return CQ.form.rte.ui.ToolbarBuilder.STYLE_TOOLBAR;
    },

    addToToolbar: function(toolbar) {
        this.toolbar = toolbar;
        if (CQ.Ext.isIE) {
            // the regular way doesn't work for IE anymore with Ext 3.1.1, hence working
            // around
            var helperDom = document.createElement("span");
            helperDom.innerHTML = "<select class=\"x-font-select\">"
                    + this.createStyleOptions() + "</span>";
            this.variableSelector = CQ.Ext.get(helperDom.childNodes[0]);
        } else {
            this.variableSelector = CQ.Ext.get(CQ.Ext.DomHelper.createDom({
                tag: "select",
                cls: "x-font-select",
                html: this.createStyleOptions()
            }));
        }
        this.variableSelector.on('focus', function() {
            this.plugin.editorKernel.isTemporaryBlur = true;
        }, this);
        // fix for a Firefox problem that adjusts the combobox' height to the height
        // of the largest entry
        this.variableSelector.setHeight(19);
        var addButton = {
            "itemId": this.id + "_insert",
            "iconCls": "x-edit-insertvariable",
            "text": CQ.I18n.getMessage("Insert"),
            "enableToggle": (this.toggle !== false),
            "scope": this,
            "handler": function() {
                this.plugin.execute(this.id + "_insert");
            },
            "clickEvent": "mousedown",
            "tabIndex": -1
        };
        toolbar.add(
            CQ.I18n.getMessage("Variable"),
            " ",
            this.variableSelector.dom,
            addButton
        );
    },

    createToolbarDef: function() {
        // todo support usage in global toolbar
        return null;
    },

    getSelectedVariable: function() {
        var variable = this.variableSelector.dom.value;
        if (variable.length > 0) {
            return variable;
        }
        return null;
    },

    getExtUI: function() {
        return this.variableSelector;
    },

    getInsertButtonUI: function() {
        return this.toolbar.items.map[this.id + "_insert"];
    }

});