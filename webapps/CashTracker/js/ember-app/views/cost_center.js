define('views/cost_center', [
    'ember',
    'templates/costcenter'
], function(Ember, tpl) {
    "use strict";

    var CostCenterView = Ember.View.extend({
        templateName: 'templates/costcenter',
    });

    return CostCenterView;
});
