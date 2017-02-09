/**
 * Helper Class for Country Selector component.
 * @version 1.0
 *  
 */
/**
 * Program Name :  NavigationHelper.java
 * Purpose      :  See description
 * Description  :  This helper class contains the navigation logic for HSOnline Website
 *
 */
package com.cts.store;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.day.cq.wcm.api.Page;

import javax.jcr.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationHelper {

	/** Logger instance */
	private static final Logger LOG = LoggerFactory
			.getLogger(NavigationHelper.class);
	
	LinkedHashMap<String, HashMap<String, String>> menu = new LinkedHashMap<String, HashMap<String, String>>();
	
	HashMap<String, String> mainMenu = new HashMap<String, String>();
	/**
	 * Method for to get the page navigation details for HSOnline
	 * 
	 * @param navPage
	 *            page object
	 * @return menu map object containing title, name and path of the page
	 */
	public LinkedHashMap<String, HashMap<String, String>> getMainMenuNav(
			Page navPage) throws Exception {
		Node node = null;
		String redirectPath = "#";
		String name = null;
		String title = null;
		try {
			if (navPage != null) {
				Iterator<Page> children = navPage.listChildren();
				while (children.hasNext()) {
					Page rootChildren = children.next();
					if (!rootChildren.isHideInNav()) {
						title = rootChildren.getTitle().toUpperCase();
						name = rootChildren.getName();
						int depth = rootChildren.getDepth();
						if (depth > 3) {
							node = (Node) rootChildren.getContentResource()
									.adaptTo(Node.class);
							if (node.hasProperty("redirectTarget")) {
								redirectPath = node.getProperty(
										"redirectTarget").getString();
							} else {
								redirectPath = rootChildren.getPath();
							}
						} else {
							redirectPath = rootChildren.getPath();
						}
						mainMenu.put(title, redirectPath);
						menu.put(name, mainMenu);
						mainMenu = new HashMap<String, String>();
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Exception occured while creating mainmenu map", e);
			e.printStackTrace();
		}
		return menu;
	}

	/**
	 * Method for to get the page navigation details for HSOnline
	 * 
	 * @param navPage
	 *            page object
	 * @return menu map object containing title, name and path of the page
	 */
	public LinkedHashMap<String, HashMap<String, String>> getSubMenuNav(
			Page navPage) throws Exception {		
		String redirectPath = "#";
		String name = null;
		String title = null;
		try {
			if (null != navPage) {
				Iterator<Page> children = navPage.listChildren();
				menu = new LinkedHashMap<String, HashMap<String, String>>();
				mainMenu = new HashMap<String, String>();
				while (children.hasNext()) {
					Page rootChildren = children.next();
					if (!rootChildren.isHideInNav()) {
						title = rootChildren.getPageTitle() == null ? rootChildren
								.getTitle() : rootChildren.getPageTitle();
						name = rootChildren.getName();
						redirectPath = rootChildren.getPath();
						mainMenu.put(title, redirectPath);
						menu.put(name, mainMenu);
						mainMenu = new HashMap<String, String>();
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Exception occured while creating submenu map", e);
			e.printStackTrace();
		}
		return menu;
	}

	
	/**
	 * Method sorts the countries list in alphabetical order
	 * 
	 * @param countries
	 *            LinkedHashMap object
	 * @return sortedCountries 
	 * 			map containing the countries list in alphabetical order
	 */
	public TreeMap<String, String> getSortedCountries(LinkedHashMap<String, HashMap<String, String>> countries)	throws Exception {
		TreeMap<String, String> sortedCountries = new TreeMap<String, String>();
		HashMap<String, String> countriesList = new HashMap<String, String>();
		try {
			for (Map.Entry<String, HashMap<String, String>> entry : countries.entrySet()) {
				countriesList = entry.getValue();
				for (Map.Entry<String, String> entry1 : countriesList.entrySet()) {
					sortedCountries.put(entry1.getKey(), entry1.getValue());
				}
			}
		} catch (Exception e) {
			LOG.error("Exception occured while sorting countryselector list:", e);
			e.printStackTrace();
		}
		return sortedCountries;
	}
}