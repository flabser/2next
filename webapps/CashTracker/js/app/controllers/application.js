CT.ApplicationController = Ember.Controller.extend({
    init: function() {
        this.windowOnResize();
        $(window).resize(this.windowOnResize);
    },

    windowOnResize: function() {
        if (window.innerWidth <= 800) {
            $('body').addClass('phone');
        } else {
            $('body').removeClass('phone');
        }
    },

    actions: {
        navAppMenuToggle: function() {
            $('body').toggleClass('nav-app-open');
        },
        navUserMenuToggle: function() {
            $('body').toggleClass('nav-ws-open');
        },
        hideOpenedNav: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        },
        toggleSearchForm: function() {
            $('body').toggleClass('search-open');
        }
    }
});
