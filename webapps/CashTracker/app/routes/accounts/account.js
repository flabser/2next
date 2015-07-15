import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    actions: {
        save: function(account) {
            console.log(account);
            account.save();
            this.transitionTo('accounts');
        }
    }
});
