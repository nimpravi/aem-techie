<%--

  My Psoriasis SlideShow component.

  My Psoriasis SlideShow

--%><%
%><%@ page import="com.day.cq.commons.jcr.JcrUtil,java.util.Iterator,javax.jcr.Node,javax.jcr.NodeIterator,
                     com.day.cq.wcm.api.components.DropTarget,org.apache.jackrabbit.commons.JcrUtils" %>
 <%@include file="/libs/foundation/global.jsp"%><cq:includeClientLib js="cq.jquery"/><%
%><%@page session="false" %><%
%><%
 String name = currentNode.getPath();
 String contextPath = request.getContextPath() != null ? request.getContextPath() : "";

%>
<div class="carousel slide">
<div class="carousel-inner">

<div class="cycle-slideshow"
			data-cycle-fx="fade"
			data-cycle-speed="2000"
			data-cycle-timeout="5000"
		>

<% 

    NodeIterator ni = currentNode.getNodes();
    while (ni.hasNext()) {
      Node nii = ni.nextNode(); 
        if(nii.hasProperty("fileReference")){
      String imagepath = nii.getProperty("fileReference").getString();
      %>
      <img src="<%=imagepath%>" width="100%" height="100%" alt="" >

      <%
          //}
   }
}
    %>

</div>
    </div>
</div>