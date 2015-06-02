define('routes/application', ['ember'], function(Ember) {
    "use strict";

    var ApplicationRoute = Ember.Route.extend({
        actions: {
            willTransition: function() {
                $('body').removeClass('nav-app-open nav-ws-open');
            }
        }
    });

    return ApplicationRoute;
});
