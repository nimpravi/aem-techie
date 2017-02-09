package com.cts.accelerators.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorComponentConstants;
import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.ootbcomps.dto.CustomCarouselDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CustomCarouselItemDTO;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.helpers.ReportGeneratorHelper;
import com.cts.accelerators.migration.ootbcomps.designdto.BreadcrumbDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.DesignTitleDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.ToolbarDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.TopnavDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.UserInfoDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ArchiveDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CarouselDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ChartDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ContainerDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CoreDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CustomImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CustomTextImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.DownloadDTO;
import com.cts.accelerators.migration.ootbcomps.dto.EntryListDTO;
import com.cts.accelerators.migration.ootbcomps.dto.EntryTextDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ExternalDTO;
import com.cts.accelerators.migration.ootbcomps.dto.FlashDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ImageMultiFieldDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ListChildrenDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ListDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ListOfLinkDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ListOfLinkItemDTO;
import com.cts.accelerators.migration.ootbcomps.dto.LoginDTO;
import com.cts.accelerators.migration.ootbcomps.dto.PageComponentDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ProfileDTO;
import com.cts.accelerators.migration.ootbcomps.dto.RedirectDTO;
import com.cts.accelerators.migration.ootbcomps.dto.ReferenceDTO;
import com.cts.accelerators.migration.ootbcomps.dto.SearchDTO;
import com.cts.accelerators.migration.ootbcomps.dto.SiteMapDTO;
import com.cts.accelerators.migration.ootbcomps.dto.SlideShowDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TableDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TextDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TextImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TitleDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TitleImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.CustomTitleImageDTO;
import com.cts.accelerators.migration.ootbcomps.dto.TitleLinkDTO;
import com.cts.accelerators.migration.ootbcomps.dto.VideoDTO;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: AcceleratorCRXUtils
 *         Description: Utility class for Migration Project Dependency: none
 * 
 * 
 *         utf-8 encoding java.net.URLDecoder.decode(request.getParameter("id"),
 *         "UTF-8")
 * 
 * 
 */
public class AcceleratorCRXUtils {

	private static final String CLASS_NAME = AcceleratorCRXUtils.class
			.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AcceleratorCRXUtils.class);

	/**
	 * This method will verify if the content needs to be created or modified
	 * for a component/page
	 * 
	 * @param core
	 * @return true if update is needed
	 * @throws AcceleratorException
	 */
	public static final boolean verifyUpdate(CoreDTO core)
			throws AcceleratorException {
		String methodName = "verifyUpdate";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		try {
			String parentNodePath = core.getParentNodePath();
			if (core instanceof ContainerDTO) {
				ContainerDTO containerDTO = (ContainerDTO) core;
				if (null != JcrUtils
						.getNodeIfExists(
								parentNodePath
										.endsWith(AcceleratorGenericConstants.FORWARD_SLASH) ? parentNodePath
										+ containerDTO.getContainerNodeName()
										: parentNodePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ containerDTO
														.getContainerNodeName(),
								jcrSession)) {
					LOGGER.info(" || " + methodName + " || Container || "
							+ containerDTO.getContainerNodeName());
					return true;
				}

				//
				if (null != JcrUtils
						.getNodeIfExists(
								parentNodePath
										.endsWith(AcceleratorGenericConstants.FORWARD_SLASH) ? parentNodePath
										+ containerDTO
												.getGlobalFooterContainerName()
										: parentNodePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ containerDTO
														.getGlobalFooterContainerName(),
								jcrSession)) {
					LOGGER.info(" || " + methodName + " || if condition || "
							+ containerDTO.getContainerNodeName());
					return true;
				}
				//

				LOGGER.info(" || " + methodName
						+ " || else globalFooterContainer || "
						+ containerDTO.getContainerNodeName());
				return false;

			} else {
				if (null != JcrUtils
						.getNodeIfExists(
								parentNodePath
										.endsWith(AcceleratorGenericConstants.FORWARD_SLASH) ? parentNodePath
										+ core.getNodeName()
										: parentNodePath
												+ AcceleratorGenericConstants.FORWARD_SLASH
												+ core.getNodeName(),
								jcrSession)) {
					return true;
				}
			}
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());

		}
		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method will delete the provided component from the content node.
	 * 
	 * @param core
	 *            , resourcePath
	 * @return true if successful
	 * @throws AcceleratorException
	 */
	public static final boolean deleteComponent(String resourcePath)
			throws AcceleratorException {
		String methodName = "deleteComponent";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		try {
			if (jcrSession.itemExists(resourcePath)) {
				jcrSession.removeItem(resourcePath);
				jcrSession.save();
				return true;
			}
		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method will create container node under content node based on the
	 * provided properties present in the DTO
	 * 
	 * @param container
	 * @return true if successful
	 * @throws AcceleratorException
	 */
	public static final boolean createContainer(ContainerDTO container)
			throws AcceleratorException {
		String methodName = "createContainer";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					container.getParentNodePath(), jcrSession);
			String containerNodeName = container.getContainerNodeName();
			String containerNodePath = null;
			if (!verifyUpdate(container) && null != parentNode) {
				Node containerNode = parentNode.addNode(containerNodeName);
				containerNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				String containerVariance = container.getContainerVariance();
				containerNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								containerVariance
										.equalsIgnoreCase(AcceleratorGenericConstants.PARSYS) ? AcceleratorGenericConstants.PARSYS_COMPONENT_PATH
										: AcceleratorGenericConstants.IPARSYS_COMPONENT_PATH);
				containerNodePath = containerNode.getPath();

			} else if (null != parentNode) {
				Node containerNode = parentNode.getNode(containerNodeName);
				containerNodePath = containerNode.getPath();
			}
			List<CoreDTO> components = container.getComponents();
			Iterator<CoreDTO> componentsIterator = components.iterator();
			while (componentsIterator.hasNext()) {
				CoreDTO coreDTO = componentsIterator.next();
				coreDTO.setParentNodePath(containerNodePath);
				LOGGER.info(" || " + methodName + " || coreDTO before");
				LOGGER.info(" || " + methodName + " || coreDTO || " + coreDTO);
				creationFlag = getDTOInstanceAndSaveToSession(coreDTO);
			}

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/*
	 * 
	 */

	public static final boolean createCustomContainer(ContainerDTO container)
			throws AcceleratorException {
		String methodName = "createContainer";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					container.getParentNodePath(), jcrSession);

			//
			String globalFooterNodePath = null;
			String globalFooterContainer = container
					.getGlobalFooterContainerName();

			LOGGER.info(" || " + methodName + " || globalFooterContainer || "
					+ globalFooterContainer);
			//
			if (!verifyUpdate(container) && null != parentNode) {
				LOGGER.info(" || " + methodName + " || parentNode || "
						+ parentNode.getPath());

				//
				Node globalFooterNode = parentNode
						.addNode(globalFooterContainer);
				//

				//
				globalFooterNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);

				//
				String globalContainerVariance = container
						.getGlobalFooterContainer();

				if (null != globalContainerVariance) {
					globalFooterNode
							.setProperty(
									AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
									AcceleratorComponentConstants.GLOBAL_FOOTER_COMPONENT_PATH);

					LOGGER.info(" || "
							+ methodName
							+ " || globalFooterContainer"
							+ globalFooterNode
									.setProperty(
											AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
											AcceleratorComponentConstants.GLOBAL_FOOTER_COMPONENT_PATH));
				}
				globalFooterNodePath = globalFooterNode.getPath();
				LOGGER.info(" || " + methodName + " || globalFooterNodePath"
						+ globalFooterNodePath);

			} else if (null != parentNode) {

				//
				Node globalFooterNode = parentNode
						.getNode(globalFooterContainer);
				globalFooterNodePath = globalFooterNode.getPath();

				LOGGER.info(" || " + methodName + " || globalFooterNodePath"
						+ globalFooterNodePath);
				//
			}
			List<CoreDTO> components = container.getComponents();
			Iterator<CoreDTO> componentsIterator = components.iterator();
			while (componentsIterator.hasNext()) {
				CoreDTO coreDTO = componentsIterator.next();

				coreDTO.setParentNodePath(globalFooterNodePath);

				LOGGER.info(" || " + methodName
						+ " || coreDTO.setParentNodePath(globalFooterNodePath)");
				LOGGER.info(" || " + methodName + " || coreDTO before");
				LOGGER.info(" || " + methodName + " || coreDTO || " + coreDTO);
				creationFlag = getDTOInstanceAndSaveToSession(coreDTO);

			}

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @param coreDTO
	 * @return boolean
	 * @throws AcceleratorException
	 */
	public static final boolean getDTOInstanceAndSaveToSession(CoreDTO coreDTO)
			throws AcceleratorException {
		String methodName = "getDTOInstanceAndSaveToSession";
		LOGGER.info(" || " + methodName + " || START");
		if (coreDTO instanceof TextDTO) {
			return createTextNode((TextDTO) coreDTO);
		} else if (coreDTO instanceof ArchiveDTO) {
			return createArchiveNode((ArchiveDTO) coreDTO);
		} else if (coreDTO instanceof BreadcrumbDTO) {
			return createBreadcrumbNode((BreadcrumbDTO) coreDTO);
		} else if (coreDTO instanceof ChartDTO) {
			return createChartNode((ChartDTO) coreDTO);
		} else if (coreDTO instanceof CarouselDTO) {
			return createCarouselNode((CarouselDTO) coreDTO);
		} else if (coreDTO instanceof DownloadDTO) {
			return createDownloadNode((DownloadDTO) coreDTO);
		} else if (coreDTO instanceof EntryListDTO) {
			return createEntryListNode((EntryListDTO) coreDTO);
		} else if (coreDTO instanceof EntryTextDTO) {
			return createEntrytextNode((EntryTextDTO) coreDTO);
		} else if (coreDTO instanceof ExternalDTO) {
			return createExternalNode((ExternalDTO) coreDTO);
		} else if (coreDTO instanceof FlashDTO) {
			return createFlashNode((FlashDTO) coreDTO);
		} else if (coreDTO instanceof ImageDTO) {
			return createImageNode((ImageDTO) coreDTO);
		} else if (coreDTO instanceof ListChildrenDTO) {
			return createListChildrenNode((ListChildrenDTO) coreDTO);
		} else if (coreDTO instanceof ListDTO) {
			return createListNode((ListDTO) coreDTO);
		} else if (coreDTO instanceof LoginDTO) {
			return createLoginNode((LoginDTO) coreDTO);
		} else if (coreDTO instanceof ProfileDTO) {
			return createProfileNode((ProfileDTO) coreDTO);
		} else if (coreDTO instanceof RedirectDTO) {
			return createRedirectNode((RedirectDTO) coreDTO);
		} else if (coreDTO instanceof ReferenceDTO) {
			return createReferenceNode((ReferenceDTO) coreDTO);
		} else if (coreDTO instanceof SearchDTO) {
			return createSearchNode((SearchDTO) coreDTO);
		} else if (coreDTO instanceof SiteMapDTO) {
			return createSiteMapNode((SiteMapDTO) coreDTO);
		} else if (coreDTO instanceof SlideShowDTO) {
			return createSlideShowNode((SlideShowDTO) coreDTO);
		} else if (coreDTO instanceof TableDTO) {
			return createTableNode((TableDTO) coreDTO);
		} else if (coreDTO instanceof TextImageDTO) {
			return createTextImageNode((TextImageDTO) coreDTO);
		} else if (coreDTO instanceof TitleDTO) {
			return createTitleNode((TitleDTO) coreDTO);
		} else if (coreDTO instanceof VideoDTO) {
			return createVideoNode((VideoDTO) coreDTO);
		} else if (coreDTO instanceof BreadcrumbDTO) {
			return createBreadcrumbNode((BreadcrumbDTO) coreDTO);
		} else if (coreDTO instanceof DesignTitleDTO) {
			return createDesignTitleNode((DesignTitleDTO) coreDTO);
		} else if (coreDTO instanceof ToolbarDTO) {
			return createToolbarNode((ToolbarDTO) coreDTO);
		} else if (coreDTO instanceof TopnavDTO) {
			return createTopnavNode((TopnavDTO) coreDTO);
		} else if (coreDTO instanceof UserInfoDTO) {
			return createUserInfoNode((UserInfoDTO) coreDTO);
		} else if (coreDTO instanceof CustomImageDTO) {
			return createCustomImage((CustomImageDTO) coreDTO);
		} else if (coreDTO instanceof TitleLinkDTO) {
			return createTitleLinkNode((TitleLinkDTO) coreDTO);
		} else if (coreDTO instanceof CustomCarouselDTO) {
			return createCustomCarouselNode((CustomCarouselDTO) coreDTO);
		} else if (coreDTO instanceof ImageMultiFieldDTO) {
			return createImageMultiFieldNode((ImageMultiFieldDTO) coreDTO);
		} else if (coreDTO instanceof TitleImageDTO) {
			return createImageTitleNode((TitleImageDTO) coreDTO);
		} else if (coreDTO instanceof CustomTitleImageDTO) {
			return createImageTitleLinkNode((CustomTitleImageDTO) coreDTO);
		} else if (coreDTO instanceof ListOfLinkDTO) {
			return createListofLink((ListOfLinkDTO) coreDTO);
		} else if (coreDTO instanceof CustomTextImageDTO) {
			return createCustomTextImageNode((CustomTextImageDTO) coreDTO);
		}

		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method will create a single content node based on the provided
	 * properties present in the DTO. If updateFlag false then it is for
	 * creation otherwise update.
	 * 
	 * @param pageComponentDTO
	 * @return true if successful
	 * @throws AcceleratorException
	 */
	public static final boolean createPage(PageComponentDTO pageComponentDTO,
			boolean updateFlag, boolean createDuplicatePage)
			throws AcceleratorException {
		String methodName = "createPage";
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		Node contentNode = null, designContentNode = null, templateNode = null;
		String contentNodePath = null, templateNodePath = null, cqDesignNodePath = null;
		// check if template name or resourcetype is empty
		if (StringUtils.isEmpty(pageComponentDTO.getResourceType())
				|| StringUtils.isEmpty(pageComponentDTO.getTemplate())) {
			throw new AcceleratorException(
					AcceleratorFaultCode.EMPTY_TEMPLATE_NAME_OR_RESOURCE_TYPE,
					CLASS_NAME, methodName);
		}
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					pageComponentDTO.getParentNodePath(), jcrSession);
			Node hierarchyNode = JcrUtils.getNodeIfExists(
					pageComponentDTO.getCqDesignPath(), jcrSession);
			if (null != hierarchyNode) {
				LOGGER.info(" || " + methodName + " || inside hierarchyNode");
				if (hierarchyNode.hasNode(Node.JCR_CONTENT)) {
					LOGGER.info(" || " + methodName
							+ " || inside hierarchyNode");
					Node hierarchyContentNode = hierarchyNode
							.getNode(Node.JCR_CONTENT);
					cqDesignNodePath = hierarchyContentNode
							.hasProperty(AcceleratorGenericConstants.CQ_DESIGNPATGH) ? hierarchyContentNode
							.getProperty(
									AcceleratorGenericConstants.CQ_DESIGNPATGH)
							.getString() : null;
				}

			}
			Node designNode = StringUtils.isNotEmpty(cqDesignNodePath) ? JcrUtils
					.getNodeIfExists(cqDesignNodePath, jcrSession) : null;
			LOGGER.info(" || " + methodName + " || before designNode");
			if (null != designNode) {
				if (designNode.hasNode(Node.JCR_CONTENT)) {
					LOGGER.info(" || " + methodName + " || inside designNode");
					designContentNode = designNode.getNode(Node.JCR_CONTENT);
					String template = pageComponentDTO.getTemplate();
					String templateName = template
							.substring(template
									.lastIndexOf(AcceleratorGenericConstants.FORWARD_SLASH) + 1);
					if (designContentNode.hasNode(templateName)) {
						templateNode = designContentNode.getNode(templateName);
					} else {
						templateNode = designContentNode.addNode(templateName);
					}
					templateNodePath = templateNode.getPath();
				}
			}
			LOGGER.info(" || " + methodName + " || before pageNode");
			if (null != parentNode && updateFlag
					&& verifyUpdate(pageComponentDTO)) {
				if (parentNode.hasNode(pageComponentDTO.getNodeName())) {
					LOGGER.info(" || " + methodName + " || inside pageNode");
					Node pageComponentNode = parentNode
							.getNode(pageComponentDTO.getNodeName());
					if (pageComponentNode.hasNode(Node.JCR_CONTENT)) {
						contentNode = pageComponentNode
								.getNode(Node.JCR_CONTENT);
					}
				}
				LOGGER.debug(" || " + methodName
						+ " || update required for node || "
						+ contentNode.getPath());
				creationFlag = true;
			} else if (null != parentNode && createDuplicatePage) {
				LOGGER.debug(" || " + methodName
						+ " || duplicate page required under node || "
						+ parentNode.getPath());
				int i = 0;
				while (true) {
					if (null != parentNode
							&& parentNode.hasNode(i == 0 ? pageComponentDTO
									.getNodeName() : pageComponentDTO
									.getNodeName() + i)) {
						i++;
						continue;
					} else {
						LOGGER.debug(" || " + methodName
								+ " || node modified under node || "
								+ parentNode.getPath());
						Node pageComponentNode = parentNode
								.addNode(i == 0 ? pageComponentDTO
										.getNodeName() : pageComponentDTO
										.getNodeName() + i);
						pageComponentNode
								.setPrimaryType(AcceleratorGenericConstants.CQ_PAGE);
						contentNode = pageComponentNode
								.addNode(Node.JCR_CONTENT);
						contentNode
								.setPrimaryType(AcceleratorGenericConstants.CQ_PAGECONTENT);
						contentNode.setProperty(
								AcceleratorGenericConstants.CQ_TEMPLATE,
								pageComponentDTO.getTemplate());
						contentNode
								.setProperty(
										AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
										pageComponentDTO.getResourceType());

						creationFlag = true;
						break;
					}
				}
			} else if (null != parentNode
					&& !parentNode.hasNode(pageComponentDTO.getNodeName())) {
				Node pageComponentNode = parentNode.addNode(pageComponentDTO
						.getNodeName());
				pageComponentNode
						.setPrimaryType(AcceleratorGenericConstants.CQ_PAGE);
				contentNode = pageComponentNode.addNode(Node.JCR_CONTENT);
				contentNode
						.setPrimaryType(AcceleratorGenericConstants.CQ_PAGECONTENT);
				contentNode.setProperty(
						AcceleratorGenericConstants.CQ_TEMPLATE,
						pageComponentDTO.getTemplate());
				contentNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						pageComponentDTO.getResourceType());
				creationFlag = true;

			}
			if (null != contentNode) {
				contentNodePath = contentNode.getPath();
				contentNode.setProperty(javax.jcr.Property.JCR_TITLE,
						pageComponentDTO.getJcr_title());
				if (null != pageComponentDTO.getPagePropertiesDTO()) {
					contentNode.setProperty(javax.jcr.Property.JCR_DESCRIPTION,
							pageComponentDTO.getPagePropertiesDTO()
									.getJcr_description());
					contentNode.setProperty(
							AcceleratorGenericConstants.HIDE_IN_NAV,
							pageComponentDTO.getPagePropertiesDTO()
									.getHideInNav());
					contentNode.setProperty(
							AcceleratorGenericConstants.NAVIGATION_TITLE,
							pageComponentDTO.getPagePropertiesDTO()
									.getNavTitle());
					contentNode.setProperty(
							AcceleratorGenericConstants.OFF_TIME,
							pageComponentDTO.getPagePropertiesDTO()
									.getOffTime());
					contentNode
							.setProperty(AcceleratorGenericConstants.ON_TIME,
									pageComponentDTO.getPagePropertiesDTO()
											.getOnTime());
					contentNode.setProperty(
							AcceleratorGenericConstants.PAGE_TITLE,
							pageComponentDTO.getPagePropertiesDTO()
									.getPageTitle());
					contentNode.setProperty(
							AcceleratorGenericConstants.SLING_REDIRECT,
							pageComponentDTO.getPagePropertiesDTO()
									.getSling_redirect());
					contentNode.setProperty(
							AcceleratorGenericConstants.SLING_VANITYPATH,
							pageComponentDTO.getPagePropertiesDTO()
									.getSling_vanityPath());
					contentNode.setProperty(
							AcceleratorGenericConstants.SUB_TITLE,
							pageComponentDTO.getPagePropertiesDTO()
									.getSubtitle());
				}
				jcrSession.save();
				if (StringUtils.isNotEmpty(pageComponentDTO.getTagIds())) {
					AcceleratorTaxonomyUtilis.associateTag(
							contentNodePath,
							pageComponentDTO.getTagIds().split(
									AcceleratorGenericConstants.CSV_SEPARATOR));
				}
			}
			jcrSession.save();

			if (null == parentNode) {
				LOGGER.error(CLASS_NAME
						+ " || "
						+ methodName
						+ " || Page not created as Parent node mentioned does not exist || "
						+ pageComponentDTO.getParentNodePath());
			}

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					"createPageNode()", e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		if (creationFlag && StringUtils.isNotEmpty(contentNodePath)) {
			List<ContainerDTO> authorComponents = pageComponentDTO
					.getAuthorComponentsList();
			if (null != authorComponents) {
				Iterator<ContainerDTO> containerDTOIterator = authorComponents
						.iterator();
				while (containerDTOIterator.hasNext()) {
					ContainerDTO containerDTO = containerDTOIterator.next();
					containerDTO.setParentNodePath(contentNodePath);
					creationFlag = createContainer(containerDTO);
				}
			}
		}
		if (creationFlag && StringUtils.isNotEmpty(templateNodePath)) {
			List<ContainerDTO> designComponents = pageComponentDTO
					.getDesignComponentsList();
			if (null != designComponents) {
				Iterator<ContainerDTO> designContainerDTOIterator = designComponents
						.iterator();
				while (designContainerDTOIterator.hasNext()) {
					LOGGER.debug(" || " + methodName
							+ " || inside iterator || " + contentNodePath);
					ContainerDTO containerDTO = designContainerDTOIterator
							.next();
					containerDTO.setParentNodePath(templateNodePath);
					creationFlag = createContainer(containerDTO);
				}
			}
		}

		if (creationFlag && StringUtils.isNotEmpty(contentNodePath)) {
			LOGGER.debug(" || " + methodName + " || pageComponentDTO || "
					+ pageComponentDTO.getNodeName());
			LOGGER.debug(" || " + methodName + " || getFooterDTO || "
					+ pageComponentDTO.getFooterDTO());
			if (null != pageComponentDTO.getFooterDTO()) {
				List<ContainerDTO> authorComponents = pageComponentDTO
						.getFooterDTO().getAuthorComponentsList();
				LOGGER.debug(" || " + methodName + " || authorComponents1 || " + authorComponents);

				if (null != authorComponents) {
					LOGGER.debug(" || " + methodName
							+ " || authorComponents 1not null || "
							+ authorComponents);
					Iterator<ContainerDTO> containerDTOIterator = authorComponents
							.iterator();
					while (containerDTOIterator.hasNext()) {
						ContainerDTO containerDTO = containerDTOIterator.next();
						LOGGER.debug(" || "
								+ methodName
								+ " || containerDTO1 getGlobalFooterContainerName || "
								+ containerDTO.getGlobalFooterContainerName());

						LOGGER.debug(" || "
								+ methodName
								+ " || containerDTO1 getGlobalFooterContainer || "
								+ containerDTO.getGlobalFooterContainer());

						containerDTO.setParentNodePath(contentNodePath);
						creationFlag = createCustomContainer(containerDTO);
					}
				}
			}
			
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a design node based on the provided component and
	 * its properties present in the DTO
	 * 
	 * @param core
	 * @return true if successful
	 */
	public static final boolean createDesignNodes(CoreDTO core) {
		return true;
	}

	/**
	 * This method will create a Login node based on the provided component and
	 * its properties present in the LoginDTO
	 * 
	 * @param loginDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */

	public static final boolean createLoginNode(LoginDTO loginDTO)
			throws AcceleratorException {
		String methodName = "createLoginNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					loginDTO.getParentNodePath(), jcrSession);
			String nodeName = loginDTO.getNodeName();
			Node loginNode = null;
			if (!verifyUpdate(loginDTO) && null != parentNode) {
				loginNode = parentNode.addNode(nodeName);
				loginNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				loginNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.LOGINDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					loginNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != loginNode) {
				loginNode.setProperty(
						AcceleratorComponentConstants.LOGINDTO_SECTION_LABEL,
						loginDTO.getSectionLabel());
				loginNode.setProperty(
						AcceleratorComponentConstants.LOGINDTO_USERNAME_LABEL,
						loginDTO.getUsernameLabel());
				loginNode.setProperty(
						AcceleratorComponentConstants.LOGINDTO_PASSWORD_LABEL,
						loginDTO.getPasswordLabel());
				loginNode.setProperty(
						AcceleratorComponentConstants.LOGINDTO_LOGIN_LABEL,
						loginDTO.getLoginLabel());
				loginNode.setProperty(
						AcceleratorComponentConstants.LOGINDTO_REDIRECT_TO,
						loginDTO.getRedirectTo());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Flash node based on the provided component and
	 * its properties present in the FlashDTO
	 * 
	 * @param flashDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */

	public static final boolean createFlashNode(FlashDTO flashDTO)
			throws AcceleratorException {
		String methodName = "createFlashNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					flashDTO.getParentNodePath(), jcrSession);
			String nodeName = flashDTO.getNodeName();
			Node flashNode = null;
			if (!verifyUpdate(flashDTO) && null != parentNode) {
				flashNode = parentNode.addNode(nodeName);
				flashNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				flashNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.FLASHDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					flashNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != flashNode) {
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_FILE_REFERENCE,
						flashDTO.getFileReference());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_HEIGHT,
						flashDTO.getHeight());
				LOGGER.debug(" || " + methodName + " || height2 || " + flashNode.getProperty(AcceleratorComponentConstants.FLASHDTO_HEIGHT));
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_WIDTH,
						flashDTO.getWidth());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_IMAGE_REFERENCE,
						flashDTO.getHtml5SmartImageDTO().getFileReference());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_IMAGE_HEIGHT,
						flashDTO.getHtml5SmartImageDTO().getHeight());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_SIZE_LIMIT,
						flashDTO.getHtml5SmartImageDTO().getSize());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_WMODE,
						flashDTO.getWmode());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_BG_COLOR,
						flashDTO.getBgColor());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_FLASH_VERSION,
						flashDTO.getFlashVersion());
				flashNode.setProperty(
						AcceleratorComponentConstants.FLASHDTO_ATTRS,
						flashDTO.getAttrs());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Profile node based on the provided component
	 * and its properties present in the ProfileDTO
	 * 
	 * @param profileDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createProfileNode(ProfileDTO profileDTO)
			throws AcceleratorException {
		String methodName = "createProfileNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					profileDTO.getParentNodePath(), jcrSession);
			String nodeName = profileDTO.getNodeName();
			Node profileNode = null;

			if (!verifyUpdate(profileDTO) && null != parentNode) {
				profileNode = parentNode.addNode(nodeName);
				profileNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				profileNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.PROFILEDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					profileNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != profileNode) {
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_JCR_TITLE,
						profileDTO.getJcr_title());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_DELETE_TITLE,
						profileDTO.getDeleteTitle());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_COLS,
						profileDTO.getCols());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_WIDTH,
						profileDTO.getWidth());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_CONSTRAINT_MESSAGE,
								profileDTO.getConstraintMessage());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_CONSTRAINT_TYPE,
								profileDTO.getConstraintType());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_REQUIRED,
						profileDTO.getRequired());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_REQUIRED_MESSAGE,
								profileDTO.getRequiredMessage());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_JCR_DESCRIPTION,
								profileDTO.getJcr_description());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_ROWS,
						profileDTO.getSizeFieldDTO().getRows());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_DEFAULT_VALUE,
						profileDTO.getDefaultValue());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_HONORIC_PREFIX_TITLE,
								profileDTO.getHonoricPrefixTitle());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_TITLE,
						profileDTO.getTitle());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_GIVEN_NAME_TITLE,
								profileDTO.getGivenNameTitle());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_MIDDLE_NAME_TITLE,
								profileDTO.getMiddleNameTitle());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_FAMILY_NAME_TITLE,
								profileDTO.getFamilyNameTitle());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_HONORIC_SUFFIX_TITLE,
								profileDTO.getHonoricSuffixTitle());
				profileNode.setProperty(
						AcceleratorComponentConstants.PROFILEDTO_NAME,
						profileDTO.getName());
				profileNode
						.setProperty(
								AcceleratorComponentConstants.PROFILEDTO_CONFIRMATION_TITLE,
								profileDTO.getConfirmationTitle());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Search node based on the provided component and
	 * its properties present in the SearchDTO
	 * 
	 * @param searchDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createSearchNode(SearchDTO searchDTO)
			throws AcceleratorException {
		String methodName = "createSearchNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					searchDTO.getParentNodePath(), jcrSession);
			String nodeName = searchDTO.getNodeName();
			Node searchNode = null;

			if (!verifyUpdate(searchDTO) && null != parentNode) {
				searchNode = parentNode.addNode(nodeName);
				searchNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				searchNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.SEARCHDTO_COMPONENT_PATH);
				searchNode.setProperty(
						AcceleratorComponentConstants.SEARCHDTO_SEARCHIN,
						searchDTO.getSearchIn());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SEARCH_BUTTON_TEXT,
								searchDTO.getSearchButtonText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_STATISTICS_TEXT,
								searchDTO.getStatisticsText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_NO_RESULTS_TEXT,
								searchDTO.getNoResultsText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SPELL_CHECK_TEXT,
								searchDTO.getSpellcheckText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SIMILAR_PAGES_TEXT,
								searchDTO.getSimilarPagesText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_RELATED_SEARCHES_TEXT,
								searchDTO.getRelatedSearchesText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SEARCH_TRENDS_TEXT,
								searchDTO.getSearchTrendsText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_RESULT_PAGES_TEXT,
								searchDTO.getResultPagesText());
				searchNode.setProperty(
						AcceleratorComponentConstants.SEARCHDTO_PREVIOUS_TEXT,
						searchDTO.getPreviousText());
				searchNode.setProperty(
						AcceleratorComponentConstants.SEARCHDTO_NEXT_TEXT,
						searchDTO.getNextText());
				creationFlag = true;

			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					searchNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != searchNode) {
				searchNode.setProperty(
						AcceleratorComponentConstants.SEARCHDTO_SEARCHIN,
						searchDTO.getSearchIn());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SEARCH_BUTTON_TEXT,
								searchDTO.getSearchButtonText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_STATISTICS_TEXT,
								searchDTO.getStatisticsText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_NO_RESULTS_TEXT,
								searchDTO.getNoResultsText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SPELL_CHECK_TEXT,
								searchDTO.getSpellcheckText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SIMILAR_PAGES_TEXT,
								searchDTO.getSimilarPagesText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_RELATED_SEARCHES_TEXT,
								searchDTO.getRelatedSearchesText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_SEARCH_TRENDS_TEXT,
								searchDTO.getSearchTrendsText());
				searchNode
						.setProperty(
								AcceleratorComponentConstants.SEARCHDTO_RESULT_PAGES_TEXT,
								searchDTO.getResultPagesText());
				searchNode.setProperty(
						AcceleratorComponentConstants.SEARCHDTO_PREVIOUS_TEXT,
						searchDTO.getPreviousText());
				searchNode.setProperty(
						AcceleratorComponentConstants.SEARCHDTO_NEXT_TEXT,
						searchDTO.getNextText());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a TextImage node based on the provided component
	 * and its properties present in the TextImageDTO
	 * 
	 * @param textImageDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createTextImageNode(TextImageDTO textImageDTO)
			throws AcceleratorException {
		String methodName = "createTextImageNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					textImageDTO.getParentNodePath(), jcrSession);
			String nodeName = textImageDTO.getNodeName();
			Node textImageNode = null;
			Node imageNode = null;
			if (!verifyUpdate(textImageDTO) && null != parentNode) {
				textImageNode = parentNode.addNode(nodeName);
				textImageNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				textImageNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.TEXTIMAGEDTO_COMPONENT_PATH);
				imageNode = textImageNode.addNode("image");
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					textImageNode = parentNode.getNode(nodeName);
				}
				if (textImageNode.hasNode("image")) {
					imageNode = textImageNode.getNode("image");
				} else {
					imageNode = textImageNode.addNode("image");
				}
				creationFlag = true;
			}
			if (null != textImageNode) {
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_TEXT,
						textImageDTO.getTextDTO().getText());
				imageNode
						.setProperty(
								AcceleratorComponentConstants.TEXTIMAGEDTO_FILE_REFERENCE,
								textImageDTO.getImageDTO().getFileReference());
				imageNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.IMAGEDTO_COMPONENT_PATH);
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_TITLE,
						textImageDTO.getImageDTO().getJcr_title());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_ALT,
						textImageDTO.getImageDTO().getAlt());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_LINK_URL,
						textImageDTO.getImageDTO().getLinkURL());
				textImageNode
						.setProperty(
								AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_DESCRIPTION,
								textImageDTO.getImageDTO().getJcr_description());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_HEIGHT,
						textImageDTO.getImageDTO().getHeight());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_WIDTH,
						textImageDTO.getImageDTO().getWidth());
				textImageNode.setProperty(AcceleratorGenericConstants.TEXT_IS_RICH,
						AcceleratorGenericConstants.TRUE);
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Video node based on the provided component and
	 * its properties present in the VideoDTO
	 * 
	 * @param videoDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createVideoNode(VideoDTO videoDTO)
			throws AcceleratorException {
		String methodName = "createVideoNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					videoDTO.getParentNodePath(), jcrSession);
			String nodeName = videoDTO.getNodeName();
			Node videoNode = null;
			if (!verifyUpdate(videoDTO) && null != parentNode) {
				videoNode = parentNode.addNode(nodeName);
				videoNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				videoNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.VIDEODTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					videoNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != videoNode) {
				videoNode.setProperty(
						AcceleratorComponentConstants.VIDEODTO_FILE,
						videoDTO.getFile());
				videoNode.setProperty(
						AcceleratorComponentConstants.VIDEODTO_HEIGHT,
						videoDTO.getHeight());
				videoNode.setProperty(
						AcceleratorComponentConstants.VIDEODTO_WIDTH,
						videoDTO.getWidth());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error(
					"An Exception has occured in AcceleratorCRXUtils in createVideoNode()",
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					"createVideoNode()", e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Archive node based on the provided component
	 * and its properties present in the ArchiveDTO
	 * 
	 * @param archiveDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 */
	public static final boolean createArchiveNode(ArchiveDTO archiveDTO)
			throws AcceleratorException {
		String methodName = "createArchiveNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					archiveDTO.getParentNodePath(), jcrSession);
			String nodeName = archiveDTO.getNodeName();
			Node archiveNode = null;
			if (!verifyUpdate(archiveDTO) && null != parentNode) {
				archiveNode = parentNode.addNode(nodeName);
				archiveNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				archiveNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.ARCHIVEDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					archiveNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != archiveNode) {
				archiveNode.setProperty(
						AcceleratorComponentConstants.VIDEODTO_FILE,
						archiveDTO.getDateFormat());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Chart node based on the provided component and
	 * its properties present in the ChartDTO
	 * 
	 * @param chartDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 */
	public static final boolean createChartNode(ChartDTO chartDTO)
			throws AcceleratorException {
		String methodName = "createChartNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		try {
			Node parentNode = jcrSession.getNode(chartDTO.getParentNodePath());
			String nodeName = chartDTO.getNodeName();
			Node chartNode = null;
			if (!verifyUpdate(chartDTO) && null != parentNode) {
				chartNode = parentNode.addNode(nodeName);
				chartNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				chartNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.CHARTDTO_COMPONENT_PATH);
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					chartNode = parentNode.getNode(nodeName);
				}
			}
			if (null != chartNode) {
				chartNode.setProperty(
						AcceleratorComponentConstants.CHARTDTO_CHART_DATA,
						chartDTO.getChartData());
				chartNode.setProperty(
						AcceleratorComponentConstants.CHARTDTO_CHART_TYPE,
						chartDTO.getChartType());
				chartNode.setProperty(
						AcceleratorComponentConstants.CHARTDTO_CHART_WIDTH,
						chartDTO.getChartWidth());
				chartNode.setProperty(
						AcceleratorComponentConstants.CHARTDTO_CHART_ALT,
						chartDTO.getChartAlt());
				chartNode.setProperty(
						AcceleratorComponentConstants.CHARTDTO_CHART_HEIGHT,
						chartDTO.getChartHeight());
			}
			jcrSession.save();
			LOGGER.info(" || " + methodName + " || END");
			return true;

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
	}

	/**
	 * @author : Srikanth This method will create a single Breadcrumb node based
	 *         on the provided properties present in the DTO. If updateFlag
	 *         false then it is for creation otherwise update.
	 * 
	 * @param Breadcrumb
	 * 
	 * @return true if successful
	 */

	public static final boolean createBreadcrumbNode(BreadcrumbDTO breadcrumbDTO)
			throws AcceleratorException {
		String methodName = "createBreadcrumbNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					breadcrumbDTO.getParentNodePath(), jcrSession);

			String nodeName = breadcrumbDTO.getNodeName();
			Node breadcrumbNode = null;
			// redundancy check
			if (!verifyUpdate(breadcrumbDTO) && null != parentNode) {

				// creating a node under the path
				breadcrumbNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				breadcrumbNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				breadcrumbNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.BREADCRUMBDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				// accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					breadcrumbNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != breadcrumbNode) {
				// Setting the properties for the Node
				breadcrumbNode.setProperty(
						AcceleratorComponentConstants.BREADCRUMBDTO_ABS_PARENT,
						breadcrumbDTO.getAbsParent());
				breadcrumbNode.setProperty(
						AcceleratorComponentConstants.BREADCRUMBDTO_REL_PARENT,
						breadcrumbDTO.getRelParent());
				breadcrumbNode.setProperty(
						AcceleratorComponentConstants.BREADCRUMBDTO_DELIM,
						breadcrumbDTO.getDelim());
				breadcrumbNode.setProperty(
						AcceleratorComponentConstants.BREADCRUMBDTO_TRAIL,
						breadcrumbDTO.getTrail());
			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a design title node based on the provided
	 * component and its properties present in the DesignTitleDTO
	 * 
	 * @param titleDTO
	 * @return boolean
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 */

	public static final boolean createDesignTitleNode(DesignTitleDTO titleDTO)
			throws AcceleratorException {
		String methodName = "createDesignTitleNode";
		LOGGER.info(" || " + methodName + " || START");

		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					titleDTO.getParentNodePath(), jcrSession);
			String nodeName = titleDTO.getNodeName();
			Node titleNode = null;
			/* redundancy check */
			if (!verifyUpdate(titleDTO) && null != parentNode) {
				/* creating the titleNode under the path */
				titleNode = parentNode.addNode(nodeName);
				/* setting properties for the titleNode */
				titleNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				titleNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.DESIGNTITLE_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* creating the titleNode under the path */
				if (parentNode.hasNode(nodeName)) {
					titleNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != titleNode) {
				/* setting properties for the titleNode */
				titleNode.setProperty(
						AcceleratorComponentConstants.DEFAULT_TYPE,
						titleDTO.getDefaultType());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Chart node based on the provided component and
	 * its properties present in the DesignTitleDTO
	 * 
	 * @param toolbarDTO
	 * @return boolean
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 */

	public static final boolean createToolbarNode(ToolbarDTO toolbarDTO)
			throws AcceleratorException {
		String methodName = "createToolbarNode";
		LOGGER.info(" || " + methodName + " || START");

		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					toolbarDTO.getParentNodePath(), jcrSession);
			String nodeName = toolbarDTO.getNodeName();
			Node toolbarNode = null;
			/* redundancy check */
			if (!verifyUpdate(toolbarDTO) && null != parentNode) {
				/* creating the titleNode under the path */
				toolbarNode = parentNode.addNode(nodeName);
				/* setting properties for the titleNode */
				toolbarNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				toolbarNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.TOOLBAR_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* creating the titleNode under the path */
				if (parentNode.hasNode(nodeName)) {
					toolbarNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != toolbarNode) {
				/* setting properties for the titleNode */
				toolbarNode.setProperty(
						AcceleratorComponentConstants.ABS_PARENT,
						toolbarDTO.getAbsParent());
				toolbarNode.setProperty(AcceleratorComponentConstants.TOOLBAR,
						toolbarDTO.getToolbar());
				toolbarNode.setProperty(AcceleratorComponentConstants.LIST,
						toolbarDTO.getList());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Chart node based on the provided component and
	 * its properties present in the DesignTitleDTO
	 * 
	 * @param topnavDTO
	 * @return boolean
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 */

	public static final boolean createTopnavNode(TopnavDTO topnavDTO)
			throws AcceleratorException {
		String methodName = "createToolbarNode";
		LOGGER.info(" || " + methodName + " || START");

		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					topnavDTO.getParentNodePath(), jcrSession);
			String nodeName = topnavDTO.getNodeName();
			Node topnavNode = null;
			/* redundancy check */
			if (!verifyUpdate(topnavDTO) && null != parentNode) {
				/* creating the titleNode under the path */
				topnavNode = parentNode.addNode(nodeName);
				/* setting properties for the titleNode */
				topnavNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				topnavNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.TOPNAV_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* creating the titleNode under the path */
				if (parentNode.hasNode(nodeName)) {
					topnavNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != topnavNode) {
				/* setting properties for the titleNode */
				topnavNode.setProperty(
						AcceleratorComponentConstants.TOPNAV_ABS_PARENT,
						topnavDTO.getAbsParent());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Chart node based on the provided component and
	 * its properties present in the DesignTitleDTO
	 * 
	 * @param userInfoDTO
	 * @return boolean
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 * @author Praveen Hegde
	 * @throws AcceleratorException
	 */

	public static final boolean createUserInfoNode(UserInfoDTO userInfoDTO)
			throws AcceleratorException {
		String methodName = "createToolbarNode";
		LOGGER.info(" || " + methodName + " || START");

		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					userInfoDTO.getParentNodePath(), jcrSession);
			String nodeName = userInfoDTO.getNodeName();
			Node userinfoNode = null;
			/* redundancy check */
			if (!verifyUpdate(userInfoDTO) && null != parentNode) {
				/* creating the titleNode under the path */
				userinfoNode = parentNode.addNode(nodeName);
				/* setting properties for the titleNode */
				userinfoNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				userinfoNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.USERINFO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* creating the titleNode under the path */
				if (parentNode.hasNode(nodeName)) {
					userinfoNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != userinfoNode) {
				/* setting properties for the titleNode */
				userinfoNode.setProperty(
						AcceleratorComponentConstants.USERINFO_LOGIN_PAGE,
						userInfoDTO.getLoginPage());
				userinfoNode.setProperty(
						AcceleratorComponentConstants.USERINFO_PROFILE_PAGE,
						userInfoDTO.getProfilePage());
				userinfoNode.setProperty(
						AcceleratorComponentConstants.USERINFO_SIGNUP_PAGE,
						userInfoDTO.getSignupPage());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @author : Srikanth This method will create a single Carousel node based
	 *         on the provided properties present in the DTO. If updateFlag
	 *         false then it is for creation otherwise update.
	 * 
	 * @param Carousel
	 * 
	 * @return true if successful
	 */
	public static final boolean createCarouselNode(CarouselDTO carouselDTO)
			throws AcceleratorException {
		String methodName = "createCarouselNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					carouselDTO.getParentNodePath(), jcrSession);
			String nodeName = carouselDTO.getNodeName();
			Node carouselNode = null;
			// redundancy check
			if (!verifyUpdate(carouselDTO) && null != parentNode) {

				// creating a node under the path
				carouselNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				carouselNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				carouselNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.CAROUSELDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					carouselNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != carouselNode) {
				// Setting the properties for the Node
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_PLAY_SPEED,
						carouselDTO.getPlaySpeed());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_TRANS_TIME,
						carouselDTO.getTransTime());
				carouselNode
						.setProperty(
								AcceleratorComponentConstants.CAROUSELDTO_CONTROLS_TYPE,
								carouselDTO.getControlsType());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_LIST_FORM,
						carouselDTO.getListFrom());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_ORDER_BY,
						carouselDTO.getOrderBy());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_PARENT_PAGE,
						carouselDTO.getParentPage());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_LIMIT,
						carouselDTO.getLimit());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_PAGES,
						carouselDTO.getPages().split(
								AcceleratorGenericConstants.RECORD_SEPRARTOR));
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_SEARCH_IN,
						carouselDTO.getSearchIn());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_QUERY,
						carouselDTO.getQuery());
				carouselNode.setProperty(
						AcceleratorComponentConstants.CAROUSELDTO_SAVED_QUERY,
						carouselDTO.getQuery());
			}
			jcrSession.save();
		}

		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @author : Srikanth This method will create a single Download node based
	 *         on the provided properties present in the DTO. If updateFlag
	 *         false then it is for creation otherwise update.
	 * 
	 * @param Download
	 * 
	 * @return true if successful
	 */
	public static final boolean createDownloadNode(DownloadDTO downloadDTO)
			throws AcceleratorException {
		String methodName = "createDownloadNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					downloadDTO.getParentNodePath(), jcrSession);
			String nodeName = downloadDTO.getNodeName();
			Node downloadNode = null;
			// redundancy check
			if (!verifyUpdate(downloadDTO) && null != parentNode) {
				// creating a node under the path
				downloadNode = parentNode.addNode(nodeName);
				downloadNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				downloadNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.DOWNLOADDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					downloadNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != downloadNode) {
				// Setting the properties for the Node
				downloadNode
						.setProperty(
								AcceleratorComponentConstants.DOWNLOADDTO_JCR_DESCRIPTION,
								downloadDTO.getJcr_description());
				downloadNode.setProperty(
						AcceleratorComponentConstants.DOWNLOADDTO_FILE,
						downloadDTO.getFile());
				downloadNode.setProperty(
						AcceleratorComponentConstants.DOWNLOADDTO_FILE_NAME,
						downloadDTO.getFile());

				LOGGER.info("FileReference in crx utils "
						+ AcceleratorComponentConstants.DOWNLOADDTO_FILE_REFERNCE
						+ " " + downloadDTO.getFileReference());
				downloadNode
						.setProperty(
								AcceleratorComponentConstants.DOWNLOADDTO_FILE_REFERNCE,
								downloadDTO.getFileReference());
				downloadNode
						.setProperty(
								AcceleratorComponentConstants.DOWNLOADDTO_AUTO_UPLOAD_DELAY,
								downloadDTO.getAutoUploadDelay());
			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @author : Srikanth This method will create a single External node based
	 *         on the provided properties present in the DTO. If updateFlag
	 *         false then it is for creation otherwise update.
	 * 
	 * @param External
	 * 
	 * @return true if successful
	 */

	public static final boolean createExternalNode(ExternalDTO externalDTO)
			throws AcceleratorException {
		String methodName = "createExternalNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					externalDTO.getParentNodePath(), jcrSession);
			String nodeName = externalDTO.getNodeName();
			Node externalNode = null;
			// redundancy check
			if (!verifyUpdate(externalDTO) && null != parentNode) {
				// creating a node under the path
				externalNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				externalNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				externalNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.EXTERNALDTO_COMPONENT_PATH);
				creationFlag = true;

			} else if (null != parentNode) {
				// creating a node under the path
				if (parentNode.hasNode(nodeName)) {
					externalNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != externalNode) {
				// Setting the properties for the Node
				externalNode.setProperty(
						AcceleratorComponentConstants.EXTERNALDTO_TARGET,
						externalDTO.getTarget());
				externalNode.setProperty(
						AcceleratorComponentConstants.EXTERNALDTO_PASS_PARAMS,
						externalDTO.getPassparams());
				externalNode.setProperty(
						AcceleratorComponentConstants.EXTERNALDTO_WIDTH_HEIGHT,
						externalDTO.getWidthheight());
			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @author : Srikanth This method will create a single Image node based on
	 *         the provided properties present in the DTO. If updateFlag false
	 *         then it is for creation otherwise update.
	 * 
	 * @param Image
	 * 
	 * @return true if successful
	 */
	public static final boolean createImageNode(ImageDTO imageDTO)
			throws AcceleratorException {
		String methodName = "createImageNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					imageDTO.getParentNodePath(), jcrSession);
			String nodeName = imageDTO.getNodeName();
			Node imageNode = null;
			// redundancy check
			if (!verifyUpdate(imageDTO) && null != parentNode) {
				// creating a node under the path
				imageNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				imageNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				imageNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.IMAGEDTO_COMPONENT_PATH);
				creationFlag = true;

			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					imageNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != imageNode) {
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_FILE,
						imageDTO.getFile());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_IMAGE_CROP,
						imageDTO.getImageCrop());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_FILENAME,
						imageDTO.getFileName());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_FILE_REFERENCE,
						imageDTO.getFileReference());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_IMAGE_ROTATE,
						imageDTO.getImageRotate());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_JCR_TITLE,
						imageDTO.getJcr_title());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_ALT,
						imageDTO.getAlt());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGE_LINK_URL,
						imageDTO.getLinkURL());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGE_JCR_DESCRIPTION,
						imageDTO.getJcr_description());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_SIZE,
						imageDTO.getSize());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_HEIGHT,
						imageDTO.getHeight());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_WIDTH,
						imageDTO.getWidth());

			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @author : Srikanth This method will create a single ListChildren node
	 *         based on the provided properties present in the DTO. If
	 *         updateFlag false then it is for creation otherwise update.
	 * 
	 * @param ListChildren
	 * 
	 * @return true if successful
	 */
	public static final boolean createListChildrenNode(
			ListChildrenDTO listchildrenDTO) throws AcceleratorException {
		String methodName = "createListChildrenNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					listchildrenDTO.getParentNodePath(), jcrSession);
			String nodeName = listchildrenDTO.getNodeName();
			Node listchildrenNode = null;
			// redundancy check
			if (!verifyUpdate(listchildrenDTO) && null != parentNode) {
				// creating a node under the path
				listchildrenNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				listchildrenNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				listchildrenNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.LISTCHILDRENDTO_COMPONENT_PATH);
				creationFlag = true;

			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					listchildrenNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != listchildrenNode) {
				// Setting the properties for the Node
				listchildrenNode
						.setProperty(
								AcceleratorComponentConstants.LISTCHILDRENDTO_LIST_ROOT,
								listchildrenDTO.getListroot());
			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @author : Srikanth This method will create a single List node based on
	 *         the provided properties present in the DTO. If updateFlag false
	 *         then it is for creation otherwise update.
	 * 
	 * @param List
	 * 
	 * @return true if successful
	 */
	public static final boolean createListNode(ListDTO listDTO)
			throws AcceleratorException {
		String methodName = "createListNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {

			Node parentNode = JcrUtils.getNodeIfExists(
					listDTO.getParentNodePath(), jcrSession);
			String nodeName = listDTO.getNodeName();
			Node listNode = null;
			// redundancy check
			if (!verifyUpdate(listDTO) && null != parentNode) {
				// creating a node under the path
				listNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				listNode.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				listNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.LISTDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					listNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != listNode) {
				// Setting the properties for the Node
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_LIST_FORM,
						listDTO.getListFrom());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_DISPLAYAS,
						listDTO.getDisplayAs());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_ORDER_BY,
						listDTO.getOrderBy());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_LIMIT,
						listDTO.getLimit());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_FEED_ENABLED,
						listDTO.getFeedEnabled());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_PAGE_MAX,
						listDTO.getPageMax());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_PARENT_PAGE,
						listDTO.getParentPage());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_ANCESTOR_PAGE,
						listDTO.getAncestorPage());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_PAGES,
						listDTO.getPages().split(
								AcceleratorGenericConstants.RECORD_SEPRARTOR));
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_FIELD_CONFIG,
						listDTO.getFieldConfig());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_WIDTH,
						listDTO.getWidth());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_QUERY,
						listDTO.getQuery());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_SAVED_QUERY,
						listDTO.getSavedquery());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_TAGS_SEARCH_ROOT,
						listDTO.getTagsSearchRoot());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_TAGS,
						listDTO.getTags());
				listNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_TAGS_MATCH,
						listDTO.getTagsMatch());
			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());

		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * @author : Srikanth This method will create a single List node based on
	 *         the provided properties present in the DTO. If updateFlag false
	 *         then it is for creation otherwise update.
	 * 
	 * @param List
	 * 
	 * @return true if successful
	 */

	public static final boolean createEntrytextNode(EntryTextDTO entrytextDTO)
			throws AcceleratorException {
		String methodName = "createEntrytextNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {

			Node parentNode = JcrUtils.getNodeIfExists(
					entrytextDTO.getParentNodePath(), jcrSession);
			String nodeName = entrytextDTO.getNodeName();
			Node entrytextNode = null;
			// redundancy check
			if (!verifyUpdate(entrytextDTO) && null != parentNode) {
				// creating a node under the path
				entrytextNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				entrytextNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				entrytextNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.ENTRYTEXTDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					entrytextNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != entrytextNode) {
				// Setting the properties for the Node
				entrytextNode.setProperty(
						AcceleratorComponentConstants.LISTDTO_LIST_FORM,
						entrytextDTO.getText());
			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Redirect node based on the provided component
	 * and its properties present in the RedirectDTO
	 * 
	 * @author aditi
	 * @param redirectDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */

	public static final boolean createRedirectNode(RedirectDTO redirectDTO)
			throws AcceleratorException {
		String methodName = "createRedirectNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					redirectDTO.getParentNodePath(), jcrSession);
			String nodeName = redirectDTO.getNodeName();
			Node redirectNode = null;
			/* redundancy check */
			if (!verifyUpdate(redirectDTO) && null != parentNode) {
				/* creating the redirectNode under the path */
				redirectNode = parentNode.addNode(nodeName);
				/* setting properties for the redirectNode */
				redirectNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				redirectNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.REDIRECTDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* Accessing the redirectNode under the path */
				if (parentNode.hasNode(nodeName)) {
					redirectNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != redirectNode) {
				/* setting properties for the redirectNode */
				redirectNode
						.setProperty(
								AcceleratorComponentConstants.REDIRECTDTO_REDIRECT_TARGET,
								redirectDTO.getRedirectTarget());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Reference node based on the provided component
	 * and its properties present in the ReferenceDTO
	 * 
	 * @author aditi
	 * @param referenceDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createReferenceNode(ReferenceDTO referenceDTO)
			throws AcceleratorException {
		String methodName = "createReferenceNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					referenceDTO.getParentNodePath(), jcrSession);
			String nodeName = referenceDTO.getNodeName();
			Node referenceNode = null;
			/* redundancy check */
			if (!verifyUpdate(referenceDTO) && null != parentNode) {
				/* creating the referenceNode under the path */
				referenceNode = parentNode.addNode(nodeName);
				/* setting properties for the referenceNode */
				referenceNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				referenceNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.REFERENCEDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* Accessing the referenceNode under the path */
				if (parentNode.hasNode(nodeName)) {
					referenceNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != referenceNode) {
				/* setting properties for the referenceNode */
				referenceNode.setProperty(
						AcceleratorComponentConstants.REFERENCEDTO_PATH,
						referenceDTO.getPath());
			}
			jcrSession.save();

		}

		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;

	}

	/**
	 * This method will create a SiteMap node based on the provided component
	 * and its properties present in the SiteMapDTO
	 * 
	 * @author aditi
	 * @param sitemapDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */

	public static final boolean createSiteMapNode(SiteMapDTO sitemapDTO)
			throws AcceleratorException {
		String methodName = "createSiteMapNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					sitemapDTO.getParentNodePath(), jcrSession);
			String nodeName = sitemapDTO.getNodeName();
			Node sitemapNode = null;
			/* redundancy check */
			if (!verifyUpdate(sitemapDTO) && null != parentNode) {
				/* creating the sitemapNode under the path */
				sitemapNode = parentNode.addNode(nodeName);
				/* setting properties for the sitemapNode */
				sitemapNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				sitemapNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.SITEMAPDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* Accessing the sitemapNode under the path */
				if (parentNode.hasNode(nodeName)) {
					sitemapNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != sitemapNode) {
				/* setting properties for the sitemapNode */
				sitemapNode.setProperty(
						AcceleratorComponentConstants.SITEMAPDTO_ROOT_PATH,
						sitemapDTO.getRootPath());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a SlideShow node based on the provided component
	 * and its properties present in the SlideShowDTO
	 * 
	 * @author aditi
	 * @param slideshowDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createSlideShowNode(SlideShowDTO slideshowDTO)
			throws AcceleratorException {
		String methodName = "createSlideShowNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					slideshowDTO.getParentNodePath(), jcrSession);
			String nodeName = slideshowDTO.getNodeName();
			Node slideshowNode = null;
			/* redundancy check */
			if (!verifyUpdate(slideshowDTO) && null != parentNode) {
				/* creating the sitemapNode under the path */
				slideshowNode = parentNode.addNode(nodeName);
				/* setting properties for the sitemapNode */
				slideshowNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				slideshowNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.SLIDESHOW_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* Accessing the sitemapNode under the path */
				if (parentNode.hasNode(nodeName)) {
					slideshowNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != slideshowNode) {
				/* setting properties for the sitemapNode */
				slideshowNode
						.setProperty(
								AcceleratorComponentConstants.SLIDESHOWDTO_SLIDESHOW_HEIGHT,
								slideshowDTO.getSlideshowHeight());
				slideshowNode
						.setProperty(
								AcceleratorComponentConstants.SLIDESHOWDTO_SLIDESHOW_WIDTH,
								slideshowDTO.getSlideshowWidth());
			}
			jcrSession.save();

		}
		/* Exception handling */
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Table node based on the provided component and
	 * its properties present in the TableDTO
	 * 
	 * @author aditi
	 * @param tableDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */

	public static final boolean createTableNode(TableDTO tableDTO)
			throws AcceleratorException {
		String methodName = "createTableNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					tableDTO.getParentNodePath(), jcrSession);
			String nodeName = tableDTO.getNodeName();
			Node tableNode = null;
			/* redundancy check */
			if (!verifyUpdate(tableDTO) && null != parentNode) {
				/* creating the tableNode under the path */
				tableNode = parentNode.addNode(nodeName);
				/* setting properties for the tableNode */
				tableNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				tableNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.TABLE_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* accessing the tableNode under the path */
				if (parentNode.hasNode(nodeName)) {
					tableNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != tableNode) {
				/* setting properties for the tableNode */
				tableNode.setProperty(
						AcceleratorComponentConstants.TABLEDTO_TABLE_DATA,
						tableDTO.getTableData());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Text node based on the provided component and
	 * its properties present in the TextDTO
	 * 
	 * @author aditi
	 * @param textDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createTextNode(TextDTO textDTO)
			throws AcceleratorException {
		String methodName = "createTextNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					textDTO.getParentNodePath(), jcrSession);
			String nodeName = textDTO.getNodeName();
			Node textNode = null;
			/* redundancy check */
			if (!verifyUpdate(textDTO) && null != parentNode) {
				/* creating the textNode under the path */
				textNode = parentNode.addNode(nodeName);
				/* setting properties for the textNode */
				textNode.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				textNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.TEXT_COMPONENT_PATH);
				textNode.setProperty(AcceleratorGenericConstants.TEXT_IS_RICH,
						AcceleratorGenericConstants.TRUE);
				creationFlag = true;

			} else if (null != parentNode) {
				/* Accessing the textNode under the path */
				if (parentNode.hasNode(nodeName)) {
					textNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != textNode) {
				/* setting properties for the textNode */
				textNode.setProperty(
						AcceleratorComponentConstants.TEXTDTO_TEXT,
						textDTO.getText());
			}
			jcrSession.save();

		}
		/* Exception handling */
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());

		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Title node based on the provided component and
	 * its properties present in the TitleDTO
	 * 
	 * @author aditi
	 * @param titleDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createTitleNode(TitleDTO titleDTO)
			throws AcceleratorException {
		String methodName = "createTitleNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();

		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					titleDTO.getParentNodePath(), jcrSession);
			String nodeName = titleDTO.getNodeName();
			Node titleNode = null;
			/* redundancy check */
			if (!verifyUpdate(titleDTO) && null != parentNode) {
				/* creating the titleNode under the path */
				titleNode = parentNode.addNode(nodeName);
				/* setting properties for the titleNode */
				titleNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				titleNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.TITLE_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* creating the titleNode under the path */
				if (parentNode.hasNode(nodeName)) {
					titleNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != titleNode) {
				/* setting properties for the titleNode */
				titleNode.setProperty(
						AcceleratorComponentConstants.TITLEDTO_JCR_TITLE,
						titleDTO.getJcr_title());
				titleNode.setProperty(
						AcceleratorComponentConstants.TITLEDTO_TYPE,
						titleDTO.getType());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a EntryList node based on the provided component
	 * and its properties present in the SlideShowDTO
	 * 
	 * @author aditi
	 * @param entrylistDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createEntryListNode(EntryListDTO entrylistDTO)
			throws AcceleratorException {
		String methodName = "createEntryListNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();

		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					entrylistDTO.getParentNodePath(), jcrSession);
			String nodeName = entrylistDTO.getNodeName();
			Node entrylistNode = null;
			/* redundancy check */
			if (null != parentNode && !parentNode.hasNode(nodeName)) {
				/* creating the entrylistNode under the path */
				entrylistNode = parentNode.addNode(nodeName);
				/* setting properties for the entrylistNode */
				entrylistNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				entrylistNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.ENTRYLIST_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				/* Acessing the entrylistNode under the path */
				if (parentNode.hasNode(nodeName)) {
					entrylistNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != entrylistNode) {
				/* setting properties for the entrylistNode */
				entrylistNode.setProperty(
						AcceleratorComponentConstants.ENTRYLISTDTO_LIMIT,
						entrylistDTO.getLimit());
				entrylistNode.setProperty(
						AcceleratorComponentConstants.ENTRYLISTDTO_PAGE_MAX,
						entrylistDTO.getPageMax());
				entrylistNode.setProperty(
						AcceleratorComponentConstants.ENTRYLISTDTO_DATE_FORMAT,
						entrylistDTO.getDateFormat());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method creates a folder in CRX
	 * 
	 * @param path
	 * @return
	 * @throws AcceleratorException
	 */
	public static Node createFolder(String path) throws AcceleratorException {
		String methodName = "createFolder";
		LOGGER.info(CLASS_NAME + " || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		Node returnNode = null;
		try {
			String[] nodes = path
					.split(AcceleratorGenericConstants.FORWARD_SLASH);
			returnNode = jcrSession.getRootNode();
			for (String node : nodes) {
				if (null != node && !node.isEmpty()) {
					returnNode = returnNode.hasNode(node) ? returnNode
							.getNode(node) : returnNode.addNode(node,
							"nt:folder");
				}
			}
			jcrSession.save();
			LOGGER.debug(CLASS_NAME + " || " + methodName
					+ " || returnNode || " + returnNode);
		} catch (PathNotFoundException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ItemExistsException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ITEM_EXISTS_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (VersionException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.VERSION_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (ConstraintViolationException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.CONSTRAINT_VOILATION_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (LockException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.LOCK_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(CLASS_NAME + " || " + methodName + " || END");
		return returnNode;
	}

	/**
	 * This method is used to the read the existing CRX node and provide String
	 * output containing all the data
	 * 
	 * @param path
	 * @return
	 * @throws AcceleratorException
	 */
	public static String readCRXData(String path) throws AcceleratorException {
		String methodName = "readCRXData";
		LOGGER.info(CLASS_NAME + " || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		Node fileNode = null;
		try {

			fileNode = JcrUtils.getNodeIfExists(path, jcrSession);

			/*
			 * if (fileNode.hasNode(AcceleratorGenericConstants.JCR_CONTENT)) {
			 * contentNode = fileNode
			 * .getNode(AcceleratorGenericConstants.JCR_CONTENT); } return
			 * contentNode .getProperty(AcceleratorGenericConstants.JCR_DATA)
			 * .getString();
			 */
			if (fileNode != null) {
				StringBuilder stringBuilder = new StringBuilder();
				InputStream inputStream = JcrUtils.readFile(fileNode);
				int readInt;
				char readchars;
				while ((readInt = inputStream.read()) != -1) {
					readchars = (char) readInt;
					stringBuilder.append(readchars);
				}
				return stringBuilder.toString();
			}
		} catch (RepositoryException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (IOException e) {
			LOGGER.error("An exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(CLASS_NAME + " || " + methodName + " || END");
		return null;
	}

	public static final boolean createCustomImage(CustomImageDTO customImageDTO)
			throws AcceleratorException {
		String methodName = "createCustomImage";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					customImageDTO.getParentNodePath(), jcrSession);
			LOGGER.info(" || " + methodName + " || parentNode || "
					+ parentNode.getPath());
			String nodeName = customImageDTO.getNodeName();
			LOGGER.info(" || " + methodName + " || nodeName || " + nodeName);
			Node imageNode = null;
			// redundancy check
			if (!verifyUpdate(customImageDTO) && null != parentNode) {
				// creating a node under the path
				imageNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				imageNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				imageNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.CUSTOM_IMAGEDTO_COMPONENT_PATH);
				creationFlag = true;

			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					imageNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			LOGGER.info(" || " + methodName + " || imageNode || "
					+ imageNode.getPath());
			if (null != imageNode) {
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_FILE,
						customImageDTO.getFile());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_IMAGE_CROP,
						customImageDTO.getImageCrop());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_FILENAME,
						customImageDTO.getFileName());
				Map<String, String> propMaps = ReportGeneratorHelper
						.getPropertiesMap(AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
								+ AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES);
				imageNode
						.setProperty(
								AcceleratorComponentConstants.IMAGEDTO_ICON_FILE_REFERENCE,
								propMaps.get(customImageDTO.getIconReference()));
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_IMAGE_ROTATE,
						customImageDTO.getImageRotate());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_JCR_TITLE,
						customImageDTO.getJcr_title());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_ALT,
						customImageDTO.getAlt());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGE_LINK_URL,
						customImageDTO.getLinkURL());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGE_JCR_DESCRIPTION,
						customImageDTO.getJcr_description());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_SIZE,
						customImageDTO.getSize());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_HEIGHT,
						customImageDTO.getHeight());
				imageNode.setProperty(
						AcceleratorComponentConstants.IMAGEDTO_WIDTH,
						customImageDTO.getWidth());

				imageNode
						.setProperty(
								AcceleratorComponentConstants.CUSTOM_IMAGEDTO_LINK_TEXT,
								customImageDTO.getRedirectText());
				imageNode.setProperty(
						AcceleratorComponentConstants.CUSTOM_IMAGEDTO_LINK,
						customImageDTO.getRedirectedURL());

			}
			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	/**
	 * This method will create a Title node which redirect to link on click of
	 * title based on the provided component and its properties present in the
	 * TitleLinkDTO
	 * 
	 * @author
	 * @param titleDTO
	 *            ,nodePath,nodeName
	 * @return boolean
	 * @throws AcceleratorException
	 * @exception PathNotFoundException
	 *                and RepositoryException
	 */
	public static final boolean createTitleLinkNode(TitleLinkDTO titleLinkDTO)
			throws AcceleratorException {
		String methodName = "createTitleNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();

		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					titleLinkDTO.getParentNodePath(), jcrSession);
			String nodeName = titleLinkDTO.getNodeName();
			Node titleNode = null;
			/* redundancy check */
			if (!verifyUpdate(titleLinkDTO) && null != parentNode) {
				/* creating the titleNode under the path */
				titleNode = parentNode.addNode(nodeName);
				/* setting properties for the titleNode */
				titleNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				titleNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.TITLELINKDTO_COMPONENT);

				creationFlag = true;
			} else if (null != parentNode) {
				/* creating the titleNode under the path */
				if (parentNode.hasNode(nodeName)) {
					titleNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}
			if (null != titleNode) {
				/* setting properties for the titleNode */
				titleNode.setProperty(
						AcceleratorComponentConstants.TITLEDTO_JCR_TITLE,
						titleLinkDTO.getTitle());
				titleNode.setProperty(
						AcceleratorComponentConstants.TITLEDTO_TYPE,
						titleLinkDTO.getType());
				titleNode.setProperty(AcceleratorComponentConstants.LINK,
						titleLinkDTO.getLink());

			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	public static final boolean createCustomCarouselNode(
			CustomCarouselDTO customCarouselDTO) throws AcceleratorException {
		String methodName = "createCustomCarouselNode";
		LOGGER.info(" || " + methodName + " || START");

		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;

		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					customCarouselDTO.getParentNodePath(), jcrSession);
			String nodeName = customCarouselDTO.getNodeName();
			Node customCarouselNode = null;
			Node carouselValuesNode = null;
			Node imagesNode = null;
			Node newItemNode = null;
			Node newImageNode = null;
			int count = 1;

			// redundancy check
			if (!verifyUpdate(customCarouselDTO) && null != parentNode) {
				// creating a node under the path
				customCarouselNode = parentNode.addNode(nodeName);
				carouselValuesNode = customCarouselNode
						.addNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_VALUES_NODE);
				imagesNode = customCarouselNode
						.addNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_IMAGE_NODE);

				// Setting the properties for the Node
				customCarouselNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				customCarouselNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					customCarouselNode = parentNode.getNode(nodeName);
				}
				if (customCarouselNode
						.hasNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_VALUES_NODE)) {
					carouselValuesNode = customCarouselNode
							.getNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_VALUES_NODE);
				} else {
					carouselValuesNode = customCarouselNode
							.addNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_VALUES_NODE);
				}
				if (customCarouselNode
						.hasNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_IMAGE_NODE)) {
					imagesNode = customCarouselNode
							.getNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_IMAGE_NODE);
				} else {
					imagesNode = customCarouselNode
							.addNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_IMAGE_NODE);
				}
				creationFlag = true;
			}
			if (null != customCarouselNode) {

				// Setting the properties for the Node
				for (CustomCarouselItemDTO itemDto : customCarouselDTO
						.getItemDto()) {
					if (carouselValuesNode
							.hasNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NEW_ITEM_NODE
									+ count)) {
						newItemNode = carouselValuesNode
								.getNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NEW_ITEM_NODE
										+ count);
					} else {
						newItemNode = carouselValuesNode
								.addNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NEW_ITEM_NODE
										+ count);
					}
					if (imagesNode
							.hasNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NEW_IMAGE_NODE
									+ count)) {
						newImageNode = imagesNode
								.getNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NEW_IMAGE_NODE
										+ count);
					} else {
						newImageNode = imagesNode
								.addNode(AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NEW_IMAGE_NODE
										+ count);
					}

					newItemNode
							.setProperty(
									AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_TITLE,
									itemDto.getTitle());
					newItemNode
							.setProperty(
									AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_DESC,
									itemDto.getTitleDesc());
					newItemNode
							.setProperty(
									AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NAV_TITLE,
									itemDto.getNavTitle());
					newItemNode
							.setProperty(
									AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_NAV_DESC,
									itemDto.getNavDesc());
					newItemNode
							.setProperty(
									AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_LINK,
									itemDto.getLink());
					Map<String, String> propMaps = ReportGeneratorHelper
							.getPropertiesMap(AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
									+ AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES);
					newImageNode
							.setProperty(
									AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_IMAGE_REF,
									propMaps.get(itemDto.getImagePath()));

					count++;
				}

			}
			jcrSession.save();
		}

		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}

		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	public static final boolean createImageMultiFieldNode(
			ImageMultiFieldDTO imageMultiFieldDTO) throws AcceleratorException {
		String methodName = "createImageMultiFieldNode";
		LOGGER.info(" || " + methodName + " || START");

		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;

		try {

			Node parentNode = JcrUtils.getNodeIfExists(
					imageMultiFieldDTO.getParentNodePath(), jcrSession);
			String nodeName = imageMultiFieldDTO.getNodeName();
			String imageLayout = imageMultiFieldDTO.getImageLayout();
			Node imageMultiFieldNode = null;
			Node imageNode = null;
			Node titleImageNode = null;
			int count = 1;

			// redundancy check
			if (!verifyUpdate(imageMultiFieldDTO) && null != parentNode) {
				// creating a node under the path
				imageMultiFieldNode = parentNode.addNode(nodeName);

				// Setting the properties for the Node
				imageMultiFieldNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				imageMultiFieldNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.IMAGE_MULTIFIELD_COMPONENT_PATH);

			} else if (null != parentNode) {
				// Accessing a node under the path
				if (parentNode.hasNode(nodeName)) {
					imageMultiFieldNode = parentNode.getNode(nodeName);
				}
			}

			if (imageMultiFieldNode
					.hasNode(AcceleratorComponentConstants.IMAGE_MULTIFIELD_IMAGE_NODE)) {
				imageNode = imageMultiFieldNode
						.getNode(AcceleratorComponentConstants.IMAGE_MULTIFIELD_IMAGE_NODE);
			} else {
				imageNode = imageMultiFieldNode
						.addNode(AcceleratorComponentConstants.IMAGE_MULTIFIELD_IMAGE_NODE);
			}

			creationFlag = true;

			if (null != imageMultiFieldNode && null != imageNode) {

				// Setting the properties for the Node
				imageMultiFieldNode
						.setProperty(
								AcceleratorComponentConstants.IMAGE_MULTIFIELD_IMAGE_LAYOUT,
								imageLayout);
				imageMultiFieldNode.setProperty("linktext", imageMultiFieldDTO
						.getCustomImageDTO().getRedirectText());
				Map<String, String> propMaps = ReportGeneratorHelper
						.getPropertiesMap(AcceleratorGenericConstants.REPORT_PROPERTY_FILE_PATH
								+ AcceleratorGenericConstants.DAM_FILES_MAPPING_PROPERTIES);
				imageMultiFieldNode.setProperty("linkurl", imageMultiFieldDTO
						.getCustomImageDTO().getRedirectedURL());
				imageMultiFieldNode.setProperty("iconReference", propMaps
						.get(imageMultiFieldDTO.getCustomImageDTO()
								.getIconReference()));
				imageMultiFieldNode.setProperty("alt", imageMultiFieldDTO
						.getCustomImageDTO().getAlt());

				for (ImageDTO imageDTO : imageMultiFieldDTO.getImageDTO()) {
					imageMultiFieldNode
							.setProperty(
									AcceleratorComponentConstants.IMAGE_MULTIFIELD_IMAGE_LAYOUT,
									imageLayout);

					imageDTO.setParentNodePath(imageNode.getPath());
					imageDTO.setNodeName(AcceleratorComponentConstants.IMAGE_MULTIFIELD_INDI_IMAGE_NODE
							+ count);
					count++;
					createImageNode(imageDTO);
				}
				String order = AcceleratorGenericConstants.OPEN_SQUARE_BRAC;
				for (int i = 1; i < count; i++) {
					order = order
							+ AcceleratorGenericConstants.QUOTE
							+ AcceleratorComponentConstants.IMAGE_MULTIFIELD_INDI_IMAGE_NODE
							+ i
							+ AcceleratorGenericConstants.QUOTE
							+ (i != count - 1 ? AcceleratorGenericConstants.CSV_SEPARATOR
									: "");
				}
				order = order + AcceleratorGenericConstants.CLOSE_SQUARE_BRAC;
				imageNode
						.setProperty(
								AcceleratorComponentConstants.IMAGE_MULTIFIELD_ORDER_PROP,
								order);

			}
			jcrSession.save();

		} catch (ValueFormatException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.VALUE_FORMAT_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (VersionException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.VERSION_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (LockException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(AcceleratorFaultCode.LOCK_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (ConstraintViolationException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.CONSTRAINT_VOILATION_EXCEPTION,
					CLASS_NAME, methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}

		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	public static final boolean createImageTitleNode(TitleImageDTO textImageDTO)
			throws AcceleratorException {
		String methodName = "createTextImageNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					textImageDTO.getParentNodePath(), jcrSession);
			String nodeName = textImageDTO.getNodeName();
			Node textImageNode = null;
			Node imageNode = null;
			if (!verifyUpdate(textImageDTO) && null != parentNode) {
				textImageNode = parentNode.addNode(nodeName);
				textImageNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				textImageNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.TITLE_IMAGE_COMPONENT_PATH);
				imageNode = textImageNode.addNode("image");
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					textImageNode = parentNode.getNode(nodeName);
				}
				if (textImageNode.hasNode("image")) {
					imageNode = textImageNode.getNode("image");
				} else {
					imageNode = textImageNode.addNode("image");
				}
				creationFlag = true;
			}
			if (null != textImageNode) {
				textImageNode.setProperty(
						AcceleratorComponentConstants.TITLE_IMAGE_TEXT,
						textImageDTO.getTitle());
				imageNode
						.setProperty(
								AcceleratorComponentConstants.TEXTIMAGEDTO_FILE_REFERENCE,
								textImageDTO.getImageDTO().getFileReference());
				imageNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.IMAGEDTO_COMPONENT_PATH);
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_TITLE,
						textImageDTO.getImageDTO().getJcr_title());
				imageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_ALT,
						textImageDTO.getImageDTO().getAlt());
				imageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_LINK_URL,
						textImageDTO.getImageDTO().getLinkURL());
				textImageNode
						.setProperty(
								AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_DESCRIPTION,
								textImageDTO.getImageDTO().getJcr_description());
				imageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_HEIGHT,
						textImageDTO.getImageDTO().getHeight());
				imageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_WIDTH,
						textImageDTO.getImageDTO().getWidth());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	public static final boolean createImageTitleLinkNode(
			CustomTitleImageDTO titlelinkImageDTO) throws AcceleratorException {
		String methodName = "createImageTitleLinkNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					titlelinkImageDTO.getParentNodePath(), jcrSession);
			String nodeName = titlelinkImageDTO.getNodeName();
			Node customTitleImageNode = null;
			Node imageNode = null;
			Node titleLinkNode = null;
			Node textImageNode = null;
			Node iconNode = null;
			if (!verifyUpdate(titlelinkImageDTO) && null != parentNode) {
				customTitleImageNode = parentNode.addNode(nodeName);
				LOGGER.info(" || " + methodName
						+ " || customTitleImageNode || "
						+ customTitleImageNode.getPath());
				customTitleImageNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				customTitleImageNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.TITLE_IMAGE_LINK_COMPONENT_PATH);

				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					customTitleImageNode = parentNode.getNode(nodeName);
				}
			}
			if (customTitleImageNode.hasNode("image")) {
				imageNode = customTitleImageNode.getNode("image");
			} else {
				imageNode = customTitleImageNode.addNode("image");
			}

			if (customTitleImageNode.hasNode("titleLink")) {
				titleLinkNode = customTitleImageNode.getNode("titleLink");
			} else {
				titleLinkNode = customTitleImageNode.addNode("titleLink");
				titleLinkNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				titleLinkNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.TITLELINKDTO_COMPONENT);
			}

			if (customTitleImageNode.hasNode("textImage")) {
				textImageNode = customTitleImageNode.getNode("textImage");
			} else {
				textImageNode = customTitleImageNode.addNode("textImage");
			}

			if (null != textImageNode) {
				LOGGER.info(" || " + methodName + " || textImageNode || "
						+ textImageNode.getPath());
				if (textImageNode.hasNode("image")) {
					iconNode = textImageNode.getNode("image");
				} else {
					iconNode = textImageNode.addNode("image");
				}
				LOGGER.info(" || " + methodName + " || iconNode || "
						+ iconNode.getPath());
			}
			creationFlag = true;
			if (null != customTitleImageNode) {

				customTitleImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_TEXT,
						titlelinkImageDTO.getTextImageDTO().getTextDTO()
								.getText());
				customTitleImageNode.setProperty(
						AcceleratorGenericConstants.TEXT_IS_RICH,
						AcceleratorGenericConstants.TRUE);
				if (null != imageNode) {

					imageNode
							.setProperty(
									AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_TITLE,
									titlelinkImageDTO.getTextImageDTO()
											.getImageDTO().getJcr_title());

					imageNode
							.setProperty(
									AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
									AcceleratorComponentConstants.IMAGEDTO_COMPONENT_PATH);

					imageNode
							.setProperty(
									AcceleratorComponentConstants.TEXTIMAGEDTO_FILE_REFERENCE,
									titlelinkImageDTO.getTextImageDTO()
											.getImageDTO().getFileReference());

					imageNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_ALT,
							titlelinkImageDTO.getTextImageDTO().getImageDTO()
									.getAlt());

					imageNode
							.setProperty(
									AcceleratorComponentConstants.TEXTIMAGEDTO_LINK_URL,
									titlelinkImageDTO.getTextImageDTO()
											.getImageDTO().getLinkURL());

					imageNode
							.setProperty(
									AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_DESCRIPTION,
									titlelinkImageDTO.getTextImageDTO()
											.getImageDTO().getJcr_description());

					imageNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_HEIGHT,
							titlelinkImageDTO.getTextImageDTO().getImageDTO()
									.getHeight());

					imageNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_WIDTH,
							titlelinkImageDTO.getTextImageDTO().getImageDTO()
									.getWidth());

				}
				if (null != titleLinkNode) {

					titleLinkNode.setProperty(
							AcceleratorComponentConstants.TITLEDTO_JCR_TITLE,
							titlelinkImageDTO.getTitleLinkDTO().getTitle());
					titleLinkNode.setProperty(
							AcceleratorComponentConstants.TITLEDTO_TYPE,
							titlelinkImageDTO.getTitleLinkDTO().getType());
					titleLinkNode.setProperty(
							AcceleratorComponentConstants.LINK,
							titlelinkImageDTO.getTitleLinkDTO().getLink());

				}
				if (null != textImageNode) {
					textImageNode
							.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
					textImageNode
							.setProperty(
									AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
									AcceleratorComponentConstants.TITLE_IMAGE_COMPONENT_PATH);

					textImageNode.setProperty(
							AcceleratorComponentConstants.TITLE_IMAGE_TEXT,
							titlelinkImageDTO.getTitleImageDTO().getTitle());

					iconNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_FILE_REFERENCE,
							titlelinkImageDTO.getTitleImageDTO().getImageDTO()
									.getFileReference());
					iconNode.setProperty(
							AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
							AcceleratorComponentConstants.IMAGEDTO_COMPONENT_PATH);
					iconNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_TITLE,
							titlelinkImageDTO.getTitleImageDTO().getImageDTO()
									.getJcr_title());
					iconNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_ALT,
							titlelinkImageDTO.getTitleImageDTO().getImageDTO()
									.getAlt());
					iconNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_LINK_URL,
							titlelinkImageDTO.getTitleImageDTO().getImageDTO()
									.getLinkURL());
					iconNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_DESCRIPTION,
							titlelinkImageDTO.getTitleImageDTO().getImageDTO()
									.getJcr_description());
					iconNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_HEIGHT,
							titlelinkImageDTO.getTitleImageDTO().getImageDTO()
									.getHeight());
					iconNode.setProperty(
							AcceleratorComponentConstants.TEXTIMAGEDTO_WIDTH,
							titlelinkImageDTO.getTitleImageDTO().getImageDTO()
									.getWidth());

				}
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	public static final boolean createListofLink(ListOfLinkDTO listOfLinkDTO)
			throws AcceleratorException {
		String methodName = "createListofLink";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {

			Node parentNode = JcrUtils.getNodeIfExists(
					listOfLinkDTO.getParentNodePath(), jcrSession);
			String nodeName = listOfLinkDTO.getNodeName();
			Node listoflinkNode = null;
			Node textWithLink = null;
			Node listItemNode = null;
			int count = 1;

			if (!verifyUpdate(listOfLinkDTO) && null != parentNode) {

				listoflinkNode = parentNode.addNode(nodeName);

				listoflinkNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				listoflinkNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.LIST_OF_LINK_DTO_COMPONENT_PATH);
				creationFlag = true;
			} else if (null != parentNode) {

				if (parentNode.hasNode(nodeName)) {
					listoflinkNode = parentNode.getNode(nodeName);
					creationFlag = true;
				}
			}

			if (null != listoflinkNode) {

				if (listoflinkNode.hasNode("textwithlink")) {
					textWithLink = listoflinkNode.getNode("textwithlink");
				} else {
					textWithLink = listoflinkNode.addNode("textwithlink");
					textWithLink
							.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				}
				creationFlag = true;
			}

			if (null != textWithLink) {

				listoflinkNode.setProperty(
						AcceleratorComponentConstants.TITLE_IMAGE_TEXT,
						listOfLinkDTO.getTitle());

				for (ListOfLinkItemDTO itemDto : listOfLinkDTO.getItemDto()) {

					if (textWithLink
							.hasNode(AcceleratorComponentConstants.LIST_OF_LINK_ITEM
									+ count)) {

						listItemNode = textWithLink
								.getNode(AcceleratorComponentConstants.LIST_OF_LINK_ITEM
										+ count);

					} else {
						listItemNode = textWithLink
								.addNode(AcceleratorComponentConstants.LIST_OF_LINK_ITEM
										+ count);
						listItemNode
								.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
					}

					listItemNode.setProperty(
							AcceleratorComponentConstants.TEXTDTO_TEXT,
							itemDto.getText());

					listItemNode
							.setProperty(
									AcceleratorComponentConstants.CUSTOM_CAROUSELDTO_LINK,
									itemDto.getLink());

					count++;
				}

			}

			jcrSession.save();

		}
		// Exception handling
		catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());

		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}

	public static final boolean createCustomTextImageNode(CustomTextImageDTO customTextImageDTO)
			throws AcceleratorException {
		String methodName = "createCustomTextImageNode";
		LOGGER.info(" || " + methodName + " || START");
		Session jcrSession = ConnectionManager.getSession();
		boolean creationFlag = false;
		try {
			Node parentNode = JcrUtils.getNodeIfExists(
					customTextImageDTO.getParentNodePath(), jcrSession);
			String nodeName = customTextImageDTO.getNodeName();
			Node textImageNode = null;
			Node imageNode = null;
			if (!verifyUpdate(customTextImageDTO) && null != parentNode) {
				textImageNode = parentNode.addNode(nodeName);
				textImageNode
						.setPrimaryType(AcceleratorGenericConstants.PRIMARY_TYPE_NT_UNSTRUCTURED);
				textImageNode
						.setProperty(
								AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
								AcceleratorComponentConstants.CUSTOM_TEXTIMAGEDTO_COMPONENT_PATH);
				imageNode = textImageNode.addNode("image");
				creationFlag = true;
			} else if (null != parentNode) {
				if (parentNode.hasNode(nodeName)) {
					textImageNode = parentNode.getNode(nodeName);
				}
				if (textImageNode.hasNode("image")) {
					imageNode = textImageNode.getNode("image");
				} else {
					imageNode = textImageNode.addNode("image");
				}
				creationFlag = true;
			}
			if (null != textImageNode) {
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_HEADING_TEXT,
						customTextImageDTO.getHeadingTextDTO().getText());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_MAIN_TEXT,
						customTextImageDTO.getMainTextDTO().getText());
				imageNode
						.setProperty(
								AcceleratorComponentConstants.TEXTIMAGEDTO_FILE_REFERENCE,
								customTextImageDTO.getImageDTO().getFileReference());
				imageNode.setProperty(
						AcceleratorGenericConstants.SLING_RESOURCE_TYPE,
						AcceleratorComponentConstants.IMAGEDTO_COMPONENT_PATH);
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_TITLE,
						customTextImageDTO.getImageDTO().getJcr_title());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_ALT,
						customTextImageDTO.getImageDTO().getAlt());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_LINK_URL,
						customTextImageDTO.getImageDTO().getLinkURL());
				textImageNode
						.setProperty(
								AcceleratorComponentConstants.TEXTIMAGEDTO_JCR_DESCRIPTION,
								customTextImageDTO.getImageDTO().getJcr_description());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_HEIGHT,
						customTextImageDTO.getImageDTO().getHeight());
				textImageNode.setProperty(
						AcceleratorComponentConstants.TEXTIMAGEDTO_WIDTH,
						customTextImageDTO.getImageDTO().getWidth());
			}
			jcrSession.save();

		} catch (PathNotFoundException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.PATH_NOT_FOUND_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return creationFlag;
	}
}
