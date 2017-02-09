package com.cts.accelerators.core.services;

import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
import com.cts.accelerators.migration.exceptions.AcceleratorException;

/**
 * @author 369565
 * 
 *         A simple service interface
 */
public interface AcceleratorService {

	/**
	 * @return the AcceleratorServiceResponse based on AcceleratorServiceRequest
	 * @throws AcceleratorException
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest)
			throws AcceleratorException;

}