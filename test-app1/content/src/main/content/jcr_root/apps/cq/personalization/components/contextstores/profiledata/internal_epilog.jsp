<%@page session="false"%><script type="text/javascript">
    $CQ(function() {
        CQ_Analytics.ClientContextUI.onLoad(function() {
            //profiledata slider
            var createSliderProf = function(event, show, vertical) {
                var url = CQ.shared.HTTP.addParameter("/bin/security/authorizables.json", "limit", 25);
                url = CQ.shared.HTTP.addParameter(url, "hideGroups", true);
                url = CQ.shared.HTTP.noCaching(url);
                CQ.shared.HTTP.get(url, function(options, success, response) {
                    if( !success ) return;
                    var profiles = CQ.shared.HTTP.eval(response);
                    if( !profiles || !profiles.authorizables) return;

                    var slider = new CQ_Analytics.Slider({
                        "vertical": vertical,
                        "wrap": "circular",
                        "animation": "slow",
                        "start": start,
                        "clazz": "cq-cc-slider-profiledata",
                        "parent": $CQ(".cq-cc-thumbnail-profiledata")
                    });
                    CQ_Analytics.ProfileDataMgr.slider = slider;

                    slider.init();

                    var getItemHTML = function(id, imagePath, title) {
                        var url = "/etc/designs/default/images/social/avatar.png";
                        if( imagePath ){
                            url = imagePath + "/image.prof.thumbnail.80.png";
                        }

                        var item = $CQ("<li>");
                        var img = $CQ("<img>")
                                .attr("src",_g.shared.HTTP.externalize("/etc/clientcontext/shared/thumbnail/content.png?path=" + url))
                                .attr("width", "80")
                                .attr("height", "80")
                                .attr("alt", title)
                                .attr("title", title)
                                .attr("data-id",id)
                                .bind("click",function() {
                                    CQ_Analytics.ProfileDataMgr.slider.select($CQ(this).parent().attr("jcarouselindex"));
                                })
                                .appendTo(item);
                        return item;
                    };

                    var start = 1;
                    var totalItems = 0;
                    var current = CQ_Analytics.ProfileDataMgr.getProperty("authorizableId") || "" ;
                    for(var i = 0; i < profiles.authorizables.length; i++) {
                        var p = profiles.authorizables[i];
                        if(p["picturePath"]) {
                            totalItems ++;
                            CQ_Analytics.ProfileDataMgr.slider.carousel.append(getItemHTML(p["rep:userId"], p["picturePath"], p["name"]));
                            if( p["rep:userId"] == current ) {
                                start = totalItems;
                            }
                        }
                    }

                    slider.onSelect = function(toLoadId) {
                        var generatedThumbnail = CQ_Analytics.ProfileDataMgr.getProperty("generatedThumbnail");

                        CQ_Analytics.ProfileDataMgr.loadProfile(toLoadId);

                        //set the generatedThumbnail has it was before
                        if( generatedThumbnail ) {
                            CQ_Analytics.ProfileDataMgr.addInitProperty("generatedThumbnail",generatedThumbnail);
                            CQ_Analytics.ProfileDataMgr.setProperty("generatedThumbnail",generatedThumbnail);
                        }
                    };

                    slider.getCurrentValue = function() {
                        return CQ_Analytics.ProfileDataMgr.getProperty("authorizableId");
                    };


                    slider.show();
                });
            };

            var handleSliderProf = function(event) {
                if( !CQ_Analytics.ProfileDataMgr.slider) {
                    createSliderProf.call(this, event, true);
                } else {
                    CQ_Analytics.ProfileDataMgr.slider.show();
                }
                event.stopPropagation();
            };

            $CQ(".cq-cc-thumbnail-profiledata").bind("click", handleSliderProf);
        });
    });
</script>
