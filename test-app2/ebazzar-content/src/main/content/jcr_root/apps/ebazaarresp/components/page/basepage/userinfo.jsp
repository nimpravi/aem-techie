
<%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Shows information about the currently logged in user.

--%><%@include file="/libs/foundation/global.jsp"%>

<cq:setContentBundle/>
<%
%><%@ page import="com.day.cq.i18n.I18n,
                   com.day.cq.personalization.ProfileUtil,
                   com.day.cq.security.profile.Profile,
                   com.day.cq.wcm.api.WCMMode,
                   com.adobe.cq.commerce.api.CommerceConstants,
                  com.adobe.cq.commerce.api.CommerceService,
                  com.adobe.cq.commerce.api.CommerceSession" %><%
%><%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %><%

    final I18n i18n = new I18n(slingRequest);
    final Profile currentProfile = slingRequest.adaptTo(Profile.class);
    final boolean isAnonymous = ProfileUtil.isAnonymous(currentProfile);
//final boolean isDisabled = WCMMode.DISABLED.equals(WCMMode.fromRequest(request));
 boolean isDisabled = WCMMode.fromRequest(request).equals(WCMMode.DISABLED);
//final String logoutPath = request.getContextPath() + "/system/sling/logout.html";
// final String profilePagePath = currentStyle.get("profilePage", String.class);
//  final String myProfileLink = "${profile.path}.form.html" + profilePagePath;
    //final String loginPagePath = currentStyle.get("loginPage", String.class);
    //final String signupPagePath = currentStyle.get("signupPage", String.class);
    String loginPagePath=currentPage.getPath();
    String signupPagePath="";
    String applicationName = "";
     String language= "";
       if(request.getRequestURI() != null){
        String requestUri[] = request.getRequestURI().split("/");
        applicationName = requestUri[2].toString();
           language= requestUri[3].toString();
               }


    CommerceService commerceService = resource.adaptTo(CommerceService.class);
    CommerceSession commerceSession = commerceService.login(slingRequest, slingResponse);

if(applicationName.equalsIgnoreCase("ebazzar"))
{
     loginPagePath="/content/ebazaar/en";
    signupPagePath="/content/ebazaar/en";
}

%>

<script type="text/javascript">function logout() {


        <%      if( !isDisabled ) { %>
            if( CQ_Analytics && CQ_Analytics.CCM) {
                CQ_Analytics.ProfileDataMgr.loadProfile("anonymous");
                CQ.shared.Util.reload();
            }
        <%      } else { %>
            if( CQ_Analytics && CQ_Analytics.CCM) {
                CQ_Analytics.ProfileDataMgr.clear();
                CQ_Analytics.CCM.reset();
            }
    // CQ.shared.HTTP.clearCookie("<%= CommerceConstants.COMMERCE_COOKIE_NAME %>", "/");
            CQ.shared.Util.load("<%= resourceResolver.map(request, "/system/sling/logout") %>.html");
        <%      } %>
        }</script>
<li class="dropdown">





<% if (!isAnonymous || !isDisabled) { %>
   <a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-user"></i> 

   <personalization:contextProfileProperty propertyName="formattedName" prefix="(" suffix=")"/></a>

<% }
     if (!isAnonymous) { %>
            <ul class="dropdown-menu">

							<li><a href="/content/ebazzar/en/cart.html"><i class="icon-shopping-cart"></i> <fmt:message key="Cart"/></a></li>
							<li><a href="#"><i class="icon-list"></i> Wishlist</a></li>
							<li><a href="/content/ebazzar/en/checkout.html"><i class="icon-globe"></i> Track My Orders</a></li>
							<li class="divider"></li>
							<li>
                                <a href="javascript:logout();"><i class="icon-off"></i> <fmt:message key="Sign Out"/></a></li>
				</ul>
    <% } else { %>

       <ul class="dropdown-menu">
							<li><a href="#myModal" data-toggle="modal"><i class="icon-pencil"></i> <fmt:message key="Sign In"/></a></li>
							<li><a href="/content/ebazzar/en/user/register.html"><i class="icon-plus-sign"></i> <fmt:message key="Sign Up"/></a></li>
							<li><a href="/content/ebazzar/en/cart.html"><i class="icon-shopping-cart"></i> <fmt:message key="Cart"/></a></li>
							<li><a href="#"><i class="icon-list"></i> Wishlist</a></li>

				</ul>
    <% }%>
	
  </li>
	
	
