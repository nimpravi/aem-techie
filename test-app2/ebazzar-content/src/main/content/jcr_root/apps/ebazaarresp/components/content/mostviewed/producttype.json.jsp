

<%@page import="java.util.List"%>
<%@page import="javax.jcr.query.QueryResult"%>
<%@page import="javax.jcr.query.Query"%>
<%@page import="javax.jcr.query.QueryManager"%>
<%@page import="com.day.cq.search.QueryBuilder"%>
<%@page import="com.cts.productlist.SampleQueryUtil"%>
<%@include file="/libs/foundation/global.jsp"%><%

    
    response.setContentType("text/plain");

    String queryPath = properties.get("queryPath","");
    
%>[<%

Session jcrSession = resourceResolver.adaptTo(Session.class);

try{
    if(jcrSession.itemExists(queryPath)){
        
        String delim = "";
        
        if(queryPath !=null && !queryPath.equalsIgnoreCase("")){
            try{    
                Session session           = currentNode.getSession();
            
                SampleQueryUtil sampleQueryUtil     = new SampleQueryUtil();
                QueryBuilder queryBuilder           = sling.getService(QueryBuilder.class);
                List<String> prodTypeList           = sampleQueryUtil.fetchProdType(session, queryPath, queryBuilder);
                
                if(prodTypeList != null && prodTypeList.size()>0){
                    
                    for(int i=0; i<prodTypeList.size(); i++){
                    	String text = prodTypeList.get(i);
                    	%><%= delim %><%
                        %>{<%
                             %>"text":"<%= text %>",<%
                             %>"value":"<%= text %>"<%
                         %>}<%
                         if ("".equals(delim)) delim = ",";
                    }
                }
            }catch(Exception ex){
                out.println("Exception is: "+ex);
                
            }
            
        }
        
    }
}catch (Exception ex){
    out.println("Exception is: "+ex);
}
%>]