package com.cts.accelerators.lifeplus.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.core.dao.ConnectionManager;
import com.cts.accelerators.migration.exceptions.AcceleratorException;
import com.day.cq.wcm.api.Page;

@SlingServlet(paths="/bin/CustomFeedServlet", methods="POST", metatype=false)
public class CustomFeedServlet  extends SlingAllMethodsServlet {
	
	private static final long serialVersionUID = -4945870859489187371L;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(CustomFeedServlet.class);
	private final static String CLASS_NAME = CustomFeedServlet.class.getName();
	
	@Override
	protected void doPost(SlingHttpServletRequest slingRequest,
			SlingHttpServletResponse slingResponse) throws IOException {
		String methodName = "doPost";
		LOGGER.info(" || " + methodName + " || START");
		
		JSONArray json = new JSONArray();
	
		try {
			LOGGER.info(" || " + methodName + " || request || "+slingRequest.getParameter("requestType"));
			json = getFeedDynamicProperties(slingRequest);
			LOGGER.info(" || " + methodName + " || current path || "+slingRequest.getParameter("listroot"));

			if (json != null) {
				slingResponse.getWriter().write(json.toString());
			} else {
				slingResponse.getWriter().write("Response was null");
			}
		} catch (AcceleratorException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION OCCURED || "+e.getMessage());
		}
			
		LOGGER.info(" || " + methodName + " || END");
	}

	private JSONArray getFeedDynamicProperties(
			SlingHttpServletRequest slingRequest) throws AcceleratorException {
		String methodName = "getFeedDynamicProperties";
		LOGGER.info(" || " + methodName + " || START");
		
		Session jcrSession = ConnectionManager.getSession();
		LOGGER.debug(" || " + methodName + " || jcrSession || "+jcrSession);
		JSONObject jsonObject = new JSONObject();
		Map<String,String> map = new HashMap<String, String>();
		JSONArray jsonArray = new JSONArray();
		ResourceResolver resourceResolver = slingRequest.getResourceResolver();
		List<String> compResouceTypeList = new ArrayList<String>();
		Map<String, String> compPropertyMap = new HashMap<String, String>();
		List<String> temp = new ArrayList<String>();
		Node rootNode = null;
		String title = "";
		int count = 0;
		String componentResourceType = "";
		
		try {
			rootNode = jcrSession.getRootNode();
//			Node parentNode = JcrUtils.getNodeIfExists(slingRequest.getParameter("listroot"), jcrSession);
//			LOGGER.debug(" || " + methodName + " || parentNode || "+parentNode.getPath());
//			String listroot = parentNode.getPath();
//			Resource resource = resourceResolver.resolve(listroot);
			Resource resource = resourceResolver.resolve(slingRequest.getParameter("listroot"));
			Page rootPage = resource.adaptTo(Page.class);
			LOGGER.debug(" || " + methodName + " || rootPage || "+rootPage.getPath());
		    if (rootPage != null) {
		    	LOGGER.debug(" || " + methodName + " || rootPage NOT NULL");
		        Iterator<Page> children = rootPage.listChildren();
		        while (children.hasNext()) {
		            Page child = children.next();
		            Node node = child.adaptTo(Node.class);
		            Node jcrNode = node.getNode("jcr:content");
		            NodeIterator nodes = jcrNode.getNodes();
		            while(nodes.hasNext()) {
		            	temp = new ArrayList<String>();
						Node parNode = nodes.nextNode();
		                String resourceType = parNode.getProperty("sling:resourceType").getString();
		                if(resourceType.equals("foundation/components/parsys") || resourceType.equals("foundation/components/iparsys")) {
							NodeIterator componentNodes = parNode.getNodes(); 
		                    while(componentNodes.hasNext()) {
		                        Node componentNode = componentNodes.nextNode();
		                        componentResourceType = componentNode.getProperty("sling:resourceType").getString();
		                        if(count!=0) {
		                        	if(compResouceTypeList.contains(componentResourceType)) {
		                        		temp.add(componentResourceType);
		                        	}
		                        } else {
		                        	compResouceTypeList.add(componentResourceType);
		                        } 
		                    }
		                    compResouceTypeList = count!=0 ? temp : compResouceTypeList;
		                }
		                LOGGER.debug(" || " + methodName + " || compResouceTypeList11 || "+compResouceTypeList);
					}		
		            count++;
		        }
		        Iterator<Page> childrenPages = rootPage.listChildren();
		        while (childrenPages.hasNext()) {
		            Page child = childrenPages.next();
		            Node node = child.adaptTo(Node.class);
		            Node jcrNode = node.getNode("jcr:content");
		            NodeIterator nodes = jcrNode.getNodes();
		            while(nodes.hasNext()) {
						Node parNode = nodes.nextNode();
		                String resourceType = parNode.getProperty("sling:resourceType").getString();
		                if(resourceType.equals("foundation/components/parsys") || resourceType.equals("foundation/components/iparsys")) {
							NodeIterator componentNodes = parNode.getNodes(); 
		                    while(componentNodes.hasNext()) {
		                        Node componentNode = componentNodes.nextNode();
		                        componentResourceType = componentNode.getProperty("sling:resourceType").getString();
	                        	if(compResouceTypeList.contains(componentResourceType)) {
	                        		compPropertyMap.put(componentNode.getPath(), componentNode.getName());
	                        		LOGGER.debug(" || " + methodName + " || componentResourceType || "+componentResourceType);
	                        		LOGGER.debug(" || " + methodName + " || compResouceTypeList || "+compResouceTypeList);
	                        	}
		                    }
		                }
					}		
		        }
		    }
		    
		    LOGGER.debug(" || " + methodName + " || compPropertyMap || "+compPropertyMap);
		    
		    for(String resourceType : compPropertyMap.keySet()) {
		    	jsonObject = new JSONObject();
		    	Iterator properties = jcrSession.getNode(resourceType).getProperties();
		    	String property = "";
		    	String propertyName = "";
		    	String text = "";
		    	while(properties.hasNext()) {
		    		property = properties.next().toString();
		    		property = property.substring(property.indexOf("/"), property.length());
		    		propertyName = property.substring(property.lastIndexOf("/")+1, property.length());
		    		if (null!=propertyName && !propertyName.contains("jcr:") && !propertyName.contains("sling:")) {
		    			LOGGER.debug(" || " + methodName + " || propertyName || "+propertyName);
		    			text = compPropertyMap.get(resourceType) + " : " + propertyName;
		    			if (null == map.get(text)) {
		    				map.put(text, property);
						} else {
							map.put(text, map.get(text)+","+property);
						}
					}
		    	}
		    	LOGGER.info(" || " + methodName + " || map || "+map);
		    }
		    for(String keys : map.keySet()) {
	    		jsonObject = new JSONObject();
	    		jsonObject.put("text", keys);
	    		jsonObject.put("value", map.get(keys));
	    		jsonArray.put(jsonObject);
	    	}

		} catch (RepositoryException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION OCCURED || "+e.getMessage());
		} catch (JSONException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION OCCURED || "+e.getMessage());
		}
		
		LOGGER.info(" || " + methodName + " || jsonArray1 || "+jsonArray);
		LOGGER.info(" || " + methodName + " || END");
		return jsonArray;
	}
	
}
