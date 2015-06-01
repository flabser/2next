define('views/AccountsView', [
    'backbone',
    'underscore',
    'text!templates/accounts.html'
], function(Backbone, _, tpl) {
    "use strict";

    var AccountsView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return AccountsView;
});
