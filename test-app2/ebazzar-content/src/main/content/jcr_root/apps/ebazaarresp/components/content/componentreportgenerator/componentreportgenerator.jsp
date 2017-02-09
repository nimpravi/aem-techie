<%--
   Apps Report generator Component.

  Given a path of a page; it will print the details of the 
  components used in the page and how many times it has been used .

--%>
<%@ page
	import="java.util.HashMap,
    java.util.Set,
    java.util.HashSet,
    java.util.Map.Entry,
    java.util.Map,
    java.util.HashMap,
    java.util.List,
    com.cts.store.ComponentBean,
    com.cts.store.ComponentReportGenerator,
    org.slf4j.Logger,
    org.slf4j.LoggerFactory"%>
<%
%><%@include file="/libs/foundation/global.jsp"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script>$document.scrollTop();</script>


<%
Logger LOG = LoggerFactory.getLogger(ComponentReportGenerator.class);
if(properties.get("path") == null){
    %><h2>Right Click to edit the dialog and select a path to generate component Report</h2><%
}
else{

HashMap<String, List<ComponentBean>> componentDetailMap = new HashMap<String, List<ComponentBean>>();
String rootPath = properties.get("path").toString();
Node parent = slingRequest.getResourceResolver().getResource(rootPath).adaptTo(Node.class);
componentDetailMap = ComponentReportGenerator.countComponents(parent,resourceResolver);
int count = 0;

 %>
<div style="padding-left: 580px">
    <h4 style="font-family:Lucida Bright;color:DarkCyan;font-size:20px;margin-bottom: 10px;"><b>Component Library </b></h4>
</div>

<div style="padding-left: 110px;cursor: pointer"> <a align="left" onclick="goBack()" style="width:90px;font-size:17px;color:blue;text-decoration: underline;font-family: Times New Roman;">Back</a>

</div>
<div align="center">
<table  border="solid" style="background-color:AliceBlue">
    <tr>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Sl</th>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Component Icon</th>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Component Name</th>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Component Description</th>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Component Group</th>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Owner</th>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Issues Reported</th>
        <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Wiki Link</th>
        
      
    </tr>

    <%
    for (Map.Entry<String, List<ComponentBean>> entry : componentDetailMap.entrySet()){
    	%>
   <%
         String name = entry.getKey();
       
        %>
    <tr>
        <%  for (ComponentBean list : entry.getValue()) {%>
        <td align="center"><%=++count %></td>
        
         <script>
        function overLay(path){

        	var imgid = document.getElementById("dialog");
            if(imgid != undefined){
                if(imgid.hasChildNodes()){
                    imgid.removeChild(imgid.childNodes[0]);
                }
            }
         
            var imgobj = document.createElement("img");
            imgobj.id = "overlayImage";
            imgobj.setAttribute('src',path);
            
            $( "#dialog" ).append(imgobj);
            
            $( "#dialog" ).dialog({height:"auto",width:"1000"});
           
        }
   </script>
   
        <td align="center"><img onclick="overLay('<%=list.getThumbnailPath()%>')" width="60px" height="74px" src="<%=list.getThumbnailPath() %>"></img></td>
        <td style="font-size:15px;font-family: Times New Roman;"><%=name %></td>
        <td style="font-size:15px;font-family: Times New Roman;"><%=list.getComponentDescription() %></td>
        <td style="font-size:15px;font-family: Times New Roman;"><%=list.getComponentGroup() %></td>
        <td style="font-size:15px;font-family: Times New Roman;">Admin</td>
         <td align="center" style="font-size:15px;font-family: Times New Roman;">0</td>
        <%if(list.getWikiLink() != ""){
        	LOG.error("WIKI "+list.getWikiLink());%>
        <td align="center" style="font-size:17px;font-family: Times New Roman;"><a style="color:blue;" href="http://<%=list.getWikiLink() %>" target="_blank">Wiki</a></td>
        <%}
        else {%>
        <td></td>
        <%} %>
        <%
     }
     %>
        
    </tr>
    <%
          
 }
%>
</table>
</div>

    <div style="padding-left: 110px;cursor: pointer"> <a align="left" onclick="goBack()" style="width:90px;font-size:17px;color:blue;text-decoration: underline;font-family: Times New Roman;">Back</a>

</div>
<%}
%>
<div id="dialog" title="Component Design">
  
  
</div>

<script>
function goBack()
  {
  
  window.history.back()
  }


</script>