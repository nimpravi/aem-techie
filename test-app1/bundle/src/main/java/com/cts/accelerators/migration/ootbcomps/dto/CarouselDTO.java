package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Chart component
 *         (/libs/foundation/components/carousel) Description: DTO for Carousel
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class CarouselDTO extends CoreDTO {

	/**
	 * Properties for the Carousel component
	 */
	@XmlElement
	private String playSpeed;
	@XmlElement
	private String transTime;
	@XmlElement
	private String controlsType;
	@XmlElement
	private String listFrom;
	@XmlElement
	private String orderBy;
	@XmlElement
	private String parentPage;
	@XmlElement
	private String limit;
	@XmlElement
	private String pages;
	@XmlElement
	private String searchIn;
	@XmlElement
	private String query;
	@XmlElement
	private String savedquery;
	/**
	 * @return the playSpeed
	 */
	public String getPlaySpeed() {
		return playSpeed;
	}

	/**
	 * @param playSpeed the playSpeed to set
	 */
	public void setPlaySpeed(String playSpeed) {
		this.playSpeed = playSpeed;
	}

	/**
	 * @return the transTime
	 */
	public String getTransTime() {
		return transTime;
	}

	/**
	 * @param transTime the transTime to set
	 */
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	/**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}
	/**
	 * @return controlsType
	 */
	public String getControlsType() {
		return controlsType;
	}

	/**
	 * @param controlsType
	 */
	public void setControlsType(String controlsType) {
		this.controlsType = controlsType;
	}

	/**
	 * @return listFrom
	 */
	public String getListFrom() {
		return listFrom;
	}

	/**
	 * @param listFrom
	 */
	public void setListFrom(String listFrom) {
		this.listFrom = listFrom;
	}

	/**
	 * @return orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return parentPage
	 */
	public String getParentPage() {
		return parentPage;
	}

	/**
	 * @param parentPage
	 */
	public void setParentPage(String parentPage) {
		this.parentPage = parentPage;
	}

	

	/**
	 * @return pages
	 */
	public String getPages() {
		return pages;
	}

	/**
	 * @param pages
	 */
	public void setPages(String pages) {
		this.pages = pages;
	}

	/**
	 * @return searchIn
	 */
	public String getSearchIn() {
		return searchIn;
	}

	/**
	 * @param searchIn
	 */
	public void setSearchIn(String searchIn) {
		this.searchIn = searchIn;
	}

	/**
	 * @return query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return savedquery
	 */
	public String getSavedquery() {
		return savedquery;
	}

	/**
	 * @param savedquery
	 */
	public void setSavedquery(String savedquery) {
		this.savedquery = savedquery;
	}

}
