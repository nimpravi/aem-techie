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

if( CQ_Analytics.ClientContextUI ) {
    $CQ(function() {
        CQ_Analytics.ClientContextUI.addListener("render", function() {
            CQ_Analytics.ClientContextUI.ipe = new CQ.ipe.PlainTextEditor({
                "enterKeyMode": "save",
                "tabKeyMode": "save"
            });
        });

        CQ_Analytics.CCM.addListener('storeregister', function(e, sessionstore) {
            var initIPE = function(event) {
                var ipe = CQ_Analytics.ClientContextUI.ipe;
                var $t = $CQ(this);
                var $parent = $t.parent();

                var store = $t.attr("data-store");
                var property = $t.attr("data-property");
                var propertyPath = "/" + store + "/" + property;

                var stop = function() {
                    if( ipe.running ) {
                        if( !ipe.isCancelled ) {
                            ipe.finish();
                        } else {
                            CQ_Analytics.ClientContext.set(
                                ipe.editComponent.propertyPath,
                                ipe.editComponent.initialValue
                            );
                            ipe.cancel();
                        }
                        $CQ(document).unbind("click",handleDocumentClick);
                        ipe.editComponent.parent.removeClass("cq-clientcontext-editing");
                        ipe.running = false;
                        ipe.isCancelled = false;
                    }
                    delete ipe.clicked;
                };

                if( ! ipe.running ) {
                    var initialValue = CQ_Analytics.ClientContext.get(propertyPath);
                    if( typeof(initialValue) == "string" && initialValue.toLowerCase().indexOf("http") == 0) {
                        initialValue= initialValue.replace(new RegExp("&amp;","g"),"&");
                    }

                    var handleDocumentClick = function() {
                        if( !ipe.clicked || ipe.clicked != ipe.editComponent.propertyPath ) {
                            stop();
                        }
                        ipe.clicked = null;
                    };

                    var editMockup = {
                        store: store,
                        property: property,
                        propertyPath: propertyPath,
                        initialValue: initialValue,
                        parent: $parent,
                        updateParagraph: function(textPropertyName, editedContent) {
                            if( editedContent && typeof(editedContent) == "string") {
                                editedContent = editedContent.replace(new RegExp("&amp;","g"),"&");
                            }
                            CQ_Analytics.ClientContext.set(this.propertyPath, editedContent);
                        },
                        cancelInplaceEditing: function() {
                            ipe.isCancelled = true;
                            stop();
                        },
                        finishInplaceEditing: function() {
                            stop();
                        },
                        refreshSelf: function() {
                            ipe.editComponent.parent.removeClass("cq-clientcontext-editing");
                        }
                    };
                    $parent.addClass("cq-clientcontext-editing");
                    ipe.start(
                        editMockup,
                        CQ.Ext.get($t[0]),
                        editMockup.initialValue
                    );

                    $CQ(document).bind("click",handleDocumentClick);
                    //$CQ(document).bind("keyup",stop);

                    ipe.running = true;
                    ipe.clicked = null;

                    event.stopPropagation();
                } else {
                    if( ipe.editComponent.propertyPath != propertyPath ) {
                        stop();
                    } else {
                        ipe.clicked = propertyPath;
                    }
                }
            };

            sessionstore.addListener("initialpropertyrender",function(event, store, divId){
                if( $CQ("#" + divId).parents(".cq-cc-content").length > 0) {
                    $CQ("[data-store][data-property]", $CQ("#" + divId).parent()).bind("click",initIPE);
                }
            });

            sessionstore.addListener("beforerender",function(event, store, divId){
                $CQ("[data-store][data-property]", $CQ("#" + divId).parent()).unbind("click",initIPE);
            });

            sessionstore.addListener("render",function(event, store, divId){
                $CQ("[data-store][data-property]", $CQ("#" + divId).parent()).bind("click",initIPE);
            });
        });
    });
}

