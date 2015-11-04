AdminApp.AppsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('app');
    }
});

AdminApp.UsersNewRoute = Ember.Route.extend({
    templateName: 'app'
});
