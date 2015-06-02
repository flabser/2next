define('routes/TransactionsRoute', ['ember'], function(Ember) {
    "use strict";

    var Route = Ember.Route.extend({
        hideOpenedNav: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }.on('activate'),

        model: function() {
            return [{
                title: 'test'
            }]
        }
    });

    return Route;
});
