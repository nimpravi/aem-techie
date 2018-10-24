<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  XSD form component
  
  This component will contain form which is used to take inputs for creating sample xsdfromat
  ==============================================================================

--%>
<%
	
%><%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="content">
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

	
	<form id="xsdgeneratorform" name="xsdgeneratorform" action="/bin/MigrationManager">
		<div class="form-wrapper">
			<div id="xsdDestination" class="field-wrapper1 col-lg-12 last">
				<label>Storage Destination <span style="color: red;">*</span></label>
				<input name="moveTo" type="radio" value="fileSystem" checked 
					title="Choose the destination path for storage: File system" />
				File System &nbsp;&nbsp;&nbsp; 
				<input name="moveTo" type="radio" value="CRX"
					title="Choose the destination path for storage:CRX " /> CRX
			</div>
			<div id="destPathDiv" class="field-wrapper1 col-lg-12 last" style="display: none">
				<label>Storage Path <span style="color: red;">*</span></label> 
				<input id="destinationRootPath" name="destinationRootPath" type="text"
					class="textbox no-spchr"
					title="Enter the destination path where the generated file has to be stored"/>
			</div>
			<div id="submitDiv" class="field-wrapper2 col-lg-12" style="display: none">
				<input type="hidden" id="requestType" name="requestType" value="GenerateXSD" />
				<input type="button" value="Store" class="xsdbutton" id="xsdGenerateAction1"/>
				<input type="reset" value="Reset" class="reset-button"/>
			</div>
			<div id="downloadDiv" class="field-wrapper2 col-lg-12">
				<a id="xsdFilePath" href="#" style="display:none"><span>FilePath</span></a> 
				<input type="button" value="Download" class="xsdbutton" id="xsdGenerateAction"/>
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
	function validateXsdForm() {
		if (document.xsdgeneratorform.moveTo.value == "CRX") {
			if (document.xsdgeneratorform.destinationRootPath.value == "") {
				alert("Please provide Storage path value!");
				document.xsdgeneratorform.destinationRootPath.focus();
				return false;
			}
			return true;
		}
	}
</script>
<script type="text/javascript">
	function disableForm() {
		$('.form-wrapper :input').prop('disabled', true);

	}
</script>



