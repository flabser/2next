import Em from 'ember';

export default Em.Route.extend({
    templateName: 'transactions/transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    }
});
