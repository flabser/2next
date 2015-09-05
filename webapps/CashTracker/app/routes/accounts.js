import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('account');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('accounts.new');
        },

        saveAccount: function(account) {
            var _this = this;
            account.save().then(function() {
                _this.transitionTo('accounts');
            });
        },

        deleteRecord: function(account) {
            var _this = this;
            account.destroyRecord().then(function() {
                _this.transitionTo('accounts');
            }, function(resp) {
                account.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
