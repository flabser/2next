CT.TransactionRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    actions: {
        save: function(transaction) {
            transaction.save();
            this.transitionTo('transactions');
        }
    }
});
