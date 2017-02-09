package com.cts.store;

/**

Cognizant Technology solutions  
 */

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import javax.jcr.Node;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;

/**
* @author Cognizant Technology solutions
* 
 */
@Component(immediate = true, metatype = true)
@Service(Servlet.class)
@Properties( {
                                @Property(name = "service.description", value = "Product Servlet "),
                                @Property(name = "service.vendor", value = "Cognizant"),

                                @Property(name = "sling.servlet.paths", value = "/bin/productList"),

                                // Generic handler for all get requests
                                @Property(name = "sling.servlet.methods", value = "GET", propertyPrivate = true) })
@SuppressWarnings("serial")
public class ProductJsonServlet extends SlingAllMethodsServlet {

                Logger log = LoggerFactory.getLogger("ProductJsonServlet");
                /** serialVersionUID. */
                private static final long serialVersionUID = 92417369592432728L;

                /** Logger instance. */
                private static final Logger LOG = LoggerFactory
                                                .getLogger(ProductJsonServlet.class);

                @Override
                public final void init() throws ServletException {
                                super.init();
                }

                /** resolver. */
                private ResourceResolver resolver = null;

                /** session. */
                private Session session = null;

                /**
                * helper Object
                */

                /**
                * @return
                */
                public static Logger getLog() {
                                return LOG;
                }

                /*
                * (non-Javadoc)
                * 
                 * @see
                * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache
                * .sling.api.SlingHttpServletRequest,
                * org.apache.sling.api.SlingHttpServletResponse)
                */
                protected final void doGet(final SlingHttpServletRequest slingRequest,
                                                final SlingHttpServletResponse response) throws ServletException,
                                                IOException {
                                String category = "";
                                if (null != slingRequest.getParameter("category")) {
                                                category = (String) slingRequest.getParameter("category");
                                }
                                LOG.info("Category" + category);
                                JSONObject jsonObj = new JSONObject();
                                try {

                                                JSONArray jsonArr = new JSONArray();

                                                // String category = slingRequest.getParameter("category");

                                                LOG.error("Inside TRY block productdata json ");
                                                String delim = "";

                                                NodeIterator scripts = slingRequest.getResourceResolver()
                                                                                .getResource(category).adaptTo(Node.class).getNodes();
                                                String prodCode = "";
                                                String prodTitle = "";
                                                while (scripts.hasNext()) {

                                                                Node script = scripts.nextNode();
                                                                String name = script.getName();
                                                                if (name.equalsIgnoreCase("jcr:content")) {
                                                                                NodeIterator scriptNodeIter = script.getNodes();
                                                                                while (scriptNodeIter.hasNext()) {
                                                                                                Node scriptNode = scriptNodeIter.nextNode();
                                                                                                // null check
                                                                                                if (scriptNode
                                                                                                                                .hasProperty("sling:resourceType")
                                                                                                                                && scriptNode.getProperty("sling:resourceType")
                                                                                                                                                                .getValue().getString().equals(
                                                                                                                                                                                                "foundation/components/parsys")) {
                                                                                                                NodeIterator parNodeIter = scriptNode.getNodes();
                                                                                                                while (parNodeIter.hasNext()) {

                                                                                                                                Node productNode = parNodeIter.nextNode();

                                                                                                                                if (productNode.hasProperty("sling:resourceType")
                                                                                                                                                                && productNode
                                                                                                                                                                                                .getProperty(
                                                                                                                                                                                                                                "sling:resourceType")
                                                                                                                                                                                                .getValue()
                                                                                                                                                                                                .getString()
                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                "ebazaarresp/components/content/productdetails")) {
                                                                                                                                                if (productNode.hasProperty("prodCode")
                                                                                                                                                                                && productNode
                                                                                                                                                                                                                .hasProperty("prodTitle")) {
                                                                                                                                                                Value codes = productNode.getProperty(
                                                                                                                                                                                                "prodCode").getValue();
                                                                                                                                                                prodCode = codes.getString();
                                                                                                                                                                Value title = productNode.getProperty(
                                                                                                                                                                                                "prodTitle").getValue();
                                                                                                                                                                prodTitle = title.getString();

                                                                                                                                                                if (!prodCode.isEmpty()
                                                                                                                                                                                                && (prodCode != "")
                                                                                                                                                                                                && !prodTitle.isEmpty()
                                                                                                                                                                                                && (prodTitle != "")) {
                                                                                                                                                                                LOG
                                                                                                                                                                                                                .error("Inside prod code and PROD TITLE >>>"
                                                                                                                                                                                                                                                + prodCode
                                                                                                                                                                                                                                                + prodTitle);
                                                                                                                                                                                JSONObject items = new JSONObject();

                                                                                                                                                                                items.putOpt("text", prodTitle);
                                                                                                                                                                                items.putOpt("value", prodCode);
                                                                                                                                                                                LOG
                                                                                                                                                                                .error("ITEMS PUTOPTS >>>"
                                                                                                                                                                                                                + items.get("text")
                                                                                                                                                                                                                + items.get("value"));
                                                                                                                                                                                jsonArr.put(items);
                                                                                                                                                                                LOG
                                                                                                                                                                                .error("After json error >>>"
                                                                                                                                                                                                                + jsonArr);

                                                                                                                                                                }
                                                                                                                                                } else {
                                                                                                                                                                break;
                                                                                                                                                }
                                                                                                                                }
                                                                                                                }

                                                                                                }
                                                                                }
                                                                }
                                                }
                                                LOG
                                                .error("OUTSIDE block >>>"
                                                                                + jsonArr);
                                                jsonObj.put("items", jsonArr);
                                                LOG.info("items :::   " + jsonObj.getJSONArray("items"));
                                                LOG.info("response" + response);
                                                response.setStatus(HttpServletResponse.SC_OK);
                                                response.setContentType("application/json");
                                                response.getWriter().println(jsonObj.toString());
                                                LOG.info("after writer" + response.getWriter());
                                                // session.save();
                                                // flushResponse(response, jsonObj);

                                } catch (RepositoryException e) {LOG.error("catch exception" + e);

                                } catch (Exception ex) {
                                                LOG.error("catch exception" + ex);
                                                flushResponse(response, new JSONObject());
                                }

                }

                /**
                * Generates error response
                * 
                 * @param slingHttpServletResponse
                *            - Servlet Response
                * @param errorMsg
                *            - error msg
                * @throws IOException
                *             - IO exception
                */
                public final void flushResponse(
                                                final SlingHttpServletResponse slingHttpServletResponse,
                                                JSONObject jsonObj) throws IOException {
                                try {

                                                if (jsonObj.has("items")) {
                                                                slingHttpServletResponse.setStatus(HttpServletResponse.SC_OK);
                                                                slingHttpServletResponse.setContentType("application/json");
                                                                slingHttpServletResponse.getWriter()
                                                                                                .println(jsonObj.toString());
                                                                LOG.error(" Transferred JSON object");
                                                } else {
                                                                LOG.error(" ELSEEEE no jsonObj JSON object");

                                                                slingHttpServletResponse
                                                                                                .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                                                slingHttpServletResponse.setContentType("application/json");
                                                }

                                } catch (final Exception ex) {
                                                slingHttpServletResponse
                                                                                .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                                slingHttpServletResponse.setContentType("application/json");
                                                LOG.debug(" Flush error method catch");
                                                LOG.error("Error: " + ex.getMessage());
                                }

                }

}
