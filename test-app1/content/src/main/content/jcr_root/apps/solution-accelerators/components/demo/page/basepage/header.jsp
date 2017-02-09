<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator Header script.

  This component will be used for displaying the contents of the header component

  ==============================================================================

--%>
<%
	
%><%@include file="/libs/foundation/global.jsp"%>
<%
	
%><%@page session="false"%>
<%

%>




<header id="header" class="navbar-fixed-top ">
	<div class="row-fluid top_header">

    <cq:include path="logocomp" resourceType="/apps/solution-accelerators/components/demo/content/logo" />
	<div class="col-md-8 hidden-xs hidden-sm hidden-md">
		<div class="row">
            <cq:include path="sitenav" resourceType="/apps/solution-accelerators/components/demo/content/sitenav" />
	    </div>
		<div class="row">
        	<cq:include path="topnav" resourceType="/apps/solution-accelerators/components/demo/content/topnavigation" />
        </div>
	</div>

        <div class="collapse" id="mobView">
          <ul class="nav navbar-nav collapse" id="collapseButtonData1" style="height: 0px; display: block;">

              
                                      <li class=""><a href="/content/solution-accelerators/Demo/home-page/company.html" class="">Company</a></li>    
                        
                                      <li class=""><a href="/content/solution-accelerators/Demo/home-page/brands.html" class="">Brands</a></li>    

                                      <li class=""><a href="/content/solution-accelerators/Demo/home-page/sustainability.html" class="">Sustainability</a></li>    
                        
                                      <li class=""><a href="/content/solution-accelerators/Demo/home-page/rnd.html" class="">R n D</a></li>    
                        
                                      <li class="">Corporate</li>    
                        

                                <li><div class="right-inner-addon">
            <i class="glyphicon glyphicon-search"></i>
            <input type="search" class="form-control" placeholder="Search">
        </div></li>  
                  </ul>
        </div>

    </div>
</header>


