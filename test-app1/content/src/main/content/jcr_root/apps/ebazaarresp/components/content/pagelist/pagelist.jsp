<%--

  pagelist  component.

  pagelist component for campaign

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
	// TODO add you code here
%>

<ul class="thumbnails">
					<li class="span4">
						<div class="thumbnail">
                            <a href="<cq:text property="link1"/>.html">
                                <img  height="200" width="300"  src="<cq:text property="image1/fileReference" />" alt="<cq:text property="title1"/>"></a>

						</div>
					</li>
    	<li class="span4">
						<div class="thumbnail">
                               <a href="<cq:text property="link2"/>.html">
                                   <img  height="200" width="300"  src="<cq:text property="image2/fileReference" />" alt="<cq:text property="title2"/>"></a>

						</div>
					</li>

    	       <li class="span4">
						<div class="thumbnail">
                               <a href="<cq:text property="link3"/>.html">
                                   <img height="200" width="300"   src="<cq:text property="image3/fileReference" />" alt="<cq:text property="title3"/>"></a>

						</div>
					</li>

  </ul>