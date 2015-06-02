define('routes/CostCenterRoute', ['ember'], function(Ember) {
    "use strict";

    var Route = Ember.Route.extend({
        hideOpenedNav: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }.on('activate')
    });

    return Route;
});
