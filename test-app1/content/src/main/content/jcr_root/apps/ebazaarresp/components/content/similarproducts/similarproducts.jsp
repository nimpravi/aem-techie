<%--

  Similar Products component.

  Displays the similar products

--%><%@page import="com.day.cq.commons.RangeIterator,com.day.cq.tagging.TagManager,java.util.List,java.util.ArrayList,com.day.cq.tagging.Tag,com.day.cq.wcm.api.WCMMode"%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
if(currentNode != null) {
%>
<div id="simillar-items">
<%
String currentPageNodePath = currentPage.getPath() + "/jcr:content";

List<String> tagIdsList = new ArrayList<String>();
Tag[] tags = currentPage.getTags();
String tagId = null;
for (int i = 0; i < tags.length; i++) {
	tagId = tags[i].getTagID();
	if(tagId.startsWith("philips_category:")) {
		tagIdsList.add(tagId);
	}
}

String[] tagIds = new String[tagIdsList.size()];
tagIdsList.toArray(tagIds);

TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

RangeIterator<Resource> pages = tagManager.find(currentPage.getParent().getPath(), tagIds, true);

int numberofItems = 0;
try {
	numberofItems = properties.get("numberofitems", Integer.class);
} catch(Exception e) {
	//
}

int count = 0;

while(pages.hasNext()) {
	Node pageNode = pages.next().adaptTo(Node.class);
	if(pageNode.getPath().equals(currentPageNodePath)) {
		continue;
	}
	String title = pageNode.getProperty("jcr:title").getString();
	String description = pageNode.getProperty("jcr:title").getString();
	String link = pageNode.getPath().replace("/jcr:content", "");
	String imagePath = null;
	if(pageNode.hasNode("image")) {
		Node image = pageNode.getNode("image");
		imagePath = image.hasProperty("fileReference") ? image.getProperty("fileReference").getString() : null;
	}
	%>
	<div id="similar-item">
		<a title="<%=title%>" href="<%=link%>.html">
			<%if(imagePath != null) { %><img alt="<%=title%>" src="<%=imagePath%>/jcr:content/renditions/cq5dam.thumbnail.140.100.png"> <%} %>
			<h2><%=title %></h2>
		</a>
		<p><%=description %></p>
	</div>
	<%
	count++;
	if(count == numberofItems) {
		break;
	}
}

%>
</div>
<style>
	#simillar-items {
		height: 200px;
	}
	#similar-item {
		float: left;
		width: 200px;
	}
</style>
<%} else {
    if( WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.DESIGN ){ 
        %><div style='width:340px; padding:10px; text-align:center; text-transform:uppercase; background:#eee; border:dotted 3px #333;'>Configure in Edit Mode</div><%
    }
}%>
