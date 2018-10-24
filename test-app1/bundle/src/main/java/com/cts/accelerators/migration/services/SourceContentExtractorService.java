package com.cts.accelerators.migration.services;

/**
 * @author 369565
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
import com.cts.accelerators.core.services.AcceleratorService;

@Component (metatype = false,immediate=true)
@Service (value = SourceContentExtractorService.class)

public class SourceContentExtractorService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SourceContentExtractorService.class);

	@Activate
    protected void activate() {
        LOGGER.info("Source Content Extractor service started");
    }

    @Deactivate
    protected void deactivate() {
        LOGGER.info("Source Content Extractorservice stopped");
    }
 
	public String getRepositoryName() {
        return ConnectionManager.getRepositoryName();
    }
	
	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest) {
		LOGGER.info("execute || SourceContentExtractorService || start");
		LOGGER.info("execute || SourceContentExtractorService || end");
		// TODO Auto-generated method stub
		return null;
	}

}
