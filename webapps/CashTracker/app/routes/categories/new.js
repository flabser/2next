import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'category',

    model: function(params) {
        return this.store.createRecord('category', {

        });
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
                model.transitionTo('categories');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
