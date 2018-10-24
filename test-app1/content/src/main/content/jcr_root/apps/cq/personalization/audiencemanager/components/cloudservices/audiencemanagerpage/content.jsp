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

  This component is used when editing the configuration for an Audience Manager Connection eg
  /etc/cloudservices/audiencemanager/geometrixx

--%><%@page contentType="text/html"
            pageEncoding="utf-8"
%><%@include file="/libs/foundation/global.jsp"
%><%@include file="/libs/wcm/global.jsp"
%><%@include file="/libs/cq/cloudserviceconfigs/components/configpage/init.jsp"
%>
<cq:setContentBundle/>
<div>
    <h3><fmt:message key="AudienceManager Settings"/></h3>
     <img src="<%= xssAPI.encodeForHTML(thumbnailPath)%>" alt="<%= xssAPI.encodeForHTML(serviceName)%>" style="float: left;" />
     <ul style="float: left; margin: 0px;">
       <li><div class="li-bullet"><strong><fmt:message key="Partner name"/>: </strong><%= xssAPI.encodeForHTML(properties.get("partner", "")) %></div></li>
       <li><div class="li-bullet"><strong><fmt:message key="Container Namespace ID"/>: </strong><%= xssAPI.encodeForHTML(properties.get("container", "")) %></div></li>
       <li><div class="li-bullet"><strong><fmt:message key="Access Token Expires"/>: </strong><%= xssAPI.encodeForHTML(properties.get("oauthExpiresDate", "")) %></div></li>
       <li><div class="li-bullet"><strong><fmt:message key="Automatic Refresh"/>: </strong><% if ( properties.containsKey("oauthRefreshToken") ) { %><fmt:message key="Yes"/><% } else { %><fmt:message key="No"/><% } %> </div></li>
       <li class="config-successful-message when-config-successful" style="display: none">
       <fmt:message key="Adobe AudienceManager configuration is successful."/><br>
       <fmt:message key="You can now use Audience Manager’s rich data set and modeling capabilities within CQ."/>
       </li>
    </ul>
</div>
