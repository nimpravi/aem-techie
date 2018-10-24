package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author Cognizant DTO for Chart component -/libs/foundation/components/chart
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso(CoreDTO.class)
public class ChartDTO extends CoreDTO {

	/**
	 * Properties for the Chart component
	 */
	@XmlElement
	String chartData;
	@XmlElement
	String chartType;
	@XmlElement
	String chartAlt;
	@XmlElement
	String chartWidth;
	@XmlElement
	String chartHeight;

	/**
	 * @return String
	 */
	public String getChartData() {
		return chartData;
	}

	/**
	 * @param chartData
	 */
	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	/**
	 * @return String
	 */
	public String getChartType() {
		return chartType;
	}

	/**
	 * @param chartType
	 */
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	/**
	 * @return String
	 */
	public String getChartAlt() {
		return chartAlt;
	}

	/**
	 * @param chartAlt
	 */
	public void setChartAlt(String chartAlt) {
		this.chartAlt = chartAlt;
	}

	/**
	 * @return String
	 */
	public String getChartWidth() {
		return chartWidth;
	}

	/**
	 * @param chartWidth
	 */
	public void setChartWidth(String chartWidth) {
		this.chartWidth = chartWidth;
	}

	/**
	 * @return String
	 */
	public String getChartHeight() {
		return chartHeight;
	}

	/**
	 * @param chartHeight
	 */
	public void setChartHeight(String chartHeight) {
		this.chartHeight = chartHeight;
	}

}
