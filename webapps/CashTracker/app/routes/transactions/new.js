import TransactionRoute from './transaction';

export default TransactionRoute.extend({
    model: function() {
        return this.store.createRecord('transaction', {
            parentCategory: null,
            date: null,
            startDate: null,
            endDate: null,
            category: null,
            account: null,
            costCenter: null,
            amount: 0,
            repeat: false,
            every: 0,
            repeatStep: 0,
            note: '',
            basis: ''
        });
    }
});
