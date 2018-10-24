<%--

  userloggedIn component.

  userloggedIn

--%><%

%><%@include file="/libs/foundation/global.jsp"%>
<%@ page import="com.day.cq.i18n.I18n,
                   com.day.cq.personalization.ProfileUtil,
                   com.day.cq.security.profile.Profile,
                   com.day.cq.wcm.api.WCMMode,
                   com.adobe.cq.commerce.api.CommerceConstants,
                  com.adobe.cq.commerce.api.CommerceService,
                  com.adobe.cq.commerce.api.CommerceSession,org.apache.jackrabbit.api.security.user.UserManager,org.apache.jackrabbit.api.security.user.Authorizable,javax.jcr.Value" %>
<%@page session="false" %>

<%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %><%

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
    final String logoutPath = request.getContextPath() + "/system/sling/logout.html";
%>
<script type="text/javascript">function logout1() {
    if (_g && _g.shared && _g.shared.ClientSidePersistence) {
        _g.shared.ClientSidePersistence.clearAllMaps();
    }

<% if( !isDisabled ) { %>
    if (CQ_Analytics && CQ_Analytics.CCM) {
        CQ_Analytics.ProfileDataMgr.loadProfile("anonymous");
        CQ.shared.Util.reload();
    }
<% } else { %>
    if (CQ_Analytics && CQ_Analytics.CCM) {
        CQ_Analytics.ProfileDataMgr.clear();
        CQ_Analytics.CCM.reset();
    }
    CQ.shared.HTTP.clearCookie("<%= CommerceConstants.COMMERCE_COOKIE_NAME %>", "/");
    CQ.shared.Util.load("<%= xssAPI.encodeForJSString(logoutPath) %>");
<% } %>
}</script>

<%
    	String user = slingRequest.getResourceResolver().adaptTo(Session.class).getUserID();

		/* UserManager userManager = slingRequest.getResourceResolver().adaptTo(UserManager.class);
		Authorizable auth = userManager.getAuthorizable(user);
		Value[] givenNameArray = auth.getProperty("./profile/givenName");
		Value[] familyNameArray = auth.getProperty("./profile/familyName");
		String givenName="";
		String familyName="";
		if(null!=givenNameArray){
			for (Value value : givenNameArray) {
				givenName = value.getString();
			}
		}
		if(null!=familyNameArray){
			for (Value value : familyNameArray) {
				familyName = value.getString();
			}
		}
		pageContext.setAttribute("givenName",givenName);
		pageContext.setAttribute("familyName",familyName);
		pageContext.setAttribute("user",user); */
	if(!isAnonymous)
    {
%> 	 <a href="#">
                        <li class="pad_right_5">Welcome&nbsp;
                        	  <personalization:contextProfileProperty propertyName="formattedName" prefix="(" suffix=")"/>
                        </li>
                     </a>
                     <li>
                      	<a href="javascript:logout1();"><i class="icon-off"></i>Sign Out</a>
                     </li>
					<li>
                        <img src="/content/dam/LifePlus/Life_Homepage_search-Icon.png" alt="search" class="pad_bot_10">
                         <input type="text" class="myBox" name="srch-term" id="srch-term">
                     </li><%
    }else{
%>
					<a href="#">
                        <li class="pad_right_5 sign_in">SIGN IN OR LOGIN WITH</li>
                     </a>
                     <a href="#">
                        <li class="pad_right_5 sign_in"><img src="/content/dam/LifePlus/Life_Homepage_facebook-icon.png" class="pad_bot_10" alt="Facebook"></li>
                     </a>
                     <a href="#">
                        <li><img src="/content/dam/LifePlus/Life_Homepage_Gplus-icon.png" class="pad_bot_10" alt="Google"></li>
                     </a>
 <li>
                        <img src="/content/dam/LifePlus/Life_Homepage_search-Icon.png" alt="search" class="pad_bot_10">
                         <input type="text" class="myBox" name="srch-term" id="srch-term">
                     </li>
<%}%>