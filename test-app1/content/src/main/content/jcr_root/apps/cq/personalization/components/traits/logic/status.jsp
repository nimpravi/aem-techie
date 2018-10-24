<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@include file="/libs/foundation/global.jsp"%><%
    String id = "trait-status-" + currentNode.getPath();
    String traitsPath = currentNode.getPath() + ".trait.js";
    final String logic = currentNode.hasNode("andpar") ? "and" : "or";
%><script type="text/javascript">
{
    window.setTimeout(function() {
        var toEval = "";
        CQ_Analytics.Utils.load("<%=xssAPI.encodeForJSString(traitsPath)%>",function(config, status, response) {
            toEval = response.responseText;
        },this);

        var updateTraitStatus = function() {
            var elem = document.getElementById("<%=xssAPI.encodeForJSString(id)%>");
            if(elem && toEval) {
                var clientcontext = CQ_Analytics.ClientContextMgr.get();
                //for backward compatibitity
                var contextcloud = clientcontext;
                var clickstreamcloud = clientcontext;
                var b = eval(toEval);
                if( b ) {
                    elem.className = "segmenteditor-container-<%=logic%>-header logic-trait-resolved";
                } else {
                    elem.className = "segmenteditor-container-<%=logic%>-header logic-trait-unresolved";
                }
            }
        };

        if(CQ_Analytics.SegmentMgr) {
            CQ_Analytics.SegmentMgr.addListener("update",function() {
                updateTraitStatus();
            });
            updateTraitStatus();
        }

    },100);
}
</script>
