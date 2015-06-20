CT.TransactionController = Ember.ObjectController.extend({
    actions: {
        save: function(transaction) {
            transaction.save();
            this.transitionTo('transactions');
        }
    }
});
