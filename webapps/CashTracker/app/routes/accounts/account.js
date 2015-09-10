import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'accounts/account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    users: function() {
        return this.store.findAll('user');
    }.property(),

    currencies: [{
        'value': 'KZT',
        'name': 'Kazakhstani Tenge'
    }, {
        'value': 'USD',
        'name': 'US Dollar'
    }, {
        'value': 'RUB',
        'name': 'Russian Ruble'
    }],

    setupController: function(controller, model) {
        controller.set('account', model);
        controller.set('users', this.get('users'));
        controller.set('currencies', this.get('currencies'));
    }
});
