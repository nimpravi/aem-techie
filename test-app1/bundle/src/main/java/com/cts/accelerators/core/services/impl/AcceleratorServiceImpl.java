package com.cts.accelerators.core.services.impl;

/**
 * @author 369565
 */

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;
import com.cts.accelerators.core.services.AcceleratorService;

/**
 * One implementation of the {@link AcceleratorService}. Note that the
 * repository is injected, not retrieved.
 */
@Service
@Component(metatype = false, immediate = true)
public class AcceleratorServiceImpl implements AcceleratorService {
	/**
	 * @Override supper class method to provide reference to the repository
	 */
	public String getRepositoryName() {
		return ConnectionManager.getRepositoryName();
	}

	/**
	 * @Override supper class method
	 */
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest acceleratorServiceRequest) {
		return null;
	}
}
