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
 * A helper class providing a set of utility methods.
 * <br>
 * @class CQ_Analytics.Utils
 */
CQ_Analytics.Utils = new function() {
    return {
        /**
         * Attaches an event handler while properly considering (aka chaining) an existing one
         * @static
         * @param {Object} event Event
         * @param {Function} func Function
         */
        registerDocumentEventHandler: function(event, func) {
            var oldHandler = window.document[event];
            if (typeof window.document[event] != 'function') {
                window.document[event] = func;
            } else {
                // chain old with new func
                window.document[event] = function(e) {
                    if (oldHandler) {
                        oldHandler(e);
                    }
                    func(e);
                };
            }
        },

        /**
         * Creates a event wraping function of a parameter function. Available parameters are:<ul>
         *        <li><b>event</b> : Object<div class="sub-desc">Event</div></li>
         *        <li><b>keyCode</b> : Number<div class="sub-desc">Key code</div></li>
         *        </ul>
         * @static
         * @param {Function} func The function to wrap
         * @return {Function} Wrapping function
         */
        eventWrapper: function(func) {
            return function(e) {
                var keyCode, event;
                if (document.all) {
                    keyCode = window.event.keyCode;
                    event = window.event;
                } else {
                    keyCode = (typeof(e.which) != 'undefined') ? e.which : 0;
                    event = e;
                }
                if (event) {
                    func(event, keyCode);
                }
            };
        },

        /**
         * Loads the HTML returned by a GET request into a DOM element.
         * @static
         * @param {String} url URL to request
         * @param {String} elemId Id of the DOM element where HTML is inserted.
         */
        loadElement: function(url, elemId) {
            $CQ("#" + elemId).load(url);
        },

        /**
         * Loads the HTML returned by a GET request into a DOM element. Teasers smooth loading.
         * @static
         * @param {String} url URL to request
         * @param {String} elemId Id of the DOM element where HTML is inserted.
         */
        loadTeaserElement: function(url, elemId) {
            var hbckup = $CQ("#" + elemId).css("height");
            var h = $CQ("#" + elemId).height();
            if (h > 0) {
                //force height to avoid flickering
                $CQ("#" + elemId).css("height", h);
            }

            var showTeaser = function(text) {
                if (text && text != "") {
                    var toInject = $CQ(text).css("display", "none");
                    $CQ("#" + elemId).append(toInject);
                    toInject.fadeIn(/*"fast", */function() {
                        if (hbckup && hbckup != "0px") {
                            $CQ("#" + elemId).css("height", hbckup);
                        }
                    });
                } else {
                    if (hbckup && hbckup != "0px") {
                        $CQ("#" + elemId).css("height", hbckup);
                    }
                }
            };

            var cacheTeaser = function(url, text) {
                if (!CQ_Analytics.Utils.teasersCache) {
                    CQ_Analytics.Utils.teasersCache = {};
                }

                CQ_Analytics.Utils.teasersCache[url] = text;
            };


            var handleTeaser = function() {
                if (CQ_Analytics.Utils.teasersCache && CQ_Analytics.Utils.teasersCache[url]) {
                    showTeaser(CQ_Analytics.Utils.teasersCache[url]);
                } else {
                    CQ_Analytics.Utils.teasersLoading = CQ_Analytics.Utils.teasersLoading || {};
                    //teaser might be alreading being loaded
                    if( CQ_Analytics.Utils.teasersLoading[url]) {
                        //"come back" in some few ms
                        window.setTimeout(function() {
                            CQ_Analytics.Utils.loadTeaserElement(url, elemId);
                        }, 100);
                    } else {
                        CQ_Analytics.Utils.teasersLoading[url] = true;
                        loadTeaser();
                    }
                }
            };

            var loadTeaser = function() {
                // the WCM mode might have been changed using a parameter, in such cases
                // we need to proxy the parameter to the teaser paragraph to avoid JS
                // errors that originate from the parameter rendered in the wrong mode
                // (for example, rendered for edit mode when the WCM mode is actually
                // disabled); also ensure that a "overridden URL" (for example, provided
                // by the portal adapter) takes precedence over the actual URL
                var requestURL = url;
                var baseUrl = location.href;
                if (typeof CQ_CONTENT_PATH != "undefined") {
                    baseUrl = CQ_CONTENT_PATH;
                }
                var wcmMode = CQ.shared.HTTP.getParameter(baseUrl, "wcmmode");
                if (wcmMode) {
                    requestURL += (requestURL.indexOf("?") > 0 ? "&" : "?") + "wcmmode=" + wcmMode;
                }

                CQ.shared.HTTP.get(requestURL, function(o, success, response) {
                    if (success) {
                        var text = response.body;
                        if (text) {
                            //filter response because following fadeOut cannot be applied to
                            //empty text nodes
                            text = text.replace(new RegExp("\\n", "ig"), "");
                            text = text.replace(new RegExp("\\r", "ig"), "");

                            cacheTeaser(url, text);

                            delete CQ_Analytics.Utils.teasersLoading[url];

                            handleTeaser();
                        }
                    } else {
                        cacheTeaser(url, "");
                    }
                });
            };

            var length = $CQ("#" + elemId).children().length;
            if (length > 0) {
                var item = 0;
                $CQ("#" + elemId).children().fadeOut(/*"fast",*/ function() {
                    var child = $CQ(this);
                    window.setTimeout(function() {
                        child.remove();
                        item ++;
                        if (item >= length) {
                            handleTeaser();
                        }
                    }, 50);
                });
            } else {
                handleTeaser();
            }


        },

        /**
         * Clears in the inner HTML content of a DOM element.
         * @static
         * @param {String} elemId Id of the DOM element to clear.
         */
        clearElement: function(elemId) {
            if (elemId) {
                $CQ("#" + elemId).html("");
            }
        },

        /**
         * Checks whether or not the specified object exists in the array.
         * @static
         * @param {Object} o The object to check for
         * @return {Number} The index of o in the array (or -1 if it is not found)
         */
            //Copied from CQ.Ext
        indexOf : function(array, o) {
            for (var i = 0, len = array.length; i < len; i++) {
                if (array[i] == o) {
                    return i;
                }
            }
            return -1;
        },

        /**
         * Requests the specified URL from the server using GET. The request
         * will be synchronous, unless a callback function is specified.
         * @static
         * @param {String} url The URL to request
         * @param {Function} callback (optional) The callback function which is
         *        called regardless of success or failure and is passed the following
         *        parameter:<ul>
         *        <li><b>xhr</b> : Object<div class="sub-desc">The XMLHttpRequest object containing the response data.
         *        See <a href="http://www.w3.org/TR/XMLHttpRequest/">http://www.w3.org/TR/XMLHttpRequest/</a> for details about
         *        accessing elements of the response.</div></li>
         *        </ul>
         */
        load: function(url, callback, scope) {
            return CQ.shared.HTTP.get(url, callback, scope);
        },

        /**
         * Requests the specified URL from the server using POST. The request
         * will be synchronous, unless a callback function is specified.
         * The returned response object looks like this:
         * <pre><code>{ headers: { "Status": 200, ... } }</code></pre>
         * See constants above for all supported headers.
         * @static
         * @param {String} url The URL to request
         * @param {Function} callback (optional) The callback function which is
         *        called regardless of success or failure and is passed the following
         *        parameter:<ul>
         *        <li><b>xhr</b> : Object<div class="sub-desc">The XMLHttpRequest object containing the response data.
         *        See <a href="http://www.w3.org/TR/XMLHttpRequest/">http://www.w3.org/TR/XMLHttpRequest/</a> for details about
         *        accessing elements of the response.</div></li>
         *        </ul>
         * @param {Object} params The parameters
         * @param {Object} scope The scope
         */
        post: function(url, callback, params, scope) {
            return CQ.shared.HTTP.post(url, callback, params, scope);
        },

        /**
         * Returns the current page path.
         * @static
         * @return {String} Page path
         */
        getPagePath: function() {
            return CQ.shared.HTTP.getPath();
        },

        /**
         * Removes all parts but the path from the specified URL.
         * <p>Examples:<pre><code>
         /x/y.sel.html?param=abc => /x/y
         </code></pre>
         * <pre><code>
         http://www.day.com/foo/bar.html => /foo/bar
         </code></pre><p>
         * @static
         * @param {String} url The URL
         * @return {String} The path
         */
        getPath: function(url) {
            return CQ.shared.HTTP.getPath(url);
        },

        /**
         * Adds a parameter to the specified URL. The parameter name and
         * value will be URL-endcoded.
         * @static
         * @param {String} url The URL
         * @param {String} name The name of the parameter
         * @param {String/String[]} value The value of the parameter.
         *        Since 5.3, an array of strings can be passed
         * @return {String} The URL with the new parameter
         */
        addParameter: function(url, name, value) {
            return CQ.shared.HTTP.addParameter(url, name, value);
        },

        /**
         * Removes all parameter from the specified URL.
         * @static
         * @param {String} url The URL
         * @return {String} The URL without parameters
         */
        removeParameters: function(url) {
            return CQ.shared.HTTP.removeParameters(url);
        },

        /**
         * Removes the anchor from the specified URL.
         * @static
         * @param {String} url The URL
         * @return {String} The URL without anchor
         * Copied from CQ.HTTP
         */
        removeAnchor: function(url) {
            return CQ.shared.HTTP.removeAnchor(url);
        },

        /**
         * Returns the scheme and authority (user, hostname, port) part of
         * the specified URL or an empty string if the URL does not include
         * that part.
         * @static
         * @param {String} url The URL
         * @return {String} The scheme and authority part
         */
            //Copied from CQ.HTTP
        getSchemeAndAuthority: function(url) {
            return CQ.shared.HTTP.getSchemeAndAuthority(url);
        },

        /**
         * Removes scheme, authority and context path from the specified
         * absolute URL if it has the same scheme and authority as the
         * specified document (or the current one).
         * @static
         * @param {String} url The URL
         * @param {String} doc (optional) The document
         * @return {String} The internalized URL
         */
        internalize: function(url, doc) {
            return CQ.shared.HTTP.internalize(doc);
        },

        /**
         * Makes sure the specified relative URL starts with the context path
         * used on the server. If an absolute URL is passed, it will be returned
         * as-is.
         * @static
         * @param {String} url The URL
         * @param {boolean} encode true to encode the path of the URL (optional)
         * @return {String} The externalized URL
         * @since 5.3
         */
        externalize: function(url, encode) {
            return CQ.shared.HTTP.externalize(url, encode);
        },

        /**
         * Encodes the path of the specified URL if it is not already encoded.
         * Path means the part of the URL before the first question mark or
         * hash sign.<br>
         * See {@link CQ.shared.HTTP#encodePath} for details about the encoding.<br>
         * Sample:<br>
         * <code>/x/y+z.png?path=/x/y+z >> /x/y%2Bz.png?path=x/y+z</code><br>
         * Note that the sample would not work because the "+" in the request
         * parameter would be interpreted as a space. Parameters must be encoded
         * separately.
         * @static
         * @param {String} url The URL to encoded
         * @return {String} The encoded URL
         * @since 5.3
         */
        encodePathOfURI: function(url) {
            return CQ.shared.HTTP.encodePathOfURI(url);
        },

        /**
         * Encodes the specified path using encodeURI. Additionally <code>+</code>,
         * <code>#</code> and <code>?</code> are encoded.<br>
         * The following characters are not encoded:<br>
         * <code>0-9 a-z A-Z</code><br>
         * <code>- _ . ! ~ * ' ( )</code><br>
         * <code>; / : @ & = $ ,</code><br>
         * @static
         * @param {String} path The path to encode
         * @return {String} The encoded path
         * @since 5.3
         */
        encodePath: function(path) {
            return CQ.shared.HTTP.encodePath(path);
        },

        /**
         * Returns the context path used on the server.
         * @static
         * @return {String} The context path
         * @since 5.3
         */
        getContextPath: function() {
            return CQ.shared.HTTP.getContextPath();
        },

        /**
         * Detects the context path used on the server.
         * @private
         * @static
         * @since 5.3
         */
        detectContextPath: function() {
            return CQ.shared.HTTP.detectContextPath();
        },

        /**
         * Takes an object and converts it to an encoded URL. e.g. <code>CQ.Ext.urlEncode({foo: 1, bar: 2});</code>
         * would return "foo=1&bar=2".  Optionally, property values can be arrays, instead of keys and the resulting
         * string that's returned will contain a name/value pair for each array value.
         * @static
         * @param {Object} o
         * @return {String}
         */
            //Copied from CQ.HTTP
        urlEncode : function(o) {
            if (!o) {
                return "";
            }
            if (typeof o == 'string') {
                return o;
            }
            var buf = [];
            for (var key in o) {
                var ov = o[key], k = encodeURIComponent(key);
                var type = typeof ov;
                if (type == 'undefined') {
                    buf.push(k, "=&");
                } else if (type != "function" && type != "object") {
                    buf.push(k, "=", encodeURIComponent(ov), "&");
                } else if (typeof ov == "array") {
                    if (ov.length) {
                        for (var i = 0, len = ov.length; i < len; i++) {
                            buf.push(k, "=", encodeURIComponent(ov[i] === undefined ? '' : ov[i]), "&");
                        }
                    } else {
                        buf.push(k, "=&");
                    }
                }
            }
            buf.pop();
            return buf.join("");
        },

        /**
         * Returns a base 16 encoded UID based on a timestamp and a random number.
         * @static
         * @return {String} UID
         */
        getUID: function() {
            //concatenate a timestamp + a 42 bits number ( 2^42- 1)
            var r = Math.floor(Math.random() * (Math.pow(2, 42) - 1));
            return this.getTimestamp().toString(16) + r.toString(16);
        },

        /**
         * Returns a timestamp.
         * @static
         * @return {Number} Timestamp
         */
        getTimestamp: function() {
            var d = new Date();
            return d.getTime();
        },

        /**
         * Inserts a string every x character into another string.
         * @static
         * @param {String} str Source string
         * @param {Number} every Inserts <b>every</b> x characters
         * @param {String} separator String to insert
         * @return {String} The string with inserted separators
         */
        insert: function(str, every, separator) {
            if (!str || isNaN(every) || !separator) return str;
            var res = "";
            var from = 0;
            var to = every;
            while (to < str.length) {
                res += str.substring(from, to) + separator;
                from += every;
                to += every;
            }
            if (from < str.length) {
                res += str.substring(from);
            }
            return res;
        },

        /**
         * @private
         */
        addListener: function () {
            if (window.addEventListener) {
                return function(el, eventName, fn, capture) {
                    el.addEventListener(eventName, fn, (capture));
                };
            } else if (window.attachEvent) {
                return function(el, eventName, fn, capture) {
                    el.attachEvent("on" + eventName, fn);
                };
            } else {
                return function() {
                };
            }
        },

        /**
         * @private
         */
        removeListener: function() {
            if (window.removeEventListener) {
                return function (el, eventName, fn, capture) {
                    el.removeEventListener(eventName, fn, (capture));
                };
            } else if (window.detachEvent) {
                return function (el, eventName, fn) {
                    el.detachEvent("on" + eventName, fn);
                };
            } else {
                return function() {
                };
            }
        }
    };
};

/**
 * A helper class providing a set of clickstream cloud rendering utility methods.
 * <br>
 * @class CQ_Analytics.ClickstreamcloudRenderingUtils
 */
CQ_Analytics.ClickstreamcloudRenderingUtils = new function() {
    return {
        /**
         * Creates a link DOM element.
         * @static
         * @param {String} text Text
         * @param {Function} func Onclick method
         * @param {Object} props Tag attributes
         * @return {Element} Link DOM element.
         */
        createLink: function(text, func, props, anchor) {
            var link = document.createElement("a");
            link.href = anchor;
            link.onclick = func;
            link.innerHTML = text;
            if (props) {
                for (var p in props) {
                    if (props.hasOwnProperty(p)) {
                        link[p] = props[p];
                    }
                }
            }
            return link;
        },

        /**
         * Creates a link DOM element.
         * @static
         * @param {String} text Text
         * @param {String} href Href
         * @param {Object} title Title
         * @return {Element} Link DOM element.
         */
        createStaticLink: function(text, href, title) {
            var link = document.createElement("a");
            link.href = href;
            link.innerHTML = text;
            link.title = title;
            link.alt = title;
            return link;
        },

        /**
         * Creates a span DOM element with format "property = value"
         * @static
         * @param {String} label Property label
         * @param {String} value Value
         * @param {String} cssClass CSS class name
         * @param {String} title Span title
         * @return {Element} Span DOM element.
         */
        createNameValue: function(label, value, cssClass, title) {
            var span = document.createElement("span");
            span.className = cssClass || "ccl-data";
            span.innerHTML = label + " = " + value;
            span.title = title;
            span.alt = title;
            return span;
        },

        /**
         * Creates a span DOM element
         * @static
         * @param {String} text Span inner HTML
         * @param {String} cssClass CSS class name
         * @param {String} title Span title
         * @return {Element} Span DOM element.
         */
        createText: function(text, cssClass, title) {
            var span = document.createElement("span");
            span.className = cssClass || "ccl-data";
            if (text && text.indexOf && (
                (text.indexOf("/home") != -1 && text.indexOf("/image") != -1)
                    || (text.indexOf("/") != -1 && text.indexOf(".png") != -1))) {
                //image
                span.innerHTML = "<img src=\"" + text + ".prof.thumbnail.png\" border=\"0\">";
            }
            else if (text && text.indexOf && text.indexOf("www.gravatar.com") != -1) {
                span.innerHTML = "<img src=\"" + text + "\">";
            }
            else {
                span.innerHTML = text;
            }
            span.title = title;
            span.alt = title;
            return span;
        },

        /**
         * Creates a span DOM element with format "property = value" and transforms
         * it to a text input field on click.
         * @static
         * @param {String} name Property label
         * @param {String} value Value
         * @return {Element} Span DOM element.
         */
        createEditablePropertySpan: function(name, value) {
            //TODO generalize css classes
            var onclick = "var editSpan = this.nextSibling; " +
                "this.style.display = 'none'; " +
                "editSpan.style.display = 'block';";
            var onblur = "var editSpan = this.parentNode; " +
                "var readSpan = this.parentNode.previousSibling;" +
                "var newValue = this.value;" +
                "editSpan.style.display = 'none'; " +
                "readSpan.innerHTML = '" + name + " = '+value; " +
                "readSpan.style.display = 'block';";
            var span = document.createElement("span");
            span.innerHTML = "<span class=\"ccl-data\" onclick=\"" + onclick + "\">" + name + " = " + value + "</span>";
            span.innerHTML += "<span class=\"ccl-data\" style=\"display: none;\">" + name + " = <input class=\"ccl-input\" type=\"text\" value=\"" + value + "\" onblur=\"" + onblur + "\"></span>";
            span.className = "ccl-data";
            return span;
        }
    }
};

/**
 * A helper class providing a set of ClientContext utility methods.
 * <br>
 * @class CQ_Analytics.ClientContextUtils
 * @since 5.5
 */
CQ_Analytics.ClientContextUtils = new function() {
    return {
        /**
         * Renders a store property value into the DOM. On store update, rendering will be updated.
         * @static
         * @param {String} id Id of the DOM element that will contain the property rendering
         * @param {String} storeName Name of the store
         * @param {String} propertyName Name of the property
         * @param {String} prefix (Optional) Fixed prefix to prepend to property value
         * @param {String} suffix (Optional) Fixed suffix to append to property value
         * @param {String} defaultValue (Optional) Default value to render if property is not in the store
         */
        renderStoreProperty: function(id, storeName, propertyName, prefix, suffix, defaultValue) {
            if (CQ_Analytics && CQ_Analytics.CCM) {
                CQ_Analytics.CCM.onReady(function() {
                    var init = function() {
                        var store = CQ_Analytics.StoreRegistry.getStore(storeName);
                        if (store) {
                            var renderer = function() {
                                var value = store.getProperty(propertyName) || defaultValue;
                                var el = $CQ("#" + id);

                                if (el.attr("contenteditable") &&
                                    /* test needed for IE7 */
                                    el.attr("contenteditable") != "inherit") return;

                                if (typeof(value) == "string" &&
                                    (
                                        (value.indexOf("/") == 0 &&
                                            (value.toLowerCase().indexOf(".png") != -1
                                                || value.toLowerCase().indexOf(".jpg") != -1
                                                || value.toLowerCase().indexOf(".gif") != -1)
                                            || value.toLowerCase().indexOf("http") == 0)
                                        )
                                    ) {
                                    if (!value || value == "") {
                                        el.children().remove();
                                        if( CQ_Analytics.isUIAvailable) {
                                            el.html(CQ.I18n.getMessage("No", null, "Ex: No address, No keywords") + " " + propertyName);
                                        } else {
                                            el.html("No " + propertyName);
                                        }
                                    } else {
                                        var url = "";
                                        if (el.parents(".cq-cc-thumbnail").length == 0 ||
                                            value.toLowerCase().indexOf("http") == 0 ||
                                            value.indexOf("/libs/wcm/mobile") == 0) {
                                            url = value.replace(new RegExp("&amp;", "g"), "&");
                                        } else {
                                            url = "/etc/clientcontext/shared/thumbnail/content.png";
                                            url = CQ.shared.HTTP.addParameter(url, "path", CQ_Analytics.Variables.replaceVariables(value));
                                        }
                                        url = CQ_Analytics.Variables.replaceVariables(url);
                                        if (el.find("div").css("background-image") != "url(" + url + ")") {
                                            if (store.fireEvent("beforepropertyrender", store, id) !== false) {
                                                //image
                                                el.html("");
                                                el.children().remove();
                                                $CQ("<div>")
                                                    .addClass("cq-cc-thumbnail-img")
                                                    .css("background-image", "url(" + CQ.shared.HTTP.externalize(url) + ")")
                                                    .appendTo(el);
                                                store.fireEvent("propertyrender", store, id);
                                            }
                                        }
                                    }
                                } else {
                                    value = CQ_Analytics.Variables.replaceVariables(value);
                                    if( CQ_Analytics.isUIAvailable) {
                                        value = (!value || value == "") ?
                                            CQ.I18n.getMessage("No", null, "Ex: No address, No keywords") + " " + propertyName :
                                            value = prefix + value + suffix;
                                    } else {
                                        value = (!value || value == "") ?
                                            "No " + propertyName :
                                            value = prefix + value + suffix;
                                    }
                                    if (el.html() != value) {
                                        if (store.fireEvent("beforepropertyrender", store, id) !== false) {
                                            el.html(value);
                                            store.fireEvent("propertyrender", store, id);
                                        }
                                    }
                                }
                            };

                            if (store.fireEvent("beforeinitialpropertyrender", store, id) !== false) {
                                renderer();

                                if (store.addListener) {
                                    store.addListener('update', function() {
                                        renderer();
                                    });
                                }

                                store.fireEvent("initialpropertyrender", store, id);
                            }
                        }
                    };

                    CQ_Analytics.ClientContextUtils.onStoreRegistered(storeName, init);
                });
            }
        },

        /**
         * Renders a store into the DOM. On store update, rendering will be updated. Rendering is done by calling the
         * <code>renderer</code> method of the store.
         * @static
         * @param {String} id Id of the DOM element that will contain the property rendering
         * @param {String} storeName Name of the store
         */
        renderStore: function(id, storeName) {
            if (CQ_Analytics && CQ_Analytics.CCM && id && storeName) {
                CQ_Analytics.CCM.onReady(function() {
                    var init = function() {
                        var store = CQ_Analytics.StoreRegistry.getStore(storeName);
                        if (store) {
                            store.divId = id;
                            var renderer = function() {
                                if (store.fireEvent("beforerender", store, store.divId) !== false) {
                                    store.renderer(store, store.divId);
                                    store.fireEvent("render", store, store.divId);
                                }
                            };

                            if (store.fireEvent("beforeinitialrender", store, id) !== false) {
                                renderer();

                                if (store.addListener) {
                                    store.addListener('update', function() {
                                        renderer();
                                    });
                                }

                                store.fireEvent("initialrender", store, id);
                            }
                        }
                    };

                    CQ_Analytics.ClientContextUtils.onStoreRegistered(storeName, init);
                });
            }
        },

        /**
         * Returns a list of options: list of stores registered in the {@link CQ_Analytics.StoreRegistry}.
         * @static
         * @return {Object[]} Computed options
         */
        storesOptionsProvider: function() {
            var options = [];
            var stores = CQ_Analytics.StoreRegistry.getStores();
            for (var name in stores) {
                options.push({
                    value: name
                });
            }
            return options;
        },

        /**
         * Returns a list of options: list of properties of a store.
         * @static
         * @param {String} storeName Name of the store
         * @param {Boolean} showValue (Optional) True to add value to the options (defaults to false)
         * @return {Object[]} Computed options
         */
        storePropertiesOptionsProvider: function(storeName, showValue) {
            var options = [];
            var store = CQ_Analytics.StoreRegistry.getStore(storeName);
            if (store) {
                var names = store.getPropertyNames();
                for (var i = 0; i < names.length; i++) {
                    var value = names[i];
                    if (!CQ.shared.XSS.KEY_REGEXP.test(value)) {
                        var o = {
                            value: value
                        };

                        if (showValue) {
                            o["text"] = value + " - " + store.getProperty(value);
                        }
                        options.push(o);
                    }
                }
            }
            return options;
        },

        /**
         * Executes a callback function when a store is registered.
         * @static
         * @param {String} storeName Name of the store
         * @param {Function} callback Function to execute
         */
        onStoreRegistered: function(storeName, callback) {
            if (callback) {
                var store = CQ_Analytics.StoreRegistry.getStore(storeName);
                if (store) {
                    callback.call(store, store);
                } else {
                    CQ_Analytics.CCM.addListener('storeregister', function(e, sessionstore) {
                        if (sessionstore.getName() == storeName) {
                            callback.call(sessionstore, sessionstore);
                        }
                    });
                }
            }
        },

        /**
         * Executes a callback function when a store is initialized. Some store might be initialized several times
         * (default init + asynchronous data loading): use delay parameter to try to get only one call of the callback.
         * A timeout is defined before every init event, next init event kills all previous calls. Latest one execute
         * the callback. WARNING: if time between 2 init events is bigger than timeout, callback is called several
         * times.
         * @static
         * @param {String} storeName Name of the store
         * @param {Function} callback Function to execute
         * @param {Boolean/Number} delay True to enable a default delay
         * (value is {@link #ClientContextUtils.DEFAULT_INIT_DELAY DEFAULT_INIT_DELAY} or a number of milliseconds (defaults
         * false).
         */
        onStoreInitialized: function(storeName, callback, delay) {
            if( delay === true) {
                delay = CQ_Analytics.ClientContextUtils.DEFAULT_INIT_DELAY;
            }

            var internal_callback = function() {
                var store = CQ_Analytics.StoreRegistry.getStore(storeName);
                if( store.DELAYED_INIT_TIMEOUT ) {
                    window.clearTimeout(store.DELAYED_INIT_TIMEOUT);
                    store.DELAYED_INIT_TIMEOUT = null;
                }

                if( delay > 0 ) {
                    store.DELAYED_INIT_TIMEOUT = window.setTimeout(function() {
                        store.DELAYED_INIT_TIMEOUT = null;
                        callback.call(store, "initialize", store);
                    }, delay);
                } else {
                    callback.call(store, "initialize", store);
                }
            };

            var store = CQ_Analytics.StoreRegistry.getStore(storeName);
            if (store) {
                if( store.isInitialized()) {
                    internal_callback.call(store);
                    store.addListener("initialize",function(event, store) {
                        internal_callback.call(store);
                    });
                } else {
                    store.addListener("initialize",function(event, store) {
                        internal_callback.call(store);
                    });
                }
            } else {
                    CQ_Analytics.CCM.addListener('storeregister', function(e, sessionstore) {
                        if (sessionstore.getName() == storeName) {
                            CQ_Analytics.ClientContextUtils.onStoreInitialized(storeName, callback, delay);
                        }
                    });
                }
        },

        /**
         * Inits the ClientContext.
         * @static
         * @param {String} ccPath ClientContext path
         * @param {String} pagePath Current page path
         */
        init: function(ccPath, pagePath) {
            CQ_Analytics.ClientContextMgr.PATH = ccPath;
            CQ_Analytics.ClientContextMgr.loadConfig(null, true);

            var url = CQ.shared.HTTP.externalize(ccPath + "/content/jcr:content/stores.init.js");
            url = CQ.shared.HTTP.addParameter(url, "path", pagePath);
            url = CQ.shared.HTTP.noCaching(url);
            //jquery will do the eval
            var res = CQ.shared.HTTP.get(url);
        },

        /**
         * Inits the ClientContext UI.
         * @static
         * @param {String} ccPath ClientContext path
         * @param {String} pagePath Current page path
         */
        initUI: function(ccPath, pagePath, editMode) {
            CQ_Analytics.ClientContextUI.create(ccPath, pagePath, editMode);
        }

    }
};

/**
 * Default init delay. See {@link #onStoreInitialized}.
 * @static
 * @type Number
 */
CQ_Analytics.ClientContextUtils.DEFAULT_INIT_DELAY = 200;

/**
 * A helper class providing a set of ClientContext utility methods to handle variables: a variable is a marker used
 * in a store property value to referenced the value of another property. Format: ${/storeName/propertyName}
 * <br>
 * @class CQ_Analytics.Variables
 * @since 5.5
 */
CQ_Analytics.Variables = new function() {
    return {
        /**
         * Returns if a value contains a variable of not.
         * @static
         * @param {String} value The value to test
         */
        containsVariable: function(value) {
            return CQ_Analytics.Variables.getVariables(value).length > 0;
        },

        /**
         * Returns the variables contained into a value.
         * @static
         * @param {String} value The value
         */
        getVariables: function(value) {
            if (!value || typeof(value) != "string") return [];
            var res = value.match(new RegExp("\\$\\{([\\w/]*)\\}", "ig"));
            return res ? res : [];
        },

        /**
         * Replaces the variables into a value by their corresponding values in the ClientContext and returns
         * the result.
         * @static
         * @param {String} value The value
         * @return {String} The result of the replacement
         */
        replaceVariables: function(value) {
            if (!value) return value;
            //keep history to avoid infinite loops
            var history = "";
            var res = value;
            var variables = CQ_Analytics.Variables.getVariables(value);
            while (variables.length > 0 && history.indexOf(variables.join()) == -1) {
                for (var i = 0; i < variables.length; i++) {
                    //current format should ${store/property}
                    //extract store/property
                    var vName = CQ_Analytics.Variables.getPropertyPath(variables[i]);
                    var v = CQ_Analytics.ClientContext.get(vName) || "";
                    res = CQ_Analytics.Variables.replace(res,vName,v);
                }
                history += variables.join();
                variables = CQ_Analytics.Variables.getVariables(res);
            }
            return res;
        },

        /**
         * Returns the path to the property contained in the provided variable.
         * @static
         * @param {String} variable The variable
         * @return The path to the property. Null if no path available.
         */
        getPropertyPath: function(variable) {
            if( !variable || variable.length < 2 ) return null;
            return variable.substring(2, variable.length - 1);
        },

        /**
         * Returns the name of the property contained in the provided variable.
         * @static
         * @param {String} variable The variable
         * @return The name of the property. Null if no property name available.
         */
        getPropertyName: function(variable) {
            var p = CQ_Analytics.Variables.getPropertyPath(variable);
            if( p ) {
                var s = p.split("/");
                if(s.length == 3) {
                    return s[2];
                }
            }
            return null;
        },

        /**
         * Returns the name of the store contained in the provided variable.
         * @static
         * @param {String} variable The variable
         * @return The name of the store. Null if no store name available.
         */
        getStoreName: function(variable) {
            var p = CQ_Analytics.Variables.getPropertyPath(variable);
            if( p ) {
                var s = p.split("/");
                if(s.length > 1) {
                    return s[1];
                }
            }
            return null;
        },

        /**
         * Replaces all instances of the variable <code>${key}</code> by <code>value</code>
         * in the provided <code>string</code> and returns the result.
         * @static
         * @param {String} string The string
         * @param {String} key The variable key
         * @param {String} value The replacement value
         * @return {String} The result of the replacement
         */
        replace: function(string, key, value) {
            return string.replace(new RegExp("\\\$\\{" + key + "\\}", "ig"), value);
        }
    }
};