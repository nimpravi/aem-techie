<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" 
        import="com.day.cq.wcm.api.WCMMode" 
%>
<c:choose>
    <c:when test="${empty fn:trim(properties.title)}">
        <c:if test="<%= WCMMode.EDIT.equals(WCMMode.fromRequest(request)) %>">
            <font>Edit Title & Popup Size here</font>
        </c:if>
    </c:when>
    <c:otherwise>
        <cq:text property="title"/>
    </c:otherwise>
</c:choose>