define('views/transaction', [
    'ember',
    'templates/transaction'
], function(Ember, tpl) {
    "use strict";

    var TransactionView = Ember.View.extend({
        templateName: 'templates/transaction',
    });

    return TransactionView;
});
