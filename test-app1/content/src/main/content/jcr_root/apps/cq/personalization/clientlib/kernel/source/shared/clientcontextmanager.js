/*
 * Copyright 1997-2009 Day Management AG
 * Barfuesserplatz 6, 4001 Basel, Switzerland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Day Management AG, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered int\o
 * with Day.
 */
/**
 * The <code>CQ_Analytics.ClientContextMgr</code> object is a singleton providing methods for registration,
 * persistence and management of different session stores linked to the clientcontext.<br>
 * Each store is basically a set of pairs (key,value) and will be used by segmentation (see {@link CQ_Analytics.SegmentMgr})
 * and displayed by clientcontext UI (see {@link CQ_Analytics.ClientContextUI}).
 * @singleton
 * @class CQ_Analytics.ClientContextMgr
 * @extends CQ_Analytics.PersistedSessionStore
 */
if (!CQ_Analytics.ClientContextMgr) {
    CQ_Analytics.ClientContextMgr = function() {
        this.clientcontext = null;
        this.clientcontextToServer = null;
        this.stores = {};
        this.data = null;
        this.config = null;
        this.isConfigLoaded = false;
        this.areStoresLoaded = false;
    };

    CQ_Analytics.ClientContextMgr.prototype = new CQ_Analytics.PersistedSessionStore();

    /**
     * Store internal key (used by persistence).
     * @final
     * @private
     */
    CQ_Analytics.ClientContextMgr.prototype.STOREKEY = "CLIENTCONTEXT";

    /**
     * Store internal name
     * @final
     * @private
     */
    CQ_Analytics.ClientContextMgr.prototype.STORENAME = "clientcontext";

    /**
     * Number of milliseconds between the last store gets registered and the event storesinitialize
     * gets fired.
     * @final
     * @private
     */
    CQ_Analytics.ClientContextMgr.prototype.INITIALIZATION_EVENT_TIMER = 1000;

    /**
     * Location of the config.
     * @static
     * @type String
     */
     CQ_Analytics.ClientContextMgr.prototype.CONFIG_PATH = CQ_Analytics.Utils.externalize("/etc/clientcontext/legacy/config.json",true);

    //inheritDoc
    CQ_Analytics.ClientContextMgr.prototype.init = function() {
        if( !this.initialized) {
            this.clientcontext = {};
            this.clientcontextToServer = {};
        }

        var store = new CQ_Analytics.SessionPersistence({'container': 'ClientContext'});
        var value = store.get(this.getStoreKey());
        if (value) {
            this.data = this.parse(value);
        } else {
            this.data = {};
        }

        this.initialized = true;
        this.fireEvent("initialize",this);

    };

    /**
     * Returns the unique session ID.
     * @return {String} the session ID.
     */
    CQ_Analytics.ClientContextMgr.prototype.getSessionId = function() {
        if (!this.data["sessionId"]) {
            this.setSessionId(CQ_Analytics.Utils.getUID());
        }
        return this.data["sessionId"];
    };

    /**
     * Sets the session ID.
     * @param {String} id the session ID.
     * @private
     */
    CQ_Analytics.ClientContextMgr.prototype.setSessionId = function(id) {
        if (id) {
            this.setProperty("sessionId", id);
        }
    };

    /**
     * Returns the visitor ID if defined.
     * @return {String} the visitor ID, <code>undefined</code> if not defined.
     */
    CQ_Analytics.ClientContextMgr.prototype.getVisitorId = function() {
        return this.data["visitorId"];
    };

    /**
     * Sets the visitor ID.
     * @param {String} id the visitor ID.
     */
    CQ_Analytics.ClientContextMgr.prototype.setVisitorId = function(id) {
        this.setProperty("visitorId", id);
    };

    /**
     * Returns the current clientcontext ID. Can be either: <ul>
     * <li>visitor ID if defined</li>
     * <li>session unique ID in other case.</li>
     * </ul>
     * If visitor ID is not defined, clientcontext is considered as anonymous.
     * @return {String} the ID.
     */
    CQ_Analytics.ClientContextMgr.prototype.getId = function() {
        var id = this.getVisitorId();
        if (!id) {
            return this.getSessionId();
        }
        return id;
    };

    /**
     * Returns if manager is still defined in anonymous mode (no visitor id defined).
     * @return {Boolean} true if anonymous.
     */
    CQ_Analytics.ClientContextMgr.prototype.isAnonymous = function() {
        var id = this.getVisitorId();
        return (!id);
    };

    /**
     * Returns if mode is defined.
     * @param {Number} mode Mode to check.
     * @return {Boolean} true if mode is defined.
     */
    CQ_Analytics.ClientContextMgr.prototype.isMode = function(mode) {
        return CQ_Analytics.ClientContextMgr.ServerStorage.isMode(mode);
    };

    /**
     * Returns the current clientcontext object. Object can contain the non server persited data.
     * @param {Boolean} toServer true to exclue non server persisted data.
     * @return {Object} object representing the clientcontext.
     */
    CQ_Analytics.ClientContextMgr.prototype.get = function(toServer) {
        if (this.clientcontext == null) {
            this.init();
        }
        if (toServer) {
            return this.clientcontextToServer;
        }
        return this.clientcontext;
    };

    /**
     * Registers a session store. The current ClickstreamcloudManager will handle its own persistence store
     * depending on updates done into the registred session store.
     * @param {CQ_Analytics.SessionStore} sessionstore The session store
     */
    CQ_Analytics.ClientContextMgr.prototype.register = function(sessionstore) {
        if (this.clientcontext == null) {
            this.init();
        }
        var ccm = this;

        this.clientcontext[sessionstore.getName()] = sessionstore.getData();
        this.stores[sessionstore.getName()] = sessionstore;
        CQ_Analytics.StoreRegistry.register(sessionstore);

        var config = this.getStoreConfig(sessionstore.getName());
        if (config["stats"] !== false && config["stats"] != "false") {
            this.clientcontextToServer[sessionstore.getName()] = sessionstore.getData(config["excludedFromStats"]);
        }

        if( this.initTimer ) {
            window.clearTimeout(this.initTimer);
            this.initTimer = null;
        }

        this.initTimer = window.setTimeout(function() {
            ccm.fireEvent("storesinitialize");
            ccm.areStoresInitialized = true;
        }, this.INITIALIZATION_EVENT_TIMER);

        //auto update current obj if sessionstore is updated
        sessionstore.addListener("update", function() {
            ccm.update(sessionstore);
        });

        CQ_Analytics.ClientContextMgr.ServerStorage.handleStoreRegistration(sessionstore);

        //clear sessionstore if clientcontext is cleared
        this.addListener("clear", function() {
            sessionstore.clear();
        });

        this.fireEvent("storeregister", sessionstore);
        this.fireEvent("storeupdate", sessionstore);
    };

    /**
     * Updates a session store. The current registred session store with the same name is updated by the given one.
     * @param {CQ_Analytics.SessionStore} sessionstore The session store
     */
    CQ_Analytics.ClientContextMgr.prototype.update = function(sessionstore) {
        if (this.clientcontext == null) {
            this.init();
        }
        this.clientcontext[sessionstore.getName()] = sessionstore.getData();

        var config = this.getStoreConfig(sessionstore.getName());
        if (config["stats"] !== false && config["stats"] != "false") {
            this.clientcontextToServer[sessionstore.getName()] = sessionstore.getData(config["excludedFromStats"]);
        }
        this.fireEvent("storeupdate", sessionstore);
    };

    /**
     * Starts the posting.
     */
    CQ_Analytics.ClientContextMgr.prototype.startPosting = function() {
        return CQ_Analytics.ClientContextMgr.ServerStorage.startPosting();
    };

    /**
     * Stops the posting.
     */
    CQ_Analytics.ClientContextMgr.prototype.stopPosting = function() {
        return CQ_Analytics.ClientContextMgr.ServerStorage.stopPosting();
    };

    /**
     * Posts the current clientcontext object to the server (occurs only if posting is started).
     */
    CQ_Analytics.ClientContextMgr.prototype.post = function() {
        return CQ_Analytics.ClientContextMgr.ServerStorage.post();
    };

    /**
     * Returns the current clientcontext object in "JCR style"
     * o.property = value --> ./property = value
     * o.level1.property = value --> ./level1/property = value
     * 2 levels only
     * @return {Object} object representing the clientcontext in "JCR style"
     * @private
     */
    CQ_Analytics.ClientContextMgr.prototype.getCCMToJCR = function() {
        return CQ_Analytics.ClientContextMgr.ServerStorage.getCCMToJCR();
    };

    //inheritDoc
    CQ_Analytics.ClientContextMgr.prototype.getName = function() {
        return this.STORENAME;
    };

    //inheritDoc
    CQ_Analytics.ClientContextMgr.prototype.clear = function() {
        this.clientcontext = null;
        this.clientcontextToServer = null;
        this.fireEvent("clear");
    };

    /**
     * Returns the registered store looked up by name.
     * @param {String} name Name of the store to retrieve
     * @return {CQ_Analytics.SessionStore} The registered store or null.
     * @since 5.5
     */
    CQ_Analytics.ClientContextMgr.prototype.getRegisteredStore = function(name) {
        return this.stores && this.stores[name] ? this.stores[name] : null;
    };

    /**
     * Loads the config and fires <code>configloaded</code> and <code>storesloaded</code> events.
     */
    CQ_Analytics.ClientContextMgr.prototype.loadConfig = function(c, autoConfig) {
        var setConfig = function(ccm, config) {
            ccm.config = config;

            ccm.isConfigLoaded = true;
            ccm.fireEvent("configloaded");
            ccm.fireEvent("storesloaded");
            ccm.areStoresLoaded = true;
        };

        if( c ) {
            setConfig(this, c);
        } else {
            if( !autoConfig ) {
                //asynchronous load
                var params = {};
                params["path"] = CQ_Analytics.Utils.getPagePath();
                params["cq_ck"] = new Date().valueOf();
                var url = this.CONFIG_PATH;
                url += "?" + CQ_Analytics.Utils.urlEncode(params);

                CQ_Analytics.Utils.load(url, function(data, status, response) {
                    var config = {};
                    try {
                        config = eval("config = " + response.responseText);
                    } catch(error) {}
                    setConfig(this, config);
                }, this);
            } else {
                setConfig(this, {});
            }
        }
    };

    /**
     * Returns the config object.
     * @return {Object} config object if loaded, null otherwise.
     */
    CQ_Analytics.ClientContextMgr.prototype.getConfig = function() {
        return this.config;
    };

    /**
     * Returns the store config object for the give store name.
     * Shortcut to <code>config["configs"][storename]["store"]</code>.
     * @param {String} storename Request config store name.
     * @return {Object} config object if loaded, {} otherwise.
     */
    CQ_Analytics.ClientContextMgr.prototype.getStoreConfig = function(storename) {
        if (this.config &&
            this.config["configs"] &&
            this.config["configs"][storename] &&
            this.config["configs"][storename]["store"]) {
            return this.config["configs"][storename]["store"];
        }
        return {};
    };

    /**
     * Returns the edit config object for the give store name.
     * Shortcut to <code>config["configs"][storename]["edit"]</code>.
     * @param {String} storename Request config store name.
     * @return {Object} config object if loaded, {} otherwise.
     */
    CQ_Analytics.ClientContextMgr.prototype.getEditConfig = function(storename) {
        if (this.config &&
            this.config["configs"] &&
            this.config["configs"][storename] &&
            this.config["configs"][storename]["edit"]) {
            return this.config["configs"][storename]["edit"];
        }
        return {};
    };

    /**
     * Returns the UI config object for the give store name.
     * Shortcut to <code>config["configs"][storename]["ui"]</code>.
     * @param {String} storename Request config store name.
     * @return {Object} config object if loaded, {} otherwise.
     */
    CQ_Analytics.ClientContextMgr.prototype.getUIConfig = function(storename) {
        if (this.config &&
            this.config["configs"] &&
            this.config["configs"][storename] &&
            this.config["configs"][storename]["ui"]) {
            return this.config["configs"][storename]["ui"];
        }
        return {};
    };

    /**
     * Returns the initial data for the give store name.
     * Shortcut to <code>config["data"][storename]</code>.
     * @param {String} storename Request config store name.
     * @return {Object} data object if loaded, {} otherwise.
     */
    CQ_Analytics.ClientContextMgr.prototype.getInitialData = function(storename) {
        if (this.config &&
            this.config["data"] &&
            this.config["data"][storename]) {
            return this.config["data"][storename];
        }
        return {};
    };

    /**
     * Returns the registered stores.
     * @return {Object} All registered stores
     * @since 5.5
     */
    CQ_Analytics.ClientContextMgr.prototype.getStores = function() {
        return this.stores;
    };

    /**
     * Executes the callback when the current ClientContextMgr is ready, i.e. when all stores are loaded.
     * @param {Function} callback Function to execute on ready
     * @param {Object} scope (optional) The execution scope; window object if null
     * @since 5.5
     */
    CQ_Analytics.ClientContextMgr.prototype.onReady = function(callback, scope) {
        if( callback ) {
            if( this.areStoresLoaded) {
                callback.call(scope);
            } else {
                this.addListener("storesloaded", callback, scope);
            }
        }
    };

    CQ_Analytics.ClientContextMgr = CQ_Analytics.CCM = new CQ_Analytics.ClientContextMgr();

    //backward compatibility
    CQ_Analytics.ClickstreamcloudMgr = CQ_Analytics.CCM;
    //just kept for compatibility with internal name during 5.5 dev
    CQ_Analytics.ContextCloudMgr = CQ_Analytics.CCM;

    //Path to the clientcontext on the server. To be defined in app.
    CQ_Analytics.ClientContextMgr.PATH = null;

    /**
     * Prepends the client context path to the provided url.
     * @param {String} url URL to prepend
     * @return {String} the computed url
     * @since 5.5
     */
    CQ_Analytics.ClientContextMgr.getClientContextURL = function(url) {
        return CQ_Analytics.ClientContextMgr.PATH + url;
    };

    //inits the CCM store in a different thread.
    window.setTimeout(function() {
        CQ_Analytics.CCM.init();
    }, 1);

    CQ_Analytics.Utils.addListener(window, "unload", function() {
        try {
            for(var p in CQ_Analytics.ClientContextMgr) {
                delete CQ_Analytics.ClientContextMgr[p];
            }
            CQ_Analytics.ClientContextMgr = null;
        } catch(error) {}
        CQ_Analytics.CCM = null;
    });
}