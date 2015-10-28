import Em from 'ember';
import ModelRoute from '../../mixins/model-route';

export default Em.Route.extend(ModelRoute, {
    templateName: 'accounts/account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    currencies: [{
        'code': 'KZT',
        'name': 'Kazakhstani Tenge'
    }, {
        'code': 'USD',
        'name': 'US Dollar'
    }, {
        'code': 'RUB',
        'name': 'Russian Ruble'
    }],

    setupController: function(controller, model) {
        controller.set('account', model);
        controller.set('users', this.store.findAll('user'));
        controller.set('currencies', this.currencies);
    }
});
