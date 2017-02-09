<%--

  kaltura component.



--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
	// TODO add you code here
%>

Hi...<br>
FileName:${properties.fileName}<br>
Multi:${properties.test}
<br>
<%
String event = properties.get("fileName", "");
out.println("out >>>>>>"+event+"<br>");

Node rootNode = null;
	Node targetNode = null;



Session jcrSession = resourceResolver.adaptTo(Session.class);
String value = "";
try{
    out.println("currentNode>>>>>>"+currentNode+ "<br>");
    if(jcrSession.itemExists(currentNode.getPath())){
        out.println("inside <br>");
        rootNode = jcrSession.getRootNode();
         out.println("rootNode>>>>>>>>>>>"+rootNode+ "<br>");
        targetNode = rootNode.getNode("content/KTp/jcr:content/kaltura/kaltura_dialog");
 out.println("targetNode>>>>>>>>>>>"+targetNode+ "<br>");
        if(targetNode != null && targetNode.hasProperty("fileName"))
        {

            Value[] listItems = targetNode.getProperty("fileName").getValues();

            for(int i=0; i<listItems.length; i++)
            {

                    String text = listItems[i].getString();

out.println("text>>>>>>>>>>>>>"+text);

       }

    }


}
}

catch (Exception ex){
    out.println("Exception is: "+ex);
}


%>
