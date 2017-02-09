package com.cts.accelerators.lifeplus.tag;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorComponentConstants;
import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.ootbcomps.dto.CarouselDTO;

public class OOTBCarouselTag extends BodyTagSupport {

	String carouselNodePath;
	private String carouselOutput;

	/**
	 * @return the carouselNodePath
	 */
	public String getCarouselNodePath() {
		return carouselNodePath;
	}

	/**
	 * @param carouselNodePath
	 *            the carouselNodePath to set
	 */
	public void setCarouselNodePath(String carouselNodePath) {
		this.carouselNodePath = carouselNodePath;
	}

	/**
	 * @return the carouselOutput
	 */
	public String getOOTBCarouselList() {
		return carouselOutput;
	}

	/**
	 * @param carouselOutput
	 *            the carouselOutput to set
	 */
	public void setOOTBCarouselList(String s) {
		this.carouselOutput = s;
	}

	Session jcrSession = null;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OOTBCarouselTag.class);

	public int doStartTag() throws JspException {

		List<CarouselDTO> carouselTagDTOList = new ArrayList<CarouselDTO>();

		try {
			jcrSession = ConnectionManager.getSession();
		} catch (AcceleratorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		carouselTagDTOList = updateCarousel(carouselTagDTOList);

		pageContext.setAttribute(carouselOutput, carouselTagDTOList);

		return SKIP_BODY;

	}

	private List<CarouselDTO> updateCarousel(
			List<CarouselDTO> carouselTagDTOList) {
		// TODO Auto-generated method stub
		LOGGER.debug(">>>>>>>>>>>>>>>>>>>updateCarousel>>>>>>>>>>>>>>>>>>>>");
		Node carouselNode = null;
		CarouselDTO carouselTagDTO = new CarouselDTO();
		if (carouselNodePath != null) {
			try {
				LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselNodePath>>>>>>>>>>>>>>>>>>>>"
						+ carouselNodePath);

				carouselNode = jcrSession.getNode(carouselNodePath);
				LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselNode>>>>>>>>>>>>>>>>>>>>"
						+ carouselNode);
				if (carouselNode != null) {

					carouselTagDTO = new CarouselDTO();
					
					carouselTagDTO
					.setPlaySpeed(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_PLAY_SPEED)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_PLAY_SPEED)
														.getString():null);
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>getPlaySpeed>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getPlaySpeed());
					
					carouselTagDTO
					.setTransTime(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_TRANS_TIME)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_TRANS_TIME)
											.getString():null);
					
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.setTransTime>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getTransTime());

					carouselTagDTO
					.setControlsType(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_CONTROLS_TYPE)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_CONTROLS_TYPE)
											.getString():null);
					
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.setControlsType>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getControlsType());
					
					carouselTagDTO
					.setListFrom(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_LIST_FORM)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_LIST_FORM)
											.getString():null);

					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getListFrom>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getListFrom());
					
					carouselTagDTO
					.setOrderBy(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_ORDER_BY)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_ORDER_BY)
											.getString():null);
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getOrderBy>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getOrderBy());
					
					carouselTagDTO
					.setParentPage(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_PARENT_PAGE)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_PARENT_PAGE)
											.getString():null);
					
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getParentPage>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getParentPage());
					
					carouselTagDTO
					.setLimit(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_LIMIT)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_LIMIT)
											.getString():null);
					

					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getLimit>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getLimit());
					
					
					carouselTagDTO
					.setPages(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_PAGES)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_PAGES)
											.getString():null);
					
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getPages>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getPages());
					
					carouselTagDTO
					.setSearchIn(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_SEARCH_IN)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_SEARCH_IN)
											.getString():null);

					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getSearchIn>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getSearchIn());
					
					carouselTagDTO
					.setQuery(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_QUERY)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_QUERY)
											.getString():null);
					
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getQuery>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getQuery());
					
					

					carouselTagDTO
					.setSavedquery(carouselNode
							.hasProperty(
									AcceleratorComponentConstants.CAROUSELDTO_SAVED_QUERY)?carouselNode
											.getProperty(
													AcceleratorComponentConstants.CAROUSELDTO_SAVED_QUERY)
											.getString():null);

					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTO.getSavedquery>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTO.getSavedquery());
					

					carouselTagDTOList.add(carouselTagDTO);
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>carouselTagDTOList>>>>>>>>>>>>>>>>>>>>"
							+ carouselTagDTOList);
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>FINISHED>>>>>>>>>>>>>>>>>>>>"
							);
				}
			}

			catch (PathNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return carouselTagDTOList;

	}

}
