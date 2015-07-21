import AccountRoute from './account';

export default AccountRoute.extend({
    model: function() {
        return this.store.createRecord('account', {
            type: 0,
            name: '',
            currency: 'KZT',
            openingBalance: 0,
            amountControl: 0,
            owner: null,
            observers: []
        });
    },

    deactivate: function() {
        let model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    }
});
