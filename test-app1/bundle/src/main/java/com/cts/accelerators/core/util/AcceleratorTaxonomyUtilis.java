package com.cts.accelerators.core.util;

import java.security.AccessControlException;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorComponentConstants;
import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.ootbcomps.dto.TagDTO;
import com.day.cq.tagging.InvalidTagFormatException;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * @author Cognizant Utility class for Migration Project
 * 
 * 
 */
public class AcceleratorTaxonomyUtilis {

	private static final String CLASS_NAME = AcceleratorTaxonomyUtilis.class
			.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AcceleratorTaxonomyUtilis.class);

	/**
	 * This method will verify if the tag needs to be created or modified.
	 * 
	 * @param tag
	 * @return
	 * @throws AcceleratorException
	 */
	public static final boolean verifyUpdate(TagDTO tag)
			throws AcceleratorException {

		String methodName = "verifyUpdate";
		LOGGER.info(" || " + methodName + " || START");
		ResourceResolver resorceResolver = ConnectionManager
				.getResourceResolver();
		TagManager tagManager = resorceResolver.adaptTo(TagManager.class);
		Tag taxonomy = tagManager.resolve(tag.getParentID()
				+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
				+ tag.getName());
		if (null != taxonomy) {
			LOGGER.info(" || " + methodName + " || END");
			return true;
		}
		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method will delete the provided tag from the repository if no
	 * content assets are linked. If forceful flag provided then references will
	 * be dropped and tag will be deleted.
	 * 
	 * @param resourcePath
	 * @return true if successful
	 * @throws AcceleratorException
	 */

	public static final boolean deleteTag(String resourcePath)
			throws AcceleratorException {
		String methodName = "deleteTag";
		LOGGER.info(" || " + methodName + " || START");
		ResourceResolver resorceResolver = ConnectionManager
				.getResourceResolver();
		TagManager tagManager = resorceResolver.adaptTo(TagManager.class);
		Tag taxonomy = tagManager.resolve(resourcePath);
		if (null != taxonomy) {
			tagManager.deleteTag(taxonomy, true);
			LOGGER.info(" || " + methodName + " || END");
			return true;
		}
		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method will create tag based on the specified properties
	 * 
	 * @param tag
	 * @return true if successful
	 * @throws AcceleratorException
	 */
	public static boolean createTag(TagDTO tag) throws AcceleratorException {

		String methodName = "createTag";
		LOGGER.info(" || " + methodName + " || START");
		LOGGER.info(" || " + methodName + " || Parent Id " + tag.getParentID());
		LOGGER.info(" || " + methodName + " ||  Tag Name " + tag.getName());
		LOGGER.info(" || " + methodName + " ||  Tag Id " + tag.getTagId());

		boolean creationFlag = false;
		ResourceResolver resorceResolver = ConnectionManager
				.getResourceResolver();
		TagManager tagManager = resorceResolver.adaptTo(TagManager.class);
		Tag taxonomy = null;

		if (tag.getParentID().contains(
				AcceleratorGenericConstants.TAG_FIELD_SEPARATOR)) {
			taxonomy = tagManager
					.resolve(tag.getParentID()
							+ AcceleratorGenericConstants.FORWARD_SLASH
							+ tag.getName());
			LOGGER.info(" || " + methodName + " ||  inside if block  "
					+ taxonomy);

		} else {
			taxonomy = tagManager.resolve(tag.getParentID()
					+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
					+ tag.getName());
		}
		try {
			if (null == taxonomy) {
				if (tag.getParentID().contains(
						AcceleratorGenericConstants.TAG_FIELD_SEPARATOR)) {
					taxonomy = tagManager.createTag(
							tag.getParentID()
									+ AcceleratorGenericConstants.FORWARD_SLASH
									+ tag.getName(), tag.getJcr_title(),
							tag.getJcr_description(), true);
				} else {
					taxonomy = tagManager.createTag(tag.getParentID()
							+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
							+ tag.getName(), tag.getJcr_title(),
							tag.getJcr_description(), true);
				}
				creationFlag = true && setParentTagTitles(taxonomy);
			} else {
				tagManager.deleteTag(taxonomy);
				if (tag.getParentID().contains(
						AcceleratorGenericConstants.TAG_FIELD_SEPARATOR)) {
					taxonomy = tagManager.createTag(
							tag.getParentID()
									+ AcceleratorGenericConstants.FORWARD_SLASH
									+ tag.getName(), tag.getJcr_title(),
							tag.getJcr_description(), true);
				} else {
					taxonomy = tagManager.createTag(tag.getParentID()
							+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR
							+ tag.getName(), tag.getJcr_title(),
							tag.getJcr_description(), true);
				}
				if (StringUtils.isNotEmpty(taxonomy.getPath())) {
					tag.setParentNodePath(taxonomy.getPath());
				}

				creationFlag = true && setParentTagTitles(taxonomy);
			}
		} catch (AccessControlException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCESS_CONTROL_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (InvalidTagFormatException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.INVALID_TAG_FORMAT_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		}

		LOGGER.info(" || " + methodName + " || END");
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will associate the provided tag to the specified resource
	 * content asset.
	 * 
	 * @param resourcePath
	 * @param tagNameArray
	 * @return true if successful
	 * @throws AcceleratorException
	 */
	public static final boolean associateTag(String resourcePath,
			String[] tagNameArray) throws AcceleratorException {
		String methodName = "associateTag";
		LOGGER.info(" || " + methodName + " || START");
		ResourceResolver resorceResolver = ConnectionManager
				.getResourceResolver();
		TagManager tagManager = resorceResolver.adaptTo(TagManager.class);
		Tag[] taxonomyArray = new Tag[tagNameArray.length];
		Resource targetResource = resorceResolver.getResource(resourcePath);
		if (null != targetResource && tagNameArray.length != 0) {
			for (int i = 0; i < tagNameArray.length; i++) {
				taxonomyArray[i] = tagManager.resolve(tagNameArray[i]);
			}
			tagManager.setTags(targetResource, taxonomyArray, true);
			LOGGER.info(" || " + methodName + " || END");
			return true;
		}
		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method will create tag based on the specified properties
	 * 
	 * @param tag
	 * @return true if successful
	 * @throws AcceleratorException
	 */
	public static String getMappedTagPath(String tagId)
			throws AcceleratorException {
		String methodName = "getMappedTagPath";
		LOGGER.info(" || " + methodName + " || START");
		ResourceResolver resorceResolver = ConnectionManager
				.getResourceResolver();
		TagManager tagManager = resorceResolver.adaptTo(TagManager.class);
		String tagPath = null;
		Tag taxonomy = null;
		if (tagId.contains(AcceleratorGenericConstants.TAG_FIELD_SEPARATOR)) {
			taxonomy = tagManager.resolve(tagId);
		} else {
			taxonomy = tagManager.resolve(tagId
					+ AcceleratorGenericConstants.TAG_FIELD_SEPARATOR);
		}
		if (null != taxonomy) {
			tagPath = taxonomy.getPath();
		}
		return tagPath;
	}

	public static final boolean setParentTagTitles(Tag tag)
			throws AcceleratorException {
		String methodName = "setParentTagTitles";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		if (null == tag)
			return true;
		try {
			Node tagNode = JcrUtils.getNodeIfExists(tag.getPath(),
					jcrSession);
			String tagTitle="";
			if (null != tagNode) {
				tagTitle=tagNode.hasProperty(AcceleratorGenericConstants.JCR_TITLE)?tagNode.getProperty(AcceleratorGenericConstants.JCR_TITLE).getString():"";
			}
			if (StringUtils.isEmpty(tagTitle)) {	
				tagNode.setProperty(AcceleratorGenericConstants.JCR_TITLE, tag.getName());
				jcrSession.save();
			}
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());

		}
		LOGGER.info(" || " + methodName + " || END");
		return setParentTagTitles(tag.getParent());
	}

}
