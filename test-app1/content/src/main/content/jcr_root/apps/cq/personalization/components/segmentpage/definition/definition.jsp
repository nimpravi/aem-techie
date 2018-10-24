<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Segment definition component

--%>
<%@page contentType="text/html" pageEncoding="utf-8" import="java.io.IOException"%>
<%@include file="/libs/foundation/global.jsp" %>
<!-- table class="propsheet" --><%
//    int row = 1;
//    row += printItem(out, row, "Name", currentPage.getName());
//    row += printItem(out, row, properties, "boost", "Boost");
%><!--/table-->

<%!
    private int printItem(JspWriter out, int row, String title, String value)
            throws IOException {
        out.print("<tr class=\"");
        out.print(row % 2 == 0 ? "even" : "odd");
        out.print("\"><td>");
        out.print(title);
        out.print("</td><td>");
        out.print(value);
        out.println("</td></tr>");
        return 1;
    }

    private int printItem(JspWriter out, int row, ValueMap props, String name, String title)
            throws IOException {
        String value = props.get(name, "");
        return printItem(out, row, title, value);
    }
%>
