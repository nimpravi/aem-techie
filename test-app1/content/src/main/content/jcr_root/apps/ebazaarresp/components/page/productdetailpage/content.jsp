<%@include file="/libs/foundation/global.jsp"%>

<%@page import="com.day.cq.ebazaar.SearchUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="javax.servlet.http.HttpSession"%>


<%
    String applicationName = "";
    if(request.getRequestURI() != null){
        String requestUri[] = request.getRequestURI().split("/");
        applicationName = requestUri[2].toString();

    }

            String slingResourceType = "ebazaarresp/components/content/productdetails";
            String prodCode = (String)slingRequest.getParameter("prodCode");

            String sHiddenProdCode = (String)slingRequest.getParameter("hiddenProdCode-1");




            if(prodCode == null || "".equals(prodCode)) 
            {
                if(sHiddenProdCode != null)
                {
                    prodCode = "W1";
                    // out.println("hi");
                }
                else
                {
                    javax.servlet.http.HttpSession session = slingRequest.getSession();
                    prodCode = (String)session.getAttribute("pCode");
                    
                    //out.println("P Code : " +prodCode);
                }
            }
            
            if(prodCode != null && prodCode.trim().length() > 0) {
                String nodePath = "";
                Map prophmap = new HashMap();
                Map resultMap = new HashMap();
                prophmap.put("prodCode",prodCode);
                List productDetails = SearchUtils.getProducts(prophmap,slingResourceType,slingRequest);

                javax.servlet.http.HttpSession session = slingRequest.getSession();
                   session.setAttribute("RecentVProduct", productDetails);

                for(int i=0;i<productDetails.size();i++){

                    resultMap = (HashMap)productDetails.get(i);
                    nodePath = (String)resultMap.get("nodePath");
                }
            %>

               <sling:include path="<%= nodePath %>" />

            <%

            
            } else {
            %>

               <cq:include path="prodparcoffee" resourceType="/libs/foundation/components/parsys"/>
            <%
            }
            
            %>

