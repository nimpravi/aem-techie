// JavaScript Document
var CurrentMenu = 9999;
function fixWidth() {
	var site = $(window).width();
	var main = $(".mainContent").width();
	if ($(".sidebar").length) { var sidebar = $(".sidebar").width(); }
    if ($(".subBanner .banner").length) {
        var bannerHeight = $(".bannerTxt div").height();
		bannerHeight = (108 - bannerHeight) / 2;
        $(".bannerTxt div").css("padding-top", bannerHeight + "px");
	}
	if ($(".subBanner2 .banner").length) {
        var bannerHeight = $(".bannerTxt div").height();
		bannerHeight = (300 - bannerHeight) / 2;
        $(".bannerTxt div:first").css("padding-top", bannerHeight + "px");
	}		
	if (site >= 1285) {
		if(!$("body").hasClass('homePage')){
			$(".header").css("min-width", "1280px");
		}
		if ($(".subBanner .banner").length) { $(".banner").css("min-width", "1280px"); }
		if ($(".sidebar").length) { $(".sidebar").css("width", "500px"); }
		if ($(".sideNav").length) { $(".sideNav").css("padding-left", "40px"); }
		$(".footerTopRight").css("width", "500px");
		$(".footerBottomRight").css("width", "500px");
        $(".footerTop .footerTopRight .footerCol3").css("width", "88%");
        $(".mainContent img").css("max-width", "700px");
	}
	else {
        $(".mainContent .fastfacts li").css('padding','0 15px');
		$(".header").css("min-width", "960px");
		if ($(".subBanner .banner").length) { $(".banner").css("min-width", "960px"); }
		if ($(".sidebar").length) { $(".sidebar").css("width", "240px"); }
		if ($(".sideNav").length) { $(".sideNav").css("padding-left", "20px"); }
		$(".footerTopRight").css("width", "300px");
		$(".footerBottomRight").css("width", "300px");
        $(".footerTop .footerTopRight .footerCol3").css("width", "100%");
        $(".mainContent img").css("max-width", $(".mainContent").width() + "px");
	}
}
$(document).ready(function () {
	$(".textimage").addClass("clearfix"); // Text Image Component Clearfix
    if($("span.leadership").length){ // Leadership Page RTE Fix
        $("span.leadership").each(function(){
            $(this).parent().html($(this).text()).addClass("leadership");
        });
    }
    $("img").removeAttr("title"); // Text Image Component Remove title fix

    var currentLocation = "https://svcs.abbvie.com/contact-webapp/contact.html?CUID=&BUID=&redirected_referer=" + window.location;
    $('.specialLink').attr("href", currentLocation);
    $('.specialLink').attr("target", '_blank');

   	if($("body").hasClass("homePage")){
		if($(".bigBanner").length){
			$(".homeBlackBar").height(285);
			$(".homeBlackCover").height(285);
			$(".contentArea").height(431);
			$(".bannerTxt").addClass("banner_big");
			if($("body").hasClass("rtl"))
				$(".bannerTxt").addClass("banner_left");
		}		
		var caFlag = $(".contentArea").height();
		var infoBox = $(".infoBox").height();
		var infoFlag = $(".info").height();
		var openArr = new Array();
		var closeArr = new Array();	
		var H = infoBox - infoFlag;
		$(".contentArea").find(".infoInside").each(function() {
			var II = $(this).outerHeight();
			var IIb = $(this).height();
			var IItotal = II - IIb;				
			if (II < H) {
				$(this).css("height", (H - IItotal) + "px");
			}
		});
		$(".contentArea").find(".infoButton span").each(function() {
			var count = $(this).text().length;
			if (count > 42) {
				$(this).html($(this).html().substr(0, 42) + "&hellip;");
			}
		});		
		$("body").on("click", ".info", function () {
			if ($(this).css("top") == "0px") {			
				$(this).animate({ height: infoFlag }, 500, function () {
					$(this).animate({ top: (infoBox - infoFlag) }, 500);				
					$(this).parents(".contentArea").find(".info").each(function(i) {
						closeArr[i] = $(this).outerHeight();
					});				
					if (Math.max.apply(Math, closeArr) == infoFlag) {
						$(this).parents(".contentArea").animate({ height: caFlag + 'px' }, 500, function () {
						});
					}	
					$(this).find('span').removeClass('active')					
				});
			}
			else {
				$(this).animate({ top: 0 }, 500, function () {
					var h = infoFlag + ($(this).find('.infoInside').innerHeight());				
					if (h > infoBox) {			
						$(this).parents(".contentArea").find(".infoInside").each(function(i) {
							openArr[i] = $(this).outerHeight();
						});					
						var content = caFlag;
						var hPush = Math.max.apply(Math, openArr);
						hPush = content + (hPush - (infoBox - 40));
						if (caFlag == $(".contentArea").height()) {
							$(this).parents(".contentArea").animate({ height: hPush + 'px' }, 500);
						}
					}					
					$(this).animate({ height: h + 'px' }, 500, function () {
						$(this).find('span').addClass('active');
					});
				});
			}
		});
	}
	if($(".homeContentContainer").length){
		/* home.js */
		var homeW = 850;
		function fixhomeWidth() {
			var site = $(window).width();
			if (site > 994) {
				site = ((site - 994) / 2) + 644;
				$(".homeBlackBar").css("width", site + "px");
			}
		}
		$(".slide div img").each(function(i) {
			$(this).css("margin-left", "-73px");
		});	
		$(".slide div:first").css("width", "252px");
		$(".slide div:first img").css("margin-left", "0px");
		$(".carouselTxt div").eq(0).css("display", "block");
		var sliderFlag = false;	
		$(".sliders .last").click(function () { 
			if (sliderFlag == false) {
				sliderFlag = true;
				var F = $(".slide div").eq(0).html();
				var M = $(".slide div").eq(1).html();
				var L = $(".slide div").eq(2).html();			
				var txtF = $(".carouselTxt div").eq(0).html();
				var txtM = $(".carouselTxt div").eq(1).html();
				var txtL = $(".carouselTxt div").eq(2).html();			
				$(".carouselTxt div").eq(0).fadeToggle(200, function() {
					$(".carouselTxt div").eq(0).html(txtM);
					$(".carouselTxt div").eq(0).fadeToggle(800, function() {		
						sliderFlag = false;
					});		
					$(".carouselTxt div").eq(1).html(txtL);
					$(".carouselTxt div").eq(2).html(txtF);
				});			
				$(".slide div").eq(0).css("background-image", "url(" + $(".slide div img").eq(0).attr("src") + ")");
				$(".slide div").eq(0).html(M);
				$(".slide div img").eq(0).css("margin-left", "252px");
				$(".slide div img").eq(0).animate({marginLeft: '0px'} , 700);			
				$(".slide div").eq(1).html(L);
				$(".slide div img").eq(1).css("margin-left", "0px");
				$(".slide div img").eq(1).animate({marginLeft: '-73px'} , 700);			
				$(".slide div").eq(2).html(F);
				$(".slide div img").eq(2).animate({marginLeft: '-73px'} , 700);
			}
		});
		$(window).trigger('resize');	
	}
	/* End of home.js */
	    
    WireUpMainNav();
    WireUpTopNav();
	warnOnLeaveInit();
	WireUpForm();
    WireUpFirstAndLastChild();
	
	/* subnav.js */
	if ($(".sideNav").length) {
        // Accordion
        function initAccord() {
            var flag = CurrentMenu;			
            //$('#Accord > li > a').eq(flag).addClass('ATopON');
            SubMenuCheck(flag);
            $('#Accord ul').eq(flag).show();			
			/* Not hiding the elements which are having 'show' class */
            $('#Accord ul').not('.show').hide();			
            $('#Accord li a').click(function () {
				if($(this).attr("href") == "#"){var checkElement = $(this).next();}
			    if ((checkElement.is('ul')) && (checkElement.is(':visible'))) {
			        $('#Accord ul:visible').slideUp('normal');
			        $(this).removeClass('ATopON')
			        return false;
			    }
			    if ((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
			        if (flag != null) {
			            $('#Accord a').attr('selectedIndex', flag).removeClass('ATopON');
			        }
			        else {
			            $('#Accord a:first').removeClass('ATopON');
			        }
			        flag = $('#Accord > li > a').index(this);
			        $(this).addClass("ATopON");
			        SubMenuCheck(flag);
			        $('#Accord ul:visible').slideUp('normal');
			        if (!checkElement.is('.none') && $('li', checkElement).length != 0) {
			            checkElement.slideDown('normal')
			        }
			        else {
			            checkElement.toggleClass('none', true);
			        }
			        return false;
			    }
			});
        }
        function SubMenuCheck(flag) {
            if ($.trim($('#Accord > li > ul > li').eq(flag).html()) == "") {
                $('#Accord > li > ul > li').eq(flag).css("padding-bottom", "0px")
                $('#Accord > li > a').eq(flag).css("background-image", "url(images/Plus.png)")
            }
        }
        if ($('.subMenu').length) { initAccord(); }
        // Page Accordion content
        function initAccordContent() {
            $('#AccordContent ul').hide();
            $('#AccordContent li a').click(
			function (e) {
			    e.preventDefault();
			    $(this).next().slideToggle('normal');
			    if ($(this).hasClass("AContentON")) {
			        $(this).removeClass("AContentON")
			    }
			    else {
			        $(this).addClass("AContentON");
			    }
			});
        }
        if ($('#AccordContent').length) { initAccordContent(); }
		if($(".pipeline_table").length){ $(".pipeline_table").find("tr").find("td:even").css("background-color", "#f5f5f5"); }
    }
    fixWidth();
    $(".footerTopRight .footerRCol1").height($(".footerTopContainer .footerTopRight .footerRCol2").height()); // Footer Fix

	/* End of subnav.js */	
	
    /* Plugin Tooltip
     */
	(function(a){a.tools=a.tools||{version:"v1.2.7"},a.tools.tooltip={conf:{effect:"toggle",fadeOutSpeed:"fast",predelay:0,delay:30,opacity:1,tip:0,fadeIE:!1,position:["top","center"],offset:[0,0],relative:!1,cancelDefault:!0,events:{def:"mouseenter,mouseleave",input:"focus,blur",widget:"focus mouseenter,blur mouseleave",tooltip:"mouseenter,mouseleave"},layout:"<div/>",tipClass:"tooltip"},addEffect:function(a,c,d){b[a]=[c,d]}};var b={toggle:[function(a){var b=this.getConf(),c=this.getTip(),d=b.opacity;d<1&&c.css({opacity:d}),c.show(),a.call()},function(a){this.getTip().hide(),a.call()}],fade:[function(b){var c=this.getConf();!a.browser.msie||c.fadeIE?this.getTip().fadeTo(c.fadeInSpeed,c.opacity,b):(this.getTip().show(),b())},function(b){var c=this.getConf();!a.browser.msie||c.fadeIE?this.getTip().fadeOut(c.fadeOutSpeed,b):(this.getTip().hide(),b())}]};function c(b,c,d){var e=d.relative?b.position().top:b.offset().top,f=d.relative?b.position().left:b.offset().left,g=d.position[0];e-=c.outerHeight()-d.offset[0],f+=b.outerWidth()+d.offset[1],/iPad/i.test(navigator.userAgent)&&(e-=a(window).scrollTop());var h=c.outerHeight()+b.outerHeight();g=="center"&&(e+=h/2),g=="bottom"&&(e+=h),g=d.position[1];var i=c.outerWidth()+b.outerWidth();g=="center"&&(f-=i/2),g=="left"&&(f-=i);return{top:e,left:f}}function d(d,e){var f=this,g=d.add(f),h,i=0,j=0,k=d.attr("title"),l=d.attr("data-tooltip"),m=b[e.effect],n,o=d.is(":input"),p=o&&d.is(":checkbox, :radio, select, :button, :submit"),q=d.attr("type"),r=e.events[q]||e.events[o?p?"widget":"input":"def"];if(!m)throw"Nonexistent effect \""+e.effect+"\"";r=r.split(/,\s*/);if(r.length!=2)throw"Tooltip: bad events configuration for "+q;d.on(r[0],function(a){clearTimeout(i),e.predelay?j=setTimeout(function(){f.show(a)},e.predelay):f.show(a)}).on(r[1],function(a){clearTimeout(j),e.delay?i=setTimeout(function(){f.hide(a)},e.delay):f.hide(a)}),k&&e.cancelDefault&&(d.removeAttr("title"),d.data("title",k)),a.extend(f,{show:function(b){if(!h){l?h=a(l):e.tip?h=a(e.tip).eq(0):k?h=a(e.layout).addClass(e.tipClass).appendTo(document.body).hide().append(k):(h=d.next(),h.length||(h=d.parent().next()));if(!h.length)throw"Cannot find tooltip for "+d}if(f.isShown())return f;h.stop(!0,!0);var o=c(d,h,e);e.tip&&h.html(d.data("title")),b=a.Event(),b.type="onBeforeShow",g.trigger(b,[o]);if(b.isDefaultPrevented())return f;o=c(d,h,e),h.css({position:"absolute",top:o.top,left:o.left}),n=!0,m[0].call(f,function(){b.type="onShow",n="full",g.trigger(b)});var p=e.events.tooltip.split(/,\s*/);h.data("__set")||(h.off(p[0]).on(p[0],function(){clearTimeout(i),clearTimeout(j)}),p[1]&&!d.is("input:not(:checkbox, :radio), textarea")&&h.off(p[1]).on(p[1],function(a){a.relatedTarget!=d[0]&&d.trigger(r[1].split(" ")[0])}),e.tip||h.data("__set",!0));return f},hide:function(c){if(!h||!f.isShown())return f;c=a.Event(),c.type="onBeforeHide",g.trigger(c);if(!c.isDefaultPrevented()){n=!1,b[e.effect][1].call(f,function(){c.type="onHide",g.trigger(c)});return f}},isShown:function(a){return a?n=="full":n},getConf:function(){return e},getTip:function(){return h},getTrigger:function(){return d}}),a.each("onHide,onBeforeShow,onShow,onBeforeHide".split(","),function(b,c){a.isFunction(e[c])&&a(f).on(c,e[c]),f[c]=function(b){b&&a(f).on(c,b);return f}})}a.fn.tooltip=function(b){var c=this.data("tooltip");if(c)return c;b=a.extend(!0,{},a.tools.tooltip.conf,b),typeof b.position=="string"&&(b.position=b.position.split(/,?\s/)),this.each(function(){c=new d(a(this),b),a(this).data("tooltip",c)});return b.api?c:this}})(jQuery);

    /*
     ADOBE CONFIDENTIAL
     __________________
    
      Copyright 2012 Adobe Systems Incorporated
      All Rights Reserved.
    
     NOTICE:  All information contained herein is, and remains
     the property of Adobe Systems Incorporated and its suppliers,
     if any.  The intellectual and technical concepts contained
     herein are proprietary to Adobe Systems Incorporated and its
     suppliers and are protected by trade secret or copyright law.
     Dissemination of this information or reproduction of this material
     is strictly forbidden unless prior written permission is obtained
     from Adobe Systems Incorporated.
     */
    // Tab Control
    var isWCMEdit = false,
    	isAuthor  = ("CQ" in window && "WCM" in CQ),
   	 	CQTop     = isAuthor && CQ.WCM.getTopWindow().CQ;
	$(".tabctrl").each(function () {
    	var tabctrl   = $(this);
    	// Called when clicking on a tab link
   		function switchTab() {
			var link   = $(this),
            newItem    = link.closest("li"),
            oldItem    = tabctrl.find(".tabctrl-header li.active"),
            newContent = tabctrl.find(link.attr("href")),
            oldContent = tabctrl.find(".tabctrl-content:visible");
        
        	if (!newItem.is(oldItem)) {
            	oldItem.removeClass("active");
            	newItem.addClass("active");
            
            	if (isWCMEdit) {
                    oldContent.hide();
                    newContent.show();
                    toggleEditables(false, oldContent.attr("data-path"));
                    toggleEditables(true, newContent.attr("data-path"));
                } else {
                    oldContent.hide();
                    newContent.show();
                    tabctrl.find(".tabctrl-container").height(newContent.height());
                }
        	}
        	return false;
    	}
    
        function setTabHeight() {
            if (isWCMEdit) {
                tabctrl.find(".tabctrl-container").css("height", "auto");
                tabctrl.find(".tabctrl-content:hidden").each(function () {
                    toggleEditables(false, $(this).attr("data-path"));
                });
            } else {
                tabctrl.find(".tabctrl-container").height(tabctrl.find(".tabctrl-content:visible").height());
            }
    	}
    
        function updateWCM() {
            setTimeout(function () {
                isWCMEdit = CQTop.WCM.getMode() == CQTop.WCM.MODE_EDIT;
                setTabHeight();
            }, 1);
        }
    
        // Initialization
        tabctrl.find(".tabctrl-header a").bind("click.tabctrl", switchTab);
        tabctrl.find(".tabctrl-header li:first").addClass("active");
        tabctrl.find(".tabctrl-content:gt(0)").hide();
        
        if (isAuthor) {
            CQTop.WCM.on("wcmmodechange", updateWCM);
            CQTop.WCM.on("sidekickready", updateWCM);
            CQ.WCM.on("editablesready", updateWCM);
        } else {
            setTabHeight();
        }

	});

    // Shows/Hides the component widgets
    // The optional filter argument offers the possibility to limit the effect
    // only to the components below the provided content path 
    function toggleEditables(show, filter) {
        filter = filter && filter.replace("/_jcr_content/", "/jcr:content/");
        if (isAuthor) {
            var editables = CQ.WCM.getEditables();
            for (var path in editables) {
                if (!filter || path.indexOf(filter) == 0) {
                    editables[path][show ? "show" : "hide"]();
                }
            }
        }
    };
    $('.tooltip').wrapInner('<div class="tooltipMiddle"></div>').prepend('<div class="tooltipTop">&nbsp;</div>').append('<div class="tooltipBottom">&nbsp</div>');
    $(".additional_info").tooltip({
        position: "top center",
        relative: true,
        events: {
            def: "click,mouseout"     // the tooltip element
        }
    });
});
$(window).resize(function () {
	fixWidth();
	if($(".homeContentContainer").length){
		var squeeze = $(window).width() < 995;
		$('.headerContainer').toggleClass('Squeeze', squeeze);
		$('.homeContentContainer').toggleClass('Squeeze', squeeze);
	}
});


function WireUpMainNav(){
    var mainNavMenus = $('.mainNavMenus');
    var menus = $('.mainNavMenus .mainNavMenu');
    menus.show().each(function (i, e) {
        $('> span', e).css('height', $(e).height() + 'px')
        $('> span:first', e).toggleClass('first', true);
        $('> span:last', e).toggleClass('last', true);
    })
    menus.hide();
    $('.mainNav a').each(function (i, e) {
        var span = $('<span/>');
        var $aCur = $(this);
        span.css('width', $aCur.width() + 34 + 'px').css('left', '-17px')
        $(this).append(span);
        var shadowLeftWidth = 11 * $aCur.width() / 151;
        var shadowRightWidth = 17 * $aCur.width() / 151;
        var img = $aCur.find('img.menuShadow');
        img.css({
			'border':'none',
			'width'	:$aCur.width() + 34 + shadowLeftWidth + shadowRightWidth + 'px',
			'left'	:-1 * (shadowLeftWidth + 17) + 'px'
		});
        $aCur.attr('hideFocus', 'true')
    });
    $('.mainNavMenus .mainNavMenu').each(function (i, e){
        var img = $(this).find('img.ddShadow');
        img.css({
			'border'	:'none',
			'position'	:'absolute',
			'width'		:'102.8169014084507%',
			'top'		:'0px',
			'left'		:'-1.14084507042253502%',
			'height'	:'109%',
			'z-index'	:'-1',
			'display'	:'block'
		});
    });
    $(document).on('click', '.mainNav a', function (e) {
        var a = $(this);
        var toggle = !a.is('.active');
        $('.mainNav a').toggleClass('active', false);
        var as = $('.mainNav a');
        var index = as.index(this);
        var menus = $('.mainNavMenus > div');
        var menu = $(menus[index])
        var counter = 0;
        var length = menus.length;
        if (a.attr('href').indexOf('#') == 0 && $.trim(menu.text()) != '') {
            e.preventDefault();
            a.toggleClass('active', toggle);
        }
        var visibleMenu = $('.mainNavMenus .mainNavMenu:visible');
        if (visibleMenu.length == 0) {
            menus.stop().slideUp('fast', function () {
                $(this).hide();
                if (length == (++counter)) {
                    if ($.trim($(menus[index]).text()) != '')
                        $(menus[index]).fadeTo(0.0, 1.0).slideUp(0.0).slideDown('normal');
                }
            });
        }
        else if (menus[index] !== visibleMenu[0]) {
            if ($.trim($(menus[index]).text()) != '')
                $(menus[index]).fadeTo(0, 0).slideDown(0).fadeTo('fast', 1.0);

            visibleMenu.delay(100).fadeTo('fast', 0.0, function () {
                $(this).hide().slideUp(0.0);
            });
        }
        else {
            visibleMenu.slideUp('fast', function () {
                $(this).hide();
            });
        }
        TopNavMouseMove();
    });
    $(document).on('mousemove', 'body', MainNavMouseMove);
    $(document).on('touchmove', 'body', MainNavMouseMove);
    $(document).on('touchstart', 'body', MainNavMouseMove);
}
function MainNavMouseMove(e, flag){
    var element = $('body');
    if (!!e){
        element = $(e.srcElement || e.target);   
    }    
    if (!element.is('.mainNav a') && element.parents('.mainNav').length == 0 &&
        !element.is('.mainNavMenus') && element.parents('.mainNavMenus').length == 0 &&
            element.parents('.header').length == 0) {
        $('.mainNavMenus .mainNavMenu:visible').slideUp('fast', function () {
            $(this).hide();
        });
        $('.mainNav a.active').toggleClass('active', false);
    }
}
function WireUpTopNav(){   
    $('.topNavMenus > div').each(function (i, e) { $('a', e).wrapAll('<div class="topNavMenuBody"/>') });
    $('.topNavMenus > div').prepend('<div class="navArrowTop" />');
    $('.topNav a').attr('hideFocus', 'true');
    $(document).on('click', '.topNav a', function (e) {
        var a = $(this);
        var aid = a.attr("id");
        var as = $('.topNav a');
        var index = as.index(a);
        var menus = $('.topNavMenus > div');
        var menu;
        $(menus).each(function(){
            if($(this).hasClass(aid))
                menu = $(this);
        });
        var toggle = !a.is('.active');
        var counter = 0;
        var length = menus.length;
        as.toggleClass('active', false);
        if (a.attr('href').indexOf('#') == 0 && $.trim(menu.text()) != '') {
            e.preventDefault();
            a.toggleClass('active', toggle);
        }
        menu.css('left', a.position().left + (a.width() - 192) / 2 + 'px').css('top', a.offset().top + 16 + 'px');
        var visibleMenu = $('.topNavMenus > div:visible');
        if (visibleMenu.length == 0) {
            menus.stop().slideUp('fast', function () {
                $(this).hide();
                if (length == (++counter)) {
                    if ($.trim($(menu).text()) != '')
                        $(menu).fadeTo(0.0, 1.0).slideUp(0.0).slideDown('normal');
                }
            })
        }
        else if (menu !== visibleMenu[0]) {
            if ($.trim($(menu).text()) != ''){
                $(menu).show().fadeTo(0, 0).slideDown(0).fadeTo('fast', 1.0);
			}
            visibleMenu.delay(100).fadeTo('fast', 0.0, function () {
                $(this).hide().slideUp(0.0);
            });
        }
        else {
            visibleMenu.slideUp('fast', function () {
                $(this).hide();
            });
        }
        MainNavMouseMove();
    });
    $(document).on('mousemove', 'body', TopNavMouseMove);
    $(document).on('touchmove', 'body', TopNavMouseMove);
    $(document).on('touchstart', 'body', TopNavMouseMove);
}
function TopNavMouseMove(e, flag){
    var element = $('body');
    if (!!e){
        element = $(e.srcElement || e.target);   
    }
    if (!element.is('.topNav a') && element.parents('.topNav').length == 0 && !element.is('.topNavMenus') && element.parents('.topNavMenus').length == 0 &&
		element.parents('.header').length == 0) {
			$('.topNavMenus > div:visible').slideUp('fast', function () {
				$(this).hide();
			});
			$('.topNav a.active').toggleClass('active', false);
    }   
}
function WireUpForm(){
    $('form label > input').focus(function () {
        $(this).toggleClass('focus', true);
    }).blur(function () {
        $(this).toggleClass('focus', false);
    });
}
/*
 * Fix for IE7 and IE8 not supporting both :last-child and :first-child.
 * Please use separate rules instead of combining the two ie:
 * :first-child { ... }
 * .first-child { ... }
 *
 * not
 * 
 * :first-child, .last-child { ... }
 */
function WireUpFirstAndLastChild(){
    $('body :first-child').toggleClass('first-child', true);
    $('body :last-child').toggleClass('last-child', true);
}
function getFilePath(){			
	var location = window.location.href, splitter = 1;
	if(location.toLowerCase().lastIndexOf("abbvie.com")!=-1){
		location = location.substr(location.toLowerCase().lastIndexOf("abbvie.com"), location.length).split("/");
		splitter = location.length;
	}
	var path = "";
	for(var i=1; i<splitter - 1; i++){
		path += "../";
	}	
	path += 'assets/images/';
	return path;
}
function warnOnLeaveInit(){
	$('a.warnonleave, a.warnonexit').each(function(){
        var url = $(this).attr("href");
        $(this).attr("href", "#").attr("rel", url);		
	});
    $('span.warnonleave, span.warnonexit').each(function(){
		var url;
        if($(this).children('a').length > 0)
        	$t = $(this).children('a');
        else
            $t = $(this).parent('a');
		url = $t.attr("href");
		$t.attr("href", "#").attr("rel", url);		
	});
	var lnk;
    function showPopup($a){
        var a;
        if($a.hasClass("warnonleave")){
			a= "warnonleave";
		}
        else if($a.hasClass("warnonexit")){
			a= "warnonexit";
		}
        $("#"+a).show();
		$("#overlay").show();		
		var left = ($(window).width() - $("#warnonleave").width()) / 2;
		var top = ($(window).height() - $("#warnonleave").height()) / 2;		
		$("#"+a).css({"top":top+"px", "left":left+"px"});
    }
	$('a.warnonleave, a.warnonexit').click(function(e){
		lnk = $(this).attr("rel");
        showPopup($(this));
        e.preventDefault();
	});
    $('span.warnonleave, span.warnonexit').click(function(e){
        if($(this).children('a').length > 0)
        	$x = $(this).children('a');
        else
            $x = $(this).parent('a');
		lnk = $x.attr("rel");
        if($.trim(lnk).length!=0)
        	showPopup($(this));
        e.preventDefault();
	});
	$("#warnonleave input, #warnonleave a").click(function(){
		$("#warnonleave, #overlay").fadeOut("slow");
		if($(this).hasClass("no") || $(this).hasClass("close")){
			return false;
		}
		else{
			window.open(lnk, "_blank");
		}
	});
	$("#warnonexit input, #warnonexit a").click(function(){
		$("#warnonexit, #overlay").fadeOut("slow");
		if($(this).hasClass("no") || $(this).hasClass("close")){
			return false;
		}
		else{
			window.open(lnk, "_blank");
		}
	});
}
/*to set background color for pipeline table td elements in pipeline page*/

/* Video functionality */
var videoPlayer='';
function loadVideo(){	
	/* Opening video in a window */
	//window.open("video.html", "video","width=703, height=436, menubar=false, directories=0,titlebar=0,toolbar=0,location=no,status=0, menubar=0");
	$("#overlay").show();	
	$("#videoPopup").show();	 
	var left = ($(window).width() - $("#videoPopup").width()) / 2;
	var top = ($(window).height() - $("#videoPopup").height()) / 2;	
	if(videoPlayer != ""){
		DelvePlayer.doPlay();		
	}	
	$("#videoPopup").css({"top":top+"px", "left":left+"px"});	
	$("#videoPopup .close").click(function(e){
		$("#overlay").hide();	
	 	$("#videoPopup").hide();		
		DelvePlayer.doSeekToRatio(0);
		DelvePlayer.doPause();		
		e.preventDefault();
		videoPlayer = DelvePlayer;
	});
}
function delvePlayerCallback(playerId, eventName, data){
	var id = "limelight_player_208467";
	if (eventName == 'onPlayerLoad' && (DelvePlayer.getPlayers() == null || DelvePlayer.getPlayers().length == 0)){
	  DelvePlayer.registerPlayer(id);
	}
}

/* Validation.js */
function setState(){
	setCCC();
	var a=document.getElementById("statediv"),b=document.autoform.country.value;
	if(b=="US"||b==""){
		a.style.display="block";
		a=document.getElementById("provincediv");
		a.style.display="none";
		document.autoform.province.value="";
		document.autoform.prefix.focus();
		b!=""&&document.autoform.prefix.focus()
	}
	else{
		document.autoform.state.value="";document.autoform.zip.value="";document.autoform.zip.focus();
		a.style.display="none";
		a=document.getElementById("provincediv");
		a.style.display="block";
		document.autoform.province.value="";
		document.autoform.province.focus();
		document.autoform.prefix.focus()
	}
	return true
}
function setCountry(){
	if(document.autoform.state.value){
		if(!document.autoform.country.value){
			document.autoform.country.value="US";
			setCCC();
			document.autoform.country.focus()
		}
	}
	else {
		gvalidator.getGForm("autoform").getFieldByName("state").setState();
	}
	document.autoform.zip.value="";
	document.autoform.zip.focus();
	document.autoform.zip.click();
	chkReqs();
	return true
}
function setCCC(){	
	var a=document.autoform.phone.value,b=document.autoform.fax.value,d=document.autoform.country.value;
	if(!a&&!b||!d){
		document.autoform.ccc.value="";document.autoform.ccc.focus()
	}
	else {
		d=document.getElementById("country").selectedIndex*rLen-1;document.autoform.ccc.value=countries[d]>0?countries[d]:"";
		document.autoform.ccc.focus();
		if(a!==""){
			b!==""?document.autoform.custemail.focus():document.autoform.fax.focus();
		}
		else {
			document.autoform.phone.focus()
		}
	}
	return true
}
function chkReqs(){
	if(document.autoform.custemail.value){
		document.autoform.custemail.value=document.autoform.custemail.value.toLowerCase();
		setReqAddrFields(false);
		changeReqChar("addressdiv",false);
		changeReqChar("addressdiv2",false)
	}
	else {
		if(document.autoform.address1.value||document.autoform.city.value||document.autoform.state.value||document.autoform.province.value||document.autoform.zip.value){
			changeReqFieldState("custemail",false);
			changeReqChar("custemaildiv",false)
		}
		else {
			changeReqFieldState("custemail",true);
			changeReqChar("custemaildiv",true)
		}
		setReqAddrFields(true);changeReqChar("addressdiv",true);
		changeReqChar("addressdiv2",true)
	}
	return true;
}
function setReqAddrFields(a){
	for(var b=["address1","city","state","zip"],d=0;d<b.length;d++){
		var e=b[d];
		document.getElementById(e).value?changeReqFieldState("custemail",false):changeReqFieldState(e,a);
	}
	return true;
}
function changeReqFieldState(a,b){
	var d=gvalidator.getGForm("autoform").getFieldByName(a);	
	if(d.isRequired!==b){
		d.isRequired=b;
		d.setState(ONEGEEK.forms.FIELD_STATUS_INFO)
	}
	return true;
}
function changeReqChar(a,b){
	for(var d=document.getElementById(a).getElementsByTagName("span"),e=0;e<d.length;e++);
	if(d[e].className=="required")
		d[e].innerHTML=b?ONEGEEK.forms.GValidator.options.reqChar:"&nbsp;";
	return true;
}
/* End of Validation.js */