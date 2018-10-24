<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  BulkUpload Form component

  This component will contain form which is used for taking inputs for  bulkupload of Files
   ==============================================================================

--%>
<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false"%>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="content">

	<div class="status" id="status" style="display: none">
		<div id="table-wrapper">
			<table>
				<tbody>
					<tr>
						<th id="totalHeader" colspan="2" class="center"></th>
					</tr>
					<tr>
						<td><b class="successTD"></b><br>
						<span class="viewFile"></span><b class="successPathTD"></b></td>
						<td><b class="failureTD"></b><br>
						<span class="viewFile"></span><b class="failurePathTD"></b></td>
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
                        <th>Status</th><th>Description</th>
					</tr>
                    <tr id="failureHeader" style="display: none">
                        <th class="failureTH" colspan="2" class="center"></th>
                    </tr>
					<tr>
						<td><span class="statusTD" style="text-transform: capitalize;"></span></td>
                        <td><span class="descriptionTD"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<form name="damBulkUploadForm" id="damBulkUploadForm"
		action="/bin/MigrationManager" method="POST"
		enctype="multipart/form-data" target="_blank">
		<div class="bulk-upload">
			<div class="form-wrapper">
				<div class="field-wrapper1 col-lg-12">
					<label>Source Path<span style="color: red;">*</span></label> <input
						type="text" name="sourceRootPath" id="sourceRootPath"
						class="textbox required no-spchr" value="D:\MigrationFolders\DAMXMLSourceContent" 
						title="Enter the source root path from where the files have to be read for bulk upload">
				</div>
				<div class="field-wrapper1 col-lg-12">
					<label>File Destination Path <span style="color: red;">*</span></label>
					<input type="text" name="fileDestinationPath" value="D:\MigrationFolders\DAMDestination" 
						id="fileDestinationPath" class="textbox required no-spchr"
						title="Enter the destination path where the files have to be placed">
				</div>
				<div class="field-wrapper1 col-lg-12 last">
					<label>Destination Path <span style="color: red;">*</span></label>
					<input type="text" name="destinationRootPath" id="destinationRootPath"
						class="textbox required no-spchr" value="/content/dam/solution-accelerators/damfolder" 
						title="Enter the DAM destination path in CQ Repository">
				</div>

				<div align="right" class="field-wrapper2 col-lg-12">
					<input type="hidden" name="requestType" id="requestType"
						value="DamBulkUpload" /> <input type="button" value="Submit"
						id="bulkUploadAction" class="button"><input type="reset"
						value="Reset" class="reset-button">
				</div>

			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
	function validateDamBulkUploadForm() {
		if (document.damBulkUploadForm.sourceRootPath.value == "") {
			alert("Please provide source root path value!");
			document.damBulkUploadForm.sourceRootPath.focus();
			return false;
		}
		if (document.damBulkUploadForm.fileDestinationPath.value == "") {
			alert("Please provide path where files needs to be moved!");
			document.damBulkUploadForm.fileDestinationPath.focus();
			return false;
		}
		if (document.damBulkUploadForm.destinationRootPath.value == "") {
			alert("Please provide bulk upload destination path value!");
			document.damBulkUploadForm.destinationRootPath.focus();
			return false;
		}
		return true;
	}
</script>
