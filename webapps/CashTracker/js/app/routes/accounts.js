CT.AccountsRoute = Ember.Route.extend({
    model: function(params) {
        // return $.getJSON('/CashTracker/RestProvider/accounts');
        return this.store.find('account');
    }
});
