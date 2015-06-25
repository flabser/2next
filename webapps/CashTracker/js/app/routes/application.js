CT.ApplicationRoute = Ember.Route.extend({

    actions: {
        logout: function() {
            var route = this;

            //API.logout().then(function() {
                route.session.set('user', null);
                route.transitionTo('index');
            //});
        },

        expireSession: function() {
            //API.token = 'expired';
        },

        error: function(error, transition) {
            if (error.status === 'Unauthorized') {
                var loginController = this.controllerFor('login');

                loginController.setProperties({
                    message: error.message,
                    transition: transition
                });

                this.transitionTo('login');
            } else {
                // Allow other error to bubble
                return true;
            }
        },

        willTransition: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
