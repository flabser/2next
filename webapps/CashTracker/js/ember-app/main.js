(function(root) {
    require(['config'], function(config) {
        'use strict';

        requirejs.config(config);
        require(['App', 'ember'], function(App, Ember) {
            var app_name = config.app_name || 'CashTracker';
            root[app_name] = App = Ember.Application.create(App, {
                LOG_TRANSITIONS: true,
                LOG_TRANSITIONS_INTERNAL: true
            });
        });
    });
})(this);
