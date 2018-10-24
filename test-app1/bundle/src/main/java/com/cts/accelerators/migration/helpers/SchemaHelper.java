package com.cts.accelerators.migration.helpers;

/**
 * This helper class is used for Schema Service to carry out the generation of the XSD schema.
 * 
 * @author Cognizant
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.util.AcceleratorFileUtils;
import com.cts.accelerators.core.util.AcceleratorUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.ootbcomps.dto.ContainerDTO;
import com.cts.accelerators.migration.services.dto.SchemaServiceRequest;
import com.cts.accelerators.migration.services.dto.SchemaServiceResponse;

public class SchemaHelper {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SchemaHelper.class);
	private static final String CLASS_NAME = SchemaHelper.class.getName();

	/**
	 * This method is called by the service to get the request response.
	 * 
	 * @param schemaRequest
	 * @return serviceResponse
	 * @throws AcceleratorException
	 * 
	 */
	public SchemaServiceResponse getResponse(SchemaServiceRequest schemaRequest)
			throws AcceleratorException {
		String methodName = "getResponse";
		LOGGER.info(" || " + methodName + " || START");
		SchemaServiceResponse serviceResponse = new SchemaServiceResponse();
		LOGGER.debug(" || " + methodName + "  || request Type "
				+ schemaRequest.getRequestType());
		LOGGER.debug(" || " + methodName + "  || move to "
				+ schemaRequest.getMoveTo());
		LOGGER.debug(" || " + methodName + "  || destination path  "
				+ schemaRequest.getStoragePath());

		LOGGER.debug(" || "
				+ methodName
				+ "  || boolean value "
				+ AcceleratorGenericConstants.GENERATE_SAMPLE_XSD
						.equals(schemaRequest.getRequestType()));
		if (schemaRequest != null
				&& schemaRequest.getRequestType() != null
				&& AcceleratorGenericConstants.GENERATE_SAMPLE_XSD
						.equals(schemaRequest.getRequestType())) {
			serviceResponse = generateSchema(schemaRequest);
		} else if (schemaRequest != null
				&& schemaRequest.getRequestType() != null
				&& AcceleratorGenericConstants.XSL.equals(schemaRequest
						.getRequestType())) {
			serviceResponse = generateXSL(schemaRequest);
		}
		LOGGER.info(" || " + methodName + " || END");
		return serviceResponse;
	}

	/**
	 * This method generate the XSD file at the destination specified in the
	 * request or the configuration parameter.
	 * 
	 * @param schemaRequest
	 * @return serviceResponse
	 * @throws AcceleratorException
	 */
	private SchemaServiceResponse generateSchema(
			SchemaServiceRequest schemaRequest) throws AcceleratorException {
		String methodName = "generateSchema";
		LOGGER.info(" || " + methodName + " || START");
		SchemaServiceResponse serviceResponse = new SchemaServiceResponse();

		JAXBContext jc;
		JSONObject jsonResponse = new JSONObject();
		boolean deleteFileStatus = false;
		try {
			jc = JAXBContext.newInstance(ContainerDTO.class);
			deleteFileStatus = deleteFile(schemaRequest);
			if (deleteFileStatus) {
				if (schemaRequest != null
						&& AcceleratorGenericConstants.FILE_SYSTEM
								.equals(schemaRequest.getMoveTo())) {
					jsonResponse = generateAndCopyToFS(jc, schemaRequest);
				} else {

					jsonResponse = generateAndCopyToCRX(jc, schemaRequest);
				}
			} else {
				jsonResponse.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_FAILURE);
				jsonResponse
						.put(AcceleratorGenericConstants.DESCRIPTION,
								"Unable to delete already Existing file please check the logs;");
				jsonResponse.put("XSD_FilePath", "");
			}
		} catch (JAXBException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (JSONException jsonE) {
			LOGGER.error("An exception has occured in " + methodName, jsonE);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, jsonE.getCause());
		}

		// Set the service response
		serviceResponse.setJsonResponse(jsonResponse);

		LOGGER.info(" || " + methodName + " || END");
		return serviceResponse;
	}

	/**
	 * This method will delete the existing file from the fileSystem and CRX
	 * 
	 * @param schemaRequest
	 * @return boolean
	 * @throws AcceleratorException
	 */
	private boolean deleteFile(SchemaServiceRequest schemaRequest) {
		boolean deleteFileStatus;
		String methodName = "deleteFile";
		LOGGER.info(" || " + methodName + " || START");
		try {
			String deleteFile = null;
			String filePath = null;
			if (schemaRequest != null
					&& AcceleratorGenericConstants.FILE_SYSTEM
							.equals(schemaRequest.getMoveTo())) {
				LOGGER.debug(" || " + methodName + "  || request Type "
						+ schemaRequest.getRequestType());
				deleteFile = AcceleratorGenericConstants.TEMP_FILE_PATH;

			} else if (schemaRequest != null
					&& AcceleratorGenericConstants.CRX.equals(schemaRequest
							.getMoveTo())) {

				LOGGER.debug(" || " + methodName + "  || request Type "
						+ schemaRequest.getRequestType());
				filePath = AcceleratorUtils.replaceSlashInPath(schemaRequest
						.getStoragePath());

				deleteFile = filePath;
			}

			deleteFileStatus = AcceleratorFileUtils.deleteFile(
					AcceleratorGenericConstants.XSD_FILE_NAME, deleteFile);
		} catch (AcceleratorException e) {
			deleteFileStatus = false;
		}
		LOGGER.info(" || " + methodName + " || END");
		return deleteFileStatus;
	}

	/**
	 * This method is called for generate XSD and copy to File System
	 * 
	 * @param jc
	 * @param schemaRequest
	 *            return jsonResponse
	 * @throws AcceleratorException
	 * 
	 */
	private JSONObject generateAndCopyToFS(JAXBContext jc,
			SchemaServiceRequest schemaRequest) throws AcceleratorException {
		String methodName = "generateAndCopyToFS";
		LOGGER.info(" || " + methodName + " || START");
		String outputFileName = AcceleratorGenericConstants.XSD_FILE_NAME;
		String storagePath = AcceleratorGenericConstants.TEMP_FILE_PATH;
		File sourceFile = new File(outputFileName);

		JSONObject jsonResponse = new JSONObject();
		try {
			boolean isCreated = jaxBXSDConversion(jc, outputFileName);
			if (isCreated) {
				boolean isMovedToTemp = AcceleratorFileUtils
						.moveOrCopyFileInCRX(sourceFile,
								AcceleratorGenericConstants.XSD_FILE_NAME,
								storagePath, false);
				jsonResponse = getJSONResponse(isMovedToTemp, storagePath
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ AcceleratorGenericConstants.XSD_FILE_NAME);
			}
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			try {
				jsonResponse.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_FAILURE);
				jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION,
						"Failed to move or Copy File to FileSystem;");
				jsonResponse.put("XSD_FilePath", "");

			} catch (JSONException jsonE) {
				LOGGER.error("An exception has occured in " + methodName, jsonE);
				throw new AcceleratorException(
						AcceleratorFaultCode.JSON_EXCEPTION, CLASS_NAME,
						methodName, jsonE.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	/**
	 * This method is called for generate XSD and copy to CRX
	 * 
	 * @param jc
	 * @param schemaRequest
	 *            return jsonResponse
	 * @throws AcceleratorException
	 * 
	 */
	private JSONObject generateAndCopyToCRX(JAXBContext jc,
			SchemaServiceRequest schemaRequest) throws AcceleratorException {
		String methodName = "generateAndCopyToCRX";
		LOGGER.info(" || " + methodName + " || START");
		String outputFileName = AcceleratorGenericConstants.XSD_FILE_NAME;
		String storagePath = AcceleratorUtils.replaceSlashInPath(schemaRequest
				.getStoragePath());
		File sourceFile = new File(outputFileName);

		JSONObject jsonResponse = new JSONObject();
		try {
			boolean isCreated = jaxBXSDConversion(jc, outputFileName);
			if (isCreated) {
				boolean isMovedToTemp = AcceleratorFileUtils
						.moveOrCopyFileInCRX(sourceFile,
								AcceleratorGenericConstants.XSD_FILE_NAME,
								storagePath, false);
				jsonResponse = getJSONResponse(isMovedToTemp, storagePath
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ outputFileName);
			}
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			try {
				jsonResponse.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_FAILURE);
				jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION,
						"Failed to move or Copy File to CRX;");
				jsonResponse.put("XSD_FilePath", "");

			} catch (JSONException jsonE) {
				LOGGER.error("An exception has occured in " + methodName, jsonE);
				throw new AcceleratorException(
						AcceleratorFaultCode.JSON_EXCEPTION, CLASS_NAME,
						methodName, jsonE.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	/**
	 * This method is used to get the JSON Response based on the status
	 * 
	 * @param movementStatus
	 * @param filePath
	 * @return jsonResponse
	 * @throws AcceleratorException
	 */
	private JSONObject getJSONResponse(boolean movementStatus, String filePath)
			throws AcceleratorException {
		String methodName = "getJSONResponse";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject jsonResponse = new JSONObject();
		try {
			if (movementStatus) {
				jsonResponse.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_SUCCESS);
				jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION,
						"Successfully generated the file");
				jsonResponse.put("XSD_FilePath", filePath);

			} else {
				jsonResponse.put(AcceleratorGenericConstants.STATUS,
						AcceleratorGenericConstants.STATUS_FAILURE);
				jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION,
						"Failed to create XSD File.Please check the Logs;");

				jsonResponse.put("XSD_FilePath", "");
			}
		} catch (JSONException jsonE) {
			LOGGER.error("An exception has occured in " + methodName, jsonE);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, jsonE.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	/**
	 * This method is used to
	 * 
	 * @param jc
	 * @param outputFileName
	 * @throws AcceleratorException
	 */
	private boolean jaxBXSDConversion(JAXBContext jc,
			final String outputFileName) throws AcceleratorException {
		String methodName = "jaxBXSDConversion";
		LOGGER.info(" || " + methodName + " || START");
		try {
			jc.generateSchema(new SchemaOutputResolver() {
				@Override
				public Result createOutput(String namespaceUri,
						String suggestedFileName) throws IOException {
					StreamResult result = new StreamResult(
							new FileOutputStream(new File(outputFileName)));
					result.setSystemId(suggestedFileName);
					return result;
				}
			});
			LOGGER.info(" || " + methodName + " || END");
			return true;
		} catch (IOException e) {

			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}
	}

	/**
	 * This method is kept just in case in future if XSL generation can be
	 * automated.
	 * 
	 * @param xslServiceRequest
	 * @return
	 */
	private SchemaServiceResponse generateXSL(
			SchemaServiceRequest xslServiceRequest) {
		return null;
	}

}
