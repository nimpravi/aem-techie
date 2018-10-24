<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" 
        import="com.day.cq.wcm.api.WCMMode" 
%>
<c:if test="<%= WCMMode.EDIT.equals(WCMMode.fromRequest(request)) %>">
    <strong>
        <c:out value='Popup ${properties.label} :' />
    </strong>
    <c:out value=' To configure this popup, open the popup window and edit it.' />
</c:if> 
    