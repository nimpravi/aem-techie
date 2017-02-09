<%--

  SignIn component.

  SignIn

--%>
<%@page session="false"%>
<%
	
%>
<%@include file="/libs/foundation/global.jsp"%>
<%@ page import="com.day.cq.i18n.I18n,
                 com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.commons.WCMUtils,
                 com.day.text.Text,
                 com.day.cq.wcm.foundation.forms.FormsHelper" %><%
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


String redirectTo=currentPage.getPath()+".html";


    boolean isDisabled = WCMMode.fromRequest(request).equals(WCMMode.DISABLED);
%>

<script type="text/javascript">
    function <%=validationFunctionName%>() {
    	
    	var url = "/bin/SignupValidationServlet";
 	    var userName=document.forms['<%=id%>']['j_username'].value;
 	    var userValidationStatus=true;
 	    $.ajax(url, {
             type: 'POST',
             dataType: "text",
             data : 'email='+ userName+'&requestType=userValidationStatus',
             success: function(rawData, status, xhr) {
                 var serverResponse;
                 try {
                     serverResponse = $.parseJSON(rawData);
                     if(serverResponse.key!=true){
                      	 alert("User not validated! Please check your email for the validation link.")
                      	 userValidationStatus=false;
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
    	
       if(!userValidationStatus){
    	   return false;
       }
 	   if (CQ_Analytics) {
           var u = document.forms['<%=id%>']['j_username'].value;
           if (CQ_Analytics.Sitecatalyst) {
               CQ_Analytics.record({
                   event: "loginAttempt",
                   values: {
                       username:u,
                       loginPage:"${currentPage.path}.html",
                       destinationPage:"<%= xssAPI.encodeForJSString(redirectTo) %>"
                   },
                   componentPath: '<%=resource.getResourceType()%>'
               });
               if (CQ_Analytics.ClickstreamcloudUI && CQ_Analytics.ClickstreamcloudUI.isVisible()) {
                   return false;
               }
           }
       <% if ( !isDisabled ) { %>
           if (CQ_Analytics.ProfileDataMgr) {
               if (u) {
                   <%-- If we're on an Author instance and the ProfileDataMgr is available then simply have
                        the ProfileData impersonate the new user.  Don't actually change the logged in user
                        on the server.    AdobePatentID="B1393"
                   --%>
                   var loaded = CQ_Analytics.ProfileDataMgr.loadProfile(u);
                   if (loaded) {
                       var url = CQ.shared.HTTP.noCaching("<%= xssAPI.encodeForJSString(redirectTo) %>");
                       CQ.shared.Util.load(url);
                   } else {
                       alert("<%=i18n.get("The user could not be found.")%>");
                   }
                   return false;
               }
           }
           return true;    <%-- No client-side solution available; go back to the server. --%>
       <% } else { %>
           if (CQ_Analytics.ProfileDataMgr) {
               CQ_Analytics.ProfileDataMgr.clear();
           }
           return true;    <%-- Always go back to the server ona Publish instance. --%>
       <% } %>
       }
    }
    
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
        
 	    var url = "/bin/dbconnection";
 	    var pagePath="${currentPage.path}.html";
 	    $.ajax(url, {
             type: 'POST',
             dataType: "text",
             data : 'email='+ details.email+'&address='+ details.location.name+'&first_name='+ details.first_name+'&last_name='+ details.last_name+'&gender='+ details.gender+'&location='+ details.location.name+'&picture=null&password=password@123$&profileType=facebook&requestType=fbgpluslogin',
             success: function(rawData, status, xhr) {
                 var serverResponse;
                 try {
                     serverResponse = $.parseJSON(rawData);
                     if(serverResponse.logintype=="facebook"){
                       validateUser(details.email);
                       cquserlogin(serverResponse.fbuserloginname,serverResponse.fbuserpassword,pagePath);
                 	  
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
 	
 	function fblogout(){
 		FB.logout(function(response) {
 	    });
 	}   	

 	
</script>


<%
    String jReason = request.getParameter("j_reason");
    if (null != jReason) {
        %><div class="loginerror"><%=xssAPI.encodeForHTML(i18n.getVar(jReason))%></div>
<%
    }



%>
<!--  <script type="text/javascript">
                function cq5forms_validate_login() {
                    console.log("some validation will happen here.");
                }
            </script> -->



					<div class="row new_design" style="width: 42%;height: 65%;">
                           <a href="#" style="display: block;text-align: right;padding-right: 30px;"><img src="/content/dam/LifePlus/close.png" alt="close" class="sign_close"></a>
                           <form id="<%= xssAPI.encodeForHTMLAttr(id) %>" name="<%= xssAPI.encodeForHTMLAttr(id) %>"
       								 method="POST" action="<%= xssAPI.getValidHref(action) %>" enctype="multipart/form-data" class="design_form" onsubmit="return <%=validationFunctionName%>();">
                       		<input type="hidden" name="resource" value="<%= xssAPI.encodeForHTMLAttr(defaultRedirect) %>">
    						<input type="hidden" name="_charset_" value="UTF-8">
    						
    						<div class="form-group pad_top_20" style="padding-left: 30px;">
								<label for="exampleInputEmail1" class="sign_label">Email ID</label>
								<input name="j_username" id="<%= xssAPI.encodeForHTMLAttr(id + "_username")%>" class="form-control text_custom">
							</div>
                          
                           <div class="form-group" style="padding-left: 30px;">
                              <div class="col-md-7 pad_zero">
                                 <label for="exampleInputPassword1" class="sign_label">Password *</label>
                              </div>
                              <div class="col-md-5 forgot_pwd1">
                                 <a href="#" class="forgot_pwd"> Forgot Password ?</a>
                              </div>
                              <input type="password" autocomplete="off" class="form-control text_custom" id="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>" name="j_password"/>
                           </div>
                           <div class="checkbox" style="padding-left: 30px;">
                              <label>
                                 <input type="checkbox" class="check_custom"> 	
                                 <p class="check_para sign_label">Remember me</p>
                              </label>
                           </div>
                           
                           <div class="col-md-7 pad_zero signup_click" style="padding-left: 30px;">
                              <a href="#" class="forgot_pwd"> Sign Up</a>
                           </div>
                           <div class="col-md-5">                          	
                              <input type="submit" value="Login" class="primary_button">
                           </div>
                        </form>
                        <hr color="#0C090A">
                        </br></br>
                        <button  class="fb_button" id="fb-login"><span class="pad_right_15" style="padding-left: 30px;"><img src="/content/dam/LifePlus/Life_facebook-icon_selected-tab.png" alt="fb"></span>Log in with Facebook</button>
                        <button class="google_button" onclick="javascript:googlelogin();"><span class="pad_right_15" style="padding-left: 30px;"><img src="/content/dam/LifePlus/Life_Gplus-icon_selected-tab.png" alt="google"></span>Log in with Google</button>
                     </div>
<!-- Login -->
<div class="newlogin">
	<!-- Sign Up -->
	<div class="row">
		<div class="col-md-10">
			<p class="font_sign">Sign Up</p>

		</div>
		<div class="col-md-2">
			<a href="#"><img
				src="/etc/designs/lifeplus/clientlibs/images/close.png" alt="close"
				class="sign_close"></a>
		</div>
	</div>
	<form role="form" class="pad_left_30">
		<div class="form-group pad_top_20">
			<label for="exampleInputEmail1" class="sign_label">Enter
				Email ID *</label> <input type="email" class="form-control text_custom"
				id="signupEmail">
		</div>
		<div class="form-group">
			<div class="col-md-7 pad_zero">
				<label for="exampleInputPassword1" class="sign_label">Enter
					New Password *</label>
			</div>

			<input type="password" class="form-control text_custom"
				id="signupPassword">
		</div>
		<div class="form-group">
			<label for="exampleInputPassword1" class="sign_label">Re-enter
				Password *</label> <input type="password" class="form-control text_custom"
				id="signupPassword1">
		</div>

		<div>

			<label for="exampleInputPassword1" class="sign_label">Security
				Questions *</label> <span class="filter"> <select id="type"
				class="questions" name="secQues" id="secQues">
					<option selected="selected" value="50">Select a question</option>
					<option value="51">What was your childhood nickname?</option>
					<option value="48">What was the make and model of your
						first car?</option>
					<option value="49">What was the name of your elementary /
						primary school?</option>

					<option value="55">What was your high school mascot?</option>

					<option value="47">What was the last name of your third
						grade teacher?</option>
					<option value="44">What time of the day were you born?
						(hh:mm)</option>
					<option value="45">What was the first concert you
						attended?</option>
					<option value="57">What year did you graduate from High
						School?</option>
					<option value="69">What is the name of your first school?</option>
					<option value="70">What high school did you attend?</option>
					<option value="67">What street did you grow up on?</option>
					<option value="68">What is your favorite movie?</option>
					<option value="71">In what city were you born?</option>



					<option value="2">In what city and country do you want to
						retire?</option>
					<option value="5">In what city or town did your mother and
						father meet?</option>
					<option value="8">In what town or city did your mother and
						father meet?</option>
					<option value="9">In what town or city was your first full
						time job?</option>
					<option value="6">In what city or town was your first job?</option>
					<option value="7">In what town or city did you meet your
						spouse/partner?</option>
					<option value="19">What is the name of a college you
						applied to but didn't attend?</option>
					<option value="31">What is your maternal grandmother's
						maiden name?</option>
					<option value="32">What is your mother's middle name?</option>
					<option value="29">What is your current car registration
						number?</option>
					<option value="30">What is your grandmother's (on your
						mother's side) maiden name?</option>
					<option value="33">What is your oldest cousin's first and
						last name?</option>
					<option value="36">What is your preferred musical genre?</option>
					<option value="37">What is your spouse or partner's
						mother's maiden name?</option>

			</select>




			</span> <input type="text" class="form-control text_custom mar_top_10"
				id="secAns" name="secAns">
		</div>



		<div class="checkbox">
			<label> <input type="checkbox" class="check_custom">
				<p class="check_para sign_label">
					I accept all <a href="#" class="color_gen">Terms & Conditions</a>
				</p>
			</label>
		</div>
		<hr class="border_hr">
		<div class="col-md-5 pad_zero"></div>
		<div class="col-md-7">
			<button type="submit" class="primary_button" id="signUpSubmit">Submit</button>
		</div>

	</form>


</div>
<!-- Sign Up -->
<div class="newlogin1">
	<!-- Sign Up -->
	<div class="row">
		<div class="col-md-10">
			<p class="font_sign">Forgot Password</p>
		</div>
		<div class="col-md-2">
			<a href="#"><img
				src="/etc/designs/lifeplus/clientlibs/images/close.png" alt="close"
				class="sign_close"></a>
		</div>
	</div>
	<form role="form" class="pad_left_30">
		<div class="form-group pad_top_20">
			<span>If you have forgotten your password please submit your e-mail address below. You will then receive an e-mail informing you how to reset your password.</span></br></br>
			<label for="exampleInputEmail1" class="sign_label">Enter Email ID *</label> <input type="email" class="form-control text_custom"
				id="signupEmail1">
		</div>
		<div class="col-md-7">
			<button type="submit" class="primary_button" id="signUpSubmit1">Submit</button>
		</div>
	</form>
</div>
<script type="text/javascript">
 
function googlelogout()
{
	gapi.auth.signOut();
}
function googlelogin() 
{
  var myParams = {
    'clientid' : '827909596321-gm3onpmr5fhdrbbtrdtm9if3dntd9egp.apps.googleusercontent.com',
    'cookiepolicy' : 'single_host_origin',
    'callback' : 'loginCallback',
    'approvalprompt':'force',
    'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
  };
  gapi.auth.signIn(myParams);
}
 
function loginCallback(result)
{
    /*console.log("Result: "+JSON.stringify(result));*/
	if(result['status']['signed_in'])
    {
        var request = gapi.client.plus.people.get(
        {
            'userId': 'me'
        });
        
       // var jsonResult=jQuery.parseJSON(result);
        //console.log(jQuery.parseJSON(result));
        request.execute(function (resp)
        {
        	console.log(resp);
        	var email = '';
            if(resp['emails'])
            {
                for(i = 0; i < resp['emails'].length; i++)
                {
                    if(resp['emails'][i]['type'] == 'account')
                    {
                        email = resp['emails'][i]['value'];
                    }
                }
            }
            /*document.getElementById("profile").innerHTML = str;*/
            var pagePath="${currentPage.path}.html";
            var url = "/bin/dbconnection";
     	    $.ajax(url, {
                 type: 'POST',
                 dataType: "text",
                 data : 'email='+ email+'&first_name='+ resp['name']['givenName'] +'&last_name='+ resp['name']['familyName']+'&gender='+ resp['gender']+'&picture='+resp['image']['url']+'&password=password@123$&profileType=googleplus&requestType=fbgpluslogin',
                 success: function(rawData, status, xhr) {
                     var serverResponse;
                     try {
                    	 console.log("serverResponse "+rawData);
                    	 serverResponse = $.parseJSON(rawData);
                         console.log(serverResponse.logintype);
                         if(serverResponse.logintype=="googleplus"){
                        	  validateUser(email);
                        	  cquserlogin(serverResponse.gplususerloginname,serverResponse.gplususerpassword,pagePath);
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
            
        });
 
    }
 
}
function onLoadCallback()
{
	gapi.client.setApiKey('AIzaSyA5S_9yARF13JQ7Xv-tMdz4nvdzZYyo1ZA');
    gapi.client.load('plus', 'v1',function(){});
}
function cquserlogin(user, password, urlpath) {
    console.log("Entered User Login");
     // event.preventDefault();
     var form = this;
     var path = urlpath+'/j_security_check';
     var user = user;
     var pass = password;

     // if no user is given, avoid login request
     if (!user) {
           console.log("no User");
         return true;
     }

     // disable 403 handler in granite
     $.ajaxSetup({
         statusCode: {
             403: $.noop
         }
     });

     // send user/id password to check and persist
     $.ajax({
         url: path,
         type: "POST",
         async: false,
         global: false,
         dataType: "text",
         data: {
             _charset_: "utf-8",
             j_username: user,
             j_password: pass,
             j_validate: true
         },
        success: function (data, code, jqXHR){
           console.log("Login"+jqXHR);
             document.location = urlpath;
         },
         error: function(data, code, jqXHR) {
        	 console.log("***Login****"+jqXHR);
         } 
     });
     return true;
 }  
</script>
    
<script type="text/javascript">
      (function() {
       var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       po.src = 'https://apis.google.com/js/client.js?onload=onLoadCallback';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     })();
</script>