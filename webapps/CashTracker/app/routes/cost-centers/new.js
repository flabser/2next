import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'cost-center',

    model: function(params) {
        return this.store.createRecord('costCenter');
    },

    deactivate: function() {
        var model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    },

    actions: {
        save: function() {
            model.save().then(function() {
                model.transitionTo('cost_centers');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
