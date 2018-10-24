package com.cts.accelerators.lifeplus.feed;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;

// Referenced classes of package com.day.cq.commons.feed:
//            AbstractFeed

public class CustomRssFeed extends CustomAbstractFeed {

	public CustomRssFeed(SlingHttpServletRequest req,
			SlingHttpServletResponse resp) throws RepositoryException {
		super(null, req, resp);
	}

	public CustomRssFeed(Resource res, SlingHttpServletRequest req,
			SlingHttpServletResponse resp) throws RepositoryException {
		super(res, req, resp);
	}

	public void printHeader() throws IOException {
		initXml();
		xml.openDocument();
		xml.open("rss").attr("version", "2.0")
				.attr("xmlns:atom", "http://www.w3.org/2005/Atom")
				.open("channel").open("link", getHtmlLink(), false).close()
				.open("atom:link").attr("rel", "self")
				.attr("href", getHtmlLink()).close()
				.open("title", getTitle(), false).close()
				.open("description", getDescription(), false).close()
				.open("pubDate", getPublishedDate(), false).close()
				.open("generator", getGeneratorLink(), false).close();
		if (!"".equals(getLanguage()))
			xml.open("language", getLanguage(), false).close();
	}

	public void printEntry() throws IOException {
		String link = isFile() ? getFileLink() : getHtmlLink();
		String comments = isFile() ? "" : getCommentsLink();
		initXml();
		xml.omitXmlDeclaration(true);
		xml.open("item").open("link", link, false).close()
				.open("comments", comments, false).close()
				.open("guid", getFeedLink(), false).close()
				.open("title", getTitle(), false).close()
				.open("description", getDescription(), true).close()
				.open("pubDate", getPublishedDate(), false).close()
				.open("category", getTags(), false).close();
		if (!"".equals(getAuthorEmail()))
			xml.open("author", getAuthorEmail(), false).close();
		xml.tidyUp();
	}

	public String getContentType() {
		return "application/rss+xml";
	}

	String getFeedEntryPath(Resource res) {
		return (new StringBuilder()).append(res.getPath()).append(".")
				.append("feedentry").append(".").append("rss").append(".xml")
				.toString();
	}

	protected String getFeedLink() {
		return (new StringBuilder()).append(getUrlPrefix())
				.append(getNodePath()).append(".").append("customfeed").append(".")
				.append("rss").append(".xml").toString();
	}

	protected String formatDate(Calendar calendar) {
		try {
			return (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"))
					.format(calendar.getTime());
		} catch (Exception e) {
			return "";
		}

	}
}
