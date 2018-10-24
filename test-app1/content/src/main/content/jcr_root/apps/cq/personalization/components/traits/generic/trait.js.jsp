<%@page session="false"%><%--
  ~
  ~ ADOBE CONFIDENTIAL
  ~ __________________
  ~
  ~  Copyright 2011 Adobe Systems Incorporated
  ~  All Rights Reserved.
  ~
  ~ NOTICE:  All information contained herein is, and remains
  ~ the property of Adobe Systems Incorporated and its suppliers,
  ~ if any.  The intellectual and technical concepts contained
  ~ herein are proprietary to Adobe Systems Incorporated and its
  ~ suppliers and are protected by trade secret or copyright law.
  ~ Dissemination of this information or reproduction of this material
  ~ is strictly forbidden unless prior written permission is obtained
  ~ from Adobe Systems Incorporated.
  --%><%@include file="/libs/foundation/global.jsp"%><%
    String jsObject = "clientcontext." + properties.get("jsObject", "null");
    String name = properties.get("name", "");
    String operator = properties.get("operator", "");
    String value = properties.get("value", "");
    String valueFormat = properties.get("valueFormat", "");
    if(!"".equals(name) && !"".equals(operator)) {
        %>CQ_Analytics.OperatorActions.operate(<%
            %><%=xssAPI.getValidJSToken(jsObject, "null")%>, <%
            %>'<%=xssAPI.encodeForJSString(name)%>', <%
            %>'<%=xssAPI.encodeForJSString(operator)%>', <%
            %>'<%=xssAPI.encodeForJSString(value)%>', <%
            %>'<%=xssAPI.encodeForJSString(valueFormat)%>')<%
    }
%>
