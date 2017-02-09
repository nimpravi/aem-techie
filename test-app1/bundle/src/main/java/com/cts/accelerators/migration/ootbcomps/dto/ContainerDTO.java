package com.cts.accelerators.migration.ootbcomps.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.cts.accelerators.migration.ootbcomps.designdto.BreadcrumbDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.ToolbarDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.TopnavDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.UserInfoDTO;
import com.cts.accelerators.migration.ootbcomps.designdto.DesignTitleDTO;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso({ ArchiveDTO.class, BreadcrumbDTO.class, CarouselDTO.class,
		ChartDTO.class, CoreDTO.class, DAMDTO.class, DownloadDTO.class,
		EntryListDTO.class, EntryTextDTO.class, ExternalDTO.class,
		FlashDTO.class, ImageDTO.class, ListChildrenDTO.class, ListDTO.class,
		LoginDTO.class, PageComponentDTO.class, ProfileDTO.class,
		RedirectDTO.class, ReferenceDTO.class, SearchDTO.class,
		SiteMapDTO.class, SizeFieldDTO.class, SlideShowDTO.class,
		TableDTO.class, TagDTO.class, TextDTO.class, TextImageDTO.class,
		TitleDTO.class, DesignTitleDTO.class, ToolbarDTO.class,
		TopnavDTO.class, UserInfoDTO.class, VideoDTO.class,
		CustomCarouselDTO.class, CustomImageDTO.class, TitleLinkDTO.class,
		ImageMultiFieldDTO.class, TitleImageDTO.class,
		CustomTitleImageDTO.class, ListOfLinkDTO.class,
		ListOfLinkItemDTO.class, GlobalFooterComponentDTO.class, CustomTextImageDTO.class })
public class ContainerDTO extends CoreDTO {

	@XmlElement(name = "ContainerVariance")
	private String containerVariance;

	@XmlElement(name = "ContainerNodeName")
	private String containerNodeName;

	@XmlElements({ @XmlElement(name = "Components") })
	private List<CoreDTO> components;

	@XmlElement(name = "GlobalFooterContainer")
	private String globalFooterContainer;

	@XmlElement(name = "GlobalFooterContainerName")
	private String globalFooterContainerName;

	public ContainerDTO() {
		super();
	}

	public ContainerDTO(String containerVariance, String containerNodeName,
			List<CoreDTO> components, String globalFooterContainer,
			String globalFooterContainerName) {
		super();
		this.containerVariance = containerVariance;
		this.containerNodeName = containerNodeName;
		this.components = components;
		this.globalFooterContainer = globalFooterContainer;
		this.globalFooterContainerName = globalFooterContainerName;
	}

	/**
	 * @return the containerVariance
	 */
	public String getContainerVariance() {
		return containerVariance;
	}

	/**
	 * @param containerVariance
	 *            the containerVariance to set
	 */
	public void setContainerVariance(String containerVariance) {
		this.containerVariance = containerVariance;
	}

	/**
	 * @return the containerNodeName
	 */
	public String getContainerNodeName() {
		return containerNodeName;
	}

	/**
	 * @param containerNodeName
	 *            the containerNodeName to set
	 */
	public void setContainerNodeName(String containerNodeName) {
		this.containerNodeName = containerNodeName;
	}

	/**
	 * @return the components
	 */
	public List<CoreDTO> getComponents() {
		return components;
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents(List<CoreDTO> components) {
		this.components = components;
	}

	/**
	 * @return the globalFooterContainer
	 */
	public String getGlobalFooterContainer() {
		return globalFooterContainer;
	}

	/**
	 * @param globalFooterContainer
	 *            the globalFooterContainer to set
	 */
	public void setGlobalFooterContainer(String globalFooterContainer) {
		this.globalFooterContainer = globalFooterContainer;
	}

	/**
	 * @return the globalFooterContainerName
	 */
	public String getGlobalFooterContainerName() {
		return globalFooterContainerName;
	}

	/**
	 * @param globalFooterContainerName
	 *            the globalFooterContainerName to set
	 */
	public void setGlobalFooterContainerName(String globalFooterContainerName) {
		this.globalFooterContainerName = globalFooterContainerName;
	}

}
