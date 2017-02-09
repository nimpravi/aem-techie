<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  LifePlus Header script.

  This component will be used for displaying the contents of the header component

  ==============================================================================

--%>
<%

%><%@include file="/libs/foundation/global.jsp"%>
<%
	
%><%@page session="false"%>
<%

%>

    <!-- top nav start -->

    <div class="row nav_main">
               <div class="col-md-10 col-xs-push-2">  
                  <ul class="main_menu">
 <cq:include path="prim_nav" resourceType="/apps/solution-accelerators/components/global/content/page_nav"/>
 <cq:include path="logged_in" resourceType="/apps/solution-accelerators/components/lifeplus/content/userloggedIn"/>
                                   </ul>
                  <!--<form class="navbar-form" role="search">
                     <div class="input-group">
                     
                         <input type="text" class="form-control" placeholder="Search" name="srch-term" id="srch-term">
                        
                         <div class="input-group-btn">
                             <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                         </div>
                     </div>
                     </form>-->
               </div>
            </div>
    <!-- top nav end -->

    <!-- logo & main nav start -->

    <div class="row nav_secondary">
        <div class="col-md-2 lifeplus">
        <!-- logo -->
               <cq:include path="logo"
				resourceType="solution-accelerators/components/global/content/logo" />

        </div>
         <!-- main nav -->
        <cq:include path="topnav"
				resourceType="solution-accelerators/components/global/content/topnav" />


    <!-- logo & main nav end -->
    </div>

<script type="text/javascript">


    var jsonData = {"Values": [
        {}
    ],
    "coloumnName": "",
        "appointment_details": [
                        {
                            "docName": "Dr. Eric Foreman, MD",
                            "speciality": "Neurologist",
                            "address1": "1 Gustave L. Levy Pl,",
                            "address2": "New York, NY",
                            "zip" : "560045",
                            "telephone": "(212) 241-6500",
                            "date": "Sat, 15 NOv 2014| 8.00 A.m",
                            "image":"/content/dam/LifePlus/Life_Testimonials_profile-pic_2.png",
                            "reason": "",
                            "insurance": "",
                            "flname": "",
                            "gender": "",
                            "pemail": ""

                        }
                    ]
                }
	</script>




