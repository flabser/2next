define('views/cost_center', [
    'ember',
    'text!templates/costcenter.html'
], function(Ember, tpl) {
    "use strict";

    var CostCenterView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return CostCenterView;
});
