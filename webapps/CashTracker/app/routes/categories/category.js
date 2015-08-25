import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    categories: function() {
        return this.store.findAll('category');
    }.property(),

    transactionTypes: [{
        'value': 'E',
        'name': "Expense"
    }, {
        'value': 'I',
        'name': "Income"
    }, {
        'value': 'T',
        'name': "Transfer"
    }],

    setupController: function(controller, model) {
        controller.set('category', model);
        controller.set('categories', this.get('categories'));
        controller.set('transactionTypes', this.get('transactionTypes'));
    },

    actions: {
        save: function() {
            let _this = this;
            let model = this.get('controller').get('category');
            model.save().then(function() {
                _this.transitionTo('categories');
            });
        }
    }
});
