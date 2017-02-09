
<%@include file="/libs/foundation/global.jsp"%><%
%> 

   <%@ page import="java.util.Iterator,
        java.util.HashMap,
        java.util.ArrayList,
        com.day.text.Text,
        com.day.cq.wcm.api.PageFilter,
        com.day.cq.wcm.api.Page,
        com.day.cq.commons.Doctype"
       %>
<cq:includeClientLib css="app.sitemap"/>

       <div class="sitemap">
           <h1><%=properties.get("title", "SiteMap") %></h1>
    <ul>
    <%
     
    String menuItemName = "",mainMenuItemName = "",pagePath = null,   LastmenuItemName=" " ,submenuItemName=" ";
    Page mainChild = null;


    Session session = resourceResolver.adaptTo(Session.class);
   resource = slingRequest.getResourceResolver().getResource(currentNode.getPath());
   Node Navagational = resource.adaptTo(Node.class);

      try {  if(Navagational.hasProperty("rootPath")){

    pagePath  = Navagational.getProperty("rootPath").getString();

        
    if (pagePath!=null){

        Resource rootResp = slingRequest.getResourceResolver().getResource(pagePath);
        Page rootPage = rootResp == null ? null : rootResp.adaptTo(Page.class);
             
                Iterator<Page> menuItemsp = null;
                if (rootPage != null){
                    menuItemsp = rootPage.listChildren(new PageFilter(request));
                    
                    HashMap<String, ArrayList<String>> menuMap = new HashMap<String, ArrayList<String>>();
                    ArrayList<String> subMenus = new ArrayList<String>();
                    while (menuItemsp.hasNext()) {
                        
                        mainChild = menuItemsp.next();
                    
                       
                        mainMenuItemName = mainChild.getTitle(); 
                 %>
                   <li><a href="<%=mainChild.getPath()%>.html"><%=mainChild.getTitle() %></a>
              </li>   
                 <% 
        
              Iterator<Page> childPages = mainChild.listChildren(new PageFilter(request));
                %>
                      <ul>
        <%
                while (childPages.hasNext()) {
                    
                    Page child = childPages.next();
                    
                    menuItemName= child.getTitle();
            %>
               <li><a href="<%=child.getPath()%>.html"><%=child.getTitle() %></a>
              </li>  
            
            <%
         
                  if(childPages !=null ){
                  Iterator<Page> subchildPages =null;
                  subchildPages= child.listChildren(new PageFilter(request));     
                  %>

                   <ul>

                          <%
                  while (subchildPages.hasNext()) {
                      
                      Page subchild = subchildPages.next();
                      
                     submenuItemName=  subchild .getTitle();
             %>
             <li><a href="<%=subchild.getPath()%>.html"><%=subchild.getTitle() %></a>
              </li>  
             
             
             <% 
         
               Iterator<Page> lastchildPages =null;
             lastchildPages= subchild.listChildren(new PageFilter(request));   

                      %>

  <ul>
                       <%
             while (lastchildPages.hasNext()) {
                 
                 Page lastchild = lastchildPages.next();
                 
                LastmenuItemName=  lastchild .getTitle();
             
                    
                   %>
                <li><a href="<%=lastchild .getPath()%>.html"><%=lastchild .getTitle()%></a></li>
              
           <%     }
             %>
                       </ul>

      <%
               
                  }

            %>
                          </ul>

                       <%



        }
            

                }

%>

        </ul>
                          <%


            }
           
        
}   %><%



}else {
    out.print("Edit to Configure sitemap Component"); 
}


} %> <% 

}catch(Exception e){} 
        
    %>
</ul>


<div class="clear"></div>

</div>
                
                
