define('views/cost_centers', [
    'ember',
    'text!templates/costcenters.html'
], function(Ember, tpl) {
    "use strict";

    var CostCentersView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return CostCentersView;
});
