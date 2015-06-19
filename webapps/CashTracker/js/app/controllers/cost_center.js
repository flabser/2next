CT.CostCenterController = Ember.ObjectController.extend({
    actions: {
        save: function(costCenter) {
            costCenter.save().then(function() {
                this.transitionTo('cost_centers');
            });
        }
    }
});
