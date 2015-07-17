import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'account',

    model: function(params) {
        return this.store.createRecord('account', {
            type: 0,
            name: '',
            currency: 'KZT',
            openingBalance: 0,
            amountControl: 0,
            owner: null,
            observers: []
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
                model.transitionTo('accounts');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
