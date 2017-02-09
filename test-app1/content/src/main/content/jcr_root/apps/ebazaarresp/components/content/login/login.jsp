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
  Login component
--%><%
%><%@ page import="com.day.cq.i18n.I18n,
                 com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.commons.WCMUtils,
                 com.day.text.Text,
                 com.day.cq.wcm.foundation.forms.FormsHelper" %><%
%><%@include file="/libs/foundation/global.jsp"%><%
    String id = Text.getName(resource.getPath());
    I18n i18n = new I18n(slingRequest);
    String action = currentPage.getPath() + "/j_security_check";
    final String validationFunctionName = "cq5forms_validate_" + id;
 String signupPagePath = WCMUtils.getInheritedProperty(currentPage, resourceResolver, "cq:signupPage");
    if (signupPagePath != null) signupPagePath = resourceResolver.map(signupPagePath);
    String defaultRedirect = currentPage.getPath();
    if (!defaultRedirect.endsWith(".html")) {
        defaultRedirect += ".html";
    }

 String usernameLabel = properties.get("./usernameLabel", String.class);
    if (usernameLabel == null) {
        usernameLabel = i18n.get("Username");
    } else {
        usernameLabel = i18n.getVar(usernameLabel);
    }
    String passwordLabel = properties.get("./passwordLabel", String.class);
    if (passwordLabel == null) {
        passwordLabel = i18n.get("Password");
    } else {
        passwordLabel = i18n.getVar(passwordLabel);
    }
//  String referer = request.getHeader("Referer");
//String defaultRedirect = currentPage.getPath();
// if( referer != null ) {
//     //TODO check if referer is inside app
//   defaultRedirect = referer;
//  }
    // managed URIs should respect sling mapping
    // String redirectTo = slingRequest.getResourceResolver().map(request, properties.get("./redirectTo",defaultRedirect));

String applicationName = "";
 String imagName="";
String language="";
         if(request.getRequestURI() != null){
            String requestUri[] = request.getRequestURI().split("/");
            applicationName = requestUri[2].toString();
             language= requestUri[3].toString();
         }


String redirectTo=currentPage.getPath();


    boolean isDisabled = WCMMode.fromRequest(request).equals(WCMMode.DISABLED);
%>
<script type="text/javascript">
    function <%=validationFunctionName%>() {
        if (CQ_Analytics) {
            var u = document.forms['<%=id%>']['j_username'].value;
            if (CQ_Analytics.Sitecatalyst) {
                CQ_Analytics.record({ event:"loginAttempt", values:{
                    username:u,
                    loginPage:"${currentPage.path}.html",
                    destinationPage:"<%= xssAPI.encodeForJSString(defaultRedirect) %>"
                }, componentPath:'<%=resource.getResourceType()%>'});
                if (CQ_Analytics.ClickstreamcloudUI && CQ_Analytics.ClickstreamcloudUI.isVisible()) {
                    return false;
                }
            }
        <% if ( !isDisabled ) {
           final String contextPath = slingRequest.getContextPath();
           final String authorRedirect = contextPath + defaultRedirect; %>
            if (CQ_Analytics.ProfileDataMgr) {
                if (u) {
                    /*
                     * AdobePatentID="B1393"
                     */
                    var loaded = CQ_Analytics.ProfileDataMgr.loadProfile(u);
                    if (loaded) {
                        var url = CQ.shared.HTTP.noCaching("<%= xssAPI.encodeForJSString(authorRedirect) %>");
                        CQ.shared.Util.load(url);
                    } else {
                        alert("<%=i18n.get("The user could not be found.")%>");
                    }
                    return false;
                }
            }
            return true;
        <% } else { %>
            if (CQ_Analytics.ProfileDataMgr) {
                CQ_Analytics.ProfileDataMgr.clear();
            }
            return true;
        <% } %>
        }
    }
</script>
<%
    String jReason = request.getParameter("j_reason");
    if (null != jReason) {
        %><div class="loginerror"><%=xssAPI.encodeForHTML(i18n.getVar(jReason))%></div>
<%
    }



%>

 <script type="text/javascript">
                function cq5forms_validate_login() {
                    console.log("some validation will happen here.");
                }
            </script>
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">Sign in</h3>
		</div>
		<div class="modal-body">
			<form method="POST"
      action="<%= xssAPI.getValidHref(action) %>"
      id="<%= xssAPI.encodeForHTMLAttr(id) %>"
      name="<%= xssAPI.encodeForHTMLAttr(id) %>"
      enctype="multipart/form-data"
      onsubmit="return <%=validationFunctionName%>();">
            <input type="hidden" name="resource" value="<%= xssAPI.encodeForHTMLAttr(defaultRedirect) %>">
    		<input type="hidden" name="_charset_" value="UTF-8"/>
			<label style="padding-right:18px;" for="<%= xssAPI.encodeForHTMLAttr(id + "_username")%>"><%= xssAPI.encodeForHTML(usernameLabel) %></label>
        <input id="<%= xssAPI.encodeForHTMLAttr(id + "_username")%>" type="text" name="j_username"/>
				 <label style="padding-right:20px;" for="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>"><%= xssAPI.encodeForHTML(passwordLabel) %></label>
        <input id="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>"  type="password" name="j_password"/>
				<br>
          <input type="submit" class="btn btn-primary" value="Sign in">
				<br><a href="/content/ebazzar/en/user/forgot.html">forgot password?</a>
			</form>
		</div>
