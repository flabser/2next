define('views/cost_centers', [
    'ember',
    'templates/costcenters'
], function(Ember, tpl) {
    "use strict";

    var CostCentersView = Ember.View.extend({
        templateName: 'templates/costcenters',
    });

    return CostCentersView;
});
