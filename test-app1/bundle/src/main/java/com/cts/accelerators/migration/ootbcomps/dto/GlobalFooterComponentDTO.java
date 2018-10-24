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
@XmlRootElement(name = "GlobalFooter")
@XmlSeeAlso({CoreDTO.class, ContainerDTO.class})
public class GlobalFooterComponentDTO extends CoreDTO{
	
	@XmlElements({ @XmlElement(name = "AuthorComponents") })
	private List<ContainerDTO> authorComponentsList;

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

	

}
