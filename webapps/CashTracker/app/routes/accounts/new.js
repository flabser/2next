import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'account',

    actions: {
        save: function() {
            var controller = this.controller;
            // TODO validate
            var newAccount = this.store.createRecord('account', {
                type: controller.get('type'),
                name: controller.get('name'),
                currency: controller.get('currency'),
                openingBalance: controller.get('openingBalance'),
                amountControl: controller.get('amountControl'),
                owner: controller.get('owner'),
                observers: controller.get('observers')
            });
            newAccount.save();
        },
        cancel: function() {
            this.transitionTo('accounts');
        }
    }
});
