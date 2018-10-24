package com.cts.accelerators.migration.services;

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
import com.cts.accelerators.migration.helpers.DamUploadHelper;
import com.cts.accelerators.migration.helpers.ReportGeneratorHelper;
import com.cts.accelerators.migration.services.dto.DamUploadServiceRequest;
import com.cts.accelerators.migration.services.dto.DamUploadServiceResponse;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceRequest;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceResponse;

@Component(metatype = true, immediate = true, enabled = true)
@Service(DamUploadService.class)
/**
 * This class is used for uploading bulk of Dam Asset files
 * @author cognizant
 *
 */
public class DamUploadService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DamUploadService.class);
	private static final String CLASS_NAME = DamUploadService.class.getName();

	@Reference
	private ConfigurationUtil configurationUtil;

	@Reference
	public ReportGeneratorService reportService;

	// Server properties
	@Property(label = "Host", name = "host", unbounded = PropertyUnbounded.DEFAULT, description = "Host name of the server to which to migrate the contents")
	private static final String VALUE1 = "host";

	@Property(label = "Port", name = "port", unbounded = PropertyUnbounded.DEFAULT, description = "Port on which the server is running")
	private static final String VALUE2 = "port";

	@Property(label = "Username", name = "username", unbounded = PropertyUnbounded.DEFAULT, description = "Username to be used for creating contents")
	private static final String VALUE3 = "username";

	@Property(label = "Password", name = "password", unbounded = PropertyUnbounded.DEFAULT, description = "Password for the provided username")
	private static final String VALUE4 = "password";

	// Folder Settings
	@Property(label = "Source Root Folder", name = "sourceRootFolder", unbounded = PropertyUnbounded.DEFAULT, description = "Source root folder containing digital asset to be migrated")
	private static final String VALUE5 = "sourceRootFolder";

	@Property(label = "File Destination Folder", name = "fileDestinationFolder", unbounded = PropertyUnbounded.DEFAULT, description = "Destination root path where to store input XMLs from reference later during the testing phase.")
	private static final String VALUE6 = "fileDestinationFolder";

	@Property(label = "Destination Folder in CQ", name = "cqDestinationFolder", unbounded = PropertyUnbounded.DEFAULT, description = "Destination root path in CQ Repository")
	private static final String VALUE7 = "cqDestinationFolder";

	// Proxy Configurations
	@Property(label = "Enable Proxy?", boolValue = false, description = "Please check this if you need to go through proxy settings to acquire images.")
	public static final String VALUE8 = "enableProxy";

	@Property(label = "Proxy hostname", name = "proxyhost", unbounded = PropertyUnbounded.DEFAULT, description = "Proxy host name")
	private static final String VALUE9 = "proxyhost";

	@Property(label = "Proxy Port", name = "proxyPort", value = "0000", unbounded = PropertyUnbounded.DEFAULT, description = "Proxy Port")
	private static final String VALUE10 = "proxyPort";

	@Property(label = "Proxy Username", name = "proxyUsername", unbounded = PropertyUnbounded.DEFAULT, description = "Proxy user name")
	private static final String VALUE11 = "proxyUsername";

	@Property(label = "Proxy Password", name = "proxyPassword", unbounded = PropertyUnbounded.DEFAULT, description = "Proxy Password")
	private static final String VALUE12 = "proxyPassword";

	@Activate
	protected void activate() {
		LOGGER.info("DAM Upload service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("DAM Upload service stopped");
	}

	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject validationResponse = new JSONObject();
		DamUploadServiceResponse damBulkResponse = new DamUploadServiceResponse();
		ReportGeneratorServiceRequest reportRequest = new ReportGeneratorServiceRequest();

		// Get DAM request object
		DamUploadServiceRequest damBulkRequest = null;
		if (serviceRequest instanceof DamUploadServiceRequest) {
			damBulkRequest = (DamUploadServiceRequest) serviceRequest;
			if (damBulkRequest.isLoadDefault()) {
				damBulkRequest = setConfigProperties(damBulkRequest);
			}
		}

		// Get reports path
		if (!damBulkRequest.isCompleteMigration()) {
			reportRequest.setReportType(AcceleratorGenericConstants.DAM_REPORT);
			reportRequest.setLoadDefault(true);

			Map<String, String> reportFilePaths = new HashMap<String, String>();
			reportFilePaths = ((ReportGeneratorServiceResponse) reportService
					.execute(reportRequest)).getReportFilePaths();

			damBulkRequest.setReportFilePath(reportFilePaths
					.get(AcceleratorGenericConstants.DAM_REPORT));
		}

		// Validate input parameters
		validationResponse = validateRequiredParameters(damBulkRequest);
		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
		} catch (JSONException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		}

		LOGGER.debug(" || " + methodName + " validationStatus "
				+ validationStatus);
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(validationStatus)) {
			LOGGER.debug(" || " + methodName
					+ " Before entering damBulkUpload in helper "
					+ damBulkResponse);
			damBulkResponse = DamUploadHelper.damBulkUpload(damBulkRequest,
					damBulkRequest.getReportFilePath());
			LOGGER.debug(" || " + methodName
					+ " After damBulkUpload in helper "
					+ damBulkResponse.getJsonResponse());
		} else {
			damBulkResponse.setJsonResponse(validationResponse);
		}
		LOGGER.debug(" || "
				+ methodName
				+ " || Report request path || "
				+ damBulkRequest.getReportFilePath().replace(
						AcceleratorGenericConstants.CSV_SEPARATOR,
						AcceleratorGenericConstants.FORWARD_SLASH));

		ReportGeneratorHelper
				.addToPropertyFile(
						reportRequest
								.getReportRootPath()
								.substring(
										0,
										reportRequest
												.getReportRootPath()
												.lastIndexOf(
														AcceleratorGenericConstants.BACKWARD_SLASH)),
						AcceleratorGenericConstants.IMPORT_TYPE_DAM);

		LOGGER.info(" || " + methodName + " || END");
		return damBulkResponse;
	}

	/**
	 * This method will be used for getting configurations like the port and
	 * credential information which need not be asked from the authors.
	 * 
	 * @param damBulkRequest
	 * @return
	 * @throws AcceleratorException
	 */
	private DamUploadServiceRequest setConfigProperties(
			DamUploadServiceRequest damBulkRequest) throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || " + methodName + " || START");

		String PID = "com.cts.accelerators.migration.services.DamUploadService";

		// Server Properties
		if (damBulkRequest.getLocalhost() == null
				|| StringUtils.trim(damBulkRequest.getLocalhost()).isEmpty()) {
			damBulkRequest.setLocalhost(configurationUtil
					.getConfig(PID, VALUE1));
			LOGGER.info(" || " + methodName + " || Localhost -> "
					+ damBulkRequest.getLocalhost());
		}
		if (damBulkRequest.getPort() == null
				|| StringUtils.trim(damBulkRequest.getPort()).isEmpty()) {
			damBulkRequest.setPort(configurationUtil.getConfig(PID, VALUE2));
			LOGGER.info(" || " + methodName + " || Port -> "
					+ damBulkRequest.getPort());
		}
		if (damBulkRequest.getUsername() == null
				|| StringUtils.trim(damBulkRequest.getUsername()).isEmpty()) {
			damBulkRequest
					.setUsername(configurationUtil.getConfig(PID, VALUE3));
			LOGGER.info(" || " + methodName + " || username -> "
					+ damBulkRequest.getUsername());
		}
		if (damBulkRequest.getPassword() == null
				|| StringUtils.trim(damBulkRequest.getPassword()).isEmpty()) {
			damBulkRequest
					.setPassword(configurationUtil.getConfig(PID, VALUE4));
			LOGGER.info(" || " + methodName + " || password -> "
					+ damBulkRequest.getPassword());
		}

		// Folder configurations
		if (damBulkRequest.getSourceRootPath() == null
				|| StringUtils.trim(damBulkRequest.getSourceRootPath())
						.isEmpty()) {
			damBulkRequest.setSourceRootPath(configurationUtil.getConfig(PID,
					VALUE5));
		}
		if (damBulkRequest.getCqRootPath() == null
				|| StringUtils.trim(damBulkRequest.getCqRootPath()).isEmpty()) {
			damBulkRequest.setCqRootPath(configurationUtil.getConfig(PID,
					VALUE6));
		}
		if (damBulkRequest.getFileDestinationRootPath() == null
				|| StringUtils
						.trim(damBulkRequest.getFileDestinationRootPath())
						.isEmpty()) {
			damBulkRequest.setFileDestinationRootPath(configurationUtil
					.getConfig(PID, VALUE7));
		}
		damBulkRequest.setEnableProxy(configurationUtil.getBooleanConfig(PID,
				VALUE8));
		// Proxy Properties
		if (damBulkRequest.isEnableProxy()) {
			damBulkRequest.setEnableProxy(configurationUtil.getBooleanConfig(
					PID, VALUE8));
			if (damBulkRequest.getProxyHost() == null
					|| StringUtils.trim(damBulkRequest.getProxyHost())
							.isEmpty()) {
				damBulkRequest.setProxyHost(configurationUtil.getConfig(PID,
						VALUE9));
			}
			damBulkRequest.setProxyPort(new Integer(configurationUtil
					.getConfig(PID, VALUE10)));
			if (damBulkRequest.getProxyUsername() == null
					|| StringUtils.trim(damBulkRequest.getProxyUsername())
							.isEmpty()) {
				damBulkRequest.setProxyUsername(configurationUtil.getConfig(
						PID, VALUE11));
			}
			if (damBulkRequest.getProxyPassword() == null
					|| StringUtils.trim(damBulkRequest.getProxyPassword())
							.isEmpty()) {
				damBulkRequest.setProxyPassword(configurationUtil.getConfig(
						PID, VALUE12));
			}
		}

		LOGGER.info(" || " + methodName + " || END");
		return damBulkRequest;
	}

	/**
	 * This method will be used by the external services to get all the
	 * configuration set for DAM Upload service
	 * 
	 * @param damBulkRequest
	 * @return
	 * @throws AcceleratorException
	 */
	public DamUploadServiceRequest getConfigProperties(
			DamUploadServiceRequest damBulkRequest) throws AcceleratorException {
		String methodName = "getConfigProperties";
		LOGGER.info(" || " + methodName + " || START");
		damBulkRequest = setConfigProperties(damBulkRequest);
		LOGGER.info(" || " + methodName + " || END");
		return damBulkRequest;
	}

	/**
	 * This method will be used for validating if all the required parameters
	 * are present or not and sending appropriate response in case of missing
	 * parameters.
	 * 
	 * @param bulkUploadRequest
	 * @return
	 * @throws AcceleratorException
	 */
	private JSONObject validateRequiredParameters(
			DamUploadServiceRequest damBulkRequest) throws AcceleratorException {
		String methodName = "validateRequiredParameters";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true;
		boolean isValidPath = true;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (damBulkRequest != null) {
				// Server Properties
				if (damBulkRequest.getLocalhost() == null
						|| StringUtils.trim(damBulkRequest.getLocalhost())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription.append("Server hostname missing;");
				}
				if (damBulkRequest.getPort() == null
						|| StringUtils.trim(damBulkRequest.getPort()).isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Server port information missing;");
				}
				if (damBulkRequest.getUsername() == null
						|| StringUtils.trim(damBulkRequest.getUsername())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Username missing for accessing the server;");
				}
				if (damBulkRequest.getPassword() == null
						|| StringUtils.trim(damBulkRequest.getPassword())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Password missing for the provided username;");
				}

				// Folder configurations
				if (damBulkRequest.getSourceRootPath() == null
						|| StringUtils.trim(damBulkRequest.getSourceRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Source Path Information Missing;");
				} else {
					isValidPath = AcceleratorUtils
							.checkServerLocation(damBulkRequest
									.getSourceRootPath());
					if (!isValidPath) {
						parameterPresent = false;
						statusDescription.append("Source Path Does Not Exist;");
					}
				}
				if (damBulkRequest.getCqRootPath() == null
						|| StringUtils.trim(damBulkRequest.getCqRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("CQ Repository Path Information Missing;");
				} else {
					try {
						isValidPath = AcceleratorUtils
								.checkCRXLocation(damBulkRequest
										.getCqRootPath());
						if (!isValidPath) {
							parameterPresent = false;
							statusDescription
									.append("CQ Repository Path Does Not Exist in CRX;");
						}
					} catch (AcceleratorException e) {
						LOGGER.error("Accelerator Exception thrown from "
								+ methodName + " || " + e.getMessage());
						parameterPresent = false;
						statusDescription
								.append("CQ Repository Path is Not a Valid CRX Path;");
					}
				}
				if (damBulkRequest.getFileDestinationRootPath() == null
						|| StringUtils.trim(
								damBulkRequest.getFileDestinationRootPath())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("File Destination Path Information Missing;");
				} else {
					isValidPath = AcceleratorUtils
							.checkServerLocation(damBulkRequest
									.getFileDestinationRootPath());
					if (!isValidPath) {
						parameterPresent = false;
						statusDescription
								.append("File Destination Path Does Not Exist;");
					}
				}

				// Check Proxy Properties only if proxy enabled
				if (damBulkRequest.isEnableProxy()) {
					if (damBulkRequest.getProxyHost() == null
							|| StringUtils.trim(damBulkRequest.getProxyHost())
									.isEmpty()) {
						parameterPresent = false;
						statusDescription.append("Proxy hostname missing;");
					}
					if (new Integer(damBulkRequest.getProxyPort()).intValue() == 0) {
						parameterPresent = false;
						statusDescription.append("Proxy port missing;");
					}
					if (damBulkRequest.getProxyUsername() == null
							|| StringUtils.trim(
									damBulkRequest.getProxyUsername())
									.isEmpty()) {
						parameterPresent = false;
						statusDescription.append("Proxy user name missing;");
					}
					if (damBulkRequest.getProxyPassword() == null
							|| StringUtils.trim(
									damBulkRequest.getProxyPassword())
									.isEmpty()) {
						parameterPresent = false;
						statusDescription.append("Proxy Password missing;");
					}
				}
			} else {
				parameterPresent = false;
				statusDescription.append("DAM request information missing;");
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
