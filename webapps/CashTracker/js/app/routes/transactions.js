CT.TransactionsRoute = Ember.Route.extend({
    model: function() {
        /*return $.getJSON('/CashTracker/RestProvider/transactions').then(function(data) {
            return data.elements[0].value.list;
        });*/
        return this.store.find('transaction');
    }
});
