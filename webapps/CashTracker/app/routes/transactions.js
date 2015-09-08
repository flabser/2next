import Em from 'ember';

export default Em.Route.extend({
    actions: {
        composeRecord: function() {
            this.transitionTo('transactions.new');
        },

        saveTransaction: function(transaction) {
            transaction.save().then(() => {
                this.transitionTo('transactions');
            });
        },

        deleteRecord: function(transaction) {
            transaction.destroyRecord().then(() => {
                this.transitionTo('transactions');
            }, function(resp) {
                transaction.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
