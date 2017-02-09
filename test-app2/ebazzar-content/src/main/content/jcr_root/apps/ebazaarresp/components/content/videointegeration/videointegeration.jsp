<%@include file="/libs/foundation/global.jsp"%><%
%>
<cq:includeClientLib categories="kalturascript"/>

<c:set var="videotype" value="${properties.videotype}" />
<c:set var="inputtype" value="normal"/>
<c:if test="${videotype eq null && properties.entryid eq null}">
Configure the Video.
</c:if>
<c:choose>
    <c:when test="${properties.videotype eq inputtype}">
        <video width="320" height="240" controls>
         <source src="${properties.url}" type="video/mp4">
        </video>
    </c:when>
    <c:otherwise>
        <c:set var="entryid" value="${properties.entryid}"/>
        <c:set var="partnerid" value="${properties.partnerid}"/>
        <c:set var="playerid" value="${properties.playerid}"/>
	        <object id="kaltura_player" name="kaltura_player"
	             type="application/x-shockwave-flash"
	             allowFullScreen="true" allowNetworking="all"
	             allowScriptAccess="always" height="330" width="400"
	             data="https://www.kaltura.com/kwidget/wid/_${partnerid}/uiconf_id/${playerid}/sus/ash/entry_id/${entryid}">
	             <param name="allowFullScreen" value="true" />
	             <param name="allowNetworking" value="all" />
	             <param name="allowScriptAccess" value="always" />
	             <param name="bgcolor" value="#000000" />
	             <param name="flashVars" value="&" />
	             <param name="movie" value="https://www.kaltura.com/kwidget/wid/_${partnerid}/uiconf_id/${playerid}/sus/ash/entry_id/${entryid}" />
	        </object>
   </c:otherwise>
</c:choose>
