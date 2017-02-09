<%@include file="/libs/foundation/global.jsp" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.sling.api.SlingHttpServletRequest"%>

<script type="text/javascript">

var menuids=["sidebarmenu1"] //Enter id(s) of each Side Bar Menu's main UL, separated by commas

function initsidebarmenu(){
for (var i=0; i<menuids.length; i++){
  var ultags=document.getElementById(menuids[i]).getElementsByTagName("ul")
    for (var t=0; t<ultags.length; t++){
    ultags[t].parentNode.getElementsByTagName("a")[0].className+=" subfolderstyle"
  if (ultags[t].parentNode.parentNode.id==menuids[i]) //if this is a first level submenu
   ultags[t].style.left=ultags[t].parentNode.offsetWidth+"px" //dynamically position first level submenus to be width of main menu item
  else //else if this is a sub level submenu (ul)
    ultags[t].style.left=ultags[t-1].getElementsByTagName("a")[0].offsetWidth+"px" //position menu to the right of menu item that activated it
    ultags[t].parentNode.onmouseover=function(){
    this.getElementsByTagName("ul")[0].style.display="block"
    }
    ultags[t].parentNode.onmouseout=function(){
    this.getElementsByTagName("ul")[0].style.display="none"
    }
    }
  for (var t=ultags.length-1; t>-1; t--){ //loop through all sub menus again, and use "display:none" to hide menus (to prevent possible page scrollbars
  ultags[t].style.visibility="visible"
  ultags[t].style.display="none"
  }
  }
}

if (window.addEventListener)
window.addEventListener("load", initsidebarmenu, false)
else if (window.attachEvent)
window.attachEvent("onload", initsidebarmenu)

</script>



<%
    String applicationName = "";
    if(request.getRequestURI() != null){
        String requestUri[] = request.getRequestURI().split("/");
        applicationName = requestUri[2].toString();
        //applicationName = "ebazaar";
        
    }
// String leftNavComp = "/apps/ebazaar_RWD/components/component/leftnav";
// String brandFilterComp = "/apps/ebazaar_RWD/components/component/brandfilter";
//  String breadCrumb =  "/apps/ebazaar_RWD/components/component/breadcrumb";
//   String topnav="/apps/ebazaar_RWD/components/component/topnavigation";
    // Adding categories
    String categories[]=properties.get("categories",String[].class);
    request.setAttribute("categories",categories);



%>
<cq:include script="filter.jsp"/>
<cq:include path="par" resourceType="foundation/components/parsys"/>          