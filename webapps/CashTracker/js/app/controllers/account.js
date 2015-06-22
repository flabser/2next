CT.AccountController = Ember.ObjectController.extend({
    actions: {
        save: function(account) {
            account.save();
            this.transitionTo('accounts');
        }
    }
});
