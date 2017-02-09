package com.cts.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentReportGenerator {
	static Integer count = 0;
	static Integer totalCount = 0;
	private static HashMap<String, List<ComponentBean>> componentDetailMap = new HashMap<String, List<ComponentBean>>();
	
	private static final Logger LOG = LoggerFactory.getLogger(ComponentReportGenerator.class);
	
	/**
	 *Method will check all the components and their
	 *properties and will return a map 
	 * @param node
	 * @param resourceResolver
	 * @return
	 */
	public static HashMap<String,List<ComponentBean>> countComponents(Node node,
			final ResourceResolver resourceResolver) {
		try {
			 componentDetailMap = new HashMap<String, List<ComponentBean>>();
			Map<Node, Integer> values = new HashMap<Node, Integer>();
			Session session = resourceResolver.adaptTo(Session.class);
			
			if (node != null) {
				if (node.hasNodes()) {
					NodeIterator rootNodeIt = node.getNodes();
					while (rootNodeIt.hasNext()) {
						Node nextNode = rootNodeIt.nextNode();
						count = 0;
						LOG.error("Node " + nextNode);
						if (nextNode.hasNodes()) {
							NodeIterator ni = nextNode.getNodes();
							getChildCompNodes(nextNode);
						}
						totalCount += count;
						values.put(nextNode, count);
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Excerption ", e);
		}
		return componentDetailMap;
	}
	
	private static void getChildCompNodes(Node node) {
		// TODO Auto-generated method stub
		
		try {
			if (node.hasNodes()) {
				NodeIterator ni = node.getNodes();
				while (ni.hasNext()) {
					
					Node n = ni.nextNode();
					if (!n.getPrimaryNodeType().getName()
							.equals("cq:Component")
							&& n.hasNodes()) {
						getChildCompNodes(n);

					} else if (n.getPrimaryNodeType().getName().equals(
							"cq:Component")) {
				
						String name = n.getName();
						List<ComponentBean> compList = new ArrayList<ComponentBean>();
						ComponentBean cmpBean = new ComponentBean();
						if (n.hasProperty("componentGroup")) {
							String componentGroup = n.getProperty(
									"componentGroup").getValue().getString();
							cmpBean.setComponentGroup(componentGroup);
							count++;
							
						}
						else
							cmpBean.setComponentGroup("");
						if (n.hasProperty("jcr:description")) {
							String componentDescription = n.getProperty(
									"jcr:description").getValue()
									.getString();
							cmpBean.setComponentDescription(componentDescription);
						}
						else
							cmpBean.setComponentDescription("");
						if(n.hasNode("icon.png")){
							Node icon  = n.getNode("icon.png");
							String iconPath = icon.getPath();
							cmpBean.setIconPath(iconPath);
						}
						else
							cmpBean.setIconPath("");
						if(n.hasNode("thumbnail.png")){
					
							Node icon  = n.getNode("thumbnail.png");
							String thumbnailPath = icon.getPath();
							cmpBean.setThumbnailPath(thumbnailPath);
						}
						else
							cmpBean.setThumbnailPath("");
						if(n.hasProperty("wikiLink")){
							String wikiLink = n.getProperty("wikiLink")
									.getValue().getString();
							cmpBean.setWikiLink(wikiLink);
						}
						else
							cmpBean.setWikiLink("");
						
						compList.add(cmpBean);
						componentDetailMap.put(name,compList);
						
					}
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Exception ", e);
		}
	}

}
