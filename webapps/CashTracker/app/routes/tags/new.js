import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'tag',

    model: function(params) {
        return this.store.createRecord('tag', {
            name: '',
            color: 0
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
                model.transitionTo('tags');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
