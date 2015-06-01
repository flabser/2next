(function(root) {
    require(['config'], function(config) {
        'use strict';

        requirejs.config(config);
        require(['App', 'router'], function(App, Router) {
            App.Router = new Router(App);
            root[config.app_name] = App;
            Backbone.history.start();
        });
    });
})(this);
