define('views/TransactionsView', [
    'backbone',
    'underscore',
    'text!templates/transactions.html'
], function(Backbone, _, tpl) {
    "use strict";

    var TransactionsView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return TransactionsView;
});
