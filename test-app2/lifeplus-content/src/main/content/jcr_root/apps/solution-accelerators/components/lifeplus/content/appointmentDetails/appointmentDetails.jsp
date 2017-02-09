<%--

  appointmentDetails component.

  Appointment details for booking doctors

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%>

<div class="appointment_details">
                      <ul class="nav nav-pills pad_bot_15 steps">
                         <li class="active" id="appointment"><a> Appointment Details</a><span class="appointmentClass">1</span></li>                     
                         <li class="disabled display_none" id="verification"><a>Verification</a> <span class="appointmentClass">2</span></li>
                         <li class="disabled" id="confirmation"><a>Confirmation</a> <span class="appointmentClass">3</span></li>                   
                      </ul>                      
                      <div class="tab-content">
                         <div class="tab-pane active " id="step1">
                           <div>
                             <label class="sign_label"> Reason for your visit?</label>
                             <span class="filter">
                                <select id="type" class="reason">



                                    <option selected="selected" value="Select a question">Select a question</option>
                                    <option value="Constant migranes">Constant migraines </option>
                                   <option value="Constant headache">Constant headache</option>
                                   <option value="Constant stomachache">Constant stomachache</option>                          
                                   <option value="Constant eye pain">Constant eye pain</option>   
                                      <!--<option selected="selected" value="${properties.select}" >${properties.select}</option>-->

                                </select>
                             </span>
                           
                             <label class="sign_label mar_top_10"> Insurance</label>
                             <span class="filter">
                                <select id="type1" class="reason">
                                   <option selected="selected" value="Select a question">Select a question</option>
                                   <option value="Im paying">Im paying </option>
                                   <option value="I'm not paying">I'm not paying</option>
                                    <option value="I dont know">I dont know</option>

                                </select>
                             </span>
                             <label class="sign_label mar_top_10 block" id="appointmentFor" value=""> The appointment is for</label>
                             <label class="radio-inline"><input type="radio" name="optradio" id="opt1" value="me" onclick="showhidediv(this.id);">Me</label>
                            <label class="radio-inline pad_left_60"><input type="radio" name="optradio" id="opt2" value="someone else" onclick="showhidediv(this.id);">Someone else</label>
                           </div> 
                            <div class="patient_details" style="display: none;">
                                <label class="mar_top_10 block"> Patient Details</label>
                                <div class="form-group">
                                    <label class="sign_label mar_top_10 block"  value="First and Last name"> First and Last name</label>
                                    <input type="email" id="flname" class="form-control text_normal">
                                </div> 
                                <label class="sign_label mar_top_10 block"> Gender</label>
                                <span class="filter">
                                    <select class="dropdown_small"  id="gender">
                                       <option selected="selected" value="Gender">Gender</option>
                                       <option value="Male">Male </option>
                                       <option value="Female">Female</option>                                                      
                                    </select>
                                </span>                        
                                <div class="form-group">
                                    <label class="sign_label mar_top_10 block"value="email"> Email (Optional)</label>
                                    <input type="email"  id="pemail" class="form-control text_normal">
                                </div>                                
                            </div>
                            <button type="submit" class="primary_button mar_top_10" id="continue">Continue</button>   
                         </div>  
                          <!-- tab 1 ends-->                
                         <div class="tab-pane " id="step2">
                            <p>Verify your mobile number</p>
                            <p> * Doctors need your mobile number to confirm your review.  </p>
                            <div class="send_code">
                                <p> We will send a verification code to this  number instantly via SMS  </p>
                                <select id="type" class="mini">
							  <option selected="selected" value="50">+91</option>
							  <option value="51">+123</option>
							  <option value="48">+4567</option>
							  <option value="49">+8987</option>
						   </select>
						   
						   <input class="form-control medium">
                                <button type="submit" class="primary_button mar_top_10">Send code</button> 
                            </div>
                            <div class="verify_code" style="display: none;">
                            	<span> +1 234 78970 <a href="#" class="forgot_pwd pad_left_30">Change</a> </span>
                                <p class="pad_top_20">Enter  verification code </p>
                                <p> Please enter the 6 digit verification code sent to your mobile number  </p>
                                <input class="form-control text_small" id="verify" value="123">
                                <button type="submit" class="primary_button mar_top_10 " id="verify_code">verify code</button> 
                            </div>
                         </div>
                           <!-- Tab 2 ends-->
                          <div class="tab-pane " id="step3">
                             <p> Your appointment is confirmed </p>
                             <p> Please carry a photo ID and insurance details with you. </p>
                             <div class="map">
                               <cq:include path="google_maps"
                    resourceType="/apps/solution-accelerators/components/global/content/google-maps" />
                                 <!-- Map-->

                     		 </div>  <!-- Map-->
                         </div>
                      </div> 
                         <!-- tab content ends-->                      
                  </div> 
<input type="hidden" value="bookYourAppointment" id="requestType" />
<input type="hidden" id="member_id" name="member_id" value="0">

<script type="text/javascript" src="/etc/designs/lifeplus/clientlibs/js/appointmentDetails.js"></script> 
