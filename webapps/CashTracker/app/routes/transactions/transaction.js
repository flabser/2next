import Em from 'ember';
import ModelRouteMixin from '../../mixins/routes/model';
import TransactionTypesMixin from '../../mixins/transaction-types';

export default Em.Route.extend(ModelRouteMixin, TransactionTypesMixin, {
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
        controller.set('transactionTypes', this.transactionTypes);
    }
});
