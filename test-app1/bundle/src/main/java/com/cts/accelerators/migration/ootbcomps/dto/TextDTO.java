package com.cts.accelerators.migration.ootbcomps.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cts.accelerators.migration.helpers.CDATAAdapter;

/**
 * 
 * 
 * @author Cognizant Application : Migration Project Name: OOTB Text component
 *         (/libs/foundation/components/text) Description: DTO for Text
 *         component Dependency: none
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Text")
@XmlSeeAlso(CoreDTO.class)
public class TextDTO extends CoreDTO {

	/**
	 * Properties for the Text component
	 */
	@XmlJavaTypeAdapter(CDATAAdapter.class)
    private String text;
	
	public TextDTO() {
		super();
	}
	
	public TextDTO(String text){
		super();
		this.text = text;
	}

	/**
	 * @return String
	 */
	public final String getText() {
		return text;
	}

	/**
	 * @param text
	 */
	public final void setText(String text) {
		this.text = text;
	}

}
