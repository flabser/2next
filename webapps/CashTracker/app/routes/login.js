import Ember from 'ember';

export default Ember.Route.extend({

    actions: {
        login: function() {
            var route = this,
                controller = this.get('controller');

            var userName = controller.get('username'),
                password = controller.get('password');

            this.session.login(userName, password).then(function(user) {
                route.session.set('user', user);

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
        this.session.set('user', null);
    },

    resetController: function(controller) {
        controller.setProperties({
            username: null,
            password: null,
            transition: null
        });
    }
});
