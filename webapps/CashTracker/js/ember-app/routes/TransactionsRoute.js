define('routes/TransactionsRoute', ['ember'], function(Ember) {
    "use strict";

    var Route = Ember.Route.extend({
        model: function() {
            return [{
                title: 'test'
            }]
        }
    });

    return Route;
});
