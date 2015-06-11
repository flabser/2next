CT.CostCentersRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('cost-center');
    }
});
