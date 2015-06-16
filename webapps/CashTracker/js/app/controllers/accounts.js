CT.AccountsController = Ember.ArrayController.extend({
    queryParams: ['offset', 'limit', 'order_by'],

    actions: {
        selectAll: function() {;
        }
    }
});

CT.AccountsNewController = Ember.ArrayController.extend({
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
