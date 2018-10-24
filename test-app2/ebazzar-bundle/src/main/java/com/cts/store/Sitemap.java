package com.cts.store;
//import java.io.BufferedInputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
import java.util.Iterator;
//import java.util.List;
//import java.util.StringTokenizer;
import javax.jcr.Node;
//import javax.jcr.Property;
//import javax.jcr.Session;
//import javax.jcr.Value;
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import com.abbvie.cq.exception.AbbVieException;
//import com.abbvie.cq.models.NewsResult;
//import com.abbvie.cq.util.Constants;
//import com.abbvie.cq.util.AbbvieUtil;
import com.day.cq.wcm.api.Page;
//import com.day.cq.wcm.api.PageFilter;
//import com.day.cq.wcm.api.PageManager;
import java.util.LinkedList;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.apache.sling.api.SlingHttpServletRequest;
//TODO comment
public class Sitemap {

	private static final Logger LOG = LoggerFactory
			.getLogger(Sitemap.class);

	private LinkedList<Link> links = new LinkedList<Link>();
	Node jcrNode = null;
	public Sitemap(Page rootPage) {
		buildLinkAndChildren(rootPage, 1);
	}
	public boolean isHiddenInSiteMap(Node jcrNode) throws Exception {
		return jcrNode.hasProperty("hideInSiteMap") ? true : false;
	}
	private void buildLinkAndChildren(Page page, int level) {
     		if (page!= null) {
     			try{
                    String title = page.getTitle();
                    if (title == null) title = page.getName();
                    if(level >1){
                    	links.add(new Link(page.getPath(), title, level));
                    } 
                    Iterator<Page> children = page.listChildren();
                    while (children.hasNext()) {
                        Page child = children.next();
                        jcrNode = child.getContentResource().adaptTo(Node.class);
                        if(null != jcrNode){
                        	if(!isHiddenInSiteMap(jcrNode)){
                        		buildLinkAndChildren(child,level+1);
                        	}
                        }
                    }
                }catch(Exception e){
                	LOG.error("Exception in site map",e);
                }
     		}
	}
        public class Link {
            private String path;
            private String title;
            private int level;

            public Link(String path, String title, int level) {
                this.path = path;
                this.title = title;
                this.level = level;
            }

            public String getPath() {
                return path;
            }

            public int getLevel() {
                return level;
            }

            public String getTitle() {
                return title;
            }
        }

	public void draw(Writer w) throws IOException {
		PrintWriter out = new PrintWriter(w);

		int previousLevel = -1;

		for (Link aLink : links) {

			if (aLink.getLevel() > previousLevel) {
				out.println("<ul>");

			}

			else if (aLink.getLevel() < previousLevel) {

				for (int i = aLink.getLevel(); i < previousLevel; i++)
					out.println("</li></ul>");

			}

			out.printf("<li><a href=\"%s.html\">%s</a>", (aLink.getPath()),
					aLink.getTitle());

			previousLevel = aLink.getLevel();

		}

		for (int i = -1; i < previousLevel; i++)
			out.print("</li>");

	}

	public LinkedList<Link> getLinks() {
		return links;
	}
}
