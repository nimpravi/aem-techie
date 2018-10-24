<%--

  Home Page component.

  This is the Home Page for LifePlus site

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%>

<sling:include path="lifeplusCarousel" resourceType="/apps/solution-accelerators/components/lifeplus/content/lifePlusCarousel" addSelectors="lifePlusCarousel"/>
<cq:include path="custompar" resourceType="foundation/components/parsys" />
<cq:include path="custompar1" resourceType="/apps/solution-accelerators/components/global/content/customParsys" />
<cq:include path="testimonials" resourceType="/apps/solution-accelerators/components/lifeplus/content/testimonials"/>

<cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>
<cq:include path="clickstreamcloud" resourceType="cq/personalization/components/clickstreamcloud"/>
<%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %>