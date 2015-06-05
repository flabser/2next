define('views/transactions', [
    'ember',
    'templates/transactions'
], function(Ember, tpl) {
    "use strict";

    var TransactionsView = Ember.View.extend({
        templateName: 'templates/transactions',
    });

    return TransactionsView;
});
