CT.AccountRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    actions: {
        save: function(account) {
            account.save();
            this.transitionTo('accounts');
        }
    }
});
