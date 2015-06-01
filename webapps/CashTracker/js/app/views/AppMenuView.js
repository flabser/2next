define('views/AppMenuView', ['backbone', 'handlebars'], function(Backbone, Handlebars) {

    var AppMenuView = Backbone.View.extend({
        el: '#nav-app',

        template: Handlebars.compile($('#tpl_app_menu').html()),

        initialize: function() {
            Handlebars.registerPartial('tpl_app_menu_item', Handlebars.compile($('#tpl_app_menu_item').html()));

            this.menuTree = [{
                title: 'Operations',
                items: [{
                    title: 'all',
                    url: '#!transactions'
                }, {
                    title: 'cash1',
                    url: '#!transaction/1'
                }, {
                    title: 'cash2',
                    url: '#!transaction/2'
                }]
            }, {
                title: 'Users',
                items: [{
                    title: 'user1',
                    url: '#!user1'
                }, {
                    title: 'user2',
                    url: '#!user2'
                }]
            }, {
                title: 'Property',
                items: [{
                    title: 'Accounts',
                    url: '#!accounts'
                }, {
                    title: 'Categories',
                    url: '#!categories'
                }, {
                    title: 'Cost center',
                    url: '#!costcenters'
                }]
            }];
        },

        events: {
            'click [data-role="side-tree-toggle"]': 'sideTreeToggle'
        },

        sideTreeToggle: function(e) {
            $(e.target).parent().toggleClass('side-tree-collapse');
        },

        render: function() {
            this.$el.html(this.template({
                items: this.menuTree
            }));
            return this;
        }
    });

    return new AppMenuView();
});
