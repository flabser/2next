import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    actions: {
        save: function(transaction) {
            transaction.save();
            this.transitionTo('transactions');
        }
    }
});
