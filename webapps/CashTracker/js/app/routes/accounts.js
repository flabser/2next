CT.AccountsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('account');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.AccountsNewRoute = Ember.Route.extend({
    templateName: 'account',

    actions: {
        create: function() {
            this.transitionTo('accounts.new');
        },
        save: function() {
            var controller = this.controller;
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
