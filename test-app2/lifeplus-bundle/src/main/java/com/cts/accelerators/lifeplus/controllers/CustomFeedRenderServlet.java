package com.cts.accelerators.lifeplus.controllers;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.feed.CustomAtomFeed;
import com.cts.accelerators.lifeplus.feed.CustomRssFeed;
import com.cts.accelerators.lifeplus.feed.Feed;

@SlingServlet(selectors = { "customfeeds", "feedentry" }, resourceTypes = "sling/servlet/default", metatype = false, generateService = true)
public class CustomFeedRenderServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 5406752739629457297L;
	private final static Logger LOGGER = LoggerFactory
			.getLogger(CustomFeedRenderServlet.class);
	private final static String CLASS_NAME = CustomFeedRenderServlet.class
			.getName();

	/*
	 * @Override protected void doGet(SlingHttpServletRequest slingRequest,
	 * SlingHttpServletResponse slingResponse) throws IOException { String
	 * methodName = "doPost"; LOGGER.info(" || " + methodName + " || START");
	 * 
	 * LOGGER.info(" || " + methodName +
	 * " || request || "+slingRequest.getParameter("requestType")); String xml =
	 * feedLink(slingRequest, slingResponse);
	 * slingResponse.getWriter().write(xml);
	 * 
	 * LOGGER.info(" || " + methodName + " || END");
	 * 
	 * }
	 * 
	 * private String feedLink(SlingHttpServletRequest slingRequest,
	 * SlingHttpServletResponse slingResponse) { String methodName = "feedTest";
	 * LOGGER.info(" || " + methodName + " || START");
	 * 
	 * ResourceResolver resourceResolver = slingRequest.getResourceResolver();
	 * String xml = "";
	 * 
	 * try { Session jcrSession = ConnectionManager.getSession();
	 * LOGGER.debug(" || " + methodName + " || jcrSession || "+jcrSession);
	 * 
	 * String format = slingRequest.getParameter("format"); String feedFields =
	 * slingRequest.getParameter("feedFields");
	 * 
	 * LOGGER.debug(" || " + methodName + " || format || "+format);
	 * LOGGER.debug(" || " + methodName + " || feedFields || "+feedFields);
	 * 
	 * Resource resource =
	 * resourceResolver.resolve(slingRequest.getParameter("listroot"));
	 * if(format.equalsIgnoreCase("atom")) { LOGGER.debug(" || " + methodName +
	 * " || atom"); CustomAtomFeed atom = new CustomAtomFeed(resource,
	 * slingRequest, slingResponse); atom.printHeader(); atom.printEntry(); xml
	 * = atom.xml.toString(); } else { LOGGER.debug(" || " + methodName +
	 * " || rss"); CustomRssFeed rss = new CustomRssFeed(resource, slingRequest,
	 * slingResponse); rss.printHeader(); rss.printEntry(); xml =
	 * rss.xml.toString(); } LOGGER.debug(" || " + methodName +
	 * " || xml || "+xml);
	 * 
	 * } catch (Exception e) { LOGGER.error(" || " + methodName +
	 * " || EXCEPTION OCCURED || "+e.getMessage()); } LOGGER.info(" || " +
	 * methodName + " || END"); return xml; }
	 */

	public CustomFeedRenderServlet() {
	}

	protected void doGet(SlingHttpServletRequest req,
			SlingHttpServletResponse resp) throws ServletException, IOException {
		String methodName = "doGet";
		LOGGER.info("|| "+methodName+" || START");
		String sels[] = req.getRequestPathInfo().getSelectors();
		boolean isEntry = req.getAttribute("noinclude") == null
				&& req.getAttribute("org.apache.sling.api.include.servlet") != null
				|| sels.length > 0 && "feedentry".equals(sels[0]);
		int maxCount = 30;
		try {
			Feed feed = getFeed(req, resp);
			resp.setContentType(feed.getContentType());
			resp.setCharacterEncoding(feed.getCharacterEncoding());
			if (isEntry) {
				feed.printEntry();
			} else {
				feed.printHeader();
				feed.printChildEntries(maxCount);
				feed.printFooter();
			}
		} catch (Exception e) {
			LOGGER.error(" || EXCEPTION || "+e.getMessage());
			throw new ServletException((new StringBuilder())
					.append("Error while rendering resource as feed: ")
					.append(e.getMessage()).toString());
		}
		LOGGER.info("|| "+methodName+" || END");
	}

	protected Feed getFeed(SlingHttpServletRequest req,
			SlingHttpServletResponse resp) throws RepositoryException {
		String methodName = "getFeed";
		LOGGER.info("|| "+methodName+" || START");
		String sels[] = req.getRequestPathInfo().getSelectors();
		
		if (sels.length > 1 && "rss".equals(sels[1]))
			return (Feed) new CustomRssFeed(req, resp);
		else
			return (Feed) new CustomAtomFeed(req, resp);
	}
	
}
