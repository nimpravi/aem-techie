// usage: log('inside coolFunc', this, arguments);
// paulirish.com/2009/log-a-lightweight-wrapper-for-consolelog/
window.log = function f(){ log.history = log.history || []; log.history.push(arguments); if(this.console) { var args = arguments, newarr; args.callee = args.callee.caller; newarr = [].slice.call(args); if (typeof console.log === 'object') log.apply.call(console.log, console, newarr); else console.log.apply(console, newarr);}};

// make it safe to use console.log always
(function(a){function b(){}for(var c="assert,count,debug,dir,dirxml,error,exception,group,groupCollapsed,groupEnd,info,log,markTimeline,profile,profileEnd,time,timeEnd,trace,warn".split(","),d;!!(d=c.pop());){a[d]=a[d]||b;}})
(function(){try{console.log();return window.console;}catch(a){return (window.console={});}}());


// place any jQuery/helper plugins in here, instead of separate, slower script files.


/**
 * jQuery plugin
 * Equal heights of matched elements
 */
;(function ($) {

	$.fn.equalHeight = function ( options ) {

		var maxHeight = 0;
		var curHeight = 0;

		return this.each(function () {

			curHeight = $(this).height();
			if (curHeight > maxHeight) {
				maxHeight = curHeight;
			}
			
		}).css({'height': maxHeight});

	}

})(jQuery);

/**
 * jQuery plugin
 * Centers a block DOM element using absolute positioning
 */
;(function ($) {

	$.fn.centerBlock = function ( ) {

		return this.each(function () {

			var el = $(this);
			el.css({
			    position: 'absolute',
			    left: '50%',
			    'margin-left': (0 - (parseInt(el.width(), 10) / 2))
			})

		});	

	}

})(jQuery);	

/**
 * jQuery plugin
 * Filters selection into visible rows, and returns the row elements as arrays.
 */
;(function ($) {

    function processRow (col, callback) {
        col = col || [];
    
        if (col.length !== 0) {
            return callback(col);
        }
    };

	$.fn.eachRow = function ( callback ) {

		var offsetTop = 0;
        var col = [];

		return this.each(function () {

			 // New row
            if ($(this).offset().top > offsetTop) {
                offsetTop = $(this).offset().top;
               
                if (processRow(col, callback) === false) {
                    return false;
                }

                col = [];
            } 
            
            // Add elem to row
            col = $(col).add($(this));
			
		});

        // Process last row
       processRow(col, callback);

	}

})(jQuery);

/**
 * jQuery plugin
 * Carousel component
 */
