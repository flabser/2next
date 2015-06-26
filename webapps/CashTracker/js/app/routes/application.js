CT.ApplicationRoute = Ember.Route.extend({

    model: function() {
        var route = this;
        var authUser = this.store.find('auth_user');
        authUser.then(function(user) {
            route.session.set('auth_user', user);
        });
        return authUser;
    },

    actions: {
        logout: function() {
            var route = this;
            var authUser = this.session.get('auth_user');

            authUser.deleteRecord();
            authUser.save().then(function() {
                route.session.set('auth_user', null);
                route.transitionTo('index');
            });
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
