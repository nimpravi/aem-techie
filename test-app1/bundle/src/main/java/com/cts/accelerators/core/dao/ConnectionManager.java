package com.cts.accelerators.core.dao;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.AcceleratorGenericConstants;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;

/**
 * 
 * @author Cognizant Application : Migration Project Name: ConnectionManager
 *         Description: Connection Manager class to establish connectivity with
 *         CRX Repository Dependency: none
 * 
 */
public class ConnectionManager {

	private static SlingRepository repository;
	private static ResourceResolverFactory resourceResolverFactory;
	private static ResourceResolver resourceResolver;
	private static Logger LOGGER = LoggerFactory
			.getLogger(ConnectionManager.class);
	private static String CLASS_NAME = ConnectionManager.class.getName();
	private static Session jcrSession;

	private ConnectionManager() {

	}

	public static String getRepositoryName() {
		return repository.getDescriptor(Repository.REP_NAME_DESC);
	}

	/**
	 * Singleton getSession method to get instance of JCR Session
	 * 
	 * @return jcrSession
	 * @throws AcceleratorException
	 */
	public static Session getSession() throws AcceleratorException {
		String methodName = "getSession";
		LOGGER.info(" || " + methodName + " || START");
		BundleContext bundleContext = FrameworkUtil.getBundle(
				SlingRepository.class).getBundleContext();
		repository = (SlingRepository) bundleContext.getService(bundleContext
				.getServiceReference(SlingRepository.class.getName()));
		try {
			if (null == jcrSession) {
				jcrSession = repository.login(new SimpleCredentials(
						AcceleratorGenericConstants.CQ_USER,
						AcceleratorGenericConstants.CQ_PASSWORD.toCharArray()));
			}
		} catch (LoginException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.LOGIN_EXCEPTION,
					CLASS_NAME, methodName,
					e.getCause());
		} catch (RepositoryException e) {
			LOGGER.error(
					"An exception has occured in "+methodName,
					e);
			throw new AcceleratorException(
					AcceleratorFaultCode.REPOSITORY_EXCEPTION,
					CLASS_NAME, methodName,
					e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return jcrSession;
	}
	
	/**
	 * Singleton getSession method to get instance of JCR Session
	 * 
	 * @return jcrSession
	 * @throws AcceleratorException
	 */
	public static Session getSessionFromSlingRequest(SlingHttpServletRequest slingRequest) throws AcceleratorException {
		String methodName = "getSessionFromSlingRequest";
		LOGGER.info(" || " + methodName + " || START");
		if (null == jcrSession) {
			jcrSession = slingRequest.getResourceResolver().adaptTo(Session.class);
		}
		LOGGER.info(" || " + methodName + " || END");
		return jcrSession;
	}

	/**
	 * Singleton getResourceResolver method to get instance of ResourceResolver
	 * 
	 * @return resourceResolver
	 * @throws AcceleratorException
	 */
	public static ResourceResolver getResourceResolver()
			throws AcceleratorException {
		String methodName = "getResourceResolver";
		LOGGER.info(" || " + methodName + " || START");
		BundleContext bundleContext = FrameworkUtil.getBundle(
				ResourceResolverFactory.class).getBundleContext();
		resourceResolverFactory = (ResourceResolverFactory) bundleContext
				.getService(bundleContext
						.getServiceReference(ResourceResolverFactory.class
								.getName()));
		if (null == resourceResolver) {
			try {
				resourceResolver = resourceResolverFactory
						.getAdministrativeResourceResolver(null);
			} catch (org.apache.sling.api.resource.LoginException e) {
				LOGGER.error(
						"An exception has occured in "+methodName,
						e);
				throw new AcceleratorException(
						AcceleratorFaultCode.LOGIN_EXCEPTION,
						CLASS_NAME,
						methodName, e.getCause());
			}
		}
		LOGGER.info(" || " + methodName + " || END");
		return resourceResolver;
	}
}
