define('routes/cost_center', [
    'ember',
    'routes/application'
], function(Ember, ApplicationRoute) {
    "use strict";

    var Route = ApplicationRoute.extend({
        model: function() {
            return {
                name: 'medet'
            }
        }
    });

    return Route;
});
