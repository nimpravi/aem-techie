<%@include file="/libs/foundation/global.jsp" %>
<cq:setContentBundle/>

 <div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button class="btn btn-navbar" data-target=".nav-collapse" data-toggle="collapse" type="button">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
                <a class="brand" href="#"><img src="/etc/designs/ebazzarresp/clientlibs/images/Cstore_logo.png"></a>
				<div class="nav-collapse collapse">

                    <ul class="nav pull-right">
                         <li><a href="/content/ebazzar/en/fashion.html">Take a tour</a></li>

					<li>
					<form class="navbar-form form-search pull-right" action="/content/ebazzar/en/search-result.html">
						<input id="Search" type="text" placeholder="Search" class="input-medium search-query" name="q" value ="">

                        <button type="submit" class="btn"><i class="icon-search"></i></button>
					</form>
	             </li>

                        <cq:include script="userinfo.jsp"/>
					  <li class="hidden-phone hidden-tablet"><a href="#modalGeo" id="geolocA" data-toggle="modal"><i class="icon-globe"></i> location</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <cq:include path="signin" resourceType="/apps/ebazaarresp/components/content/login"/>
	</div>


	<div id="modalGeo" data-backdrop="static" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modalGeoLabel" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h3 id="modalGeoLabel">Welcome to  CSTORE</h3>
                </div>
                <div class="modal-body" style="overflow: auto;">
                    <form class="" action="" method="">
                        Enter Your Location:&nbsp;  <input type="text" class="addresspicker" id="address" />
                    </form>
                </div>
                <div class="modal-footer hidden">
                    <button id="geosave" class="btn" data-dismiss="modal" aria-hidden="true">Save</button>
                </div>
	</div>

