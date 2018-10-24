package com.cts.accelerators.core.services;

import java.io.IOException;
import java.util.Dictionary;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.cts.accelerators.migration.exceptions.AcceleratorFaultCode;

/**
 * This service to will return configurations based on pid OR pid and key
 * combinations. It will be used by all the service lasses to retrieve the
 * configurations set for any services in the application
 * 
 * @author 369565
 * 
 */
@Component(metatype = true, immediate = true)
@Service(value = ConfigurationUtil.class)
public class ConfigurationUtil {

	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
	private ConfigurationAdmin configAdmin;

	private Configuration conf = null;
	private static final String CLASS_NAME = ConfigurationUtil.class.getName();

	private static final Logger LOGGER = LoggerFactory.getLogger(CLASS_NAME);

	/**
	 * This method provides the configuration value for a single property for a
	 * specific service identified by the provided by PID
	 * 
	 * @param pid
	 *            - service identifier of whose properties to be checked
	 * @param key
	 *            - name of the property
	 * @return String - value of the property
	 * @throws AcceleratorException
	 */
	public String getConfig(String pid, String key) throws AcceleratorException {
		String methodName = "getConfig";
		LOGGER.info(" || " + methodName + " || START");
		try {
			if (configAdmin != null) {
				conf = configAdmin.getConfiguration(pid);
				if (conf != null) {
					if (null != conf.getProperties()
							&& null != conf.getProperties().get(key)) {
						return conf.getProperties().get(key).toString();
					}
				}
			} else
				LOGGER.error("Configuration for pid " + pid + " is Null");
		} catch (IOException e) {
			LOGGER.error("An Exception has occured in " + CLASS_NAME + " in "
					+ methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return null;
	}

	/**
	 * This method provides the configuration value for a single property for a
	 * specific service identified by the provided by PID
	 * 
	 * @param pid
	 *            - service identifier of whose properties to be checked
	 * @param key
	 *            - name of the property
	 * @return String - value of the property
	 * @throws AcceleratorException
	 */
	public boolean getBooleanConfig(String pid, String key)
			throws AcceleratorException {
		String methodName = "getBooleanConfig";
		LOGGER.info(" || " + methodName + " || START");
		
		boolean isTrue = true;
		try {
			if (configAdmin != null) {
				conf = configAdmin.getConfiguration(pid);
				if (conf != null) {
					if (null != conf.getProperties()
							&& null != conf.getProperties().get(key)) {
						return conf.getProperties().get(key).equals(isTrue);
					}
				}
			} else
				LOGGER.error("Configuration for pid " + pid + " is Null");
		} catch (IOException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return false;
	}

	/**
	 * This method gets complete list of configurations for a specific service
	 * identified by the provided by PID
	 * 
	 * @param pid
	 *            - service identifier of whose properties to be checked
	 * @return Dictionary - containing key-value of all configurations
	 * @throws AcceleratorException
	 */

	public Dictionary<?, ?> getConfig(String pid) throws AcceleratorException {
		String methodName = "getConfig";
		LOGGER.info(" || " + methodName + " || START");
		try {
			if (configAdmin != null) {
				conf = configAdmin.getConfiguration(pid);
				if (conf != null) {
					return conf.getProperties();
				}
			} else
				LOGGER.error("Configuration for pid " + pid + " is Null");
		} catch (IOException e) {
			LOGGER.error("An Exception has occured in " + methodName, e);
			throw new AcceleratorException(
					AcceleratorFaultCode.IOEXCEPTION_STRING, CLASS_NAME,
					methodName, e.getCause());
		}
		LOGGER.info(" || " + methodName + " || END");
		return null;
	}
}