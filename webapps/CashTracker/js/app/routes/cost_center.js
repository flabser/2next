CT.CostCenterRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('cost-center', params.costcenter_id);
    }
});
