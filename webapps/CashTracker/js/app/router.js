define('CashTrackerRouter', ['backbone', 'LayoutView'], function(Backbone, LayoutView) {

    var CashTrackerRouter = Backbone.Router.extend({
        initialize: function() {
            LayoutView.render();
        },

        routes: (function() {
            return {
                '!': 'main',
                '!operations': 'operations',
                '!operations/:id': 'operations',
                '!costcenters': 'costcenters',
            }
        })(),

        main: function() {
            LayoutView.loadContent('main');
        },

        operations: function(id) {
            LayoutView.loadContent(id ? 'cash ' + id : 'operations');
        },

        costcenters: function() {
            LayoutView.loadContent('costcenters');
        }
    });

    return CashTrackerRouter;
});
