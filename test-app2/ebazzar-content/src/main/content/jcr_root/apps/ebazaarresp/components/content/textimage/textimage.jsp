<%@include file="/libs/foundation/global.jsp" %>
<%@ page
	import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.WCMMode,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image,
    java.util.Random,
    org.apache.commons.lang3.RandomStringUtils"%>
<%
%>
<cq:includeClientLib categories="ebazaarresp.textimage"/>
<cq:setContentBundle/>
<c:set var="title" value="${properties.textTitle}" />
<c:set var="topContentBeak" value="${properties.topContentBeak}" />
<c:set var="bottomContentBreak" value="${properties.bottomContentBreak}" />
<c:set var="topContentBeakNew" value="${properties.topContentBeakNew}" />
<c:set var="bottomContentBreakNew" value="${properties.bottomContentBreakNew}" />
<%Random rand = new Random();
if ( null ==  properties.get("anchor", String.class)){
	if (null != currentNode){
		currentNode.setProperty("anchor", RandomStringUtils.randomAlphanumeric(10));		
		currentNode.getSession().save();
	}
}%>
 
<div id="${properties.anchor}"  >
<c:choose>
    <c:when test="${not empty topContentBeakNew}">
        <c:if test="${('green' == topContentBeakNew) ||
           ('green small' ==topContentBeakNew) ||
		   ('green medium' ==topContentBeakNew) }">
        <hr class="${topContentBeakNew}" />
        </c:if>
    </c:when>
    <c:otherwise>
     <c:if test="${topContentBeak}">
        <hr class="green" />
        </c:if>
    </c:otherwise>
</c:choose>

<% 
Image image = new Image(resource, "image");
String imagePosition = "imageTop";

if(null != properties.get("imagePosition")){
imagePosition = properties.get("imagePosition").toString();

}
if (image.hasContent() || WCMMode.fromRequest(request) == WCMMode.EDIT) {
    image.loadStyleData(currentStyle);
    // add design information if not default (i.e. for reference paras)
    if (!currentDesign.equals(resourceDesign)) {
        image.setSuffix(currentDesign.getId());
    }
    //drop target css class = dd prefix + name of the drop target in the edit config
   if ("imageLeft".equalsIgnoreCase(imagePosition)) {
	   image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image place-left");	   
   } else if ( "imageRight".equalsIgnoreCase(imagePosition)){
	   image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image place-right");
   } else if ("imageTop".equalsIgnoreCase(imagePosition)){
	   image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image place-none");
   }
    if (null !=  properties.get("marginindent")){
        if ( properties.get("marginindent").equals("imageIndentTopright")){
             image.addCssClass("imageIndentTopright");
        }else if (properties.get("marginindent").equals("imageIndentRight")){
			 image.addCssClass("imageIndentRight");
        }else if(properties.get("marginindent").equals("imageIndentRight35")) {
             image.addCssClass("imageIndentRight35");
        }else if(properties.get("marginindent").equals("marginbottom0")) {
             image.addCssClass("marginbottom0");
        }

    }
  //  image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image place-left");
    // image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image place-right");
     //image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image place-none");
    image.setSelector(".img");
    image.setDoctype(Doctype.fromRequest(request));

    String divId = "cq-textimage-jsp-" + resource.getPath();
    String imageHeight = image.get(image.getItemName(Image.PN_HEIGHT));
    String imageWidth = image.get(image.getItemName(Image.PN_WIDTH));
    // div around image for additional formatting
    %><div class="${imagePosition}" id="<%= divId %>"  ><%
    %><% image.draw(out); %><%

    %><cq:text property="image/jcr:description" placeholder="" tagName="small"/><%
    %></div>
    <%@include file="/libs/foundation/components/image/tracking-js.jsp"%>
    <%
}
%><cq:text property="text" tagClass="text"/>
<c:choose>
    <c:when test="${not empty bottomContentBreakNew}">
        <c:if test="${('green' == bottomContentBreakNew) ||
           ('green small' ==bottomContentBreakNew) ||
		   ('green medium' ==bottomContentBreakNew) }">
        <hr class="${bottomContentBreakNew}" />
        </c:if>
    </c:when>
    <c:otherwise>
     <c:if test="${bottomContentBreak}">
        <hr class="green" />
        </c:if>
    </c:otherwise>
</c:choose>

</div>