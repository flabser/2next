CT.AccountController = Ember.ObjectController.extend({
    actions: {
        save: function(account) {
            account.save().then(function() {
                this.transitionTo('accounts');
            });
        }
    }
});
