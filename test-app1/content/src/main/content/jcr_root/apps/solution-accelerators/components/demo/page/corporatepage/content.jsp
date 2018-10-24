<%-- Content Page component. --%>

<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<% if(currentNode.hasNode("globalFooter")) { %>
<div class="corporate">
    <section class="corporate-profile">
        <cq:include path="custompar" resourceType="foundation/components/parsys" />
        <section>
             <div class="container">
                <div class="row company1" style=" margin-left:0px">
                    <cq:include path="custompar1" resourceType="foundation/components/parsys" />
                    <% if(currentNode.hasNode("custompar1")) { %>
                   		<cq:include path="news-release" resourceType="/apps/solution-accelerators/components/demo/content/news-release" />
                    <% } %>
                </div>       
                <div class="row company2" style="margin-left:0px;">   
                    <cq:include path="custompar2" resourceType="foundation/components/parsys" />
                </div>
            </div>
        </section>
    </section>
    <div class="clear-fix"></div>

    <section class="other-venture">
        <cq:include path="custompar3" resourceType="foundation/components/parsys" />
    </section>
    <div class="clear-fix"></div>
    <% if(currentNode.hasNode("globalFooter")) { %>
        <cq:include path="globalFooter" resourceType="/apps/solution-accelerators/components/demo/content/GlobalFooter"/>
    <% } %>
</div>
<% } %>