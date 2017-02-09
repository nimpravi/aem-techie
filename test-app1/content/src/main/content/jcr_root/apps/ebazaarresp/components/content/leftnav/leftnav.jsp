<%@include file="/libs/foundation/global.jsp"%>
<%@ page import="java.util.Iterator, com.day.text.Text,
        com.day.cq.wcm.api.PageFilter, com.day.cq.wcm.api.Page,
         com.day.cq.commons.Doctype,
        org.apache.commons.lang3.StringEscapeUtils" %>
<%

long absParent = currentStyle.get("absParent", 4L);

Page navRootPage = currentPage.getAbsoluteParent((int)absParent);

//String leftNav = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);
%>

        <jsp:useBean id="leftNavigation" class="com.cts.store.LeftNavigation"/> 
        <jsp:setProperty name="leftNavigation" property="homePage" value="<%= currentPage.getAbsoluteParent(1) %>"/>
<jsp:setProperty name="leftNavigation" property="level1Page" value="<%= navRootPage %>"/>
		
	<c:if test="${not empty leftNavigation.level1Page}">
<ul class="nav">

			        <c:forEach items="${leftNavigation.level1Pages}" var="child2"> 

			           <jsp:setProperty name="leftNavigation" property="level2Page" value="${child2}"/>

			           <c:choose>
				           <c:when test="${currentPage.name eq child2.name}">
				             <c:set var="firstClassName" value="active"/>
				           </c:when>
				           <c:otherwise>
				             <c:set var="firstClassName" value="inactive"/>
				           </c:otherwise>
			           </c:choose>
			           <li class="<c:out value="nav-header"/>"><a href="<c:out value="${child2.path}"/>.html"  title="<c:out value="${child2.title}"/>"  class="title" ><c:out value="${child2.title}"/></a></li>

			               <c:forEach items="${leftNavigation.level2Pages}" var="child3">
			                   <jsp:setProperty name="leftNavigation" property="level3Page" value="${child3}"/>
			                   
			                   <c:choose>
		                           <c:when test="${currentPage.name eq child3.name}">
		                             <c:set var="secondClassName" value="active"/>
		                           </c:when>
		                           <c:otherwise>
		                             <c:set var="secondClassName" value="inactive"/>
		                           </c:otherwise>
		                       </c:choose>
			                   
			                   <li class="<c:out value="${secondClassName}"/>"><a href="<c:out value="${child3.path}"/>.html" title="<c:out value="${child3.title}"/>"><c:out value="${child3.title}"/></a></li>

			               </c:forEach>

			        </c:forEach>
				</ul>
	        </div>
        </c:if>

	
