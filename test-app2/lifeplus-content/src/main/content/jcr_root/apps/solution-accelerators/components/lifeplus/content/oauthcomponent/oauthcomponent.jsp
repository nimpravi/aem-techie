<%@include file="/libs/foundation/global.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.day.cq.i18n.I18n,
                 com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.commons.WCMUtils,
                 com.day.text.Text,
                 com.day.cq.wcm.foundation.forms.FormsHelper" %>
<%
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
<!DOCTYPE html>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
    // Global variable for storing access informations
var accessInfo = {
	siteName : "No site",
	token : 0,
	additionalInfo : ""

};

$(document).ready(function() {
	$(".logButtons").click(function(event) {
		event.preventDefault();
	});
});

function initLogin(site) {
	accessInfo.siteName = site;
	$("#loginButtons").hide();
	$("#signoutButton").show();
	if (accessInfo.siteName != undefined) {
		if (accessInfo.siteName == 'FB') {
			accessInfo.token = initFBLogin();
		} else if (accessInfo.siteName == 'Google') {
			accessInfo.token = initGoogleLogin();
		} else if (accessInfo.siteName == 'Life') {
			accessInfo.token = initLifeLogin();
		}
	}
}

function initLogout() {
	alert("Coming from auth.js sitename -> " + accessInfo.siteName);
	$("#signoutButton").hide();
	$("#loginButtons").show();

	if (accessInfo.siteName != undefined) {
		if (accessInfo.siteName == 'FB') {
			accessInfo.token = initFBLogout();
		} else if (accessInfo.siteName == 'Google') {
			accessInfo.token = initGoogleLogout();
		} else if (accessInfo.siteName == 'Life') {
			accessInfo.token = initLifeLogout();
		}
	}
}

function initFBLogin() {
	alert("Initiating FB Login  -> " + accessInfo.siteName);
	/*$
			.ajax(
					{
						url : 'https://www.facebook.com/dialog/oauth?client_id=512497928871541&redirect_uri=//localhost:8000/OAuthApplication/pages/Authentication1.jsp',
						dataType : 'text'
					}).done(function(data) {
				alert(data);
			});*/
	$.ajax({
		url:"/LoginController"
	}).done(function(data){
		alert("data from servlet -> "+ data);
	});
	/*
	 * FB.init({ appId : '512497928871541', cookie : true, // enable cookies to
	 * allow the server to access // the session xfbml : true, // parse social
	 * plugins on this page version : 'v2.1' // use version 2.1 });
	 */
}

function initGoogleLogin() {
	alert("Initiating Google Login -> " + accessInfo.siteName);

}

function initLifeLogin() {
	alert("Initiating Lif+ Login -> " + accessInfo.siteName);

}
	/* window.fbAsyncInit = function() {
		//alert("Loaded");
		FB.init({
			appId : '512497928871541',
			xfbml : true,
			version : 'v2.2',
			oauth : true
		});
	};

	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) {
			return;
		}
		js = d.createElement(s);
		js.id = id;
		js.src = "//connect.facebook.net/en_US/sdk.js";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk')); */
	//Load the Facebook JS SDK
	(function(d){
	   var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	   if (d.getElementById(id)) {return;}
	   js = d.createElement('script'); js.id = id; js.async = true;
	   js.src = "//connect.facebook.net/en_US/all.js";
	   ref.parentNode.insertBefore(js, ref);
	 }(document));

	// Init the SDK upon load
	window.fbAsyncInit = function() {
	  FB.init({
	    appId      : '512497928871541', // App ID
	    status     : true, // check login status
	    cookie     : true, // enable cookies to allow the server to access the session
	    xfbml      : true  // parse XFBML
	  });


	// Specify the extended permissions needed to view user data
	// The user will be asked to grant these permissions to the app (so only pick those that are needed)
	        var permissions = [
	          'email',
	          'user_about_me',
	          'user_birthday',
	          'user_location',
	          'user_website',
	          'friends_website'
	          ].join(',');

	// Specify the user fields to query the OpenGraph for.
	// Some values are dependent on the user granting certain permissions
	        var fields = [
	          'id',
	          'email',
	          'name',
	          'first_name',
	          'middle_name',
	          'last_name',
	          'gender',
	          'location',
	          'locale',
	          'languages',
	          'username',
	          'birthday',
	          'website',
	          'picture'
	          ].join(',');

	  function showDetails() {
	    FB.api('/me', {fields: fields}, function(details) {
	      // output the response
	      //var fbData=JSON.parse(details);
        
	      alert("Email: "+details.email);
	      alert("location: "+details.location.name);
	      alert("picture: "+details.picture.data.url);
	      
	    var url = "/bin/dbconnection";

	    $.ajax(url, {
            type: 'POST',
            dataType: "text",
            data : 'email='+ details.email+'&address='+ details.location.name+'&first_name='+ details.first_name+'&last_name='+ details.first_name+'&gender='+ details.gender+'&location='+ details.location.name+'&picture=null&password=password@123$&profileType=facebook&requestType=fbgpluslogin',
            success: function(rawData, status, xhr) {
                var serverResponse;
                try {
                    serverResponse = $.parseJSON(rawData);
                    
                    if(serverResponse.logintype=="facebook"){
                    	//alert("FB***");
                    	alert(validateUser(serverResponse.gplususerloginname));
                    } 

                } catch(err) {
                	alert(err);
                    failure(err);
                }
            },
            error: function(xhr, status, err) {
                failure(err);
            } 
        }); 
       

        
	      //$('#userdata').html(JSON.stringify(details, null, '\t'));
	      $('#fb-login').attr('style', 'display:none;');
	    });
	  }


	  $('#fb-login').click(function(){
	    //initiate OAuth Login
	    FB.login(function(response) { 
	      // if login was successful, execute the following code
	      if(response.authResponse) {
	          showDetails();
	      }
	    }, {scope: permissions});
	  });

	};
	
	
	 function validateUser(u) {
	        if (CQ_Analytics) {
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
	                     alert("isDisabled***");
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
	            	alert("ProfileDataMgr***");
	                CQ_Analytics.ProfileDataMgr.clear();
	            }
	            return true;
	        <% } %>
	        }
	    }
</script>
<button id="fb-login">Login to Facebook</button>
<pre id="userdata">

	<div id="loginButtons">
		<div class="fb-login-button" data-max-rows="1" data-size="medium"
			data-show-faces="false" data-auto-logout-link="false"></div>

		<div>
			<a class="logButtons" href="" onclick="initLogin('Google')"><span
				id="glogin">Login to Google Plus</span></a>
		</div>
		<div id="lifelogin">
			<a class="logButtons" href="" onclick="initLogin('Life')"><span
				id="lifelogin">Login to Life Plus</span></a>
		</div>
	</div>
	<div id="signoutButton" style="display: none">
		<div>
			<a class="logButtons" href="" onclick="initLogout()"><span
				id="logout">Logout</span></a>
		</div>
	</div>

	<div class="fb-like" data-share="true" data-width="450"
		data-show-faces="true"></div>
