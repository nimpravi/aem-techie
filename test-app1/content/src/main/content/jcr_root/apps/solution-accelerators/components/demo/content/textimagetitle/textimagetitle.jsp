<%@page session="false"%><%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Text-Image component

  Combines the text and the image component

--%><%@ page import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.WCMMode,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image, com.day.cq.wcm.foundation.Placeholder" %><%
%><%@include file="/libs/foundation/global.jsp"%><%
    boolean isAuthoringUIModeTouch = Placeholder.isAuthoringUIModeTouch(slingRequest);

    Image image = new Image(resource, "image");

    // don't draw the placeholder in case UI mode touch it will be handled afterwards
    if (isAuthoringUIModeTouch) {
        image.setNoPlaceholder(true);
    }

    //drop target css class = dd prefix + name of the drop target in the edit config
    String ddClassName = DropTarget.CSS_CLASS_PREFIX + "image";

    if (image.hasContent() || WCMMode.fromRequest(request) == WCMMode.EDIT) {
        image.loadStyleData(currentStyle);
        // add design information if not default (i.e. for reference paras)
        if (!currentDesign.equals(resourceDesign)) {
            image.setSuffix(currentDesign.getId());
        }
        //image.addCssClass(ddClassName);
        image.addCssClass("img-thumbnail");
        image.setSelector(".img");
        image.setDoctype(Doctype.fromRequest(request));

        String divId = "cq-textimage-jsp-" + resource.getPath();
        String imageHeight = image.get(image.getItemName(Image.PN_HEIGHT));
        // div around image for additional formatting
        %>
<div class="col-md-4 inncol company1_innerDiv">
    <cq:include path="textImage" resourceType="/apps/solution-accelerators/components/demo/content/imagetitle" />
    <div class="for-border">
            <% image.draw(out); %><br>


            <%@include file="/libs/foundation/components/image/tracking-js.jsp"%>
            <%
        }
    
           String placeholder = (isAuthoringUIModeTouch && !image.hasContent())
                   ? Placeholder.getDefaultPlaceholder(slingRequest, component, "", ddClassName)
                   : "";
    %> 


        <p id="infoAgain"><%=currentNode.getProperty("text").getString()%>
            <cq:include path="titleLink" resourceType="solution-accelerators/components/demo/content/title"/>
        </p>
    </div>
</div>


