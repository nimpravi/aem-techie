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
%><%@ page import="
    java.util.ResourceBundle,
    com.day.cq.commons.jcr.JcrUtil,
    com.day.cq.wcm.api.WCMMode,
    com.day.cq.i18n.I18n" %><%

    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);

    NodeIterator ni = currentNode.getParent().getNodes();
    long count = ni.getSize();
    int num = 0;
    while(ni.hasNext()) {
        num++;
        Node child = ni.nextNode();
        if(child.getName().equals(currentNode.getName())) {
            break;
        }
    }
    if(count > num) {
        String text = null;
        String parentName = currentNode.getParent().getName();
        if( parentName.equals("andpar")) {
            text = i18n.get("AND", "Logical AND in ClientContext segment editor");
        } else {
            if( parentName.equals("orpar")) {
                text = i18n.get("OR", "Logical OR in ClientContext segment editor");
            }
        }

        if( text != null) {
            %><div class="segmenteditor-logic-text"><%=text%></div><%
        }
    }
%>
