define('views/transactions', [
    'ember',
    'text!templates/transactions.html'
], function(Ember, tpl) {
    "use strict";

    var TransactionsView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return TransactionsView;
});
