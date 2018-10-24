<%--

Generic Search Component

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
                // TODO add you code here
%>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.day.cq.wcm.foundation.Image" %>
<%@ page import="org.apache.sling.commons.json.JSONArray" %>
<%@page import="java.util.*,com.day.cq.wcm.api.WCMMode"%>

<%@ taglib prefix="acc" uri="http://localhost:4502/bin/customComponents"%>

<% 

    if (currentNode.getPath()!=null)
{
String fieldsNodePath = currentNode.getPath()+"/fields";
                String imagePath = "";

    if (null != properties.get("image/fileReference")) {
                                imagePath = properties.get("image/fileReference", "");
                }
    %>


 <!-- Search component Starts-->
<div class="row searchComponent ">
  <div class="col-md-5">
       <!-- Title of the form-->
    <p class="subheading">${properties.formTitle}</p>

      <!-- Form Starts-->
       <form name="formName" role="form">
          <c:set var="fieldsNodePath" value="<%=fieldsNodePath%>"/>

           <!-- Using the custom components tag library for fetching the array list of Generic Search details and row details -->

           <acc:customForm fieldsNodePath="${fieldsNodePath}" genericSearchList="values" rowList="rows"/>


           <c:set var="arraySize" value="${fn:length(values)}"/>

           <c:set var="rowListSize" value="${fn:length(rows)}"/>

           <c:set var="outerLoopVar" value="0"/>

           <!-- Iterating through the list to fetch the corresponding field detail -->


           <c:forEach items="${values}" var="field" begin="0" end="${arraySize}" varStatus="i">


               <c:if test="${values[outerLoopVar].addGroup!='on'}">


                   <div class="form-group ">                         

                <!-- Non-Predictive fields Start-->


                       <c:if test="${values[outerLoopVar].predictive=='no'}">
                                <c:if test="${values[outerLoopVar].select=='text'}">
                                    <label  class="sign_label">${values[outerLoopVar].fieldLabel}<span class="font12">${values[outerLoopVar].fieldLabelDescription}</span></label><br>
                                    <input type="${values[outerLoopVar].select}" class="${values[outerLoopVar].fieldWidth}" name="fieldLabel">  
                                </c:if>
            
                                <c:if test="${values[outerLoopVar].select=='selectionfield'}">
                                    <label class="sign_label">${values[outerLoopVar].fieldLabel}</label>			
                                    <select  class="dropdown_normal">
                                          <c:forEach items="${values[outerLoopVar].selectionOptions}" var="option" >
                                            <option value="${option}">${option}</option>
                                          </c:forEach>
                                    </select>
                                </c:if>
                                <c:if test="${(values[outerLoopVar].select=='checkbox')}">
                                    <input type="${values[outerLoopVar].select}" id="check_${outerLoopVar}"  name="fieldLabel">	
                                    <label for="check_${outerLoopVar}" >${values[outerLoopVar].fieldLabel}</label>
                                </c:if>
                                <c:if test="${values[outerLoopVar].select=='date'}">
                                    <label class="sign_label">${values[outerLoopVar].fieldLabel}</label>
                                       <div class='date'>                                       
                                          <input type="text" class="form-control date_input" name="fieldLabel">
                                       </div> 
                                </c:if>

                                <c:if test="${(values[outerLoopVar].select=='radio')}">
                                     <div> <label class="sign_label ">${values[outerLoopVar].fieldLabel}</label> </div> 
            
                                   <input type="radio"  name="fieldLabel" >${values[outerLoopVar].fieldLabel}                          
                                </c:if>
                        </c:if>
                <!-- Non-Predictive fields End-->

                <!-- Predictive Text field Starts-->
                        <c:if test="${values[outerLoopVar].predictive=='yes'}">
            
                            <c:set var="name1" value="fieldLabel_0${i.count}"/>
                            <c:set var="name2" value="fieldLabel_1${i.count}"/>
            
                            <label class="sign_label">${values[outerLoopVar].fieldLabel}<span class="font12">${values[outerLoopVar].fieldLabelDescription}</span></label><br>
                            <input class="form-control normal tags" name="fieldLabel"  onfocus="myFunction(${i.count})">
                            <input type="hidden" name="${name1}" value="${values[outerLoopVar].tableName}">  
                            <input type="hidden" name="${name2}" value="${values[outerLoopVar].fieldQueryParameter}">   
            
                        </c:if>

                <!-- Predictive Text field Ends-->

                   </div>

                   <c:set var="outerLoopVar" value="${outerLoopVar+1}"/>

               </c:if>

               <!-- Grouping of fields Row-wise Starts-->
                       <c:if test="${values[outerLoopVar].addGroup=='on'}">
                
                              <c:forEach items="${rows}" var="rowDetails" begin="0" end="${rowListSize}">
                                 <c:set var="labelCount" value="0"/>
                                  <div class="form-group">
                                   <c:forEach items="${values}" var="field" begin="${outerLoopVar}" end="${arraySize}">
                                        <c:set var="count" value="0" />
                
                                       <c:if test="${values[outerLoopVar].groupName==rowDetails && field.addGroup=='on'}">
                
                                         <c:if test="${values[outerLoopVar].select=='text'}">
                                             <label class="sign_label">${values[outerLoopVar].fieldLabel}<span class="font12">${values[outerLoopVar].fieldLabelDescription}</span></label><br>  
                                             <input type="${values[outerLoopVar].select}" class="${values[outerLoopVar].fieldWidth}" name="fieldLabel">
                                             <c:set var="count" value="${count+1}"/>
                                          </c:if>
                
                                          <c:if test="${(values[outerLoopVar].select=='checkbox')}">
                                                <input type="${values[outerLoopVar].select}" id="check_${outerLoopVar}" name="fieldLabel">
                                                <label for="check_${outerLoopVar}">${values[outerLoopVar].fieldLabel}</label>  
                                                <c:set var="count" value="${count+1}"/>
                                          </c:if>
                
                                           <c:if test="${(values[outerLoopVar].select=='radio')}">
                                               <c:if test="${labelCount==0}">
                                                   <div> 
                                                        <label class="sign_label ">${values[outerLoopVar].fieldLabel}</label> 
                                                   </div>
                
                                                   <input type="radio"  name="fieldLabel">${values[outerLoopVar].fieldLabelDescription}  
                                                </c:if>
                
                                               <c:if test="${labelCount!=0}" >
                                               <input type="radio"  name="fieldLabel" style="margin-left: 50px;">${values[outerLoopVar].fieldLabelDescription}
                                               </c:if>
                
                                               <c:set var="labelCount" value="${labelCount+1}"/>
                                               <c:set var="count" value="${count+1}"/>
                                           </c:if>
                
                                        <c:set var="outerLoopVar" value="${outerLoopVar+count}"/>
                
                                      </c:if>
                              </c:forEach>
                
                            </div>
                
                        </c:forEach>
                
                     </c:if>
                
                <!-- Grouping of fields Row-wise Ends-->


           </c:forEach>


           <!-- Submit and Update results Buttons-->
              <div class=" form-group"> 
            
                  <a class="primary_button " onclick="getDetails();"  id="search">Search</a>       

                  <a class="primary_button " onclick="getDetails();"  id="update_results">Update Results</a>         
            
               </div>

      </form>

       <!-- Form ends-->

    </div>

    <!-- Image corresponding to the form starts-->

            <div class="col-md-7 pad_left_15 def_img ">
                <c:set var="imagePath" value="<%=imagePath%>"/>
                <img src="${imagePath}">
            </div>
    <!-- Image corresponding to the form ends-->

    <!-- Google Map starts-->

            <div class="col-md-7 pad_left_15 map_search ">
                <cq:include path="google_maps" resourceType="/apps/solution-accelerators/components/global/content/google-maps" />
            </div>
    <!-- Google Map ends-->
</div>
<!-- Search component ends-->

<%
}%>
   <input type="hidden" id="requestType" value="doctorresults" />
   <input type="hidden" id="jsonObj" value="{}" />
<div class="srch_div doctor-search-container" data-pagenumber="1" data-records-perpage="5">
</div>

