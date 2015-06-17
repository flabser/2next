CT.TransactionsController = Ember.ArrayController.extend({
    queryParams: ['offset', 'limit', 'order_by'],

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
