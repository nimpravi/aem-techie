<%--
   Report generator Component.

  Given a path of a page; it will print the details of the 
  components used in the page and how many times it has been used .

--%>
<%@ page
	import="java.util.HashMap,
    java.util.Set,
    java.util.HashSet,
    java.util.Map.Entry,
    java.util.List,
    com.cts.store.ReportGeneratorHelper"%>
<%
%><%@include file="/libs/foundation/global.jsp"%>

<script>$document.scrollTop();</script>
<%
if(properties.get("path") == null){
	%><h2 style="font-family:Lucida Bright;color:DarkCyan;font-size:20px;"><c:out value="Please Edit the Dialog"/></h3><%
}
else{
HashMap<String, Integer> hmap = new HashMap<String, Integer>();
HashMap<String, List<String>> cmap = new HashMap<String, List<String>>();
HashMap<String, String> nameMap = new HashMap<String, String>();
    //out.println(properties.get("path")); 
 String rootPath = properties.get("path").toString();
 Node parent = slingRequest.getResourceResolver().getResource(rootPath).adaptTo(Node.class);
// hmap = nodeChk(parent,slingRequest);

 hmap = ReportGeneratorHelper.nodeChk(parent,slingRequest);
 cmap = ReportGeneratorHelper.getComponentPagePath();
 nameMap = ReportGeneratorHelper.checkComponent(slingRequest);

 
 %>
<div style="padding-left: 560px">
    <h4 style="font-family:Lucida Bright;color:DarkCyan;font-size:20px;margin-bottom: 10px;"><b>Component Usage Reference </b></h4>
</div>

    <div style="padding-left: 75px;cursor: pointer"> <a align="left" onclick="goBack()" style="width:90px;font-size:17px;color:blue;text-decoration: underline;font-family: Times New Roman;">Back</a>

</div>
<div align="center">
<table border="solid" style="background-color:AliceBlue">
	<tr>
	    <th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">DeleteComponent(?)</th>
		<th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Component Name</th>
		<th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Component Path</th>
		<th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Total usage</th>
		<th style="color:DarkRed;font-family: Times New Roman;font-size:17px;">Pages Used</th>
	</tr>

	<%
 for (Entry<String, List<String>> entry : cmap.entrySet()) {
   
     if(nameMap.containsKey(entry.getKey())){
         String name = nameMap.get(entry.getKey());
         String Name = name.substring(0, 1).toUpperCase() + name.substring(1);
       
        %>
	<tr>
        <%if(Name != ""){%>
        <td align="center"><input type="checkbox"></input></td>
        <td style="font-size:15px;font-family: Times New Roman;" ><%=Name %></td>
		<td style="font-size:15px;font-family: Times New Roman;"><%=entry.getKey() %></td>
		<td align="center" style="font-size:15px;font-family: Times New Roman;"><%=entry.getValue().size() %></td>
		<%
     }
     %>
		<td style="font-size:15px;font-family: Times New Roman;">
   <%
     Set <String> lis = new HashSet<String>(entry.getValue());
     int i=0;
            for (String s : lis) {
                i++;

                %><%=i%>)<a href="<%=s %>"><%=s%></a> <br>
		<%
                           }
            %>
		</td>
        <%}%>
	</tr>
	<%
          
 }
 
%>

</table>
</div>
    <div style="padding-left: 75px;cursor: pointer"> <a align="left" onclick="goBack()" style="width:90px;font-size:17px;color:blue;text-decoration: underline;font-family: Times New Roman;">Back</a>

</div>

<%}
%>

<script>
function goBack()
  {
  window.history.back()
  }
</script>