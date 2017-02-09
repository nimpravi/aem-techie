package com.cts.accelerators.migration.helpers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.util.AcceleratorDAMUtils;
import com.cts.accelerators.core.util.AcceleratorExcelUtils;
import com.cts.accelerators.core.util.AcceleratorFileUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.ootbcomps.dto.DAMDTO;
import com.cts.accelerators.migration.services.dto.DamUploadServiceRequest;
import com.cts.accelerators.migration.services.dto.DamUploadServiceResponse;

/**
 * This helper class is used for Dam Upload method to carry out content movement
 * activity smoothly
 * 
 * @author cognizant
 * 
 */
public class DamUploadHelper {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DamUploadHelper.class);
	private static final String CLASS_NAME = DamUploadHelper.class.getName();

	/**
	 * This method will be used for getting the list of files in specified root
	 * folder.
	 * 
	 * @param - directory path for which the file list needs to be compiled
	 * @return - an array containing the list of all files
	 */
	public static File[] getAllFiles(String sourceRootPath) {
		String methodName = "getAllFiles";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + " || sourceRootPath || "
				+ sourceRootPath);
		File sourcePath = new File(sourceRootPath);
		File[] listOfDAMFiles = sourcePath.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		LOGGER.info(" || " + methodName + " || END");
		return listOfDAMFiles;
	}

	/**
	 * This method will create the DAMDTO object
	 * 
	 * @param dServiceRequest
	 * @param damFile
	 * @return
	 * @throws AcceleratorException
	 */
	public static DAMDTO getDAMObject(DamUploadServiceRequest dServiceRequest,
			File damFile) throws AcceleratorException {
		String methodName = "getDAMObject";
		LOGGER.info(" || " + methodName + " || START");

		String damFileName = damFile.getName().substring(0,
				damFile.getName().lastIndexOf("."));
		LOGGER.debug(" || " + methodName + " || damFile.getName() || "
				+ damFile.getName());
		// Create DAMDTO object
		DAMDTO damObject = new DAMDTO();

		if (damFile.getName().endsWith(AcceleratorGenericConstants.XML)) {
			damObject = (DAMDTO) ContentImporterHelper
					.convertXMLToObject(damFile);
			if (damObject != null) {
				damObject.setDestinationPath(dServiceRequest.getCqRootPath());
				damObject.setSourcePath(dServiceRequest.getSourceRootPath());
				damObject.setHost(dServiceRequest.getLocalhost());
				damObject.setPort(dServiceRequest.getPort());
				damObject.setUserName(dServiceRequest.getUsername());
				damObject.setPassword(dServiceRequest.getPassword());
				if (null == damObject.getId()
						|| damObject.getId().equalsIgnoreCase("")) {
					damObject.setId(damFileName);
					LOGGER.debug(" || " + methodName
							+ " || damObject.getId() || " + damObject.getId());
				}
			}
		} else if (!damFile.getName().endsWith(
				AcceleratorGenericConstants.EXCEL_FILE_EXTENSION)) {
			LOGGER.debug(" || " + methodName + " || else");
			damObject.setFileName(damFile.getName());
			damObject.setDestinationPath(dServiceRequest.getCqRootPath());
			damObject.setSourcePath(dServiceRequest.getSourceRootPath());
			damObject.setHost(dServiceRequest.getLocalhost());
			damObject.setPort(dServiceRequest.getPort());
			damObject.setUserName(dServiceRequest.getUsername());
			damObject.setPassword(dServiceRequest.getPassword());
			damObject.setId(damFileName);
		}

		// If the image file is referenced from a websites
		damObject = imageFileRefFromWebsite(dServiceRequest, damObject);
		LOGGER.info(" || " + methodName + " || END");
		return damObject;
	}

	/**
	 * @param dServiceRequest
	 * @param damObject
	 * @throws AcceleratorException
	 */
	public static DAMDTO imageFileRefFromWebsite(
			DamUploadServiceRequest dServiceRequest, DAMDTO damObject)
			throws AcceleratorException {
		String methodName = "imageFileRefFromWebsite";
		LOGGER.info(" || " + methodName + " || START");

		if (null != damObject && null != damObject.getFileName()) {
			if (damObject.getFileName().startsWith(
					AcceleratorGenericConstants.HTTP)) {
				String imageFolder = damObject.getSourcePath()
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ AcceleratorGenericConstants.IMAGE_FOLDER_NAME;
				String downloadedImageName = null;
				int startIndexOfFileName = 0, startIndexOfExt = 0;
				if (damObject.getFileName().contains(
						AcceleratorGenericConstants.FORWARD_SLASH)) {
					startIndexOfFileName = damObject.getFileName().lastIndexOf(
							AcceleratorGenericConstants.FORWARD_SLASH);
					startIndexOfExt = damObject.getFileName().lastIndexOf(
							AcceleratorGenericConstants.DOT);
					downloadedImageName = damObject.getFileName().substring(
							startIndexOfFileName + 1, startIndexOfExt);

					LOGGER.info(" || " + methodName + " || FileName || "
							+ damObject.getFileName());
				}
				if (AcceleratorDAMUtils.downloadMedia(damObject.getFileName(),
						imageFolder, downloadedImageName,
						AcceleratorGenericConstants.URL,
						dServiceRequest.isEnableProxy(),
						dServiceRequest.getProxyHost(),
						dServiceRequest.getProxyPort())) {
					String extension = damObject.getFileName().substring(
							startIndexOfExt, damObject.getFileName().length());
					downloadedImageName = downloadedImageName + extension;
					damObject
							.setFileName(AcceleratorGenericConstants.IMAGE_FOLDER_NAME
									+ AcceleratorGenericConstants.FORWARD_SLASH
									+ downloadedImageName);
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return damObject;
	}

	/**
	 * This method will be called to iterate over all digital asset files and
	 * import them in CQ.
	 * 
	 * @param dServiceRequest
	 * @param reportFilePath
	 * @return
	 * @throws AcceleratorException
	 */
	public static DamUploadServiceResponse damBulkUpload(
			DamUploadServiceRequest dServiceRequest, String reportFilePath) {
		String methodName = "damBulkUpload";
		LOGGER.info(" || " + methodName + " || START");
		DamUploadServiceResponse response = new DamUploadServiceResponse();
		File[] listOfAssets = getAllFiles(dServiceRequest.getSourceRootPath());

		LOGGER.info(" || " + methodName + " || reportFilePath || "
				+ reportFilePath + " || listOfAssets.length || "
				+ listOfAssets.length);
		// Construct finalFile report path
		String[] reportStrings = reportFilePath
				.split(AcceleratorGenericConstants.CSV_SEPARATOR);
		String reportPath = "";
		for (String singleString : reportStrings) {
			reportPath = reportPath + singleString
					+ AcceleratorGenericConstants.FORWARD_SLASH;
		}
		reportPath = reportPath.substring(0, reportPath
				.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH));

		// Success and Failure folders
		String failurePath = dServiceRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_FAILURE;
		String successPath = dServiceRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_SUCCESS;
		if (dServiceRequest.isCompleteMigration()) {
			failurePath = failurePath
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ AcceleratorGenericConstants.IMPORT_TYPE_DAM;
			successPath = successPath
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ AcceleratorGenericConstants.IMPORT_TYPE_DAM;
		}

		// Iterate and import one by one and construct DamDTO
		DAMDTO damDTO = null;
		List<DAMDTO> damDTOList = new ArrayList<DAMDTO>();
		if (null != listOfAssets) {
			LOGGER.debug(" || " + methodName + " || Num Files || "
					+ listOfAssets.length);
			int counter = 1;
			for (File singleFile : listOfAssets) {
				boolean isUploaded = false;
				boolean isUpdated = false;
				LOGGER.debug(" || " + methodName + " || Counter || "
						+ counter++);
				singleFile = getModifiedFileName(singleFile);
				if (dServiceRequest.getTransformationType().equalsIgnoreCase(
						AcceleratorGenericConstants.XML)) {
					LOGGER.debug(" || " + methodName + " || XML");
					try {
						damDTO = getDAMObject(dServiceRequest, singleFile);
						isUploaded = uploadToDAM(reportFilePath, failurePath,
								successPath, damDTO, singleFile, isUploaded,
								isUpdated);
					} catch (AcceleratorException e1) {
						try {
							AcceleratorFileUtils.moveOrCopyFileOnServer(
									singleFile, failurePath, true);
							if (singleFile.getName().endsWith(
									AcceleratorGenericConstants.XML)) {
								moveReferencedImageFile(
										damDTO.getSourcePath()
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ damDTO.getFileName(),
										failurePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ AcceleratorGenericConstants.IMAGE_FOLDER_NAME);
							}
							addToReportLog(damDTO, isUploaded, reportFilePath,
									false);
						} catch (AcceleratorException e) {
							LOGGER.error(" || "
									+ methodName
									+ " || error while moving the file to failure folder");
						}
						LOGGER.error(" || " + methodName
								+ " || error in creating DAM object");
					}
				} else if (dServiceRequest.getTransformationType()
						.equalsIgnoreCase(AcceleratorGenericConstants.EXCEL)
						&& singleFile
								.getName()
								.endsWith(
										AcceleratorGenericConstants.EXCEL_FILE_EXTENSION)) {
					LOGGER.debug(" || " + methodName + " || EXCEL");
					damDTOList = (List<DAMDTO>) AcceleratorExcelUtils
							.setDAMDTO(dServiceRequest, singleFile);
					for (DAMDTO damObject : damDTOList) {
						isUploaded = false;
						try {
							isUploaded = uploadToDAM(reportFilePath,
									failurePath, successPath, damObject,
									singleFile, isUploaded, isUpdated);
						} catch (AcceleratorException e1) {
							LOGGER.error(" || " + methodName + " || ACC EXCEPTION");
							try {
								moveReferencedImageFile(
										damObject.getSourcePath()
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ damObject.getFileName(),
										failurePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ AcceleratorGenericConstants.IMAGE_FOLDER_NAME);
							} catch (AcceleratorException e) {
								LOGGER.error(" || "
										+ methodName
										+ " || error while moving the file to failure folder");
							}
							addToReportLog(damObject, isUploaded,
									reportFilePath, false);
							LOGGER.error(" || " + methodName
									+ " || error in creating DAM object");

						}
					}
				}
			}
		}
		// Get report data
		try {
			response.setJsonResponse(ReportGeneratorHelper
					.fetchConciseReportWithFolders(reportPath, successPath,
							failurePath));
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName
					+ " while fetching reports", e);
		}
		LOGGER.debug(" || " + methodName + " || Final Response || "
				+ response.getJsonResponse().toString());
		LOGGER.info(" || " + methodName + " || END");
		return response;
	}

	/**
	 * @param reportFilePath
	 * @param methodName
	 * @param failurePath
	 * @param successPath
	 * @param damDTO
	 * @param singleFile
	 * @param isUploaded
	 * @param isUpdated
	 * @return
	 * @throws RepositoryException
	 * @throws AcceleratorException
	 */
	private static boolean uploadToDAM(String reportFilePath,
			String failurePath, String successPath, DAMDTO damDTO,
			File singleFile, boolean isUploaded, boolean isUpdated)
			throws AcceleratorException {
		String methodName = "uploadToDAM";
		LOGGER.info(" || " + methodName + " || START");
		try {
			if (!AcceleratorDAMUtils.verifyUpdate(damDTO)) {
				isUploaded = AcceleratorDAMUtils.singleDAMUpload(damDTO);
			} else if (AcceleratorDAMUtils.verifyUpdate(damDTO)) {
				LOGGER.debug(" || " + methodName
						+ " || File Already Exists and updated the properties");
				LOGGER.debug(" || " + methodName + " || END");
				isUploaded = true;
				isUpdated = true;
			}

			// Move original files to success folder
			if (isUploaded) {
				AcceleratorFileUtils.moveOrCopyFileOnServer(singleFile,
						successPath, false);
				if (singleFile.getName().endsWith(
						AcceleratorGenericConstants.XML) || singleFile.getName().endsWith(
								AcceleratorGenericConstants.EXCEL_FILE_EXTENSION)) {
					moveReferencedImageFile(damDTO.getSourcePath()
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ damDTO.getFileName(), successPath
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ AcceleratorGenericConstants.IMAGE_FOLDER_NAME);
				}

				addToReportLog(damDTO, isUploaded, reportFilePath, isUpdated);
			} else {

				AcceleratorFileUtils.moveOrCopyFileOnServer(singleFile,
						failurePath, false);
				if (singleFile.getName().endsWith(
						AcceleratorGenericConstants.XML) || singleFile.getName().endsWith(
								AcceleratorGenericConstants.EXCEL_FILE_EXTENSION)) {
					moveReferencedImageFile(damDTO.getSourcePath()
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ damDTO.getFileName(), failurePath
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ AcceleratorGenericConstants.IMAGE_FOLDER_NAME);
				}

				addToReportLog(damDTO, isUploaded, reportFilePath, false);

			}
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || ACCELERATOR EXCEPTION");
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCELERATOR_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error(" || " + methodName + " || REPOSITORY EXCEPTION");
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return isUploaded;
	}

	/**
	 * This method will be called to move the referenced image files to either
	 * success or failure folders
	 * 
	 * @param filePath
	 * @param destinationPath
	 * @throws AcceleratorException
	 */
	private static void moveReferencedImageFile(String filePath,
			String destinationPath) throws AcceleratorException {
		String methodName = "moveReferencedImageFile";
		LOGGER.info(" || " + methodName + " || START");

		File imageFile = new File(filePath);
		LOGGER.debug(" || " + methodName + " || imageFile || "
				+ imageFile.getAbsolutePath());
		AcceleratorFileUtils.moveOrCopyFileOnServer(imageFile, destinationPath,
				false);
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method will be used to log success and failure of individual assets
	 * in the dam reports.
	 * 
	 * @param damDTO
	 * @param isUploaded
	 */
	public static void addToReportLog(DAMDTO damDTO, boolean isUploaded,
			String reportFilePath, boolean isUpdated) {
		String methodName = "addToReportLog";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || reportFilePath || "
				+ reportFilePath + "damDTO.getFileName()"
				+ damDTO.getFileName());

		String reportingMessage;

		// Modified Date for reprots
		DateFormat dateFormat = new SimpleDateFormat(
				AcceleratorGenericConstants.YYYYMMDD_FORMAT);
		Date date = new Date();
		String modifiedDate = dateFormat.format(date);

		String fileName = "";
		if (damDTO.getFileName().contains(
				AcceleratorGenericConstants.FORWARD_SLASH)) {
			fileName = damDTO.getFileName().substring(
					damDTO.getFileName().lastIndexOf(
							AcceleratorGenericConstants.FORWARD_SLASH) + 1,
					damDTO.getFileName().length());
		} else {
			fileName = damDTO.getFileName();
		}

		// Common Message part
		reportingMessage = damDTO.getId()
				+ AcceleratorGenericConstants.CSV_SEPARATOR
				+ damDTO.getSourcePath()
				+ AcceleratorGenericConstants.FORWARD_SLASH + fileName
				+ AcceleratorGenericConstants.CSV_SEPARATOR
				+ damDTO.getDestinationPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH + fileName
				+ AcceleratorGenericConstants.CSV_SEPARATOR + modifiedDate
				+ AcceleratorGenericConstants.CSV_SEPARATOR
				+ damDTO.getUserName()
				+ AcceleratorGenericConstants.CSV_SEPARATOR;

		// Adding success or failure description
		if (isUploaded) {
			if (!isUpdated) {
				reportingMessage = reportingMessage
						+ AcceleratorGenericConstants.STATUS_SUCCESS
						+ AcceleratorGenericConstants.CSV_SEPARATOR
						+ AcceleratorGenericConstants.SUCCESS_DESC;
			} else {
				reportingMessage = reportingMessage
						+ AcceleratorGenericConstants.STATUS_SUCCESS
						+ AcceleratorGenericConstants.CSV_SEPARATOR
						+ AcceleratorGenericConstants.SUCCESS_UPDATED_DESC;
			}
		} else {
			reportingMessage = reportingMessage
					+ AcceleratorGenericConstants.STATUS_FAILURE
					+ AcceleratorGenericConstants.CSV_SEPARATOR
					+ AcceleratorGenericConstants.FAILURE_DESC;
		}

		// Add message to the reports log
		try {
			ReportGeneratorHelper.addOrUpdateMessage(reportFilePath,
					reportingMessage);
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName
					+ " while adding or updating report", e);
		}
		LOGGER.info(" || " + methodName + " || END");
	}

	private static File getModifiedFileName(File singleFile) {
		String methodName = "getModifiedFileName";
		LOGGER.info(" || " + methodName + " || START");

		File modifiedFile = null;
		try {
			LOGGER.debug(" || " + methodName + " || singleFile.getName() || "
					+ singleFile.getName());
			if (singleFile.getName().contains(" ")) {
				LOGGER.debug(" || " + methodName
						+ " || file name contains space");
				LOGGER.debug(" || " + methodName
						+ " || singleFile.getPath() || " + singleFile.getPath());
				String modifiedName = singleFile.getName().replace(" ", "");
				String modifiedFilePath = singleFile.getPath().substring(0,
						singleFile.getPath().lastIndexOf("\\") + 1)
						+ modifiedName;
				LOGGER.debug(" || " + methodName + " || modifiedFilePath || "
						+ modifiedFilePath);
				modifiedFile = new File(modifiedFilePath);
				LOGGER.debug(" || " + methodName
						+ " || modifiedFile.getPath() || "
						+ modifiedFile.getPath());
				FileUtils.copyFile(singleFile, modifiedFile);
				AcceleratorFileUtils.deleteFile(
						singleFile.getPath().substring(
								singleFile.getPath().lastIndexOf("\\") + 1,
								singleFile.getPath().length()),
						singleFile.getPath().substring(0,
								singleFile.getPath().lastIndexOf("\\")));
				modifiedFile.createNewFile();
			} else {
				LOGGER.debug(" || " + methodName
						+ " || file name does not contain space");
				modifiedFile = singleFile;
			}
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || Exception occured || "
					+ e.getMessage());
		} catch (IOException e) {
			LOGGER.error(" || " + methodName + " || Exception occured || "
					+ e.getMessage());
		}
		LOGGER.info(" || " + methodName + " || END");
		return modifiedFile;
	}
}