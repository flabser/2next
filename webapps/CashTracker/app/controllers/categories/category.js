import Em from 'ember';

export default Em.Controller.extend({
    category: Em.computed.alias('model'),

    categories: function() {
        return this.store.findAll('category');
    }.property(),

    transactionTypes: [{
        "code": 1,
        "name": "Expense"
    }, {
        "code": 2,
        "name": "Income"
    }, {
        "code": 3,
        "name": "Transfer"
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
