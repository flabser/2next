CT.CostCentersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('cost_center');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.CostCentersNewRoute = Ember.Route.extend({
    templateName: 'cost_center',

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
