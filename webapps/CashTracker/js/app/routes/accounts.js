CT.AccountsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('account');
    }
});
