package com.cts.accelerators.migration.services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.osgi.service.component.ComponentContext;
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
import com.cts.accelerators.migration.helpers.ReportGeneratorHelper;
import com.cts.accelerators.migration.helpers.TransformationHelper;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceResponse;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceRequest;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceResponse;

@Component(metatype = true, immediate = true)
@Service(value = ContentTransformerService.class)
/**
 * This class will Transform file from one form to another form
 * @author cognizant
 *
 */
public class ContentTransformerService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentTransformerService.class);
	private static final String CLASS_NAME = ContentTransformerService.class
			.getName();

	@Reference
	private ConfigurationUtil configurationUtil;

	@Reference
	public ReportGeneratorService reportService;

	@Property(label = "Transformation Order", name = "transformationOrder", unbounded = PropertyUnbounded.DEFAULT, value = "images,taxonomy,page", description = "Comma separated transformation order in which to read and tranform XML files.")
	private static final String VALUE1 = "transformationOrder";

	@Property(label = "Source Root Path", name = "sourcePath", description = "Source Content Path")
	private static final String VALUE2 = "sourcePath";

	@Property(label = "Mapping File", name = "mappingFile", description = "Mapping file for converting from source format to destination format")
	private static final String VALUE3 = "mappingFile";

	@Property(label = "Destination Root Path", name = "destinationRootPath", unbounded = PropertyUnbounded.DEFAULT, description = "Destination Folder that in turn will contain successusfully and failed transformed files")
	private static final String VALUE4 = "destinationRootPath";

	@Property(label = "Max File Size Of XML", name = "fileSize", unbounded = PropertyUnbounded.DEFAULT, description = "Maximum size of the XML File in bytes")
	private static final String VALUE5 = "fileSize";

	@Activate
	protected void activate(ComponentContext context) {
		LOGGER.info("Content Transform service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Content Transform service stopped");
	}

	/**
	 * This method will be called by the Migration Manager to start the
	 * transformation service
	 * 
	 * @param serviceRequest
	 *            Request variable containing necessary parameters
	 * @return AcceleratorServiceResponse containing the response
	 * @throws AcceleratorException
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");

		// Get all required parameters
		ContentTransformerServiceRequest transformerRequest = null;
		if (serviceRequest instanceof ContentTransformerServiceRequest) {
			transformerRequest = (ContentTransformerServiceRequest) serviceRequest;
			if (transformerRequest.isLoadDefault()) {
				transformerRequest = setConfigProperties(transformerRequest);
			}
		}

		// Validate if necessary parameters are present
		JSONObject validationResponse = new JSONObject();
		try {
			validationResponse = validateRequiredParameters(transformerRequest);
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCELERATOR_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
		} catch (JSONException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		}

		// Get the reports path if standalone mode
		ReportGeneratorServiceRequest reportRequest = new ReportGeneratorServiceRequest();
		if (!transformerRequest.isCompleteMigration()) {
			reportRequest
					.setReportType(AcceleratorGenericConstants.TRANSFORMATION_REPORT);
			reportRequest.setLoadDefault(true);

			Map<String, String> reportFilePaths = new HashMap<String, String>();
			reportFilePaths = ((ReportGeneratorServiceResponse) reportService
					.execute(reportRequest)).getReportFilePaths();
			transformerRequest.setReportFilePath(reportFilePaths
					.get(AcceleratorGenericConstants.TRANSFORMATION_REPORT));
		}

		// Start transformation and send the response back
		ContentTransformerServiceResponse transformerResponse = new ContentTransformerServiceResponse();
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(validationStatus)) {
			transformerResponse = TransformationHelper
					.transformContents(transformerRequest);
		} else {
			transformerResponse.setJsonResponse(validationResponse);
		}
		LOGGER.info(" || " + methodName + " || END");
		return transformerResponse;
	}

	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param transformServiceRequest
	 * @return Content reader request variable containing configured values
	 * @throws AcceleratorException
	 */
	public ContentTransformerServiceRequest setConfigProperties(
			ContentTransformerServiceRequest transformServiceRequest)
			throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START");
		String pid = "com.cts.accelerators.migration.services.ContentTransformerService";
		if (transformServiceRequest.getTransformationOrder() == null
				|| StringUtils.trim(
						transformServiceRequest.getTransformationOrder())
						.isEmpty()) {
			transformServiceRequest.setTransformationOrder(configurationUtil
					.getConfig(pid, VALUE1));
		}
		if (transformServiceRequest.getSourceRootPath() == null
				|| StringUtils
						.trim(transformServiceRequest.getSourceRootPath())
						.isEmpty()) {
			transformServiceRequest.setSourceRootPath(configurationUtil
					.getConfig(pid, VALUE2));
		}
		if (transformServiceRequest.getMappingFilePath() == null
				|| StringUtils.isEmpty(transformServiceRequest
						.getMappingFilePath())) {
			transformServiceRequest.setMappingFilePath(configurationUtil
					.getConfig(pid, VALUE3));
		}
		if (transformServiceRequest.getDestinationRootPath() == null
				|| StringUtils.trim(
						transformServiceRequest.getDestinationRootPath())
						.isEmpty()) {
			transformServiceRequest.setDestinationRootPath(configurationUtil
					.getConfig(pid, VALUE4));
		}

		if (transformServiceRequest.getFileSize() == null
				|| StringUtils.trim(transformServiceRequest.getFileSize())
						.isEmpty()) {
			transformServiceRequest.setFileSize(configurationUtil.getConfig(
					pid, VALUE5));
		}
		LOGGER.info(" || " + methodName + " || END");
		return transformServiceRequest;
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
			ContentTransformerServiceRequest transformRequest)
			throws AcceleratorException {
		String methodName = "validateRequiredParameters";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true;
		boolean isValidPath = true;
		boolean pathCheck = true;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (transformRequest != null) {
				if (transformRequest.getDestinationRootPath() == null
						|| StringUtils.trim(
								transformRequest.getDestinationRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Transformer Destination Root Information Missing;");
				} else if(!transformRequest.isCompleteMigration()) {
					pathCheck = isValidPath & AcceleratorUtils
							.checkServerLocation(transformRequest
									.getDestinationRootPath());
					isValidPath = isValidPath & pathCheck;
					if (!pathCheck) {
						parameterPresent = false;
						statusDescription
								.append("Transformer Destination Root Path Does Not Exist;");
					}
				}
				if (transformRequest.getTransformationOrder() == null
						|| StringUtils.trim(
								transformRequest.getTransformationOrder())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Transformation Order Information Missing;");
				}

				if (transformRequest.getFileSize() == null
						|| StringUtils.trim(transformRequest.getFileSize())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Max XML File Size is missing from configuration;");

				} else {
					pathCheck = isValidPath & AcceleratorUtils.patternMatching(
							AcceleratorGenericConstants.BYTES_VALUE_PATTERN,
							transformRequest.getFileSize());
					isValidPath = isValidPath & pathCheck;
					if (!pathCheck) {

						parameterPresent = false;
						statusDescription
								.append("Please provide Max File Size in bytes;");
					}
				}
				if (transformRequest.getSourceRootPath() == null
						|| StringUtils.trim(
								transformRequest.getSourceRootPath()).isEmpty()) {
					parameterPresent = false;
					statusDescription.append("Source Folder Path is missing;");
				} else {
					pathCheck = isValidPath & AcceleratorUtils
							.checkServerLocation(transformRequest
									.getSourceRootPath());
					isValidPath = isValidPath & pathCheck;
					if (!pathCheck) {
						parameterPresent = false;
						statusDescription
								.append("Source Folder Path Does Not Exist;");
					}
				}

				if (transformRequest.getTransformationType() != null
						&& !StringUtils.trim(
								transformRequest.getTransformationType())
								.isEmpty() && transformRequest.getTransformationType().equalsIgnoreCase("XML")) {
					if (transformRequest.getMappingFilePath() == null
							|| StringUtils.trim(
									transformRequest.getMappingFilePath())
									.isEmpty()) {
						parameterPresent = false;
						statusDescription
								.append("Transformer File Information Missing;");
					} else {
						File file = new File(
								transformRequest.getMappingFilePath());
						if (!file.exists() || (file.exists() && !file.isFile())) {
							LOGGER.debug(" || " + methodName
									+ " || XSLT FILE NOT THERE");
							parameterPresent = false;
							statusDescription
									.append("Transformer XSLT File Does Not Exist or Filename is Invalid. Please Provide Valid Filename with Extension .xsl;");
						}
					}
				}
			} else {
				parameterPresent = false;
				statusDescription
						.append("Content Transformation request information missing;");
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
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());

		}
		LOGGER.info(" || " + methodName + " || END");
		return responseObject;
	}
}
