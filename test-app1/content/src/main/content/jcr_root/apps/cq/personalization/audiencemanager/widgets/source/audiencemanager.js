// declare variables correctly.
CQ = window.CQ || {};
CQ.audiencemanager = CQ.audiencemanager || {};

CQ.Ext.namespace("CQ.audiencemanager");

CQ.audiencemanager.AudienceManager = CQ.audiencemanager.AudienceManager ||
        {

            /**
             * Shows a progress bar dialog.
             *
             * @param dialog
             *            Parent dialog to attach the progress bar to.
             * @param isShown
             *            Indicator if progress dialog is shown or not.
             */
            showButtonIndicator : function(dialog, isShown) {
                var btn = dialog.find("localName", "connectButton")[0];
                if (this.labelBtn === null) {
                    this.labelBtn = btn.getText();
                }
                if (!isShown) {
                    CQ.Ext.Msg.wait(CQ.I18n.getMessage("Connection successful")).hide();
                } else {
                    CQ.Ext.Msg.wait(CQ.I18n.getMessage("Connecting to AudienceManager ..."));
                }
            },

            /**
             * Gets a field with the provided key from a panel.
             *
             * @param panel
             *            Panel which holds the field.
             * @param key
             *            Field name
             */
            getField : function(panel, key) {
                var items = panel.find("name", "./" + key);
                if ((CQ.Ext.isArray(items)) && (items.length > 0))
                    return items[0];
            },

            /**
             * Loads the form with data after loading.
             */
            afterRender : function(comp) {
                var dialog = comp.findParentByType('dialog');
                dialog.on('loadcontent', function(dlg) {
                    var searchformxml = dialog.find('name', './searchformxml');
                    if (searchformxml[0].getValue() !== '') {
                        comp.setText(CQ.I18n.getMessage('Re-Connect to AudienceManager'));
                    }
                });
            },

            fieldEmpty : function fieldEmpty(dialog, field, msg) {
                if (!field || field.getValue() === "") {
                    this.showButtonIndicator(dialog, false);
                    CQ.Ext.Msg.alert(CQ.I18n.getMessage("Error"), msg);
                    return true;
                }
                return false;
            },

            checkDil : function(comp, evt) {
                try {
                    var dialog = comp.findParentByType('dialog');
                    var partner = this.getField(dialog, 'partner');
                    var container = this.getField(dialog, 'container');

                    if (this.fieldEmpty(dialog, partner, CQ.I18n.getMessage("Please enter the Parner name.")) ||
                            this.fieldEmpty(dialog, container, CQ.I18n.getMessage("Please enter the Container ID."))) {
                        return;
                    }
                    this.showButtonIndicator(dialog, true);
                    // This assumes that the Audience Manager DIL is loaded.
                    // we can check the DIL by performing a JSONP request to the correct URL.
                    var checkUrl = "http://";
                    if ( "https:" == document.location.protocol ) {
                        checkUrl = "https://";
                    }
                    // the =? at the end is a placeholder for jQuery jsonp. Do not remove.
                    checkUrl += encodeURIComponent(partner.getValue())+".demdex.net/event?d_cb=?";
                    var that = this;
                    $.jsonp({
                        url : checkUrl,
                        callback : "__checkpartner",
                        data : {
                            "d_nsid" : container.getValue(),
                            "d_rtbd" : "json", // response type json
                            "d_dst" : 1 // return destination inside the json.
                        },
                        success : function(json, textStatus, xOptions) {
                                        // console.log("Result Json Data is " + JSON.stringify(json));
                                        // at the moment we don't do anything with the response

                                        // success
                                        that.showButtonIndicator(dialog, false);

                                        dialog.find("localName", "connectButton")[0].setText(CQ.I18n
                                                .getMessage('Re-Connect to AudienceManager'));
                                        // show the auth dialog
                                        var username = CQ.Util.build({
                                            allowBlank : false,
                                            fieldDescription : CQ.I18n.getMessage('Audience Manager REST API Username'),
                                            fieldLabel : CQ.I18n.getMessage('Username'),
                                            fieldSubLabel : CQ.I18n.getMessage('not stored'),
                                            name : './username',
                                            xtype : 'textfield'
                                        });
                                        var password = CQ.Util.build({
                                            allowBlank : false,
                                            fieldDescription : CQ.I18n.getMessage('Audience Manager REST API Password'),
                                            fieldLabel : CQ.I18n.getMessage('Password'),
                                            fieldSubLabel : CQ.I18n.getMessage('not stored'),
                                            name : './password',
                                            xtype : 'password'
                                        });


                                        var authDialog = null;
                                        authDialog = new CQ.Dialog({
                                            "height" : 250,
                                            "width" : 400,
                                            "title" : CQ.I18n.getMessage("Login to AudienceManager REST API"),
                                            "buttons" : [
                                                    {
                                                        "text" : CQ.I18n.getMessage("OK"),
                                                        "handler" : function() {
                                                            if (username.isValid() && password.isValid()) {
                                                                authDialog.hide();
                                                                that.retrieveTokens(username.getValue(), password.getValue(),
                                                                        partner.getValue(), container.getValue(), authDialog, dialog);
                                                            }
                                                        }
                                                    }, CQ.Dialog.CANCEL ],
                                            "items" : {
                                                "xtype" : 'panel',
                                                "items" : [ username, password ]
                                            }
                                        });
                                        authDialog.show();

                                    },
                            error : function(xOptions, textStatus) {
                                        that.showButtonIndicator(dialog, false);
                                        // console.log("Oops an error " + textStatus + "," + xOptions);
                                        CQ.Ext.Msg
                                                .alert(
                                                        CQ.I18n.getMessage("Error"),
                                                        CQ.I18n
                                                                .getMessage("Client Connection to AudienceManager could not be established, please check the partner name"));
                                    }
                    });

                    // Show the auth dialog to retrieve the REST API tokens and
                    // check access.
                    // The server must have already been configured with the
                    // ClientID and Client Secret for this installation.
                    // This is done by setting
                    // com.adobe.cq.aam.client.AudienceManagerClientImpl:clientid
                    // and clientsecret in the OSGi
                    // properties.
                } catch (e) {
                    CQ.Ext.Msg.alert(CQ.I18n.getMessage("Error"), CQ.I18n
                            .getMessage("Client Connection to AudienceManager could not be established."));
                    console.log("Client Connection to AudienceManager could not be established. " + e + " " + e.stack);
                }
            },

            /**
             * Fetches search form XML from a remote location and stores it in a
             * hidden text area for later persisting.
             *
             * @param dialog
             */
            retrieveTokens : function(username, password, partner, containerId, authDialog, dialog) {

                this.showButtonIndicator(dialog, true);
                var that = this;

                $.post("/etc/cloudservices/audiencemanager/jcr:content.aamtest.json",
                        {
                        "username" : username,
                        "password" : password,
                        "partnerID" : partner,
                        "containerNSID" : containerId
                        }, function(data, textStatus, jqXHR) {

                            // extract the OAUthe response, and put it
                            // in the dialog ready for posting.
                            that.showButtonIndicator(dialog, false);

                            if ( data.status_codes && data.status_codes.partner_invalid ) {
                                console.log("Failed to retrive Oauth authorization from Audience Manager server , invalid partner ");
                                CQ.Ext.Msg.alert(CQ.I18n.getMessage("Error"), CQ.I18n
                                        .getMessage("Server Connection to AudienceManager could not be established, please check the partner and container."));
                                that.getField(dialog, 'oauthAccessToken').setValue("");
                                that.getField(dialog, 'oauthRefreshToken').setValue("");
                                that.getField(dialog, 'oauthExpiresDate').setValue("");
                                that.getField(dialog, 'oauthExpiresEpoch').setValue("");
                            } else if ( data.status_codes && data.status_codes.client_not_configured ) {
                                console.log("Failed to retrive Oauth authorization from Audience Manager server , client is not configured ");
                                CQ.Ext.Msg.alert(CQ.I18n.getMessage("Error"), CQ.I18n
                                        .getMessage("Server Connection to AudienceManager could not be established, please set AAM ClientID and Client Secret in the OSGi Console."));
                                that.getField(dialog, 'oauthAccessToken').setValue("");
                                that.getField(dialog, 'oauthRefreshToken').setValue("");
                                that.getField(dialog, 'oauthExpiresDate').setValue("");
                                that.getField(dialog, 'oauthExpiresEpoch').setValue("");
                            } else {
                                if ( data.access_token && data.expires_in_date && data.expires_in_epoch ) {


                                    // save the values
                                    that.getField(dialog, 'oauthAccessToken').setValue(data.access_token);
                                    that.getField(dialog, 'oauthRefreshToken').setValue(data.refresh_token);
                                    that.getField(dialog, 'oauthExpiresDate').setValue(data.expires_in_date);
                                    that.getField(dialog, 'oauthExpiresEpoch').setValue(data.expires_in_epoch);

                                    // enable edit save button

                                } else {
                                    that.getField(dialog, 'oauthAccessToken').setValue("");
                                    that.getField(dialog, 'oauthRefreshToken').setValue("");
                                    that.getField(dialog, 'oauthExpiresDate').setValue("");
                                    that.getField(dialog, 'oauthExpiresEpoch').setValue("");
                                    console.log("Failed to retrive Oauth authorization from Audience Manager server , invalid response "+JSON.stringify(data));
                                    CQ.Ext.Msg.alert(CQ.I18n.getMessage("Error"), CQ.I18n
                                            .getMessage("Server Connection to AudienceManager could not be established, please check credentials and ClientID/ClientSecret"));

                                }
                            }

                        }, "json" ).fail(function failedToGetOauth(data, textStatus, jqXHR){
                            that.getField(dialog, 'oauthAccessToken').setValue("");
                            that.getField(dialog, 'oauthRefreshToken').setValue("");
                            that.getField(dialog, 'oauthExpiresDate').setValue("");
                            that.getField(dialog, 'oauthExpiresEpoch').setValue("");
                            console.log("Failed to retrive Oauth authorization from Audience Manager server :"+textStatus);
                            CQ.Ext.Msg.alert(CQ.I18n.getMessage("Error"), CQ.I18n
                                    .getMessage("Server Connection to AudienceManager could not be established, please check credentials and ClientID/ClientSecret"));
                        });
            }

        };