<%@include file="/libs/foundation/global.jsp"%>
<%
String redirect = request.getParameter("redirect");
String prodCode = request.getParameter("prodCode");  
if(currentNode != null){
    try{
        if(currentNode.hasNode(slingRequest.getRequestedSessionId())){
            Node userNode = currentNode.getNode(slingRequest.getRequestedSessionId());
            if(userNode.hasNode(prodCode)){
                userNode.getNode(prodCode).remove();
                userNode.getSession().save();
                response.sendRedirect(redirect);
            }           
        }
    }catch(Exception e){}
}    
%>
