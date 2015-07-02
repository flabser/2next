CT.ApplicationRoute = Ember.Route.extend({

    model: function() {
        var route = this,
            sessionController = this.controllerFor('session');

        CT.i18n.getTranslations();
        var req = sessionController.getSession();

        req.then(function(result) {
            if (result.authUser.login) {
                route.session.set('auth_user', result.authUser);
                return result.authUser;
            }
        });
        return req;
    },

    actions: {
        logout: function() {
            var route = this;
            this.controllerFor('session').logout().then(function() {
                route.session.set('auth_user', null);
                // route.transitionTo('index');
                window.location.href = 'Provider?id=welcome';
            });

        },

        error: function(error, transition) {
            if (error.status === 401 || (!this.session.get('auth_user') && this.routeName !== 'login')) {

                /*this.controllerFor('login').setProperties({
                    transition: transition
                });*/

                // this.transitionTo('login');
                window.location.href = 'Provider?id=login' + location.hash;
            } else {
                return true;
            }
        },

        willTransition: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
