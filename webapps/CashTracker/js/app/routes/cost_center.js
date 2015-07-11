CT.CostCentersCostCenterRoute = Ember.Route.extend({
    templateName: 'cost_center',

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
