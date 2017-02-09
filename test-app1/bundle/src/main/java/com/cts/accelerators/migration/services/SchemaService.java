package com.cts.accelerators.migration.services;

/**
 * This service class is used to generate the XSD schema needed for the application.
 * 
 * @author Cognizant
 */

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyOption;
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
import com.cts.accelerators.migration.helpers.SchemaHelper;
import com.cts.accelerators.migration.services.dto.SchemaServiceRequest;
import com.cts.accelerators.migration.services.dto.SchemaServiceResponse;

@Component(metatype = true, immediate = true)
@Service(value = SchemaService.class)
public class SchemaService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SchemaService.class);
	private static final String CLASS_NAME = SchemaService.class.getName();

	@Reference
	private ConfigurationUtil configurationUtil;

	@Property(label = "Schema Service Request Type", name = "xsdRequestType", options = {
	@PropertyOption(name = "XSD", value = "XSD") })
	private static final String VALUE1 = "xsdRequestType";

	@Property(label = "Storage Path", name = "destinationRootPath", unbounded = PropertyUnbounded.DEFAULT, description = "Destination root path where to store input XMLs from reference later during the testing phase.")
	private static final String VALUE2 = "destinationRootPath";

	@Property(label = "Move To", name = "moveTo", options = {
			@PropertyOption(name = "CRX", value = "CRX"),
			@PropertyOption(name = "fileSystem", value = "File System") })
	private static final String VALUE3 = "moveTo";

	@Activate
	protected void activate() {
		LOGGER.info("Schema service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Schema service stopped");
	}

	/**
	 * This method overrides the super service class execute method and start
	 * the process for XSD generation.
	 * 
	 * @param xsdRequest
	 *            - contains the properties set at request initiation time
	 * @return AceeleratorServiceResponse - containing any information to be
	 *         returned to the calling program
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "execute";
		LOGGER.info(" || " + methodName + " || START");
		
		JSONObject validationResponse = new JSONObject();
		SchemaServiceResponse xslServiceResponse = new SchemaServiceResponse();
		SchemaHelper xslHelper = new SchemaHelper();
		SchemaServiceRequest xslServiceRequest = null;
		if (serviceRequest instanceof SchemaServiceRequest) {
			xslServiceRequest = (SchemaServiceRequest) serviceRequest;
			if (xslServiceRequest.isLoadDefault()) {
				xslServiceRequest = setConfigProperties(xslServiceRequest);
			}
		}

		validationResponse = validateRequiredParameters(xslServiceRequest);
		String validationStatus = AcceleratorGenericConstants.STATUS_FAILURE;
		try {
			validationStatus = validationResponse
					.getString(AcceleratorGenericConstants.STATUS);
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
			xslServiceResponse = xslHelper.getResponse(xslServiceRequest);
		} else {
			xslServiceResponse.setJsonResponse(validationResponse);
		}

		LOGGER.info(" || " + methodName + " || END");
		return xslServiceResponse;
	}

	/**
	 * This method will be used to get the values set in service configurations
	 * 
	 * @param xslServiceRequest
	 * @return Content reader request variable containing configured values
	 * @throws AcceleratorException
	 */
	private SchemaServiceRequest setConfigProperties(
			SchemaServiceRequest xslServiceRequest) throws AcceleratorException {
		String methodName = "setConfigProperties";
		LOGGER.info(" || "+methodName+" || START ");
		String PID = "com.cts.accelerators.migration.services.XSLService";
		if (xslServiceRequest.getMoveTo() == null
				|| xslServiceRequest.getMoveTo().equalsIgnoreCase("")) {
			if (xslServiceRequest.getStoragePath() == null
					|| xslServiceRequest.getStoragePath().equalsIgnoreCase("")) {
				if (xslServiceRequest.getRequestType() == null
						|| xslServiceRequest.getRequestType().equalsIgnoreCase(
								"")) {
					xslServiceRequest.setRequestType(configurationUtil
							.getConfig(PID, VALUE1));
					xslServiceRequest.setStoragePath(configurationUtil
							.getConfig(PID, VALUE2));
					xslServiceRequest.setMoveTo(configurationUtil.getConfig(
							PID, VALUE3));
				}
			}
		}
		LOGGER.info(" || "+methodName+" || END");
		return xslServiceRequest;
	}

	/**
	 * This method is used to validate the XSD format parameters
	 * 
	 * @param xsdRequest
	 * @return responseObject
	 * @throws AcceleratorException
	 */
	private JSONObject validateRequiredParameters(
			SchemaServiceRequest xsdRequest) throws AcceleratorException {
		String methodName = "validateRequiredParameters";
		LOGGER.info(" || " + methodName + " || START");
		boolean parameterPresent = true;
		boolean isValidPath = true;
		StringBuffer statusDescription = new StringBuffer();
		JSONObject responseObject = new JSONObject();
		try {
			if (xsdRequest != null) {
				if (xsdRequest.getMoveTo() == null
						|| StringUtils.trim(xsdRequest.getMoveTo())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Move to Path Information is Missing;");
				}
				if (xsdRequest.getRequestType() == null
						|| StringUtils.trim(xsdRequest.getRequestType())
								.isEmpty()) {
					parameterPresent = false;
					statusDescription
							.append("Request Type is Information is Missing;");
				}
				
				if (xsdRequest.getMoveTo().equalsIgnoreCase(
						AcceleratorGenericConstants.CRX)) {
					if (xsdRequest.getStoragePath() == null
							|| StringUtils.trim(xsdRequest.getStoragePath())
									.isEmpty()) {
						parameterPresent = false;
						statusDescription
								.append("Storage Path  Information is Missing;");
					} else {
						try {
							isValidPath = AcceleratorUtils.checkCRXLocation(xsdRequest.getStoragePath());
							if (!isValidPath) {
								parameterPresent = false;
								statusDescription
										.append("Storage Path Does Not Exist in CRX;");
							}
						} catch (AcceleratorException e) {
							LOGGER.error("Accelerator Exception thrown from "+methodName+" || "+e.getMessage());
							parameterPresent = false;
							statusDescription
									.append("Storage Path is Not a Valid CRX Path;");
						}
					}
				}

			} else {
				parameterPresent = false;
				statusDescription
						.append("XSL Service request information missing;");
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
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME,
					methodName,
					e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return responseObject;
	}

}
