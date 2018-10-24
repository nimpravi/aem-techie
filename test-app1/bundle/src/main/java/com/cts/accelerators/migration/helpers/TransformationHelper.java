package com.cts.accelerators.migration.helpers;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentTransformerServiceResponse;
import com.cts.accelerators.migration.transformer.factory.XMLTransformer;

/**
 * This is the helper class for ContentTransformerService to carry out content
 * 
 * @author cognizant
 * 
 */
public class TransformationHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransformationHelper.class);
	private static final String CLASS_NAME = TransformationHelper.class.getName();

	/**
	 * This method will be called for initiating the content transformation
	 * 
	 * @param transformerServiceRequest
	 * @return transformerResponse
	 * @throws AcceleratorException
	 */
	public static ContentTransformerServiceResponse transformContents(
			ContentTransformerServiceRequest transformerServiceRequest)
			throws AcceleratorException {
		String methodName = "transformContents";
		ContentTransformerServiceResponse transformerResponse = null;
		LOGGER.info(" || " + methodName + " || START");
		try {
			if (transformerServiceRequest.getTransformationType() != null
					&& transformerServiceRequest.getTransformationType()
							.equalsIgnoreCase("XML")) {
				
					transformerResponse = new XMLTransformer()
							.transformContents(transformerServiceRequest);
				}
			
		} catch (AcceleratorException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.ACCELERATOR_EXCEPTION,
					CLASS_NAME,
					methodName,
					e.getCause());
		}
		LOGGER.debug(" || " + methodName
				+ " || contentTransformerServiceResponse || "
				+ transformerResponse);
		LOGGER.info(" || " + methodName + " || END");
		return transformerResponse;
	}

}
