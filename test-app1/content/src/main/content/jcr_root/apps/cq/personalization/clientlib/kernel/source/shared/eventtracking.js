/**
 * Stores the 'data' object into the appropriate properties of the 'store'
 * client side store.
 * 
 * @param {Object}
 *            SessionStore object.
 * @param {Object}
 *            Data object.
 * @private
 */
CQ_Analytics.storeData = function(store, data) {
	
	var findMappingFor = function(prop,value) {
		for (var i=0; i< CQ_Analytics.Sitecatalyst.frameworkMappings.length; i++) {
			var m = CQ_Analytics.Sitecatalyst.frameworkMappings[i];
			if (m[prop] === value) {
				return m;
			}
		}
		return null;
	}
	
	var stripValue = function(value) {
		if (typeof value === 'string') {
			return value.replace(/[,;=\|]/g,"");
		}
		return value;
	}
	
	for (var j in data) {
    	//handle generic data
    	if (j !== "product") {
        	var idx = j.indexOf(".");
        	var storeName = (idx > 0) ? j.substr(0,idx-1) : undefined;
        	var key = (idx > 0) ? j.substr(idx) : j;
        	if ( storeName && CQ_Analytics.StoreRegistry.getStore(storeName) ) {
        		store = CQ_Analytics.StoreRegistry.getStore(storeName);
        	}
            store.setProperty(key, data[j]);
    	// handle product data
    	} else {
    		var productProperties = ["category","sku","quantity","price","events","evars"];
    		var products = store.getProperty("products").split(",");
    		products = (products[0] == "") ? new Array() : products; 

    		var data = (data[j] instanceof Array)
    				   ? data[j] 
    				   : [data[j]];
    			
    		for (var prod = 0; prod < data.length; prod++) {
    			var p = data[prod];
    			var product = new Array(6);
    			for (var k in p) {
	    			var idx = productProperties.indexOf(k);
	    			if (idx > -1) {
	    				if (idx < 4) {
	    					product[idx] = stripValue(p[k]);
	    				} else {
	    					var multival = []; 
	    					for(var l in p[k]) {
	    						var propPath = store.getName() + "." + j + "." + k + "." + l;
	    						var cm = findMappingFor("cqVar", propPath);
	    						if (cm) {
	    							multival.push(cm.scVar + "=" + stripValue(p[k][l]));
	    							//add to events store like normal event
	    							var events = store.getProperty("events").split("\u2026")
	    							if (events.indexOf(cm.cqVar) < 0) {
	    								events.push(cm.cqVar.replace(/.+\./,""));
	    								store.setProperty("events", events.join("\u2026"));
	    							}
	    						}
	    					}
	    					product[idx] = multival.join("|"); 
	    				}
	            	}
	    		}
    			products.push(product.join(";"));
    		}
    		store.setProperty("products", products.join(","));
    	}
    }
};

/**
 * Records a user interaction in the a ClientContext store (by default
 * EventDataManager) to be picked up by the used analytics solution for further
 * processing.
 * 
 * @param {Object}
 *            options Tracking options.
 * 
 * <p>
 * Generic options properties.
 * </p>
 * @param {String}/{Array}
 *            options.event Tracking event name or Array of Strings for multiple 
 *            event names.
 * @param {Object}
 *            options.values Tracking values.
 * @param {Boolean}
 *            options.collect Flag which indicates if event and values should be
 *            collected (optional).
 * @param {String}
 *            options.dataMgr User ClientContext store to hold the information
 *            (optional). Default is <code>CQ_Analytics.EventDataMgr</code>.
 * @param {Object}
 *            options.options Options object holding analytics specific options
 *            (optional).
 * 
 * <p>
 * By default when using SiteCatalyst following <code>options.options</code>
 * are supported.
 * </p>
 * @param {DOM
 *            Element} options.options.obj Link DOM element
 * @param {String}
 *            options.options.defaultLinkType SiteCatalyst link type
 * 
 * @return {Array} An array holding the options.event and options.values if
 *         <code>options.collect</code> is <code>true</code>, otherwise
 *         nothing is returned.
 * @since 5.5
 */
CQ_Analytics.record = function(options) {

    if (options.collect) {
        return [options.event, options.values]; 
    } else {  
        if (options.event) { 
            options.options = options.options || { };
            //execute callbacks before data is set
            try {
                CQ_Analytics.recordBeforeCallbacks.sort(function(a, b){
                    return a.rank-b.rank;
                });
                for(var callback in CQ_Analytics.recordBeforeCallbacks) {
                    if (CQ_Analytics.recordBeforeCallbacks[callback].func.call(this, options)) {
                        return;
                    }
                }
            } catch(err) {
                //nothing to do 
            }
         
            //record data to clickStreamCloud
            var dataMgr = options.dataMgr || CQ_Analytics.EventDataMgr
            dataMgr.reset();

            if (typeof options.event == "string") {
                dataMgr.setProperty("events",options.event);
            } else {
                dataMgr.setProperty("events",options.event.join("\u2026"));
            }
            
            if (options.values) {
                CQ_Analytics.storeData(dataMgr, options.values);
            }
            
            //execute callbacks after data was set
            try {
               CQ_Analytics.recordAfterCallbacks.sort(function(a, b){
                   return a.rank-b.rank;
               });
               for(var callback in CQ_Analytics.recordAfterCallbacks) {
                    if (CQ_Analytics.recordAfterCallbacks[callback].func.call(this, options)) {
                        return;
                    }
                }
            } catch(err) {
                //nothing to do 
            }
        }
    }
};

/**
 * @private
 */
CQ_Analytics.recordBeforeCallbacks = [];

/**
 * @private
 */
CQ_Analytics.recordAfterCallbacks = []; 

/**
 * Registers a callback handler which is called before data in ClientContext
 * store was set.
 * 
 * @param {Function}
 *            Callback function. Parameter passed to the callback is the
 *            <code>options</code> object from the record method.
 * @param {Integer}
 *            Execution rank.
 */
CQ_Analytics.registerBeforeCallback = function(callback, rank) {
    CQ_Analytics.recordBeforeCallbacks.push({rank: rank, func: callback});
};

/**
 * Registers a callback handler which is called after data in ClientContext
 * store was set.
 * 
 * @param {Function}
 *            Callback function. Parameter passed to the callback is the
 *            <code>options</code> object from the record method.
 * @param {Integer}
 *            Execution rank.
 */
CQ_Analytics.registerAfterCallback = function(callback, rank) {
    CQ_Analytics.recordAfterCallbacks.push({rank: rank, func: callback});
};
