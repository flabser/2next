CT.UserRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('user', params.user_id);
    },

    actions: {
        save: function(user) {
            user.save();
            this.transitionTo('users');
        }
    }
});
