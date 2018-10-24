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

CQ.personalization.variables.Variables = {};

CQ.personalization.variables.Variables.SCANNED_TAGS = ["*"];

CQ.personalization.variables.Variables.applyToEditComponent = function(path) {
    CQ.Ext.onReady(function() {
        //TODO configurable prefix and suffix


        CQ.WCM.onEditableBeforeRender(path, function(config) {
            var element = config.element;
            if( element ) {
                CQ.personalization.variables.Variables.injectSpans(element, CQ.personalization.variables.Variables.SCANNED_TAGS, "cq-variable-code");
                if (CQ_Analytics && CQ_Analytics.ProfileDataMgr) {
                    CQ.personalization.variables.Variables.updateVariables(element, CQ_Analytics.ProfileDataMgr.getData());
                    CQ_Analytics.ProfileDataMgr.addListener("update", function() {
                        CQ.personalization.variables.Variables.updateVariables(element, CQ_Analytics.ProfileDataMgr.getData());
                    });
                }
            }
        });

        CQ.WCM.onEditableReady(path, function() {
            this.on(CQ.wcm.EditBase.EVENT_AFTER_EDIT,function() {
                CQ.personalization.variables.Variables.injectSpans(this.element, CQ.personalization.variables.Variables.SCANNED_TAGS, "cq-variable-code");
                CQ.personalization.variables.Variables.updateVariables(this.element, CQ_Analytics.ProfileDataMgr.getData());
            });
        });
    });
};

CQ.personalization.variables.Variables.injectSpans = function(element, tags, className) {
    element = CQ.Ext.get(element);
    if( element ) {
        className = className || "";
        for (var t = 0; t < tags.length; t++) {
            var reg = new RegExp("\\\$\\{[\\w]*\\}", "ig");
            var pars = CQ.Ext.DomQuery.jsSelect(tags[t] + ":contains(\${)", element.dom);
            for( var i=0;i<pars.length;i++) {
                var p = pars[i];
                //check if matches ...\${}...
                var text = p.innerHTML;
                if (text) {
                    var variables = text.match(reg);
                    var performedVariables = [];
                    for(var j = 0; j < variables.length; j++) {
                        var v = variables[j];
                        if( performedVariables.indexOf(v) == -1) {
                            //vName is variable name (no "\${" and "}")
                            var vName = v.replace(new RegExp("\\\$\\{([\\w]*)\\}", "ig"),"$1");
                            var repl = "<span class=\"cq-variable " + className + " cq-variable-vars-"+vName+"\" title=\""+v+"\">"+v+"</span>";
                            text = text.replace(new RegExp("\\\$\\{"+vName+"\\}", "ig"),repl);
                            performedVariables.push(v);
                        }
                    }
                    p.innerHTML = text;
                }
            }
        }
    }
};

CQ.personalization.variables.Variables.updateVariables = function(element, data) {
    element = CQ.Ext.get(element);
    if( element ) {
        var pars = CQ.Ext.DomQuery.jsSelect("span.cq-variable", element.dom);
        data = data || {};

        for( var i=0;i<pars.length;i++) {
            var p = pars[i];
            var className = p ? p.className : "";
            var reg = new RegExp(".+cq-variable-vars-(\\w+)\\s*(\\w*)", "ig");
            var variable = className.replace(reg, "$1");
            if(variable) {
                var text = p.innerHTML;
                if( text && text != "" && text != " ") {
                    var value = data[variable];
                    value = value && value != "" ? value : "\${"+variable+"}";
                    p.innerHTML = value;
                }
            } else {
                p.innerHTML = "";
            }
        }
    }
};
