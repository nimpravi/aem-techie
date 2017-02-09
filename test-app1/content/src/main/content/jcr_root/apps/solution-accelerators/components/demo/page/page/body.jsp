<%--

  Page component.


--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>

<%
	// TODO add you code here
%>

<body>
    <div class="wrapper">
        <cq:include script="header.jsp" />  
		<cq:include path="topnav" resourceType="/apps/solution-accelerators/components/demo/content/topnav" />
        <div class="sub-container-01">
            <cq:include path="leftnav" resourceType="/apps/solution-accelerators/components/demo/content/leftnav"/>
        </div>
        <cq:include script="content.jsp" />
        <div class="sub-container-02"></div>
        <cq:include script="footer.jsp" />
    </div>
</body>