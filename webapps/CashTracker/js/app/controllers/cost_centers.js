CT.CostCentersController = Ember.ArrayController.extend({
    actions: {
        selectAll: function() {}
    }
});

CT.CostCentersNewController = Ember.ArrayController.extend({
    actions: {
        create: function() {
            this.transitionTo('cost_centers.new');
        },
        save: function() {
            var newCostCenter = this.store.createRecord('costCenter', {
                name: this.get('name')
            });
            newCostCenter.save();
        },
        cancel: function() {
            this.transitionTo('cost_centers');
        }
    }
});
