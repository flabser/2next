CT.TransactionsRoute = Ember.Route.extend({
    queryParams: {
        offset: {
            refreshModel: true
        },
        limit: {
            refreshModel: true
        },
        order_by: {
            refreshModel: true
        }
    },

    model: function(params) {
        return this.store.find('transaction');
    },

    beforeModel: function(transition) {
        if (transition.targetName === 'transactions.index') {
            if (!parseInt(transition.queryParams.limit, 0)) {
                transition.queryParams.limit = 10;
            }

            if (!parseInt(transition.queryParams.offset, 0)) {
                transition.queryParams.offset = 0;
            }

            if (!transition.queryParams.order_by || transition.queryParams.order_by === 'undefined') {
                transition.queryParams.order_by = '';
            }

            this.transitionTo('transactions', {
                queryParams: transition.queryParams
            });
        }

        this._super(transition);
    },

    actions: {
        addTransaction: function() {
            var controller = this.controller;
            var newTransaction = this.store.createRecord('transaction', {
                author: controller.get('author'),
                regDate: controller.get('regDate'),
                date: controller.get('date'),
                endDate: controller.get('endDate'),
                parentCategory: controller.get('parentCategory'),
                category: controller.get('category'),
                account: controller.get('account'),
                costCenter: controller.get('costCenter'),
                amount: controller.get('amount'),
                repeat: controller.get('repeat'),
                every: controller.get('every'),
                repeatStep: controller.get('repeatStep'),
                basis: controller.get('basis'),
                observers: controller.get('observers'),
                comment: controller.get('comment')
            });
            newTransaction.save();
        }
    }
});

CT.TransactionsNewRoute = Ember.Route.extend({
    templateName: 'transaction'
});
