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
 * The <code>CQ_Analytics.TagCloudMgr</code> object is a store providing tag cloud information.
 * @class CQ_Analytics.TagCloudMgr
 * @singleton
 * @extends CQ_Analytics.PersistedSessionStore
 */
if (CQ_Analytics.CCM && CQ_Analytics.TagCloudMgr) {
    /**
     * Renders the tagcloud as a section of the clickstreamcloud UI
     * @return {Element} DOM element
     * @private
     */
    CQ_Analytics.TagCloudMgr.createHTMLElement = function() {
        var div = document.createElement("div");
        var cloud = document.createElement("div");
        var currentTagCloud = this;
        cloud.className = "cloud";
        var nbTags = 0;
        this.each(function(tag, count) {
            var li = document.createElement("div");
            var dectil = currentTagCloud.calculateNtile(count, 10);
            var namespaceSplit = tag.split(":");
            var pathSplit = namespaceSplit[namespaceSplit.length - 1].split("/");
            li.innerHTML = pathSplit[pathSplit.length - 1];// + "<div class='count tag" + dectil + "'>&nbsp;(" + count + ")</div>";
            li.className = "tag";
            if (currentTagCloud.addedTags[tag]) {
                li.className += " new";
            }
            li.className += " tag" + dectil;
            li.title = tag + " (" + count + ")";
            li.setAttribute("data-property", tag);
            li.setAttribute("data-store", currentTagCloud.STORENAME);
            cloud.appendChild(li);
            // for proper wrapping in IE
            cloud.appendChild(document.createTextNode(" "));
            nbTags ++;
        });

        if( nbTags == 0 ) {
            var li = document.createElement("div");
            li.className = "tag notag";
            li.innerHTML = CQ.I18n.getMessage("No interest", null, "Tag cloud is empty");
            cloud.appendChild(li);
        }

        div.appendChild(cloud);

        return div;
    };

    CQ_Analytics.TagCloudMgr.renderer = function(store, targetId) {
        if( store && store.STORENAME == CQ_Analytics.TagCloudMgr.STORENAME ) {
            $CQ("#" + targetId).children().remove();
            $CQ("#" + targetId).append(store.createHTMLElement());
        }
    };

    CQ_Analytics.CCM.addListener("configloaded",function() {
        //add to std clickstream cloud ui
        CQ_Analytics.ClickstreamcloudUI.register(
                this.getSessionStore(),
                CQ_Analytics.CCM.getUIConfig(this.getName()),
                this.createHTMLElement);
    },CQ_Analytics.TagCloudMgr);
}