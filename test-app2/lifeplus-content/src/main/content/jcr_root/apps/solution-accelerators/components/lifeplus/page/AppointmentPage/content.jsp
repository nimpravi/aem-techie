<%@include file="/libs/foundation/global.jsp" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.day.cq.wcm.foundation.Image" %>
<%@ page import="org.apache.sling.commons.json.JSONArray" %>
<%@page import="java.util.*,com.day.cq.wcm.api.WCMMode" %>
<%@page import="com.cts.accelerators.lifeplus.helpers.ScheduleTimeHelper" %>
<%@page import="com.cts.accelerators.lifeplus.core.DatabaseConnectionHelper" %>
<%@page import="java.sql.Connection" %>
<%@page import="org.apache.sling.commons.json.JSONObject" %>
<%@page session="false" %>
     <% /* ScheduleTimeHelper sth=new ScheduleTimeHelper(); Connection con=n ull; DatabaseConnectionHelper dataBaseHelper=n ew DatabaseConnectionHelper(); con=d ataBaseHelper.getConnection(); JSONObject jo=n ew JSONObject(); JSONObject object=n ew JSONObject(); JSONArray arrayJson=new JSONArray(); jo=sth.appointmentScheduleTiming(slingRequest,con); out.print(jo); /*Iterator iter=j o.keys(); Object[] hospitalId=new Object[20]; String[] hospital_Id=new String[20]; int i=0; while(iter.hasNext()) { hospitalId[i]=iter.next(); //out.print( "hos_id+++++++++"+hospitalId[i]); hospital_Id[i]=hospitalId[i].toString(); // out.print( "hos_id+++++++++"+hospital_Id[i]); JSONObject values=jo.getJSONObject(hospital_Id[i]); //out.print( "values+++++++++"+values); Iterator iter1=v alues.keys(); int j=0; Object[] days=new Object[20]; String[] days_string=new String[20]; Object[] availability=new Object[20]; while(iter1.hasNext()){ days[j]=iter1.next(); //out.print( "days+++++++++"+days[j]); days_string[j]=days[j].toString(); availability[j]=values.get(days_string[j]); //out.print( "availability+++++++++"+availability[j]); j++; } i++; } */ %>

                                            <form id="detailsForm" name="detailsForm" action="/bin/dbconnection" method="POST">

                                                <input type="hidden" id="requestType" name="requestType" value="scheduletime" />

                                            </form>


                                            <div class="row doc_details" style="display: block;">

                                              <sling:include path="Appoint_schedule" resourceType="/apps/solution-accelerators/components/lifeplus/content/AppointmentDocInfo" addSelectors="AppointmentSchedule" />
                                                <div class="col-md-7">
                                                    <!--Maps-->
                                                </div>

                                            </div>

<div class="container pad_left_15 pad_right_15">
    <div class="row appointment "style="display: block;">
        <p class="heading"> APPOINTMENTS</p>
         <p class="text-right">
                                                        <img src="/content/dam/LifePlus/Life_Banner_dot_active.png" alt="available" class="pad_right_5">
                                                        <span class="pad_right_15"> Available</span>
                                                        <img src="/content/dam/LifePlus/Life_Banner_dot_inactive.png" alt="unavailable" class="pad_right_5">
                                                        <span> Not Available</span>
                                                    </p>




   <div class="date_avail">
   <div class=" row date_slide">
   <!-- Carousel 2 starts-->
    <div class="col-md-8 pull-right">
      <div id="myCarousel1" class="carousel slide">




                </div>
           </div>
         </div>

<div class="row border_bot_ccc"> 

    <sling:include path="Appoint_schedule1" resourceType="/apps/solution-accelerators/components/lifeplus/content/AppointmentDocInfo" addSelectors="AppointmentSchedule" />

      <div class="col-md-7 mar_left_9per pad_top_20 appResults">


     </div>
        </div>

</div>
        </div>
        </div>


<script>
var dateGlobal;
    var doc_idGlobal;
  $(document).ready(function() {


      var url = document.URL;
      var params = url.split("/")

      var length = params.length;


       var values = params[length-1];
      var queryParams = values.split(".");

      var Docid =queryParams[1];
    doc_idGlobal = Docid;



      var Date =queryParams[2].split("date")[1];


      Date=  Date.replace( /\-/g,"/");
      dateGlobal = Date;



     hitservlet(doc_idGlobal, dateGlobal);





});
    function dateIncrement(date ,id)
      {

          if(id == "increment")
             {

          var tomorrow = new Date(date);
tomorrow.setDate(tomorrow.getDate() + 7);

      }
          else
          {
var tomorrow = new Date(date);
tomorrow.setDate(tomorrow.getDate() - 7);


          }
var Docid = doc_idGlobal;



          var dateString = tomorrow.format("m/d/Y");
          alert("dateString : "+dateString);
          hitservlet(Docid, dateString);

      }
</script>
<script>
var count = 0;
     function hitservlet(doc_id, date) {
      alert("going");

      var docResHtml = "";
                  var docDetHtml = "";
      var slotshtml="";
                var URL = $("#detailsForm").attr("action");

     var queryParams = "requestType=" + $("#requestType").val() + "&doc_id=" + doc_id + "&date=" + date;

      var jsonResponse;
       var slots;
                $.post(URL, queryParams, function(data) {
                                                 jsonResponse = data;
                                                }, "json").done(function() {
          var i = 1;
          var jsonArray = jsonResponse.hospitalSlots;

            docResHtml = docResHtml +"<div class='carousel-inner pad_left_30'><div class='item active' id='notransition'><ul class='date_list'>"
      $.each(jsonArray, function(i, item) {


            $.each(item.slots, function(i, item) {



          docResHtml = docResHtml +"<li><span>"+i.substring(0, 3)+"</span>"
          docResHtml = docResHtml +"<span>"+i.substring(4)+"</span></li>"


slots=JSON.stringify(item);

                slotshtml= slotshtml+"<div class='col-md-1 mar_right_5per app_time' ><ul class='list-unstyled time_avail'> " ;
                count = 0;
                 $.each(item, function(k, item1) {



var str2 = "DR";



if(k.indexOf(str2) != -1)
{
    slotshtml= slotshtml+ "<li><a class='timing_book'> 8:00 AM </a></li><li><a class='timing_book'> 8:30 AM </a></li><li><a class='timing_book'> 9:00 AM </a></li><li><a class='timing_book'> 9:30 AM </a></li><li><a class='timing_book'> 10:00 AM </a></li></ul><ul class='list-unstyled time_avail'><li><a class='timing_book'> 10:30 AM </a></li>"
  +"<li><a class='timing_book'> 11:00 AM </a></li><li><a class='timing_book'> 11:30 AM </a></li><li><a class='timing_book'> 12:00 AM </a></li><li><a class='timing_book'> 1:00 PM </a></li><li><a class='timing_book'> 1:30 PM </a></li><li><a class='timing_book'> 2:00 PM </a></li><li><a class='timing_book'> 2:30 PM </a></li><li><a class='timing_book'> 3:00 PM </a></li>"
}
                     else
                     {

                      if(item1=="Available" && count < 5)
                      {

             slotshtml= slotshtml+"<li><a class='timing_book green_primary'>"+k+"</a></li>";


                      }

                         if(item1=="Available" && count==5) {
                             slotshtml= slotshtml+"</ul><ul class='list-unstyled time_avail' style='display:none'>";
                         }

                     if(item1=="Available" && count >= 5)
                     {



slotshtml= slotshtml+"<li><a class='timing_book green_primary'> "+k+"</a></li>";

                     }

                     }

count++;
          });
                slotshtml= slotshtml+"</ul></div>";

        $(".appResults").html(slotshtml);

          });




  $(".carousel-inner").html(docResHtml);



    });

            slotshtml= slotshtml+"<span class='load-more'>Load More</span>";
                $(".appResults").html(slotshtml);
            var datecall="dateIncrement("+date+")";
            var inc="increment";
            var dec="decrement";
            docResHtml = docResHtml +"</ul></div></div>"
          docResHtml = docResHtml +"<a id='decrement'class='left carousel-control carousel-prev'onclick='dateIncrement(\""+date+"\",\""+dec+"\")' role='button' data-slide='prev'>"
                                +"<span aria-hidden='true'><img src='/content/dam/LifePlus/Find-Doc_icon_calendar_left.png' alt='Previous'></span>"
                                +"<span class='sr-only'>Previous</span></a><a id='incrememt' class='right carousel-control carousel-next' onclick='dateIncrement(\""+date+"\",\""+inc+"\")'  role='button' data-slide='next'>"
                                +"<span aria-hidden='true'> <img src='/content/dam/LifePlus/Find-Doc_icon_calendar_right.png' alt='Next'></span>"
                                +"<span class='sr-only'>Next</span></a>"




             $("#myCarousel1").html(docResHtml);

                      + "<span class='class_tb'>  <b> Dr. Eric Foreman, MD</b></span>"
                      +" <span>Neurologist</span>"
                       +"<span class=' mar_left_17per'> 1 Gustave L. Levy Pl, </span>"
                       +"<span class=' mar_left_17per'> New York, NY</span>"
                       +"<span class=' mar_left_17per '> (212) 241-6500 </span>"
                       +"<span class=' mar_left_17per pad_top_20'> Mon -Sat</span>"
                       +"<span class=' mar_left_17per'> 8.00 A.m - 12.00 P.m</span>"                



});



}
</script>
