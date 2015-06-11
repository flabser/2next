CT.AccountsRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('account');
    }
});
