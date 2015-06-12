CT.AccountRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('account', params.account_id);
    }
});

CT.AccountIndexRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('account', params.account_id);
    }
});

CT.AccountNewRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('account');
    }
});
