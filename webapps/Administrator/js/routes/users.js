AdminApp.UsersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('user');
    }
});

AdminApp.UsersNewRoute = Ember.Route.extend({
    templateName: 'user'
});
