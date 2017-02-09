/**
 * jQuery Cycle Plugin Transition Definitions
 * This script is a plugin for the jQuery Cycle Plugin
 * Examples and documentation at: http://malsup.com/jquery/cycle/
 */
(function ($) {
"use strict";

	$.fn.cycle.transitions.scrollLeftFade = function($cont, $slides, opts) {
	 	$slides.not(':eq('+opts.currSlide+')').css('opacity',0);
		opts.before.push(function(curr,next,opts) {
			$.fn.cycle.commonReset(curr,next,opts);
			opts.cssBefore.opacity = 0;
		});
		opts.animIn.opacity = 1;
		opts.animOut.opacity = 0;
		$cont.css('overflow','hidden');
		var w = $cont.width();
		w = (w < 25? w: 25);
		opts.cssFirst.left = 0;
		opts.cssBefore.left = w;
		opts.cssBefore.top = 0;
		opts.animIn.left = 0;
		opts.animOut.left = 0-w;
	};

})(jQuery);