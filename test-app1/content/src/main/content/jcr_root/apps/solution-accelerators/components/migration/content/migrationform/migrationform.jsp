<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Migration Form Component component.

  This component will contain the form that will be used for taking inputs for 
  triggering migration process


  ==============================================================================

--%>
<%
	
%><%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="content">
	<div class="status" id="status" style="display: none">
		<div id="table-wrapper">
			<table>
				<tbody>
					<tr>
						<th id="totalHeader" colspan="2" class="center"></th>
					</tr>
					<tr>
						<td><b class="successTD"></b><br> <span class="viewFile"></span><b
							class="successPathTD"></b></td>
						<td><b class="failureTD"></b><br> <span class="viewFile"></span><b
							class="failurePathTD"></b></td>
					</tr>
					<tr>
						<td colspan="2" class="center"><span class="detailAll"></span><b
							class="reportFolder"></b></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="failure_status" id="failure_status" style="display: none">
		<div id="table-wrapper">
			<table>
				<tbody>
					<tr id="totalHeader">
						<th>Status</th>
						<th>Description</th>
					</tr>
					<tr id="failureHeader" style="display: none">
						<th class="failureTH" colspan="2" class="center"></th>
					</tr>
					<tr>
						<td><span class="statusTD"
							style="text-transform: capitalize;"></span></td>
						<td><span class="descriptionTD"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<form id="migrationRequestForm" name="migrationRequestForm"
		action="/bin/MigrationManager">

		<div class="form-wrapper">

			<div class="field-wrapper1 col-lg-12">
				<label>Read All Data From OSGi Configs</label> <select
					onchange="getForm(this.value)" name="readFromConfigs"
					id="readFromConfigs"
					title="Choose 'yes' if data is to be read from OSGi config">
					<option value="yes">Yes</option>
					<option selected value="no">No</option>
				</select>
			</div>
			<div class="complete-form field-wrapper1 col-lg-12">
				<label>Allow Duplicate Page Creation</label> <input type="checkbox"
					id="enableDuplication" name="enableDuplication"
					title="Check this if you want to create duplicate pages." />
			</div>
			<div class="complete-form field-wrapper1 col-lg-12">
				<label>Disable auto-update of content</label> <input type="checkbox"
					id="disableUpdate" name="disableUpdate"
					title="Check this if you want to disable auto-update" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Source Root Path<span style="color: red;">*</span></label> <input
					type="text" class="textbox"
					value="D:\MigrationFolders\CompleteMigrationSource"
					id="sourceRootPath" name="sourceRootPath"
					title="Select folder path from where content has to be read" />
			</div>
			<div class="complete-form field-wrapper1 col-lg-12">
				<label>File Destination Path<span style="color: red;">*</span></label>
				<input type="text" id="fileDestinationPath" class="textbox"
					name="fileDestinationPath"
					value="D:\MigrationFolders\CompleteMigrationDestination"
					title="Enter the path where the files are to be stored after processing" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Mapping File Path (XSLT File)<span
					style="color: red;">*</span></label> <input type="text"
					id="mappingFilePath" class="textbox"
					value="D:\MigrationFolders\XSL\InputXSL.xsl" name="mappingFilePath"
					title="Select the path of xsl file" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Transformation Type<span style="color: red;">*</span></label>
				<select name="transformationType" id="transformationType"
					title="Select the transformation type">
					<option selected value="XML">XML</option>
					<option value="EXCEL">EXCEL</option>
				</select>
			</div>
			<div class="complete-form field-wrapper1 col-lg-12">
				<label>Content Migration Order<span style="color: red;">*</span></label>
				<input type="text" id="importOrder" class="textbox"
					name="importOrder" value="page"
					title="Enter the transformation Order (Comma-separated)" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Destination Page Path in CRX<span style="color: red;">*</span></label>
				<input type="text" id="pagePath" class="textbox"
					value="/content/solution-accelerators" name="pagePath"
					title="Enter the path where the page has to be created" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Digital Asset Path in CRX<span style="color: red;">*</span></label>
				<input type="text" id="damPath" class="textbox"
					value="/content/dam/demo-site" name="damPath"
					title="Enter the path where the dam content has to be stored" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Taxonomy/Tag Path<span style="color: red;">*</span></label> <input
					type="text" id="taxonomyPath" class="textbox" value="demo"
					name="taxonomyPath"
					title="Enter the path were the page tags created ave to be stored" />
			</div>
			<div class="field-wrapper1 col-lg-12 last">
				<label>Move contents to publish environment<span
					style="color: red;">*</span></label> <select name="enableReplication"
					id="enableReplication"
					title="Check this if you want to enable replication">
					<option selected value="yes">Yes</option>
					<option value="no">No</option>
				</select>
			</div>
			<div class="field-wrapper2 col-lg-12">
				<input type="hidden" id="requestType" name="requestType"
					value="CompleteMigration" /> <input type="hidden" id="adminemail"
					name="adminemail" value="${currentStyle.adminEmail}" /> <input
					type="button" value="Submit" id="migrationbutton"
					class="migrationbutton"><input type="reset" value="Reset"
					class="reset-button">
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
	function getForm(optionVal) {
		if (optionVal == "no") {
			$(".complete-form").show();
		} else {
			$(".complete-form").hide();
		}
	}

	function validateMigrationRequestForm() {
		if (document.migrationRequestForm.readFromConfigs.value == "yes") {
			if (document.migrationRequestForm.sourceRootPath.value == "") {
				alert("Please provide Source Root Path!");
				document.migrationRequestForm.sourceRootPath.focus();
				return false;
			}
			if (document.migrationRequestForm.fileDestinationPath.value == "") {
				alert("Please provide File Destination Path!");
				document.migrationRequestForm.fileDestinationPath.focus();
				return false;
			}
			if (document.migrationRequestForm.mappingFilePath.value == "") {
				alert("Please provide Mapping File Path (.xsl)!");
				document.migrationRequestForm.mappingFilePath.focus();
				return false;
			}
			if (document.migrationRequestForm.pagePath.value == "") {
				alert("Please provide Destination Page Path in CQ Repository!");
				document.migrationRequestForm.pagePath.focus();
				return false;
			}
			if (document.migrationRequestForm.damPath.value == "") {
				alert("Please provide Digital Asset Path in CQ Repository!");
				document.migrationRequestForm.damPath.focus();
				return false;
			}
			if (document.migrationRequestForm.taxonomyPath.value == "") {
				alert("Please provide Taxonomy/Tag Path in CQ Repository!");
				document.migrationRequestForm.taxonomyPath.focus();
				return false;
			}
		} else if (document.migrationRequestForm.readFromConfigs.value == "no") {
			if (document.migrationRequestForm.sourceRootPath.value == "") {
				alert("Please provide Source Root Path!");
				document.migrationRequestForm.sourceRootPath.focus();
				return false;
			}
			if (document.migrationRequestForm.fileDestinationPath.value == "") {
				alert("Please provide File Destination Path!");
				document.migrationRequestForm.fileDestinationPath.focus();
				return false;
			}
			if (document.migrationRequestForm.mappingFilePath.value == "") {
				alert("Please provide Mapping File Path (.xsl)!");
				document.migrationRequestForm.mappingFilePath.focus();
				return false;
			}
			if (document.migrationRequestForm.importOrder.value == "") {
				alert("Please provide Content Migration Order!");
				document.migrationRequestForm.importOrder.focus();
				return false;
			}
			if (document.migrationRequestForm.pagePath.value == "") {
				alert("Please provide Destination Page Path in CQ Repository!");
				document.migrationRequestForm.pagePath.focus();
				return false;
			}
			if (document.migrationRequestForm.damPath.value == "") {
				alert("Please provide Digital Asset Path in CQ Repository!");
				document.migrationRequestForm.damPath.focus();
				return false;
			}
			if (document.migrationRequestForm.taxonomyPath.value == "") {
				alert("Please provide Taxonomy/Tag Path in CQ Repository!");
				document.migrationRequestForm.taxonomyPath.focus();
				return false;
			}
		}
		return true;
	}
</script>
