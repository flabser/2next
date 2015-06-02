define('config', {
    app_name: 'CashTracker',
    urlArgs: "v=" + (new Date()).getTime(),
    baseUrl: './js',
    paths: {
        App: 'ember-app/app',
        models: 'ember-app/models',
        views: 'ember-app/views',
        controllers: 'ember-app/controllers',
        templates: 'ember-app/templates',
        router: 'ember-app/router',
        routes: 'ember-app/routes',
        /* libs */
        ember: 'lib/ember.min',
        text: 'lib/requirejs-text',
        jquery: '/SharedResources/vendor/jquery/jquery-1.11.3.min',
        'jquery.ui': '/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min'
    },
    shim: {
        ember: {
            deps: ['jquery'],
            exports: 'Ember'
        },
        'jquery.ui': ['jquery']
    },
    hbs: {
        disableI18n: true,
        templateExtension: "html"
    }
});
