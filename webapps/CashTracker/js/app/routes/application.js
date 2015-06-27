CT.ApplicationRoute = Ember.Route.extend({

    model: function() {
        return this.store.find('auth_user');
    },

    afterModel: function(user) {
        this.session.get('auth_user', user);
    },

    actions: {
        logout: function() {
            var route = this;
            var authUser = this.session.get('auth_user');

            if (authUser) {
                authUser.deleteRecord();
                authUser.save().then(function() {
                    route.session.set('auth_user', null);
                    route.transitionTo('index');
                });
            } else {
                route.transitionTo('index');
            }
        },

        error: function(error, transition) {
            if (error.status === 401) {

                this.controllerFor('login').setProperties({
                    transition: transition
                });

                this.transitionTo('login');
            } else {
                return true;
            }
        },

        willTransition: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
