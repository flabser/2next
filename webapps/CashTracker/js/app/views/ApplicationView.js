define('views/ApplicationView', [
    'jquery',
    'backbone',
    'views/AppMenuView'
], function($, Backbone, AppMenuView) {

    var ApplicationView = Backbone.View.extend({
        el: '#page-root',

        className: 'layout',

        initialize: function() {
            if (window.innerWidth <= 800) {
                $('body').addClass('phone');
            } else {
                $('body').removeClass('phone');
            }

            this.$el.addClass(this.className);
        },

        events: {
            'click #toggle-nav-ws': 'toggleWsNav',
            'click #toggle-nav-app': 'toggleAppNav',
            'mousedown #content-overlay': 'hideOpenedNav',
            'touchstart #content-overlay': 'hideOpenedNav',
            'submit form[name=search]': 'searchSubmit',
            'click #toggle-head-search': 'toggleSearchForm',
            'mousedown #search-close': 'toggleSearchForm',
            'click a': 'loadContent'
        },

        toggleWsNav: function(e) {
            e.preventDefault();
            $('body').toggleClass('nav-ws-open');
        },

        toggleAppNav: function(e) {
            e.preventDefault();
            $('body').toggleClass('nav-app-open');
        },

        hideOpenedNav: function(e) {
            e && e.preventDefault();
            $('body').removeClass('nav-app-open nav-ws-open');
        },

        toggleSearchForm: function(e) {
            e.preventDefault();
            $('body').toggleClass('search-open');
        },

        searchSubmit: function(e) {
            e.preventDefault();
            ContentView.search(e.target);
        },

        render: function() {
            this.$el.show();
            $('.page-loading').remove();
            AppMenuView.render();
            $('body').removeClass('no_transition');
            return this;
        }
    });

    return ApplicationView;
});
