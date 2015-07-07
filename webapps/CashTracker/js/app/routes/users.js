CT.UsersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('user');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.UsersNewRoute = Ember.Route.extend({
    templateName: 'user',

    actions: {
        create: function() {
            this.transitionTo('users.new');
        },
        save: function() {
            var newUser = this.store.createRecord('user', {
                name: this.get('name')
            });
            newUser.save();
        },
        cancel: function() {
            this.transitionTo('users');
        }
    }
});
