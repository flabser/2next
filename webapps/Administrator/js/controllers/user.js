AdminApp.UserController = Ember.ObjectController.extend({
    actions: {
        save: function(user) {
        	console.log("upda5te user");

            user.save();
            this.transitionTo('users');
        }
    }
});
