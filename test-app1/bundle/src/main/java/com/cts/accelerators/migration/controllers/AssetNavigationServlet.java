package com.cts.accelerators.migration.controllers;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@SlingServlet(paths = "/bin/AssetNavigationServlet", methods = "POST", metatype = false)
public class AssetNavigationServlet extends SlingAllMethodsServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AssetNavigationServlet.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServerException,
			IOException {
		LOGGER.info("doGet || MyServlet || Redirecting to doPost");
		doPost(request, response);
	}

	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) {
		String pagePath = request.getParameter("pagePath");
		JSONArray jSONArray=new JSONArray();
		PageManager pageManager =request.getResource().getResourceResolver().adaptTo(
					PageManager.class);
		Page rootPage = pageManager.getPage(pagePath);
			
			if (rootPage != null) {
				Iterator<Page> children = rootPage.listChildren();
				
				while (children.hasNext()) {
					Map<String,String> jSONObject = new HashMap<String,String>();
					Page child = children.next();
					String title = child.getTitle() == null ? child.getName()
							: child.getTitle();
					String childNodePath = child.getPath();
					jSONObject.put("value", childNodePath);
					jSONObject.put("text", title);
					jSONArray.put(jSONObject);
				}
			}
		try {
			response.getWriter().write(jSONArray.toString());
		} catch (IOException e) {
			
		}
	}
}