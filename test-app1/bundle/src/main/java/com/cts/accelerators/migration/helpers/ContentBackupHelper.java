package com.cts.accelerators.migration.helpers;

/**
 * This is the helper class for Content Reader Service to carry out content movement activity smoothly.
 * 
 *  @author Cognizant
 */

import java.io.File;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.util.AcceleratorFileUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.services.dto.ContentBackupServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentBackupServiceResponse;

public class ContentBackupHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentBackupHelper.class);
	private static final String CLASS_NAME = ContentBackupHelper.class.getName();

	/**
	 * This method will use methods from FileUtils and other classes to move
	 * contents to CRX / File System
	 * 
	 * @param backupRequest
	 * @param reportFilePath
	 * @return backupResponse
	 * @throws AcceleratorException
	 */
	public static ContentBackupServiceResponse copyContents(
			ContentBackupServiceRequest backupRequest, String reportFilePath)
			throws AcceleratorException {
		String methodName = "copyContents";
		LOGGER.info(" || " + methodName + " || START");
		ContentBackupServiceResponse backupResponse = new ContentBackupServiceResponse();

		// Get report path
		String reportPath = "";
		if (backupRequest.getReportFilePath() != null) {
			String[] reportStrings = backupRequest.getReportFilePath().split(
					AcceleratorGenericConstants.CSV_SEPARATOR);
			for (String singleString : reportStrings) {
				reportPath = reportPath + singleString
						+ AcceleratorGenericConstants.FORWARD_SLASH;
			}
			reportPath = reportPath.substring(0, reportPath
					.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH));
		}

		// Check moveTo request variable and call the appropriate function
		if (backupRequest != null
				&& AcceleratorGenericConstants.CRX.equals(backupRequest
						.getMoveTo())) {
			copyContentsToCRX(backupRequest.getSourceRootPath(),
					backupRequest.getDestinationRootPath(),
					backupRequest.getReportFilePath());
		} else {
			copyContentsToFileSystem(backupRequest.getSourceRootPath(),
					backupRequest.getDestinationRootPath(),
					backupRequest.getReportFilePath());
		}

		// Get Backup report data
		backupResponse.setJsonResponse(ReportGeneratorHelper
				.fetchConciseReportWithFolders(reportPath,
						backupRequest.getDestinationRootPath(),
						backupRequest.getSourceRootPath()));

		LOGGER.info(" || " + methodName + " || END");
		return backupResponse;
	}

	/**
	 * This method will be called to create a content copy at the specified
	 * destination path
	 * 
	 * @param sourceRootPath
	 * @param destinationPath
	 * @param reportPath
	 * 
	 */
	private static void copyContentsToFileSystem(String sourceRootPath,
			String destinationPath, String reportPath) {
		String methodName = "copyContentsToFileSystem";
		LOGGER.info(" || " + methodName + " || START");

		// Get the source folder location
		File sourcePath = new File(sourceRootPath);
		if (sourcePath.exists()) {
			// Get all files/first level folders inside source folder
			File[] fileToBeIterated = sourcePath.listFiles();
			if (fileToBeIterated.length > 0) {
				for (File singleFile : fileToBeIterated) {
					if (singleFile.isDirectory()) {
						copyDirectoryToFileSystem(singleFile, destinationPath
								+ AcceleratorGenericConstants.FORWARD_SLASH
								+ singleFile.getName(), reportPath);
					}
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method will be called to create a copy of contents at a specified
	 * location in CRX
	 * 
	 * @param sourceRootPath
	 * @param destinationPath
	 * @param reportPath
	 * 
	 */
	private static void copyContentsToCRX(String sourceRootPath,
			String destinationPath, String reportPath) {
		String methodName = "copyContentsToCRX";
		LOGGER.info(" || " + methodName + " || START");

		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method will be called to create a copy of contents at a specified
	 * location on File system
	 * 
	 * @param directoryToCopy
	 * @param destinationPath
	 * @param reportPath
	 * 
	 */
	private static void copyDirectoryToFileSystem(File directoryToCopy,
			String destinationPath, String reportPath) {
		String methodName = "copyDirectoryToFileSystem";
		LOGGER.info(" || " + methodName + " || START");

		File[] filesInThedirectory = directoryToCopy.listFiles();
		if (null != filesInThedirectory && filesInThedirectory.length > 0) {
			for (File fileToBeMoved : filesInThedirectory) {
				if (fileToBeMoved.isDirectory()) {
					copyDirectoryToFileSystem(fileToBeMoved, destinationPath
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ fileToBeMoved.getName(), reportPath);
				} else {
					try {
						AcceleratorFileUtils.moveOrCopyFileOnServer(
								fileToBeMoved, destinationPath, false);
						addToReportLog(fileToBeMoved.getAbsolutePath(),
								destinationPath,
								AcceleratorGenericConstants.STATUS_SUCCESS,
								reportPath);
					} catch (AcceleratorException e) {
						LOGGER.error(
								"An exception has occured in "+methodName,
								e);
					}
				}
			}
		}

		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method will be used to log success and failure of individual assets
	 * in the reader reports.
	 * 
	 * @param sourcePath
	 * @param destinationPath
	 * @param movementStatus
	 * @param reportFilePath
	 * 
	 */
	public static void addToReportLog(String sourcePath,
			String destinationPath, String movementStatus, String reportFilePath) {
		String methodName = "addToReportLog";
		LOGGER.info(" || " + methodName + " || START");

		String reportingMessage;

		// Common Message part
		reportingMessage = sourcePath
				+ AcceleratorGenericConstants.CSV_SEPARATOR + destinationPath
				+ AcceleratorGenericConstants.CSV_SEPARATOR + movementStatus
				+ AcceleratorGenericConstants.CSV_SEPARATOR;

		// Adding success or failure description
		if (AcceleratorGenericConstants.STATUS_SUCCESS.equals(movementStatus)) {
			reportingMessage = reportingMessage
					+ AcceleratorGenericConstants.SUCCESS_DESC;
		} else {
			reportingMessage = reportingMessage
					+ AcceleratorGenericConstants.FAILURE_DESC;
		}

		// Add message to the reports log
		try {
			ReportGeneratorHelper.addOrUpdateMessage(reportFilePath,
					reportingMessage);
		} catch (AcceleratorException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
		}
		LOGGER.info(" || " + methodName + " || END");
	}

}
