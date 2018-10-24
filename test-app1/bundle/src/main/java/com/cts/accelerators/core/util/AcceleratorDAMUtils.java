package com.cts.accelerators.core.util;

/**
 * 
 * @author Cognizant Application : Migration Project Name: AcceleratorCRXUtils
 *         Description: Utility class for Migration Project Dependency: none
 * 
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.commons.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.ootbcomps.dto.DAMDTO;
import com.cts.accelerators.migration.ootbcomps.dto.DamPropertyDTO;

public class AcceleratorDAMUtils {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AcceleratorDAMUtils.class);
	private static final String CLASS_NAME = AcceleratorDAMUtils.class
			.getName();

	final static int size = 1024;

	/**
	 * This method will download media files present either as URL or present in
	 * different folder and move to corresponding path WebDEV sync.
	 * 
	 * @param URL
	 * @param destinationPath
	 * @param saveAs
	 * @return true if successful
	 * @throws AcceleratorException
	 */
	public static boolean downloadMedia(String URL, String destinationPath,
			String saveAs, String downloadFrom, boolean proxyEnabled,
			String proxyHost, int proxyPort) throws AcceleratorException {
		String methodName = "downloadMedia";
		LOGGER.info(" || " + methodName + " || START");
		OutputStream outStream = null;
		URLConnection uCon = null;
		InputStream inputStream = null;
		try {
			HttpURLConnection con = null;
			java.net.Proxy proxy = null;
			if (proxyEnabled) {
				proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
						new InetSocketAddress(proxyHost, proxyPort));
			}
			URL urlFile;
			byte[] buf;
			int byteRead;
			urlFile = new URL(URL);
			int index = URL.lastIndexOf(AcceleratorGenericConstants.DOT);
			String extension = URL.substring(index, URL.length());

			LOGGER.debug(" || " + methodName + " || extension ||" + extension);
			if (StringUtils.isNotEmpty(URL) && destinationPath != null
					&& saveAs != null && URL != "" && destinationPath != ""
					&& saveAs != "") {
				LOGGER.debug(" || " + methodName + " || URL||" + URL);
				LOGGER.debug(" || " + methodName + " || destinationPath||"
						+ destinationPath);
				LOGGER.debug(" || " + methodName + " || saveAs||" + saveAs);

				File file = new File(destinationPath);
				if (!file.exists()) {
					file.mkdir();
				}
				LOGGER.debug(" || " + methodName + " || urlFile||" + urlFile);
				outStream = new BufferedOutputStream(new FileOutputStream(
						destinationPath + File.separator + saveAs + extension));

				LOGGER.debug(" || " + methodName + " || outStream||"
						+ outStream);

				if (downloadFrom == AcceleratorGenericConstants.URL) {
					if (proxyEnabled) {
						con = (HttpURLConnection) urlFile.openConnection(proxy);
					} else {
						con = (HttpURLConnection) urlFile.openConnection();
					}
					inputStream = con.getInputStream();
				} else if (downloadFrom == AcceleratorGenericConstants.FILE_SYSTEM_1) {
					uCon = urlFile.openConnection();
					inputStream = uCon.getInputStream();
				}
				buf = new byte[size];
				while ((byteRead = inputStream.read(buf)) != -1) {
					outStream.write(buf, 0, byteRead);
				}
				return true;
			}
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		} finally {
			try {
				inputStream.close();
				outStream.close();
			} catch (IOException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
						methodName, e.getCause());

			}
		}
		return false;
	}

	/**
	 * This method will add to mapping file to maintain record of what all media
	 * files have been uploaded with their respective DAM path
	 * 
	 * @param sourcePath
	 * @param destinationPath
	 * @return
	 * @throws IOException
	 */
	public static boolean addToMapping(String key, String value)
			throws AcceleratorException {
		String methodName = "addToMapping";
		LOGGER.info(" || " + methodName + " || START");

		boolean flag = false;

		try {
			HashMap<String, String> map = new HashMap<String, String>();
			String path = AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
					+ AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES;

			String fileContents = key + AcceleratorGenericConstants.EQUAL_TO
					+ value;
			LOGGER.info(" || " + methodName + " || fileContents || "
					+ fileContents);
			AcceleratorFileUtils
					.createFileFromString(
							fileContents,
							AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES,
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
							AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES,
							AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH,
							true);

			flag = true;
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
		return flag;
	}

	/**
	 * This method will verify if the content needs to be created or modified
	 * for a media file
	 * 
	 * @param dam
	 * @return true if update is needed
	 * @throws RepositoryException
	 * @throws AcceleratorException
	 */
	public static final boolean verifyUpdate(DAMDTO dam)
			throws RepositoryException, AcceleratorException {
		String methodName = "verifyUpdate";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		LOGGER.debug(" || " + methodName + " || jcrSession" + jcrSession);

		Node damVerifyUpdateNode = null;
		String mediaFileDestPath = dam.getDestinationPath();
		String singleFileSourcePath = dam.getSourcePath()
				+ AcceleratorGenericConstants.FORWARD_SLASH + dam.getFileName();
		LOGGER.debug(" || " + methodName + " || mediaFileDestPath "
				+ mediaFileDestPath + " || singleFileSourcePath "
				+ singleFileSourcePath);
		boolean updateflag = false;
		try {
			if (jcrSession.nodeExists(mediaFileDestPath)) {
				Node damNode = jcrSession.getNode(mediaFileDestPath);
				LOGGER.debug(" || " + methodName + " || damNode " + damNode);
				String[] temp = singleFileSourcePath
						.split(AcceleratorGenericConstants.FORWARD_SLASH);
				String damFileNameWithExtension = temp[temp.length - 1];
				LOGGER.debug(" || " + methodName
						+ " || damFileNameWithExtension "
						+ damFileNameWithExtension);
				if (damNode.hasNode(damFileNameWithExtension)) {
					damVerifyUpdateNode = damNode
							.getNode(damFileNameWithExtension);
					LOGGER.debug(" || " + methodName
							+ " || damVerifyUpdateNode " + damVerifyUpdateNode);

					if (damVerifyUpdateNode
							.hasNode(AcceleratorGenericConstants.JCR_CONTENT)) {
						List<DamPropertyDTO> customMetaProps = dam
								.getCustomMetaProperties();
						Node damPath = damVerifyUpdateNode
								.getNode(AcceleratorGenericConstants.JCR_CONTENT);
						if (customMetaProps != null) {
							setCustomDamProperty(damPath, customMetaProps);
						}
					}
					jcrSession.save();
					LOGGER.debug(" || " + methodName + " || if || END");
					updateflag=true;
					return true;
				} else {
					LOGGER.debug(" || " + methodName + " || else || END");
					return false;
				}

			}
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
		LOGGER.info(" || " + methodName + " || END");
		if(updateflag){
		return false;
		}
		else{
			return true;
		}
	}

	/**
	 * This method will delete the provided DAM asset from the DAM.
	 * 
	 * @param resourcePath
	 * @return true if successful
	 */
	public static final boolean deleteComponent(String resourcePath)
			throws AcceleratorException {
		String methodName = "deleteComponent";
		LOGGER.info(" || " + methodName + " || START");

		Session jcrSession;
		jcrSession = ConnectionManager.getSession();
		try {
			if (jcrSession.itemExists(resourcePath)) {
				jcrSession.removeItem(resourcePath);
				jcrSession.save();
				return true;
			}
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
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method will be called to create digital assets one at a time with
	 * basic as well as custom properties
	 * 
	 * @param dam
	 * @return
	 * @throws AcceleratorException
	 */
	public static final boolean singleDAMUpload(DAMDTO dam)
			throws AcceleratorException {
		String methodName = "singleDAMUpload";
		LOGGER.debug(" || " + methodName + " || START");

		Node damFileUploadNode = null;
		try {
			String mediaFileDestPath = dam.getDestinationPath();
			String singleFileSourcePath = dam.getSourcePath()
					+ AcceleratorGenericConstants.FORWARD_SLASH
					+ dam.getFileName();
			String port = dam.getPort();
			String host = dam.getHost();
			String userName = dam.getUserName();
			String password = dam.getPassword();

			// Verify if fresh upload or modifications
				Session jcrSession = ConnectionManager.getSession();

				if (StringUtils.isNotEmpty(mediaFileDestPath)
						&& StringUtils.isNotEmpty(singleFileSourcePath)
						&& StringUtils.isNotEmpty(port)
						&& StringUtils.isNotEmpty(host)
						&& StringUtils.isNotEmpty(password)
						&& StringUtils.isNotEmpty(userName)) {

					LOGGER.debug(" || " + methodName
							+ " || mediaFileDestPath beforeCall || "
							+ mediaFileDestPath);

					// Create root node if it doesn't exists
					Node damNode = null;
					damNode = (null == JcrUtils.getNodeIfExists(
							mediaFileDestPath, jcrSession)) ? AcceleratorCRXUtils
							.createFolder(mediaFileDestPath) : jcrSession
							.getNode(mediaFileDestPath);
					LOGGER.debug(" || " + methodName
							+ " || mediaFileDestPath || " + damNode);

					String url = null;
					String[] temp = singleFileSourcePath
							.split(AcceleratorGenericConstants.FORWARD_SLASH);
					String damFileNameWithExtension = temp[temp.length - 1];
					url = !mediaFileDestPath
							.endsWith(AcceleratorGenericConstants.FORWARD_SLASH) ? AcceleratorGenericConstants.HTTP
							+ host
							+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
							+ port
							+ mediaFileDestPath
							+ AcceleratorGenericConstants.FORWARD_SLASH
							: AcceleratorGenericConstants.HTTP
									+ host
									+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
									+ port
									+ AcceleratorGenericConstants.FORWARD_SLASH
									+ mediaFileDestPath;
					LOGGER.debug(" || " + methodName
							+ " || URL to save Path || " + url);

					// Create the list of commands to be executed
					List<String> command = new ArrayList<String>();
					String sourecPath = singleFileSourcePath
							.substring(
									0,
									singleFileSourcePath
											.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH));

					String curlCommandString = AcceleratorGenericConstants.CURL_CD
							+ AcceleratorGenericConstants.QUOTE
							+ sourecPath
							+ AcceleratorGenericConstants.QUOTE
							+ AcceleratorGenericConstants.AMP
							+ AcceleratorGenericConstants.CURL_COMMAND
							+ " -S -u"
							+ " "
							+ userName
							+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
							+ password
							+ " -T "
							+ damFileNameWithExtension
							+ " " + url;

					LOGGER.debug(" || " + methodName
							+ " || path eneterd in commandPropmt ||"
							+ curlCommandString);
					command.add(AcceleratorGenericConstants.EXE_COMMAND);
					command.add(AcceleratorGenericConstants.CURL_C);
					command.add(curlCommandString);

					LOGGER.debug(" || " + methodName + " || curl command ||"
							+ command);

					// Start process and execute commands
					ProcessBuilder processBuilder = new ProcessBuilder(command);
					processBuilder.redirectErrorStream(true);
					Process process;
					process = processBuilder.start();
					BufferedReader r = new BufferedReader(
							new InputStreamReader(process.getInputStream()));
					String line = "", line1 = "";

					StringBuilder stringBuilder = new StringBuilder();
					while ((line1 = r.readLine()) != null) {
						line = line1.trim();
						stringBuilder.append(line);

					}
					String output = stringBuilder.toString();
					if (StringUtils.isEmpty(output) || line.startsWith("0")
							|| output.contains("'curl' is not recognized")) {
						LOGGER.info(" || "
								+ methodName
								+ " || Failed to copy through curl.either curl is not install or invalid host,port,username,password"
								+ port + " " + host + " " + userName + " "
								+ password);
						return false;

					} else {
						LOGGER.info(" || " + methodName + " ||"
								+ stringBuilder.toString());
						jcrSession.save();
					}
					damFileUploadNode = damNode
							.getNode(damFileNameWithExtension);
					if (damFileUploadNode
							.hasNode(AcceleratorGenericConstants.JCR_CONTENT)) {
						Node damPath = damFileUploadNode
								.getNode(AcceleratorGenericConstants.JCR_CONTENT);
						if (dam.getCustomMetaProperties() != null) {
							setCustomDamProperty(damPath,
									dam.getCustomMetaProperties());
						}
						jcrSession.save();
					}
				} else {
					LOGGER.info(" || "
							+ methodName
							+ " || Failed because either source Path or destination path are empty or configurations are not set"
							+ " || singleFileSourcePath "
							+ singleFileSourcePath + " || mediaFileDestPath "
							+ mediaFileDestPath);
					return false;
				}
				LOGGER.debug(" || " + methodName + " || END");
				return true;
		
		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (StringIndexOutOfBoundsException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.STRING_INDEX_OUT_OF_BOUND, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}

	}

	/**
	 * Setting the custom properties of DAM
	 * 
	 * @param damPath
	 * @param customMetaProp
	 * @throws AcceleratorException
	 */
	public static void setCustomDamProperty(Node damPath,
			List<DamPropertyDTO> customMetaProps) throws AcceleratorException {
		String methodName = "setCustomDamProperty";
		LOGGER.info(" || " + methodName + " || START");

		for (DamPropertyDTO property : customMetaProps) {
			try {
				damPath.setProperty(property.getKey(), property.getValue());
				LOGGER.debug("|| " + methodName + " || Key -> "
						+ property.getKey() + property.getValue());
			} catch (ValueFormatException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.VALUE_FORMAT_EXCEPTION,
						CLASS_NAME, methodName, e.getCause());
			} catch (VersionException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.VERSION_EXCEPTION, CLASS_NAME,
						methodName, e.getCause());
			} catch (LockException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.LOCK_EXCEPTION, CLASS_NAME,
						methodName, e.getCause());
			} catch (ConstraintViolationException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.CONSTRAINT_VOILATION_EXCEPTION,
						CLASS_NAME, methodName, e.getCause());
			} catch (RepositoryException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
						methodName, e.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
	}
}
