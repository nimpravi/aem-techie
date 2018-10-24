package com.cts.accelerators.core.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.helpers.MigrationEventHandler;
import com.cts.accelerators.migration.ootbcomps.dto.CoreDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CustomTitleImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.PageComponentDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TableDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TextDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TextImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TitleImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TitleLinkDTO;

/**
 * @author cognizant Utility class for Migration Project
 * 
 */
public class AcceleratorUtils {

	private static final String CLASS_NAME = AcceleratorUtils.class.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AcceleratorUtils.class);

	/**
	 * Returns a random long number
	 * 
	 * @return long random number
	 */
	public static final long randomGenerator() {
		Random randomNumber = new Random();
		long randomLong = Math.abs(randomNumber.nextLong());
		return randomLong;
	}

	/**
	 * this method is used to validate for null pointer exceptions
	 * 
	 * @param requestParam
	 * @param methodName
	 * @return
	 */
	public static boolean nullValidation(Object requestParam, String methodName) {
		String thisMethodName = "nullValidation";
		LOGGER.info(" || " + thisMethodName + " || START");
		boolean flag = false;
		if (requestParam == null) {
			LOGGER.error(" : " + methodName + " : " + "null value");
			flag = false;
		} else {
			flag = true;
		}
		LOGGER.info(" || " + thisMethodName + " || END");
		return flag;
	}

	/**
	 * converting Hashmap to JSON array
	 * 
	 * @param propertyValMap
	 * @return
	 */
	public static JSONArray createPropertyJSON(
			HashMap<String, String> propertyValMap) {
		String methodName = "createPropertyJSON";
		LOGGER.info(" || " + methodName + " || START");

		JSONArray propValArr = null;

		JSONObject propJsonObject = new JSONObject(propertyValMap);
		ArrayList<JSONObject> propValList = new ArrayList<JSONObject>();
		propValList.add(propJsonObject);
		propValArr = new JSONArray(propValList);

		LOGGER.debug(methodName + " json otuput from map : " + propValArr);

		LOGGER.info(" || " + methodName + " || END");
		return propValArr;
	}

	/**
	 * 
	 * @param coreObject
	 * @param sourcePath
	 * @throws AcceleratorException
	 */
	public static void convertObjectToXML(CoreDTO coreObject, String sourcePath)
			throws AcceleratorException {
		String methodName = "convertObjectToXML";
		LOGGER.info(" || " + methodName + " || START");
		BufferedWriter writer = null;
		try {
			File file = new File(sourcePath);
			JAXBContext jaxbContext;
			try {
				writer = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				LOGGER.error("An exception has occured in " + methodName, e);
				throw new AcceleratorException(
						AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
						methodName, e.getCause());
			}
			jaxbContext = JAXBContext.newInstance(PageComponentDTO.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			/*
			 * jaxbMarshaller.setProperty(
			 * "com.sun.xml.bind.characterEscapeHandler", new
			 * CharacterEscapeHandler() { public void escape(char[] ch, int
			 * start, int length, boolean isAttVal, Writer writer) throws
			 * IOException { writer.write(ch, start, length); } });
			 */
			jaxbMarshaller.marshal(coreObject, file);
			jaxbMarshaller.marshal(coreObject, writer);

		} catch (JAXBException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			e.printStackTrace();
			throw new AcceleratorException(AcceleratorFaultCode.JAXB_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());

		}
		LOGGER.info(" || " + methodName + " || END");
	}

	/**
	 * A Simple method to either returns today date or current timestamp for
	 * unique file/folder name generations
	 * 
	 * @param needTime
	 * @return
	 */
	public static String getDateOrTime(boolean needTime) {
		String methodName = "getDateOrTime";
		LOGGER.info(" || " + methodName + " || START");
		Date date = new Date();
		String dateOrTime = "";
		if (needTime) {
			dateOrTime = ((Long) System.currentTimeMillis()).toString();
			LOGGER.debug(" || " + methodName + " || returning time || "
					+ dateOrTime);

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			dateOrTime = sdf.format(date);
			LOGGER.debug(" || " + methodName + " || returning date || "
					+ dateOrTime);
		}
		LOGGER.info(" || " + methodName + " || END");
		return dateOrTime;
	}

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
			System.out.println(coreObject);
			isSuccess = true;
			LOGGER.info(" || " + methodName + " || END");
			return coreObject;
		} catch (JAXBException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.JAXB_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} finally {
			if (!isSuccess) {
				return null;
			} else {
				return coreObject;
			}
		}
	}

	/**
	 * This method is used to retrieve the email address of the given user id
	 * 
	 * @param userId
	 * @param resouce
	 * @return
	 */
	public static String getEmailAddress(Resource resouce) {
		String methodName = "getEmailAddress";
		LOGGER.info(" || " + methodName + " || START");
		String emailAddress = null;
		UserManager userManager = resouce.adaptTo(UserManager.class);
		try {
			Session jcrSession = ConnectionManager.getSession();
			Authorizable authorizable = userManager.getAuthorizable(jcrSession
					.getUserID());
			Value[] emailArray = authorizable
					.getProperty(AcceleratorGenericConstants.PROFILE_EMAIL);
			if (null != emailArray && emailArray.length > 0) {
				emailAddress = emailArray[0].getString();
			}
		} catch (AcceleratorException e) {
			LOGGER.error("Accelerator Exception thrown from " + methodName
					+ " || " + e.getMessage());
		} catch (RepositoryException e) {
			LOGGER.error("Accelerator Exception thrown from " + methodName
					+ " || " + e.getMessage());
		}
		LOGGER.info(" || " + methodName + " || END");
		return emailAddress;
	}

	public static boolean checkCRXLocation(String crxPath)
			throws AcceleratorException {
		String methodName = "checkCRXLocation";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || crxPath || " + crxPath);
		boolean isValidPath = false;
		Session jcrSession = ConnectionManager.getSession();
		try {
			if (crxPath.startsWith(AcceleratorGenericConstants.FORWARD_SLASH)
					|| crxPath
							.startsWith(AcceleratorGenericConstants.BACKWARD_SLASH)) {
				crxPath = crxPath.substring(1, crxPath.length());
				Node rootNode = jcrSession.getRootNode();
				if (rootNode.hasNode(crxPath)) {
					isValidPath = true;
					LOGGER.info(" || " + methodName + " || isValidPath || "
							+ isValidPath);
				}
			}
		} catch (AccessDeniedException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ItemExistsException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ReferentialIntegrityException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ConstraintViolationException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (InvalidItemStateException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (VersionException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (LockException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (NoSuchNodeTypeException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_DENIED_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return isValidPath;
	}

	public static boolean checkServerLocation(String serverPath) {
		String methodName = "checkServerLocation";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || serverPath || " + serverPath);
		boolean isValidPath = false;
		File destinationFolder = new File(serverPath);
		if (destinationFolder.exists() && destinationFolder.isDirectory()) {
			isValidPath = true;
			LOGGER.info(" || " + methodName + " || isValidPath || "
					+ isValidPath);
		}
		LOGGER.info(" || " + methodName + " || END");
		return isValidPath;
	}

	/**
	 * This method will replace all the backward slash to forward slash
	 * 
	 * @param path
	 * @return path starting with backward slash to forward slash
	 */
	public static String replaceSlashInPath(String path) {
		String methodName = "replaceSlashInPath";
		String filePath = path;
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || path " + path);
		if (filePath.startsWith(AcceleratorGenericConstants.BACKWARD_SLASH)) {
			filePath = filePath.replace(
					AcceleratorGenericConstants.BACKWARD_SLASH,
					AcceleratorGenericConstants.FORWARD_SLASH);
		}
		LOGGER.info(" || " + methodName + " || path " + path);
		LOGGER.info(" || " + methodName + " || END");
		return filePath;
	}

	/**
	 * This method will return true or false based on pattern matching
	 * 
	 * @param pattern
	 * @return true or false based on pattern matching
	 */
	public static boolean patternMatching(String pattern, String inputVal) {
		String methodName = "replaceSlashInPath";
		boolean result;
		LOGGER.info(" || " + methodName + " || START");
		result = Pattern.matches(pattern, inputVal);
		LOGGER.info(" || " + methodName + " || END");
		return result;
	}

	public static void main(String... args) {
		CustomTitleImageDTO customDTO = new CustomTitleImageDTO();
		//title-image
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setFileReference("ABC");
		
		TitleImageDTO titleImageDTO = new TitleImageDTO();
		titleImageDTO.setImageDTO(imageDTO);
		titleImageDTO.setNodeName("node1");
		titleImageDTO.setTitle("title");
		
		TextImageDTO textImageDTO = new TextImageDTO();
		textImageDTO.setImageDTO(imageDTO);
		textImageDTO.setNodeName("nodeName");
		
		TextDTO textDTO = new TextDTO();
		textDTO.setNodeName("textnode");
		textDTO.setText("Text is the here");
		textImageDTO.setTextDTO(textDTO);
		
		TitleLinkDTO titleLinkDTO = new TitleLinkDTO();
		titleLinkDTO.setTitle("title");
		titleLinkDTO.setLink("http://www");
		
		customDTO.setTextImageDTO(textImageDTO);
		customDTO.setTitleImageDTO(titleImageDTO);
		customDTO.setTitleLinkDTO(titleLinkDTO);
		
		try {
			convertObjectToXML(customDTO, "D://MigrationFolders//file.xml");
		} catch (AcceleratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
