define('views/ActionBarView', [
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {

    var ActionBarView = Backbone.View.extend({
        el: '<div class="action-bar"></div>',

        initialize: function() {
            this.render('ActionBarView init');
        },

        render: function(actions) {

            for (var action in actions) {
                console.log(action.id);
            };

            this.$el.html(value);

            return this;
        }
    });

    return ActionBarView;
});
