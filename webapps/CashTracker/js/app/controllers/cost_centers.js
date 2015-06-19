CT.CostCentersController = Ember.ArrayController.extend({
    queryParams: ['offset', 'limit', 'order_by']
});


CT.CostCentersNewController = Ember.ArrayController.extend({
    actions: {
        create: function() {
            this.transitionTo('cost_centers.new');
        },
        save: function() {
            var newAccount = this.store.createRecord('costCenter', {
                name: this.get('name')
            });
            newAccount.save();
        },
        cancel: function() {
            this.transitionTo('cost_centers');
        }
    }
});
