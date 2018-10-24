<%@include file="/libs/foundation/global.jsp" %>
<div class="errorpage" >
<body>
<%if(currentPage.getPath().equalsIgnoreCase("/content/philips/en/errors/default")){ %>
 <cq:include script="defaultError.jsp"/>
<%}
else{%>
    <cq:include script="content.jsp"/><%} %>
</body>
</div>
<style>
.errorpage body
{

background-color:#A1A3A5;
}
.title
{
font-family:"novel_sans_rd_proregular", Calibri, Verdana, Geneva, sans-serif;
color:#071D49;
text-align:center;
font-weight: bold;
}
.errorpage p
{
font-family:"novel_sans_rd_proregular", Calibri, Verdana, Geneva, sans-serif;
font-size:18px;
text-align:left;

}
</style>