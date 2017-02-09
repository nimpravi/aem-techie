<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================
  ContentImporter Form component
  
  This Component will contain form which is used for taking inputs for contentimporter 
  standalone process
  ==============================================================================

--%>
<%@include file="/libs/foundation/global.jsp"%>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="content">
	<div class="status" id="status" style="display:none">
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
	<form name="contentImporterForm" id="contentImporterForm"
		action="/bin/MigrationManager" method="POST"
		enctype="multipart/form-data" target="_blank">

		<div class="form-wrapper">

			<div class="field-wrapper1 col-lg-12">
				<label>Allow Duplicate Page Creation</label> <input type="checkbox"
					id="enableDuplication" name="enableDuplication"
					title="Check this if you want to create duplicate pages." />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Disable auto-update of content</label> <input type="checkbox"
					id="disableAutoUpdate" name="disableAutoUpdate"
					title="Check this if you want to disable auto-update." />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Replicate content to Publish server</label> <input
					type="checkbox" id="enableReplication" name="enableReplication"
					title="Check this if you want to replicate content to publish server." />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Source Root Path<span style="color: red;">*</span></label> <input
					type="text" name="sourceRootPath" id="sourceRootPath" class="textbox"
					title="Enter the source path from where the content is to be imported"
					value="D:\MigrationFolders\ImportSourceRoot">
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>File Destination Path<span style="color: red;">*</span></label>
				<input type="text" name="fileDestinationPath"
					id="fileDestinationPath" class="textbox"
					title="Enter the destination path where the content is to be imported"
					value="D:\MigrationFolders\ImportDestination" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Content Migration Order<span style="color: red;">*</span></label>
				<input type="text" name="importOrder" id="importOrder"
					class="textbox" title="Enter the Import Order (Comma-separated)"
					/>
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Destination Page Path in CQ Repository<span
					style="color: red;">*</span></label> <input type="text" id="pagePath"
					class="textbox required no-spchr" name="pagePath"
					title="Enter the path where the page has to be created"
					value="/content/geometrixx/en" />
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Digital Asset Path in CQ Repository<span
					style="color: red;">*</span></label> <input type="text" id="damPath"
					class="textbox required no-spchr" name="damPath"
					title="Enter the path where the dam content has to be stored"
					value="/content/dam/solution-accelerators/damfolder" />
			</div>
			<div class="field-wrapper1 col-lg-12 last">
				<label>Taxonomy/Tag Path<span style="color: red;">*</span></label> <input
					type="text" id="taxonomyPath" class="textbox required no-spchr"
					name="taxonomyPath"
					title="Enter the path were the page tags created ave to be stored"
					value="migration" />
			</div>
			<div align="right" class="field-wrapper2 col-lg-12">
				<input type="hidden" name="requestType" id="requestType"
					value="contentImporter" /> <input type="button" value="Submit"
					id="importerActionButton" class="button"><input
					type="reset" value="Reset" class="reset-button">
			</div>

		</div>

	</form>

</div>
<script type="text/javascript">
	function validateContentimporterForm() {
		if (document.contentImporterForm.sourceRootPath.value == "") {
			alert("Please provide source root path value!");
			document.contentReaderForm.sourceRootPath.focus();
			return false;
		}
		if (document.contentImporterForm.fileDestinationPath.value == "") {
			alert("Please provide file archive destination path value!");
			document.contentReaderForm.fileDestinationPath.focus();
			return false;
		}
		if (document.contentImporterForm.importOrder.value == "") {
			alert("Please provide import order value!");
			document.contentReaderForm.importOrder.focus();
			return false;
		}
		if (document.contentImporterForm.pagePath.value == "") {
			alert("Please provide content page path value!");
			document.contentReaderForm.pagePath.focus();
			return false;
		}
		if (document.contentImporterForm.damPath.value == "") {
			alert("Please provide digital content path value!");
			document.contentReaderForm.damPath.focus();
			return false;
		}
		if (document.contentImporterForm.taxonomyPath.value == "") {
			alert("Please provide taxonomy path value!");
			document.contentReaderForm.taxonomyPath.focus();
			return false;
		}
		if (document.contentImporterForm.enableReplication.value == "") {
			alert("Please provide content replication to publish value!");
			document.contentReaderForm.enableReplication.focus();
			return false;
		}
		return true;
	}
</script>

