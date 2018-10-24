/**
 * Copyright (c)  AbbVie Inc – Business Administration (BA).
 *
 * Program Name :  MultiFieldHelper.java
 * Application  :  <AbbVie.com CMS Migration>
 * Purpose      :  See description
 * Description  :  Helper class for MultiField 
 * Names of Databases accessed: JCR
 * Modification History:
 * ---------------------
 *    Date          Modified by                       Modification Description
 *-----------    ----------------                    -------------------------
 *  29-June-2013   Cognizant Technology solutions            Initial Creation
 *-----------    ----------------                    -------------------------
 */

package com.cts.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.commons.json.JSONObject;

/**
 * Helper class for MultiFieldField
 * 
 * @version 1.0 09 June 2013
 * @author Cognizant Technology solutions
 * @since JDK 1.6.45
 */

public class MultiFieldHelper {

	private static final Logger LOG = LoggerFactory
			.getLogger(MultiFieldHelper.class);

	public ArrayList<HashMap<String, String>> getValuesArrayList(
			String[] applicationProperties) {

		try {
			final ArrayList<HashMap<String, String>> fieldList = new ArrayList<HashMap<String, String>>();
			if (null != applicationProperties) {
				for (String value : applicationProperties) {
					JSONObject propertyNames = new JSONObject(value);
					Iterator<String> keys = propertyNames.keys();
					HashMap<String, String> map = new HashMap<String, String>();
					while (keys.hasNext()) {
						String key = keys.next().toString();
						map.put(key, propertyNames.get(key).toString());
					}

					fieldList.add(map);
				}

			}
			return fieldList;
		} catch (final Exception e) {
			LOG.error("exception occured while retreiving compositevalues"
					+ e.getMessage());
			return null;
		}
	}
}
