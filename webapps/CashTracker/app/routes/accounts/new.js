import AccountRoute from './account';

export default AccountRoute.extend({
    model: function() {
        return this.store.createRecord('account', {
            type: 0,
            name: '',
            currency: 'KZT',
            openingBalance: 0,
            amountControl: 0,
            writers: [],
            readers: []
        });
    }
});
