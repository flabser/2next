CT.ApplicationRoute = Ember.Route.extend({

    model: function() {
        var route = this,
            controller = this.get('controller'),
            sessionController = this.controllerFor('session'),
            loginController = this.controllerFor('login');

        var req = sessionController.getSession();

        req.then(function(user) {
            if (user.authUser.login) {
                route.session.set('auth_user', user.authUser);
                return user;
            }
        });
        return req;
    },

    actions: {
        logout: function() {
            var route = this;
            this.controllerFor('session').logout().then(function() {
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
                if (!this.session.get('auth_user') && this.routeName !== 'login') {
                    this.transitionTo('login');
                }
                return true;
            }
        },

        willTransition: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
