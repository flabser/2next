define('views/CategoryView', [
    'backbone',
    'underscore',
    'text!templates/category.html'
], function(Backbone, _, tpl) {
    "use strict";

    var CategoryView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return CategoryView;
});
