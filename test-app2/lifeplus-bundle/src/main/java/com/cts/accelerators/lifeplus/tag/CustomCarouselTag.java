package com.cts.accelerators.lifeplus.tag;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cts.accelerators.core.AcceleratorComponentConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.lifeplus.core.LifePlusComponentConstants;
import com.cts.accelerators.lifeplus.customDTO.CarouselTagDTO;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

/**
 * This Tag Handler class has the functional logic and is used for setting the
 * values to the tag attributes of Custom Carousel tag for customComponents.tld
 * 
 * 
 * @author 406407
 * 
 */
public class CustomCarouselTag extends BodyTagSupport{

	String imageNodePath;
	String carouselValuesNodePath;
	private String carouselOutput;

	public String getImageNodePath() {
		return imageNodePath;
	}

	public void setImageNodePath(String imageNodePath) {
		this.imageNodePath = imageNodePath;
	}

	public String getCarouselValuesNodePath() {
		return carouselValuesNodePath;
	}

	public void setCarouselValuesNodePath(String carouselValuesNodePath) {
		this.carouselValuesNodePath = carouselValuesNodePath;
	}

	public String getCarouselList() {
		return carouselOutput;
	}

	public void setCarouselList(String a) {
		carouselOutput = a;
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomCarouselTag.class);
	Session jcrSession = null;

	/**
	 * This method will be used for setting the carouselList attribute of
	 * CustomCarouselTag for the custom tag library (customComponents.tld).
	 * 
	 * */
	public int doStartTag() throws JspException {


		List<CarouselTagDTO> tagDTOList = new ArrayList<CarouselTagDTO>();

		try {
			jcrSession = ConnectionManager.getSession();
		} catch (AcceleratorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}


		tagDTOList = updateImageReferences(tagDTOList);
		tagDTOList = updateText(tagDTOList);
		pageContext.setAttribute(carouselOutput, tagDTOList);

		return SKIP_BODY;

	}

	/**
	 * This method will be used for fetching the list of images from the Custom
	 * Carousel Component.
	 * 
	 * @param - List of CustomCarouselTagDTO objects
	 * 
	 * @return - an array containing the list of images
	 * */

	public List<CarouselTagDTO> updateImageReferences(
			List<CarouselTagDTO> tagDTOList) {
		String methodName = "updateImageReferences";
		// TODO Auto-generated method stub
		Node imagesNode = null;
		NodeIterator itemIterator = null;
		Node imageNode = null;
		CarouselTagDTO carouselTagDTO = new CarouselTagDTO();

		if (imageNodePath != null) {
			try {
				imagesNode = jcrSession.getNode(imageNodePath);
				if (imagesNode != null) {

					LOGGER.debug("********************imagesNode>>>>>>"
							+ imagesNode);
					itemIterator = imagesNode.getNodes();
					while (itemIterator.hasNext()) {
						
					carouselTagDTO = new CarouselTagDTO();
						imageNode = itemIterator.nextNode();

						carouselTagDTO
								.setImageReference(imageNode
										.hasProperty(LifePlusComponentConstants.CAROUSEL_IMAGE_REFERENCE) ? imageNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_IMAGE_REFERENCE)
										.getString()
										: null);
						LOGGER.debug("********************tagDTOList>>>>>>"
								+ imageNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_IMAGE_REFERENCE)
										.getString());

						tagDTOList.add(carouselTagDTO);
						LOGGER.debug("********************tagDTOList>>>>>>"
								+ tagDTOList);

					}
				}
			} catch (PathNotFoundException e) {
				LOGGER.error(" || " + methodName
						+ " || PATH NOT FOUND EXCEPTION || ", e);

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				LOGGER.error(" || " + methodName
						+ " || REPOSITORY EXCEPTION || ", e);
			}
		}
		return tagDTOList;
	}

	/**
	 * This method will be used for fetching the list of text details for the
	 * carousel from the Custom Carousel Component.
	 * 
	 * @param - List of CustomCarouselTagDTO objects
	 * 
	 * @return - an array containing the list of text details
	 * */
	private List<CarouselTagDTO> updateText(
			List<CarouselTagDTO> textCarouselList) {
		// TODO Auto-generated method stub
		String methodName = "updateText";
		List<CarouselTagDTO> updatedTagDTOList = new ArrayList<CarouselTagDTO>();
		LOGGER.debug("********************updateText*************************");
		Node carouselValuesNode = null;
		Node slidesNode = null;
		CarouselTagDTO carouselTagDTO = null;
		int count=0;
		if (carouselValuesNodePath != null) {

			try {
				carouselValuesNode = jcrSession.getNode(carouselValuesNodePath);

				if (carouselValuesNode != null) {
					LOGGER.debug("********************carouselValuesNode>>>>>>"
							+ carouselValuesNode);

					NodeIterator slidesIterator = null;
					slidesIterator = carouselValuesNode.getNodes();
				

					while (slidesIterator.hasNext()) {
						


						LOGGER.debug("********************carouselTagDTO COUNT>>>>>>"
								+ carouselTagDTO);
						carouselTagDTO=textCarouselList.get(count);

						slidesNode = slidesIterator.nextNode();

						carouselTagDTO
								.setTitle(slidesNode
										.hasProperty(LifePlusComponentConstants.CAROUSEL_TITLE) ? slidesNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_TITLE)
										.getString()
										: null);

						carouselTagDTO
								.setDescription(slidesNode
										.hasProperty(LifePlusComponentConstants.CAROUSEL_DESCRIPTION) ? slidesNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_DESCRIPTION)
										.getString()
										: null);

						carouselTagDTO
								.setNavTitle(slidesNode
										.hasProperty(LifePlusComponentConstants.CAROUSEL_NAV_TITLE) ? slidesNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_NAV_TITLE)
										.getString()
										: null);

						carouselTagDTO
								.setNavDescription(slidesNode
										.hasProperty(LifePlusComponentConstants.CAROUSEL_NAV_DESCRIPTION) ? slidesNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_NAV_DESCRIPTION)
										.getString()
										: null);

						carouselTagDTO
								.setButtonText(slidesNode
										.hasProperty(LifePlusComponentConstants.CAROUSEL_BUTTON_TEXT) ? slidesNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_BUTTON_TEXT)
										.getString()
										: null);

						carouselTagDTO
								.setLink(slidesNode
										.hasProperty(LifePlusComponentConstants.CAROUSEL_BUTTON_LINK) ? slidesNode
										.getProperty(
												LifePlusComponentConstants.CAROUSEL_BUTTON_LINK)
										.getString()
										: null);

						String link = carouselTagDTO.getButtonText();

						if (link.startsWith("/")) {
							link += LifePlusComponentConstants.CAROUSEL_BUTTON_LINK_HTML_EXTENSION;
						}
						updatedTagDTOList.add(carouselTagDTO);
						LOGGER.debug("********************updatedTagDTOList>>>>>>"
								+ updatedTagDTOList);
						LOGGER.debug(">>>>>>>>>>>>>>>>>>> update text FINISHED>>>>>>>>>>>>>>>>>>>>");
					count++;
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
		return updatedTagDTOList;

	}

}
