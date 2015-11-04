AdminApp.NewUserController = Ember.Controller.extend({
    actions: {
        save: function(user) {
            console.log("save new user", user);

            var newUser = this.store.createRecord('user', {
                login: user.login,
                pwd: user.pwd,
                email: user.email
            });
            newUser.save();

            this.transitionTo('users');
        }
    }
});
