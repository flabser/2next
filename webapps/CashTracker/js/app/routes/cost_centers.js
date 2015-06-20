CT.CostCentersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('cost-center');
    }
});

CT.CostCentersNewRoute = Ember.Route.extend({
    templateName: 'cost_center'
});
