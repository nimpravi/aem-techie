package com.cts.accelerators.migration.services;

/**
 * This class is used to import files to CRX from destination According to the Migration order
 * @author 369565
 */

import java.io.File;

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
import com.cts.accelerators.core.services.impl.ReplicationServiceImpl;
import com.cts.accelerators.core.util.AcceleratorTaxonomyUtilis;
import com.cts.accelerators.core.util.AcceleratorUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.helpers.ContentImporterHelper;
import com.cts.accelerators.migration.services.dto.ContentImporterServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentImporterServiceResponse;
import com.cts.accelerators.migration.services.dto.DamUploadServiceRequest;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceRequest;

@Component(immediate = true, metatype = true)
@Service(value = ContentImporterService.class)
public class ContentImporterService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentImporterService.class);

	@Reference
	private ConfigurationUtil configurationUtil;

	@Reference
	public ReportGeneratorService reportService;

	@Reference
	private DamUploadService damService;

	@Reference
	private ReplicationServiceImpl replicationService;

	@Property(label = "Enable Duplicate Page Creation?", boolValue = false, description = "Please check this if you want to create duplicate pages.")
	public static final String VALUE1 = "enableDuplication";

	@Property(label = "Disable auto-update?", boolValue = false, description = "Please check this disable auto update")
	public static final String VALUE2 = "disableUpdate";

	@Property(label = "Source Root Path", name = "sourceRootPath", unbounded = PropertyUnbounded.DEFAULT, description = "Source root path from where to read the input XML files.")
	private static final String VALUE3 = "sourceRootPath";

	@Property(label = "File Destination Root Path", name = "fileDestinationRootPath", unbounded = PropertyUnbounded.DEFAULT, description = "Root Folder Path where all the files successfully imported are stored")
	private static final String VALUE4 = "fileDestinationRootPath";

	@Property(label = "Content Import Order", name = "importOrder", unbounded = PropertyUnbounded.DEFAULT, description = "Please specify the content import order")
	public static final String VALUE5 = "importOrder";

	@Property(label = "Content Page Root Path", name = "pageRootPath", unbounded = PropertyUnbounded.DEFAULT, description = "Root Folder Path where all the files successfully imported are stored")
	private static final String VALUE6 = "pageRootPath";

	@Property(label = "DAM Root Path", name = "damRootPath", unbounded = PropertyUnbounded.DEFAULT, description = "Root Folder Path where all the files successfully imported are stored")
	private static final String VALUE7 = "damRootPath";

	@Property(label = "Parent Tag Name", name = "parentTagName", unbounded = PropertyUnbounded.DEFAULT, description = "Root Folder Path where all the files successfully imported are stored")
	private static final String VALUE8 = "parentTagName";

	@Property(label = "Replication Agent", name = "replicationAgent", value = "publish", unbounded = PropertyUnbounded.DEFAULT, description = "Please specify the replication agent")
	public static final String VALUE9 = "replicationAgent";

	@Activate
	protected void activate() {
		LOGGER.info("Content Importer service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Content Importer service stopped");
	}

	/**
	 * This method will be called by external services and application to start
	 * the content import
	 * 
	 * @param ServiceRequest
	 *            serviceReequest
	 * @return ServiceResponse containing status in JSON format
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");
		ContentImporterServiceResponse serviceResponse = new ContentImporterServiceResponse();
		ReportGeneratorServiceRequest reportRequest = new ReportGeneratorServiceRequest();

		// Get all required parameters
		ContentImporterServiceRequest importerServiceRequest = null;
		if (serviceRequest instanceof ContentImporterServiceRequest) {
			importerServiceRequest = (ContentImporterServiceRequest) serviceRequest;
			if (importerServiceRequest.isLoadDefault()) {
				importerServiceRequest = setConfigProperties(importerServiceRequest);
			}
		}

		// Get DAM Service Request required parameters
		importerServiceRequest.setDamRequest(damService
				.getConfigProperties(new DamUploadServiceRequest()));
		
		// Get reports path
		if (!importerServiceRequest.isCompleteMigration()) {
			LOGGER.info(" || " + methodName + " || START");
			reportRequest.setReportType(AcceleratorGenericConstants.IMPORT_REPORT);
		}

		// Validate if the required parameters are available
		JSONObject validationResponse = new JSONObject();
		validationResponse = validateRequiredParameters(importerServiceRequest);
		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
		} catch (JSONException e) {
			LOGGER.error(
					"An exception has occured in ContentImporterService.execute()",
					e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					ContentImporterService.class.getName(),
					"execute(AcceleratorServiceRequest serviceRequest)",
					e.getCause());
		}

		// Start content import and send the response back to the caller
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(validationStatus)) {
			serviceResponse = importContent(importerServiceRequest);
		} else {
			serviceResponse.setJsonResponse(validationResponse);
		}
		LOGGER.info(" || " + methodName + " || serviceResponse || "
				+ serviceResponse);
		LOGGER.info(" || " + methodName + " || END");
		return serviceResponse;
	}

	/**
	 * This method will initiate the content import based on the provided inputs
	 * 
	 * @param importerServiceRequest
	 * @return
	 * @throws AcceleratorException
	 */
	private ContentImporterServiceResponse importContent(
			ContentImporterServiceRequest importerRequest)
			throws AcceleratorException {
		String methodName = "importContent";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject jsonResponse = importerRequest.getJsonResponse();
		ContentImporterServiceResponse importResponse = new ContentImporterServiceResponse();
		ReportGeneratorServiceRequest reportRequest = new ReportGeneratorServiceRequest();
		
		try {
			reportRequest = reportService.setConfigProperties(reportRequest);
			reportService.validateRequiredParameters(reportRequest);
		
			// Start iterating the source folder in the order specified in the
			// service request
			String[] importIterationOrder = null;
			if (importerRequest.getImportOrder() != null) {
				importIterationOrder = importerRequest.getImportOrder().split(AcceleratorGenericConstants.CSV_SEPARATOR);
				for (String folderName : importIterationOrder) {
					File folderFile = new File(importerRequest.getSourceRootPath()
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ folderName);
					if (folderFile.exists()) {
						LOGGER.debug(" || " + methodName + " || Folder name || "
								+ folderFile.getAbsolutePath() + " || reportRequest.getReportRootPath() || "+reportRequest.getReportRootPath());
						jsonResponse = ContentImporterHelper.consolidateJSON(jsonResponse,
								ContentImporterHelper.importToCRX(
								importerRequest, folderFile.getName(), reportRequest.getReportRootPath()));
					}
				}
			}

			// Set json response in the response object
			importResponse.setJsonResponse(jsonResponse);
		} catch (JSONException e) {
			LOGGER.error(
					"An exception has occured in ContentImporterService.execute()",
					e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					ContentImporterService.class.getName(),
					"execute(AcceleratorServiceRequest serviceRequest)",
					e.getCause());
		}
		LOGGER.info(" || " + methodName + " || final jsonResponse || "+jsonResponse);
		LOGGER.info(" || " + methodName + " || END");
		return importResponse;
	}

	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param importerServiceRequest
	 * @return Content reader request variable containing configured values
	 * @throws AcceleratorException
	 */
	private ContentImporterServiceRequest setConfigProperties(
			ContentImporterServiceRequest importerServiceRequest)
			throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START");
		String PID = "com.cts.accelerators.migration.services.ContentImporterService";

		importerServiceRequest.setEnableDuplication(configurationUtil
				.getBooleanConfig(PID, VALUE1));
		importerServiceRequest.setDisableUpdate(configurationUtil
				.getBooleanConfig(PID, VALUE2));

		if (importerServiceRequest.getSourceRootPath() == null
				|| StringUtils.trim(importerServiceRequest.getSourceRootPath())
						.isEmpty()) {
			importerServiceRequest.setSourceRootPath(configurationUtil
					.getConfig(PID, VALUE3));
		}
		if (importerServiceRequest.getFileDestinationRootPath() == null
				|| StringUtils.trim(
						importerServiceRequest.getFileDestinationRootPath())
						.isEmpty()) {
			importerServiceRequest.setFileDestinationRootPath(configurationUtil
					.getConfig(PID, VALUE4));
		}
		if (importerServiceRequest.getImportOrder() == null
				|| StringUtils.trim(importerServiceRequest.getImportOrder())
						.isEmpty()) {
			importerServiceRequest.setImportOrder(configurationUtil.getConfig(
					PID, VALUE5));
		}
		if (importerServiceRequest.getPageStoragePath() == null
				|| StringUtils
						.trim(importerServiceRequest.getPageStoragePath())
						.isEmpty()) {
			importerServiceRequest.setPageStoragePath(configurationUtil
					.getConfig(PID, VALUE6));
		}
		if (importerServiceRequest.getDamStoragePath() == null
				|| StringUtils.trim(importerServiceRequest.getDamStoragePath())
						.isEmpty()) {
			importerServiceRequest.setDamStoragePath(configurationUtil
					.getConfig(PID, VALUE7));
		}
		if (importerServiceRequest.getTaxonomyStoragePath() == null
				|| StringUtils.trim(
						importerServiceRequest.getTaxonomyStoragePath())
						.isEmpty()) {
			importerServiceRequest.setTaxonomyStoragePath(configurationUtil
					.getConfig(PID, VALUE8));
		}
		if (importerServiceRequest.getRelpicationAgent() == null
				|| StringUtils.trim(
						importerServiceRequest.getRelpicationAgent()).isEmpty()) {
			importerServiceRequest.setRelpicationAgent(configurationUtil
					.getConfig(PID, VALUE9));
		}
		LOGGER.info(" || " + methodName + " || END");
		return importerServiceRequest;
	}

	/**
	 * This method is used to validate if all the necessary request variables
	 * are present or not
	 * 
	 * @param importerServiceRequest
	 * @return
	 * @throws AcceleratorException
	 */
	private JSONObject validateRequiredParameters(
			ContentImporterServiceRequest importerServiceRequest)
			throws AcceleratorException {
		String methodName = "validateRequiredParameters : content importer";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true;
		boolean isValidPath = true;
		boolean pathCheck = true;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (importerServiceRequest != null) {
				if (importerServiceRequest.getSourceRootPath() == null
						|| StringUtils.trim(
								importerServiceRequest.getSourceRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Content Importer Source Path Information Missing;");
				}
				else if(!importerServiceRequest.isCompleteMigration()) {
					pathCheck=AcceleratorUtils.checkServerLocation(importerServiceRequest.getSourceRootPath());
					isValidPath = isValidPath & pathCheck;
					if(!pathCheck){
						statusDescription
						.append("Content Importer Source Path does not exist on the server;");
					}
				}
				if (importerServiceRequest.getFileDestinationRootPath() == null
						|| StringUtils.trim(
								importerServiceRequest
										.getFileDestinationRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Content Importer File Archiving Folder Path Information Missing;");
				}
				else if(!importerServiceRequest.isCompleteMigration()) {
					pathCheck = AcceleratorUtils.checkServerLocation(importerServiceRequest.getFileDestinationRootPath());
					isValidPath = isValidPath & pathCheck;
					if(!pathCheck){
						statusDescription
						.append("Content Importer Destination Path does not exist on the server;");
					}
				}
				if (importerServiceRequest.getImportOrder() == null
						|| StringUtils.trim(
								importerServiceRequest.getImportOrder())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Content Importer Import-Order Information Missing;");
				}

				if (importerServiceRequest.getPageStoragePath() == null
						|| StringUtils.trim(
								importerServiceRequest.getPageStoragePath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
					.append("Content Importer Parent Page Path Information Missing or it does not exist on CRX location;");
				}
				else if(!importerServiceRequest.isCompleteMigration()) {
					pathCheck = AcceleratorUtils.checkCRXLocation(importerServiceRequest.getPageStoragePath());
					isValidPath = isValidPath & pathCheck;
					if(!pathCheck){
						statusDescription
						.append("Content Importer Parent Page Path Information Missing or it does not exist on CRX location;");
					}
				}
				if (importerServiceRequest.getTaxonomyStoragePath() == null
						|| StringUtils
								.trim(importerServiceRequest
										.getTaxonomyStoragePath()).isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Content Importer Taxonomy Path Information Missing or it does not exist on CRX location;");
				}
				else if(!importerServiceRequest.isCompleteMigration()) {
					String tagPath=AcceleratorTaxonomyUtilis.getMappedTagPath(importerServiceRequest.getTaxonomyStoragePath());
					if(StringUtils.isEmpty(tagPath)){
						pathCheck = false;
					}
					isValidPath = isValidPath & pathCheck;
					if(!pathCheck){
						statusDescription
						.append("Content Importer Taxonomy Path Information Missing or it does not exist on CRX location;");
					}
				}
				if (importerServiceRequest.getDamStoragePath() == null
						|| StringUtils.trim(
								importerServiceRequest.getDamStoragePath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Content Importer Parent DAM Path Information Missing or it does not exist on CRX location;");
				}
				else if(!importerServiceRequest.isCompleteMigration()) {
					pathCheck = AcceleratorUtils.checkCRXLocation(importerServiceRequest.getPageStoragePath());
					isValidPath = isValidPath & pathCheck;
					if(!pathCheck){
						statusDescription
						.append("Content Importer Parent DAM Path Information Missing or it does not exist on CRX location;");
					}
				}
			} else {
				parameterPresent = false;
				statusDescription
						.append("Content Import request information missing;");
			}
			if (!parameterPresent || !isValidPath) {
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
					"An exception has occured in ContentImporterService.execute()",
					e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					ContentImporterService.class.getName(),
					"execute(AcceleratorServiceRequest serviceRequest)",
					e.getCause());

		}
		LOGGER.debug(" || " + methodName + " || responseObject || "
				+ responseObject);
		LOGGER.info(" || " + methodName + " || END");
		return responseObject;
	}
}
