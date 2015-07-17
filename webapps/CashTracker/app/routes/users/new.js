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
        save: function() {
            var _this = this;
            var model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('users');
            });
        }
    }
});
