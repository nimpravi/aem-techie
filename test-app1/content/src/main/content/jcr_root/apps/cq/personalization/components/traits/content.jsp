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
%><%@page import="
       com.day.cq.wcm.api.WCMMode"%><%
%><%
    String name = properties.get("name", "");
    String label = properties.get("label", name);
    String operator = properties.get("operator", "");
    String value = properties.get("value", "");
    if(!"".equals(name)) {
        %><b><%=xssAPI.encodeForHTML(label)%></b>&nbsp;<%
        %><div class="segmenteditor-operator" id="<%=xssAPI.encodeForHTMLAttr(currentNode.getPath())%>"></div>&nbsp;<%
        %><b><%=xssAPI.encodeForHTML(value)%></b>
        <script type="text/javascript">document.getElementById("<%=xssAPI.encodeForJSString(currentNode.getPath())%>").innerHTML <%
            %>= CQ_Analytics.OperatorActions.getText('<%=xssAPI.encodeForJSString(operator)%>');</script>
<%
    } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
        %><img src="/libs/cq/ui/resources/0.gif" class="cq-teaser-placeholder" alt=""><%
    }
%>
