package com.cts.accelerators.migration.services;
/**
 * 
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
import com.cts.accelerators.migration.services.dto.ContentImporterServiceRequest;
import com.cts.accelerators.migration.services.dto.ContentImporterServiceResponse;

@Component(metatype = false,immediate=true)
@Service (value = ContentExtractorService.class)
/**
 * 
 * @author cognizant
 *
 */
public class ContentExtractorService implements AcceleratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentExtractorService.class);

	@Activate
    protected void activate() {
        LOGGER.info("Content Importer service started");
    }

    @Deactivate
    protected void deactivate() {
        LOGGER.info("Content Importer service stopped");
    }
    
	public String getRepositoryName() {
        return ConnectionManager.getRepositoryName();
    }

	public AcceleratorServiceResponse execute(
			AcceleratorServiceRequest serviceRequest) {
		LOGGER.info("execute || ContentImporterService || start");
		AcceleratorServiceResponse serviceResponse = new ContentImporterServiceResponse();
		
		ContentImporterServiceRequest importerServiceRequest = null;
		if(serviceRequest instanceof  ContentImporterServiceRequest){
			importerServiceRequest = (ContentImporterServiceRequest) serviceRequest;
		}
		
		LOGGER.info("execute || ContentImporterService || end");
		return null;
	}
}
