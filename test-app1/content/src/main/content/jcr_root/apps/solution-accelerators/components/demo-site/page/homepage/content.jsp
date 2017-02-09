<%--

  Homepage component.

--%>

<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%@page import="com.day.cq.wcm.api.WCMMode" %>
<script>
    $( document ).ready(function() {
        var curTopLevelIndx = 0;
    });
</script>
<style>
			#container{max-width:982px;}
			.footer{max-width:982px;}
			@media only screen and (min-width : 941px) and (max-width : 1024px){
				#container{max-width:940px;}
				.footer{max-width:940px;}
			}
			@media only screen and (min-width : 721px)  and (max-width : 940px) {
				#container{max-width:720px;}
				.footer{max-width:720px;}
			}
			@media only screen and (min-width : 421px)  and (max-width : 720px) {
				#container{width:100%}
				#pgContent{position:relative; max-width:420px;margin-left:auto;margin-right:auto;}
				.footer{width:100%;}
			}
			@media only screen and (max-width : 420px) {
				#container{width:100%;}
				#pgContent{position:relative; max-width:280px;margin-left:auto;margin-right:auto;}
				.footer{width:100%;}
			}
			
			
			/*.mainnavigation {float:left;}*/
			#bg{
				position: fixed;
				top: 0px;
				left: 0px;
				/*width: 1353px;*/
				height: 100%;
				background-image:url('/content/dam/demo-site/bg.jpg');
				background-repeat:no-repeat;
				background-size: 100% 100%;
			}
			#shadowLeft{
				position: absolute;
				top: 0px;
				left: 0px;
				width: 195px;
				height: 100%;
				background-image:url('/content/dam/demo-site/Background_Shadow_left.png');
				background-repeat: repeat-y;
			}
			#shadowRight{
				position: absolute;
				top: 0px;
				right: 0px;
				width: 196px;
				height: 100%;
				background-image:url('/content/dam/demo-site/Background_Shadow_right.png');
				background-repeat: repeat-y;
			}
			#shadowBottom{
				position: absolute;
				bottom: 0px;
				left: 0px;
				width: 100%;
				height: 131px;
				background-image:url('/content/dam/demo-site/Background_Shadow_Bottom.png');
				background-repeat: repeat-x;
			}
			.hideBGImg{
				background-image:none;
			}

			#propInfo{
				display: none;
				position: absolute;
				left: 0px;
				top: 0px;
			}		



		</style>

<cq:include path="custompar" resourceType="foundation/components/parsys" />

