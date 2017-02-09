<%@include file="/libs/foundation/global.jsp" %>
<%@page import="java.io.*,java.net.*"%>
<%@page import="com.day.cq.security.profile.Profile"%>

<%
    // Clean up Session Value
    // if already loaded (if the user has already navigated to the product page)
    javax.servlet.http.HttpSession session = slingRequest.getSession();
    
    String sProdCodeSession = (String)session.getAttribute("pCode");

    if(sProdCodeSession != null && !"".equals(sProdCodeSession.trim()))
    {
       // out.println("clearing value"); 
        session.setAttribute("pCode", null);
    }
%>

<%
  
    /*
    String geoCity = "my city";

    com.day.cq.security.profile.Profile currentProfile = slingRequest.adaptTo(Profile.class);
    geoCity =(String)currentProfile.get("city");
    */    
    
%>

<script>
var myCity;
function getCityNameForLatitude(lat, lang, cookKey)
{
    // For the First time, if the latitude and Longitudes
    // are not received, make the climate as rainly
    // This is only for Geolocation
    if(lat == undefined || lang == undefined)
    {
        //alert("Undefined");
        setCookie(cookKey, "rain", 1); 
        return;
    }
    
    
    var city='';
    
    var url = 'http://maps.googleapis.com/maps/api/geocode/xml?'
           + 'latlng=' + lat + ',' + lang 
           + '&sensor=true';

    var xmlhttp;

            if(window.XMLHttpRequest)
            {
                xmlhttp = new XMLHttpRequest();
                if(window.DOMParser)
                {
                    xmlhttp.open('GET',url, true);
                }
                else
                {
                    xmlhttp.open('GET',url, false);
                }

            }
            else
            {
            }   

    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState==4 && xmlhttp.status==200)
            {
            
            var xmlDoc;
                            
                if(!window.DOMParser)
                {
                    xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
                    xmlDoc.loadXML(xmlhttp.responseText);
                }
                else
                {
                    parser = new DOMParser();
                    xmlDoc = parser.parseFromString(xmlhttp.responseText,"text/xml");
                }

            city = xmlDoc.getElementsByTagName('long_name')[4].childNodes[0].nodeValue;

            // Now get the Weather
            weatherService_JSONP(city, cookKey);
        }
    };

    xmlhttp.send();

    return city;

}

function weatherService_JSONP(city, cookKey)
{


    var uri = "http://api.worldweatheronline.com/free/v1/weather.ashx?q="
                 + city 
                 + '&format=json&num_of_days=2&key=5tyz8trtc4tz5xcjq4ngtvwe';
 
     // uri-encode it to prevent errors :
     uri = encodeURI(uri); 

    // alert("cookKey:"+ cookKey);
     jQuery.get(uri, function(result)
         {
              if(cookKey == "AccountCity")
              {

              } 

              if(result.data.error) {

               // error exists, so display it

                  
                  this.current_condition = "No Weather Info !";
              } 
              else 
              {
                 var current_condition = result.data.current_condition[0].weatherDesc[0].value;

                 setCookie(cookKey, current_condition, 1);

                 
             }//end if

         },
                         
     "jsonp"); // end jQuery.ajax

     //return current_condition;
 }


function setCookie(c_name,value,exdays){
    var exdate=new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
    document.cookie=c_name + "=" + c_value;
    
}

var geoPlance = getCityNameForLatitude(ClientContext.get("geolocation/latitude"), 
                                       ClientContext.get("geolocation/longitude"),
                                       "GeoCity");
//alert("City is : " + myCity);
//setCookie("GeoCity", "Sunny", 1);

// For Profile City
if(CQ_Analytics.ProfileDataMgr.getProperty("city") != null && CQ_Analytics.ProfileDataMgr.getProperty("city") != "")
{

    //setCookie("AccountCity", "Sunny", 1); 
    weatherService_JSONP(
            CQ_Analytics.ProfileDataMgr.getProperty("city"),
            "AccountCity"
            );
}
    

</script>