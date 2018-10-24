<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" 
        import="java.util.List,
                com.cts.store.richtextpopup.OverlayPopupService,
                com.cts.store.richtextpopup.OverlayPopupContentBean,
                com.day.cq.wcm.api.WCMMode" 
%>
<cq:includeClientLib categories="ebazaarresp.overlaypopup"/>

<%
   Session session = resourceResolver.adaptTo( Session.class ); 
   String currentChildPath = null;
   OverlayPopupContentBean overlayPopupContentBean = null; 

   String text = properties.get( "text", String.class );
   
   if( text != null && WCMMode.EDIT.equals(WCMMode.fromRequest(request)) ){        
      text = OverlayPopupService.updateOverlayPopupComponent(session, currentPage, currentStyle, currentNode, text);
   }   
%>

<script type="text/javascript">
    function displayPopup( event, popupItem ){
        overlay_showref( event, popupItem );
    }
</script>

<c:choose>
    <c:when test="${empty properties.text}">
        <c:if test="<%= WCMMode.EDIT.equals(WCMMode.fromRequest(request)) %>">
            <c:out value="Overlay Popup: No text found. Please add text in this Overlay component" /> 
        </c:if> 
    </c:when>
    <c:otherwise>
    
        <cq:text value="<%=text%>" />
           
        <div id='wrap-references'> 
        <%      List<Long> popupIds = OverlayPopupService.getExistingOverlayPopupChildIds(currentNode);
                Node childTitleNode=null;
                for( Long popupId : popupIds ){
                    overlayPopupContentBean = OverlayPopupService.getContentForId( currentNode, popupId );
                    currentChildPath = "overlay_popup_children/overlay_popup_child_" + popupId;
                    Resource childTitleResource=resourceResolver.getResource(currentNode.getPath()+"/"+currentChildPath+"/component_title");
                    String childHeight="134px";
                    String childWidth="200px";
                    int outsideContentHeight=66;
                    if(childTitleResource!=null){
                        childTitleNode=childTitleResource.adaptTo(Node.class);
                        
                        if(childTitleNode.hasProperty("width")){
                        childWidth= childTitleNode.getProperty("width").getString()+"px";
                        
                        }
                        if(childTitleNode.hasProperty("height")){
                            childHeight= childTitleNode.getProperty("height").getLong()-outsideContentHeight+"px";
                        }
                        
                    }
                    
                   
        %>   
        
                 <div  id='<%= popupId %>' style='display:none; z-index: 200;'>
                     <div onclick="closePopup('<%= popupId %>')" 
                            class="glossary_popup_close" 
                            id="popupclose" eventdetail="{&quot;ReferenceDetail&quot;:&quot;Reference&quot;}">
                     </div>
                     <div class="glossary_popup_top_img">
                         <div class="popuparrowtop"></div>
                         
                     </div>
                     <div class='glossary_popup_top' id='popuptop‡‡'>
                         <div class='glossary_popup_title'>
                             <div class='glossary_popup_title_text' >
                                <cq:include 
                                    path="<%= currentChildPath + "/component_title" %>" 
                                    resourceType="ebazaarresp/components/content/overlaypopup/overlaytitle"
                                    />
                             </div>
                         </div>
                     </div>
                     <div class="glossary_popup_mid" style="min-height:<%=childHeight %>;min-width:<%=childWidth %>">
                         <div class='glossary_popup_text'>
                             <div class='' style="width:<%=childWidth %>;">
                                <!--  Holder to have multiple child components, which will be added by Author -->                            
                                <cq:include 
                                    path="<%= currentChildPath + "/component_holder" %>" 
                                    resourceType="foundation/components/parsys"/>
                             </div>
                         </div>
                     </div>
                     <div class='glossary_popup_btm'>
                         <div class="popuparrow"></div>
                     </div>
                 </div>
        <%      }  // end for %>
        </div>
    </c:otherwise>
</c:choose>         
<cq:include path="overlay_popup_children" resourceType="foundation/components/parsys"/>
  