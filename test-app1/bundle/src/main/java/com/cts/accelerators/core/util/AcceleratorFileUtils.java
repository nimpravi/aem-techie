package com.cts.accelerators.core.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.Binary;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

/**
 * @author Cognizant
 * 
 *         Utility class for Migration Project
 * 
 */

public class AcceleratorFileUtils {
	private static final String CLASS_NAME = AcceleratorFileUtils.class
			.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AcceleratorFileUtils.class);

	/**
	 * This method will be used to create or append to a file the contents
	 * passed as an input parameter
	 * 
	 * @param fileContents
	 * @param fileNameWithExtension
	 * @param path
	 * @param storeInCRX
	 * @return
	 * @throws AcceleratorException
	 */
	public static boolean createFileFromString(String fileContents,
			String fileNameWithExtension, String path, boolean storeInCRX)
			throws AcceleratorException {
		String methodName = "createFileFromString";
		LOGGER.info(" || " + methodName + " || START");
		boolean isFileCreated = false;
		Session jcrSession = ConnectionManager.getSession();
		Node tempNode = null, contentNode = null, fileNode = null;
		if (storeInCRX) {
			if (!path.startsWith(AcceleratorGenericConstants.FORWARD_SLASH)) {
				throw new AcceleratorException(
						AcceleratorFaultCode.INVALID_JCR_PATH, CLASS_NAME,
						methodName);
			}
			try {
				LOGGER.info(" || " + methodName + " || inside IF");
				if (path.startsWith(AcceleratorGenericConstants.FORWARD_SLASH)) {
					if (jcrSession.nodeExists(path)) {
						tempNode = jcrSession.getNode(path);
						LOGGER.info(" || " + methodName + " || node exists");

					} else {
						tempNode = AcceleratorCRXUtils.createFolder(path);
					}
				} else {
					return isFileCreated;
				}
				ValueFactory valueFactory = jcrSession.getValueFactory();
				if (tempNode.hasNode(fileNameWithExtension)) {
					fileNode = tempNode.getNode(fileNameWithExtension);
				} else {
					fileNode = tempNode.addNode(fileNameWithExtension,
							AcceleratorGenericConstants.FILE_TYPE);
				}
				fileNode.addMixin(AcceleratorGenericConstants.MIX_REFERENCABLE);
				if (!fileNode.hasNode(AcceleratorGenericConstants.JCR_CONTENT)) {
					contentNode = fileNode.addNode(
							AcceleratorGenericConstants.JCR_CONTENT,
							AcceleratorGenericConstants.NT_RESOURCE);
				} else {
					contentNode = fileNode
							.getNode(AcceleratorGenericConstants.JCR_CONTENT);
				}
				contentNode.setProperty(
						AcceleratorGenericConstants.JCR_MIME_TYPE,
						AcceleratorGenericConstants.MIME_TYPE);

				if (contentNode
						.hasProperty(AcceleratorGenericConstants.JCR_DATA)) {
					String filepath = "";
					if (path.endsWith(AcceleratorGenericConstants.FORWARD_SLASH)) {
						filepath = path + fileNameWithExtension;
					} else {
						filepath = path
								+ AcceleratorGenericConstants.FORWARD_SLASH
								+ fileNameWithExtension;
					}

					String existingvalue = AcceleratorCRXUtils
							.readCRXData(filepath);
					String appendedFileContents = existingvalue + "\n"
							+ fileContents;

					Value appendedFileContentsValue = valueFactory
							.createValue(appendedFileContents);
					contentNode.setProperty(
							AcceleratorGenericConstants.JCR_DATA,
							appendedFileContentsValue);
				} else {
					Value appendedFileContentsValue = valueFactory
							.createValue(fileContents);
					contentNode.setProperty(
							AcceleratorGenericConstants.JCR_DATA,
							appendedFileContentsValue);
				}
				jcrSession.save();
				isFileCreated = true;
			} catch (RepositoryException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
						methodName, e.getCause());
			}

		} else {
			LOGGER.info(" || " + methodName + " || In else block");
			try {
				String filePath = path
						+ AcceleratorGenericConstants.FORWARD_SLASH
						+ fileNameWithExtension;
				LOGGER.info(" || " + methodName + " || filePath - > "
						+ filePath);
				File directory = new File(path);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				File file = new File(filePath);
				if (!file.exists()) {
					file.createNewFile();
				}
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(filePath, true)));
				out.println(fileContents);
				out.close();
				isFileCreated = true;
			} catch (IOException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
						methodName, e.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return isFileCreated;
	}

	/**
	 * This method will create a DAM asset from the Contents specified in Source
	 * File and store it in Destination location
	 * 
	 * @param sourcePath
	 * @param destinationPath
	 * @param mimeType
	 * @param isExternalUrl
	 * @return imageNodePath
	 * @throws AcceleratorException
	 */
	public static String copyAssetsToDam(String sourcePath,
			String destinationPath, String mimeType, boolean isExternalUrl)
			throws AcceleratorException {
		String methodName = "copyAssetsToDam";
		LOGGER.info(" || " + methodName + " || START");
		Session session = ConnectionManager.getSession();
		ByteArrayOutputStream byteArrayOutputStream = null;
		InputStream inputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			if (isExternalUrl) {
				URL imageUrl = new URL(sourcePath);
				URLConnection urlConnection = imageUrl.openConnection();
				inputStream = urlConnection.getInputStream();
			} else {
				File file = new File(sourcePath);
				inputStream = new FileInputStream(file);
			}
			byte[] byteChunk = new byte[4096]; // Or whatever size you want
												// to read in at a time.
			int n;

			while ((n = inputStream.read(byteChunk)) > 0) {
				byteArrayOutputStream.write(byteChunk, 0, n);
			}
			inputStream.close();

			Binary binary = session.getValueFactory().createBinary(
					new ByteArrayInputStream(byteArrayOutputStream
							.toByteArray()));
			Node imageRootNode = session
					.getNode(destinationPath.substring(
							0,
							destinationPath
									.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH)));
			Node imageNode = null;
			String imageNodeName = destinationPath
					.substring(destinationPath
							.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH) + 1);
			if (imageRootNode.hasNode(imageNodeName)) {
				imageNode = session.getNode(destinationPath);
			} else {
				imageNode = imageRootNode.addNode(imageNodeName,
						AcceleratorGenericConstants.DAM_ASSET);
			}
			Node contentNode = null;
			if (imageNode.hasNode(AcceleratorGenericConstants.JCR_CONTENT)) {
				contentNode = imageNode
						.getNode(AcceleratorGenericConstants.JCR_CONTENT);
			} else {
				contentNode = imageNode.addNode(
						AcceleratorGenericConstants.JCR_CONTENT,
						AcceleratorGenericConstants.DAM_ASSET_CONTENT);
			}
			Node metadataNode = null;
			if (contentNode.hasNode(AcceleratorGenericConstants.METADATA)) {
				metadataNode = contentNode
						.getNode(AcceleratorGenericConstants.METADATA);
			} else {
				metadataNode = contentNode
						.addNode(
								AcceleratorGenericConstants.METADATA,
								AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
			}
			contentNode.getSession().save();

			Node relatedNode = null;
			if (contentNode.hasNode(AcceleratorGenericConstants.RELATED)) {
				relatedNode = contentNode
						.getNode(AcceleratorGenericConstants.RELATED);
			} else {
				relatedNode = contentNode
						.addNode(
								AcceleratorGenericConstants.RELATED,
								AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
			}
			Node renditionsNode = null;
			if (contentNode.hasNode(AcceleratorGenericConstants.RENDITIONS)) {
				renditionsNode = contentNode
						.getNode(AcceleratorGenericConstants.RENDITIONS);
			} else {
				renditionsNode = contentNode.addNode(
						AcceleratorGenericConstants.RENDITIONS,
						AcceleratorGenericConstants.FOLDER_TYPE);
			}
			Node originalRenditionsNode = null;
			if (renditionsNode.hasNode(AcceleratorGenericConstants.ORIGINAL)) {
				originalRenditionsNode = renditionsNode
						.getNode(AcceleratorGenericConstants.ORIGINAL);
			} else {
				originalRenditionsNode = renditionsNode.addNode(
						AcceleratorGenericConstants.ORIGINAL,
						AcceleratorGenericConstants.FILE_TYPE);
			}
			// renditionsNode.getSession().save();
			Node originalRenditionsResource = null;
			if (originalRenditionsNode
					.hasNode(AcceleratorGenericConstants.JCR_CONTENT)) {
				originalRenditionsResource = originalRenditionsNode
						.getNode(AcceleratorGenericConstants.JCR_CONTENT);
			} else {
				originalRenditionsResource = originalRenditionsNode.addNode(
						AcceleratorGenericConstants.JCR_CONTENT,
						AcceleratorGenericConstants.NT_RESOURCE);
			}

			// Set the mandatory properties
			originalRenditionsResource.setProperty(
					AcceleratorGenericConstants.JCR_DATA, binary);
			originalRenditionsResource.setProperty(
					AcceleratorGenericConstants.JCR_LAST_MODIFIED,
					Calendar.getInstance());
			originalRenditionsResource.setProperty(
					AcceleratorGenericConstants.JCR_MIME_TYPE, mimeType);
			binary = null;
			byteArrayOutputStream.close();
			byteArrayOutputStream = null;
			/*
			 * if(mimeType.contains(AcceleratorGenericConstants.IMAGE)) {
			 * ResourceResolver
			 * resourceResolver=ConnectionManager.getResourceResolver();
			 * Resource resource=resourceResolver.getResource(destinationPath);
			 * 
			 * BundleContext bundleContext = FrameworkUtil.getBundle(
			 * AssetHandler.class).getBundleContext(); AssetStore assetStore =
			 * (AssetStore
			 * )bundleContext.getService(bundleContext.getServiceReference
			 * (AssetStore.class.getName())); AssetHandler
			 * assetHandler=assetStore.getAssetHandler(mimeType); final
			 * ArrayList> config = new ArrayList>();
			 * 
			 * 
			 * 
			 * Asset asset = resource.adaptTo(Asset.class); Layer layer140x100 =
			 * ImageHelper.createLayer(session,asset.getOriginal().getPath());
			 * layer140x100.resize(140, 100); Layer layer319x319 =
			 * ImageHelper.createLayer(session,asset.getOriginal().getPath());
			 * layer319x319.resize(319, 319); Layer layer48x48 =
			 * ImageHelper.createLayer(session,asset.getOriginal().getPath());
			 * layer48x48.resize(48, 48); }
			 */
			imageNode.getSession().save();
			LOGGER.info(" || " + methodName + " || END");
			return imageNode.getPath();
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (UnsupportedRepositoryOperationException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.UNSUPPORTED_REPOSITORY_OPERATION_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
	}

	/**
	 * This method takes a directory name and extension and creates a list of
	 * all the files of that specific extension
	 * 
	 * @param directoryPath
	 * @param extension
	 * @return list of files of specific extension type
	 * @throws AcceleratorException
	 */
	public static String[] listFilesInCRX(String directoryPath,
			final String extension) throws AcceleratorException {
		String methodName = "listFilesInCRX";
		LOGGER.info(" || listFiles || START");
		Session jcrSession = ConnectionManager.getSession();
		LOGGER.info(" || listFiles || session");
		String[] listOfFiles;
		try {
			LOGGER.info(" || listFiles || try");
			if (jcrSession.getRootNode().hasNode(directoryPath)) {
				BundleContext bundleContext = FrameworkUtil.getBundle(
						QueryBuilder.class).getBundleContext();
				QueryBuilder queryBuilder = (QueryBuilder) bundleContext
						.getService(bundleContext
								.getServiceReference(QueryBuilder.class
										.getName()));
				Map<String, String> queryBuilderMap = new HashMap<String, String>();
				queryBuilderMap.put(AcceleratorGenericConstants.PATH,
						directoryPath);
				if (StringUtils.isNotEmpty(extension)) {
					queryBuilderMap.put("nodename", "*." + extension);
				}
				queryBuilderMap.put(AcceleratorGenericConstants.ORDER_BY,
						AcceleratorGenericConstants.JCR_CONTENT_LAST_MODIFIED);
				queryBuilderMap.put(AcceleratorGenericConstants.ORDER_BY_SORT,
						AcceleratorGenericConstants.DESC);
				queryBuilderMap.put(AcceleratorGenericConstants.P_LIMIT,
						AcceleratorGenericConstants.P_LIMIT_VALUE);
				Query query = queryBuilder.createQuery(
						PredicateGroup.create(queryBuilderMap), jcrSession);
				query.setHitsPerPage(query.getResult().getTotalMatches());
				SearchResult result = query.getResult();
				listOfFiles = new String[(int) result.getTotalMatches()];
				int i = 0;
				List<Hit> hits = result.getHits();
				for (Hit hit : hits) {
					listOfFiles[i++] = hit.getResource().getPath();
				}
				LOGGER.info(" || listFiles || END");
				return listOfFiles;
			}
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return null;
	}

	/**
	 * This method returns the list of files of specific extensions in the
	 * directory
	 * 
	 * @param directoryPath
	 * @param extension
	 * @return
	 */
	public static File[] listFiles(String directoryPath, final String extension) {
		String methodName = "listFiles";
		LOGGER.info(" || " + methodName + " || START");
		File[] files = null;
		if (directoryPath != null && !StringUtils.trim(directoryPath).isEmpty()) {
			File directory = new File(directoryPath);
			if (directory.exists()) {
				LOGGER.debug(" || listFiles || directory || " + directory);
				if (StringUtils.isNotEmpty(extension)) {
					files = directory.listFiles(new FilenameFilter() {
						public boolean accept(File directory, String name) {
							return name.endsWith(extension);
						}
					});
				} else {
					files = directory.listFiles();
					LOGGER.debug(" || listFiles || files || " + files);
				}
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return files;
	}

	/**
	 * This method will move file from a one location in file system to another
	 * location
	 * 
	 * @param fileName
	 * @param destinationFolderPath
	 * @throws AcceleratorException
	 */
	public static boolean moveOrCopyFileOnServer(File fileName,
			String destinationFolderPath, boolean isMove)
			throws AcceleratorException {
		String methodName = "moveOrCopyFileOnServer Server to Server";
		LOGGER.info(" || " + methodName + " || START");

		boolean requestStatus = false;
		File destinationFolder = new File(destinationFolderPath);
		try {
			FileUtils.copyFileToDirectory(fileName, destinationFolder);
			if (isMove) {
				// deleteFile(fileName.getName(), fileName.getPath());
				deleteFile(
						fileName.getName(),
						fileName.getPath()
								.substring(
										0,
										fileName.getPath()
												.lastIndexOf(
														AcceleratorGenericConstants.BACKWARD_SLASH)));
			}
			requestStatus = true;
		} catch (IOException e) {
			LOGGER.error("An exception has occured in" + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return requestStatus;
	}

	/**
	 * This is an overloaded method for moving the a file in CRX to a server
	 * location
	 * 
	 * @param sourcePathInCRX
	 * @param destinationFolderPath
	 * @param isMove
	 * @throws AcceleratorException
	 */
	public static boolean moveOrCopyFileOnServer(String sourcePathInCRX,
			String fileNameWithExtension, String destinationFolderPath,
			boolean isMove) throws AcceleratorException {
		String methodName = "moveOrCopyFileOnServer CRX to Server";
		LOGGER.info(" || " + methodName + " || START");
		boolean requestStatus = false;

		String sourceFileContents = AcceleratorCRXUtils
				.readCRXData(sourcePathInCRX);
		if (!StringUtils.isEmpty(sourceFileContents)) {
			createFileFromString(sourceFileContents, fileNameWithExtension,
					destinationFolderPath, false);
			if (isMove) {
				deleteFile(fileNameWithExtension, sourcePathInCRX);
			}
		}
		requestStatus = true;

		LOGGER.info(" || " + methodName + " || END");
		return requestStatus;
	}

	/**
	 * This method will move the file from one location in CRX to another
	 * location in CRX
	 * 
	 * @param sourcePath
	 * @param destinationPath
	 * @throws AcceleratorException
	 */
	public static boolean moveOrCopyFileInCRX(String sourcePathInCRX,
			String fileNameWithExtension, String destinationPathInCRX,
			boolean isMove) throws AcceleratorException {
		String methodName = "moveOrCopyFileInCRX CRX to CRX";
		LOGGER.info(" || " + methodName + " || START");
		boolean requestStatus = false;

		String sourceFileContents = AcceleratorCRXUtils
				.readCRXData(sourcePathInCRX);
		if (!StringUtils.isEmpty(sourceFileContents)) {
			createFileFromString(sourceFileContents, fileNameWithExtension,
					destinationPathInCRX, true);
			if (isMove) {
				deleteFile(fileNameWithExtension, sourcePathInCRX);
			}
		}
		requestStatus = true;

		LOGGER.info(" || " + methodName + " || END");
		return requestStatus;
	}

	/**
	 * This method will move the file from one location in CRX to another
	 * location in CRX
	 * 
	 * @param sourcePath
	 * @param destinationPath
	 * @throws AcceleratorException
	 */
	public static boolean moveOrCopyFileInCRX(File sourceFile,
			String fileNameWithExtension, String destinationPathInCRX,
			boolean isMove) throws AcceleratorException {
		String methodName = "moveOrCopyFileInCRX Server to CRX";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean requestStatus = false;
		try {
			String fileContents = FileUtils.readFileToString(sourceFile,
					AcceleratorGenericConstants.UTF_8);
			LOGGER.info(" || " + methodName + " fileNameWithExtension || "
					+ fileNameWithExtension);

			LOGGER.info(" || " + methodName + " destinationPathInCRX || "
					+ destinationPathInCRX);
			if (!StringUtils.isEmpty(fileContents)) {
				requestStatus = createFileFromString(fileContents,
						fileNameWithExtension, destinationPathInCRX, true);
				if (isMove) {
					deleteFile(fileNameWithExtension,
							sourceFile.getAbsolutePath());
				}
			}
			jcrSession.save();
		} catch (IOException e) {
			LOGGER.error("An exception has occured in" + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} catch (AccessDeniedException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ItemExistsException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ITEM_EXISTS_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ReferentialIntegrityException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REFERENTIALINTEGRITY_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (ConstraintViolationException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.CONSTRAINT_VOILATION_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (InvalidItemStateException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.INVALID_ITEM_STATE_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (VersionException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.VERSION_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (LockException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.LOCK_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (NoSuchNodeTypeException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.NO_SUCH_NODETYPE_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (AcceleratorException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			requestStatus = false;
		}
		LOGGER.info(" || moveOrCopyFilesInCRX || "
				+ sourceFile.getAbsolutePath());

		LOGGER.info(" || " + methodName + " || END");
		return requestStatus;
	}

	/**
	 * This method will delete the file either from folder location on the
	 * server OR from CRX as part of cleanup
	 * 
	 * @param fileNameWithExtension
	 * @param sourceLocation
	 * @return
	 * @throws AcceleratorException
	 */

	public static boolean deleteFile(String fileNameWithExtension,
			String sourceLocation) throws AcceleratorException {
		String methodName = "deleteFile";

		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		try {
			Node cleanUpFolder = null;
			File cleanUpFile = new File(
					sourceLocation
							.endsWith(AcceleratorGenericConstants.FORWARD_SLASH) ? sourceLocation
							+ fileNameWithExtension
							: sourceLocation
									+ AcceleratorGenericConstants.FORWARD_SLASH
									+ fileNameWithExtension);
			if (cleanUpFile.exists()) {
				cleanUpFile.delete();
			} else if ((cleanUpFolder = JcrUtils.getNodeIfExists(
					sourceLocation, jcrSession)) != null) {
				if (cleanUpFolder.hasNode(fileNameWithExtension)) {
					cleanUpFolder.getNode(fileNameWithExtension).remove();
				}
			}
			LOGGER.info(" || " + methodName + " || END");
			return true;
		} catch (AccessDeniedException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (VersionException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.VERSION_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (LockException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.LOCK_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (ConstraintViolationException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.CONSTRAINT_VOILATION_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (PathNotFoundException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
	}

	/**
	 * This method is used for counting number of folders at particular time
	 * 
	 * @param folderRootPath
	 * @param folderName
	 * @return
	 * @throws AcceleratorException
	 */
	public static String createTimeBasedFolder(String folderRootPath,
			String folderName) throws AcceleratorException {
		String methodName = "createTimeBasedFolder";
		LOGGER.info(" || " + methodName + " || START");

		File currentFolderPath = null;
		String todaysDate = AcceleratorUtils.getDateOrTime(false);
		int counter = 0;

		// Get the list of folders in the report directory
		LOGGER.info(" || " + methodName + " || reportRootPath || "
				+ folderRootPath);
		File destinationFolder = new File(folderRootPath);
		File[] subDirs = destinationFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		// Count the number of folders created today
		for (File singleFolder : subDirs) {
			String singleFolderName = singleFolder.getName();
			if (singleFolderName.contains(todaysDate)) {
				counter++;
			}
		}
		currentFolderPath = new File(folderRootPath
				+ AcceleratorGenericConstants.FORWARD_SLASH + folderName
				+ AcceleratorGenericConstants.UNDERSCORE + todaysDate
				+ AcceleratorGenericConstants.UNDERSCORE + counter);

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
	 * Wrapper method to get all folders within a folder
	 * 
	 * @param folderName
	 * @return
	 */
	public static File[] getFolders(String folderName) {
		String methodName = "getFolders";
		LOGGER.info(" || " + methodName + " || START");
		File destinationFolder = new File(folderName);
		File[] subDirs = destinationFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		LOGGER.info(" || " + methodName + " || END");
		return subDirs;
	}

	/**
	 * wrapper methods to get all files within a folder
	 * 
	 * @param folderName
	 * @return
	 */
	public static File[] getFiles(String folderName) {
		String methodName = "getFiles";
		LOGGER.info(" || " + methodName + " || START");
		File destinationFolder = new File(folderName);
		File[] subFiles = destinationFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		LOGGER.info(" || " + methodName + " || END");
		return subFiles;
	}

}
