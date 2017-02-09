<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Default body script.

  Draws the HTML body with the page content.

  ==============================================================================

--%>
<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>
<%
	StringBuffer cls = new StringBuffer();
	for (String c : componentContext.getCssClassNames()) {
		cls.append(c).append(" ");
	}
%>
<body>
<div class="container-fluid" id="body_wrapper">
	<div class="container">

        <cq:include script="header.jsp" />

        <cq:include script="content.jsp" />

        <sling:include path="login" resourceType="/apps/solution-accelerators/components/lifeplus/content/SignIn"/>
        <!-- footer component will be included here -->


<div class="container bottom_border ">
     <div class="row pad_top_20  pad_left_15">
        <sling:include path="lifeplusfooter1"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
        <sling:include path="lifeplusfooter2"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
                 <sling:include path="lifeplusfooter3"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
                 <sling:include path="lifeplusfooter4"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
         <sling:include path="lifeplusfooter5"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
         <sling:include path="lifeplusfooter6"
			resourceType="/apps/solution-accelerators/components/global/content/footer" addSelectors="lifeplusfooter"/>
    </div> 
    <p class="copyright">Copyright 2014 Life Inc. All Rights Reserved</p>
</div>

        </div>
</div>
<div class="overlay"></div>

<script type="text/javascript">
 
function logout()
{
    gapi.auth.signOut();
    location.reload();
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
            var str = "Name:" + resp['displayName'] + "<br>";
            str += "Image:" + resp['image']['url'] + "<br>";
            str += "<img src='" + resp['image']['url'] + "' /><br>";
 
            str += "URL:" + resp['url'] + "<br>";
            str += "Email:" + email + "<br>";
            /*document.getElementById("profile").innerHTML = str;*/
            console.log(str);
        });
 
    }
 
}
function onLoadCallback()
{
	gapi.client.setApiKey('AIzaSyA5S_9yARF13JQ7Xv-tMdz4nvdzZYyo1ZA');
    gapi.client.load('plus', 'v1',function(){});
}
 
    </script>
    
<script type="text/javascript">
      (function() {
       var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       po.src = 'https://apis.google.com/js/client.js?onload=onLoadCallback';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     })();
</script>
</body>
