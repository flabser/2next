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
            var newAccount = this.store.createRecord('account', {
                type: this.get('type'),
                name: this.get('name'),
                currency: this.get('currency'),
                openingBalance: this.get('openingBalance'),
                amountControl: this.get('amountControl'),
                owner: this.get('owner'),
                observers: this.get('observers')
            });
            newAccount.save();
        },
        cancel: function() {
            this.transitionTo('accounts');
        }
    }
});
