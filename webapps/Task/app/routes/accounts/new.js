import AccountRoute from './account';

export default AccountRoute.extend({
    model: function() {
        return this.store.createRecord('account');
    }
});
