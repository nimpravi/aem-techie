package com.cts.accelerators.migration.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.core.util.AcceleratorCRXUtils;
import com.cts.accelerators.core.util.AcceleratorFileUtils;
import com.cts.accelerators.core.util.AcceleratorUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.services.dto.ReportGeneratorServiceRequest;

/**
 * This helper class is used for carry out ReportGenerationService smoothly
 * 
 * @author cognizant
 * 
 * 
 */
/**
 * @author Cognizant
 * 
 */
public class ReportGeneratorHelper {

	private static final String CLASS_NAME = ReportGeneratorHelper.class
			.getName();
	private static final Logger LOGGER = LoggerFactory.getLogger(CLASS_NAME);

	/**
	 * This method will be called for get reports for the reporting form
	 * 
	 * @param reportRequest
	 * @return
	 * @throws AcceleratorException
	 */
	public static JSONObject getReportViewingResponse(
			ReportGeneratorServiceRequest reportRequest)
			throws AcceleratorException {
		String methodName = "getReportViewingResponse";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject jsonObject = null;
		try {
			if (!reportRequest.isSpecificReport()) {
				jsonObject = getReportsForSpecificDay(
						reportRequest.getReportRootPath(),
						reportRequest.getReportDate(),
						reportRequest.getReportType(),
						reportRequest.getReportDetailedType(),
						reportRequest.isCompleteMigrationReports());
			} else {
				String crxPath = getDownloadLink(reportRequest
						.getSpecificReportPath());
				jsonObject = getSpecificReport(
						reportRequest.getSpecificReportPath(),
						reportRequest.getReportDetailedType());
				jsonObject.put(AcceleratorGenericConstants.DOWNLOAD_LINK,
						crxPath);
			}
		} catch (JSONException e) {
			LOGGER.info(" || " + methodName + " || JSONEXCEPTION || "
					+ e.getMessage());
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || jsonObject || " + jsonObject);
		LOGGER.info(" || " + methodName + " || END");
		return jsonObject;
	}

	/**
	 * This method returns a detailed JSONObject containing all the information
	 * from the report file identified by the specified report path
	 * 
	 * @param reportPath
	 * @return
	 * @throws AcceleratorException
	 */
	public static JSONObject fetchDetailedReport(String reportPath)
			throws AcceleratorException {
		String methodName = "fetchDetailedReport";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + "reportPath" + reportPath);
		JSONObject jsonObject = new JSONObject();
		JSONObject tempObject = null;
		JSONArray jsonRecords = new JSONArray();

		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = AcceleratorGenericConstants.CSV_SEPARATOR;
		String[] headerValues = null;
		String[] recordValues = null;
		boolean isHeaderRecord = true;

		try {
			jsonObject.put(AcceleratorGenericConstants.REPORT_PATH, reportPath);

			bufferedReader = new BufferedReader(new FileReader(reportPath));
			while ((line = bufferedReader.readLine()) != null) {
				if (isHeaderRecord) {
					headerValues = line.split(cvsSplitBy);
					LOGGER.debug(" || " + methodName + " || headerValues || "
							+ headerValues.toString());
					isHeaderRecord = false;
				} else {
					recordValues = line.split(cvsSplitBy);
					tempObject = new JSONObject();
					for (int counter = 0; counter < recordValues.length; counter++) {
						tempObject.put(headerValues[counter],
								recordValues[counter]);
					}
					LOGGER.debug(" || " + methodName
							+ " || individual records || "
							+ tempObject.toString());
					jsonRecords.put(tempObject);
				}
			}
			jsonObject.put(AcceleratorGenericConstants.REPORT_RECORDS,
					jsonRecords);
		} catch (FileNotFoundException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.FILE_NOT_FOUND,
					CLASS_NAME, methodName, e.getCause());

		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (JSONException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("An exception has occured in " + methodName, e);
					throw new AcceleratorException(
							AcceleratorFaultCode.IOEXCEPTION_STRING,
							CLASS_NAME, methodName, e.getCause());
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");

		return jsonObject;
	}

	/**
	 * This method returns a JSONObject containing summarized information from
	 * the report file identified by the specified report path
	 * 
	 * @param reportPath
	 * @return
	 * @throws AcceleratorException
	 */
	public static JSONObject fetchConciseReport(String reportPath)
			throws AcceleratorException {
		String methodName = "fetchConciseReport";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(methodName + " || reportPath || " + reportPath);
		JSONObject jsonResponse = new JSONObject();
		JSONObject successJSON = new JSONObject();
		JSONObject failureJSON = new JSONObject();

		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] recordValues = null;
		boolean isHeaderRecord = true;
		int recordLength = 0;
		int successCounter = 0, failureCounter = 0;

		try {

			bufferedReader = new BufferedReader(new FileReader(reportPath));
			while ((line = bufferedReader.readLine()) != null) {
				if (isHeaderRecord) {
					isHeaderRecord = false;
				} else {
					recordValues = line.split(cvsSplitBy);
					recordLength = recordValues.length;
					if (recordLength > 0) {
						if (AcceleratorGenericConstants.STATUS_SUCCESS
								.equals(recordValues[recordLength - 2])) {
							successCounter++;
						} else if (AcceleratorGenericConstants.STATUS_FAILURE
								.equals(recordValues[recordLength - 2])) {
							failureCounter++;
						}
					}
				}
			}
			jsonResponse.put(AcceleratorGenericConstants.TOTAL_RECORDS,
					successCounter + failureCounter);

			successJSON.put(AcceleratorGenericConstants.COUNT, successCounter);
			successJSON.put(AcceleratorGenericConstants.REPORT_PATH, "");
			jsonResponse.put(AcceleratorGenericConstants.STATUS_SUCCESS,
					successJSON);

			failureJSON.put(AcceleratorGenericConstants.COUNT, failureCounter);
			failureJSON.put(AcceleratorGenericConstants.REPORT_PATH, "");
			jsonResponse.put(AcceleratorGenericConstants.STATUS_FAILURE,
					failureJSON);

			jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION, "");
			jsonResponse.put(AcceleratorGenericConstants.REPORT_PATH,
					reportPath);
		} catch (FileNotFoundException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.FILE_NOT_FOUND,
					CLASS_NAME, methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (JSONException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("An exception has occured in " + methodName, e);
					throw new AcceleratorException(
							AcceleratorFaultCode.IOEXCEPTION_STRING,
							CLASS_NAME, methodName, e.getCause());
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");

		return jsonResponse;
	}

	/**
	 * This method returns a JSONObject containing summarized information from
	 * the report file identified by the specified report path and success and
	 * failure folders
	 * 
	 * @param reportPath
	 * @return
	 * @throws AcceleratorException
	 */
	public static JSONObject fetchConciseReportWithFolders(String reportPath,
			String successFolder, String failureFolder)
			throws AcceleratorException {
		String methodName = "fetchConciseReport";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(methodName + " || reportPath || " + reportPath);
		JSONObject jsonResponse = new JSONObject();
		JSONObject successJSON = new JSONObject();
		JSONObject failureJSON = new JSONObject();

		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] recordValues = null;
		boolean isHeaderRecord = true;
		int recordLength = 0;
		int successCounter = 0, failureCounter = 0;

		try {

			bufferedReader = new BufferedReader(new FileReader(reportPath));
			while ((line = bufferedReader.readLine()) != null) {
				if (isHeaderRecord) {
					isHeaderRecord = false;
				} else {
					recordValues = line.split(cvsSplitBy);
					recordLength = recordValues.length;
					if (recordLength > 0) {
						if (AcceleratorGenericConstants.STATUS_SUCCESS
								.equals(recordValues[recordLength - 2])) {
							successCounter++;
						} else if (AcceleratorGenericConstants.STATUS_FAILURE
								.equals(recordValues[recordLength - 2])) {
							failureCounter++;
						}
					}
				}
			}
			jsonResponse.put(AcceleratorGenericConstants.TOTAL_RECORDS,
					successCounter + failureCounter);

			successJSON.put(AcceleratorGenericConstants.COUNT, successCounter);
			successJSON.put(AcceleratorGenericConstants.REPORT_PATH,
					successFolder);
			jsonResponse.put(AcceleratorGenericConstants.STATUS_SUCCESS,
					successJSON);

			failureJSON.put(AcceleratorGenericConstants.COUNT, failureCounter);
			failureJSON.put(AcceleratorGenericConstants.REPORT_PATH,
					failureFolder);
			jsonResponse.put(AcceleratorGenericConstants.STATUS_FAILURE,
					failureJSON);

			jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION, "");
			jsonResponse.put(AcceleratorGenericConstants.REPORT_PATH,
					reportPath);
		} catch (FileNotFoundException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.FILE_NOT_FOUND,
					CLASS_NAME, methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (JSONException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("An exception has occured in " + methodName, e);
					throw new AcceleratorException(
							AcceleratorFaultCode.IOEXCEPTION_STRING,
							CLASS_NAME, methodName, e.getCause());
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");

		return jsonResponse;
	}

	/**
	 * This method returns a JSONObject containing only failure information from
	 * the report file identified by the specified report path
	 * 
	 * @param reportPath
	 * @return
	 * @throws AcceleratorException
	 */
	public static JSONObject fetchFailureReport(String reportPath)
			throws AcceleratorException {
		String methodName = "fetchFailureReport";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + "reportPath" + reportPath);

		JSONObject jsonResponse = new JSONObject();
		JSONObject tempObject = null;
		JSONArray jsonFailedRecords = new JSONArray();
		JSONArray jsonMiscRecords = new JSONArray();
		JSONArray tempArray = null;

		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = AcceleratorGenericConstants.CSV_SEPARATOR;
		String[] recordValues = null;
		boolean isHeaderRecord = true;
		int recordLength = 0;
		int successCounter = 0, failureCounter = 0;

		try {
			bufferedReader = new BufferedReader(new FileReader(reportPath));
			while ((line = bufferedReader.readLine()) != null) {
				if (isHeaderRecord) {
					isHeaderRecord = false;
				} else {
					recordValues = line.split(cvsSplitBy);
					recordLength = recordValues.length;
					if (recordLength > 0) {
						if (AcceleratorGenericConstants.STATUS_FAILURE
								.equals(recordValues[recordLength - 2])) {
							tempObject = new JSONObject();
							tempObject.put(recordValues[0],
									recordValues[recordLength - 1]);
							jsonFailedRecords.put(tempObject);
							failureCounter++;
						}
					}
				}
			}
			jsonResponse.put(AcceleratorGenericConstants.TOTAL_RECORDS,
					successCounter + failureCounter);
			jsonResponse.put(AcceleratorGenericConstants.STATUS_SUCCESS,
					successCounter);
			jsonResponse.put(AcceleratorGenericConstants.STATUS_FAILURE,
					failureCounter);
			jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION, "");
			jsonResponse.put(AcceleratorGenericConstants.REPORT_PATH,
					reportPath);

			tempArray = new JSONArray();
			tempObject = new JSONObject();
			tempObject.put(AcceleratorGenericConstants.STATUS_FAILURE
					+ AcceleratorGenericConstants.UNDERSCORE
					+ AcceleratorGenericConstants.COUNT, failureCounter);
			tempObject.put(AcceleratorGenericConstants.RECORDS,
					jsonFailedRecords);
			tempArray.put(tempObject);

			tempObject = new JSONObject();
			tempObject
					.put(AcceleratorGenericConstants.RECORDS, jsonMiscRecords);
			tempArray.put(tempObject);

			jsonResponse.put(AcceleratorGenericConstants.DETAILS, tempArray);
		} catch (FileNotFoundException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.FILE_NOT_FOUND,
					CLASS_NAME, methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (JSONException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JSON_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("An exception has occured in " + methodName, e);
					throw new AcceleratorException(
							AcceleratorFaultCode.IOEXCEPTION_STRING,
							CLASS_NAME, methodName, e.getCause());
				}
			}
		}
		LOGGER.debug(" || " + methodName + " || jsonResponse || "
				+ jsonResponse);
		LOGGER.info(" || " + methodName + " || END");

		return jsonResponse;
	}

	/**
	 * This method will return the list of files along with snapshot of first
	 * file created in a specific folder
	 * 
	 * @param specificDate
	 * @return
	 * @throws AcceleratorException
	 */
	public static JSONObject getReportsForSpecificDay(String reportRootPath,
			String specificDate, String reportType, String reportDetailType,
			final boolean isMigrationReports) throws AcceleratorException {
		String methodName = "getReportsForSpecificDay";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || reportRootPath || "
				+ reportRootPath + " || specificDate || " + specificDate
				+ " || isMigrationReports || " + isMigrationReports);

		JSONObject jsonResponse = new JSONObject();
		JSONObject folderObject = null;
		JSONObject fileObject = null;
		JSONArray tempArray = new JSONArray();
		JSONArray fileArray = new JSONArray();
		String specDate = "";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					AcceleratorGenericConstants.MMDDYYYY_FORMAT);
			Date date = sdf.parse(specificDate);
			sdf = new SimpleDateFormat(
					AcceleratorGenericConstants.DDMMYYYY_FORMAT);
			specDate = sdf.format(date);

		} catch (ParseException e1) {
			LOGGER.error("An exception has occured in " + methodName, e1);
			throw new AcceleratorException(
					AcceleratorFaultCode.PARSE_EXCEPTION, CLASS_NAME,
					methodName, e1.getCause());
		}

		String firstFileName = null;
		int counter = 0;
		int folderId = 0;

		File destinationFolder = new File(reportRootPath);
		File[] reportFolders = destinationFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				String individualOrMigration = isMigrationReports ? AcceleratorGenericConstants.MIGRATION_REPORTS
						: AcceleratorGenericConstants.INDIVIDUAL_REPORTS;
				if (pathname.getName().contains(individualOrMigration)) {
					return pathname.isDirectory();
				}
				return false;
			}
		});
		Arrays.sort(reportFolders, new Comparator<File>() {
			public int compare(File file1, File file2) {
				return Long.valueOf(file2.lastModified()).compareTo(
						file1.lastModified());
			}
		});
		for (File singleFolder : reportFolders) {
			String folderName = singleFolder.getName();
			if (folderName.contains(specDate)) {
				counter = 0;
				try {
					folderObject = new JSONObject();
					fileArray = new JSONArray();
					folderObject.put(AcceleratorGenericConstants.FOLDER_NAME,
							folderName);
					folderObject.put(AcceleratorGenericConstants.FOLDER_ID,
							folderId++);
					folderObject.put(AcceleratorGenericConstants.FOLDER_PATH,
							singleFolder.getAbsolutePath());

					for (File singleFile : singleFolder.listFiles()) {
						if (!reportType
								.equalsIgnoreCase(AcceleratorGenericConstants.ALL_REPORT_TYPES)) {
							if (singleFile
									.getName()
									.contains(
											reportType
													.split(AcceleratorGenericConstants.UNDERSCORE)[0])) {
								fileObject = new JSONObject();
								fileObject.put(
										AcceleratorGenericConstants.FILE_NAME,
										singleFile.getName());
								fileObject.put(
										AcceleratorGenericConstants.FILE_PATH,
										singleFile.getAbsolutePath());
								if (counter == 0) {
									firstFileName = singleFile
											.getAbsolutePath();
								}
								fileArray.put(fileObject);
								counter++;
							}
						} else {
							fileObject = new JSONObject();
							fileObject.put(
									AcceleratorGenericConstants.FILE_NAME,
									singleFile.getName());
							fileObject.put(
									AcceleratorGenericConstants.FILE_PATH,
									singleFile.getAbsolutePath());
							if (counter == 0) {
								firstFileName = singleFile.getAbsolutePath();
							}
							fileArray.put(fileObject);
							counter++;
						}
					}
					LOGGER.debug(" || " + methodName + " || firstFileName || "
							+ firstFileName);
					folderObject.put(AcceleratorGenericConstants.FILES,
							fileArray);
					if (null != firstFileName) {
						String crxPath = getDownloadLink(firstFileName);
						JSONObject jsonObject = getSpecificReport(
								firstFileName, reportDetailType);
						jsonObject.put(
								AcceleratorGenericConstants.DOWNLOAD_LINK,
								crxPath);
						folderObject
								.put(AcceleratorGenericConstants.FIRST_REPORT_SUMMARY,
										jsonObject);
					} else {
						jsonResponse.put(AcceleratorGenericConstants.STATUS,
								AcceleratorGenericConstants.STATUS_FAILURE);
						LOGGER.error(" || " + methodName
								+ " || no reports available");
						return jsonResponse;
					}

					tempArray.put(folderObject);
					jsonResponse.put(
							AcceleratorGenericConstants.ALL_MIGRATION_REPORTS,
							tempArray);
				} catch (JSONException e) {
					LOGGER.error("An exception has occured in " + methodName, e);
					throw new AcceleratorException(
							AcceleratorFaultCode.JSON_EXCEPTION, CLASS_NAME,
							methodName, e.getCause());

				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	/**
	 * This method returns the JSON response of a specific report
	 * 
	 * @param slingRequest
	 * @return
	 * @throws AcceleratorException
	 */
	public static JSONObject getSpecificReport(String reportPath,
			String reportDetailsType) throws AcceleratorException {
		String methodName = "getSpecificReport";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + "reportPath || " + reportPath
				+ " || reportDetailsType || " + reportDetailsType);

		JSONObject jsonResponse = new JSONObject();
		if (reportDetailsType
				.equalsIgnoreCase(AcceleratorGenericConstants.FAILURE_REPORT)) {
			jsonResponse = fetchFailureReport(reportPath);
		} else {
			jsonResponse = fetchConciseReport(reportPath);
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	/**
	 * This method creates the report folder for storing the required reports
	 * 
	 * @param reportRootPath
	 * @param reportType
	 * @return
	 * @throws AcceleratorException
	 */
	public static String createReportFolder(String reportRootPath,
			String reportType) throws AcceleratorException {
		String methodName = "createReportFolder";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + "|| reportRootPath || "
				+ reportRootPath + "|| reportType || " + reportType);

		File currentFolderPath = null;
		String todaysDate = AcceleratorUtils.getDateOrTime(false);
		int counter = 0;

		// Get the list of folders in the report directory
		LOGGER.info(" || " + methodName + " || reportRootPath || "
				+ reportRootPath);
		File destinationFolder = new File(reportRootPath);
		File[] subDirs = destinationFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		if (null != subDirs) {
			// Count the number of folders created today
			for (File singleFolder : subDirs) {
				String folderName = singleFolder.getName();
				if (folderName.contains(todaysDate)) {
					counter++;
				}
			}
		}
		if (reportType != null
				&& !reportType
						.equals(AcceleratorGenericConstants.COMPLETE_MIGRATION_PROCESS)) {

			// Create a folder with the increased counter with todays date
			currentFolderPath = new File(reportRootPath
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ AcceleratorGenericConstants.STANDALONE_REPORT_FOLDER
					+ AcceleratorGenericConstants.UNDERSCORE + todaysDate
					+ AcceleratorGenericConstants.UNDERSCORE + counter);
		} else {
			currentFolderPath = new File(reportRootPath
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ AcceleratorGenericConstants.ALL_REPORT_FOLDER
					+ AcceleratorGenericConstants.UNDERSCORE + todaysDate
					+ AcceleratorGenericConstants.UNDERSCORE + counter);

		}
		if (!currentFolderPath.exists()) {
			if (currentFolderPath.mkdir()) {
				LOGGER.debug(" || " + methodName
						+ " || Directory is created! || "
						+ currentFolderPath.getPath());
			} else {
				LOGGER.debug(" || " + methodName
						+ " || Failed to create directory! || "
						+ currentFolderPath.getPath());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return currentFolderPath.getAbsolutePath();
	}

	/**
	 * This method returns the header string for standalone reports
	 * 
	 * @param reportType
	 * @return
	 */
	private static String getHeaderString(String reportType) {
		String methodName = "getHeaderString";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + " || reportType || " + reportType);

		String headerString = "";
		if (reportType != null) {
			if (reportType.equals(AcceleratorGenericConstants.BACKUP_REPORT)) {
				headerString = AcceleratorGenericConstants.BACKUP_REPORT_HEADERS;
			} else if (reportType
					.equals(AcceleratorGenericConstants.IMPORT_REPORT)) {
				headerString = AcceleratorGenericConstants.IMPORT_REPORT_HEADERS;
			} else if (reportType
					.equals(AcceleratorGenericConstants.DAM_REPORT)) {
				headerString = AcceleratorGenericConstants.DAM_REPORT_HEADERS;
			} else if (reportType
					.equals(AcceleratorGenericConstants.TRANSFORMATION_REPORT)) {
				headerString = AcceleratorGenericConstants.TRANSFORMATION_REPORT_HEADERS;
			} else if (reportType
					.equals(AcceleratorGenericConstants.TAXONOMY_REPORT)) {
				headerString = AcceleratorGenericConstants.TAXONOMY_REPORT_HEADERS;
			} else if (reportType
					.equals(AcceleratorGenericConstants.REPLICATION_REPORT)) {
				headerString = AcceleratorGenericConstants.REPLICATION_REPORT_HEADERS;
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return headerString;
	}

	/**
	 * This method will create the report for single type of request and return
	 * the file path in the map
	 * 
	 * @param reportType
	 * @param reportHeader
	 * @return
	 * @throws AcceleratorException
	 */
	private static Map<String, String> createStandaloneReport(
			String reportType, String reportFolderPath, String headerString)
			throws AcceleratorException {
		String methodName = "createStandaloneReport";
		LOGGER.info(" || " + methodName + " || START");

		String currentTime = AcceleratorUtils.getDateOrTime(true);

		Map<String, String> reportFilePaths = new HashMap<String, String>();
		String fileNameWithExtension = reportType
				+ AcceleratorGenericConstants.UNDERSCORE + currentTime
				+ AcceleratorGenericConstants.DOT
				+ AcceleratorGenericConstants.CSV_EXTENSION;
		String completeFileName = null;

		LOGGER.debug(" || " + methodName + " || reportType || " + reportType
				+ " || reportFolderPath || " + " || currentTime || "
				+ currentTime + " || fileNameWithExtension || "
				+ fileNameWithExtension);

		try {
			completeFileName = reportFolderPath
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileNameWithExtension;
			if (null != headerString) {
				AcceleratorFileUtils.createFileFromString(headerString,
						fileNameWithExtension, reportFolderPath, false);
			} else {
				AcceleratorFileUtils.createFileFromString(
						ReportGeneratorHelper.getHeaderString(reportType),
						fileNameWithExtension, reportFolderPath, false);
			}

			reportFilePaths.put(reportType, reportFolderPath
					+ AcceleratorGenericConstants.CSV_SEPARATOR
					+ fileNameWithExtension);
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCELERATOR_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return reportFilePaths;
	}

	/**
	 * This method is used to create reporting files for all components for
	 * single migration run
	 * 
	 * @param reportRootPath
	 * @return boolean
	 * @throws AcceleratorException
	 */
	private static Map<String, String> createAllReportFilesWithHeaders(
			String reportFolderPath) throws AcceleratorException {
		String methodName = "createAllReportFilesWithHeaders";
		LOGGER.info(" || " + methodName + " || START");

		String todaysDate = AcceleratorUtils.getDateOrTime(false);

		String fileExtension = AcceleratorGenericConstants.UNDERSCORE
				+ todaysDate + AcceleratorGenericConstants.DOT
				+ AcceleratorGenericConstants.CSV_EXTENSION;
		String completeFileName = null;

		LOGGER.debug(" ||  " + methodName + " || reportFolderPath || "
				+ reportFolderPath + " || todaysDate || " + todaysDate
				+ " || fileExtension || " + fileExtension);

		Map<String, String> reportFilePaths = new HashMap<String, String>();
		try {
			// Taxonomy Report
			completeFileName = AcceleratorGenericConstants.TAXONOMY_REPORT
					+ fileExtension;
			AcceleratorFileUtils.createFileFromString(
					AcceleratorGenericConstants.TAXONOMY_REPORT_HEADERS,
					completeFileName, reportFolderPath, false);
			reportFilePaths.put(AcceleratorGenericConstants.TAXONOMY_REPORT,
					reportFolderPath
							+ AcceleratorGenericConstants.CSV_SEPARATOR
							+ AcceleratorGenericConstants.TAXONOMY_REPORT
							+ fileExtension);

			// DAM Report
			completeFileName = AcceleratorGenericConstants.DAM_REPORT
					+ fileExtension;
			AcceleratorFileUtils.createFileFromString(
					AcceleratorGenericConstants.DAM_REPORT_HEADERS,
					completeFileName, reportFolderPath, false);
			reportFilePaths.put(AcceleratorGenericConstants.DAM_REPORT,
					reportFolderPath
							+ AcceleratorGenericConstants.CSV_SEPARATOR
							+ AcceleratorGenericConstants.DAM_REPORT
							+ fileExtension);

			// Replication Report
			completeFileName = AcceleratorGenericConstants.REPLICATION_REPORT
					+ fileExtension;
			AcceleratorFileUtils.createFileFromString(
					AcceleratorGenericConstants.REPLICATION_REPORT_HEADERS,
					completeFileName, reportFolderPath, false);
			reportFilePaths.put(AcceleratorGenericConstants.REPLICATION_REPORT,
					reportFolderPath
							+ AcceleratorGenericConstants.CSV_SEPARATOR
							+ AcceleratorGenericConstants.REPLICATION_REPORT
							+ fileExtension);

			// Reader Report
			completeFileName = AcceleratorGenericConstants.BACKUP_REPORT
					+ fileExtension;
			AcceleratorFileUtils.createFileFromString(
					AcceleratorGenericConstants.BACKUP_REPORT_HEADERS,
					completeFileName, reportFolderPath, false);
			reportFilePaths.put(AcceleratorGenericConstants.BACKUP_REPORT,
					reportFolderPath
							+ AcceleratorGenericConstants.CSV_SEPARATOR
							+ AcceleratorGenericConstants.BACKUP_REPORT
							+ fileExtension);

			// Import Report
			completeFileName = AcceleratorGenericConstants.IMPORT_REPORT
					+ fileExtension;
			AcceleratorFileUtils.createFileFromString(
					AcceleratorGenericConstants.IMPORT_REPORT_HEADERS,
					completeFileName, reportFolderPath, false);
			reportFilePaths.put(AcceleratorGenericConstants.IMPORT_REPORT,
					reportFolderPath
							+ AcceleratorGenericConstants.CSV_SEPARATOR
							+ AcceleratorGenericConstants.IMPORT_REPORT
							+ fileExtension);

			// Transformation Report
			completeFileName = AcceleratorGenericConstants.TRANSFORMATION_REPORT
					+ fileExtension;
			AcceleratorFileUtils.createFileFromString(
					AcceleratorGenericConstants.TRANSFORMATION_REPORT_HEADERS,
					completeFileName, reportFolderPath, false);
			reportFilePaths.put(
					AcceleratorGenericConstants.TRANSFORMATION_REPORT,
					reportFolderPath
							+ AcceleratorGenericConstants.CSV_SEPARATOR
							+ AcceleratorGenericConstants.TRANSFORMATION_REPORT
							+ fileExtension);
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCELERATOR_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return reportFilePaths;
	}

	/**
	 * This method will be called to configure the report file paths which will
	 * be used through out single application run to log all activities
	 * 
	 * @param reportRequest
	 * @return
	 * @throws AcceleratorException
	 */
	public static Map<String, String> configureRequiredReportPath(
			ReportGeneratorServiceRequest reportRequest)
			throws AcceleratorException {
		String methodName = "configureRequiredReportPath";
		LOGGER.info(" || " + methodName + " || START");

		Map<String, String> reportFilePaths = null;
		if (reportRequest.getReportType() != null
				&& (reportRequest.getReportType())
						.equals(AcceleratorGenericConstants.COMPLETE_MIGRATION_PROCESS)) {
			reportFilePaths = ReportGeneratorHelper
					.createAllReportFilesWithHeaders(reportRequest
							.getReportRootPath());
		} else {
			reportFilePaths = ReportGeneratorHelper.createStandaloneReport(
					reportRequest.getReportType(),
					reportRequest.getReportRootPath(), null);
		}
		LOGGER.info(" || " + methodName + " || END");
		return reportFilePaths;
	}

	/**
	 * This method is used to log the required reporting message in the
	 * specified file in the input parameters
	 * 
	 * @param reportFilePath
	 * @param message
	 * @return
	 * @throws AcceleratorException
	 */
	public static boolean addOrUpdateMessage(String reportFilePath,
			String message) throws AcceleratorException {
		String methodName = "addOrUpdateMessage";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + " || reportFilePath || "
				+ reportFilePath + " || message || " + message);
		boolean isLogged = false;
		String[] fileParts = null;
		if (reportFilePath != null) {
			fileParts = reportFilePath
					.split(AcceleratorGenericConstants.CSV_SEPARATOR);
			if (fileParts.length > 1)
				try {
					AcceleratorFileUtils.createFileFromString(message,
							fileParts[1], fileParts[0], false);
				} catch (AcceleratorException e) {
					LOGGER.error("An exception has occured in " + methodName, e);
					throw new AcceleratorException(
							AcceleratorFaultCode.ACCELERATOR_EXCEPTION,
							CLASS_NAME, methodName, e.getCause());
				}
		}
		LOGGER.info(" || " + methodName + " || END");
		return isLogged;
	}

	/**
	 * This method is used for Downloading the link
	 * 
	 * @param reportPath
	 * @return
	 * @throws AcceleratorException
	 */
	public static String getDownloadLink(String reportPath)
			throws AcceleratorException {
		String methodName = "getDownloadLink";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		String crxFilePath = "";

		String fileNameWithExtension = reportPath.substring(reportPath
				.lastIndexOf(AcceleratorGenericConstants.BACKWARD_SLASH) + 1,
				reportPath.length());
		File file = new File(reportPath);
		String destinationPathInCRX = AcceleratorGenericConstants.REPORT_CRX_TEMP_PATH;
		try {
			LOGGER.info(" || "
					+ methodName
					+ " || getNodeIfExists || "
					+ JcrUtils.getNodeIfExists(
							AcceleratorGenericConstants.REPORT_CRX_TEMP_PATH,
							jcrSession));
			if (null == JcrUtils.getNodeIfExists(
					AcceleratorGenericConstants.REPORT_CRX_TEMP_PATH,
					jcrSession)) {
				Node node = AcceleratorCRXUtils
						.createFolder(AcceleratorGenericConstants.REPORT_CRX_TEMP_PATH);
				LOGGER.info(" || " + methodName + " || node.getPath() || "
						+ node.getPath());
			}
			jcrSession.save();
			crxFilePath = destinationPathInCRX
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileNameWithExtension;
			AcceleratorFileUtils.moveOrCopyFileInCRX(file,
					fileNameWithExtension, destinationPathInCRX, false);
		} catch (RepositoryException e) {
			LOGGER.info(" || " + methodName + " || EXCEPTION || "
					+ e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || crxFilePath || " + crxFilePath);
		LOGGER.info(" || " + methodName + " || END");
		return crxFilePath;
	}

	public static boolean addToPropertyFile(String reportPath,
			String propertyFileType) throws AcceleratorException {
		String methodName = "addToPropertyFile";
		LOGGER.info(" || " + methodName + " || START");

		LOGGER.debug(" || " + methodName + " || reportPath || " + reportPath
				+ " || propertyFileType || " + propertyFileType);

		HashMap<String, String> map = new HashMap<String, String>();
		String propertyFileName = "";
		String reportFileType = "";
		String fileContents = "";
		BufferedReader bufferedReader = null;
		String line = "";
		boolean isHeaderRecord = true;
		String[] recordValues = null;
		int recordLength = 0;

		try {
			if (propertyFileType
					.equalsIgnoreCase(AcceleratorGenericConstants.IMPORT_TYPE_DAM)) {
				propertyFileName = AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES;
				reportFileType = AcceleratorGenericConstants.DAM_REPORT;
			} else if (propertyFileType
					.equalsIgnoreCase(AcceleratorGenericConstants.IMPORT_TYPE_TAG)) {
				propertyFileName = AcceleratorGenericConstants.TAG_FILES_MAPPING_PROPERTIES;
				reportFileType = AcceleratorGenericConstants.TAXONOMY_REPORT;
			} else {
				propertyFileName = AcceleratorGenericConstants.PAGE_FILES_MAPPING_PROPERTIES;
				reportFileType = AcceleratorGenericConstants.IMPORT_REPORT;
			}

			File reportFolder = new File(reportPath);
			LOGGER.info(" || " + methodName + " || reportFolder || "
					+ reportFolder.getName());
			// to get all report folders - Migration and Individual
			File[] subDirs = reportFolder.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});

			// to sort older to latest
			Arrays.sort(subDirs, new Comparator<File>() {
				public int compare(File file1, File file2) {
					return Long.valueOf(file1.lastModified()).compareTo(
							file2.lastModified());
				}
			});

			// iterate over all files in folders to get property file content
			for (File folder : subDirs) {
				File[] files = AcceleratorFileUtils.listFiles(
						folder.getAbsolutePath(), "csv");
				for (File file : files) {
					if (file.getName().contains(reportFileType)) {
						bufferedReader = new BufferedReader(new FileReader(
								file.getAbsolutePath()));
						isHeaderRecord = true;
						fileContents = "";
						while ((line = bufferedReader.readLine()) != null) {
							if (isHeaderRecord) {
								isHeaderRecord = false;
							} else {
								recordValues = line
										.split(AcceleratorGenericConstants.CSV_SEPARATOR);
								recordLength = recordValues.length;
								if (recordLength > 0) {
									fileContents = fileContents
											+ recordValues[0]
											+ AcceleratorGenericConstants.EQUAL_TO
											+ recordValues[2]
											+ AcceleratorGenericConstants.NEW_LINE;
									LOGGER.info(" || " + methodName
											+ " || fileContents || "
											+ fileContents);
								}
							}
						}
					}
				}
			}

			String path = AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
					+ propertyFileName;

			LOGGER.info(" || " + methodName + " || fileContents || "
					+ fileContents);
			AcceleratorFileUtils
					.createFileFromString(
							fileContents,
							propertyFileName,
							AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH,
							true);

			// to keep only latest dam path for corresponding ids
			String previousFileContents = AcceleratorCRXUtils.readCRXData(path);
			LOGGER.info(" || " + methodName + " || previousFileContents || "
					+ previousFileContents);

			Properties properties = new Properties();
			properties.load(new StringReader(previousFileContents));

			for (final String name : properties.stringPropertyNames()) {
				map.put(name, properties.getProperty(name));
			}

			AcceleratorCRXUtils.deleteComponent(path);

			Properties new_properties = new Properties();
			if (null != map) {
				for (Entry<String, String> entry : map.entrySet()) {
					new_properties
							.setProperty(entry.getKey(), entry.getValue());
				}
			}
			LOGGER.info(" || " + methodName + " || new_properties || "
					+ new_properties);
			String temp = new_properties.toString().replace(
					AcceleratorGenericConstants.CSV_SEPARATOR, "");
			temp = temp.replace(" ", AcceleratorGenericConstants.NEW_LINE);
			fileContents = temp.substring(1, temp.length() - 1);

			AcceleratorFileUtils
					.createFileFromString(
							fileContents,
							propertyFileName,
							AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH,
							true);
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					+ e.getMessage());
		} catch (IOException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					+ e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}

		LOGGER.info(" || " + methodName + " || END");
		return true;
	}

	public static Map<String, String> getPropertiesMap(String propertyFilePath)
			throws AcceleratorException {
		String methodName = "getPropertiesMap";
		LOGGER.info(" || " + methodName + " || START");

		Map<String, String> map = new HashMap<String, String>();

		try {
			String fileContents = AcceleratorCRXUtils
					.readCRXData(propertyFilePath);

			Properties properties = new Properties();
			properties.load(new StringReader(fileContents));

			for (final String name : properties.stringPropertyNames()) {
				map.put(name, properties.getProperty(name));
			}
		} catch (IOException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "
					+ e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}

		LOGGER.info(" || " + methodName + " || map || " + map);
		LOGGER.info(" || " + methodName + " || END");
		return map;
	}

	public static void createEmptyPropertyFiles() throws AcceleratorException {
		String methodName = "createEmptyPropertyFiles";
		LOGGER.info(" || " + methodName + " || START");

		String[] propertyFileNames = {
				AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES,
				AcceleratorGenericConstants.TAG_FILES_MAPPING_PROPERTIES,
				AcceleratorGenericConstants.PAGE_FILES_MAPPING_PROPERTIES };

		for (String propertyFileName : propertyFileNames) {
			String path = AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
					+ propertyFileNames;
			AcceleratorFileUtils
					.createFileFromString("",
							propertyFileName,
							AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH,
							true);
		}
		LOGGER.info(" || " + methodName + " || END");
	}
}
