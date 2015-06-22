CT.AccountsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('account');
    }
});

CT.AccountsNewRoute = Ember.Route.extend({
    templateName: 'account'
});
