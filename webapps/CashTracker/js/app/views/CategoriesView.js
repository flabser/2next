define('views/CategoriesView', [
    'backbone',
    'underscore',
    'text!templates/categories.html'
], function(Backbone, _, tpl) {
    "use strict";

    var CategoriesView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return CategoriesView;
});
