<%@include file="/libs/foundation/global.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="javax.jcr.query.QueryResult"%>
<%@page import="javax.jcr.query.Query"%>
<%@page import="javax.jcr.query.QueryManager"%>
<%@page import="com.cts.productlist.*"%>
<%@page import="com.day.cq.search.QueryBuilder"%>
<% 
  
	String queryPath = properties.get("queryPath","");
	String prodTypeSelected = properties.get("displayAs", "");

	
	    if(queryPath !=null && !queryPath.equalsIgnoreCase("")){
			      
			Session session           = currentNode.getSession();
		    SampleQueryUtil sampleQueryUtil     = new SampleQueryUtil();
		    QueryBuilder queryBuilder           = sling.getService(QueryBuilder.class);
		    
		    if(prodTypeSelected !=null && !prodTypeSelected.equalsIgnoreCase("")){
		    	List<String> prodTypelinkList               = sampleQueryUtil.fetchProdTypeLinks(session, queryPath, queryBuilder, prodTypeSelected);
		    	if(prodTypelinkList != null && prodTypelinkList.size()>0){ 
		            out.println("<br/><b><h3>Top Three Most Viewed Products Types: </h3></b><br/>");
		            for(int i=0; i<prodTypelinkList.size(); i++){
		                out.println(prodTypelinkList.get(i)+"<br/>");
		            }
		        }else{
		            out.println("<br/><b><h3>No Products Available Based on Product Type Selection</h3></b><br/>");
		        }
		    }else{   
				try{     
					
			        List<String> linkList               = sampleQueryUtil.fetchLinks(session, queryPath, queryBuilder);
			        
			        if(linkList != null && linkList.size()>0){
			            out.println("<br/><b><h3>Top Three Most Viewed Products: </h3></b><br/>");
			            for(int i=0; i<linkList.size(); i++){
			                out.println(linkList.get(i)+"<br/>");
			            }
			        }else{
			            out.println("<br/><b><h3>No Products Viewed Yet</h3></b><br/>");
			        }
				}catch(Exception ex){
			        out.println("Exception is: "+ex);
					
				}
		    
			}
			
		}else{
		    out.println("Please configure the query path");
		}
  %> 

   