import Em from 'ember';

export default Em.Route.extend({
    actions: {
        composeRecord: function() {
            this.transitionTo('transactions.new');
        },

        saveTransaction: function(transaction) {
            var _this = this;
            transaction.save().then(function() {
                _this.transitionTo('transactions');
            });
        },

        deleteRecord: function(transaction) {
            var _this = this;
            transaction.destroyRecord().then(function() {
                _this.transitionTo('transactions');
            }, function(resp) {
                transaction.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
