CT.TransactionRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    }
});
