<%@include file="/libs/foundation/global.jsp" %>
<cq:setContentBundle/>

 <div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button class="btn btn-navbar" data-target=".nav-collapse" data-toggle="collapse" type="button">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
                <a class="brand" href="#"><img src="/etc/designs/ebazzarresp/clientlibs/images/Cstore_logo.png"></a>
				<div class="nav-collapse collapse">
                    <cq:include path="topnav" resourceType="ebazaarresp/components/content/topnav"/>
                    <ul class="nav pull-right">
					<li>
					<form class="navbar-form form-search pull-right" action="/content/ebazzar/en/search-result.html">
                        <input id="Search" type="text" placeholder="Search" class="input-medium search-query" name="q" value =""/>

                        <button type="submit" class="btn"><i class="icon-search"></i></button>
					</form>
	             </li>
                        
                        <cq:include script="userinfo.jsp"/>

					</ul>
				</div>
			</div>
		</div>
	</div>

	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <cq:include path="signin" resourceType="/apps/ebazaarresp/components/content/login"/>
	</div>


