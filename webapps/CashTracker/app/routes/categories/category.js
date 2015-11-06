import Em from 'ember';
import ModelRouteMixin from '../../mixins/routes/model';
import TransactionTypesMixin from '../../mixins/transaction-types';

export default Em.Route.extend(ModelRouteMixin, TransactionTypesMixin, {
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    setupController: function(controller, model) {
        controller.set('category', model);
        controller.set('categories', this.store.findAll('category'));
        controller.set('transactionTypes', this.transactionTypes);
    }
});
