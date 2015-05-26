define(['backbone'], function(Backbone) {

    var CostCenterView = Backbone.View.extend({
        className: 'layout-content',
        initialize: function() {
            this.title = 'cost center view';
        },
        render: function() {
            this.$el.html(this.title).show();
            return this;
        }
    });

    return CostCenterView;
});
