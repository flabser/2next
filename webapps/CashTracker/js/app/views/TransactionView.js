define('views/TransactionView', [
    'backbone',
    'underscore',
    'text!templates/transaction.html'
], function(Backbone, _, tpl) {
    "use strict";

    var TransactionView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return TransactionView;
});
