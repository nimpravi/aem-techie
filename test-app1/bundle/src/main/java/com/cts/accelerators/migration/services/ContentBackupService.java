package com.cts.accelerators.migration.services;

/**
 * This service class is used for the reading the original content provided for migration to be stored for 
 * reference either in CRX or an external storage server based on user preferences
 *  
 * @author Cognizant
 */

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
import com.cts.accelerators.core.services.AcceleratorService;
import com.cts.accelerators.core.services.ConfigurationUtil;
import com.cts.accelerators.core.util.AcceleratorUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.helpers.ContentBackupHelper;
import com.cts.accelerators.migration.services.dto.ContentBackupServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentBackupServiceResponse;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceRequest;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceResponse;

@Component(metatype = true, immediate = true, enabled = true)
@Service(ContentBackupService.class)
public class ContentBackupService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentBackupService.class);
	private static final String CLASS_NAME = ContentBackupService.class.getName();

	@Reference
	private ConfigurationUtil configurationUtil;
	
	@Reference
	public ReportGeneratorService reportService;

	@Activate
	protected void activate() {
		LOGGER.info("Content Backup service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Content Backup service stopped");
	}

	/**
	 * This method overrides the super service class execute method and start
	 * the process for content backup.
	 * 
	 * @param serviceRequest
	 *            - contains the properties set at migration initiation time
	 * @return AceeleratorServiceResponse - containing any information to be
	 *         returned to the calling program
	 * @throws AcceleratorException
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject validationResponse = new JSONObject();
		ContentBackupServiceResponse serviceResponse = new ContentBackupServiceResponse();
		ReportGeneratorServiceRequest reportRequest = new ReportGeneratorServiceRequest();
		ContentBackupServiceRequest backupRequest = null;
		if (serviceRequest instanceof ContentBackupServiceRequest) {
			backupRequest = (ContentBackupServiceRequest) serviceRequest;
		}

		if (!backupRequest.isCompleteMigration()) {
			reportRequest
					.setReportType(AcceleratorGenericConstants.BACKUP_REPORT);
			reportRequest.setLoadDefault(true);

			Map<String, String> reportFilePaths = new HashMap<String, String>();
			
			reportFilePaths = ((ReportGeneratorServiceResponse) reportService
					.execute(reportRequest)).getReportFilePaths();
				backupRequest.setReportFilePath(reportFilePaths
						.get(AcceleratorGenericConstants.BACKUP_REPORT));
		}

		validationResponse = validateRequiredParameters(backupRequest);
		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
		} catch (JSONException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME,
					methodName,
					e.getCause());
		}
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(validationStatus)) {
			serviceResponse = (ContentBackupServiceResponse) ContentBackupHelper
					.copyContents(backupRequest,
							backupRequest.getReportFilePath());
		} else {
			serviceResponse.setJsonResponse(validationResponse);
		}
		LOGGER.info(" || " + methodName + " || END");
		return serviceResponse;
	}


	/**
	 * This method is used to validate the content backup parameters
	 * 
	 * @param backupRequest
	 * @return responseObject
	 * @throws AcceleratorException
	 */
	private JSONObject validateRequiredParameters(
			ContentBackupServiceRequest backupRequest)
			throws AcceleratorException {
		String methodName = "validateRequiredParameters";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true;
		boolean isValidPath = true;
		boolean pathCheck = true;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (backupRequest != null) {
				if (backupRequest.getSourceRootPath() == null
						|| StringUtils.trim(backupRequest.getSourceRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Content Backup Source Path Information Missing;");
				} else {
					pathCheck = AcceleratorUtils.checkServerLocation(backupRequest.getSourceRootPath());
					isValidPath = isValidPath & pathCheck;
					if (!pathCheck) {
						parameterPresent = false;
						statusDescription
								.append("Source Path Does Not Exist;");
					}
				}
				if (backupRequest.getDestinationRootPath() == null
						|| StringUtils.trim(
								backupRequest.getDestinationRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Content Backup Destination Path Information Missing;");
				} else if(!backupRequest.isCompleteMigration()) {
					pathCheck = AcceleratorUtils.checkServerLocation(backupRequest.getDestinationRootPath());
					isValidPath = isValidPath & pathCheck;
					if (!pathCheck) {
						parameterPresent = false;
						statusDescription
								.append("Destination Path Does Not Exist;");
					}
				}

			} else {
				parameterPresent = false;
				statusDescription
						.append("Content Backup Request Information Missing;");
			}
			if (!parameterPresent) {
				responseObject.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_FAILURE);
				responseObject.put(AcceleratorGenericConstants.DESCRIPTION,
						statusDescription.toString());
			} else {
				responseObject.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_SUCCESS);

			}
		} catch (JSONException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME,
					methodName,
					e.getCause());
		}
		LOGGER.info(" || " + methodName + " || responseObject || "
				+ responseObject);
		LOGGER.info(" || " + methodName + " || END");
		return responseObject;
	}
}
