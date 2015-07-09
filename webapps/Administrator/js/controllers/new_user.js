AdminApp.NewUserController = Ember.ObjectController.extend({
    actions: {
        save: function(user) {
            console.log("save new user" + user);
            user.save();
            this.transitionTo('users');
        }
    }
});
