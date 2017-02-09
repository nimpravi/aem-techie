<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================
  ContentTransformer form component
  
  This Component will contain form which is used for taking inputs for ContentTransformer 
  standalone process
   ==============================================================================

--%>
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

	<form name="contentTransformerForm" id="contentTransformerForm"
		action="/bin/MigrationManager" method="POST"
		enctype="multipart/form-data" target="_blank">
		<div class="form-wrapper">

			<div class="field-wrapper1 col-lg-12">
				<label>Source Root Path (XML Files)<span style="color: red;">*</span></label> <input
					type="text" name="sourceRootPath" id="sourceRootPath"
					class="textbox required no-spchr"
					value="D:\MigrationFolders\CompleteMigrationSource"
					title="Enter the path which contains the Input XML">
			</div>
			<div class="field-wrapper1 col-lg-12">
				<label>Mapping File Path (XSLT File)<span style="color: red;">*</span></label> <input
					type="text" name="mappingFilePath" id="mappingFilePath"
					class="textbox required no-spchr"
					value="D:\MigrationFolders\InputXSL.xsl"
					title="Enter the path of the mapping file to transform the source content">
			</div>

			<div class="field-wrapper1 col-lg-12">
				<label>Transformation Order<span style="color: red;">*</span></label>
				<input type="text" name="transformationOrder"
					id="transformationOrder" class="textbox required no-spchr"
					value="dam,tag,page"
					title="Enter the transformation Order (Comma-separated)">
			</div>

			<div class="field-wrapper1 col-lg-12">
				<label>File Destination Path<span style="color: red;">*</span></label>
				<input type="text" name="fileDestinationPath"
					id="fileDestinationPath" class="textbox required no-spchr"
					value="D:\MigrationFolders\TransformerDestination"
					title="Enter the path where the files are to be stored after transformation">
			</div>
			<div class="field-wrapper1 col-lg-12 last">
				<label>Transformation Type<span style="color: red;">*</span></label>
				<input type="text" name="transformationType" id="transformationType"
					class="textbox required no-spchr" value="XML"
					title="Enter the transformation type" readonly>
			</div>

			<div align="right" class="field-wrapper2 col-lg-12">
				<input type="hidden" name="requestType" id="requestType"
					value="contentTransformer" /> <input type="button" value="Submit"
					id="transformerActionButton" class="button"><input
					type="reset" value="Reset" class="reset-button">
			</div>

		</div>

	</form>
</div>

<script type="text/javascript">
	function validateTransformerForm() {
		if (document.contentTransformerForm.sourceRootPath.value == "") {
			alert("Please provide Source Root path value!");
			document.contentTransformerForm.sourceRootPath.focus();
			return false;
		}
		if (document.contentTransformerForm.mappingFilePath.value == "") {
			alert("Please provide mapping file Path value!");
			document.contentTransformerForm.mappingFilePath.focus();
			return false;
		}
		if (document.contentTransformerForm.transformationOrder.value == "") {
			alert("Please provide transformation order value!");
			document.contentTransformerForm.transformationOrder.focus();
			return false;
		}
		if (document.contentTransformerForm.fileDestinationPath.value == "") {
			alert("Please provide file destination path value!");
			document.contentTransformerForm.fileDestinationPath.focus();
			return false;
		}
		if (document.contentTransformerForm.transformationType.value == "") {
			alert("Please provide transformation type value!");
			document.contentTransformerForm.transformationType.focus();
			return false;
		}

		return true;
	}
</script>