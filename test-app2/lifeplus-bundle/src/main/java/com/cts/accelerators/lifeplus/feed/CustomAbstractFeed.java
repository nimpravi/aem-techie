package com.cts.accelerators.lifeplus.feed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.SimpleTimeZone;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceNotFoundException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.jcr.resource.JcrPropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.SimpleXml;
import com.day.cq.commons.feed.AbstractFeed;
import com.day.cq.commons.feed.StringResponseWrapper;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.text.Text;

public class CustomAbstractFeed implements Feed {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(CustomAbstractFeed.class);

	public CustomAbstractFeed(Resource res, SlingHttpServletRequest req,
			SlingHttpServletResponse resp) throws RepositoryException {
		Node node;
		JcrPropertyMap props;
		request = req;
		response = resp;
		if (res == null)
			res = request.getResource();
		if (ResourceUtil.isNonExistingResource(res))
			throw new ResourceNotFoundException("No data to render.");
		node = (Node) res.adaptTo(Node.class);
		if (node == null)
			throw new RepositoryException((new StringBuilder())
					.append("No node found for resource: ")
					.append(res.getPath()).toString());
		Node propNode = node;
		nodePath = node.getPath();
		if (node.getName().equals("jcr:content"))
			nodePath = node.getParent().getPath();
		else if (node.hasNode("jcr:content"))
			propNode = node.getNode("jcr:content");
		props = new JcrPropertyMap(propNode);
		if (node.isNodeType("nt:file")) {
			fileSize = props.get("jcr:data", Property.class) == null ? 0L
					: ((Property) props.get("jcr:data", Property.class))
							.getLength();
			mimeType = (String) props.get("jcr:mimeType",
					"application/octet-stream");
		} else {
			mimeType = "text/html";
		}
		Text.getRelativeParent(nodePath, 1);
		baseUrl = getUrlPrefix();
		title = (String) props.get("jcr:title", node.getName());
		description = (String) props.get("jcr:description", "");
		language = (String) props.get("jcr:language", "");
		author = (String) props.get("jcr:createdBy", "");
		publishedDate = formatDate((Calendar) props.get("jcr:created",
				props.get("cq:lastModified", Calendar.getInstance())));
		tags = Text
				.implode((String[]) props.get("cq:tags", new String[0]), ",");
		title = Text.escape(title);
		author = Text.escape(author);
		tags = Text.escape(tags);
		
//		String[] allProps = getAllProperties();
//		if(null != allProps) {
//			setProperties(allProps);
//		}
//		LOGGER.info("|| abs constructor properties || 2 || "+properties);

		return;
	}
	
	public String getContentType() {
		return "text/xml";
	}

	public String getCharacterEncoding() {
		return "utf-8";
	}

	public void printEntry(Resource res) throws IOException {
		initXml();
		try {
			request.setAttribute(
					"com.day.cq.wcm.api.components.ComponentContext/bypass",
					"true");
			StringResponseWrapper wrapper = new StringResponseWrapper(response);
			request.getRequestDispatcher(getFeedEntryPath(res)).include(
					request, wrapper);
			xml.getWriter().print(wrapper.getString());
		} catch (ServletException se) {
			throw new IOException(se.getMessage());
		}
	}

	public void printChildEntries() throws IOException {
		printEntries(request.getResourceResolver().listChildren(
				request.getResource()));
	}

	public void printChildEntries(int max)
        throws IOException
    {
        Iterator iter;
        ArrayList children;
        iter = request.getResourceResolver().listChildren(request.getResource());
        children = new ArrayList();
        //TODO convert to do-while loop
/*_L2:
        Resource res;
        if(!iter.hasNext())
            break;  Loop/switch isn't completed 
        res = (Resource)iter.next();
        RepositoryException re;
        if(!((Node)res.adaptTo(Node.class)).getName().equals("jcr:content"))
            children.add(res);
        continue;  Loop/switch isn't completed 
        re;
        throw new IOException(re.getMessage());
        if(true) goto _L2; else goto _L1
_L1:
        printEntries(children.iterator(), max);
        return;*/
    }

	public void printEntries(Iterator iter) throws IOException {
		printEntries(iter, 0);
	}

	public void printEntries(Iterator iter, int max) throws IOException {
		int i = 0;
		do {
			if (!iter.hasNext())
				break;
			printEntry((Resource) iter.next());
		} while (max <= 0 || ++i <= max);
	}

	public void printFooter() throws IOException {
		initXml();
		xml.closeDocument();
	}

	void initXml() throws IOException {
		if (xml == null)
			xml = new SimpleXml(response.getWriter());
	}

	String getFeedEntryPath(Resource res) {
		return (new StringBuilder()).append(res.getPath())
				.append(".feedentry.xml").toString();
	}

	String formatDate(Calendar calendar) {
		try {
			calendar.setTimeZone(new SimpleTimeZone(0, "UTC"));
			return String.format("%1$tFT%1$tTZ", new Object[] { calendar });
		} catch (Exception e) {
			return "";
		}
	}

	boolean isFile() {
		return fileSize > 0L;
	}

	String getFileSize() {
		return (new StringBuilder()).append(fileSize).append("").toString();
	}

	String getMimeType() {
		return mimeType;
	}

	String getTitle() {
		return title;
	}

	String getSummary() {
		return description;
	}

	String getDescription() {
		return description;
	}

	String getTags() {
		return tags;
	}

	String getLanguage() {
		return language;
	}

	String getAuthorName() {
		return author;
	}

	String getAuthorEmail() {
		return "".equals(author) ? "" : (new StringBuilder()).append(author)
				.append("@").append(request.getServerName()).toString();
	}

	String getPublishedDate() {
		return publishedDate;
	}

	String getNodePath() {
		LOGGER.debug("getNodePath || nodePath || "+nodePath);
		return nodePath;
	}

	String getUrlPrefix() {
		StringBuffer url = new StringBuffer();
		url.append(request.getScheme());
		url.append("://");
		url.append(request.getServerName());
		if (request.getServerPort() != 80) {
			url.append(":");
			url.append(request.getServerPort());
		}
		url.append(request.getContextPath());
		return url.toString();
	}

	String getHtmlLink() {
		return (new StringBuilder()).append(getUrlPrefix())
				.append(getNodePath()).append(".html").toString();
	}

	String getFeedLink() {
		return (new StringBuilder()).append(getUrlPrefix())
				.append(getNodePath()).append(".customfeed.xml").toString();
	}

	String getFeedEntryLink() {
		return (new StringBuilder()).append(getUrlPrefix())
				.append(getNodePath()).append(".feedentry.xml").toString();
	}

	String getCommentsLink() {
		return (new StringBuilder()).append(getUrlPrefix())
				.append(getNodePath()).append(".html#comments").toString();
	}

	String getFileLink() {
		return (new StringBuilder()).append(getUrlPrefix())
				.append(getNodePath()).toString();
	}

	String getBaseUrl() {
		return baseUrl;
	}

	String getGeneratorName() {
		return "CQ5";
	}

	String getGeneratorVersion() {
		return "5.2";
	}

	String getGeneratorLink() {
		return "http://www.day.com/communique";
	}
	
	String[] getProperties() {
		return properties;
	}

	static final String textNodes[];
	public SimpleXml xml;
	SlingHttpServletRequest request;
	SlingHttpServletResponse response;
	String title;
	String description;
	String tags;
	String language;
	String author;
	String publishedDate;
	String nodePath;
	String baseUrl;
	long fileSize;
	String mimeType;
	String[] properties;

	static {
		textNodes = new String[3];
		textNodes[0] = "par/text";
		textNodes[1] = "par/textimage";
		textNodes[2] = "blogentry";
	}
	
	protected String[] getAllProperties() {

		String methodName = "getAllProperties";
		LOGGER.info(" || " + methodName + " || START");

		ResourceResolver resourceResolver = request.getResourceResolver();
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Resource resource = request.getResource();
		String pagePath = resource.getPath();

		Page page = pageManager.getPage(pagePath);
		Node node = page.adaptTo(Node.class);
		String[] propVals = null;
		try {
			Node jcrNode = node.getNode("jcr:content");
			LOGGER.info(" || " + methodName + " || jcrNode || " + jcrNode);
			NodeIterator nodes = jcrNode.getNodes();
	
			while (nodes.hasNext()) {
				Node parNode = nodes.nextNode();
				LOGGER.info(" || " + methodName + " || parNode || " + parNode);
				String resourceType = parNode.getProperty("sling:resourceType")
						.getString();
				if (resourceType.equals("foundation/components/parsys")
						|| resourceType.equals("foundation/components/iparsys")) {
					NodeIterator compNodes = parNode.getNodes();
					while (compNodes.hasNext()) {
						Node compNode = compNodes.nextNode();
						resourceType = compNode.getProperty("sling:resourceType")
								.getString();
						LOGGER.info(" || " + methodName + " || resourceType || "
								+ resourceType);
						if (resourceType
								.equalsIgnoreCase("solution-accelerators/components/global/content/feedlink")
								&& compNode.hasProperty("feedFields")) {
							Value[] values = compNode.getProperty("feedFields")
									.getValues();
							LOGGER.info(" || " + methodName + " || values || "
									+ values);
							int i = 0;
							if (null != values) {
								propVals = new String[values.length];
								for (Value value : values) {
									propVals[i++] = value.getString();
									LOGGER.info(" || " + methodName
											+ " || value || " + value.getString());
								}
							}
						}
					}
				}
			}
		} catch (PathNotFoundException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "+e.getMessage());
		} catch (RepositoryException e) {
			LOGGER.error(" || " + methodName + " || EXCEPTION || "+e.getMessage());
		}

		LOGGER.info(" || " + methodName + " || END");
		return propVals;
	}
	
	protected void setProperties(String[] allProps) {
		String methodName = "setProperties";
		LOGGER.info(" || " + methodName + " || START");
		String node = getNodePath();
		LOGGER.info(" || " + methodName + " || node || "+node);
		String nodeName = node.substring(node.lastIndexOf("/"), node.length());
		LOGGER.info(" || " + methodName + " || nodeName || "+nodeName);
		LOGGER.info(" || " + methodName + " || allProps || "+allProps.length);
		
		int i = 0;
		for (String props : allProps) {
			LOGGER.info(" || " + methodName + " || props || "+props);
			String[] propsTemp = props.split(",");
			properties = new String[propsTemp.length];
			for (String prop : propsTemp) {
				if (prop.contains(nodeName)) {
					LOGGER.info(" || " + methodName + " || prop contains || "
							+ prop);
					properties[i] = prop;
					i++;
				}
			}
		}
		LOGGER.info(" || " + methodName + " || properties || "+properties);
		LOGGER.info(" || " + methodName + " || END");
	}

	public void printHeader() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void printEntry() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
