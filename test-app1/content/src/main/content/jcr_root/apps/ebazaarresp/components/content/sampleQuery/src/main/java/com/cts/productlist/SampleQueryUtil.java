package com.cts.productlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

public class SampleQueryUtil {
	protected final static Logger log = LoggerFactory.getLogger(SampleQueryUtil.class);

	/*
	 * Fetch the top 3 products
	 */
	public List<String> fetchLinks(Session session, String queryPath, QueryBuilder queryBuilder){
		List<String> linkList = new ArrayList<String>();
		
		try{
			
			Map<String, String> queryMap = new HashMap<String, String>();
	        queryMap.put("path", queryPath);
	        queryMap.put("type", "nt:unstructured");
	        queryMap.put("property", "viewCount");
	        queryMap.put("property.value", "0");
	        queryMap.put("property.operation", "unequals");
	        queryMap.put("orderby", "property");
            queryMap.put("orderby.sort", "desc");
	        queryMap.put("p.limit", "3");
	        
	        Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
	        if(query != null){
	        	System.out.println("query is ----"+query.toString());
	        }else{
	        	System.out.println("query is null");
	        }
	        SearchResult searchResults = query.getResult();
	        Iterator<Node> resultNodes = searchResults.getNodes();
	        
	        System.out.println("");
	        while(resultNodes.hasNext()){
                Node resultNode = resultNodes.next();
                String linkPath[] = resultNode.getPath().split("jcr:content");
                String prodlink=linkPath[0].substring(0,linkPath[0].length()-1)+".html"+"?"+resultNode.getProperty("prodCode").getName()+"="+resultNode.getProperty("prodCode").getValue().getString();
                String image=resultNode.getProperty("fileReference").getValue().getString();
                String pageLink = "<a href='"+prodlink+"' target='_blank'>"+"<img src='"+image+"'width='173px' height ='120px'/>"+"</a>";
                linkList.add(pageLink);
                
            }
		}catch(Exception ex){
			System.out.println("Exception in SampleQueryUtil is: "+ex);
			
		}
		
		return linkList;
	}
/*
 * Fetch the drop down values
 */
	public List<String> fetchProdType(Session session, String queryPath, QueryBuilder queryBuilder){
		List<String> linkList = new ArrayList<String>();
		
		try{
			
			Map<String, String> queryMap = new HashMap<String, String>();
	        queryMap.put("path", queryPath);
	        queryMap.put("type", "nt:unstructured");
	        queryMap.put("property", "prodCategories");
	        queryMap.put("property.operation","exists");
	        queryMap.put("p.limit", "-1");
	        
	        Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
	        if(query != null){
	        	System.out.println("query is ----"+query.toString());
	        }else{
	        	System.out.println("query is null");
	        }
	        SearchResult searchResults = query.getResult();
	        Iterator<Node> resultNodes = searchResults.getNodes();
	        String prodCategories=null;
	       
	        while(resultNodes.hasNext()){
                Node resultNode = resultNodes.next();
                if(resultNode != null && resultNode.hasProperty("prodCategories")){
                	prodCategories=resultNode.getProperty("prodCategories").getValue().getString();
                	//log.info("prodCategories::::::::::::"+prodCategories);
                }
                if(!prodCategories.equalsIgnoreCase("null")){
                linkList.add(prodCategories);
                }
                HashSet<String> hs = new HashSet<String>();
                hs.addAll(linkList);
                linkList.clear();
                linkList.addAll(hs);
                
            }
		}catch(Exception ex){
			System.out.println("Exception in SampleQueryUtil is: "+ex);
			
		}
		
		return linkList;
	}
	
	/*
	 * Get the products based on the product type-fetchProdTypeLinks
	 */
	public List<String> fetchProdTypeLinks(Session session, String queryPath, QueryBuilder queryBuilder, String prodTypeSelected){
		List<String> linkList = new ArrayList<String>();
		
		try{
			
			Map<String, String> queryMap = new HashMap<String, String>();
			
	        queryMap.put("path", queryPath);
	        queryMap.put("type", "nt:unstructured");
	        queryMap.put("1_property", "prodCategories");
	        queryMap.put("1_property.value", prodTypeSelected);
	        queryMap.put("1_property.operation","equals");
	        queryMap.put("2_property", "viewCount");
	        queryMap.put("2_property.value", "0");
	        queryMap.put("2_property.operation", "unequals");
	        queryMap.put("orderby", "2_property");
            queryMap.put("orderby.sort", "desc"); 
	        queryMap.put("p.limit", "3");
	        
	        Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
	        if(query != null){
	        	//log.info("query is ----"+query.toString()+"</br>:"+query.getResult().getQueryStatement());
	        }else{
	        	log.info("query is null");
	        }
	        SearchResult searchResults = query.getResult(); 
	        Iterator<Node> resultNodes = searchResults.getNodes();
	        while(resultNodes.hasNext()){
                Node resultNode = resultNodes.next();
                String linkPath[] = resultNode.getPath().split("jcr:content");
                String prodlink=linkPath[0].substring(0,linkPath[0].length()-1)+".html"+"?"+resultNode.getProperty("prodCode").getName()+"="+resultNode.getProperty("prodCode").getValue().getString();
                String image=resultNode.getProperty("fileReference").getValue().getString();
                String pageLink = "<a href='"+prodlink+"' target='_blank'>"+"<img src='"+image+"'width='173px' height ='120px'/>"+"</a>";
                linkList.add(pageLink);
                
            }
		}catch(Exception ex){
			System.out.println("Exception in SampleQueryUtil is: "+ex);
			
		}
		
		return linkList;
	}
	
}
