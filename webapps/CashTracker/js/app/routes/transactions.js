CT.TransactionsRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('transaction');
    }
});
