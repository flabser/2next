CT.UsersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('user');
    }
});

CT.UsersNewRoute = Ember.Route.extend({
    templateName: 'user'
});
