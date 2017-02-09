<%--

  searchDoctor component.

  Search Doctor Component

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
                // TODO add you code here
%>
<div class="row  find_doctor mar_bot_25 fd_search">
    <div class="col-md-5  pad_left_20">
        <p class="subheading"> Search for Doctor</p>
            <form role="form" class="">

                <div class="form-group ">                                                
                     <label for="exampleInputEmail1" class="sign_label">Doctor's Name</label>                         
                      <input type="text" class="form-control text_normal" id="exampleInputEmail1" value=${properties.docName} >                         
                </div>


               <div class="form-group">
                   <div class="col-md-7 pad_zero">
                       <label for="exampleInputPassword1" class="sign_label">Speciality</label>
                   </div>

                   <div class="col-md-5">                                
                   </div>


                   <select id="type" class="dropdown_normal">
                                                                    <option selected="selected" value="${properties.select}" >${properties.select}</option>
                    <option  value="Cardio">cardio</option>
                    <option value="Dentist">dentist</option>
                    <option value="Dermatologist">dermatologist</option>
                    <option value="Oncologist">oncologist</option>
                    <option value="Surgeon">surgeon</option>
                    <option value="Radiologist">radiologist</option>
                    <option value="Neurologist">neurologist</option>

                    </select>
                </div>


                 <div class="row">
                     <div class="col-md-7 ">                                                
                          <label class="sign_label">Location *<span class="font12">(Zipcode, City, State)</span></label>                         
                           <input type="text" class="form-control text_medium" id="Location"  value=${properties.location} >                         
                      </div>                          

                     <div class="col-md-5 checkbox mar_top_37">                           
                          <label>
                              <input type="checkbox" class="check_custom" value=${properties.near}>           
                              <p class="check_para sign_label">Near Me Now</p>
                          </label>
                      </div>
                  </div>


                  <div class="row" >                          
                       <div class="col-md-8 width_55">
                           <label for="exampleInputPassword1" class="sign_label">Date</label>
                               <div class='input-group date' id='datetimepicker1' >
                                   <input type='text' class="form-control"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                                     </span>
                                </div>
                        </div>


                        <div class="col-md-4 pad_left_30">
                            <label for="exampleInputPassword1" class="sign_label"></label>

                         </div>


                          <label for="exampleInputPassword1" class="sign_label mar_top_10">Consultation</label>

                           <div class="col-md-12">                                 
                                   <label class="radio-inline">
                                      <input type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1"> Personal
                                    </label>
                                    <label class="radio-inline pad_left_60">
                                      <input type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2"> Online
                                    </label>
                            </div>    

                    </div>     


                    <!--<div class=" pad_top_20 pad_bot_22">
                       <a class="primary_button search_button "onclick="hitservlet();" id="search">Search</a>
                       <input type="hidden" id="requestType" value="doctorresults" />

                     </div>-->

                 <div class=" pad_top_20 pad_bot_22">                               
                             <!-- <button type="submit" class="primary_button">Search</button>   -->    
                             <a class="primary_button search_button " onclick="hitservlet();" id="search">Search</a>               
                               <button type="submit" class="primary_button" id="update_results" >Update Results</button>

               <input type="hidden" id="requestType" value="doctorresults" />
                </div>
                 </form>

             </div>                

  <div class="col-md-7 pad_left_15 def_img ">
                                <img src="/content/dam/LifePlus/Life_Find-Doc_Banner-Image.jpg" alt="Search Doc">                        
                </div>
                <div class="col-md-7 pad_left_15 map_search ">
                    <cq:include path="google_maps" resourceType="/apps/solution-accelerators/components/global/content/google-maps/google-maps.jsp"/>                   
                </div>


</div> 
<div id="result">
</div>
<div class="srch_div">
                    </div>


<script>
function hitservlet()
{
                var value=$("#Location").val();
    alert("value :: "+value);
                                $.ajax({
                                      url: "/bin/dbconnection",
                                      type: "post",
                                                                                  data: "v1=" + $("#exampleInputEmail1").val()+"&v2=" +$("#type").val()+"&v3=" +$("#Location").val()+"&requestType="+$("#requestType").val(),
                                  datatype: 'json',
                                      success: function(data){
                                    var json = JSON.parse(data);
                                    alert("data:"+data);
                                    var docResHtml="";


                                    docResHtml=docResHtml+"<div class='table-responsive '><table class='table table-striped'><thead><tr><th class='text-center'>Neurologist</th><th>Doctor's Availability                </th>";
                                    docResHtml=docResHtml+"<th>Practice Name</th><th>Expertise</th><th>Language/s Known</th></tr></thead>";

                                                                                                                                                docResHtml+="<tbody class='table_bg'>";
                                                                                                $.each(json, function(i, item) {
                                        docResHtml+="<tr><td><span class='left_align'><img src='/content/dam/LifePlus/Find-Doc_Location-pin_unavailabe.png' alt='unavailable' class='pad_left_20'><img src='/content/dam/LifePlus/Find-Doc_results_profile-pic-1.png' alt='doc' class='pad_left_20'></span>";



                                    docResHtml=docResHtml+"<span class='class_tb'><a class='doc_individual'>  <b>"+ json[i].doctor_Name+"</b></a>"+json[i].speciality_id+",</br>"+json[i].doctor_Location+"</br>New York,<br>"+json[i].doctor_zip+" <br> </span>"
                                    docResHtml+="<span class='mar_left_46per'><img src='/content/dam/LifePlus/Find-Doc_rating_coloured.png' alt='star'><img src='/content/dam/LifePlus/Find-Doc_rating_coloured.png' alt='star'><img src='/content/dam/LifePlus/Find-Doc_rating_coloured.png' alt='star'><img src='/content/dam/LifePlus/Find-Doc_rating_coloured.png' alt='star'><img src='/content/dam/LifePlus/Find-Doc_rating_coloured.png' alt='star'></span><span><a  class='btn btn-primary btn-lg mar_left_46per' role='button' id='book'>Book</a></span></td>"
                                    docResHtml+="<td><p><b>Not Available</b> Next Availability: Thu, 20th Nov 2014</p></td><td class='td_3'>Princston Plainsbro</td><td class='td_4'>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</td><td class='td_5'>English,Spanish</td>";
                                    docResHtml=docResHtml+"</tr>";

                                                                                                });


                                                                                                                                docResHtml=docResHtml+"</tbody></table></div>";
                                                                                                                                                $(".srch_div").append(docResHtml);


}                                                                                                                                              
                                    }); 

}



</script>  

      <script>
            $(function () {
                $('#datetimepicker1').datepicker();                         
                                                                                });


$('#search').click(function(){
$('.find_doc_by').hide();
                                                                                $('.testimonial_bg').hide(); 
                                                                                 $('.def_img').hide();
                     $('.map_search').show();
                                                                                $('.srch_div').show();    
                                                                                $('#search').hide();
                                                                                $('#update_results').show();
                                                                                $('.pagination_div').show();       
                                                                                $("#verification").addClass("display_none");                     

});



$('.srch_div').hide();       
$('.map_search').hide();
                                                                $('#update_results').hide(); 
    $('#book').click(function(){
                $('.find_doctor').hide();               
                $('.srch_div').hide();       
                $('.pagination_div').hide();                                                                        
                 $('.book_appointment').show();                                                                                                                             
                                                                  });

</script>
