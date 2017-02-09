package com.cts.accelerators.migration.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.core.util.AcceleratorCRXUtils;
import com.cts.accelerators.core.util.AcceleratorExcelUtils;
import com.cts.accelerators.core.util.AcceleratorFileUtils;
import com.cts.accelerators.core.util.AcceleratorTaxonomyUtilis;
import com.cts.accelerators.core.util.AcceleratorUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.ootbcomps.dto.ContainerDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CoreDTO;
import com.cts.accelerators.migration.ootbcomps.dto.DAMDTO;
import com.cts.accelerators.migration.ootbcomps.dto.DamPropertyDTO;
import com.cts.accelerators.migration.ootbcomps.dto.FlashDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.PageComponentDTO;
import com.cts.accelerators.migration.ootbcomps.dto.PagePropertiesDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TagDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TextDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TextImageDTO;
import com.cts.accelerators.migration.services.dto.ContentImporterServiceRequest;
import com.cts.accelerators.migration.services.dto.DamUploadServiceRequest;
import com.cts.accelerators.migration.services.dto.DamUploadServiceResponse;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * This is the helper class for Content importer Service to carry out content
 * movement activity smoothly
 * 
 * @author cognizant
 * 
 * 
 */

public class ContentImporterHelper {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentImporterHelper.class);

	/**
	 * This method will convert Xml to object
	 * 
	 * @return coreObject
	 * @throws AcceleratorException
	 */

	@SuppressWarnings("finally")
	public static CoreDTO convertXMLToObject(File file)
			throws AcceleratorException {
		String methodName = "convertXMLToObject";
		CoreDTO coreObject = null;
		boolean isSuccess = false;
		LOGGER.info(" || " + methodName + " || START");
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(PageComponentDTO.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new MigrationEventHandler());
			coreObject = (CoreDTO) jaxbUnmarshaller.unmarshal(file);
			isSuccess = true;
			return coreObject;
		} catch (JAXBException e) {
			LOGGER.error(
					"An exception has occured in ContentImporterHelper.convertXMLToObject()",
					e);
			throw new AcceleratorException(AcceleratorFaultCode.JAXB_EXCEPTION,
					ContentImporterHelper.class.getName(),
					" convertXMLToObject()", e.getCause());
		} finally {
			if (!isSuccess) {
				LOGGER.info(" || " + methodName + " || END");
				return null;
			} else {
				LOGGER.info(" || " + methodName + " || END");
				return coreObject;
			}
		}
	}

	/**
	 * This method Creates content for SampleDam
	 * 
	 * @param serviceRequest
	 * @throws AcceleratorException
	 */
	public static void createSampleDamContent(
			ContentImporterServiceRequest serviceRequest)
			throws AcceleratorException {
		String methodName = "createSampleDamContent";
		LOGGER.info(" || " + methodName + " || START");

		DAMDTO damdto = new DAMDTO();
		damdto.setDestinationPath("/content/dam/temp");
		damdto.setHost("localhost");
		damdto.setPassword("admin");
		damdto.setPort("4502");
		damdto.setProxyaddress("proxy.cognizant.com");
		damdto.setProxyport("6050");
		damdto.setUserName("admin");
		// for Single file Upload

		damdto.setSourcePath("D:/images");
		damdto.setFileName("image.png");

		List<DamPropertyDTO> list = new ArrayList<DamPropertyDTO>();
		DamPropertyDTO prop1 = new DamPropertyDTO();
		prop1.setKey("Illinois");
		prop1.setValue("Springfield");
		list.add(prop1);

		prop1 = new DamPropertyDTO();
		prop1.setKey("Missouri");
		prop1.setValue("Jefferson City");
		list.add(prop1);

		damdto.setCustomMetaProperties(list);

		// For multi file upload
		String sourcePath = serviceRequest.getSourceRootPath();

		AcceleratorUtils.convertObjectToXML(damdto, sourcePath);
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This is the method will import contents of a single type and move the
	 * files to success or failure folder
	 * 
	 * @param importRequest
	 * @param importType
	 * @throws AcceleratorException
	 */
	public static JSONObject importToCRX(
			ContentImporterServiceRequest importRequest, String importType,
			String reportPath) throws AcceleratorException {
		String methodName = "importToCRX";
		LOGGER.info(" || " + methodName + " || START");

		LOGGER.debug(" || " + methodName + " || importRequest || "
				+ importRequest + " || importType || " + importType
				+ " reportPath ||" + reportPath);

		JSONObject jsonResponse = null;
		// Call respective import methods based on the importType
		if (importType != null
				&& AcceleratorGenericConstants.IMPORT_TYPE_DAM
						.equals(importType)) {
			jsonResponse = importDAMContents(importRequest);
			ReportGeneratorHelper.addToPropertyFile(reportPath,
					AcceleratorGenericConstants.IMPORT_TYPE_DAM);
		} else if (importType != null
				&& AcceleratorGenericConstants.IMPORT_TYPE_TAG
						.equals(importType)) {
			jsonResponse = importTagContents(importRequest, reportPath);
		} else if (importType != null
				&& AcceleratorGenericConstants.IMPORT_TYPE_PAGE
						.equals(importType)) {
			if (importRequest.getTransformationType().equalsIgnoreCase(
					AcceleratorGenericConstants.XML)) {
				jsonResponse = importPageContents(importRequest);
			} else if (importRequest.getTransformationType().equalsIgnoreCase(
					AcceleratorGenericConstants.EXCEL)) {
				jsonResponse = importPageContentsFromExcel(importRequest);
			}
			ReportGeneratorHelper.addToPropertyFile(reportPath,
					AcceleratorGenericConstants.IMPORT_TYPE_PAGE);
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	/**
	 * This method will be used to import DAM contents
	 * 
	 * @param importRequest
	 */
	private static JSONObject importDAMContents(
			ContentImporterServiceRequest importRequest) {
		String methodName = "importDAMContents";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject jsonResponse = new JSONObject();

		DamUploadServiceRequest damRequest = new DamUploadServiceRequest();
		damRequest.setCompleteMigration(true);
		damRequest.setLoadDefault(true);
		damRequest.setSourceRootPath(importRequest.getSourceRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.IMPORT_TYPE_DAM);
		damRequest.setFileDestinationRootPath(importRequest
				.getFileDestinationRootPath());
		damRequest.setCqRootPath(importRequest.getDamStoragePath());
		damRequest
				.setEnableProxy(importRequest.getDamRequest().isEnableProxy());
		damRequest.setTransformationType(importRequest.getTransformationType());

		// Get other required parameters for executing CURL command
		if (importRequest.getDamRequest() != null) {
			damRequest.setLocalhost(importRequest.getDamRequest()
					.getLocalhost());
			damRequest.setPort(importRequest.getDamRequest().getPort());
			damRequest.setUsername(importRequest.getDamRequest().getUsername());
			damRequest.setPassword(importRequest.getDamRequest().getPassword());
			damRequest.setProxyHost(importRequest.getDamRequest()
					.getProxyHost());
			damRequest.setProxyPort(importRequest.getDamRequest()
					.getProxyPort());
			jsonResponse = ((DamUploadServiceResponse) DamUploadHelper
					.damBulkUpload(
							damRequest,
							importRequest.getReportRootPaths().get(
									AcceleratorGenericConstants.DAM_REPORT)))
					.getJsonResponse();
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	/**
	 * This method will be called to import taxonomy contents
	 * 
	 * @param importRequest
	 * @param reportRootPath
	 */
	private static JSONObject importTagContents(
			ContentImporterServiceRequest importRequest, String reportRootPath) {
		String methodName = "importTagContents";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject jsonResponse = new JSONObject();

		// Report Path
		String reportPath = "";
		if (importRequest.getReportRootPaths().get(
				AcceleratorGenericConstants.TAXONOMY_REPORT) != null) {
			String[] reportPathValues = importRequest.getReportRootPaths()
					.get(AcceleratorGenericConstants.TAXONOMY_REPORT)
					.split(",");

			if (reportPathValues.length > 0) {
				reportPath = reportPathValues[0]
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ reportPathValues[1];
			}
		}

		LOGGER.debug(" || " + methodName + " || Tag Report file path -> "
				+ reportPath);

		// Success and Failure folders
		String failurePath = importRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_FAILURE;
		String successPath = importRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_SUCCESS;

		// Iterate over tag files and import contents
		File[] tagFiles = new File(importRequest.getSourceRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.IMPORT_TYPE_TAG).listFiles();
		TagDTO tagDTO = new TagDTO();
		List<TagDTO> tagDTOList = new ArrayList<TagDTO>();
		boolean tagCreated = false;
		for (File tagFile : tagFiles) {
			tagCreated = false;
			if (importRequest.getTransformationType().equalsIgnoreCase(
					AcceleratorGenericConstants.XML)
					&& tagFile.getName().endsWith(
							AcceleratorGenericConstants.XML)) {
				LOGGER.debug(" || " + methodName + " || XML");
				try {
					tagDTO = (TagDTO) convertXMLToObject(tagFile);
					tagCreated = uploadTag(importRequest, reportRootPath,
							methodName, successPath, tagDTO, tagCreated,
							tagFile);
				} catch (AcceleratorException e) {
					LOGGER.error(" || " + methodName + " || ACC EXCEPTION");
				} finally {
					// Move original file to failure folder
					if (!tagCreated) {
						try {
							AcceleratorFileUtils
									.moveOrCopyFileOnServer(
											tagFile,
											failurePath
													+ AcceleratorGenericConstants.FORWARD_SLASH
													+ AcceleratorGenericConstants.IMPORT_TYPE_TAG,
											true);
						} catch (AcceleratorException e) {
							LOGGER.error(" || " + methodName
									+ " || ACC EXCEPTION");
						}

						// Log status report
						String tagId = tagDTO != null ? tagDTO.getTagId()
								: tagFile.getName();
						addToReportLog(
								tagId,
								tagDTO.getParentID(),
								importRequest.getTaxonomyStoragePath()
										+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
										+ tagDTO.getParentID()
										+ AcceleratorGenericConstants.FORWARD_SLASH
										+ tagDTO.getName(),
								AcceleratorGenericConstants.STATUS_FAILURE,
								AcceleratorGenericConstants.FAILURE_DESC,
								importRequest
										.getReportRootPaths()
										.get(AcceleratorGenericConstants.TAXONOMY_REPORT));
					}
				}
			} else if (importRequest.getTransformationType().equalsIgnoreCase(
					AcceleratorGenericConstants.EXCEL)
					&& tagFile.getName().endsWith(
							AcceleratorGenericConstants.EXCEL_FILE_EXTENSION)) {
				LOGGER.debug(" || " + methodName + " || EXCEL");
				tagDTOList = AcceleratorExcelUtils.setTagDTO(tagFile);
				for (TagDTO tagDTOItem : tagDTOList) {
					try {
						tagCreated = uploadTag(importRequest, reportRootPath,
								methodName, successPath, tagDTOItem,
								tagCreated, tagFile);
					} catch (AcceleratorException e) {
						LOGGER.error(" || " + methodName + " || ACC EXCEPTION");
					} finally {
						// Move original file to failure folder
						if (!tagCreated) {

							// Log status report
							String tagId = tagDTOItem != null ? tagDTOItem
									.getTagId() : tagFile.getName();
							addToReportLog(
									tagId,
									tagDTOItem.getParentID(),
									importRequest.getTaxonomyStoragePath()
											+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
											+ tagDTOItem.getParentID()
											+ AcceleratorGenericConstants.FORWARD_SLASH
											+ tagDTOItem.getName(),
									AcceleratorGenericConstants.STATUS_FAILURE,
									AcceleratorGenericConstants.FAILURE_DESC,
									importRequest
											.getReportRootPaths()
											.get(AcceleratorGenericConstants.TAXONOMY_REPORT));
						}
					}
				}
			}

		}
		LOGGER.info(" || " + methodName + " || END");
		try {
			jsonResponse = ReportGeneratorHelper.fetchConciseReportWithFolders(
					reportPath, successPath, failurePath);
		} catch (AcceleratorException e) {

			e.printStackTrace();
		}
		return jsonResponse;
	}

	/**
	 * @param importRequest
	 * @param reportRootPath
	 * @param methodName
	 * @param successPath
	 * @param tagDTO
	 * @param tagCreated
	 * @param tagFile
	 * @return
	 * @throws AcceleratorException
	 */
	private static boolean uploadTag(
			ContentImporterServiceRequest importRequest, String reportRootPath,
			String methodName, String successPath, TagDTO tagDTO,
			boolean tagCreated, File tagFile) throws AcceleratorException {
		if (null != tagDTO) {
			LOGGER.debug(" || " + methodName + " || Tag Method -> "
					+ tagCreated);
			if (StringUtils.isNotEmpty(importRequest.getTaxonomyStoragePath())
					&& StringUtils.isNotEmpty(tagDTO.getTagId())) {
				String tagPath = importRequest.getTaxonomyStoragePath();
				String tagId = tagDTO.getTagId() != null ? tagDTO.getTagId()
						: tagDTO.getName();
				String parentPath = tagDTO.getParentID();
				Map<String, String> map = new HashMap<String, String>();
				map = ReportGeneratorHelper
						.getPropertiesMap(AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
								+ AcceleratorGenericConstants.TAG_FILES_MAPPING_PROPERTIES);
				String val = (String) map.get(tagDTO.getParentID());
				if (StringUtils.isNotEmpty(val)) {
					parentPath = val
							.substring(val
									.indexOf(AcceleratorGenericConstants.TAG_FIELD_SEPARATOR) + 1);
					;
					LOGGER.debug(" || " + methodName
							+ " || value_>>>>>>>>>>>  -> " + val);

				}
				String componentID = tagPath
						+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
						+ parentPath
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ tagDTO.getName();
				LOGGER.debug(" || " + methodName + " || componentID  -> "
						+ componentID);
				tagDTO.setParentID(tagPath
						.endsWith(AcceleratorGenericConstants.TAG_FIELD_SEPARATOR) ? tagPath
						+ parentPath
						: tagPath
								+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
								+ parentPath);
				LOGGER.debug(" || " + methodName + " || Tag Method -> "
						+ tagCreated);
				tagCreated = AcceleratorTaxonomyUtilis.createTag(tagDTO);
				LOGGER.debug(" || " + methodName + " || Tag Method -> "
						+ tagCreated);
				if (tagCreated) {
					if (importRequest.getTransformationType().equalsIgnoreCase(
							AcceleratorGenericConstants.XML)) {
						AcceleratorFileUtils
								.moveOrCopyFileOnServer(
										tagFile,
										successPath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ AcceleratorGenericConstants.IMPORT_TYPE_TAG,
										true);
					}

					// Log status report
					addToReportLog(
							tagId,
							parentPath,
							componentID,
							AcceleratorGenericConstants.STATUS_SUCCESS,
							AcceleratorGenericConstants.SUCCESS_DESC,
							importRequest
									.getReportRootPaths()
									.get(AcceleratorGenericConstants.TAXONOMY_REPORT));
					ReportGeneratorHelper.addToPropertyFile(reportRootPath,
							AcceleratorGenericConstants.IMPORT_TYPE_TAG);
				}
			}
		} else {
			throw new AcceleratorException(
					AcceleratorFaultCode.EMPTY_TAG_HIERARCHY,
					ContentImporterHelper.class.getName(), methodName);
		}
		return tagCreated;
	}

	/**
	 * This method will be called to import page contents
	 * 
	 * @param importRequest
	 */
	private static JSONObject importPageContents(
			ContentImporterServiceRequest importRequest) {
		String methodName = "importPageContents";
		LOGGER.info(" || " + methodName + " || START");
		JSONObject jsonResponse = new JSONObject();

		// Report Path
		String reportPath = "";
		if (importRequest.getReportRootPaths().get(
				AcceleratorGenericConstants.IMPORT_REPORT) != null) {
			String[] reportPathValues = importRequest.getReportRootPaths()
					.get(AcceleratorGenericConstants.IMPORT_REPORT).split(",");

			if (reportPathValues.length > 0) {
				reportPath = reportPathValues[0]
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ reportPathValues[1];
			}
		}
		LOGGER.debug(" || " + methodName + " || Page Report file path -> "
				+ reportPath);

		// Success and Failure folders
		String failurePath = importRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_FAILURE;
		String successPath = importRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_SUCCESS;

		// Iterate over page files and import contents
		File[] pageFiles = new File(importRequest.getSourceRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.IMPORT_TYPE_PAGE).listFiles();

		boolean pageCreationFailed = false;
		JSONObject obj = new JSONObject();
		for (File pageFile : pageFiles) {
			pageCreationFailed = false;
			try {
				PageComponentDTO pageObject = (PageComponentDTO) convertXMLToObject(pageFile);
				obj = createPageFromObject(importRequest, successPath,
						pageFile, pageObject);
				if (obj.has("true")) {
					pageObject = (PageComponentDTO) obj.get("true");
					pageCreationFailed = true;
				} else if (obj.has("false")) {
					pageObject = (PageComponentDTO) obj.get("false");
					pageCreationFailed = false;
				}
			} catch (AcceleratorException e) {
				pageCreationFailed = true;
				LOGGER.error(" || " + methodName
						+ " || AcceleratorException || " + e.getMessage());
			} catch (JSONException e) {
				LOGGER.error(" || " + methodName + " || JSONException || "
						+ e.getMessage());
			} finally {
				// Move original file to failure folder
				if (pageCreationFailed) {
					try {
						AcceleratorFileUtils
								.moveOrCopyFileOnServer(
										pageFile,
										failurePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ AcceleratorGenericConstants.IMPORT_TYPE_PAGE,
										true);
					} catch (AcceleratorException e) {

						e.printStackTrace();
					}

					// Log status report
					addToPageReportLog(
							pageFile.getAbsolutePath(),
							importRequest.getPageStoragePath(),
							AcceleratorGenericConstants.STATUS_FAILURE,
							AcceleratorGenericConstants.FAILURE_DESC,
							importRequest.getReportRootPaths().get(
									AcceleratorGenericConstants.IMPORT_REPORT));
				}
			}
		}

		try {
			jsonResponse = ReportGeneratorHelper.fetchConciseReportWithFolders(
					reportPath, successPath, failurePath);
			LOGGER.debug(" || " + methodName + " || Report Path " + reportPath);
			LOGGER.debug(" || " + methodName + " || Report Log at the end "
					+ jsonResponse.toString());
		} catch (AcceleratorException e) {

			e.printStackTrace();
		}

		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	private static JSONObject createPageFromObject(
			ContentImporterServiceRequest importRequest, String successPath,
			File pageFile, PageComponentDTO pageObject)
			throws AcceleratorException {
		String methodName = "createPageFromObject";
		LOGGER.info(" || " + methodName + " || START");

		JSONObject obj = new JSONObject();
		boolean pageCreationFailed = false;

		try {
			if (null != pageObject) {
				if (StringUtils.isNotEmpty(importRequest.getPageStoragePath())
						&& StringUtils.isNotEmpty(pageObject
								.getParentNodePath())) {
					String pagePath = importRequest.getPageStoragePath();
					String parentNodePath = pageObject.getParentNodePath();
					pageObject.setCqDesignPath(importRequest
							.getPageStoragePath());
					pageObject
							.setParentNodePath(pagePath
									.endsWith(AcceleratorGenericConstants.FORWARD_SLASH) ? pagePath
									+ parentNodePath
									: pagePath
											+ AcceleratorGenericConstants.FORWARD_SLASH
											+ parentNodePath);
				}
			} else {
				pageCreationFailed = true;
				throw new AcceleratorException(
						AcceleratorFaultCode.EMPTY_PAGE_HIERARCHY,
						ContentImporterHelper.class.getName(), methodName);
			}
			LOGGER.debug(" || " + methodName
					+ " || pageObject.getParentNodePath() || "
					+ pageObject.getParentNodePath());

			// Update Image Asset References
			pageObject = updateDAMReferences(
					pageObject,
					importRequest.getReportRootPaths().get(
							AcceleratorGenericConstants.DAM_REPORT));

			// Create and Update Tag References
			pageObject = updateTagReferences(pageObject);

			// updateTagReferences(pageObject);
			// Create Page
			if (AcceleratorCRXUtils.createPage(pageObject,
					importRequest.isEnableDuplication() ? false
							: !importRequest.isDisableUpdate(), importRequest
							.isEnableDuplication())) {

				// Move Original File to Success folder
				AcceleratorFileUtils.moveOrCopyFileOnServer(pageFile,
						successPath + AcceleratorGenericConstants.FORWARD_SLASH
								+ AcceleratorGenericConstants.IMPORT_TYPE_PAGE,
						true);

				// Log status report
				addToPageReportLog(
						pageFile.getAbsolutePath(),
						importRequest.getPageStoragePath(),
						AcceleratorGenericConstants.STATUS_SUCCESS,
						AcceleratorGenericConstants.SUCCESS_DESC,
						importRequest.getReportRootPaths().get(
								AcceleratorGenericConstants.IMPORT_REPORT));
			} else {
				pageCreationFailed = true;
			}
			LOGGER.debug(" || " + methodName + " || pageCreationFailed || "
					+ pageCreationFailed);
			if (pageCreationFailed) {
				obj.put("true", pageObject);
			} else {
				obj.put("false", pageObject);
			}
		} catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || JSONException || "
					+ e.getMessage());
		}

		LOGGER.info(" || " + methodName + " || END");
		return obj;
	}

	/**
	 * This method will update those digital asset references which have been
	 * migrated using this tool
	 * 
	 * @param pageComponentDTO
	 * @param reportRootPath
	 * @return
	 * @throws AcceleratorException
	 */
	private static PageComponentDTO updateDAMReferences(
			PageComponentDTO pageComponentDTO, String reportRootPath)
			throws AcceleratorException {
		String methodName = "updateDAMReferences";
		LOGGER.info(" || " + methodName + " || START");
		File[] reportFiles = AcceleratorFileUtils
				.listFiles(reportRootPath
						.split(AcceleratorGenericConstants.CSV_SEPARATOR)[0],
						"csv");
		Map<String, String> imagePathMap = new HashMap<String, String>();
		if (null != pageComponentDTO && null != reportFiles) {
			for (File reportFile : reportFiles) {
				if (reportFile.getName().contains(
						AcceleratorGenericConstants.DAM_REPORT)) {
					String line = "";
					BufferedReader bufferedReader = null;
					try {
						bufferedReader = new BufferedReader(new FileReader(
								reportFile));
						while ((line = bufferedReader.readLine()) != null) {
							// use comma as separator
							String[] importPaths = line
									.split(AcceleratorGenericConstants.RECORD_SEPRARTOR);
							if (importPaths.length >= 5
									&& importPaths[4]
											.equals(AcceleratorGenericConstants.STATUS_SUCCESS)) {
								imagePathMap
										.put(importPaths[0], importPaths[1]);
							}
						}
					} catch (FileNotFoundException e) {
						LOGGER.error("An exception has occured in " + "."
								+ methodName, e);
						throw new AcceleratorException(
								AcceleratorFaultCode.FILE_NOT_FOUND,
								ContentImporterHelper.class.getName(),
								methodName, e.getCause());
					} catch (IOException e) {
						LOGGER.error("An exception has occured in " + "."
								+ methodName, e);
						throw new AcceleratorException(
								AcceleratorFaultCode.IOEXCEPTION_STRING,
								ContentImporterHelper.class.getName(),
								methodName, e.getCause());
					}
					break;
				}
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Image Path Map: " + imagePathMap);
			}
			List<ContainerDTO> authorComponents = pageComponentDTO
					.getAuthorComponentsList();
			if (null != authorComponents) {
				for (ContainerDTO containerDTO : authorComponents) {
					List<CoreDTO> coreComponents = containerDTO.getComponents();
					for (CoreDTO coreDTO : coreComponents) {
						if (coreDTO instanceof TextImageDTO) {
							TextImageDTO textImageDTO = (TextImageDTO) coreDTO;
							ImageDTO imageDTO = textImageDTO.getImageDTO();
							String fileReference = imageDTO.getFileReference();
							String damPath = imagePathMap.get(fileReference);
							if (StringUtils.isNotEmpty(damPath)) {
								imageDTO.setFileReference(damPath);
							}
							textImageDTO.setImageDTO(imageDTO);
							coreDTO = textImageDTO;
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Inside TextImageDTO >>>>>>>> fileReference: "
										+ fileReference + " damPath:" + damPath);
							}
						} else if (coreDTO instanceof ImageDTO) {
							ImageDTO imageDTO = (ImageDTO) coreDTO;
							String fileReference = imageDTO.getFileReference();
							String damPath = imagePathMap.get(fileReference);
							if (StringUtils.isNotEmpty(damPath)) {
								imageDTO.setFileReference(damPath);
							}
							coreDTO = imageDTO;
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Inside ImageDTO >>>>>>>> fileReference: "
										+ fileReference + " damPath:" + damPath);
							}
						} else if (coreDTO instanceof FlashDTO) {
							FlashDTO flashDTO = (FlashDTO) coreDTO;
							ImageDTO imageDTO = flashDTO
									.getHtml5SmartImageDTO();
							String fileReference = imageDTO.getFileReference();
							String damPath = imagePathMap.get(fileReference);
							String flashDamPath = imagePathMap.get(flashDTO
									.getFileReference());
							if (StringUtils.isNotEmpty(damPath)) {
								imageDTO.setFileReference(damPath);
							}
							if (StringUtils.isNotEmpty(flashDamPath)) {
								flashDTO.setFileReference(flashDamPath);
							}
							flashDTO.setHtml5SmartImageDTO(imageDTO);
							coreDTO = flashDTO;
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Inside FlashDTO >>>>>>>> fileReference: "
										+ fileReference + " damPath:" + damPath);
							}
						}
					}
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return pageComponentDTO;
	}

	/**
	 * This method will be used for creating as well as updating tag asset
	 * references for those tags which were created using this tool
	 * 
	 * @param pageObject
	 * @param reportRootPath
	 * @throws AcceleratorException
	 */
	private static PageComponentDTO updateTagReferences(
			PageComponentDTO pageObject) throws AcceleratorException {

		String methodName = "updateTagReferences";
		LOGGER.info(" || " + methodName + " || START");

		Tag taxonomy = null;
		ResourceResolver resorceResolver = ConnectionManager
				.getResourceResolver();
		TagManager tagManager = resorceResolver.adaptTo(TagManager.class);
		TagDTO tagDTO = new TagDTO();

		if (StringUtils.isNotEmpty(pageObject.getTagIds())) {
			String[] tagIdsList = pageObject.getTagIds().split(
					AcceleratorGenericConstants.CSV_SEPARATOR);
			if (null != tagIdsList) {
				for (String tagId : tagIdsList) {
					if (tagId
							.endsWith(AcceleratorGenericConstants.FORWARD_SLASH)) {
						tagId = tagId.substring(0, tagId.length() - 1);
					}
					taxonomy = tagManager.resolve(tagId);
					if (null == taxonomy) {
						tagDTO.setTagId(tagId
								.contains(AcceleratorGenericConstants.FORWARD_SLASH) ? tagId.substring(
								0,
								tagId.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH))
								: tagId.substring(
										0,
										tagId.lastIndexOf(AcceleratorGenericConstants.TAG_FIELD_SEPARATOR)));
						tagDTO.setJcr_title(tagId
								.contains(AcceleratorGenericConstants.FORWARD_SLASH) ? tagId.substring(tagId
								.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH) + 1)
								: tagId.substring(tagId
										.lastIndexOf(AcceleratorGenericConstants.TAG_FIELD_SEPARATOR) + 1));
						tagDTO.setName(tagDTO.getJcr_title());
						AcceleratorTaxonomyUtilis.createTag(tagDTO);
					}
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return pageObject;
	}

	/**
	 * This method will be called for adding log messages for individual page
	 * file import
	 * 
	 * @param tagFile
	 * @param destinationPath
	 * @param reportFilePath
	 */
	private static void addToPageReportLog(String tagFile,
			String taxonomyRootPath, String status, String statusDescription,
			String reportFilePath) {
		String methodName = "addToPageReportLog";
		LOGGER.info(" || " + methodName + " || START");

		String reportingMessage;

		// Common Message part
		reportingMessage = tagFile + AcceleratorGenericConstants.CSV_SEPARATOR
				+ taxonomyRootPath + AcceleratorGenericConstants.CSV_SEPARATOR
				+ status + AcceleratorGenericConstants.CSV_SEPARATOR
				+ statusDescription;

		// Add message to the reports log
		try {
			ReportGeneratorHelper.addOrUpdateMessage(reportFilePath,
					reportingMessage);
			LOGGER.debug(" || " + methodName + " || Report Path "
					+ reportFilePath);
		} catch (AcceleratorException e) {

			LOGGER.error(" || " + methodName
					+ " || Error writing to the reports log");
			e.printStackTrace();
		}
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method will be called for adding log messages for individual
	 * taxonomy file import
	 * 
	 * @param tagFile
	 * @param destinationPath
	 * @param reportFilePath
	 */
	private static void addToReportLog(String tagID, String parentID,
			String componentID, String status, String statusDescription,
			String reportFilePath) {
		String methodName = "addToReportLog";
		LOGGER.info(" || " + methodName + " || START");

		String reportingMessage;

		// Common Message part
		reportingMessage = tagID + AcceleratorGenericConstants.CSV_SEPARATOR
				+ parentID + AcceleratorGenericConstants.CSV_SEPARATOR
				+ componentID + AcceleratorGenericConstants.CSV_SEPARATOR
				+ status + AcceleratorGenericConstants.CSV_SEPARATOR
				+ statusDescription;

		// Add message to the reports log
		try {
			ReportGeneratorHelper.addOrUpdateMessage(reportFilePath,
					reportingMessage);
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName
					+ " || Error writing to the reports log");
		}
		LOGGER.info(" || " + methodName + " || END");
	}

	public static JSONObject consolidateJSON(JSONObject firstJSON,
			JSONObject secondJSON) {
		String methodName = "consolidateJSON";
		LOGGER.info(" || " + methodName + " || START");
		String reportPath = "";
		JSONObject jsonResponse = new JSONObject();
		JSONObject jsonSuccess = new JSONObject();
		JSONObject jsonFailure = new JSONObject();
		try {
			// Consolidate total response
			if (firstJSON != null && secondJSON != null) {
				if (firstJSON.get(AcceleratorGenericConstants.TOTAL_RECORDS) != null) {
					if (secondJSON
							.get(AcceleratorGenericConstants.TOTAL_RECORDS) != null) {
						int totalRecords = firstJSON
								.getInt(AcceleratorGenericConstants.TOTAL_RECORDS)
								+ secondJSON
										.getInt(AcceleratorGenericConstants.TOTAL_RECORDS);
						jsonResponse.put(
								AcceleratorGenericConstants.TOTAL_RECORDS,
								totalRecords);
					}
				}
				// Consolidate success response
				if (firstJSON.get(AcceleratorGenericConstants.STATUS_SUCCESS) != null) {
					if (secondJSON
							.get(AcceleratorGenericConstants.STATUS_SUCCESS) != null) {
						int count = ((JSONObject) firstJSON
								.get(AcceleratorGenericConstants.STATUS_SUCCESS))
								.getInt(AcceleratorGenericConstants.COUNT)
								+ ((JSONObject) secondJSON
										.get(AcceleratorGenericConstants.STATUS_SUCCESS))
										.getInt(AcceleratorGenericConstants.COUNT);
						jsonSuccess.put(AcceleratorGenericConstants.COUNT,
								count);
						reportPath = ((JSONObject) firstJSON
								.get(AcceleratorGenericConstants.STATUS_SUCCESS))
								.get(AcceleratorGenericConstants.REPORT_PATH)
								.toString();
						if (reportPath
								.indexOf(AcceleratorGenericConstants.FORWARD_SLASH) > 1) {
							reportPath = reportPath
									.substring(
											0,
											reportPath
													.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH));
						}
						jsonSuccess.put(
								AcceleratorGenericConstants.REPORT_PATH,
								reportPath);
						jsonResponse.put(
								AcceleratorGenericConstants.STATUS_SUCCESS,
								jsonSuccess);
					}
				}
				// Consolidate failure response
				if (firstJSON.get(AcceleratorGenericConstants.STATUS_FAILURE) != null) {
					if (secondJSON
							.get(AcceleratorGenericConstants.STATUS_FAILURE) != null) {
						int count = ((JSONObject) firstJSON
								.get(AcceleratorGenericConstants.STATUS_FAILURE))
								.getInt(AcceleratorGenericConstants.COUNT)
								+ ((JSONObject) secondJSON
										.get(AcceleratorGenericConstants.STATUS_FAILURE))
										.getInt(AcceleratorGenericConstants.COUNT);
						LOGGER.info(" || " + methodName
								+ " || First JSON Count -> " + count);
						LOGGER.info(" || " + methodName
								+ " || Second JSON Count -> " + count);
						LOGGER.info(" || " + methodName
								+ " || Failure Count -> " + count);
						jsonFailure.put(AcceleratorGenericConstants.COUNT,
								count);
						reportPath = ((JSONObject) firstJSON
								.get(AcceleratorGenericConstants.STATUS_FAILURE))
								.get(AcceleratorGenericConstants.REPORT_PATH)
								.toString();
						if (reportPath
								.indexOf(AcceleratorGenericConstants.FORWARD_SLASH) > 1) {
							reportPath = reportPath
									.substring(
											0,
											reportPath
													.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH));
						}
						jsonFailure.put(
								AcceleratorGenericConstants.REPORT_PATH,
								reportPath);
						jsonResponse.put(
								AcceleratorGenericConstants.STATUS_FAILURE,
								jsonFailure);
					}
				}
				// Set Detailed report path
				jsonResponse.put(AcceleratorGenericConstants.REPORT_PATH,
						firstJSON.get(AcceleratorGenericConstants.REPORT_PATH));
				jsonResponse.put(AcceleratorGenericConstants.DESCRIPTION,
						firstJSON.get(AcceleratorGenericConstants.DESCRIPTION));

			} else if (firstJSON == null) {
				jsonResponse = secondJSON;
			} else {
				jsonResponse = firstJSON;
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;
	}

	public static JSONObject importPageContentsFromExcel(
			ContentImporterServiceRequest importRequest) {
		String methodName = "importPageContentsFromExcel";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.debug(" || " + methodName + " || source path || "
				+ importRequest.getSourceRootPath());

		JSONObject jsonResponse = new JSONObject();

		// Report Path
		String reportPath = "";
		if (importRequest.getReportRootPaths().get(
				AcceleratorGenericConstants.IMPORT_REPORT) != null) {
			String[] reportPathValues = importRequest.getReportRootPaths()
					.get(AcceleratorGenericConstants.IMPORT_REPORT).split(",");

			if (reportPathValues.length > 0) {
				reportPath = reportPathValues[0]
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ reportPathValues[1];
			}
		}
		LOGGER.debug(" || " + methodName + " || Page Report file path || "
				+ reportPath);

		// Success and Failure folders
		String failurePath = importRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_FAILURE;
		String successPath = importRequest.getFileDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_SUCCESS;

		File[] pageFiles = new File(importRequest.getSourceRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.IMPORT_TYPE_PAGE).listFiles();

		PageComponentDTO pageDTO = new PageComponentDTO();
		List<PageComponentDTO> pageList = new ArrayList<PageComponentDTO>();
		PagePropertiesDTO pagePropertiesDTO = new PagePropertiesDTO();
		ContainerDTO containerDTO = new ContainerDTO();
		List<ContainerDTO> authorComponentsList = new ArrayList<ContainerDTO>();

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		boolean pageCreationFailed = false;

		for (File pageFile : pageFiles) {
			try {
				pageList = setObjectFromExcel(pageList, pagePropertiesDTO,
						authorComponentsList, pageFile);

				for (PageComponentDTO pageObject : pageList) {
					LOGGER.debug("|| " + methodName + " || pageObject || "
							+ pageObject.getNodeName());

					for (ContainerDTO conDTO : pageObject
							.getAuthorComponentsList()) {
						LOGGER.debug("|| " + methodName + " || conDTO || "
								+ conDTO.getContainerNodeName());
						LOGGER.debug("|| " + methodName + " || conDTO || "
								+ conDTO.getComponents().size());
					}

					jsonObj = createPageFromObject(importRequest, successPath,
							pageFile, pageObject);
					if (jsonObj.has("true")) {
						pageDTO = (PageComponentDTO) jsonObj.get("true");
						pageCreationFailed = true;
					} else if (jsonObj.has("false")) {
						pageDTO = (PageComponentDTO) jsonObj.get("false");
						pageCreationFailed = false;
					}
				}
			} catch (BiffException e) {
				LOGGER.error("|| " + methodName + " || BiffException || "
						+ e.getMessage());
			} catch (IOException e) {
				LOGGER.error("|| " + methodName + " || IOException || "
						+ e.getMessage());
			} catch (AcceleratorException e) {
				LOGGER.error("|| " + methodName
						+ " || AcceleratorException || " + e.getMessage());
			} catch (JSONException e) {
				LOGGER.error("|| " + methodName + " || JSONException || "
						+ e.getMessage());
			} finally {
				// Move original file to failure folder
				if (pageCreationFailed) {
					try {
						AcceleratorFileUtils
								.moveOrCopyFileOnServer(
										pageFile,
										failurePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ AcceleratorGenericConstants.IMPORT_TYPE_PAGE,
										true);
					} catch (AcceleratorException e) {
						LOGGER.error("|| " + methodName
								+ " || AcceleratorException || "
								+ e.getMessage());
					}

					// Log status report
					addToPageReportLog(
							pageFile.getAbsolutePath(),
							importRequest.getPageStoragePath(),
							AcceleratorGenericConstants.STATUS_FAILURE,
							AcceleratorGenericConstants.FAILURE_DESC,
							importRequest.getReportRootPaths().get(
									AcceleratorGenericConstants.IMPORT_REPORT));
				}
			}
		}

		try {
			jsonResponse = ReportGeneratorHelper.fetchConciseReportWithFolders(
					reportPath, successPath, failurePath);
			LOGGER.debug(" || " + methodName + " || reportPath || "
					+ reportPath);
			LOGGER.debug(" || "
					+ methodName
					+ " || Report Log at the end || jsonResponse.toString() || "
					+ jsonResponse.toString());
		} catch (AcceleratorException e) {
			LOGGER.error("|| " + methodName + " || AcceleratorException || "
					+ e.getMessage());
		}
		LOGGER.info(" || " + methodName + " || END");
		return jsonResponse;

	}

	private static List<PageComponentDTO> setObjectFromExcel(
			List<PageComponentDTO> pageList,
			PagePropertiesDTO pagePropertiesDTO,
			List<ContainerDTO> authorComponentsList, File pageFile)
			throws FileNotFoundException, IOException, BiffException {

		String methodName = "setObjectFromExcel";
		LOGGER.info(" || " + methodName + " || START");

		PageComponentDTO pageDTO = new PageComponentDTO();
		ContainerDTO containerDTO = new ContainerDTO();
		if (pageFile.getAbsolutePath().endsWith(
				AcceleratorGenericConstants.EXCEL_FILE_EXTENSION)) {
			FileInputStream fs = new FileInputStream(pageFile);
			Workbook wb = Workbook.getWorkbook(fs);

			Sheet sh = wb.getSheet("PageContents");
			int no_of_pages = Integer.parseInt(sh.getCell(
					sh.findCell(AcceleratorGenericConstants.NO_OF_PAGES)
							.getColumn(), 1).getContents());

			String pageId = "";
			int page = 1;

			for (int p = 1; p <= no_of_pages; p++) {
				LOGGER.debug(" || " + methodName + " || PAGE LOOP || p || " + p);
				pageDTO = new PageComponentDTO();
				authorComponentsList = new ArrayList<ContainerDTO>();
				pageId = sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PAGE_HEADERS[0])
										.getColumn(), page).getContents();
				LOGGER.debug(" || " + methodName + " || pageId || " + pageId);
				pageDTO.setNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PAGE_HEADERS[1])
										.getColumn(), page).getContents());
				pageDTO.setParentNodePath(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PAGE_HEADERS[2])
										.getColumn(), page).getContents());
				pageDTO.setResourceType(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PAGE_HEADERS[3])
										.getColumn(), page).getContents());
				pageDTO.setTemplate(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PAGE_HEADERS[4])
										.getColumn(), page).getContents());
				pageDTO.setJcr_title(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PAGE_HEADERS[5])
										.getColumn(), page).getContents());
				pageDTO.setCqDesignPath(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.PAGE_HEADERS[6])
										.getColumn(), page).getContents());

				pagePropertiesDTO
						.setHideInNav(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[0])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setNavTitle(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[1])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setOffTime(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[2])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setOnTime(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[3])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setPageTitle(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[4])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setSling_redirect(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[5])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setSling_vanityPath(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[6])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setSubtitle(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[7])
												.getColumn(), page)
								.getContents());
				pagePropertiesDTO
						.setJcr_description(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.PAGE_PROPERTIES_HEADERS[8])
												.getColumn(), page)
								.getContents());

				pageDTO.setPagePropertiesDTO(pagePropertiesDTO);

				int rowIndex = page;
				System.out
						.println("AccessSheetsInExcel.AccessSheets() || rowIndex out || "
								+ rowIndex);
				int no_of_author_components = Integer
						.parseInt(sh
								.getCell(
										sh.findCell(
												AcceleratorGenericConstants.AUTHOR_COMPONENT_HEADERS[0])
												.getColumn(), rowIndex)
								.getContents());
				int no_of_components = 0;
				for (int k = 0; k < no_of_author_components; k++) {
					containerDTO = new ContainerDTO();

					no_of_components = Integer
							.parseInt(sh
									.getCell(
											sh.findCell(
													AcceleratorGenericConstants.AUTHOR_COMPONENT_HEADERS[2])
													.getColumn(), rowIndex)
									.getContents());

					containerDTO = setContainerDTO(rowIndex, no_of_components,
							sh, wb);
					authorComponentsList.add(containerDTO);
					rowIndex = rowIndex + no_of_components;
				}
				pageDTO.setAuthorComponentsList(authorComponentsList);
				pageList.add(pageDTO);
				page = rowIndex;
				LOGGER.info(" || " + methodName + " || pageList.size() || "
						+ pageList.size());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return pageList;
	}

	private static ContainerDTO setContainerDTO(int row, int no_of_components,
			Sheet sh, Workbook wb) {
		String methodName = "setContainerDTO";
		LOGGER.info(" || " + methodName + " || START");

		ContainerDTO containerDTO = new ContainerDTO();
		CoreDTO coreDTO = null;
		List<CoreDTO> components = new ArrayList<CoreDTO>();
		containerDTO
				.setContainerVariance(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.AUTHOR_COMPONENT_HEADERS[3])
										.getColumn(), row).getContents());
		containerDTO
				.setContainerNodeName(sh
						.getCell(
								sh.findCell(
										AcceleratorGenericConstants.AUTHOR_COMPONENT_HEADERS[4])
										.getColumn(), row).getContents());

		String componentType = "";
		String componentId = "";

		for (int j = 0; j < no_of_components; j++) {
			sh = wb.getSheet("PageContents");
			componentType = sh
					.getCell(
							sh.findCell(
									AcceleratorGenericConstants.AUTHOR_COMPONENT_HEADERS[5])
									.getColumn(), row).getContents();
			componentId = sh
					.getCell(
							sh.findCell(
									AcceleratorGenericConstants.AUTHOR_COMPONENT_HEADERS[6])
									.getColumn(), row++).getContents();
			coreDTO = identifyAndSetComponent(componentType, componentId, wb);
			components.add(coreDTO);
		}
		containerDTO.setComponents(components);

		LOGGER.info(" || " + methodName + " || END");
		return containerDTO;
	}

	public static CoreDTO identifyAndSetComponent(String componentType,
			String componentId, Workbook wb) {
		String methodName = "identifyAndSetComponent";
		LOGGER.info(" || " + methodName + " || START");
		CoreDTO coreDTO = new CoreDTO();

		if (componentType.equalsIgnoreCase("TextComponent")) {
			coreDTO = AcceleratorExcelUtils.setTextDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("LoginComponent")) {
			coreDTO = AcceleratorExcelUtils.setLoginDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("FlashComponent")) {
			coreDTO = AcceleratorExcelUtils.setFlashDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ProfileComponent")) {
			coreDTO = AcceleratorExcelUtils.setProfileDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("SearchComponent")) {
			coreDTO = AcceleratorExcelUtils.setSearchDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("TextImageComponent")) {
			coreDTO = AcceleratorExcelUtils.setTextImageDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("VideoComponent")) {
			coreDTO = AcceleratorExcelUtils.setVideoDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ArchiveComponent")) {
			coreDTO = AcceleratorExcelUtils.setArchiveDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ChartComponent")) {
			coreDTO = AcceleratorExcelUtils.setChartDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("BreadcrumbComponent")) {
			coreDTO = AcceleratorExcelUtils.setBreadcrumbDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("DesignTitleComponent")) {
			coreDTO = AcceleratorExcelUtils.setDesignTitleDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ToolbarComponent")) {
			coreDTO = AcceleratorExcelUtils.setToolbarDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("TopnavComponent")) {
			coreDTO = AcceleratorExcelUtils.setTopnavDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("UserInfoComponent")) {
			coreDTO = AcceleratorExcelUtils.setUserInfoDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("CarouselComponent")) {
			coreDTO = AcceleratorExcelUtils.setCarouselDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("DownloadComponent")) {
			coreDTO = AcceleratorExcelUtils.setDownloadDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ExternalComponent")) {
			coreDTO = AcceleratorExcelUtils.setExternalDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ImageComponent")) {
			coreDTO = AcceleratorExcelUtils.setImageDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ListChildrenComponent")) {
			coreDTO = AcceleratorExcelUtils.setListChildrenDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ListComponent")) {
			coreDTO = AcceleratorExcelUtils.setListDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("EntryTextComponent")) {
			coreDTO = AcceleratorExcelUtils.setEntryTextDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("RedirectComponent")) {
			coreDTO = AcceleratorExcelUtils.setRedirectDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ReferenceComponent")) {
			coreDTO = AcceleratorExcelUtils.setReferenceDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("SiteMapComponent")) {
			coreDTO = AcceleratorExcelUtils.setSiteMapDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("SlideShowComponent")) {
			coreDTO = AcceleratorExcelUtils.setSlideShowDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("TableComponent")) {
			coreDTO = AcceleratorExcelUtils.setTableDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("TitleComponent")) {
			coreDTO = AcceleratorExcelUtils.setTitleDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("EntryListComponent")) {
			coreDTO = AcceleratorExcelUtils.setEntryListDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("CustomImageComponent")) {
			coreDTO = AcceleratorExcelUtils.setCustomImageDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("TitleLinkComponent")) {
			coreDTO = AcceleratorExcelUtils.setTitleLinkDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("CustomCarouselComponent")) {
			coreDTO = AcceleratorExcelUtils.setCustomCarouselDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ImageMultiFieldComponent")) {
			coreDTO = AcceleratorExcelUtils.setImageMultiFieldDTO(
					componentType, componentId, wb);
		} else if (componentType.equalsIgnoreCase("ImageTitleComponent")) {
			coreDTO = AcceleratorExcelUtils.setImageTitleDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ImageTitleLinkComponent")) {
			coreDTO = AcceleratorExcelUtils.setImageTitleLinkDTO(componentType,
					componentId, wb);
		} else if (componentType.equalsIgnoreCase("ListOfLinkComponent")) {
			coreDTO = AcceleratorExcelUtils.setListOfLinkDTO(componentType,
					componentId, wb);
		}
		LOGGER.info(" || " + methodName + " || END");
		return coreDTO;
	}

}
