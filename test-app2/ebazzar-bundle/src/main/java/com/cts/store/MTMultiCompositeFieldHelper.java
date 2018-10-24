/**
 * Copyright (c)  AbbVie Inc – Business Administration (BA).
 *
 * Program Name :  MTMultiCompositeFieldHelper.java
 * Application  :  <AbbVie.com CMS Migration>
 * Purpose      :  See description
 * Description  :  Helper class for MTMultiCompositeField 
 * Names of Databases accessed: JCR
 * Modification History:
 * ---------------------
 *    Date          Modified by                       Modification Description
 *-----------    ----------------                    -------------------------
 *  09-May-2013   Cognizant Technology solutions            Initial Creation
 *-----------    ----------------                    -------------------------
 */

package com.cts.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
	 * String COMMA
	 */
	//public static final String COMMA = ",";
	
	/**
	 * String SPACE
	 */
	//public static final String SPACE = " ";

//import com.abbvie.cq.util.Constants;

/**
 *  Helper class for MTMultiCompositeField 
 * 
 * @version 1.0 09 May 2013
 * @author Cognizant Technology solutions
 * @since JDK 1.6.45
 */

public class MTMultiCompositeFieldHelper {
    public static final String COMMA = ",";
      public static final String SPACE = " ";
	private static final Logger LOG = LoggerFactory
			.getLogger(MTMultiCompositeFieldHelper.class);

	/**
	 * @param path
	 * @param nodename
	 * @param propertyNames
	 * @return
	 * @return
	 */
	public String[] getMultiCompositeFieldValues(String path, String nodename,
			String[] propertyNames, SlingHttpServletRequest slingRequest) {

		if (null != path && null != nodename && null != propertyNames) {
			try {
				String[] multiCompositeFieldValues;
				final ArrayList<String> fieldList = new ArrayList<String>();
				final Resource resource = slingRequest.getResourceResolver()
						.getResource(path);
				final Node resourceNode = resource.adaptTo(Node.class);
				if (resourceNode.hasNode(nodename)) {
					final NodeIterator nodeIterator = resourceNode.getNode(
							nodename).getNodes();
					while (nodeIterator.hasNext()) {
						final StringBuffer childNodeProprties = new StringBuffer();
						final Node childNode = (Node) nodeIterator.next();
						for (final String propertyName : propertyNames) {
							if (childNode.hasProperty(propertyName)
									&& null != childNode.getProperty(
											propertyName).getString()) {
								childNodeProprties.append(childNode
										.getProperty(propertyName).getString());
								System.out.println(childNode.getProperty(
										propertyName).getString());
							} else {
								childNodeProprties.append(SPACE);
							}
							childNodeProprties.append(COMMA);
							;
						}
						fieldList.add(childNodeProprties.toString());
					}
					multiCompositeFieldValues = fieldList
							.toArray(new String[fieldList.size()]);

					for (final String test : fieldList) {
						LOG.debug(" properties" + test);
					}
					return multiCompositeFieldValues;
				} else {
					LOG.error(nodename + " is not available in the path" + path);
					return null;
				}
			} catch (final Exception e) {
				LOG.error("exception occured while retreiving compositevalues"
						+ e.getMessage());
				return null;
			}
		}
		return propertyNames;

	}
	
	/**
	 * @param path
	 * @param delimiter
	 * @param nodename
	 * @param propertyNames
	 * @return
	 * @return
	 */
	public String[] getMultiCompositeFieldValues(String path, String nodename,
			String[] propertyNames, SlingHttpServletRequest slingRequest, String delimiter) {

		String localDelim = ",";
		if (null != delimiter && delimiter.length() == 1){
			localDelim = delimiter;
		}
		if (null != path && null != nodename && null != propertyNames) {
			try {
				String[] multiCompositeFieldValues;
				final ArrayList<String> fieldList = new ArrayList<String>();
				final Resource resource = slingRequest.getResourceResolver()
						.getResource(path);
				final Node resourceNode = resource.adaptTo(Node.class);
				if (resourceNode.hasNode(nodename)) {
					final NodeIterator nodeIterator = resourceNode.getNode(
							nodename).getNodes();
					while (nodeIterator.hasNext()) {
						final StringBuffer childNodeProprties = new StringBuffer();
						final Node childNode = (Node) nodeIterator.next();
						for (final String propertyName : propertyNames) {
							if (childNode.hasProperty(propertyName)
									&& null != childNode.getProperty(
											propertyName).getString()) {
								childNodeProprties.append(childNode
										.getProperty(propertyName).getString());
								System.out.println(childNode.getProperty(
										propertyName).getString());
							} else {
								childNodeProprties.append(SPACE);
							}
							childNodeProprties.append(localDelim);
							;
						}
						fieldList.add(childNodeProprties.toString());
					}
					multiCompositeFieldValues = fieldList
							.toArray(new String[fieldList.size()]);

					for (final String test : fieldList) {
						LOG.debug(" properties" + test);
					}
					return multiCompositeFieldValues;
				} else {
					LOG.error(nodename + " is not available in the path" + path);
					return null;
				}
			} catch (final Exception e) {
				LOG.error("exception occured while retreiving compositevalues"
						+ e.getMessage());
				return null;
			}
		}
		return propertyNames;

	}
	
	public ArrayList< HashMap<String, String>> getMultiCompositeValuesArrayList(String path, String nodename,
			String[] propertyNames, SlingHttpServletRequest slingRequest){
		 
		 if (null != path && null != nodename && null != propertyNames) {
				try {
					
					final ArrayList<HashMap<String, String>> fieldList = new ArrayList<HashMap<String, String>>();
					final Resource resource = slingRequest.getResourceResolver()
							.getResource(path);
					final Node resourceNode = resource.adaptTo(Node.class);
					if (resourceNode.hasNode(nodename)) {
						final NodeIterator nodeIterator = resourceNode.getNode(
								nodename).getNodes();
						while (nodeIterator.hasNext()) {
							final Node childNode = (Node) nodeIterator.next();
							
							HashMap<String, String> map = new HashMap<String, String>();

							for (final String propertyName : propertyNames) {
								if (childNode.hasProperty(propertyName)
										&& null != childNode.getProperty(
												propertyName).getString()) {
									
									map.put(propertyName, childNode.getProperty(
											propertyName).getString());
								} 
							}
							fieldList.add(map);
						}
						

						for ( HashMap<String, String> mapIterator  : fieldList) {
							System.out.println( "Properties.........");
							for (final String propertyName : propertyNames) {
								System.out.println(propertyName +"  :  "  +mapIterator.get(propertyName));
							}
							//LOG.debug(" properties" + test);
						}
						return fieldList;
					} else {
						LOG.error(nodename + " is not available in the path" + path);
						return null;
					}
				} catch (final Exception e) {
					LOG.error("exception occured while retreiving compositevalues"
							+ e.getMessage());
					return null;
				}
			}
		return null;
		 		
		
	}
	


}
