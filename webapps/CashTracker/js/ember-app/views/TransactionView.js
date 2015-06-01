define('views/TransactionView', [
    'ember',
    'text!templates/transaction.html',
    'controllers/TransactionController'
], function(Ember, tpl, TransactionController) {
    "use strict";

    var TransactionView = Ember.View.extend({
        controller: TransactionController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return TransactionView;
});
