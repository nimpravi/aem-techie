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

if (CQ_Analytics.SocialGraphMgr) {

    /**
     * Loads and renders the social graph.
     * @param {String} divId Id of the div to render to
     * @static
     */
    CQ_Analytics.SocialGraphMgr.internalRenderer = function(divId) {
        var uid = CQ_Analytics.ProfileDataMgr.getProperty("authorizableId");
        CQ_Analytics.SocialGraphMgr.lastUid = uid;

        var profilePath = CQ_Analytics.ProfileDataMgr.getProperty("path");

        var url = profilePath + ".form.html";
        url += CQ_Analytics.ClientContextMgr.getClientContextURL("/contextstores/socialgraph.js");
        url += "?limit=10";
        url += "&callback=${callback}";

        CQ_Analytics.SocialGraphMgr.load(CQ.shared.HTTP.externalize(url), {}, function() {
            $CQ("#" + divId).children().remove();

            CQ_Analytics.SocialGraphMgr.reset();

            var name = CQ_Analytics.ProfileDataMgr.getProperty("formattedName");
            var div = $CQ("<div>").addClass("cq-socialgraph");
            $CQ("<div>")
                .addClass("cq-socialgraph-text")
                .html(CQ.I18n.getMessage("{0}'s friends and followers (social graph):", name, "{0} is a placeholder for a person's name") + " ")
                .appendTo(div);

            var toDisplay = {};

            var data = this.getJSON();

            var friends = data["friends"];
            if (friends) {
                for (var i in friends) {
                    if (friends[i]["authorizableId"]) {
                        toDisplay[friends[i]["authorizableId"]] = friends[i];
                    }
                }
            }
            var followers = data["followers"];
            if (followers) {
                for (var i in followers) {
                    if (followers[i]["authorizableId"]) {
                        toDisplay[followers[i]["authorizableId"]] = followers[i];
                    }
                }
            }

            var count = 0;
            for (var tod in toDisplay) {
                var f = toDisplay[tod];
                $CQ("<img>")
                    .attr("title", f["formattedName"] || f["authorizableId"])
                    .attr("src", CQ.shared.HTTP.externalize(f["avatar"]))
                    .appendTo(div);
                count++;
                if (count >= 9) break;
            }

            div.hide();
            $CQ("#" + divId).append(div);
            div.fadeIn("fast");
        });
    };

    /**
     * Delegates the rendering to {@link #SocialGraphMgr.internalRenderer}.
     * @param {String} store The store to render
     * @param {String} divId Id of the div to render to
     * @static
     */
    CQ_Analytics.SocialGraphMgr.renderer = function(store, divId) {
        var uid = CQ_Analytics.ProfileDataMgr.getProperty("authorizableId");
        if (uid != CQ_Analytics.SocialGraphMgr.lastUid) {
            CQ_Analytics.SocialGraphMgr.internalRenderer(divId);
        }
    };
}