<%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Default parsys/new component

  This component does not provide any output and is used only for the "new"
  edit bar. It respects the "currentResource" edit context attribute that allows
  correct inserting components in the paragraph system.

--%><%@ page session="false"%>
<%@ page import="com.day.cq.i18n.I18n" %>
<%@ page import="com.day.cq.wcm.api.components.Toolbar" %>
<%@ page import="java.util.ResourceBundle" %>
<%@include file="/libs/foundation/global.jsp" %>

<%
    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);

    editContext.getEditConfig().getToolbar().clear();
    editContext.getEditConfig().getToolbar().add(
            new Toolbar.Label(i18n.get("Target End"))
    );
    editContext.getEditConfig().setOrderable(false);
%>