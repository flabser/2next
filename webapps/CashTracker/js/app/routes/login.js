CT.LoginRoute = Ember.Route.extend({
    actions: {
        submit: function() {
            var route = this,
                controller = this.get('controller');

            var username = controller.get('username'),
                password = controller.get('password');

            controller.set('message', null);

            //API.login(username, password).then(
            //    function(user) {
                    var transition = controller.get('transition');

                    route.session.set('user', user);

                    if (transition) {
                        transition.retry();
                    } else {
                        route.transitionTo('index');
                    }
            //    },
            //    function(error) {
            //        controller.set('message', error.message);
            //    }
            //);
        },

        cancel: function() {
            this.transitionTo('index');
        }
    },

    beforeModel: function() {
        //API.token = null;
        this.session.set('user', null);
    },

    resetController: function(controller) {
        controller.setProperties({
            username: null,
            password: null,
            message: null,
            transition: null
        });
    }
});
