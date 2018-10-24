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
 * The <code>CQ_Analytics.StrategyMgr</code> object is a singleton managing registration of different selection
 * strategies and selection of teasers
 * @class CQ_Analytics.StrategyMgr
 * @singleton
 */
if (!CQ_Analytics.StrategyMgr) {
    CQ_Analytics.StrategyMgr = function() {
        this.strategies = {};
    };

    CQ_Analytics.StrategyMgr.prototype = {};

    /**
     * Returns if a strategy is registered or not.
     * @param {String} strategy Strategy name
     * @return {Boolean} true if strategy registred. False otherwise.
     */
    CQ_Analytics.StrategyMgr.prototype.isRegistered = function(strategy) {
        return !!this.strategies[strategy];
    };

    /**
     * Registers a selection strategy. Selection function must return true or false,
     * and has one Array parameter: list of all teasers.
     * @param {String} strategy Strategy name
     * @param {Function} func Selection function
     */
    CQ_Analytics.StrategyMgr.prototype.register = function(strategy, func) {
        if (typeof func == 'function') {
            this.strategies[strategy] = func;
        }
    };

    /**
     * Chooses one teaser if the teasers list depending on the specified strategy.
     * @param {String} strategy Strategy name
     * @param {Array} teasers List of teasers
     * @return {Object} The selected teaser
     */
    CQ_Analytics.StrategyMgr.prototype.choose = function(strategy, teasers) {
        //no need to apply a strategy to choose in a list of one item!
        if (teasers.length == 1) return teasers[0];

        if (this.strategies[strategy]) {
            return this.strategies[strategy].call(this, teasers);
        }

        return null;
    };

    CQ_Analytics.StrategyMgr = new CQ_Analytics.StrategyMgr();
}