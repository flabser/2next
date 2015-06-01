define('views/CostCenterView', [
    'ember',
    'text!templates/costcenter.html',
    'controllers/CostCenterController'
], function(Ember, tpl, CostCenterController) {
    "use strict";

    var CostCenterView = Ember.View.extend({
        controller: CostCenterController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return CostCenterView;
});
