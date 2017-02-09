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
 * Name: OOTB Search component (/libs/foundation/components/search)
 * Description: DTO for Search component 
 * Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class SearchDTO extends CoreDTO {
	/**
	 * Properties for the Search component 
	 */
	@XmlElement
	private String searchIn;
	@XmlElement
	private String searchButtonText;
	@XmlElement
	private String statisticsText;
	@XmlElement
	private String noResultsText;
	@XmlElement
	private String spellcheckText;
	@XmlElement
	private String similarPagesText;
	@XmlElement
	private String relatedSearchesText;
	@XmlElement
	private String searchTrendsText;
	@XmlElement
	private String resultPagesText;
	@XmlElement
	private String previousText;
	@XmlElement
	private String nextText;
	
	/**
	 * @return String
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
	 * @return String
	 */
	public String getSearchButtonText() {
		return searchButtonText;
	}
	
	/**
	 * @param searchButtonText
	 */
	public void setSearchButtonText(String searchButtonText) {
		this.searchButtonText = searchButtonText;
	}
	
	/**
	 * @return String
	 */
	public String getStatisticsText() {
		return statisticsText;
	}
	
	
	/**
	 * @param statisticsText
	 */
	public void setStatisticsText(String statisticsText) {
		this.statisticsText = statisticsText;
	}
	
	/**
	 * @return String
	 */
	public String getNoResultsText() {
		return noResultsText;
	}
	
	
	/**
	 * @param noResultsText
	 */
	public void setNoResultsText(String noResultsText) {
		this.noResultsText = noResultsText;
	}
	
	/**
	 * @return String
	 */
	public String getSpellcheckText() {
		return spellcheckText;
	}
	
	
	/**
	 * @param spellcheckText
	 */
	public void setSpellcheckText(String spellcheckText) {
		this.spellcheckText = spellcheckText;
	}
	
	/**
	 * @return String
	 */
	public String getSimilarPagesText() {
		return similarPagesText;
	}
	
	/**
	 * @param similarPagesText
	 */
	public void setSimilarPagesText(String similarPagesText) {
		this.similarPagesText = similarPagesText;
	}
	
	/**
	 * @return String
	 */
	public String getRelatedSearchesText() {
		return relatedSearchesText;
	}
	/**
	 * @param relatedSearchesText
	 */
	
	public void setRelatedSearchesText(String relatedSearchesText) {
		this.relatedSearchesText = relatedSearchesText;
	}
	
	/**
	 * @return String
	 */
	public String getSearchTrendsText() {
		return searchTrendsText;
	}
	/**
	 * @param searchTrendsText
	 */
	public void setSearchTrendsText(String searchTrendsText) {
		this.searchTrendsText = searchTrendsText;
	}
	
	/**
	 * @return String
	 */
	public String getResultPagesText() {
		return resultPagesText;
	}
	/**
	 * @param resultPagesText
	 */
	
	public void setResultPagesText(String resultPagesText) {
		this.resultPagesText = resultPagesText;
	}
	
	/**
	 * @return String
	 */
	public String getPreviousText() {
		return previousText;
	}
	
	/**
	 * @param previousText
	 */
	
	public void setPreviousText(String previousText) {
		this.previousText = previousText;
	}
	
	/**
	 * @return String
	 */
	public String getNextText() {
		return nextText;
	}
	
	/**
	 * @param nextText
	 */
	
	public void setNextText(String nextText) {
		this.nextText = nextText;
	}
}
