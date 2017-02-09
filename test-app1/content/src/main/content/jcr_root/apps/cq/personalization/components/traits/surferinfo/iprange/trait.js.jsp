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
    String jsObject = properties.get("jsObject", "null");
    String name = properties.get("name", "");
    String valueFormat = properties.get("valueFormat", "");
    String startRange = properties.get("startRange", "");
    String endRange = properties.get("endRange", "");
    if(!"".equals(startRange) &&  !"".equals(endRange)) {
        %>CQ_Analytics.OperatorActions.operate(<%
            %><%=xssAPI.getValidJSToken(jsObject, "null")%>, <%
            %>'<%=xssAPI.encodeForJSString(name)%>', <%
            %>'greaterorequal', <%
            %>'<%=xssAPI.encodeForJSString(startRange)%>', <%
            %>'<%=xssAPI.encodeForJSString(valueFormat)%>') &&
          CQ_Analytics.OperatorActions.operate(<%
            %><%=xssAPI.getValidJSToken(jsObject, "null")%>, <%
            %>'<%=xssAPI.encodeForJSString(name)%>', <%
            %>'lessorequal', <%
            %>'<%=xssAPI.encodeForJSString(endRange)%>', <%
            %>'<%=xssAPI.encodeForJSString(valueFormat)%>')<%
    }
%>
