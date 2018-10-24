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
 * @class CQ_Analytics.PersistedJSONPStore
 * @extends CQ_Analytics.PersistedJSONStore
 * A PersistedJSONPStore is a persisted container of a JSON object retrieved from a remote JSONP service.
 * @constructor
 * Creates a new PersistedJSONPStore.
 * @since 5.5
 */
CQ_Analytics.PersistedJSONPStore = function() {};

CQ_Analytics.PersistedJSONPStore.prototype = new CQ_Analytics.PersistedJSONStore();

/**
 * Sets a new service URL.
 * @param {String} serviceURL The service URL
 */
CQ_Analytics.PersistedJSONPStore.prototype.setServiceURL = function(serviceURL) {
    this.serviceURL = serviceURL;
};

/**
 * Returns the service URL of the store.
 * @return {String} The service URL if defined. Null otherwise
 */
CQ_Analytics.PersistedJSONPStore.prototype.getServiceURL = function() {
    return this.serviceURL;
};

/**
 * Loads in the store the data from the remote JSONP service.
 * @param {String} serviceURL (Optional) Defines a new service URL
 * @param {Object} dynamicData (Optional) Data that will be appended to the store
 * @param {Function} callback (Optional) Function to execute after data loading
 */
CQ_Analytics.PersistedJSONPStore.prototype.load = function(serviceURL, dynamicData, callback) {
    var storeName = this.getName();
    if( ! CQ_Analytics.PersistedJSONPStore.JSONPCallbacks[this.getName()]) {
        CQ_Analytics.PersistedJSONPStore.JSONPCallbacks[storeName] = function(data) {
            var s = CQ_Analytics.CCM.getRegisteredStore(storeName);
            if( s ) {
                s.initJSON(data);
                if( dynamicData ) {
                    s.initJSON(dynamicData, true);
                }

            }
            if( callback ) {
                callback.call(s);
            }
        };
    }

    if( serviceURL ) {
        this.setServiceURL(serviceURL);
    }

    var url = this.serviceURL;
    url = url.replace("\$\{callback\}","CQ_Analytics.PersistedJSONPStore.JSONPCallbacks." + storeName);
    $CQ.getScript(url);
};

/**
 * Used as storage for JSONP callbacks (one callback per unique store name).
 */
CQ_Analytics.PersistedJSONPStore.JSONPCallbacks = {};

/**
 * Returns a new instance of a CQ_Analytics.PersistedJSONPStore instance.
 * @param {String} storeName The name of the new store
 * @param {String} serviceURL (Optional) The service URL of the JSONP store
 * @param {Object} dynamicData (Optional) Data that will be appended to the store
 * @param {Boolean} deferLoading (Optional) True to defer the store loading
 * @param {Function} loadingCallback (Optional) Function to execute after data loading
 * @return {CQ_Analytics.PersistedJSONPStore} The new store instance
 * @static
 */
CQ_Analytics.PersistedJSONPStore.getInstance = function(storeName, serviceURL, dynamicData, deferLoading, loadingCallback) {
    if( storeName && serviceURL) {
        try {
            var jsonpStore = new CQ_Analytics.PersistedJSONPStore();
            jsonpStore.STOREKEY = storeName.toUpperCase();
            jsonpStore.STORENAME = storeName;

            if( serviceURL ) {
                jsonpStore.setServiceURL(serviceURL);
            }

            if( !deferLoading ) {
                jsonpStore.load(serviceURL, dynamicData, loadingCallback);
            }

            return jsonpStore;
        } catch(error) {
            console.log("Cannot create the JSONP store",storeName, serviceURL,error);
        }
    }
    return null;
};

/**
 * Creates, registers in the ClientContext and returns a new instance of a CQ_Analytics.PersistedJSONPStore instance.
 * @param {String} storeName The name of the new store
 * @param {String} serviceURL The service URL of the JSONP store
 * @param {Object} dynamicData (Optional) Data that will be appended to the store
 * @param {Function} callback (Optional) Function to execute after data loading
 * @return {CQ_Analytics.PersistedJSONPStore} The new store instance
 * @static
 */
CQ_Analytics.PersistedJSONPStore.registerNewInstance = function(storeName, serviceURL, dynamicData, callback) {
    if( !serviceURL ) {
        return null;
    }

    if( !storeName ) {
        //try to extract a name from service url

        var sa = CQ.shared.HTTP.getSchemeAndAuthority(serviceURL);
        if( sa ) {
            if(sa.indexOf(".") !=-1) {
                sa = sa.substring(0, sa.lastIndexOf("."));
                storeName = sa.substring(sa.lastIndexOf(".") + 1);
            } else {
                storeName = sa.substring(sa.lastIndexOf("/") + 1);
            }
        } else {
            //weird case, should never happen
            storeName = serviceURL;
        }

    }

    var store = CQ_Analytics.PersistedJSONPStore.getInstance(storeName, serviceURL, dynamicData, false, function() {
        this.init();
        this.reset();
        if( callback ) {
            callback.call(this);
        }
    });
    if( store != null ) {
        //registers new store to clickstreamcloud manager
        CQ_Analytics.CCM.register(store);
        return store;
    }
    return null;
};