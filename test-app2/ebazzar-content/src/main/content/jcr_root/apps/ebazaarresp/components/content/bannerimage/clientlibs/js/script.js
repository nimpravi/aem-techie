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