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
            var newTransaction = this.store.createRecord('transaction', {
                author: this.get('author'),
                regDate: this.get('regDate'),
                date: this.get('date'),
                endDate: this.get('endDate'),
                parentCategory: this.get('parentCategory'),
                category: this.get('category'),
                account: this.get('account'),
                costCenter: this.get('costCenter'),
                amount: this.get('amount'),
                repeat: this.get('repeat'),
                every: this.get('every'),
                repeatStep: this.get('repeatStep'),
                basis: this.get('basis'),
                observers: this.get('observers'),
                comment: this.get('comment')
            });
            newTransaction.save();
        }
    }
});

CT.TransactionsNewRoute = Ember.Route.extend({
    templateName: 'transaction'
});
