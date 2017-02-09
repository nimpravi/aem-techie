<%--

  ebazaar-header component.

  

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %>
<c:set var="ishomepage"  value="<%=currentStyle.get("ishomepage", false)%>"/>
<c:set var="level"  value="<%=currentStyle.get("absParent", 2L)%>"/>
<c:set var="logoPath"  value="<%=currentStyle.get("logopath", "")%>"/>
<c:set var="takeatourlink"  value="<%=currentStyle.get("linkto", "")%>"/>
<cq:setContentBundle/>

 <div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button class="btn btn-navbar" data-target=".nav-collapse" data-toggle="collapse" type="button">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
                <a class="brand" href="#"><img src="${logoPath}"></a>
				<div class="nav-collapse collapse">
              <!--<c:if test="${ishomepage eq false}">
                     <cq:include script="topnav.jsp"/>

               </c:if>-->
                    <cq:include script="topnav.jsp"/>
                    <ul class="nav pull-right">
                        <!--<c:if test="${ishomepage eq true}">
                             <li><a href="${takeatourlink}.html">Take a tour</a></li>
                        </c:if>-->
					<li>
					<form class="navbar-form form-search pull-right" action="/content/ebazzar/en/search-result.html">
						<input id="Search" type="text" placeholder="Search" class="input-medium search-query" name="q" value ="">

                        <button type="submit" class="btn"><i class="icon-search"></i></button>
					</form>
	             </li>

                        <cq:include script="userinfo.jsp"/>
                        <c:if test="${ishomepage eq true}">
					  <li class="hidden-phone hidden-tablet"><a href="#modalGeo" id="geolocA" data-toggle="modal"><i class="icon-globe"></i> location</a></li>
                     </c:if>
                        </ul>
				</div>
			</div>
		</div>
	</div>

	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <cq:include path="signin" resourceType="/apps/ebazaarresp/components/content/login"/>
	</div>

 <c:if test="${ishomepage eq true}">
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


</c:if>