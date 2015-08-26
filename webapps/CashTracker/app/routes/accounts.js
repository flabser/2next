import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('account');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('accounts.new');
        },

        deleteRecord: function(account) {
            var _this = this;
            account.destroyRecord().then(function() {
                _this.transitionTo('accounts');
            }, function(resp) {
                account.rollback();
                alert(resp.errors.message);
            });
        }
    }
});
