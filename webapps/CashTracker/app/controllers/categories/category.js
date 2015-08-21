import Em from 'ember';

export default Em.Controller.extend({
    category: Em.computed.alias('model'),

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

    actions: {
        save: function() {
            let _this = this;
            let model = this.get('category');
            model.save().then(function() {
                _this.transitionToRoute('categories');
            });
        }
    }
});
