require.config({
    baseUrl: './js',
    paths: {
        jquery: '/SharedResources/vendor/jquery/jquery-1.11.3.min',
        jqueryui: '/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min',
        backbone: 'lib/backbone-min',
        underscore: 'lib/underscore-min',
        'backbone.localStorage': 'lib/backbone.localStorage',
        handlebars: 'lib/handlebars.amd.min',
        CashTrackerRouter: 'app/router',
        LayoutView: 'app/views/LayoutView',
        ContentView: 'app/views/ContentView'
    },
    shim: {
        underscore: {
            exports: '_'
        },
        backbone: {
            deps: ['underscore', 'jquery', 'jqueryui'],
            exports: 'Backbone'
        },
        'backbone.localStorage': {
            deps: ['backbone'],
            exports: 'Backbone'
        }
    }
});

require(['backbone', 'CashTrackerRouter'], function(Backbone, CashTrackerRouter) {
    new CashTrackerRouter();
    Backbone.history.start();
});
