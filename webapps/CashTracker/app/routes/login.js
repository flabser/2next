import Em from 'ember';

export default Em.Route.extend({
    beforeModel: function() {
        this.session.set('user', null);
    },

    resetController: function(controller) {
        controller.setProperties({
            username: null,
            password: null,
            transition: null
        });
    },

    actions: {
        login: function() {
            var controller = this.get('controller');

            var userName = controller.get('username'),
                password = controller.get('password');

            this.session.login(userName, password).then((user) => {
                this.session.set('user', user);

                var transition = controller.get('transition');
                if (transition) {
                    transition.retry();
                } else {
                    this.transitionTo('index');
                }
            });
        },

        cancel: function() {
            this.transitionTo('index');
        }
    }
});
