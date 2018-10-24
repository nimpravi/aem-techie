<%@ page import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image,
    org.apache.commons.lang.StringUtils,
    java.util.Locale,
    java.util.ResourceBundle,
    com.day.cq.i18n.I18n" %>
   <%@page import="javax.servlet.http.HttpSession"%>   

    <% %>
<%@include file="/libs/foundation/global.jsp"%>

<cq:setContentBundle/>
<%
        final ValueMap valueMap = resource.adaptTo(ValueMap.class);
//final float ratingToRender = Float.parseFloat(valueMap.get("response", "0"));
final float ratingToRender=2.0f;
    Image image = new Image(resource);
    //drop target css class = dd prefix + name of the drop target in the edit config
    //image.addCssClass("zoomPad");
    image.addAttribute("width", "470");
    image.addAttribute("height", "310");
    image.addAttribute("style", "margin-bottom:5px");
    image.addAttribute("id", "img_change");
    image.loadStyleData(currentStyle);
    image.setSelector(".img"); // use image script
    image.setDoctype(Doctype.fromRequest(request));
    // add design information if not default (i.e. for reference paras)
    if (!currentDesign.equals(resourceDesign)) {
        image.setSuffix(currentDesign.getId());
    }
    String divId = "productLayoutForm:pbilimage1tag";
    
    // Ratings - Begin
    // Unique Key for Product Rating Component Id
String starClass="";
float padding = 0.0f;
    String sPrdCode = properties.get("prodCode", "");
javax.servlet.http.HttpSession session = slingRequest.getSession();

    String sProdCodeSession = (String)session.getAttribute("pCode");
    if(sProdCodeSession != null)
    {
        // out.println( "Session Value is " + sProdCodeSession); 
        // out.println( "Actual Value is " + sPrdCode);
        if(sProdCodeSession.equals(sPrdCode))   
        {
            // out.println(" found product ");
            session.setAttribute("pCode", "cleared");
        }
        else
        {
            // out.println(" not matching product ");
            return;
        }
    }
    String sPrdDesc = properties.get("prodDescription", "");
    if(sPrdDesc != "")
        sPrdDesc = sPrdDesc.substring(0,3);
    
    String sPrdTitle = properties.get("prodTitle", "");
    if(sPrdTitle != "")
        sPrdTitle = sPrdTitle.substring(0,3);
    
    String sProdRating = "";
    if(sPrdCode != "" && sPrdTitle != "" && sPrdDesc != "")
    {
        sProdRating = "prodRatings" + (sPrdCode + sPrdTitle + sPrdDesc);    
    }
    
    String sPrdOffer = properties.get("prodOffer", "");
    String realPrice = properties.get("prodPrice", "");
    // Ratings - End

%>

<div class="row">

<div class="span5">
<div id="items-carousel" class="carousel slide mbottom0">
			        <div class="carousel-inner">  
			           <div class="active item"><% image.draw(out); %></div>
			          <div class="item"> <img class="media-object" src="holder.js/470x310" alt="" /> </div>
			          <div class="item"> <img class="media-object" src="holder.js/470x310" alt="" /> </div> 
		            </div> 
			        <a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a> <a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a> </div>
		        </div>
			    <div class="span4">
			      <h4><cq:text property="prodTitle"></cq:text></h4>
			      <h5><cq:text property="prodCode"></cq:text></h5>
                    <div class="ratings">
							                  <%
									 starClass = "full";
										int i = 1;
										while (i <= 5) {

                  if(i > ratingToRender) {
			        if(ratingToRender > i-1) {
			            starClass = "full";
			            padding = (16-((ratingToRender-(i-1)) * 0.8f * 20));
			        }

			        else {
			            starClass = "empty";
			        }
			    }
			    %>
              <div class="ratings-star <%= starClass %>" style="margin-right: <%= padding %>px; width: <%= 16 - padding %>px;"></div>

                                    <%
			    i++;
			}%>
                        <br/>

				  </div>
			      <p><cq:text property="prodDescription"></cq:text></p>
			      <h4>$ <cq:text property="prodPrice"></cq:text></h4>
			      <h5>Options</h5>
			      <form id="form10" name="form1" method="post" action="/services/ebazaarresp">
            <%
            String prodPrice = (String)properties.get("prodPrice");
            String prodTitle = (String)properties.get("prodTitle");
            String prodCode = (String)properties.get("prodCode");
            String prodDescription = (String)properties.get("prodDescription");
            String prodCategories = (String)properties.get("prodCategories");
            String fileReference = (String)properties.get("fileReference");
            String applicationName = "";
            String redirect="";
            String language= "";
 			Node testNode = currentNode;
            if(testNode.hasProperty("viewCount")){
            	//out.println(".....count is available.......");
            	
            	Property references = testNode.getProperty("viewCount");     
            	String value = references.getValue().getString();
            	int val = Integer.parseInt(value);
            	val=val+1;
            	value = String.valueOf(val);
            	testNode.setProperty("viewCount",value);
                testNode.save();
            	
            }else{
            	//out.println(".....count is not available.......");
            	
            	testNode.setProperty("viewCount","1");
            	testNode.save();
            	
            }
            if(request.getRequestURI() != null){
                String requestUri[] = request.getRequestURI().split("/");
                applicationName = requestUri[2].toString();
                language= requestUri[3].toString();
            }
          // prodPrice=realPrice ;
            if(applicationName.equalsIgnoreCase("ebazzar"))
            {

              redirect = "/content/ebazzar/"+language+"/cart.html?prodPrice="+prodPrice+"&prodTitle="+prodTitle+"&prodCode="+prodCode+"&prodCategories="+prodCategories+"&fileReference="+fileReference+"&prodDescription="+prodDescription;
            }


            %>
			        <select class="span1" id="size" name="size">
			          <option>size</option>
			          <option>S</option>
			          <option>M</option>
			          <option>L</option>
			          <option>XL</option>
		            </select>
			        <select class="span1" id="color" name="color">
			          <option>color</option>
			          <option>red</option>
			          <option>green</option>
			          <option>blue</option>
		            </select>

                      <label>

                      <input type="text" id="quantity" name="quantity" value="1" class="input-mini"/>
			          &nbsp;Qty </label>
                      <input type="hidden" name="redirect" value="<%=redirect %>"/>
                      <input name="Add to Cart" type="submit" class="btn btn-success" value="Add to Cart" />
			        <!-- <a class="btn btn-success" href="shopping-cart.html">Add to Cart</a> -->
		          </form>
                    <a href="#">+ Add to whishlist</a> <a href="#"><i class="icon icon-twitter"></i></a> </div></div><br>


<div class="row">
			    <div class="span9">
                    <div class="tabbable"> 
			      <ul class="nav nav-tabs" id="tabs">
			        <li class="active"><a href="#description" data-toggle="tab">Description</a></li>
			        <li><a href="#reviews" data-toggle="tab"><span class="badge badge-inverse">10</span> Reviews</a></li>
		          </ul>
			      <div class="tab-content">
			        <div class="tab-pane active" id="description">
			          <p> <cq:text property="prodDescription"></cq:text> </p>
		            </div>
			        <div class="tab-pane" id="reviews">

<div class="accordion" id="accordion2">
							<div class="accordion-group">
							  <div class="accordion-heading">
								<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
								  <i class="icon-user"></i> By Anonymous on May 30, 2013
								</a>
							  </div>
							  <div id="collapseOne" class="accordion-body collapse in">
								<div class="accordion-inner">
								      <div class="ratings">
							                  <%
									starClass = "full";
										int j = 1;
										while (j <= 5) {

                  if(j > ratingToRender) {
			        if(ratingToRender > j-1) {
			            starClass = "full";
			            padding = (16-((ratingToRender-(j-1)) * 0.8f * 20));
			        }

			        else {
			            starClass = "empty";
			        }
			    }
			    %>

                        <div class="ratings-star <%= starClass %>" style="margin-right: <%= padding %>px; width: <%= 16 - padding %>px;"></div>
                                    <%
			    j++;
			}%>
                      

				  </div>
								<p><br>Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.</p>

								</div>
							  </div>
							</div>
							<div class="accordion-group">
							  <div class="accordion-heading">
								<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
								  <i class="icon-user"></i> By Xyz on Dec 31, 2013
								</a>
							  </div>
							  <div id="collapseTwo" class="accordion-body collapse">
								<div class="accordion-inner">
								<div class="ratings">
                                  <span class="ratings-star full"></span><span class="ratings-star full"></span><span class="ratings-star full"></span><span class="ratings-star empty"></span><span class="ratings-star empty"></span>
								</div>
								<p><br>Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.</p>

								</div>
							  </div>
							</div>
							<div class="accordion-group">
							  <div class="accordion-heading">
								<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThree">
								  <i class="icon-user"></i> By Anonymous on Sep 11, 2013
								</a>
							  </div>
							  <div id="collapseThree" class="accordion-body collapse">
								<div class="accordion-inner">
								<div class="ratings"> 
									<span class="ratings-star full"></span><span class="ratings-star empty"></span><span class="ratings-star empty"></span><span class="ratings-star empty"></span><span class="ratings-star empty"></span>
								</div>
								<p><br>Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.</p>

								</div>
							  </div>
							</div>
						  </div>

                      </div>
		          </div>
		        </div>
		      </div>
</div>
<cq:include resourceType="foundation/components/parsys" path="par"/> 


