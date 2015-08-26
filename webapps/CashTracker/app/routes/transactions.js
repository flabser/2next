import Em from 'ember';

export default Em.Route.extend({
    actions: {
        composeRecord: function() {
            this.transitionTo('transactions.new');
        },

        deleteRecord: function(transaction) {
            var _this = this;
            transaction.destroyRecord().then(function() {
                _this.transitionTo('transactions');
            }, function(resp) {
                transaction.rollback();
                alert(resp.errors.message);
            });
        }
    }
});
