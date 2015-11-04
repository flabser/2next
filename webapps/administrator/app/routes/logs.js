AdminApp.LogsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('log');
    }
});

