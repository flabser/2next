define('views/UserView', [
    'backbone',
    'underscore',
    'text!templates/user.html'
], function(Backbone, _, tpl) {
    "use strict";

    var UserView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return UserView;
});
