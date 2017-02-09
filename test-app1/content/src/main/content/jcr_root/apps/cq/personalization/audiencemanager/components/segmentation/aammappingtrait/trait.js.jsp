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
    String aam_sid = properties.get("aam_sid", "");
    if(!"".equals(aam_sid) ) {
        %>CQ_Analytics.AAM.AudienceManager.matches('<%=xssAPI.encodeForJSString(aam_sid)%>')<%
    }
%>
