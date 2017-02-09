package com.cts.store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.jcr.PropertyType;
import javax.jcr.PropertyIterator;
import javax.jcr.Node;
import javax.jcr.Value;
import javax.jcr.Session;
import javax.jcr.NodeIterator;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Query;

import org.apache.commons.lang.StringEscapeUtils;


import com.day.cq.commons.JSONWriterUtil;

@Component(immediate = true, metatype = true)
@Service(Servlet.class)
@Properties({
	@Property(name = "service.name", value = "Search Suggestions Service"),
	@Property(name = "service.vendor", value = "Cognizant"),
	@Property(name = "sling.servlet.paths", value = "/bin/suggest"),
	@Property(name = "sling.servlet.methods", value = "GET", propertyPrivate = true)
})

@SuppressWarnings("serial")

public class SuggestionsService extends SlingAllMethodsServlet {

	/**  serialVersionUID. */
	private static final long serialVersionUID = 92417369592431234L;
	private static Logger logger;
	private static PrintWriter out;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	protected void doGet(SlingHttpServletRequest request,
			final SlingHttpServletResponse response)
	throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(SlingHttpServletRequest request,
			final SlingHttpServletResponse response){
		try{
			out = response.getWriter();
			//response.setContentType("text/json");
			logger = LoggerFactory.getLogger("SuggestionsService");
			Session session = request.getResourceResolver().adaptTo(Session.class);
			QueryManager qm = session.getWorkspace().getQueryManager();
			String keyword = request.getParameter("query");
			String path = request.getParameter("path");
			int maxSuggestions = Integer.parseInt(request.getParameter("max"));
			if(keyword!=null && !keyword.equals("")){
				Query query = qm.createQuery("/jcr:root"+path+"/*[jcr:contains(., '"+keyword+"')] order by @jcr:score descending",Query.XPATH);
				QueryResult queryResult = query.execute();
				NodeIterator nodes = queryResult.getNodes();
				logger.error("PATH : "+path);
				logger.error("LEN = "+nodes.getSize());
				int len = 0;
				Set<String> results = new TreeSet<String>();
				while(nodes.hasNext()){
					Node searchResultNode = nodes.nextNode();
					PropertyIterator propIter = searchResultNode.getProperties();
					while(propIter.hasNext()){
						javax.jcr.Property prop = propIter.nextProperty();
						if(searchResultNode.hasProperty(prop.getName())){
							String propValue = ""; 
							if(prop.getType()==PropertyType.STRING){
								if(prop.isMultiple()){
									Value[] values = prop.getValues();
									for(int valueindex=0;valueindex<values.length;valueindex++){
										Value val = values[valueindex];
										if(val.getString().contains(keyword)){
											propValue = val.getString();
											break;
										}
									}
								}
								else{
									if(prop.getString().contains(keyword)){
										propValue = prop.getString();
									}
								}
							}
							else if(prop.getType()==PropertyType.LONG){
								if(!prop.isMultiple()){
									propValue = prop.getLong()+"";
								}
							}

							if(!propValue.equals("") && propValue.contains(keyword)){
								String sPattern = "(?i)\\b\\w*" + Pattern.quote(keyword) + "\\w*\\b";
								Matcher matcher = Pattern.compile(sPattern).matcher(propValue);
								if(matcher.find()){
									String value = matcher.group();
									if(!value.equals("")){
										results.add(value);
										len++;
									}
								}
							}
						}
					}
					if(len>maxSuggestions){
						break;
					}
				}
				results.remove("");
				logger.error("RESULTS OF SEARCH : "+len);
				ArrayList<String> resultsList = new ArrayList(results);
				Comparator<String> comp = new Comparator<String>(){
					public int compare(String s1, String s2){
						return s1.length()-s2.length();
					}
				};
				Collections.sort(resultsList,comp);
				JSONArray arr = new JSONArray();

				int size = (resultsList.size()>maxSuggestions)?maxSuggestions:resultsList.size();
				for(int i = 0 ; i < size; i++){

					JSONObject obj1 = new JSONObject();
					obj1.put("id", i+1);
					obj1.put("data", new String(StringEscapeUtils.escapeJavaScript(resultsList.get(i)).getBytes("UTF-8")));
					obj1.put("thumbnail", "");
					obj1.put("description", new String(StringEscapeUtils.escapeJavaScript(resultsList.get(i)).getBytes("UTF-8")));
					arr.put(obj1);
				}

				out.println(arr);
				for(int i=0;i<arr.length();i++){
					out.println(arr.getString(i));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}