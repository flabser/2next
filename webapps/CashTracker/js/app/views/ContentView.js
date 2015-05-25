define('ContentView', [
    'jquery',
    'backbone',
    'underscore',
    'LayoutView'
], function($, Backbone, _, LayoutView) {

    var ContentView = Backbone.View.extend({
        el: '#content',

        initialize: function() {
            this.render('content init');
        },

        setLayoutView: function(lv) {
            this.layoutView = lv;
        },

        load: function(uri) {
            this.render(uri);
        },

        search: function(el) {
            this.render(el);
        },

        render: function(value) {
            this.$el.html(value);
            if (typeof $.fn.tabs !== 'undefined') {
                $('#tabs').tabs();
            }
            this.layoutView && this.layoutView.hideOpenedNav();
            return this;
        }
    });

    return new ContentView();
});
