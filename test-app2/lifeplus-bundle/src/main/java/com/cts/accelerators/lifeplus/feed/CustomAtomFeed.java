package com.cts.accelerators.lifeplus.feed;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.accelerators.lifeplus.controllers.CustomFeedRenderServlet;

public class CustomAtomFeed extends CustomAbstractFeed {
	
	private final static Logger LOGGER = LoggerFactory
			.getLogger(CustomAtomFeed.class);
	
	public CustomAtomFeed(SlingHttpServletRequest req, SlingHttpServletResponse resp)
			throws RepositoryException {
		super(null, req, resp);
	}

	public CustomAtomFeed(Resource res, SlingHttpServletRequest req,
			SlingHttpServletResponse resp) throws RepositoryException {
		super(res, req, resp);
	}

	public void printHeader() throws IOException {
		initXml();
		xml.openDocument();
		com.day.cq.commons.SimpleXml.Element feed = xml.open("feed");
		feed.attr("xmlns", "http://www.w3.org/2005/Atom");
		if (!"".equals(getLanguage()))
			feed.attr("xml:lang", getLanguage());
		xml.open("title", getTitle(), false).attr("type", "html").close();
		xml.open("id", getFeedLink(), false).close();
		xml.open("link").attr("rel", "alternate").attr("type", "text/html")
				.attr("href", getHtmlLink()).close();
		xml.open("link").attr("rel", "self").attr("type", getContentType())
				.attr("href", getFeedLink()).close();
		xml.open("updated", getPublishedDate(), false).close();
		xml.open("generator").attr("uri", getGeneratorLink())
				.attr("version", getGeneratorVersion())
				.text(getGeneratorName()).close();
	}

	public void printEntry() throws IOException {
		String methodName = "atom:printEntry";
		LOGGER.info("|| "+methodName+" || START");
		initXml();
		xml.omitXmlDeclaration(true);
		xml.open("entry");
		xml.open("title", getTitle(), false).attr("type", "html").close();
		xml.open("summary", getSummary(), false).attr("type", "html").close();
		xml.open("author");
		xml.open("name", getAuthorName(), false).close();
		if (!"".equals(getAuthorName()))
			xml.open("email", getAuthorEmail(), false).close();
		xml.close();
		xml.open("id", getFeedLink(), false).close();
		xml.open("updated", getPublishedDate(), false).close();
		xml.open("published", getPublishedDate(), false).close();
		xml.open("category").attr("term", getTags()).close();
		if (isFile()) {
			xml.open("link").attr("rel", "alternate")
					.attr("type", getMimeType()).attr("href", getFileLink())
					.close();
			xml.open("link").attr("rel", "enclosure")
					.attr("type", getMimeType()).attr("length", getFileSize())
					.attr("href", getFileLink()).close();
		} else {
			xml.open("link").attr("rel", "alternate")
					.attr("type", getMimeType()).attr("href", getHtmlLink())
					.close();
			xml.open("link").attr("rel", "replies").attr("type", getMimeType())
					.attr("href", getCommentsLink()).close();
			xml.open("content").attr("xml:base", getBaseUrl())
					.attr("type", "html").text(getDescription(), false).close();
		}
		
		xml.tidyUp();
	}

	public String getContentType() {
		return "application/atom+xml";
	}

}
