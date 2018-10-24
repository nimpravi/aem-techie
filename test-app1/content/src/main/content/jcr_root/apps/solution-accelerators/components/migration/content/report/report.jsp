<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================
  Report Form Component
  
  This Component will contain form which is used for taking inputs for generating reports
  
   ==============================================================================

--%>
<%@include file="/libs/foundation/global.jsp"%>

<div id="form1"
	class="formWrapper col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<fieldset>
		<form id="viewReportForm" name="viewReportForm" action="/bin/MigrationManager"
			method="POST">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 btm date">
				<label>Select Date<span style="color: red;">*</span></label> <input
					type="text" id="datepicker" name="reportDate"
					title="Select the date for which the reports are to be viewed">
				<img src="/etc/designs/solution-accelerators/images/DateChooser.png">
				<br>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 btm r_type">
				<label>Report Type<span style="color: red;">*</span></label> <select
					name="reportType" id="reportType"
					title="Select the type of report required">
					<option value="all_reports">ALL</option>
					<option value="dam_report">DAM</option>
					<option value="transformation_report">TRANSFORMATION</option>
					<option value="import_report">IMPORT</option>
					<option value="taxonomy_report">TAXONOMY</option>
					<option value="replication_report">REPLICATION</option>
					<option value="reader_report">READER</option>
				</select> <br>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 btm r_detail">
				<label>One Step Migration or Content Migration<span
					style="color: red;">*</span></label> <select name="individualOrMigration"
					id="individualOrMigration"
					title="Select the type of reports needed:Complete Migration Report or Individual Report">
					<option value="MigrationReports">ONE STEP MIGRATION</option>
					<option value="IndividualReports">CONTENT MIGRATION</option>
				</select> <br>
			</div>
			<div
				class="col-lg-12 col-md-12 col-sm-12 col-xs-12 btm r_detail last">
				<label>Report Detail Type<span style="color: red;">*</span></label>
				<select name="reportDetailType" id="reportDetailType"
					title="Select the type of Report Details">
					<option value="SummarizedReport">SUMMARIZED</option>
					<option value="DetailedReport">DETAILED</option>
					<option value="SuccessReport">SUCCESS</option>
					<option value="FailureReport">FAILED</option>
				</select> <br>
			</div>

			<input type="hidden" name="curPage"
				value="<%=currentPage.getPath()%>" /> <input type="hidden"
				name="requestType" id="requestType" value="ShowReport" />

			<div align="right"
				class="col-lg-12 col-md-12 col-sm-12 col-xs-12 last btm r_button">
				<input type="button" name="viewReportButton" value="View Report"
					class="button actionButton"> <input type="reset"
					value="Reset" class="reset-button">
			</div>
		</form>
	</fieldset>
</div>

<form id="rightReport" name="rightReport" action="/bin/MigrationManager"
	method="POST">
	<div class="report_bg">
		<div id="left_sidebar"></div>

	</div>
</form>

<div class="back-details">
	<span class="d-arrow"></span>
</div>
<div id="right_content"></div>

<script>
$(document).ready(function(){
	var currentDate = new Date();
    $("#datepicker").datepicker({
        dateFormat: 'mm/dd/yy',
        maxDate: 0,
        changeYear: true 
    }).attr('readonly', 'readonly');
    $("#datepicker").datepicker("setDate", currentDate);
});
</script>

<script type="text/javascript">
	function validateDateField() {
		if (document.activity.datepicker.value == "") {
			alert("Please provide the date !");
			document.activity.datepicker.focus();
			return false;
		}
		return true;
	}
</script>
