import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'cost-center',

    actions: {
        create: function() {
            this.transitionTo('cost_centers.new');
        },
        save: function() {
            var controller = this.controller;
            var newCostCenter = this.store.createRecord('costCenter', {
                name: controller.get('name')
            });
            newCostCenter.save();
        },
        cancel: function() {
            this.transitionTo('cost_centers');
        }
    }
});
