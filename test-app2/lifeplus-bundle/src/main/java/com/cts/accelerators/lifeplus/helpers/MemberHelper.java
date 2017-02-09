package com.cts.accelerators.lifeplus.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.core.LifePlusGenericConstants;

public class MemberHelper {

	boolean emailExist;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MemberHelper.class);
	public boolean isEmailExist(String email, Connection con) {

		String methodName = "isEmailExist";
		String checkEmailInDBQuery;
		checkEmailInDBQuery = "SELECT * from "
				+ LifePlusGenericConstants.MEMBER_TABLE + "WHERE email ="
				+ email;
		PreparedStatement ps;
		try {

			if (null != email) {

				checkEmailInDBQuery = "SELECT * from "
						+ LifePlusGenericConstants.MEMBER_TABLE
						+ " WHERE email ='" + email + "'";
				ps = con.prepareStatement(checkEmailInDBQuery);
				ResultSet resultSet = ps.executeQuery();
				emailExist = resultSet.next() ? true : false;
			}
			return emailExist;
		} catch (SQLException e) {
			LOGGER.error(" || " + methodName + " || SQLException OCCURED || ",e);
		}

		return emailExist;
	}
}
