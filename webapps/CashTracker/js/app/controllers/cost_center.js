CT.CostCenterController = Ember.ObjectController.extend({
    actions: {
        save: function(costCenter) {
            costCenter.save();
            this.transitionTo('cost_centers');
        }
    }
});
