(function($){
/*******************************************************************************************/               
// philips.timerDot.js - version 1.0 - October-2011 - PV/PS
// A jQuery plugin for the Philips Carousel navigation (on the landingpage) (NENA).
/*******************************************************************************************/
	$.fn.timerDot = function(options) {
		var defaults = {
			circleRadius: 7,
			inactiveColor: "#CCC",
			activeColor: "#005AFF",
			nonAnimatingActiveColor: "#6fa9ff",
			animateTime: 10000,
			action: 'drawInactiveCircle'  // options: 'drawInactiveCircle', 'drawActiveCircle', 'animateToActiveCircle'
	  	}; 
	  	
		var options = $.extend(defaults, options);	// global options
		var params; // local options override
  	
	  	var me = $(this);
		var raphaelObj;
	  	var paths;
		var animation;
		var radius = options.circleRadius; // radius of dot in pixels
		var circleAnimationInit = 270;  // circle animation startpoint (270 is top of circle)
		var cx = radius + 1;
		var cy = radius + 1;
		
		// public function
		me.setDot = function(action, optionsLocal){
			
			// Allow local options override into separate global params
			params = $.extend({}, options, optionsLocal);

			// clear animation and HTML from this element
			if (action !== "pauseAnimation" && action !== "resumeAnimation") {
				me.empty();
				if (typeof animation != "undefined"){
					animation.stop();
				}

				// set as Raphael drawable element
  				raphaelObj = Raphael(this.get(0));
			}
			
			switch(action){
			
				case 'drawInactiveCircle':
					drawInactiveCircle();
					break;
					
				case 'drawActiveCircle':
					drawActiveCircle();
					break;
					
				case 'drawNonAnimatingActiveCircle':
					drawNonAnimatingActiveCircle();
					break;
					
				case 'animateToActiveCircle':
					drawInactiveCircle();
					animateActiveCircle();
					break;
					
				case 'pauseAnimation':
					pauseAnimation();
					break;

				case 'resumeAnimation':
					resumeAnimation();
					break;

				default:
					trace("ERROR: invalid 'action' argument passed: '"+options.action+"'");
				
		
			}
		}
	
		// init setDot action
		me.setDot(options.action);
		
		function drawInactiveCircle() {  	
  			raphaelObj.circle(cx, cy, radius).attr({fill: params.inactiveColor, stroke: params.inactiveColor, "stroke-width": 1});
		}
		
		function drawActiveCircle() {  	
  			raphaelObj.circle(cx, cy, radius).attr({fill: params.activeColor, stroke: params.activeColor, "stroke-width": 1});
		}
		
		function drawNonAnimatingActiveCircle() {  	
  			raphaelObj.circle(cx, cy, radius).attr({fill: params.nonAnimatingActiveColor, stroke: params.nonAnimatingActiveColor, "stroke-width": 1});
		}
		
		function animateActiveCircle() {
			
			// set up segment
			raphaelObj.customAttributes.segment = function (x, y, r, a1, a2) {
	            var flag = (a2 - a1) > 180,
	                clr = (a2 - a1) / 360;
	            a1 = (a1 % 360) * Math.PI / 180;
	            a2 = (a2 % 360) * Math.PI / 180;
	            return {
	                path: [["M", x, y], ["l", r * Math.cos(a1), r * Math.sin(a1)], ["A", r, r, 0, +flag, 1, x + r * Math.cos(a2), y + r * Math.sin(a2)], ["z"]],
	                fill: params.activeColor
	            };
	        };
			
			paths = raphaelObj.set();
			paths.push(raphaelObj.path().attr({'segment': [cx, cy, radius, circleAnimationInit, 271], stroke: params.activeColor}));
			
			// now animate segment to full circle
			var val = circleAnimationInit + 359; // animate 359 degrees to create a full circel
	        animation = paths[0].animate({segment: [cx, cy, radius, circleAnimationInit, val]}, params.animateTime, "circular", function () {
	        	finishedAnimating(circleAnimationInit);
	        });

		}
  		
		function pauseAnimation () {
			var path = paths[0] || {};

			if (typeof path.pause === 'function') {
				path.pause();
			} else {
				trace("ERROR: 'action' argument passed: 'pauseAnimation' is not supported.");
			}
		
		}

		function resumeAnimation () {
			var path = paths[0] || {};

			if (typeof path.resume === 'function') {
				path.resume();
			} else {
				trace("ERROR: 'action' argument passed: 'resumeAnimation' is not supported.");
			}

		}
	    
	    function finishedAnimating(r){
			me.trigger('ANIMATION_DONE');
	    }
		
		function trace(msg) {
			if (typeof console == 'object' && typeof console.log != "undefined") {
				console.log("carousel log: "+msg);		
			}
		}
		
		
  		
  		return me;
	}
})(jQuery);