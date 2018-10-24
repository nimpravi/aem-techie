package com.cts.accelerators.migration.transformer.factory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import org.apache.sling.commons.json.JSONObject;
import org.xml.sax.SAXException;

import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceResponse;

/**
 * This class will contain the common logic for all transformation classes
 * 
 * @author 369565
 * 
 */
public abstract class TransformerAbstractFactory {

	/**
	 * This variable will contain the appropriate Transformer type
	 */
	private TransformerType transformerType = null;

	public TransformerAbstractFactory(TransformerType transformerType) {
		this.transformerType = transformerType;
	}

	/**
	 * This method will take care of transforming contents from one format to
	 * another and will vary based on the transformer type. Definition of this
	 * method will be provided by the subclasses
	 * 
	 * @param transformerServiceRequest
	 * @return
	 */
	public abstract ContentTransformerServiceResponse transformContents(
			ContentTransformerServiceRequest transformerServiceRequest);

	/**
	 * This method will do the transformation for a single type of content
	 * (image, taxonomy, pages etc.)
	 * 
	 * @param contentType
	 * @return 
	 */
	public abstract void transformContentsForContentType(
			ContentTransformerServiceRequest transformerServiceRequest,
			String order) throws AcceleratorException;

	/**
	 * This method will create the transformed XML of a particular content type
	 * 
	 * @param contentfile
	 * @param contentXSL
	 * @param contentType
	 * @return
	 * @throws AcceleratorException
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 * @throws SAXException 
	 */
	public abstract String createTransformedXML(File contentfile,
			File contentXSL, String contentType,String outputDirectory)
			throws AcceleratorException, IOException, ParserConfigurationException, TransformerException, SAXException;

	/**
	 * This method will do the actual XML stream transformation based on XSL
	 * 
	 * @param tFactory
	 * @param iStreamXml
	 * @param iStreamXsl
	 * @return
	 * @throws AcceleratorException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws TransformerConfigurationException 
	 * @throws TransformerException 
	 */
	public abstract DOMResult transform(TransformerFactory tFactory,
			InputStream iStreamXml, StringBuffer iStreamXsl)
			throws AcceleratorException, ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException;
	
	
	

	/**
	 * @return the transformerType
	 */
	
	
	public TransformerType getTransformerType() {
		return transformerType;
	}

	/**
	 * @param transformerType
	 *            the transformerType to set
	 */
	public void setTransformerType(TransformerType transformerType) {
		this.transformerType = transformerType;
	}
}
