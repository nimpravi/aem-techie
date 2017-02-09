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
 * @class CQ.form.rte.plugins.PersonalizationPlugin
 * @extends CQ.form.rte.plugins.Plugin
 * <p>This class implements styling text fragments with a CSS class (using "span" tags) as a
 * plugin.</p>
 * <p>The plugin ID is "<b>variables</b>".</p>
 * <p><b>Features</b></p>
 * <ul>
 *   <li><b>variables</b> - adds a style selector (variables will be applied on selection scope)
 *     </li>
 * </ul>
 * <p><b>Additional config requirements</b></p>
 * <p>The following plugin-specific settings must be configured through the corresponding
 * {@link CQ.form.rte.EditorKernel} instance:</p>
 * <ul>
 *   <li>The variablesheets to be used must be provided through
 *     {@link CQ.form.RichText#externalStyleSheets}.</li>
 * </ul>
 */
CQ.form.rte.plugins.PersonalizationPlugin= CQ.Ext.extend(CQ.form.rte.plugins.Plugin, {

    /**
     * @cfg {Object/Object[]} variables
     * <p>Defines CSS classes that are available to the user for formatting text fragments
     * (defaults to { }). There are two ways of specifying the CSS classes:</p>
     * <ol>
     *   <li>Providing variables as an Object: Use the CSS class name as property name.
     *   Specify the text that should appear in the style selector as property value
     *   (String).</li>
     *   <li>Providing variables as an Object[]: Each element has to provide "cssName" (the
     *   CSS class name) and "text" (the text that appears in the style selector)
     *   properties.</li>
     * </ol>
     * <p>Styling is applied by adding "span" elements with corresponding "class"
     * attributes appropriately.</p>
     * @since 5.3
     */

    /**
     * @private
     */
    cachedVariables: null,

    /**
     * @private
     */
    variablesUI: null,

    constructor: function(editorKernel) {
        CQ.form.rte.plugins.PersonalizationPlugin.superclass.constructor.call(this, editorKernel);
    },

    getFeatures: function() {
        return [ "variables" ];
    },

    getVariables: function() {
          var opts = new Array();
          try {
              var et = document.getElementsByName("cfgpath");
              
              var cfgPath = "";
                if(et.length > 0){
                  cfgPath = et[0].value;
                }

              var url =  "/_jcr_content.emailservice.json?operation=getEmailTools&cfgpath=" + cfgPath;
              
              var data = CQ.HTTP.eval(url);
                           
                for (var item in data ) {
                	if(!data.hasOwnProperty(item)) {
                		continue;
                	}
                	CQ.Log.error("message --- " + item);
                    var t1= data[item].text;
                    var v1= data[item].value;
                    if(t1 && v1){
                        opts.push({value: v1, text: t1});
                        }
                    }
                    
                    opts.sort(function(l1, l2) {
                    if (l1.text < l2.text) {
                        return -1;
                    } else if (l1.text == l2.text) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                
                return opts;
                } catch (e) {
                CQ.Log.error("CQ.form.rte.plugins.PersonalizationPlugin#getVariablesfailed: " + e.message);
            }
            return [];
    },

    initializeUI: function(tbGenerator) {
        var plg = CQ.form.rte.plugins;
        var ui = CQ.form.rte.ui;
        if (this.isFeatureEnabled("personalizationplugin")) {
            this.variablesUI = new ui.TbVariableSelector("personalizationplugin", this, null, this.getVariables());
            tbGenerator.addElement("personalizationplugin", plg.Plugin.SORT_STYLES, this.variablesUI, 10);
        }
    },

    notifyPluginConfig: function(pluginConfig) {
        pluginConfig = pluginConfig || { };
        CQ.Util.applyDefaults(pluginConfig, {
            "variables": {
                // empty default value
            }
        });
        this.config = pluginConfig;
    },

    execute: function(cmdId) {
        if (!this.variablesUI) {
            return;
        }
        var cmd = null;
        var value = null;
        switch (cmdId.toLowerCase()) {
            case "personalizationplugin_insert":
                cmd = "inserthtml";
                value = this.variablesUI.getSelectedVariable();
                
                break;
        }
        if (cmd && value) {
            var vt = value;
            //var html = "<span class=\"cq-variable cq-variable-code cq-variable-vars-"+value+"\" title=\""+vt+"\">"+vt+"</span>&nbsp;";
            this.editorKernel.relayCmd(cmd, vt);
        }
    }
});

// register plugin
CQ.form.rte.plugins.PluginRegistry.register("personalizationplugin", CQ.form.rte.plugins.PersonalizationPlugin);
