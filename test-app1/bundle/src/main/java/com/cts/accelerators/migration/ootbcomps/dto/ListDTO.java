package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant
 * Application : Migration Project
 * Name: OOTB List component (/libs/foundation/components/list)
 * Description: DTO for List component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ListDTO extends CoreDTO{
	
	/**
	 * Properties for the List component 
	 */
	@XmlElement
	private String listFrom;
	@XmlElement
	private String displayAs;
	@XmlElement
	private String orderBy;
	@XmlElement
	private int limit;
	@XmlElement
	private String feedEnabled;
	@XmlElement
	private int pageMax;
	@XmlElement
	private String parentPage;
	@XmlElement
	private String ancestorPage;
	@XmlElement
	private String pages;
	@XmlElement
	private String fieldConfig ;
	@XmlElement
	private int width;
	@XmlElement
	private String query;
	@XmlElement
	private String savedquery;
	@XmlElement
	private String tagsSearchRoot;
	@XmlElement
	private String tags;
	@XmlElement
	private String tagsMatch;
	/**
	 * @return String
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
	 * @return String
	 */
	public String getDisplayAs() {
		return displayAs;
	}
	/**
	 * @param displayAs 
	 */
	public void setDisplayAs(String displayAs) {
		this.displayAs = displayAs;
	}
	/**
	 * @return String
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
	 * @return int
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit 
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return String
	 */
	public String getFeedEnabled() {
		return feedEnabled;
	}
	/**
	 * @param feedEnabled 
	 */
	public void setFeedEnabled(String feedEnabled) {
		this.feedEnabled = feedEnabled;
	}
	/**
	 * @return int
	 */
	public int getPageMax() {
		return pageMax;
	}
	/**
	 * @param pageMax
	 */
	public void setPageMax(int pageMax) {
		this.pageMax = pageMax;
	}
	/**
	 * @return String
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
	 * @return String
	 */
	public String getAncestorPage() {
		return ancestorPage;
	}
	/**
	 * @param ancestorPage 
	 */
	public void setAncestorPage(String ancestorPage) {
		this.ancestorPage = ancestorPage;
	}
	/**
	 * @return String
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
	 * @return String
	 */
	public String getFieldConfig() {
		return fieldConfig;
	}
	/**
	 * @param fieldConfig 
	 */
	public void setFieldConfig(String fieldConfig) {
		this.fieldConfig = fieldConfig;
	}
	/**
	 * @return int
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return String
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
	 * @return String
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
	/**
	 * @return String
	 */
	public String getTagsSearchRoot() {
		return tagsSearchRoot;
	}
	/**
	 * @param tagsSearchRoot 
	 */
	public void setTagsSearchRoot(String tagsSearchRoot) {
		this.tagsSearchRoot = tagsSearchRoot;
	}
	/**
	 * @return String
	 */
	public String getTags() {
		return tags;
	}
	/**
	 * @param tags 
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}
	/**
	 * @return String
	 */
	public String getTagsMatch() {
		return tagsMatch;
	}
	/**
	 * @param tagsMatch
	 */
	public void setTagsMatch(String tagsMatch) {
		this.tagsMatch = tagsMatch;
	}
	
}
