CT.LoginRoute = Ember.Route.extend({

    actions: {
        login: function() {
            var route = this,
                sessionController = this.controllerFor('session'),
                controller = this.get('controller');

            var userName = controller.get('username'),
                password = controller.get('password');

            sessionController.login(userName, password).then(function(user) {
                route.session.set('auth_user', user);

                var transition = controller.get('transition');
                if (transition) {
                    transition.retry();
                } else {
                    route.transitionTo('index');
                }
            });
        },

        cancel: function() {
            this.transitionTo('index');
        }
    },

    beforeModel: function() {
        this.session.set('auth_user', null);
    },

    resetController: function(controller) {
        controller.setProperties({
            username: null,
            password: null,
            transition: null
        });
    }
});
