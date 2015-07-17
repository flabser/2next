import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'user',

    model: function(params) {
        return this.store.createRecord('user', {

        });
    },

    deactivate: function() {
        var model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    },

    actions: {
        save: function(model) {
            model.save().then(function() {
                model.transitionTo('users');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
