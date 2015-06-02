define('views/transaction', [
    'ember',
    'text!templates/transaction.html'
], function(Ember, tpl) {
    "use strict";

    var TransactionView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return TransactionView;
});
