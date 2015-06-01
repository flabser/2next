define('views/AccountView', [
    'backbone',
    'underscore',
    'text!templates/account.html'
], function(Backbone, _, tpl) {
    "use strict";

    var AccountView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return AccountView;
});
