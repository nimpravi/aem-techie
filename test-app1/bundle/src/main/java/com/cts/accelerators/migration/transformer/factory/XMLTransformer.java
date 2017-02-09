package com.cts.accelerators.migration.transformer.factory;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.util.AcceleratorFileUtils;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.helpers.ReportGeneratorHelper;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceResponse;

/**
 * This class takes of transforming source XMLs into the format governed by
 * provided XSL file
 * 
 * @author
 * 
 */
public class XMLTransformer extends TransformerAbstractFactory {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XMLTransformer.class);
	private static final String CLASS_NAME = XMLTransformer.class.getName();

	private Transformer transformer = null;

	private Pattern specialCharPattern = Pattern.compile("[&']*");

	/**
	 * The default constructor overload needed for the Factory configuration
	 * 
	 * @throws AcceleratorException
	 */
	public XMLTransformer() throws AcceleratorException {
		super(TransformerType.XML);
		String methodName = "XMLTransformer";
		LOGGER.info(" || " + methodName + " || START");
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING,
					AcceleratorGenericConstants.UTF_8);
			transformer.setOutputProperty(OutputKeys.STANDALONE,
					AcceleratorGenericConstants.YES);
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					AcceleratorGenericConstants.YES);
		} catch (TransformerConfigurationException e) {
			LOGGER.error(
					"An exception has occured in obtaining tansformer from the factory.. ",
					e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANS_FACTORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method provides the implementation to transform contents using
	 * provided mapping file
	 * 
	 * @param transformerRequest
	 *            - contains all the necessary inputs needed for transformation
	 * @return ServiceResponse contains - the status of transformation in JSON
	 *         format
	 */
	public ContentTransformerServiceResponse transformContents(
			ContentTransformerServiceRequest transformerRequest) {
		String methodName = "transformContents";
		LOGGER.info(" || " + methodName + " || START");
		ContentTransformerServiceResponse transformerResponse = new ContentTransformerServiceResponse();

		// Start transformation in the requested order
		String[] orderString = null;
		if (transformerRequest.getTransformationOrder() != null) {
			orderString = transformerRequest.getTransformationOrder().split(
					AcceleratorGenericConstants.CSV_SEPARATOR);

			// Perform transformation for each content type
			for (String order : orderString) {
				try {
					transformContentsForContentType(transformerRequest, order);
				} catch (AcceleratorException e) {
					LOGGER.error("An exception has occured in " + methodName,
							e.getMessage());
				}
			}

			// Construct final report path
			String reportPath = "";
			if (transformerRequest.getReportFilePath() != null) {
				String[] reportStrings = transformerRequest.getReportFilePath()
						.split(AcceleratorGenericConstants.CSV_SEPARATOR);
				for (String singleString : reportStrings) {
					reportPath = reportPath + singleString
							+ AcceleratorGenericConstants.FORWARD_SLASH;
				}
				reportPath = reportPath
						.substring(
								0,
								reportPath
										.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH));
			}

			// Get report data
			try {
				transformerResponse
						.setJsonResponse(ReportGeneratorHelper
								.fetchConciseReportWithFolders(
										reportPath,
										transformerRequest
												.getDestinationRootPath()
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ AcceleratorGenericConstants.STATUS_SUCCESS,
										transformerRequest
												.getDestinationRootPath()
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ AcceleratorGenericConstants.STATUS_FAILURE));
			} catch (AcceleratorException e) {
				LOGGER.error("An exception has occured in " + methodName,
						e.getMessage());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return transformerResponse;
	}

	/**
	 * This method transforms the contents of a particular content type.
	 * Contents after transformation are moved either to success or failure
	 * folder based on the status of transformation.
	 * 
	 * @param transformerServiceRequest
	 *            - contains all the necessary input required for transformation
	 * @param order
	 *            - specify which content type needs to be transformed
	 * @throws AcceleratorException
	 * 
	 */
	public void transformContentsForContentType(
			ContentTransformerServiceRequest transformerServiceRequest,
			String order) throws AcceleratorException {
		String methodName = "transformContentsForContentType";
		LOGGER.info(" || " + methodName + " || START");

		// Get file handle on the transformation mapping file
		File xslMappingFile = new File(
				transformerServiceRequest.getMappingFilePath());

		// Get all files to be transformed
		String completeSourcePath = transformerServiceRequest
				.getSourceRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH + order;
		LOGGER.debug(" || " + methodName + " || Source Path || "
				+ completeSourcePath);
		File[] filesToBeTransformed = new File(completeSourcePath).listFiles();

		// Set Success and Failure path
		String successPath = transformerServiceRequest.getDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_SUCCESS
				+ AcceleratorGenericConstants.FORWARD_SLASH + order;
		LOGGER.debug(" || " + methodName + " || Success Folder || "
				+ successPath);

		String failurePath = transformerServiceRequest.getDestinationRootPath()
				+ AcceleratorGenericConstants.FORWARD_SLASH
				+ AcceleratorGenericConstants.STATUS_FAILURE
				+ AcceleratorGenericConstants.FORWARD_SLASH + order;
		LOGGER.debug(" || " + methodName + " || Failure Folder || "
				+ failurePath);

		if (null != filesToBeTransformed) {
			// Start transformation of file one at a time
			for (File singleFile : filesToBeTransformed) {
				LOGGER.debug(" || " + methodName + " || singleFile || "
						+ singleFile);
				if (!singleFile.isDirectory()) {
					long fileSizeInBytes = singleFile.length();
					long expectedSize = Long
							.parseLong(transformerServiceRequest.getFileSize());
					if (fileSizeInBytes <= expectedSize
							|| order.equalsIgnoreCase(AcceleratorGenericConstants.IMPORT_TYPE_DAM)) {
						LOGGER.debug(" || " + methodName + " || filesize || "
								+ fileSizeInBytes);
						String transformedXML;
						boolean transformationSuccess = false;
						try {

							// Move just plain files without transformation in
							// case of dam files
							if (AcceleratorGenericConstants.IMPORT_TYPE_DAM
									.equalsIgnoreCase(order)
									&& !singleFile.getName().endsWith(
											AcceleratorGenericConstants.XML)) {
								boolean fileMovStat = AcceleratorFileUtils
										.moveOrCopyFileOnServer(singleFile,
												successPath, false);
								transformationSuccess = fileMovStat;

								// Log status report
								if (fileMovStat) {
									addToReportLog(
											singleFile.getAbsolutePath(),
											successPath
													+ AcceleratorGenericConstants.FORWARD_SLASH
													+ singleFile.getName(),
											transformerServiceRequest
													.getMappingFilePath(),
											AcceleratorGenericConstants.STATUS_SUCCESS,
											AcceleratorGenericConstants.SUCCESS_DESC,
											transformerServiceRequest
													.getReportFilePath());
								} else {
									addToReportLog(
											singleFile.getAbsolutePath(),
											failurePath
													+ AcceleratorGenericConstants.FORWARD_SLASH
													+ singleFile.getName(),
											transformerServiceRequest
													.getMappingFilePath(),
											AcceleratorGenericConstants.STATUS_FAILURE,
											AcceleratorGenericConstants.FAILURE_DESC,
											transformerServiceRequest
													.getReportFilePath());
								}
							}
							if (!transformationSuccess) {
								transformedXML = createTransformedXML(
										singleFile, xslMappingFile, order,
										transformerServiceRequest
												.getDestinationRootPath());

								LOGGER.debug(" || " + methodName
										+ " || Transformed Content || "
										+ transformedXML);

								// Delete if file exists and then create file
								// from
								// the
								// string
								File fileToBeDeleted = new File(
										successPath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ singleFile.getName());
								if (fileToBeDeleted.exists()) {
									fileToBeDeleted.delete();
								}

								transformationSuccess = AcceleratorFileUtils
										.createFileFromString(transformedXML,
												singleFile.getName(),
												successPath, false);

								// Updating the image reference in page xml from
								// image id to image path
								if (order
										.equalsIgnoreCase(AcceleratorGenericConstants.IMPORT_TYPE_PAGE)) {
									transformationSuccess = updateImageReference(
											order, successPath,
											singleFile.getName());
									transformationSuccess = updateTagReference(
											order, successPath,
											singleFile.getName());
								}

								// Move image to the success folder and update
								// reference in case of DAM assets
								if (AcceleratorGenericConstants.IMPORT_TYPE_DAM
										.equals(order)) {
									transformationSuccess = updateImageFileReference(
											transformerServiceRequest
													.getSourceRootPath(),
											order, successPath, singleFile
													.getName());
								}

								// Log status report
								addToReportLog(
										singleFile.getAbsolutePath(),
										successPath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ singleFile.getName(),
										transformerServiceRequest
												.getMappingFilePath(),
										AcceleratorGenericConstants.STATUS_SUCCESS,
										AcceleratorGenericConstants.SUCCESS_DESC,
										transformerServiceRequest
												.getReportFilePath());
							}
						} catch (AcceleratorException e) {
							transformationSuccess = false;
							LOGGER.error("An exception has occured in "
									+ methodName, e);
						} finally {
							LOGGER.info(" || "
									+ methodName
									+ " || Inside finally Block Transformation status "
									+ transformationSuccess);
							// Move original file to failure folder
							if (!transformationSuccess) {
								// Delete the partially transformed file
								LOGGER.info(" || " + methodName
										+ " || file name "
										+ singleFile.getName()
										+ " || successPath " + successPath);

								File fileToBeDeleted = new File(
										successPath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ singleFile.getName());
								if(fileToBeDeleted.exists()){
									fileToBeDeleted.delete();
								}
								
								// move the original file
								AcceleratorFileUtils.moveOrCopyFileOnServer(
										singleFile, failurePath, false);

								// Log status report
								addToReportLog(
										singleFile.getAbsolutePath(),
										failurePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ singleFile.getName(),
										transformerServiceRequest
												.getMappingFilePath(),
										AcceleratorGenericConstants.STATUS_FAILURE,
										AcceleratorGenericConstants.FAILURE_DESC,
										transformerServiceRequest
												.getReportFilePath());
							}
						}
					} else {
						LOGGER.debug(" || " + methodName + " || "
								+ AcceleratorGenericConstants.File_SIZE_EXCEEDS
								+ " || " + fileSizeInBytes);
					}
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method creates the transformed XML based on the inputs provided
	 * 
	 * @param contentfile
	 * @param contentXSL
	 * @param contentType
	 * @param outputDirectory
	 * @throws AcceleratorException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public String createTransformedXML(File contentfile, File contentXSL,
			String contentType, String outputDirectory)
			throws AcceleratorException {
		String methodName = "createTransformedXML";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || filePath "
				+ contentfile.getAbsolutePath());
		// Store xsl content in a string
		String returnValue = "";
		ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();
		try {
			InputStream iStreamXsl = new FileInputStream(
					contentXSL.getAbsolutePath());

			StringBuffer contentXSLString = new StringBuffer(200);
			int ch;
			ch = iStreamXsl.read();
			while (ch != -1) {
				contentXSLString.append((char) ch);
				ch = iStreamXsl.read();
			}

			// Create a DOM object containing the transformed contents
			DOMResult domResult;
			TransformerFactory tFactory = TransformerFactory.newInstance();
			InputStream iStreamXml = new FileInputStream(contentfile);
			domResult = transform(tFactory, iStreamXml, contentXSLString);

			// trimSpecialCharactersInName(domResult);
			Transformer ret = tFactory.newTransformer();
			ret.setOutputProperty(OutputKeys.ENCODING,
					AcceleratorGenericConstants.UTF_8);
			ret.setOutputProperty(OutputKeys.INDENT,
					AcceleratorGenericConstants.YES);
			ret.setOutputProperty(OutputKeys.STANDALONE,
					AcceleratorGenericConstants.YES);

			StreamResult streamResult = new StreamResult(
					new OutputStreamWriter(stringWriter, "UTF-8"));
			ret.transform(new DOMSource(domResult.getNode()), streamResult);
			returnValue = stringWriter.toString();

			iStreamXsl.close();
			stringWriter.close();
			// createFileFromString(stringWriter.toString(), "transformed.xml",
			// outputDirectory);
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (TransformerConfigurationException e) {
			LOGGER.error(
					"An exception has occured in obtaining tansformer from the factory.. ",
					e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANS_FACTORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (TransformerException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANSFORM_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return returnValue;
	}

	/**
	 * This method transform the input stream and generate the DOMResult
	 * containing transformed content
	 * 
	 * @param tFactory
	 * @param iStreamXml
	 * @param iStreamXsl
	 * 
	 * @return domResult
	 */
	public DOMResult transform(TransformerFactory tFactory,
			InputStream iStreamXml, StringBuffer iStreamXsl)
			throws AcceleratorException {
		String methodName = "transform";
		LOGGER.info(" || " + methodName + " || START");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DOMResult domResult = new DOMResult();
		factory.setNamespaceAware(false);
		DocumentBuilder parser;
		InputStream bufferStream = null;
		try {
			parser = factory.newDocumentBuilder();
			if (iStreamXml != null && iStreamXml != null) {
				Document newDoc = parser.parse(iStreamXml);
				bufferStream = new ByteArrayInputStream(iStreamXsl.toString()
						.getBytes("UTF-8"));

				Transformer transformer = getTransformer(tFactory, bufferStream);
				transformer.setOutputProperty(OutputKeys.ENCODING,
						AcceleratorGenericConstants.UTF_8);
				transformer.setOutputProperty(OutputKeys.METHOD,
						AcceleratorGenericConstants.XML);
				transformer.setOutputProperty(OutputKeys.INDENT,
						AcceleratorGenericConstants.YES);
				Source xmlSource = new DOMSource(newDoc);
				transformer.transform(xmlSource, domResult);
			}
		} catch (ParserConfigurationException e1) {
			LOGGER.error("An ParserConfigurationException has occured in "
					+ methodName, e1);
			throw new AcceleratorException(
					AcceleratorFaultCode.PARSE_EXCEPTION, CLASS_NAME,
					methodName, e1.getCause());
		} catch (SAXException e) {
			LOGGER.error("An SAXException has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.SAX_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (TransformerException e) {
			LOGGER.error(
					"An TransformerException has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANSFORM_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("An UnsupportedEncodingException has occured in "
					+ methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANSFORM_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An IOException has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} finally {
			// Close all I/O objects
			try {
				if (bufferStream != null) {
					bufferStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("An IOException has occured in " + methodName
						+ " while closing the I/O stream", e);
				throw new AcceleratorException(
						AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
						methodName, e.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return domResult;
	}

	/**
	 * This method will be called to update the reference of image files for
	 * successfully transformed files
	 * 
	 * @param sourceFolder
	 * @param order
	 * @param destinationFolder
	 * @param fileToBeModified
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public boolean updateImageFileReference(String sourceFolder, String order,
			String destinationFolder, String fileToBeModified)
			throws AcceleratorException {
		String methodName = "updateImageFileReference";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || sourceFolder || " + sourceFolder
				+ " || destinationFolder || " + destinationFolder
				+ " || fileToBeModified || " + fileToBeModified
				+ " || order || " + order);

		boolean updateStatus = false;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			InputStream iStream = new FileInputStream(destinationFolder
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileToBeModified);

			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document xmlDocument = documentBuilder.parse(iStream);

			LOGGER.debug(" || " + methodName + " || path || "
					+ destinationFolder
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileToBeModified);

			Node fileNode = xmlDocument.getElementsByTagName(
					AcceleratorGenericConstants.FILE_NAME).item(0);
			String fileValue = fileNode.getTextContent();
			if (fileValue != null
					&& !fileValue.startsWith(AcceleratorGenericConstants.HTTP)) {

				File imageFile = new File(sourceFolder
						+ AcceleratorGenericConstants.FORWARD_SLASH + order
						+ AcceleratorGenericConstants.FORWARD_SLASH + fileValue);
				LOGGER.debug(" || " + methodName + " || imageFile 1 || "
						+ imageFile.getAbsolutePath());
				String imageDestination = destinationFolder
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ AcceleratorGenericConstants.IMAGE;

				LOGGER.debug(" || " + methodName + " || imageDestination || "
						+ imageDestination);

				if (imageFile.exists()) {
					LOGGER.debug(" || " + methodName + " || imageFile 2 || "
							+ imageFile.getAbsolutePath());

					AcceleratorFileUtils.moveOrCopyFileOnServer(imageFile,
							imageDestination, false);
					fileNode.setTextContent(AcceleratorGenericConstants.IMAGE
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ imageFile.getName());

					// write the updated DOM object to the file
					TransformerFactory transformerFactory = TransformerFactory
							.newInstance();
					Transformer transformer = transformerFactory
							.newTransformer();
					DOMSource domSource = new DOMSource(xmlDocument);

					File oldFile = new File(destinationFolder
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ fileToBeModified);
					if (oldFile.delete()) {
						StreamResult streamResult = new StreamResult(new File(
								oldFile.getAbsolutePath()));
						transformer.transform(domSource, streamResult);
					}
				}
			}
			updateStatus = true;
		} catch (IOException e) {
			LOGGER.error("An IOException has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PARSE_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ParserConfigurationException e1) {
			LOGGER.error("An ParserConfigurationException has occured in "
					+ methodName, e1);
			throw new AcceleratorException(
					AcceleratorFaultCode.PARSE_EXCEPTION, CLASS_NAME,
					methodName, e1.getCause());
		} catch (SAXException e) {
			LOGGER.error("An SAXException has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.SAX_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (TransformerConfigurationException e) {
			LOGGER.error(
					"An TransformerConfigurationException has occured in obtaining tansformer from the factory.. ",
					e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANS_FACTORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (TransformerException e) {
			LOGGER.error(
					"An TransformerException has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANSFORM_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return updateStatus;
	}

	/**
	 * This method is used to update the image refernce in the page xml, from
	 * image id to image dam path
	 * 
	 * @param order
	 * @param destinationFolder
	 * @param fileToBeModified
	 * @return
	 * @throws AcceleratorException
	 */
	public static boolean updateImageReference(String order,
			String destinationFolder, String fileToBeModified)
			throws AcceleratorException {
		String methodName = "updateImageReference";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || destinationFolder || "
				+ destinationFolder + " || fileToBeModified || "
				+ fileToBeModified + " || order || " + order);

		boolean updateStatus = false;
		Map<String, String> map = ReportGeneratorHelper
				.getPropertiesMap(AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
						+ AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES);

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder;
		InputStream iStream = null;

		try {
			iStream = new FileInputStream(destinationFolder
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileToBeModified);

			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document xmlDocument = documentBuilder.parse(iStream);

			LOGGER.debug(" || " + methodName + " || path || "
					+ destinationFolder
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileToBeModified);

			Node fileNode = null;
			String fileValue = null;

			if (null != xmlDocument
					.getElementsByTagName(AcceleratorGenericConstants.FILE_REFERENCE)) {
				LOGGER.debug(" || "
						+ methodName
						+ " || item || "
						+ xmlDocument.getElementsByTagName(
								AcceleratorGenericConstants.FILE_REFERENCE)
								.item(0));

				for (int i = 0; i < xmlDocument.getElementsByTagName(
						AcceleratorGenericConstants.FILE_REFERENCE).getLength(); i++) {
					fileNode = xmlDocument.getElementsByTagName(
							AcceleratorGenericConstants.FILE_REFERENCE).item(i);
					if (null != fileNode) {
						fileValue = fileNode.getTextContent();
						LOGGER.debug(" || " + methodName
								+ " || fileValue 1 || " + fileValue);
					}
					if (null != fileValue && !fileValue.isEmpty()) {
						LOGGER.debug(" || " + methodName
								+ " || fileValue 2 || " + fileValue);
						if (fileValue != null
								&& !fileValue
										.startsWith(AcceleratorGenericConstants.HTTP)) {

							String newFileValue = map.get(fileValue);
							LOGGER.debug(" || " + methodName
									+ " || newFileValue || " + newFileValue);

							fileNode.setTextContent(newFileValue);

							// write the updated DOM object to the file
							TransformerFactory transformerFactory = TransformerFactory
									.newInstance();
							Transformer transformer = transformerFactory
									.newTransformer();
							DOMSource domSource = new DOMSource(xmlDocument);

							File oldFile = new File(destinationFolder
									+ AcceleratorGenericConstants.FORWARD_SLASH
									+ fileToBeModified);
							if (oldFile.delete()) {
								StreamResult streamResult = new StreamResult(
										new File(oldFile.getAbsolutePath()));
								transformer.transform(domSource, streamResult);
							}
						}
					}
				}
			}
			updateStatus = true;
		} catch (ParserConfigurationException e1) {
			LOGGER.error("An ParserConfigurationException has occured in "
					+ methodName, e1);
			throw new AcceleratorException(
					AcceleratorFaultCode.PARSE_EXCEPTION, CLASS_NAME,
					methodName, e1.getCause());
		} catch (SAXException e) {
			LOGGER.error("An SAXException has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.SAX_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (TransformerException e) {
			LOGGER.error(
					"An TransformerException has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANSFORM_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("An UnsupportedEncodingException has occured in "
					+ methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANSFORM_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An IOException has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} finally {
			// Close all I/O objects
			try {
				if (iStream != null) {
					iStream.close();
				}
			} catch (IOException e) {
				throw new AcceleratorException(
						AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
						methodName, e.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return updateStatus;
	}

	/**
	 * This method will update the tagID with the Parent id for transformed
	 * files
	 * 
	 * @param order
	 * @param destinationFolder
	 * @param fileToBeModified
	 * @return
	 * @throws AcceleratorException
	 */
	public static boolean updateTagReference(String order,
			String destinationFolder, String fileToBeModified)
			throws AcceleratorException {
		String methodName = "updateImageReference";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || destinationFolder || "
				+ destinationFolder + " || fileToBeModified || "
				+ fileToBeModified + " || order || " + order);

		boolean updateStatus = false;
		Map<String, String> map = ReportGeneratorHelper
				.getPropertiesMap(AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
						+ AcceleratorGenericConstants.TAG_FILES_MAPPING_PROPERTIES);

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder;
		try {
			LOGGER.info(" || " + methodName + " || 1");
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			LOGGER.info(" || " + methodName + " || 2");
			Document xmlDocument = documentBuilder.parse(destinationFolder
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileToBeModified);

			LOGGER.debug(" || " + methodName + " || path || "
					+ destinationFolder
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ fileToBeModified);

			Node tagNode = null;
			String tagValue = null;

			if (null != xmlDocument
					.getElementsByTagName(AcceleratorGenericConstants.TAGIDS)) {
				LOGGER.debug(" || "
						+ methodName
						+ " || item || "
						+ xmlDocument.getElementsByTagName(
								AcceleratorGenericConstants.TAGIDS).item(0));

				for (int i = 0; i < xmlDocument.getElementsByTagName(
						AcceleratorGenericConstants.TAGIDS).getLength(); i++) {
					tagNode = xmlDocument.getElementsByTagName(
							AcceleratorGenericConstants.TAGIDS).item(i);
					if (null != tagNode) {
						tagValue = tagNode.getTextContent();
						LOGGER.debug(" || " + methodName + " || TagValue 1 || "
								+ tagValue);
					}
					if (null != tagValue && !tagValue.isEmpty()) {
						LOGGER.debug(" || " + methodName + " || TagValue 2 || "
								+ tagValue);

						String string = "";
						for (String tagId : tagValue
								.split(AcceleratorGenericConstants.CSV_SEPARATOR)) {
							String val = (String) map.get(tagId);
							if (StringUtils.isNotEmpty(val)) {
								string = string
										+ val
										+ AcceleratorGenericConstants.CSV_SEPARATOR;
							}
						}
						if (string.length() > 0) {
							tagNode.setTextContent(string);

						} else {
							tagNode.setTextContent(AcceleratorGenericConstants.CSV_SEPARATOR);

						}

						TransformerFactory transformerFactory = TransformerFactory
								.newInstance();
						Transformer transformer = transformerFactory
								.newTransformer();
						DOMSource domSource = new DOMSource(xmlDocument);

						File oldFile = new File(destinationFolder
								+ AcceleratorGenericConstants.FORWARD_SLASH
								+ fileToBeModified);
						if (oldFile.delete()) {
							StreamResult streamResult = new StreamResult(
									new File(oldFile.getAbsolutePath()));
							transformer.transform(domSource, streamResult);

						}
					}
				}
			}
			updateStatus = true;
		} catch (ParserConfigurationException e1) {
			LOGGER.error("An exception has occured in " + methodName, e1);
			throw new AcceleratorException(
					AcceleratorFaultCode.PARSE_EXCEPTION, CLASS_NAME,
					methodName, e1.getCause());
		} catch (SAXException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.SAX_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (TransformerConfigurationException e) {
			LOGGER.error(
					"An exception has occured in obtaining tansformer from the factory.. ",
					e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANS_FACTORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (TransformerException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANSFORM_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return updateStatus;
	}

	/**
	 * This method can be used for creating CDATA section in input XMLs
	 * 
	 * @param tagName
	 * @param domResult
	 * @throws TransformerException
	 */

	public void createCDATASectionForTag(String tagName, DOMResult domResult)
			throws AcceleratorException {
		String methodName = "createCDATASectionForTag";
		LOGGER.info(" || " + methodName + " || START");
		Node bodyNode = ((Document) domResult.getNode())
				.getElementsByTagName(tagName).item(0).getFirstChild();
		((Document) domResult.getNode()).getElementsByTagName(tagName).item(0)
				.removeChild(bodyNode);

		StringWriter stringWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(stringWriter);
		try {
			transformer.transform(new DOMSource(bodyNode), streamResult);
		} catch (TransformerException e) {
			LOGGER.error(
					"A TransformerException has occured while transforming tags for CDATA ",
					e.getMessage());
			throw new AcceleratorException(
					AcceleratorFaultCode.TRANS_FACTORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		String bodyText = stringWriter.toString();
		Node bodyCData = ((Document) domResult.getNode())
				.createCDATASection(bodyText);
		((Document) domResult.getNode()).getElementsByTagName(tagName).item(0)
				.appendChild(bodyCData);
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method is used to remove any special characters from the name of the
	 * file
	 * 
	 * @param domResult
	 * @throws TransformerException
	 */
	public void trimSpecialCharactersInName(DOMResult domResult) {
		String methodName = "trimSpecialCharactersInName";
		LOGGER.info(" || " + methodName + " || START");
		String content = ((Document) domResult.getNode())
				.getElementsByTagName(AcceleratorGenericConstants.ITEMNAME)
				.item(0).getTextContent();
		Matcher matcher = specialCharPattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		((Document) domResult.getNode())
				.getElementsByTagName(AcceleratorGenericConstants.ITEMNAME)
				.item(0).setTextContent(sb.toString());
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * This method will be called for adding log messages for individual
	 * transformations
	 * 
	 * @param originalFile
	 * @param tranformedFile
	 * @param xslFilePath
	 * @param status
	 * @param statusDescription
	 * @param reportFilePath
	 */
	public void addToReportLog(String originalFile, String tranformedFile,
			String xslFilePath, String status, String statusDescription,
			String reportFilePath) {
		String methodName = "addToReportLog";
		LOGGER.info(" || " + methodName + " || START");

		String reportingMessage;

		// Modified Date for reprots
		DateFormat dateFormat = new SimpleDateFormat(
				AcceleratorGenericConstants.YYYYMMDD_FORMAT);
		Date date = new Date();
		String modifiedDate = dateFormat.format(date);

		// Common Message part
		reportingMessage = originalFile
				+ AcceleratorGenericConstants.CSV_SEPARATOR + tranformedFile
				+ AcceleratorGenericConstants.CSV_SEPARATOR + xslFilePath
				+ AcceleratorGenericConstants.CSV_SEPARATOR + modifiedDate
				+ AcceleratorGenericConstants.CSV_SEPARATOR + status
				+ AcceleratorGenericConstants.CSV_SEPARATOR + statusDescription;

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

	/**
	 * Method returns an instance of Transformer
	 * 
	 * @param tFactory
	 * @param iStream
	 * @return
	 * @throws TransformerConfigurationException
	 */
	public static Transformer getTransformer(TransformerFactory tFactory,
			InputStream iStream) throws TransformerConfigurationException {
		String methodName = "getTransformer";
		LOGGER.info(" || " + methodName + " || START");
		Source xslSource = new StreamSource(iStream);
		Templates temp = tFactory.newTemplates(xslSource);
		LOGGER.info(" || " + methodName + " || END");
		return temp.newTransformer();
	}

	/**
	 * This method will be used while standalone testing to create the
	 * transformed files
	 * 
	 * @param fileContents
	 * @param fileNameWithExtension
	 * @param path
	 * @return
	 */
	public boolean createFileFromString(String fileContents,
			String fileNameWithExtension, String path)
			throws AcceleratorException {
		String methodName = "createFileFromString";
		LOGGER.info(" || " + methodName + " || START");
		String filePath = path + "//" + fileNameWithExtension;
		File directory = new File(path);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(filePath, true)));
				out.println(fileContents);
				out.close();
			} catch (IOException e) {
				LOGGER.error("An IOException has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
						methodName, e.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return true;
	}

	public static void main(String[] args) {
		File xmlFile = new File(
				"D://MigrationFolders//CompleteMigrationSource//page//pageFinalOld.xml");
		File xslFile = new File("D://MigrationFolders//XSL//InputXSL.xsl");

		String contentType = "page";
		String outputDirectory = "D://MigrationFolders//CompleteMigrationDestination";

		XMLTransformer sampleTest;
		try {
			sampleTest = new XMLTransformer();
			System.out.println((sampleTest.createTransformedXML(xmlFile,
					xslFile, contentType, outputDirectory)));
		} catch (AcceleratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
