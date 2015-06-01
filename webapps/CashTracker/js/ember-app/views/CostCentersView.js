define('views/CostCentersView', [
    'ember',
    'text!templates/costcenters.html',
    'controllers/CostCentersController'
], function(Ember, tpl, CostCentersController) {
    "use strict";

    var CostCentersView = Ember.View.extend({
        controller: CostCentersController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return CostCentersView;
});
