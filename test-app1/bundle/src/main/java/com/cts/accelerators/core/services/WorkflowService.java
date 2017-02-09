package com.cts.accelerators.core.services;

/**
 * @author 369565
 * Work Flow Service
 */

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.core.dto.AcceleratorServiceRequest;
import com.cts.accelerators.core.dto.AcceleratorServiceResponse;

@Component(metatype = false, immediate = true)
@Service(value = WorkflowService.class)
public class WorkflowService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkflowService.class);

	@Activate
	protected void activate() {
		LOGGER.info("Workflow service started");
	}

	@Deactivate
	protected void deactivate() {
		LOGGER.info("Workflow service stopped");
	}

	public String getRepositoryName() {
		return ConnectionManager.getRepositoryName();
	}

	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest) {
		LOGGER.info("execute || WorkflowService || start");
		LOGGER.info("execute || WorkflowService || end");
		// TODO Auto-generated method stub
		return null;
	}

}
