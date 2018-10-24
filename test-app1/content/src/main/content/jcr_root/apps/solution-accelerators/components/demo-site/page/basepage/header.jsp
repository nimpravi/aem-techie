<%--
  Copyright Cognizant.  
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Cognizant, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Cognizant.

  ==============================================================================

  Accelerator Header script.

  This component will be used for displaying the contents of the header component

  ==============================================================================

--%>
<%
	
%><%@include file="/libs/foundation/global.jsp"%>
<%
	
%><%@page session="false"%>
<%

%>

<style type="text/css">
		.dlg-no-close .ui-dialog-titlebar-close {
			display: none;
		}
		.dlg-no-title .ui-dialog-titlebar {
			display: none;
		}

	</style>

<script>
			function hoverOnPinUnpin(element) {
				if( element.getAttribute('class') == 'pin_me') {
					element.setAttribute('src', '/content/dam/demo-site/Pin_icon_Hover.png');
				} else {
					element.setAttribute('src', '/content/dam/demo-site/Unpin_icon_Hover.png');
				}
			}
			function unhoverOnPinUnpin(element) {
				if( element.getAttribute('class') == 'pin_me') {
					element.setAttribute('src', '/content/dam/demo-site/Pin_icon.png');
				} else {
					element.setAttribute('src', '/content/dam/demo-site/Unpin_icon.png');
				}
			}
			
			function hover(element) {
				element.setAttribute('src', '/content/dam/demo-site/Yellow_star.png');
			}
			function unhover(element) {
				element.setAttribute('src', '/content/dam/demo-site/Gray_star.png');
			}
			
			function hoverHelpIcon(element) {
				element.setAttribute('src', '/content/dam/demo-site/Help_icon_hover.png');
			}
			function unhoverHelpIcon(element) {
				element.setAttribute('src', '/content/dam/demo-site/Help_icon.png');
			}
			
			function hoverOnPropertyMob(element) {
				document.getElementById("propertyImg").setAttribute('src','/content/dam/demo-site/MP_icon_location_ro_22x22.png');
			}
			function unhoverOnPropertyMob(element) {
				document.getElementById("propertyImg").setAttribute('src','/content/dam/demo-site/ML_property_b_22x22.png');
			}
			
			function hoverOnSearchMob(element) {
				document.getElementById("searchImg").setAttribute('src','/content/dam/demo-site/MP_icon_search_ro_22x22.png');
			}
			function unhoverOnSearchMob(element) {
				document.getElementById("searchImg").setAttribute('src','/content/dam/demo-site/ML_search_b_22x22.png');
			}
			
			function hoverOnTemperatureMob(element) {
				document.getElementById("tempratureImg").setAttribute('src','/content/dam/demo-site/MP_icon_temp_ro_22x22.png');
			}
			function unhoverOnTemperatureMob(element) {
				document.getElementById("tempratureImg").setAttribute('src','/content/dam/demo-site/ML_temp_b_22x22.png');
			}
			
			function hoverOnDropDownMob(element) {
				document.getElementById("dropdownImg").setAttribute('src','/content/dam/demo-site/mp_arrow_ro_11x7.png');
			}
			function unhoverOnDropDownMob(element) {
				document.getElementById("dropdownImg").setAttribute('src','/content/dam/demo-site/arrow_11x7.png');
			}
			
			function hoverOnMenuMob(element) {
				element.setAttribute('src', '/content/dam/demo-site/menu_ro_32x32.png'); 
			}
			function unhoverOnMenuMob(element) {
				element.setAttribute('src', '/content/dam/demo-site/menu_32x32.png');
			}
			
			function hoverOnProperty(element) {
				document.getElementById("propertyImg2").setAttribute('src','/content/dam/demo-site/property_ro_32x32.png');
			}
			function unhoverOnProperty(element) {
				document.getElementById("propertyImg2").setAttribute('src','/content/dam/demo-site/property_32x32.png');
			}
			
			function hoverOnSearch(element) {
				document.getElementById("searchImg2").setAttribute('src','/content/dam/demo-site/search_ro_32x32.png');
			}
			function unhoverOnSearch(element) {
				document.getElementById("searchImg2").setAttribute('src','/content/dam/demo-site/search_32x32.png');
			}
			
			function hoverOnTemp(element) {
				document.getElementById("tempImg2").setAttribute('src','/content/dam/demo-site/temp_ro_32x32.png');
			}
			function unhoverOnTemp(element) {
				document.getElementById("tempImg2").setAttribute('src','/content/dam/demo-site/temp_32x32.jpg');
			}
		</script>
		<script>
        	$(function() {
				$("#addToFavModal").dialog({
					autoOpen: false,
					draggable: false,
					resizable : false,
					modal: true,
					width: 410,
					height: 174,
					dialogClass: "dlg-no-title"
				});
				$("#add-to-fav-modal").on("click", function() {
					//$("#addToFavModal").load('addToFav2.html',function() { 
						$("#addToFavModal").dialog("open");
						$('.ui-widget-overlay').css('background', '#000e30');
						$('.ui-widget-overlay').css('opacity', '0.5');
					//});
				});
				
				$("#add-to-fav-modal-1").on("click", function() {
					//$("#addToFavModal").load('addToFav2.html',function() { 
						$("#addToFavModal").dialog("open");
						$('.ui-widget-overlay').css('background', '#000e30');
					//});
				});
				
				$("#add-to-fav-modal-2").on("click", function() {
					//$("#addToFavModal").load('addToFav2.html',function() { 
						$("#addToFavModal").dialog("open");
						$('.ui-widget-overlay').css('background', '#000e30');
					//});
				});
			});
			
			function closeMe() {
				$("#addToFavModal").dialog("close");
			}

			function pinUnpinMe(element) {
				var className = element.getAttribute('class');
				if(className == 'pin_me') {
					element.setAttribute('src', '/content/dam/demo-site/Unpin_icon.png');
					$('.contentcontainer').css('margin-left','275px');
					// Logic for pinning goes here...
					
					element.setAttribute('class', 'unpin_me');
				} else {
					element.setAttribute('src', '/content/dam/demo-site/pin_icon.png');
					
					// Logic for un-pinning goes here...
					$('.contentcontainer').css('margin-left','auto');
					element.setAttribute('class', 'pin_me');
				}
			}
        </script>
		
		
		<script id="secMenu-dt-template" type="text/x-handlebars-template">
			{{#each objects}}
				<div class="secNavigationitem-dt"><a href="{{#chgBlankURL}}{{url}}{{/chgBlankURL}}" class="secNavLink-dt"><img src="/content/dam/demo-site/Bullet_For_Title.png">{{#toUpper}}{{name}}{{/toUpper}}</a>
				{{#each pages}}
					<div class="pgNavigationitem-dt"><a href="{{#chgBlankURL}}{{url}}{{/chgBlankURL}}" class="pgNavLink-dt"><img src="/content/dam/demo-site/Bullet_for_content.png">{{name}}</a></div>
				{{/each}}
				</div>
			{{/each}}
		</script>
		
		


<!-- Header Start-->
			<div class="header">
				<div class="upperheader">
					<div id="btnMenu" class="upperleft_tab">
						<img src="/content/dam/demo-site/menu_32x32.png" alt="menu" onmouseover="hoverOnMenuMob(this);" onmouseout="unhoverOnMenuMob(this);" />
					</div>
					
                    <cq:include path="logocomp" resourceType="/apps/solution-accelerators/components/demo-site/content/logocomp" />


							<div class="upperrightupper6-mob">
								<div class="topnav2_tab_profile"><img src="/content/dam/demo-site/pro_pic_42x66.png" alt="Profile Picture" /></div>
							</div>
							<div class="upperrightupper7-tab" onmouseover="hoverOnDropDownMob(this);" onmouseout="unhoverOnDropDownMob(this);">
								<div class="topnav2_tab_drop"><a href="#"><img src="/content/dam/demo-site/arrow_11x7.png" alt="Profile Picture" id="dropdownImg" /></a></div>
							</div>
							<div class="upperrightupper6-tab">
								<div class="topnav2_tab_profile"><img src="/content/dam/demo-site/profile_pic_tb.png" alt="Profile Picture" /></div>
							</div>
							<div class="upperrightupper5-tab">
								<div class="topnav2_tab_welcome1">Welcome </div>
								<div class="topnav2_tab_welcome2">Wendy</div>
							</div>
							<div class="upperrightupper4-mob" onmouseover="hoverOnTemperatureMob(this);" onmouseout="unhoverOnTemperatureMob(this);">
								<div class="topnav2_tab"><a href="#"><img src="/content/dam/demo-site/ML_temp_b_22x22.png" alt="Navigation" id="tempratureImg"/></a></div>
							</div>
							<div class="upperrightupper3-mob" onmouseover="hoverOnSearchMob(this);" onmouseout="unhoverOnSearchMob(this);">
								<div class="topnav2_tab"><a href="#"><img src="/content/dam/demo-site/ML_search_b_22x22.png" alt="Search" id="searchImg" /></a></div>
							</div>
							<div class="upperrightupper2-mob" onmouseover="hoverOnPropertyMob(this);" onmouseout="unhoverOnPropertyMob(this);">
								<div class="topnav2_tab"><a href="#"><img src="/content/dam/demo-site/ML_property_b_22x22.png" alt="About Property" id="propertyImg" /></a></div>
							</div>
							<div class="upperrightupper4-tab" onmouseover="hoverOnTemp(this);" onmouseout="unhoverOnTemp(this);">
								<div class="topnav_tab"><a href="#">31&deg; F</a></div>
								<div class="topnav2_tab"><img src="/content/dam/demo-site/temp_32x32.jpg" alt="Navigation" id="tempImg2"/></div>
							</div>
							<div class="upperrightupper3-tab" onmouseover="hoverOnSearch(this);" onmouseout="unhoverOnSearch(this);">
								<div class="topnav_tab"><a href="#">Search</a></div>
								<div class="topnav2_tab"><img src="/content/dam/demo-site/search_32x32.png" alt="Search" id="searchImg2"/></div>
							</div>
							<div class="upperrightupper2-tab" onmouseover="hoverOnProperty(this);" onmouseout="unhoverOnProperty(this);">
								<div class="topnav_tab"><a href="#">Property</a></div>
								<div class="topnav2_tab"><img src="/content/dam/demo-site/property_32x32.png" alt="About property" id="propertyImg2" /></div>
							</div>


						<div class="upperrightupper">
							<div class="upperrightupper1">
								<div class="upperrightupper1container">
									<div class="date">01 - 08 - 2014</div>
									<div class="weather">
										<img src="/content/dam/demo-site/Sun_icon.png" alt="Weather" class="weatherlogo" />Sunny
									</div>
									<div class="temprature">31&deg; F, Madison, NJ 07940</div>
									<div class=""></div>
								</div>
							</div>
							<div class="upperrightnav">
								<div class="upperrightupper2">
									<div class="topnav topnav2"><a href="#">My Settings</a></div>
								</div>
								<div class="upperrightupper3">
									<div class="topnav topnav2"><a href="#">Profile</a></div>
								</div>
								<div class="upperrightupper4">
									<div class="topnav topnav2"><span>Welcome Wendy</span></div>
								</div>
								<div class="upperrightupper5">
									<img src="/content/dam/demo-site/User_Profile_Picture.jpg" alt="Profile Picture" class="profilepic" />
								</div>
								<div class="upperrightupper6">
									<div class="topnav topnav2 logout"><a href="#">Logout</a></div>
								</div>
							</div>
						</div>
						<div class="upperrightlower">
                            <cq:include path="mainnavigation" resourceType="/apps/solution-accelerators/components/demo-site/content/mainnavigation" />
						</div>
				</div>

			</div>
			<!-- Header End-->




<script>
var curTopLevelIndx = 0;
var app = app || {};
app.isDesktop = Response.band(1025);
app.menu = app.menu || {};
app.menu.menuData = [
		{name:"Dashboard", url:"index.html", sections:[]},
		{name:"Marketing", url:"", sections:	[
							{name:"Anthology", url:"", pages:[
										{name:"PDF version", url:""},
										{name:"Anthology Feedback", url:""}
									]},
							{name:"Highlights Newsletters", url:"", pages:[]},
							{name:"Reside Magazine", url:"", pages:[
										{name:"Current Issue", url:""},
										{name:"Past Issues", url:""}
									]},
							{name:"Public Relations", url:"", pages:[]},
							{name:"Social Media", url:"", pages:[
										{name:"Blog: Distinctive Real Estate", url:""}
									]},
							{name:"Corporate Marketing Kits", url:"", pages:[]},
							{name:"Print Advertising", url:"", pages:[
										{name:"Corporate Rates", url:""},
										{name:"Co-operative Advertising Program", url:""},
										{name:"Sotheby's AT AUCTION Magazine", url:""},
										{name:"Request Announcement Ad", url:""},
										{name:"Request Portrait Ad", url:""},
										{name:"Request Auction House Collaboration Ad", url:""},
										{name:"Request Fine Art Images", url:""}
									]},
							{name:"Online Advertising", url:"", pages:[]},
							{name:"Email Marketing", url:"", pages:[]},
							{name:"Request Marketing Assets", url:"", pages:[]}
						]},
		{name:"Brand", url:"brand.html", sections:	[
							{name:"About", url:"", pages:[
										{name:"Brand History", url:""},
										{name:"Awards", url:"content.html"}
									]},
							{name:"Strategy", url:"", pages:[
										{name:"Brand Vision", url:""},
										{name:"Marketing Strategy", url:""}
									]},
							/*Sotheby's international Realty */
							{name:"Brand Standards and Guidelines", url:"", pages:[
										{name:"Identity Standards", url:""},
										{name:"Policy and Procedures", url:""},
										{name:"Quality Control", url:""}
									]},
							{name:"[Affiliate Name] Brand Standards and Guidelines", url:"", pages:[
										{name:"Identity Standards", url:""},
										{name:"Policy and Procedures", url:""},
										{name:"Quality Control", url:""}
									]},
							{name:"Sotheby's Auction House", url:"", pages:[]},
							{name:"Directory", url:"directoryPage.html", pages:[]}
						]},
		{name:"Tools", url:"", sections:[


							{name:"Leads", url:"", pages:[
										{name:"LeadRouter", url:""}
									]},
							{name:"Listings", url:"", pages:[
										{name:"Listhub", url:""},
										{name:"CREST EDG", url:""},
										{name:"Listing Management Module", url:""},
										{name:"Atlas", url:""},
										{name:"Property Mapping Tool", url:""},
										{name:"Listing/Office/ Agent Language Translation Tool", url:""},
										{name:"Regional update tool", url:""}
									]},
							{name:"Marketing", url:"", pages:[
										{name:"Anthology Collection", url:""},
										{name:"Presentation Studio", url:""},
										{name:"Product Studio (xpressdocs)", url:""},
										{name:"Tool Kit CMA", url:""},
										{name:"Ad Studio", url:""},
										{name:"eGallery Admin Tool", url:""},
										{name:"Install eGallery", url:""},
										{name:"sothebysrealty.com Reports", url:""}
									]},
							{name:"CRM", url:"", pages:[
										{name:"Rezora", url:""},
										{name:"Presentation Studio", url:""}
									]},
							{name:"Office", url:"", pages:[
										{name:"Office/Company Management Module", url:""},
										{name:"Staff Management Module", url:""},
										{name:"Access Control Panel", url:""},
										{name:"Learning Institute", url:""}
									]}
						]},
		{name:"Office", url:"", sections:[
							{name:"Forms", url:"", pages:[
										{name:"Expected content here", url:""}
									]},
							{name:"Running Your Business", url:"", pages:[
										{name:"Getting Started", url:""},
										{name:"Improving Your Business Public Relations Exposure", url:""},
										{name:"Recruiting", url:""},
										{name:"SothebysRealty.com Email", url:""},
										{name:"Technology Conference Calls", url:""}
									]},
							{name:"Office Stationary and Signage", url:"", pages:[	
										{name:"Business Cards", url:""},
										{name:"Greeting Notes and Cards", url:""},
										{name:"Letterhead and Envelopes", url:""},
										{name:"Signage", url:""}
									]},
							{name:"Marquis Privileges", url:"", pages:[
										{name:"Expected content here", url:""}
									]}
						]},
		{name:"News and Events", url:"", sections:[
							{name:"Calendar", url:"", pages:[
										{name:"Calendar list view", url:""},
										{name:"Event details", url:""}
									]},
							{name:"Highlights Newsletter", url:"", pages:[]},
							{name:"Newsletter #2", url:"", pages:[]},
							{name:"Newsletter #3", url:"", pages:[]},
							{name:"Press Releases", url:"", pages:[
										{name:"Sotheby's International Realty PRs", url:""},
										{name:"Affiliates PRs", url:""},
										{name:"Sotheby's Auction House PRs", url:""}
									]}
						]},
		{name:"Help", url:"", sections:[
							{name:"Tutorials", url:"", pages:[
										{name:"Dashboard", url:""},
										{name:"Brand", url:""},
										{name:"Marketing", url:""},
										{name:"Tools", url:""},
										{name:"Office", url:""},
										{name:"News and Events", url:""}
									]},
							{name:"Training", url:"", pages:[
										{name:"Content expected", url:""}
									]},
							{name:"Sitemap", url:"", pages:[]}
						]}
		];

Handlebars.registerHelper('toUpper', function(options) {
  return options.fn(this).toUpperCase();
});
Handlebars.registerHelper('chgBlankURL', function(options) {
	if(options.fn(this)){
		return options.fn(this);
	}else{
		return "#";
	}
	
});
app.menu.topMenuDTTemplate = Handlebars.compile($("#topMenu-dt-template").html());
app.menu.secMenuDTTemplate = Handlebars.compile($("#secMenu-dt-template").html());
app.menu.topMenuTemplate = Handlebars.compile($("#topMenu-template").html());
app.menu.secMenuTemplate = Handlebars.compile($("#secMenu-template").html());
app.menu.curTopLvl = -1;

app.menu.createMenu = function (isFromResize){
	var isDesktop = Response.band(1025);
	if(isFromResize && app.isDesktop == isDesktop){
		return;
	}
	app.isDesktop = isDesktop;

	var sMenuHTML = "";

	if(app.isDesktop){
		app.menu.closeDTMenu();
		sMenuHTML = app.menu.topMenuDTTemplate({objects:app.menu.menuData});
		$(".mainnavigation").html(sMenuHTML);
		//$(".mainnavigation .mainnavigationitem-dt:eq("+curTopLevelIndx+") .mainNavLink-dt").addClass("selectedTopLevelLink");
		$("#top"+curTopLevelIndx).addClass("selectedTopLevelLink").append('<span style="visibility:hidden;width:1px;font-size:0px">&nbsp;selected</span>');
		$("#menuLeft").html("");
		$("#menuLeft").hide();
		app.menu.onLeftMenuShowHide();
		$("#menuDropDown").show();
	}else{
		sMenuHTML = app.menu.topMenuTemplate({objects:app.menu.menuData});
		$(".mainnavigation").html("");
		$("#menuLeft").html(sMenuHTML);
		var isMenuHidden = ($('#menuLeft').css('display') == 'none')
		if(isMenuHidden){
			$("#menuLeft").show();
		}
		$(".mainNavLink,.leftMenuArrow").each(function(item){
			$(this).css("top", (35-$(this).height()/2)+"px");
		});
		if(isMenuHidden){
			$("#menuLeft").hide();
		}
		$("#menuDropDown").hide();
	}

	$(".mainNavLink-dt").mouseover(app.menu.openDTMenu);
	$(".mainNavLink-dt").focus(app.menu.openDTMenu);
	$(".mainNavLink-dt").mouseout(app.menu.closeDTMenu);
}

app.menu.createSecMenu = function(indx){
	if(indx<0 || indx>=app.menu.menuData.length){
		console.log("Error in generating menu:Incorrect index");
	}
	app.menu.curTopLvl = indx;
	var sMenuHTML = '<div class="mainnavigationitem"><a href="#" class="leftMenuBackLink" onclick="app.menu.createMenu(false);return false;" class="btn"><img src="../images/arrow_back_6x11.png">'+app.menu.menuData[indx].name+'</a></div>';
	sMenuHTML += app.menu.secMenuTemplate({objects:app.menu.menuData[indx].sections});
	$(".mainnavigation").html("");
	$("#menuLeft").html(sMenuHTML);
	$(".secNavLink,.leftMenuArrow,.leftMenuBackLink").each(function(item){
		$(this).css("top", (35-$(this).height()/2)+"px");
	});
}

app.menu.createPageMenu = function(indx){
	var sMenuHTML = '<div class="mainnavigationitem"><a href="#" class="leftMenuBackLink" onclick="app.menu.createSecMenu(app.menu.curTopLvl);return false;" class="btn"><img src="../images/arrow_back_6x11.png">'+app.menu.menuData[app.menu.curTopLvl].sections[indx].name+'</a></div>';
	sMenuHTML += app.menu.secMenuTemplate({objects:app.menu.menuData[app.menu.curTopLvl].sections[indx].pages});
	$(".mainnavigation").html("");
	$("#menuLeft").html(sMenuHTML);
	$(".secNavLink,.leftMenuArrow,.leftMenuBackLink").each(function(item){
		$(this).css("top", (35-$(this).height()/2)+"px");
	});
}

app.menu.openDTMenu = function(e){
	var sMenuHTML = "";
	var indx = parseInt(e.currentTarget.id.substring(3));

	if(indx<0 || indx>=app.menu.menuData.length){
		console.log("Error in generating menu:Incorrect index");
	}

	sMenuHTML = app.menu.secMenuDTTemplate({objects:app.menu.menuData[indx].sections});
	$("#menuLeft").html("");

	//$("#menuDDLeft").height(360);
	$("#menuDDLeft").html("");
	$("#menuDDRight").html("");
	if(app.menu.menuData[indx].sections.length>0){
		var chgIndx = 0;
		$("#menuDDLeft").html(sMenuHTML);
		var maxH = $("#menuDDLeft").offset().top+$("#menuDDLeft").outerHeight()/2;
		//console.log($("#menuDDLeft").outerHeight()+"--"+maxH);
		$("#menuDDLeft").children(".secNavigationitem-dt").each(function( index ) {
			//console.log( index + ": " + $( this ).attr('class') +"--"+($( this ).offset().top+$(this).height())+"--"+maxH);
			if( ($(this).offset().top+$(this).height()) > maxH){
				chgIndx = index;
				return false;
			}
		});
		//var $chgElement = $("#menuDDLeft").children("div").eq(chgIndx);
		if(chgIndx>0){
			var $chgElements = $("#menuDDLeft").children("div:gt("+(chgIndx-1)+")");
			$chgElements.remove();
			$("#menuDDRight").append($chgElements);
		}
		//$("#menuDropDown").show();
		var $mainNav = $(e.currentTarget);//.parent().parent();
		var newL = $mainNav.offset().left-$("#container").offset().left;//$(".mainnavigation").offset().left;
		//if(newL+$("#menuDropDown").width() > $mainNav.parent().width()){
		if(newL+$("#menuDropDown").width() > $("#container").width()-10){
			newL = $("#container").width() - $("#menuDropDown").width() - 20;
		}
		$("#menuDDLeft").css("height", "auto");
		$("#menuDropDown").css({"visibility":"visible", "left":newL+"px"});
		$("#pointer").css("left", $(e.currentTarget).offset().left+$(e.currentTarget).width()/2-$("#menuDropDown").offset().left);
		$("#menuDropDown").mouseover(function() {$("#menuDropDown").css("visibility", "visible")});
		$("#menuDropDown").mouseout(function() {$("#menuDropDown").css("visibility", "hidden")});
	}
}

app.menu.closeDTMenu = function(e){
	//$("#menuDDLeft").html("");
	//$("#menuDDRight").html("");
	//$("#menuDropDown").hide();
	$("#menuDropDown").css("visibility", "hidden");
}
app.menu.onLeftMenuShowHide = function(){
	$("#container").css("margin-left", ($('#menuLeft').css('display') == 'none')?"auto":$("#menuLeft").outerWidth()+"px");
	$("#bg").css("margin-left", ($('#menuLeft').css('display') == 'none')?"auto":$("#menuLeft").outerWidth()+"px");
}

app.menu.createMenu(false);


$("#btnMenu").click(function(){
	$("#menuLeft").toggle();
	app.menu.onLeftMenuShowHide();
});

window.onresize = function(){ app.menu.createMenu(true); $("#help").css("right", "-"+$("#bgHelp").outerWidth()+"px");};


$("#help").css("right", "-"+$("#bgHelp").outerWidth()+"px");
$("a[href='#']").not("#helpHandle a,#btnHelpClose a, #localNavHandle a").click(function(event) {
    alert("2");
	// This will prevent the default action of the anchor
	event.preventDefault();
	// Failing the above, you could use this, however the above is recommended
	return false;
});
$("#helpHandle a,#btnHelpClose a").click(function(event) {
    alert("1");
	if($('.help-container').css('display') == 'none'){
		$(".help-container").show();
		$("#btnHelpClose").show();
		$("#help").css("right", "-"+$("#bgHelp").outerWidth()+"px");
	}
	$("#help").animate({
			right: ($( "#help" ).css("right")=="0px")?"-"+$("#bgHelp").outerWidth()+"px":"0px",
		}, 400, function() {
			if($( "#help" ).css("right")!="0px"){
				$(".help-container").hide();
				$("#btnHelpClose").hide();
				$("#help").css("right", "-"+$("#bgHelp").outerWidth()+"px");
			}
	});
	//event.stopImmediatePropagation();
	// This will prevent the default action of the anchor
	event.preventDefault();
	// Failing the above, you could use this, however the above is recommended
	return false;
});
//window.onresize = function(){ $("#help").css("right", "-"+$("#bgHelp").outerWidth()+"px");};


$("#localNavHandle a").click(function(event) {
	if($('#imgLocalNavPanel').css('display') == 'none'){
		$("#imgLocalNavPanel").show();
		$("#localNav").css("left", "-"+$("#bglocalNav").outerWidth()+"px");
	}
	$("#localNav").animate({
		left: ($( "#localNav" ).css("left")=="0px")?"-"+$("#bglocalNav").outerWidth()+"px":"0px",
		}, 400, function() {
		if($( "#localNav" ).css("left")!="0px"){
			$("#imgLocalNavPanel").hide();
			$("#localNav").css("left", "-"+$("#bglocalNav").outerWidth()+"px");
			$(".clsLocalNavHandle").removeClass("localNavClose");
		}
		else{
			$(".clsLocalNavHandle").addClass("localNavClose");
		}
	});
	// This will prevent the default action of the anchor
	event.preventDefault();
	// Failing the above, you could use this, however the above is recommended
	return false;
});

if (Object.hasOwnProperty.call(window, "ActiveXObject") && !window.ActiveXObject) {
		$("#help").css("margin-right", "20px");
		$(".searchportion").css("margin-right", "25px");
		$(".upperrightnav").css("margin-right", "20px");
		$(".upperrightlower").css("right", "10px");
}
$("#localNav").height($(".footer").offset().top);
/*
// Cecking if browser is safari then dashboard's banner left and right arrows are placed perfectly.
$.browser.safari = ($.browser.webkit && !(/chrome/.test(navigator.userAgent.toLowerCase())));
if ($.browser.safari) {
    $(".left, .right").css("margin-bottom", "6px");
}*/
</script>
