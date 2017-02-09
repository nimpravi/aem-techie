<%--

     KAO LOGO COMPONENT

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@ page
	import="com.day.text.Text,com.day.cq.wcm.foundation.Image,com.day.cq.commons.Doctype"%>
<%
	String home = currentStyle.get("absParent", "#");
%>
<c:set var="image" value="${currentStyle.imageReference}"></c:set>
<c:set var="mobimage" value="${currentStyle.mobImageReference}"></c:set>

<div class="hc col-md-4 navbar-default">
    <a href="<%=home%>.html"> <img class="hidden-md hidden-sm hidden-xs" src="${image}" width="175" height="75"> </a>
         <a class="hidden-lg" id="logoImage" href="<%=home%>.html">
              <img alt="Brand" src="${mobimage}">
         </a>

          <button type="button" data-target="#collapseButtonData1" data-toggle="collapse" class="navbar-toggle hidden-lg" id="mobNav">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
          </button>
          

          <a id="searchIcon" href="#"><i class="hidden-lg fa fa-search " id="posSearchIcon"></i></a>

           <li class="dropdown hidden-lg">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="glyphicon glyphicon-globe hidden-lg " id="posGlobeIcon"></i></a>
                    <ul role="menu" class="dropdown-menu ">
                        <li><a href="#">America</a></li>
                        <li><a href="#">Asia</a></li>
                        <li><a href="#">Africa</a></li>
                        <li><a href="#">Pacific</a></li>
                    </ul>
                </li>
    </div>


<script>

    $( document ).ready(function() {
        $( "#mobNav" ).click(function() {
            var classVar = $("#mobView").attr("class");
            if(classVar=="collapse") {
                $("#mobView").attr("class", "in");
            } else if(classVar=="in") {
                $("#mobView").attr("class", "collapse");
            }
        });
    });

</script>