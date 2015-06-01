define('routes/TransactionRoute', ['ember'], function(Ember) {
    "use strict";

    var Route = Ember.Route.extend({
        model: {
            userName: 'Medet'
        }
    });

    return Route;
});
