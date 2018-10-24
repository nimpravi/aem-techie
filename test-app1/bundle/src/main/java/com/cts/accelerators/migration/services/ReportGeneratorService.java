package com.cts.accelerators.migration.services;

/**
 * 
 */
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
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceRequest;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceResponse;

@Component(metatype = true, immediate = true)
@Service(value = ReportGeneratorService.class)
/**
 * This Class is used for generating Reports for flow that we are using
 * @author Cognizant
 *
 */
public class ReportGeneratorService implements AcceleratorService {

	private static final String CLASS_NAME = ReportGeneratorService.class
			.getName();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportGeneratorService.class);

	@Reference
	private ConfigurationUtil configurationUtil;

	@Property(label = "Report Root Path", name = "reportRootPath", unbounded = PropertyUnbounded.DEFAULT, description = "File Path")
	private static final String VALUE2 = "reportRootPath";

	/**
	 * activate method
	 */
	@Activate
	protected void activate(ComponentContext context) {
		LOGGER.info(" || START (activate)");
	}

	/**
	 * deactivate method
	 */
	@Deactivate
	protected void deactivate() {
		LOGGER.info(" || END (deactivate)");
	}

	/**
	 * This method overrides the super service class execute method and start
	 * the process for report generating.
	 * 
	 * @param reportRequest
	 *            - contains the properties set at migration initiation time
	 * @return AceeleratorServiceResponse - containing any information to be
	 *         returned to the calling program
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.debug(" ||" + methodName + " || START");

		JSONObject validationResponse = new JSONObject();

		ReportGeneratorServiceRequest reportRequest = new ReportGeneratorServiceRequest();
		ReportGeneratorServiceResponse reportResponse = new ReportGeneratorServiceResponse();
		if (serviceRequest instanceof ReportGeneratorServiceRequest) {
			reportRequest = (ReportGeneratorServiceRequest) serviceRequest;
			if (reportRequest.isLoadDefault()) {
				reportRequest = setConfigProperties(reportRequest);
			}
		}

		LOGGER.debug(" ||" + methodName + " || DATE || "
				+ reportRequest.getReportDate() + " || REPORT TYPE || "
				+ reportRequest.getReportType() + " || REPORT DETAILS TYPE || "
				+ reportRequest.getReportDetailedType()
				+ " || IS SPECIFIC REPORT || "
				+ reportRequest.isSpecificReport());

		try {
			validationResponse = validateRequiredParameters(reportRequest);
			LOGGER.debug(" ||" + methodName
					+ " || validationResponse || " + validationResponse);
		} catch (JSONException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME,
					methodName,
					e.getCause());
		}

		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
			LOGGER.debug(" ||" + methodName
					+ " || validationStatus || " + validationStatus);
		} catch (JSONException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME,
					methodName,
					e.getCause());
		}
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(validationStatus)) {
			if (reportRequest.isViewReport()) {
				reportResponse.setReportJsonResponse(ReportGeneratorHelper
						.getReportViewingResponse(reportRequest));
			} else {
				reportRequest.setReportRootPath(ReportGeneratorHelper
						.createReportFolder(reportRequest.getReportRootPath(),
								reportRequest.getReportType()));
				reportResponse.setReportFilePaths(ReportGeneratorHelper
						.configureRequiredReportPath(reportRequest));
				ReportGeneratorHelper.createEmptyPropertyFiles();
			}
		} else {
			reportResponse.setReportJsonResponse(validationResponse);
		}
		LOGGER.debug(" ||" + methodName + " || reportResponse || "
				+ reportResponse);
		LOGGER.info(" ||" + methodName + " || END");
		return reportResponse;
	}

	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param readerServiceRequest
	 * @return Content reader request variable containing configured values
	 * @throws AcceleratorException
	 */
	public ReportGeneratorServiceRequest setConfigProperties(
			ReportGeneratorServiceRequest reportRequest)
			throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" ||" + methodName + " || START");

		String PID = "com.cts.accelerators.migration.services.ReportGeneratorService";

		if (reportRequest.getReportRootPath() == null
				|| StringUtils.trim(reportRequest.getReportRootPath())
						.isEmpty()) {
			reportRequest.setReportRootPath(configurationUtil.getConfig(PID,
					VALUE2));
		}

		LOGGER.info(" ||" + methodName + " || END");
		return reportRequest;
	}

	/**
	 * This method is used to validate the report generator parameters 
	 * 
	 * @param reportRequest
	 * @return responseObject
	 * @throws AcceleratorException
	 * @throws JSONException
	 */
	public JSONObject validateRequiredParameters(
			ReportGeneratorServiceRequest reportRequest)
			throws AcceleratorException, JSONException {
		String methodName = "validateRequiredParameters";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true,isValidPath=false;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (reportRequest != null) {
				if (reportRequest != null) {
					// if report path missing in felix console, the take default report path
					if (reportRequest.getReportRootPath() == null
							|| StringUtils.trim(
									reportRequest.getReportRootPath())
									.isEmpty()) {
						reportRequest.setReportRootPath(System.getProperty(AcceleratorGenericConstants.USER_DIR)+AcceleratorGenericConstants.FORWARD_SLASH+AcceleratorGenericConstants.DEFAULT_REPORT_PATH);
						LOGGER.debug(" || " + methodName + " || Report Root Path Information Missing || Taking Default Report Path");
						
					}
					else{
						isValidPath=AcceleratorUtils.checkServerLocation(reportRequest.getReportRootPath());
					}
				}
				if (!isValidPath) {
					responseObject.put(AcceleratorGenericConstants.STATUS,
							AcceleratorGenericConstants.STATUS_FAILURE);
					statusDescription.append("The report root path does not exist on the server;");
					responseObject.put(AcceleratorGenericConstants.DESCRIPTION,
							statusDescription.toString());
				} else {
					responseObject.put(AcceleratorGenericConstants.STATUS,
							AcceleratorGenericConstants.STATUS_SUCCESS);

				}

			} else {
				parameterPresent = false;
				statusDescription
						.append("ReportGeneratorService request information missing;");
				LOGGER.debug(" || " + methodName + " || Report Root Path Information Missing");
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
		LOGGER.info(" || " + methodName + " || END");
		return responseObject;

	}
}
