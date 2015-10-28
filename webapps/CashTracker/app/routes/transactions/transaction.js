import Em from 'ember';
import ModelRoute from '../../mixins/model-route';

export default Em.Route.extend(ModelRoute, {
    templateName: 'transactions/transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    setupController: function(controller, model) {
        controller.set('transaction', model);
        controller.set('accounts', this.store.findAll('account'));
        controller.set('categories', this.store.findAll('category'));
        controller.set('costCenters', this.store.findAll('costCenter'));
        controller.set('tags', this.store.findAll('tag'));
    }
});
