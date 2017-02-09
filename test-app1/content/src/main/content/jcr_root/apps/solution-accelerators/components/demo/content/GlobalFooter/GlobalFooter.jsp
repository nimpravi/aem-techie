<%--

  Footer component.

  GlobalFooter

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
	// TODO add you code here
%>
<footer class="footer">
 <div class="container">
<div class="row footer-head">
<div class="col-md-2"><a>Product Catalog</a> </div>
<div class="col-md-2"><a>Brand Site Information</a> </div>
<div class="col-md-2"> <a>Faq</a></div>
<div class="col-md-2"> <a>Reach Us</a></div>
<div class="col-md-3"> <ul class="social pull-left">
                     <li><a href="http://youtube.com/"><img src="/content/dam/demo-kao/youtube.png"></a></li>
                    <li><a href="http://twiter.com/"><img src="/content/dam/demo-kao/twitter.png"></a></li>
                     <li><a href="http://facebook.com/"><img src="/content/dam/demo-kao/facebook.png"></a></li>
                      <li><a href="#"><img src="/content/dam/demo-kao/share.png"></a></li>
                         </ul></div>

<div class="col-md-1"><a id="gototop" data-toggle="collapse" data-parent="#accordion" href="#demo" class="gototop pull-left"><i class="kaoup fa fa-angle-down"></i></a></div>
</div>
</div>

<div class="footerlast container panel-collapse collapse" id="demo">
<div class="row">

<cq:include path="listoflink1" resourceType="/apps/solution-accelerators/components/demo/content/listoflinks" />
    <cq:include path="listoflink2" resourceType="/apps/solution-accelerators/components/demo/content/listoflinks" />
    <cq:include path="listoflink3" resourceType="/apps/solution-accelerators/components/demo/content/listoflinks" />
    <cq:include path="listoflink4" resourceType="/apps/solution-accelerators/components/demo/content/listoflinks" />
<div class="col-md-4"> <img src="/content/dam/demo-kao/logofooter.png"></div>


</div>
</div>

<div class="container" style="background-color:#FFF; padding-top:10px; padding-bottom:10px;">
        <div class="row-fluid">
            <div class="col-md-4">
                Copyright @ Kao Corporation All Rights Reserved.
            </div>
          

            <div class=" col-md-4">
               
            </div>

            <div class="col-md-4">
                Terms and Conditions | Privacy Policy | Social media Policy 
            </div>
            <!--/Goto Top-->
        </div>
    </div>
</footer>