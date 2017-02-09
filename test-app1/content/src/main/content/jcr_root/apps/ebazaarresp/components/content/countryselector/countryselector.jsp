<%-- 
* Program Name : countryselector.jsp
* Purpose      : See description
* Description  : Component displays the Country Selector at the header section
* Names of Databases accessed: CRX
--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %>
<%@ page import=" java.util.HashMap,
        java.util.TreeMap,
        java.util.LinkedHashMap,
        com.cts.store.NavigationHelper"
        %>   
<cq:setContentBundle/>
<cq:includeClientLib categories="apps.ebazzar.responsive"/>
<%
    NavigationHelper nav = new NavigationHelper();
try{
    LinkedHashMap<String,HashMap<String,String>>  countries= new LinkedHashMap<String,HashMap<String,String>>();
    TreeMap<String,String>  sortedCountries= new TreeMap<String,String>();   
    String countriesListRootPath = properties.get("countrypath",String.class);
   // String defaultMsg = properties.get("defaultmsg","Select Country");      
    if(countriesListRootPath!=null){
    Resource r=slingRequest.getResourceResolver().getResource(countriesListRootPath);
    Page p=r.adaptTo(Page.class);
    countries = nav.getSubMenuNav(p); 
    sortedCountries=nav.getSortedCountries(countries);
%>
<c:set var="defaultMsg" value="${properties.defaultmsg}"/>
<c:if test="${defaultMsg eq null}">
  <span>Select Country</span>
</c:if>
<c:set var="countryList" value="<%=countries%>"/>
<c:set var="sortedCountries" value="<%=sortedCountries%>"/>
<c:set var="menuSize" value="<%=countries.size()%>"/>
<c:choose>
<c:when test="${menuSize > 1}">
    <div>
        <div class="customDropdown">
            <span id="country"><c:out value="${defaultMsg}" /></span>
            <div class="dropDownOption" id="one">
                <ul>
                        <c:forEach var="titlePath" items="${sortedCountries}" varStatus="loopIterator1">
                            <c:set var="title" value="${titlePath.key}" scope="page" />
                            <c:set var="path" value="${titlePath.value}" scope="page" />
                            <%                           
                            String pages = pageContext.getAttribute("path").toString();
                            Resource resourceInstance = resourceResolver.getResource(pages);
                            Node node = resourceInstance.adaptTo(Node.class); 
                            Node newnode = node.getNode("jcr:content/image");
                            String imgsrc="";
                            if(newnode.hasProperty("fileReference")){
                                imgsrc = newnode.getProperty("fileReference").getString();
                            }%>
                            <li><a href="<c:out value="${path}" />.html"><img class="imageListing" src="<%=imgsrc%>" alt="AbbVie Pharmaceuticals" />${title}</a></li>
                        </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</c:when>
<c:otherwise>
<c:if test="${(mode eq 'EDIT') || (mode eq 'DESIGN')}">    
    <span>No Available Countries</span>                       
</c:if> 
</c:otherwise>
</c:choose>
<%}else {
%>
<c:set var="currentpage" value="<%=currentPage.getName()%>"/>
    <span>Edit Country Selector </span>
<%    
  }                 
     }catch (Exception cse) {
        log.error("Exception occured in countryselector",cse);
     }%>
<c:set var="defaultMsg" value=""/>
<c:set var="countryList" value=""/>
<c:set var="currentpage" value=""/>
<c:set var="title" value="" scope="page" />
<c:set var="path" value="" scope="page" />
