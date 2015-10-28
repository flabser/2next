import TransactionRoute from './transaction';

export default TransactionRoute.extend({
    model: function(params, transition) {
        var type = 'E';
        if (transition.queryParams) {
            if (transition.queryParams.type === 'expense') {
                type = 'E';
            } else if (transition.queryParams.type === 'income') {
                type = 'I';
            } else if (transition.queryParams.type === 'transfer') {
                type = 'T';
            }
        }

        return this.store.createRecord('transaction', {
            transactionType: type
        });
    }
});
