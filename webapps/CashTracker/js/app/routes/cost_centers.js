CT.CostCentersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('cost_center');
    }
});

CT.CostCentersNewRoute = Ember.Route.extend({
    templateName: 'cost_center'
});
