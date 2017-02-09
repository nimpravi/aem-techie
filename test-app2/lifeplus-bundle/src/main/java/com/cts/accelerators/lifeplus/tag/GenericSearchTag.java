package com.cts.accelerators.lifeplus.tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.lifeplus.core.LifePlusComponentConstants;
import com.cts.accelerators.lifeplus.customDTO.GenericSearchTagDTO;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

/**
 * This Tag Handler class has the functional logic and is used for setting the
 * values to the tag attributes of Generic search tag for customComponents.tld
 * 
 * 
 * @author 406407
 * 
 */
public class GenericSearchTag extends BodyTagSupport {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GenericSearchTag.class);

	String fieldsNodePath;
	private String genericSearchOutput;
	private String rowListOutput;

	/**
	 * @return the fieldsNodePath
	 */
	public String getFieldsNodePath() {
		return fieldsNodePath;
	}

	/**
	 * @param fieldsNodePath
	 *            the fieldsNodePath to set
	 */
	public void setFieldsNodePath(String fieldsNodePath) {
		this.fieldsNodePath = fieldsNodePath;
	}

	/**
	 * @return the customFormOutput
	 */
	public String getGenericSearchList() {
		return genericSearchOutput;
	}

	/**
	 * @param customFormOutput
	 *            the customFormOutput to set
	 */
	public void setGenericSearchList(String a) {
		genericSearchOutput = a;
	}

	/**
	 * @return the rowListOutput
	 */
	public String getRowList() {
		return rowListOutput;
	}

	/**
	 * @param rowListOutput
	 *            the rowListOutput to set
	 */
	public void setRowList(String a) {
		rowListOutput = a;
	}

	Session jcrSession = null;

	/**
	 * This method will be used for setting the genericSearchList and rowList
	 * attributes of GenericSearchTag for the custom tag library
	 * (customComponents.tld).
	 * 
	 * */
	List<Node> selectoptionsNodes = new ArrayList<Node>();

	public int doStartTag() throws JspException {

		LOGGER.debug(">>>>>>>>>>GenericSearchTag>>>>>>>>>>>>doStartTag()>>>>>>>>>>>>>START");
		List<GenericSearchTagDTO> searchDetailsList = new ArrayList<GenericSearchTagDTO>();
		Set<String> rowDetailsList = new HashSet<String>();

		try {
			jcrSession = ConnectionManager.getSession();
		} catch (AcceleratorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		searchDetailsList = updateGenericSearchDetails(searchDetailsList);
		rowDetailsList = rowDetails(searchDetailsList);

		pageContext.setAttribute(genericSearchOutput, searchDetailsList);
		pageContext.setAttribute(rowListOutput, rowDetailsList);

		LOGGER.debug(">>>>>>>>>>GenericSearchTag>>>>>>>>>>>>doStartTag()>>>>>>>>>>>>>END");

		return SKIP_BODY;

	}

	/**
	 * This method will be used for fetching the list of Search details from the
	 * Generic Search Component.
	 * 
	 * @param - List of Generic Search Tag DTO objects
	 * 
	 * @return - an array containing the list of Search Details
	 * */

	private List<GenericSearchTagDTO> updateGenericSearchDetails(
			List<GenericSearchTagDTO> searchDetailsList) {
		// TODO Auto-generated method stub

		LOGGER.debug(">>>>>>>>>>updateFormDetails>>>>>>>>>>START");
		String methodName = "updateGenericSearchDetails";
		Node fieldsNode = null;
		NodeIterator itemIterator = null;

		GenericSearchTagDTO customFormComponentDTO = new GenericSearchTagDTO();

		if (fieldsNodePath != null) {
			try {

				fieldsNode = jcrSession.getNode(fieldsNodePath);
				LOGGER.debug(">>>>>>>>>>fieldsNode>>>>>>>>>>" + fieldsNode);

				if (fieldsNode != null) {
					Node multiFieldDetailsNode = null;

					itemIterator = fieldsNode.getNodes();

					LOGGER.debug(">>>>>>>>>>fieldsNode>>>>>>>>>>" + fieldsNode);

					while (itemIterator.hasNext()) {
						customFormComponentDTO = new GenericSearchTagDTO();
						multiFieldDetailsNode = itemIterator.nextNode();
						LOGGER.debug(">>>>>>>>>>multiFieldDetailsNode>>>>>>>>>>"
								+ multiFieldDetailsNode);

						customFormComponentDTO
								.setSelect(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_SELECT) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_SELECT)
										.getString()
										: null);
						customFormComponentDTO
								.setFieldLabel(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_FIELD_LABEL) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_FIELD_LABEL)
										.getString()
										: null);
						customFormComponentDTO
								.setFieldLabelDescription(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_FIELD_LABEL_DESCRIPTION) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_FIELD_LABEL_DESCRIPTION)
										.getString()
										: null);
						customFormComponentDTO
								.setFieldWidth(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_FIELD_WIDTH) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_FIELD_WIDTH)
										.getString()
										: null);
						customFormComponentDTO
								.setPredictive(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_PREDICTIVE) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_PREDICTIVE)
										.getString()
										: null);
						customFormComponentDTO
								.setTableName(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_TABLE_NAME) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_TABLE_NAME)
										.getString()
										: null);
						customFormComponentDTO
								.setFieldQueryParameter(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_FIELD_QUERY_PARAMETER) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_FIELD_QUERY_PARAMETER)
										.getString()
										: null);
						customFormComponentDTO
								.setAddGroup(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_ADD_GROUP) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_ADD_GROUP)
										.getString()
										: null);
						customFormComponentDTO
								.setGroupName(multiFieldDetailsNode
										.hasProperty(LifePlusComponentConstants.GENERICSEARCH_GROUP_NAME) ? multiFieldDetailsNode
										.getProperty(
												LifePlusComponentConstants.GENERICSEARCH_GROUP_NAME)
										.getString()
										: null);

						List<String> optionsList = new ArrayList<String>();

						if (multiFieldDetailsNode
								.hasProperty(LifePlusComponentConstants.GENERICSEARCH_SELECTION_OPTIONS)) {

							Node OptionsNode = multiFieldDetailsNode;

							Value[] options = OptionsNode
									.getProperty(
											LifePlusComponentConstants.GENERICSEARCH_SELECTION_OPTIONS)
									.getValues();
							if (options != null) {
								for (int i = 0; i < options.length; i++) {
									String text = options[i].getString();
									optionsList.add(text);

								}

							}
						}

						customFormComponentDTO.setSelectionOptions(optionsList);

						searchDetailsList.add(customFormComponentDTO);
						LOGGER.debug(">>>>>>>>>>searchDetailsList>>>>>>>>>>"
								+ searchDetailsList);
					}

				}
			} catch (PathNotFoundException e) {
				// TODO Auto-generated catch block
				LOGGER.error(" || " + methodName
						+ " || PATH NOT FOUND EXCEPTION || ", e);
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				LOGGER.error(" || " + methodName
						+ " || REPOSITORY EXCEPTION || ", e);
			}
		}
		LOGGER.debug(">>>>>>>>>>updateFormDetails>>>>>>>>>>END");

		return searchDetailsList;
	}

	/**
	 * This method will be used for fetching the list of Row details from the
	 * Generic Search Component.
	 * 
	 * @param - List of Generic Search Tag DTO objects
	 * 
	 * @return - an array containing the list of Row Details
	 * */

	private Set<String> rowDetails(List<GenericSearchTagDTO> searchDetailsList) {
		// TODO Auto-generated method stub

		List<String> groupNames = new ArrayList<String>();

		for (GenericSearchTagDTO field : searchDetailsList) {
			String fieldGroupName = field.getGroupName();
			LOGGER.debug(">>>>>>>>>>fieldGroupName>>>>>>>>>>" + fieldGroupName);

			if (fieldGroupName != null)

			{
				groupNames.add(fieldGroupName);
			}
		}

		Set<String> groupNamesSet = new HashSet<String>(groupNames);

		LOGGER.debug(">>>>>>>>>>groupSet>>>>>>>>>>" + groupNamesSet);

		return groupNamesSet;
	}

}
