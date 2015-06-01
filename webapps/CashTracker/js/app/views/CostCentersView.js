define('views/CostCentersView', [
    'backbone',
    'underscore',
    'text!templates/costcenters.html'
], function(Backbone, _, tpl) {
    "use strict";

    var CostCentersView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.template = _.template(tpl);
        },

        render: function() {
            $(this.el).html(this.template);
            return this;
        }
    });

    return CostCentersView;
});
