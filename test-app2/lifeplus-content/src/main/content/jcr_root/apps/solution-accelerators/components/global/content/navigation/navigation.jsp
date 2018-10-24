<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator Navigation component.

  This component will be used for displaying the contents of the navigation component

  ==============================================================================

--%>
<%
	
%><%@include file="/libs/foundation/global.jsp"%>
<%
	
%><%@page session="false"%>
<%
	
%><!-- navigation	-->
<div class="nav-collapse collapse">
	<ul class="nav">
		<li class="dropdown"><a href="index.html">Home</a></li>
		<li class="dropdown"><a href="./Fashion.html">Fashion <b
				class="caret"></b></a>
			<ul class="dropdown-menu pull-left" role="menu"
				aria-labelledby="dLabel">
				<li class="dropdown-submenu"><a tabindex="-1"
					href="./Fashion-Women.html" style="text-decoration: none">
						Women </a>
					<ul class="dropdown-menu">
						<li><a tabindex="-1" href="./Fashion-Women-Watches.html"
							style="text-decoration: none"> Watches</a></li>
						<hr>
						<li><a href="./Fashion-Women-Footwear.html"
							style="text-decoration: none"> Footwear </a></li>
						<hr>
						<li><a href="./Fashion-Women-Clothing.html"
							style="text-decoration: none"> Clothing </a></li>
						<hr>
						<li><a href="./Fashion-Women-Sunglasses.html"
							style="text-decoration: none"> Sunglasses </a></li>
						<hr>
						<li><a href="./Fashion-Women-Bags.html"
							style="text-decoration: none"> Bags & Luggages </a></li>
						<hr>
						<li><a href="./Fashion-Women-Accessories.html"
							style="text-decoration: none"> Accessories </a></li>
					</ul></li>
				<hr>
				<li class="dropdown-submenu"><a tabindex="-1"
					href="./Fashion-Men.html" style="text-decoration: none"> Men </a>
					<ul class="dropdown-menu">
						<li><a tabindex="-1" href="./Fashion-Men-Watches.html"
							style="text-decoration: none"> Watches</a></li>
						<hr>
						<li><a href="./Fashion-Men-Footwear.html"
							style="text-decoration: none"> Footwear </a></li>
						<hr>
						<li><a href="./Fashion-Men-Clothing.html"
							style="text-decoration: none"> Clothing </a></li>
						<hr>
						<li><a href="./Fashion-Men-Sunglasses.html"
							style="text-decoration: none"> Sunglasses </a></li>
						<hr>
						<li><a href="./Fashion-Men-Bags.html"
							style="text-decoration: none"> Bags & Luggages </a></li>
						<hr>
						<li><a href="./Fashion-Men-Accessories.html"
							style="text-decoration: none"> Accessories </a></li>
					</ul></li>
				<hr>
				<li class="dropdown-submenu"><a tabindex="-1"
					href="./Fashion-Kids.html" style="text-decoration: none"> Kids
				</a>
					<ul class="dropdown-menu">
						<li><a tabindex="-1" href="./Fashion-Kids-Watches.html"
							style="text-decoration: none"> Watches</a></li>
						<hr>
						<li><a href="./Fashion-Kids-Footwear.html"
							style="text-decoration: none"> Footwear </a></li>
						<hr>
						<li><a href="./Fashion-Kids-Clothing.html"
							style="text-decoration: none"> Clothing </a></li>
						<hr>
						<li><a href="./Fashion-Kids-Sunglasses.html"
							style="text-decoration: none"> Sunglasses </a></li>
						<hr>
						<li><a href="./Fashion-Kids-Bags.html"
							style="text-decoration: none"> Bags & Luggages </a></li>
						<hr>
						<li><a href="./Fashion-Kids-Accessories.html"
							style="text-decoration: none"> Accessories </a></li>
					</ul></li>
				<hr>
			</ul></li>
		<li class="dropdown"><a href="./Electronics.html">Electronics</a></li>
		<li class="dropdown"><a href="./Books.html">Books</a></li>
		<li class="dropdown"><a href="./Insurance.html">Insurance</a></li>
		<li class="dropdown"><a href="./Others.html">Others</a></li>
	</ul>
	<ul class="nav pull-right">
		<li><cq:include path="searchbox"
				resourceType="solution-accelerators/components/global/content/searchbox" />
		</li>
		<li class="dropdown"><cq:include path="userinfo"
				resourceType="solution-accelerators/components/global/content/userinfo" />
		</li>
	</ul>
</div>
