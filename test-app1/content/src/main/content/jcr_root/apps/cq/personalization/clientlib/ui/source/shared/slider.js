$CQ(function() {

    /**
     * A helper class providing utility methods to create a jcarousel object.<br>
     * See <code>/etc/clientlibs/foundation/personalization/jcarousel/jquery.jcarousel.js</code>
     * <br>
     * Construction options:<ul>
     *     <li>vertical: true to create a vertical slider (defaults to false).</li>
     *     <li>clazz: custom css class to append to top container</li>
     *     <li>parent: HTMLElement to append slider to</li>
     *</ul>
     * @class CQ_Analytics.Slider
     */
    CQ_Analytics.Slider = function(options) {
        return {
            /**
             * Initializes the slider
             */
            init: function() {
                this.vertical = options.vertical;
                this.clazz = options.clazz;
                this.parent = options.parent;
                this.container = $CQ("<div>")
                    .addClass("cq-cc-slider")
                    .addClass("cq-cc-slider-" + ((this.vertical) ? "vertical" : "horizontal"))
                    .addClass(this.clazz)
                    .appendTo(this.parent);
                this.container.hide();

                this.carousel = $CQ("<ul>")
                    .addClass("jcarousel-skin-cq-cc")
                    .appendTo(this.container);
            },

            /**
             * Shows the slider
             */
            show: function() {
                if( !this.isWidget ) {
                    var currentObj = this;
                    options.initCallback = function(carousel) {
                        currentObj.carouselObj = carousel;
                    };
                    this.carousel.jcarousel(options);
                    this.isWidget = true;
                }

                var currentObj = this;
                this.select(this.computeSelectedIndex(), true, true);
                if (this.vertical) {
                    this.container.slideDown("fast");
                } else {
                    $CQ(".cq-cc-slider", this.parent).css("top",(this.parent.position().top - 9) + "px");
                    $CQ(".cq-cc-slider", this.parent).css("left",(this.parent.position().left - 27) + "px");
                    $CQ(".cq-cc-slider", this.parent).fadeIn(1000, function() {
                        $CQ(".jcarousel-container-horizontal", currentObj.parent).animate({width: "270px" }, "fast");
                        $CQ(".jcarousel-clip-horizontal", currentObj.parent).animate({width: "268px"}, "fast", function() {
                            //refresh needed for size computations
                            currentObj.carousel.jcarousel();
                        });
                    });
                }

                currentObj.container.bind("click", this.stopPropagation);
                $CQ(document).bind("click", { scope: this }, this.handleDocClick);
            },

            /**
             * Handles the document click: hides the slider.
             * @private
             */
            handleDocClick: function(event) {
                event.data.scope.hide();
            },

            /**
             * Stops the event propagation.
             * @private
             */
            stopPropagation: function(event) {
                event.stopPropagation();
            },

            /**
             * Hides the slider.
             */
            hide: function() {
                $CQ(document).unbind("click", this.handleDocClick);
                this.container.unbind("click", this.stopPropagation);
                if (this.vertical) {
                    this.container.slideUp("fast");
                } else {
                    var currentObj = this;
                    $CQ(".jcarousel-container-horizontal", this.parent).animate({width: "90px"}, "fast");
                    $CQ(".jcarousel-clip-horizontal", this.parent).animate({width: "90px"}, "fast", function() {
                        $CQ(".cq-cc-slider", currentObj.parent).fadeOut(1000);
                    });
                }
            },

            /**
             * Handles selection inside the slider
             * @private
             */
            select: function(num, force, noAnimation) {
                var selected = this.getSelected();
                if (force || selected.length == 0 || selected.attr("jcarouselindex") != num) {
                    var toSelect = this.getItem(num);
                    selected.removeClass("jcarousel-item-selected");
                    toSelect.addClass("jcarousel-item-selected");

                    //because jcarousel duplicates elements
                    $CQ(this.container).find(".jcarousel-item-selected-marker").removeClass("jcarousel-item-selected-marker");

                    var currentId = this.getCurrentValue();
                    var id = toSelect.children().attr("data-id");
                    $CQ(this.container).find("[data-id='" + id + "']").addClass("jcarousel-item-selected-marker");
                    if (currentId != id) {
                        this.onSelect(id);
                    }
                    this.carouselObj.scroll($CQ.jcarousel.intval(toSelect.attr("jcarouselindex")), !noAnimation);
                }
            },

            /**
             * Returns the selected item.
             * @private
             */
            getSelected: function() {
                return $CQ(this.container).find(".jcarousel-item-selected");
            },

            /**
             * Computes the selected item based on the current value.
             * @private
             */
            computeSelectedIndex: function() {
                var id = this.getCurrentValue();
                return $CQ(this.container).find("[data-id='" + id + "']").parent().attr("jcarouselindex") || 0;

            },

            /**
             * Returns an item.
             * @private
             */
            getItem: function(num) {
                return $CQ(this.container).find("[jcarouselindex=" + num + "]");
            },

            /**
             * Method called when an item gets selected. To get the item, use: [data-id='" + id + "'].
             * To override.
             * @param {String} id Id of the selected item
             */
            onSelect: function(id) {
                //to override
            },

            /**
             * Method called to get the current value of the slider. Returned value must match the id.
             * To override.
             */
            getCurrentValue: function() {
                //to override
            }
        }
    };
});