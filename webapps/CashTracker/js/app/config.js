define('config', {
    app_name: 'CashTracker',
    urlArgs: 'v=' + (new Date()).getTime(),
    baseUrl: './js',
    paths: {
        jquery: '/SharedResources/vendor/jquery/jquery-1.11.3.min',
        'jquery.ui': '/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min',
        backbone: 'lib/backbone-min',
        underscore: 'lib/underscore-min',
        'backbone.localStorage': 'lib/backbone.localStorage',
        text: 'lib/requirejs-text',
        handlebars: 'lib/handlebars.amd.min',
        /* --- */
        App: 'app/app',
        router: 'app/router',
        views: 'app/views',
        models: 'app/models',
        templates: 'app/templates'
    },
    shim: {
        'jquery.ui': ['jquery'],
        underscore: {
            exports: '_'
        },
        backbone: {
            deps: ['underscore', 'jquery', 'jquery.ui'],
            exports: 'Backbone'
        },
        'backbone.localStorage': {
            deps: ['backbone'],
            exports: 'Backbone'
        }
    },
    hbs: {
        disableI18n: true,
        templateExtension: 'html'
    }
});
