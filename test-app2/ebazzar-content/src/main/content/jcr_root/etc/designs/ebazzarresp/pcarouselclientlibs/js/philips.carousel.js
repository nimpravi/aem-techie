/**
 * jQuery plugin
 * Simple carousel component
 */
;(function ($) {

	$.fn.carousel = function (options) {

		return this.each(function () {
			
			/**
			 * Dependency checks:
			 * - Cycle
			 * - Timerdot.js
			 */

			var settings = $.extend(true, {
					// Default cycle settings
			     	fx					: 'scrollLeftFade',
				    timeout 			: 6000,
				    activePagerClass	: 'active',
				    sync 				: false,	// Do not fade in eachother
				    speed				: 1000,
				    // Default timerDot settings
				    pagerSettings		: {
				    	circleRadius	: 5,
				    	activeColor		: '#7cbd2a',
				    	inactiveColor	: '#e0e0e8'
				    }
			    }, options),
		    	$me = $(this),
		    	pagerSettings = settings.pagerSettings;

		    // Sync pager animation time with carousel + transition time
		    pagerSettings.animateTime = settings.timeout + settings.speed;
		    	
			// Add optional pager
			var $parent = $me.parent();
			if ($parent.hasClass('carousel')) {
				$pager = $parent.find('.carousel-nav');
				// Add pager to settings
				settings.pager = settings.pager || $pager;
			}

			// Optional animated dots navigation
			var useNavAnimatedDots = (typeof $pager !== 'undefined') && $pager.hasClass('carousel-nav-animated','carousel-nav-dotted');
			if (useNavAnimatedDots) {

	 			$pager.data('timerdots', {
	 				elems: [],
	 				firstAnimation: true
	 			});

	        	settings.updateActivePagerLink = function (pager, startingSlide, activePagerClass) {

	        		var pagerData = $(pager).data('timerdots');
	        		var dots = pagerData.elems;

	        		currentSlideIndex = startingSlide;
	        		$(pager).children().each(function (index) {
	        			// Setup circle instances
	        			if (!dots[index]) {
	        				dots[index] = $(this).timerDot(pagerSettings);
	        			}
	        			// Draw inactive circle
	        			if (startingSlide === 0 || index > startingSlide) {
	        				dots[index].setDot('drawInactiveCircle');
	        			}
	        			// Draw inactive circle	
	        			if ((index < startingSlide)) {
	        				dots[index].setDot('drawActiveCircle');
	        			}
	        		});
	            
	            	if (pagerData.firstAnimation) {
	            		pagerData.firstAnimation = false;
	            		dots[startingSlide].setDot('animateToActiveCircle', {animateTime: settings.timeout});
	            	} else {
	            		dots[startingSlide].setDot('animateToActiveCircle');
	            	}
	            }	
			}

			$me.cycle(settings);

		});

	}

})(jQuery);