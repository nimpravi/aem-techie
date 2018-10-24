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
 * @class CQ.personalization.OperatorSelection
 * @extends CQ.form.Selection
 * OperatorSelection is a specialized selection allowing to choose one of the
 * <code>{@link CQ_Analytics.Operator CQ_Analytics.Operators}</code>.
 * @constructor
 * Creates a new OperatorSelection.
 * @param {Object} config The config object
 */
CQ.personalization.OperatorSelection = CQ.Ext.extend(CQ.form.Selection, {
    constructor: function(config) {
        config = (!config ? {} : config);

        var defaults = {};

        if (CQ_Analytics.Operator && config.operators) {
            //transform operators config to options.
            config.options = config.options ? config.options : new Array();
            config.operators = config.operators instanceof Array ? config.operators : [config.operators];
            for (var i = 0; i < config.operators.length; i++) {
                if (config.operators[i].indexOf("CQ_Analytics.Operator." == 0)) {
                    try {
                        config.operators[i] = eval("config.operators[i] = " + config.operators[i] + ";");
                    } catch(e) {
                    }
                }
                var value = config.operators[i];
                if ( value ) {
                    var text = CQ_Analytics.OperatorActions.getText(config.operators[i]);
                    text = text ? text : value;
                    config.options.push({
                        "text": CQ.I18n.getVarMessage(text),
                        "value": value
                    });
                }
            }
        }

        CQ.Util.applyDefaults(config, defaults);

        // init component by calling super constructor
        CQ.personalization.OperatorSelection.superclass.constructor.call(this, config);
    }
});

CQ.Ext.reg("operatorselection", CQ.personalization.OperatorSelection);