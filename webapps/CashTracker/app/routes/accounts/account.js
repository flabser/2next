import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    actions: {
        save: function(account) {
            account.save().then(function() {
                this.transitionTo('accounts');
            });
        }
    }
});
