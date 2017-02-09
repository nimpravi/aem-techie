<%@include file="/libs/foundation/global.jsp"%>
<%
String redirect = request.getParameter("redirect");
String prodCode = request.getParameter("prodCode");
String quantity = request.getParameter("quantity");

if(currentNode != null){
    try{
        if(currentNode.hasNode(slingRequest.getRemoteUser())){
            Node userNode = currentNode.getNode(slingRequest.getRemoteUser());
            if(userNode.hasNode(prodCode)){
                userNode.getNode(prodCode).update(quantity);
                userNode.getSession().save();
                response.sendRedirect(redirect);
            }           
        }
    }catch(Exception e){}
}    
%>