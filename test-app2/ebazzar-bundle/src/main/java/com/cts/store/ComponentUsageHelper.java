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

public class ComponentUsageHelper {
	/***** Declare and initialize log variable *****/
	private static final Logger LOG = LoggerFactory
			.getLogger(ReportGeneratorHelper.class);
	private static HashMap<String, List<String>> componentPagePath = new HashMap<String, List<String>>();
	private static HashMap<String, List<String>> activePagePath = new HashMap<String, List<String>>();
	private static HashMap<String, List<String>> deactivePagePath = new HashMap<String, List<String>>();

	private static HashMap<String, Integer> componentMap = new HashMap<String, Integer>();
	//private static HashMap<String, String> nameMap = new HashMap<String, String>();
	private static HashMap<String, String> activeNameMap = new HashMap<String, String>();
	private static HashMap<String, String> deactiveNameMap = new HashMap<String, String>();
	
	static Boolean activePage = false;
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
		LOG.error("PARENT"+parent);
		//componentPagePath = new HashMap<String, List<String>>();
		componentMap = new HashMap<String, Integer>();
		activePagePath = new HashMap<String, List<String>>();
		deactivePagePath = new HashMap<String, List<String>>();
		childNodeChk(parent, slingRequest);
		return componentMap;

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
						if( (activePagePath.containsKey(resourceType)) || (deactivePagePath.containsKey(resourceType))){
							List<String> lis1 = activePagePath
									.get(resourceType);
							List<String> lis2 = deactivePagePath
							.get(resourceType);
							
							String page = getPage(child1.getPath());
							//LOG.error("PAGE IN IF"+page);
							//LOG.error("PAGENODE IF"+child1.getName());
							if(child1.getName().equals("jcr:content")){
							    if(child1.hasProperty("cq:lastReplicationAction")){
								String replicationAction= child1.getProperty("cq:lastReplicationAction").getValue().getString();
								if(replicationAction.equals("Activate")){
									lis1.add(page);
								LOG.error("SIZE LIST1"+lis1.size());	
								//LOG.error("IF HAS PROPERTY VALUE"+replicationAction);
								}
								if(replicationAction.equals("Deactivate")){
										
									lis2.add(page);
									LOG.error("SIZE LIST2"+lis2.size());
								}
							}
						}
							LOG.error("Resource type"+resourceType+"LIs1 size"+lis1.size());
							if(lis1.size()!= 0){
								
							activePagePath.put(resourceType, lis1);
							}
							
							if(lis2.size() != 0 ){
							deactivePagePath.put(resourceType, lis2);
							}
						} else {
							List<String> lis1 = new ArrayList<String>();
							List<String> lis2 = new ArrayList<String>();
							String page = getPage(child1.getPath());
							//LOG.error("PAGE IN ELSE"+page);
							//LOG.error("PAGENODE ELSE"+child1.getName());
							if(child1.getName().equals("jcr:content")){
							if(child1.hasProperty("cq:lastReplicationAction")){
								String replicationAction= child1.getProperty("cq:lastReplicationAction").getValue().getString();
								if(replicationAction.equals("Activate")){
									lis1.add(page);
									LOG.error("ELSE SIZE LIST1"+lis1.size());
									//LOG.error("ELSE LIST1"+lis1);	
								//LOG.error("ACTIVE REPLICATION"+replicationAction);
								}
								else{
									lis2.add(page);
									LOG.error("ELSE SIZE LIST2"+lis2.size());
									//LOG.error("ELSE LIST2"+lis2);	
									//LOG.error("DEACTIVE REPLICATION"+replicationAction);
								}
								}
							}
							LOG.error("Resource TYpe od 2nd list"+resourceType+ " SIZE LIST1"+lis1.size());
							activePagePath.put(resourceType, lis1);
							
							deactivePagePath.put(resourceType, lis2);
							
							
						}
						LOG.error("ACTIVE MAP"+activePagePath);
						LOG.error("DEACTIVE MAP"+deactivePagePath);
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
		//LOG.error("COMPONENTMAP"+componentMap);
		return componentMap;
	}
	
	private static String getPage(String path) {
		// TODO Auto-generated method stub
		String[] pagePath = path.split("/jcr:content");

		return pagePath[0] + ".html";
	}

	
	/**
	 * returns the componentPagePath
	 * 
	 * @return
	 */
	public static HashMap<String, List<String>> getActivePagePath() {
		LOG.error("COMPOENT PAGE PATH ACTIVE"+activePagePath);
		return activePagePath;
	}
	
	public static HashMap<String, List<String>> getDeactivePagePath() {
		LOG.error("COMPOENT PAGE PATH DEACTIVE"+deactivePagePath);
		return deactivePagePath;
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

	public static HashMap<String, String> activeNameMap(
			SlingHttpServletRequest slingRequest) {
		try {
			
			for (Entry<String, List<String>> entry : activePagePath
					.entrySet()) {
				
				String key = entry.getKey();
				LOG.error("activePagePath"+activePagePath.get(key).size());
				if(activePagePath.get(key).size()!=0){
				Resource resource = slingRequest.getResourceResolver()
						.getResource(key);
				if (resource != null) {
					Node n = resource.adaptTo(Node.class);
					if (n != null) {
						String name = n.getName();
						activeNameMap.put(key,name);
					}
				}
			}
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("excepion", e);
		}
		LOG.error("ACTIVENAMEMAP"+activeNameMap);
		return activeNameMap;
	}
	
	
	public static HashMap<String, String> deactiveNameMap(
			SlingHttpServletRequest slingRequest) {
		try {
			for (Entry<String, List<String>> entry : deactivePagePath
					.entrySet()) {
				String key = entry.getKey();
				if(deactivePagePath.get(key).size()!=0){
				Resource resource = slingRequest.getResourceResolver()
						.getResource(key);
				if (resource != null) {
					Node n = resource.adaptTo(Node.class);
					if (n != null) {
						String name = n.getName();
						if(name != ""){
						deactiveNameMap.put(key,name);
						}
					}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("excepion", e);
		}
		LOG.error("DEACTIVENAMEMAP"+deactiveNameMap);
		return deactiveNameMap;
	}

}

