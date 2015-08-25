import Em from 'ember';

export default Em.Controller.extend({
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

    isTransfer: Em.computed('transaction.transactionType', function() {
        return this.get('transaction.transactionType') === 'T';
    })
});
