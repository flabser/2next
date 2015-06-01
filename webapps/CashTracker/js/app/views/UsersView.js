define('views/UsersView', [
    'backbone',
    'underscore',
    'text!templates/users.html'
], function(Backbone, _, tpl) {
    "use strict";

    var UsersView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return UsersView;
});
