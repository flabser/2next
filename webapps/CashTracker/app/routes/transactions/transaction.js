import Em from 'ember';
import ModelRoute from '../../mixins/model-route';

export default Em.Route.extend(ModelRoute, {
    templateName: 'transactions/transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    accounts: function() {
        return this.store.findAll('account');
    }.property(),

    categories: function() {
        return this.store.findAll('category');
    }.property(),

    costCenters: function() {
        return this.store.findAll('costCenter');
    }.property(),

    tags: function() {
        return this.store.findAll('tag');
    }.property(),

    setupController: function(controller, model) {
        controller.set('transaction', model);
        controller.set('accounts', this.get('accounts'));
        controller.set('categories', this.get('categories'));
        controller.set('costCenters', this.get('costCenters'));
        controller.set('tags', this.get('tags'));
    }
});
