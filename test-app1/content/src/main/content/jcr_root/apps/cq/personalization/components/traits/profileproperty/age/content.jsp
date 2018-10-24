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
        %><div id="<%=xssAPI.encodeForHTMLAttr(currentNode.getPath())%>"></div>
        <script type="text/javascript"> {
            <%
                // !!!!!!!!!!!!!!!   NOTE WELL: careful of DOM-based XSS vulnerabilities here:   !!!!!!!!!!!!!!!
                //
                // (for guidelines, see https://www.owasp.org/index.php/DOM_based_XSS_Prevention_Cheat_Sheet)
            %>
            var label = "<%=xssAPI.encodeForJSString(xssAPI.encodeForHTML(label))%>";
            var op = "<%=xssAPI.encodeForJSString(operator)%>";
            var optext = CQ_Analytics.OperatorActions.getText(op);
            var value = "<%=xssAPI.encodeForJSString(xssAPI.encodeForHTML(value))%>";
            var html = label + "&nbsp;<div class=\"segmenteditor-operator\">"+optext+"</div>&nbsp;<b>" + value + "</b>";

            //really special case because of text for operators equals, olderorequal and youngerorequal
            if (op == "equals") {
                html = CQ.I18n.getMessage('{0}&nbsp;<div class="segmenteditor-operator">is<b>&nbsp;{1}&nbsp;</b>years old</div>', [label, value], "{0} is a placeholder for person's name and {1} for age, ex: The visitor is 22 years old");
            }

            if (op == "notequal") {
                html = CQ.I18n.getMessage('{0}&nbsp;<div class="segmenteditor-operator">is not<b>&nbsp;{1}&nbsp;</b>years old</div>', [label, value], "{0} is a placeholder for person's name and {1} for age, ex: The visitor is not 22 years old");
            }

            if (op == "olderorequal") {
                html = CQ.I18n.getMessage('{0}&nbsp;<div class="segmenteditor-operator">is<b>&nbsp;{1}</b>, or older</div>', [label, value], "{0} is a placeholder for person's name and {1} for age, ex: The visitor is 22, or older");
            }

            if (op == "youngerorequal") {
                html = CQ.I18n.getMessage('{0}&nbsp;<div class="segmenteditor-operator">is<b>&nbsp;{1}</b>, or younger</div>', [label, value], "{0} is a placeholder for person's name and {1} for age, ex: The visitor is 22, or younger");
            }
            document.getElementById("<%=xssAPI.encodeForJSString(currentNode.getPath())%>").innerHTML = html;
        } </script>
<%
    } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
        %><img src="/libs/cq/ui/resources/0.gif" class="cq-teaser-placeholder" alt=""><%
    }
%>
