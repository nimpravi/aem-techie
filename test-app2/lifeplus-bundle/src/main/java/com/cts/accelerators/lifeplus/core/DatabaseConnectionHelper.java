package com.cts.accelerators.lifeplus.core;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;
import org.apache.sling.jcr.api.SlingRepository;

public class DatabaseConnectionHelper {
//	@Reference
//	private DataSourcePool dbService;

	private static final String CLASS_NAME = DatabaseConnectionHelper.class
			.getName();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DatabaseConnectionHelper.class);

	public Connection getConnection() {
		String methodName = "getConnection";
		LOGGER.info(" ||" + methodName + "||" + CLASS_NAME + " || start @ ");
		
		BundleContext bundleContext = FrameworkUtil.getBundle(
				SlingRepository.class).getBundleContext();
		DataSourcePool dbService = (DataSourcePool) bundleContext.getService(bundleContext
				.getServiceReference(DataSourcePool.class.getName()));
		
		DataSource dataSource = null;
		Connection con = null;

		try {
			// Inject the DataSourcePool right here!

			LOGGER.info(" ||" + methodName + "||" + CLASS_NAME
					+ " || in try block @ ");

			dataSource = (DataSource) dbService
					.getDataSource(LifePlusGenericConstants.DATASOURCE_NAME);

			LOGGER.debug(" ||" + methodName + "||" + CLASS_NAME + " || dataSource || "
					+ dataSource);

			con = dataSource.getConnection();

			LOGGER.debug(" ||" + methodName + "||" + CLASS_NAME + " || con || " + con);

			/*LOGGER.info("getConnection || SignUpServlet || con @ "
					+ Class.forName("com.mysql.jdbc.Driver"));*/

			LOGGER.info("getConnection || SignUpServlet || con @ " + con);
			return con;
		} catch (SQLException e) {

			LOGGER.error(" ||" + methodName + "||" + CLASS_NAME
					+ " || SQLException @" , e);
		} /*catch (ClassNotFoundException e) {

			LOGGER.error(" ||" + methodName + "||" + CLASS_NAME
					+ " || ClassNotFoundException @ " + e);
		} */catch (DataSourceNotFoundException e) {

			LOGGER.error(" ||" + methodName + "||" + CLASS_NAME
					+ " || DataSourceNotFoundException @ " , e);
		}

		LOGGER.info("getConnection || SignUpServlet || dataSource @ "
				+ dataSource);
		LOGGER.info("|| " + methodName + " || " + CLASS_NAME + " ||End ");
		return null;

	}

}
