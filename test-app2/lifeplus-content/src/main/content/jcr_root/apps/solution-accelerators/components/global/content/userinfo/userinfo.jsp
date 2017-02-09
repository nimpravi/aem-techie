<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator User Info component.

  This component will be used for displaying the  user info

  ==============================================================================

--%>
<%
	
%><%@include file="/libs/foundation/global.jsp"%>
<%
	
%><%@page session="false"%>
<%
	
%>
<a class="dropdown-toggle" data-toggle="dropdown" href="#"><i
	class="icon-user"></i> Guest</a>
<ul class="dropdown-menu">
	<li><a href="#myModal" data-toggle="modal"><i
			class="icon-pencil"></i> Sign In</a></li>
	<li><a href="#"><i class="icon-plus-sign"></i> Create a new
			account</a></li>
	<li><a href="#"><i class="icon-shopping-cart"></i> Cart</a></li>
	<li><a href="#"><i class="icon-list"></i> Wishlist</a></li>
	<li><a href="#"><i class="icon-globe"></i> Track My Orders</a></li>
	<li class="divider"></li>
	<li><a href="#"><i class="icon-off"></i> Sign Out</a></li>
</ul>