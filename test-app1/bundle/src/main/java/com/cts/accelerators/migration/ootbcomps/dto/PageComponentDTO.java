package com.cts.accelerators.migration.ootbcomps.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Page component
 *         (/libs/foundation/components/page) Description: DTO for Page
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PageContents")
@XmlSeeAlso({CoreDTO.class, ContainerDTO.class})
public class PageComponentDTO extends CoreDTO{
	
	private String template;
	private String jcr_title;
	private String cqDesignPath;
	private PagePropertiesDTO pageProperties;
	private GlobalFooterComponentDTO footerDTO;
	
	/**
	 * @return the pagePropertiesDTO
	 */
	public PagePropertiesDTO getPagePropertiesDTO() {
		return pageProperties;
	}

	/**
	 * @param pagePropertiesDTO the pagePropertiesDTO to set
	 */
	public void setPagePropertiesDTO(PagePropertiesDTO pagePropertiesDTO) {
		this.pageProperties = pagePropertiesDTO;
	}

	/**
	 * @return the cqDesignPath
	 */
	public String getCqDesignPath() {
		return cqDesignPath;
	}

	/**
	 * @param cqDesignPath the cqDesignPath to set
	 */
	public void setCqDesignPath(String cqDesignPath) {
		this.cqDesignPath = cqDesignPath;
	}

	@XmlElements({ @XmlElement(name = "AuthorComponents") })
	private List<ContainerDTO> authorComponentsList;

	@XmlElements({ @XmlElement(name = "DesignComponents") })
	private List<ContainerDTO> designComponentsList;
	
	@XmlElement
	private String tagIds;

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the jcr_title
	 */
	public String getJcr_title() {
		return jcr_title;
	}

	/**
	 * @param jcr_title the jcr_title to set
	 */
	public void setJcr_title(String jcr_title) {
		this.jcr_title = jcr_title;
	}

	/**
	 * @return the authorComponentsList
	 */
	public List<ContainerDTO> getAuthorComponentsList() {
		return authorComponentsList;
	}

	/**
	 * @param authorComponentsList the authorComponentsList to set
	 */
	public void setAuthorComponentsList(List<ContainerDTO> authorComponentsList) {
		this.authorComponentsList = authorComponentsList;
	}

	/**
	 * @return the designComponentsList
	 */
	public List<ContainerDTO> getDesignComponentsList() {
		return designComponentsList;
	}

	/**
	 * @param designComponentsList the designComponentsList to set
	 */
	public void setDesignComponentsList(List<ContainerDTO> designComponentsList) {
		this.designComponentsList = designComponentsList;
	}

	/**
	 * @return the tagIds
	 */
	public String getTagIds() {
		return tagIds;
	}

	/**
	 * @param tagIds the tagIds to set
	 */
	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	/**
	 * @return the footerDTO
	 */
	public GlobalFooterComponentDTO getFooterDTO() {
		return footerDTO;
	}

	/**
	 * @param footerDTO the footerDTO to set
	 */
	public void setFooterDTO(GlobalFooterComponentDTO footerDTO) {
		this.footerDTO = footerDTO;
	}
	
}
