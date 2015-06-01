define('views/TransactionsView', [
    'ember',
    'text!templates/transactions.html',
    'controllers/TransactionsController'
], function(Ember, tpl, TransactionsController) {
    "use strict";

    var TransactionsView = Ember.View.extend({
        controller: TransactionsController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return TransactionsView;
});
