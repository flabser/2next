import TransactionRoute from './transaction';

export default TransactionRoute.extend({
    model: function() {
        return this.store.createRecord('transaction');
    }
});
