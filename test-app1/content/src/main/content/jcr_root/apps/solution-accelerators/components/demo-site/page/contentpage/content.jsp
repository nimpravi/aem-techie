<%--

  Content Page component.


--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>

<script>
	$( document ).ready(function() {
        var curTopLevelIndx = 2;
    });
</script>

<cq:include path="custompar" resourceType="foundation/components/parsys" />

<cq:include path="dropdownmenu" resourceType="/apps/solution-accelerators/components/demo-site/content/dropdownmenu" />