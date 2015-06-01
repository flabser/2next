define('views/CostCenterView', [
    'backbone',
    'underscore',
    'text!templates/costcenter.html'
], function(Backbone, _, tpl) {
    "use strict";

    var CostCenterView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return CostCenterView;
});
