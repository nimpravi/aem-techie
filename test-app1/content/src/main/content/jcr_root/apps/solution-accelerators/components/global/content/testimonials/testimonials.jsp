<%--

  Testimonials Component

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@ page session="false" import="com.day.cq.wcm.api.WCMMode" %>
<%@page session="false"%>

<% if (WCMMode.fromRequest(request) == WCMMode.EDIT) { %>
        CONFIGURE TESTIMONIALS
<% } %>

<form id="testimonialsForm" name="testimonialsForm" action="/bin/dbconnection" method="POST">

	<input type="hidden" id="member_id" name="member_id" value="0">
    <input type="hidden" id="interest" name="interest" value="general">
    <input type="hidden" id="testi_count" name="testi_count" value="1">

    <input type="hidden" id="requestType" name="requestType" value="testimonials"/>


</form>

<div class="container testimonial_bg">
    <!-- testimonials-->
    <p class="testimonial"> TESTIMONIALS</p>
    <div id="response">
    </div>    
</div>

<script>
    $(document).ready(function() {
        $("#interest").val("${properties.interest}");
        $("#testi_count").val("${properties.testi_count}");
        if (window.CQ_Analytics) {
            var member_id = CQ_Analytics.ProfileDataMgr.getProperty("member_id");
        }
        if(null!=member_id && ""!=member_id) {
        	$("#member_id").val(member_id);
        }
        testimonials();
    });
</script>

<cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>


