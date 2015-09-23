import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('account');
    },

    deactivate: function() {
        this._super();
        this.store.unloadAll('account');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('accounts.new');
        },

        saveAccount: function(account) {
            account.save().then(() => {
                this.transitionTo('accounts');
            });
        },

        deleteRecord: function(account) {
            account.destroyRecord().then(() => {
                this.transitionTo('accounts');
            }, function(resp) {
                account.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
