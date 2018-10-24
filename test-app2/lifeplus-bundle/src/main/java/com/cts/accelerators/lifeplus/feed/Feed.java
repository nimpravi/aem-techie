package com.cts.accelerators.lifeplus.feed;

import java.io.IOException;
import java.util.Iterator;
import org.apache.sling.api.resource.Resource;

public interface Feed
{

    public abstract String getContentType();

    public abstract String getCharacterEncoding();

    public abstract void printHeader()
        throws IOException;

    public abstract void printEntry()
        throws IOException;

    public abstract void printEntry(Resource resource)
        throws IOException;

    public abstract void printChildEntries()
        throws IOException;

    public abstract void printChildEntries(int i)
        throws IOException;

    public abstract void printEntries(Iterator iterator)
        throws IOException;

    public abstract void printEntries(Iterator iterator, int i)
        throws IOException;

    public abstract void printFooter()
        throws IOException;

    public static final String DEFAULT_CONTENT_TYPE = "text/xml";
    public static final String DEFAULT_CHARACTER_ENCODING = "utf-8";
    public static final String SELECTOR_FEED = "feed";
    public static final String SELECTOR_FEEDENTRY = "feedentry";
    public static final String SELECTOR_ATOM = "atom";
    public static final String SELECTOR_RSS = "rss";
    public static final String SUFFIX_HTML = ".html";
    public static final String SUFFIX_XML = ".xml";
    public static final String SUFFIX_FEED = ".feed.xml";
    public static final String SUFFIX_FEEDENTRY = ".feedentry.xml";
    public static final String SUFFIX_COMMENTS = ".html#comments";
}