import Em from 'ember';

export default Em.Route.extend({
    templateName: 'accounts/account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    }
});
