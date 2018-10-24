<%@include file="/libs/foundation/global.jsp"%>


<%
	String imageLayout = "";
    if(currentNode.hasProperty("imageLayout")) {
		imageLayout = currentNode.getProperty("imageLayout").getString();
        if(imageLayout.equals("infoGraphics")) { %>
            <sling:include resourceType="/apps/solution-accelerators/components/global/content/imagemultifield" addSelectors="infoGraphics" />
    <%	} else if(imageLayout.equals("icon")) { %>
            <sling:include resourceType="/apps/solution-accelerators/components/global/content/imagemultifield" addSelectors="icon" />
    <%	} else { %>
            <sling:include resourceType="/apps/solution-accelerators/components/global/content/imagemultifield" addSelectors="banner" />
    <%	}
    } else {
		out.println("Configure Images");
    }

    %>
