/*
 * ***********************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 * Copyright 2011 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 * ***********************************************************************
 */

/**
 * @class CQ_Analytics.PersistedJSONStore
 * @extends CQ_Analytics.PersistedSessionStore
 * A PersistedJSONStore is a persisted container of a JSON object.
 * @constructor
 * Creates a new PersistedJSONStore.
 * @since 5.5
 */
CQ_Analytics.PersistedJSONStore = function() {};

CQ_Analytics.PersistedJSONStore.prototype = new CQ_Analytics.PersistedSessionStore();

/**
 * @cfg {String} STOREKEY
 * Store internal key
 * @final
 * @private
 */
CQ_Analytics.PersistedJSONStore.prototype.STOREKEY = "";

/**
 * @cfg {String} STORENAME
 * Store internal name
 * @final
 * @private
 */
CQ_Analytics.PersistedJSONStore.prototype.STORENAME = "";

//inheritDoc
CQ_Analytics.PersistedJSONStore.prototype.init = function() {
    var store = new CQ_Analytics.SessionPersistence({'container': 'ClientContext'});
    var value = store.get(this.getStoreKey());
    if (!value || value == "") {
        this.data = {};
        for (var p in this.initProperty) {
            this.data[p] = this.initProperty[p];
        }
    } else {
        this.data = this.parse(value);
    }
    this.persist();

    this.initialized = true;
    this.fireEvent("initialize",this);
    this.fireEvent("update");
};

//inheritDoc
CQ_Analytics.PersistedJSONStore.prototype.clear = function() {
    var store = new CQ_Analytics.SessionPersistence({'container': 'ClientContext'});
    store.remove(this.getStoreKey());
    this.data = null;
    this.initProperty = {};
};

/**
 * Sets the store data with the specified JSON object. Note that inside the store, properties are stored based
 * on property path in the store.
 * <code>{
 * A: "valueA",
 * B: {
 *  B1: "valueBB1"
 * }</code>
 * will be accessed in the store as:
 * <code>A: "valueA"
 * B/B1: "valueBB1"</code>
 *
 * @param {Object} jsonData The JSON object containing the data.
 */
CQ_Analytics.PersistedJSONStore.prototype.initJSON = function(jsonData, doNotClear) {
    if( !doNotClear ) {
        this.initProperty = {};
    }

    var propertyToPaths= function(target, prefix, obj) {
        for(var p in obj) {
            if( typeof obj[p]  == "object") {
                propertyToPaths(target, prefix ? prefix + "/" + p : p, obj[p]);
            } else {
                target[prefix ? prefix + "/" + p : p] = obj[p];
            }
        }
    };

    propertyToPaths(this.initProperty, null, jsonData);
};

/**
 * Returns the store data as a JSON object.
 * @return {Object} The JSON object.
 */
CQ_Analytics.PersistedJSONStore.prototype.getJSON = function() {
    var data = this.getData();
    var res = {};

    for(var longProp in data) {
        var s = longProp.split("/");
        var level = res;
        for(var i = 0; i < s.length; i++) {
            var propLevel = s[i];
            if( i == s.length - 1) {
                level[propLevel] = data[longProp];
            } else {
                level[propLevel] = level[propLevel] || {};
                level = level[propLevel];
            }
        }
    }

    return res;
};

/**
 * Returns a new instance of a CQ_Analytics.PersistedJSONStore instance is initialized with the JSON object.
 * @param {String} storeName The name of the new store
 * @param {Object} jsonData The initial data as JSON object
 * @return {CQ_Analytics.PersistedJSONStore} The new store instance
 * @static
 */
CQ_Analytics.PersistedJSONStore.getInstance = function(storeName, jsonData) {
    var s = new CQ_Analytics.PersistedJSONStore();
    s.STOREKEY = storeName.toUpperCase();
    s.STORENAME = storeName;

    s.initJSON(jsonData);

    return s;
};

/**
 * Creates, registers in the ClientContext and returns a new instance of a CQ_Analytics.PersistedJSONStore
 * instance initialized with the JSON object.
 * @param {String} storeName The name of the new store
 * @param {Object} jsonData The initial data as JSON object
 * @return {CQ_Analytics.PersistedJSONStore} The new store instance
 * @static
 */
CQ_Analytics.PersistedJSONStore.registerNewInstance = function(storeName, jsonData) {
    var jsonStore = CQ_Analytics.PersistedJSONStore.getInstance(storeName, jsonData);
    jsonStore.init();
    //registers new store to clickstreamcloud manager
    CQ_Analytics.CCM.register(jsonStore);

    return jsonStore;
};

