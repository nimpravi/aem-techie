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
%><%@ page import="com.day.cq.tagging.TagManager,
                   com.day.cq.tagging.Tag" %><%
    String tagId = properties.get("tagId", "");
    String operator = properties.get("operator", "");
    String value = properties.get("value", "");
    String jsObject = properties.get("jsObject", "null");
    String valueFormat = properties.get("valueFormat", "");
    if(!"".equals(tagId)) {
        TagManager tagManager = slingRequest.getResourceResolver().adaptTo(TagManager.class);
        Tag tag = tagManager.resolve(tagId);
        if (tag == null) {
            tag = tagManager.resolveByTitle(tagId);
        }
        if ( tag != null ) {
            %>CQ_Analytics.OperatorActions.operate(<%
                %><%=xssAPI.getValidJSToken(jsObject, "null")%>, <%
                %>'<%=xssAPI.encodeForJSString(tag.getTagID())%>', <%
                %>'<%=xssAPI.encodeForJSString(operator)%>', <%
                %>'<%=xssAPI.encodeForJSString(value)%>', <%
                %>'<%=xssAPI.encodeForJSString(valueFormat)%>')<%
        } else {
            %>false<%
        }
    }
%>
