CT.CostCenterRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('cost_center', params.costcenter_id);
    },

    actions: {
        save: function(costCenter) {
            costCenter.save();
            this.transitionTo('cost_centers');
        }
    }
});
