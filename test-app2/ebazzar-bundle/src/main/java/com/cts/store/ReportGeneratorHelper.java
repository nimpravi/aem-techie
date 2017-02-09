package com.cts.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

public class ReportGeneratorHelper {
	/***** Declare and initialize log variable *****/
	private static final Logger LOG = LoggerFactory
			.getLogger(ReportGeneratorHelper.class);
	private static HashMap<String, List<String>> componentPagePath = new HashMap<String, List<String>>();
	private static HashMap<String, Integer> componentMap = new HashMap<String, Integer>();
	private static HashMap<String, String> nameMap = new HashMap<String, String>();
	static Integer count = 0;
	static Integer totalCount = 0;

	/**
	 * Method will get the component details in each page
	 * 
	 * @param parent
	 * @param slingRequest
	 * @return
	 */
	public static HashMap<String, Integer> nodeChk(Node parent,
			SlingHttpServletRequest slingRequest) {
		componentPagePath = new HashMap<String, List<String>>();
		componentMap = new HashMap<String, Integer>();
		childNodeChk(parent, slingRequest);
		return componentMap;

	}

	/**
	 * returns the componentPagePath
	 * 
	 * @return
	 */
	public static HashMap<String, List<String>> getComponentPagePath() {
		LOG.error("Component Page Path"+componentPagePath);
		return componentPagePath;
	}

	/**
	 * 
	 * @param parent
	 * @param slingRequest
	 * @return
	 */
	private static HashMap<String, Integer> childNodeChk(Node parent,
			SlingHttpServletRequest slingRequest) {
		List<String> li = new ArrayList<String>();
		// LOG.error("NOde:::: " + componentMap.size());
		Node pn = parent;
		try {
			if (pn.hasNodes()) {
				NodeIterator ni = pn.getNodes();
				while (ni.hasNext()) {
					Node child1 = ni.nextNode();
					if (child1.hasProperty("sling:resourceType")) {
						String resourceType = child1.getProperty(
								"sling:resourceType").getValue().getString();
						Boolean isComponent = checkComponent(resourceType,
								slingRequest);
						if (isComponent) {
							if (componentMap.containsKey(resourceType)) {
								Integer value = componentMap.get(resourceType);
								value += 1;
								componentMap.put(resourceType, value);
								// templateComponents.put(resourceType,);

							} else {
								componentMap.put(resourceType, 1);
							}
						}
						if (componentPagePath.containsKey(resourceType)) {
							List<String> lis = componentPagePath
									.get(resourceType);
							String page = getPage(child1.getPath());
							lis.add(page);
							componentPagePath.put(resourceType, lis);
						} else {
							List<String> lis = new ArrayList<String>();

							String page = getPage(child1.getPath());
							lis.add(page);
							componentPagePath.put(resourceType, lis);
						}
					}

					if (child1.hasNodes()) {
						childNodeChk(child1, slingRequest);
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Exception", e);
		}
		return componentMap;
	}

	private static String getPage(String path) {
		// TODO Auto-generated method stub
		String[] pagePath = path.split("/jcr:content");

		return pagePath[0] + ".html";
	}

	private static Boolean checkComponent(String resourceType,
			SlingHttpServletRequest slingRequest) {
		// TODO Auto-generated method stub
		try {
			Node n = slingRequest.getResourceResolver().getResource(
					resourceType).adaptTo(Node.class);
			Integer count = 1;
			String nodeType = n.getPrimaryNodeType().getName();
			if ("cq:Component".equals(nodeType)) {

				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static HashMap<String, String> checkComponent(
			SlingHttpServletRequest slingRequest) {
		try {
			for (Entry<String, List<String>> entry : componentPagePath
					.entrySet()) {
				String key = entry.getKey();
				Resource resource = slingRequest.getResourceResolver()
						.getResource(key);
				if (resource != null) {
					Node n = resource.adaptTo(Node.class);
					if (n != null) {
						String name = n.getName();
						nameMap.put(key, name);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("excepion", e);
		}
		LOG.error("NAMEMAP"+nameMap);
		return nameMap;
	}

}
