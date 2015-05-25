define('LayoutView', [
    'jquery',
    'backbone',
    'AppMenuView',
    'ContentView'
], function($, Backbone, AppMenuView, ContentView) {

    var LayoutView = Backbone.View.extend({
        el: '#page-root',

        className: 'layout',

        initialize: function() {
            if (window.innerWidth <= 800) {
                $('body').addClass('phone');
            } else {
                $('body').removeClass('phone');
            }

            this.$el.addClass(this.className);
            ContentView.setLayoutView(this);
            AppMenuView.render();
            $('body').removeClass('no_transition');
        },

        events: {
            'click #toggle-nav-ws': 'toggleWsNav',
            'click #toggle-nav-app': 'toggleAppNav',
            'click #content-overlay': 'hideOpenedNav',
            'touchstart #content-overlay': 'hideOpenedNav',
            'submit form[name=search]': 'contentViewSearch',
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

        contentViewSearch: function(e) {
            e.preventDefault();
            ContentView.search(e.target);
        },

        loadContent: function(uri) {
            ContentView.load(uri);
        },

        render: function() {
            this.$el.show();
            $('.page-loading').remove();
            return this;
        }
    });

    return new LayoutView();
});
