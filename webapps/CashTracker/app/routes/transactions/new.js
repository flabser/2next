import TransactionRoute from './transaction';

export default TransactionRoute.extend({
    model: function() {
        return this.store.createRecord('transaction', {
            author: null,
            regDate: null,
            date: null,
            endDate: null,
            parentCategory: null,
            category: null,
            account: null,
            costCenter: null,
            amount: 0,
            repeat: false,
            every: 0,
            repeatStep: 0,
            basis: '',
            observers: null,
            comment: ''
        });
    },

    deactivate: function() {
        var model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    }
});
